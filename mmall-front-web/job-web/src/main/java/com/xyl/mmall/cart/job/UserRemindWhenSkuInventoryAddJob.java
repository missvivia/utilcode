package com.xyl.mmall.cart.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.CartConstant;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.base.RDBResult;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.mainsite.facade.MessagePushFacade;
import com.xyl.mmall.mainsite.facade.UserProfileFacade;
import com.xyl.mmall.mainsite.vo.MainSiteUserVO;
import com.xyl.mmall.task.enums.PlatformType;
import com.xyl.mmall.util.CartRDBOperUtil;
import com.xyl.mmall.util.ResourceTextUtil;

/**
 * 处理sku库存增加时，提醒用户的逻辑
 * 
 * @author hzzhaozhenzuo
 *
 */
@JobPath("/inventory/add/remind")
@Service
public class UserRemindWhenSkuInventoryAddJob extends BaseJob {

	@Autowired
	private CartRDBOperUtil cartRDBOperUtil;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private CartFacade cartFacade;

	@Autowired
	private MessagePushFacade messagePushFacade;
	
	@Autowired
	private UserProfileFacade userProfileFacade;

	private static final Logger logger = LoggerFactory.getLogger(UserRemindWhenSkuInventoryAddJob.class);

	private static final ResourceBundle pushResourceBundle = ResourceTextUtil.getResourceBundleByName("content.push");

	@Override
	public boolean execute(JobParam param) {
		return this.process(param);
	}

	private boolean process(JobParam param) {
		List<AreaDTO> areaList = businessFacade.getAreaList();
		if (areaList == null || areaList.isEmpty()) {
			logger.warn("no area round");
			return true;
		}
		
		boolean allSuccessFlag=true;
		for (AreaDTO areaDTO : areaList) {
			Map<byte[], byte[]> dataMap = this.getUsersRemindMapByArea((int) areaDTO.getId());
			if (dataMap == null || dataMap.isEmpty()) {
				continue;
			}
			if(!this.processOneArea((int) areaDTO.getId(), dataMap)){
				allSuccessFlag=false;
			}
		}
		return allSuccessFlag;
	}

	@SuppressWarnings("unchecked")
	private boolean processOneArea(int areaId, Map<byte[], byte[]> dataMap) {
		Map<Long, Map<String, String>> usersMapToProcess = new HashMap<Long, Map<String, String>>();
		List<Long> skuIdList = new ArrayList<Long>();
		for (Entry<byte[], byte[]> entry : dataMap.entrySet()) {
			long skuId = Long.valueOf(new String(entry.getKey()));
			if (entry.getValue() == null) {
				continue;
			}
			Map<String, String> usersMap = JSON.parseObject(new String(entry.getValue()), LinkedHashMap.class);
			if (usersMap == null || usersMap.isEmpty()) {
				continue;
			}
			usersMapToProcess.put(skuId, usersMap);
			skuIdList.add(skuId);
		}

		if (skuIdList.isEmpty()) {
			return true;
		}

		// get inventory
		Map<Long, Integer> inventoryMap = cartFacade.getInventoryCount(skuIdList);
		if (inventoryMap == null || inventoryMap.isEmpty()) {
			return true;
		}

		boolean allSuccessFlag = true;
		// begin process
		for (Entry<Long, Integer> entry : inventoryMap.entrySet()) {
			Map<String, String> usersMapBySkuId = usersMapToProcess.get(entry.getKey());
			if (usersMapBySkuId == null || usersMapBySkuId.isEmpty()) {
				continue;
			}
			List<Long> usersRemindResult = this.getUsersRemindList(areaId, entry.getKey(), usersMapBySkuId,
					entry.getValue());
			if (usersRemindResult == null || usersRemindResult.isEmpty()) {
				continue;
			}

			// add sku to cart of users in usersRemindResult list
			if (!this.addSkuToCartAndSendPushMsg(areaId, entry.getKey(), usersRemindResult)) {
				allSuccessFlag = false;
			}
		}
		return allSuccessFlag;
	}

	private boolean addSkuToCartAndSendPushMsg(int areaId, long skuId, List<Long> userIdList) {
		if (userIdList == null || userIdList.isEmpty()) {
			return true;
		}

		boolean allSuccessFlag = true;
		for (long userId : userIdList) {
			logger.info("====try to put sku to user,userId:"+userId+",skuId:"+skuId);
			// add to user cart
			if (cartFacade.addItemToCartAndUpdateTime(userId, areaId, skuId, 1) > 0) {
				// push msg
				logger.info("====success put sku to user,userId:"+userId+",skuId:"+skuId);
//				String userKey = userId + CodeInfoUtil.VERTICAL_PREFIX + areaId;
				String userKey = String.valueOf(userId);
				String content = pushResourceBundle.getString("cart.inventory.remind.user");
				boolean sendFlag = messagePushFacade.pushMessageForPrivate(userKey, PlatformType.ALL_PLATFORM, content);
				logger.info("====success send msg to user,userId:"+userId+",skuId:"+skuId);
				if (!sendFlag) {
					logger.error("fail send push msg to userId:" + userId);
					allSuccessFlag = false;
				}
				
				//send sms msg
				if(!this.sendSms(userId, content)){
					logger.error("fail send sms for Userid:"+userId+",skuid:"+skuId);
				}
			}else{
				logger.error("====fail put sku to the cart of user,userId:"+userId+",skuId:"+skuId);
			}
		}
		return allSuccessFlag;
	}
	
	private boolean sendSms(long userId,String content){
		MainSiteUserVO user=userProfileFacade.getUserProfile(userId);
		if(user==null || StringUtils.isEmpty(user.getUserProfile().getMobile())){
			return true;
		}
		
		logger.info("try to send sms for userid:"+userId+",phone:"+user.getUserProfile().getMobile());
		return messagePushFacade.sendSms(user.getUserProfile().getMobile(), content);
	}

	private List<Long> getUsersRemindList(int areaId, long skuId, Map<String, String> usersMap, int num) {
		List<Long> userIdList = new ArrayList<Long>();
		if (usersMap == null || usersMap.isEmpty()) {
			return userIdList;
		}
		
		List<Object> removeList=new ArrayList<Object>();

		// get the first record
		int count = 0;
		for (Entry<String, String> entry : usersMap.entrySet()) {
			try {
				if (++count > num) {
					break;
				}
				userIdList.add(Long.valueOf(entry.getKey()));
				
				// add to removeList
				removeList.add(entry.getKey());
			} catch (NumberFormatException e) {
				logger.error("numbeFormat exception,userid key:"+entry.getKey(),e);
			}
		}
		
		//remove the ele
		if(removeList!=null && removeList.size()>0){
			for(Object key:removeList){
				usersMap.remove(key);
			}
		}

		// remove from cache
		boolean removeFlag = cartRDBOperUtil.putOrReplaceForRDBOfMap(this.getOutKeyOfRemindWhenStorage(areaId),
				this.getFieldKeyOfRemindWhenStorage(skuId), JSON.toJSONString(usersMap));
		if (!removeFlag) {
			return null;
		}
		return userIdList;
	}

	private Map<byte[], byte[]> getUsersRemindMapByArea(int areaId) {
		String outKey = this.getOutKeyOfRemindWhenStorage(areaId);
		RDBResult rdbResult = cartRDBOperUtil.getAllFromRDBOfMap(outKey);
		if (!rdbResult.isSearchSuccess()) {
			logger.warn("rdb search error when do UserRemind job for sku");
			return null;
		}
		return rdbResult.getResult();
	}

	private String getOutKeyOfRemindWhenStorage(int provinceId) {
		return (CartConstant.NKV_CART_REMIND_WHEN_STORAGE + "-" + provinceId);
	}

	private String getFieldKeyOfRemindWhenStorage(long skuId) {
		return Long.toString(skuId);
	}
}
