/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.common.enums.OrderQueryType;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.saleschedule.dto.UserFavListDTO;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.task.dto.PushManagementDTO;
import com.xyl.mmall.task.enums.PushMessageType;
import com.xyl.mmall.task.meta.PushManagement;
import com.xyl.mmall.task.service.PushManagementService;
import com.xyl.mmall.task.service.PushService;
import com.xyl.mmall.task.service.PushTaskService;

/**
 * @author hzjiangww
 *
 */
@Facade
public class MobilePushManageFacadeImpl implements MobilePushManageFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	PushManagementService pushManagementService;
	@Resource
	PushService pushService;
	@Resource
	PushTaskService pushTaskService;
	@Resource
	OrderBriefService orderBriefService;
	@Resource
	BrandService brandService;
	
	@Override
	public List<UserFavListDTO> getUserFavListByBrandIdList(List<Long> brandIds,int limit, 
			int offset) {
		RetArg retArg = brandService.getUserFavListByBrandIdList(brandIds, 0, limit, offset);
		List<UserFavListDTO> userFavList =RetArgUtil.get(retArg, ArrayList.class); 
		return userFavList;
	}
	@Override
	public RetArg getPushConfigList(PushManagementDTO pushManagementAo, DDBParam param) {
		return pushManagementService.getPushConfigList(pushManagementAo,  param);
	}

	@Override
	public PushManagementDTO getPushConfigById(long id) {
		// TODO Auto-generated method stub
		return pushManagementService.getPushConfigById(id) ;
	}

	@Override
	public PushManagementDTO addPushConfig(PushManagement pushManagement) {
		// TODO Auto-generated method stub
		return pushManagementService.addPushConfig(pushManagement) ;
	}

	@Override
	public boolean deletePushConfigById(long id) {
		// TODO Auto-generated method stub
		return pushManagementService.deletePushConfigById(id);
	}

	@Override
	public boolean updatePushManagement(long id, PushManagementDTO pushManagementDTO) {
		// TODO Auto-generated method stub
		return pushManagementService.updatePushManagement(id, pushManagementDTO);
	}

	@Override
	public boolean pushRun(long start, long end) {
		// TODO Auto-generated method stub
		if(start == 0 || end ==0)
			return false;
		return pushTaskService.push(start, end);
	}

	@Override
	public boolean push(long userId, String alertTitle, String title, String message, String appUrl) {
		// TODO Auto-generated method stub
		return pushService.push(userId, alertTitle, title, message, appUrl);
	}

	@Override
	public boolean push(long userId, int type, String title, String message, long keyId) {
		// TODO Auto-generated method stub
		return pushService.push(userId, type, title, message, keyId);
	}
	
	@Override
	public boolean push(long userId, int type, String title, String message, long keyId,long areaId) {
		// TODO Auto-generated method stubea(long userId,int type,String title , String message, long keyId,long areaCode);
		return pushService.pushByArea(userId, type, title, message, keyId,areaId);
	}

	@Override
	public boolean pushForAll(long userId, int bizTypeId, long bizUniqueId,
			Map<String, Object> otherParamMap) {
		return pushService.pushForAll(userId, bizTypeId, bizUniqueId, otherParamMap);
	}

	
	private boolean doSend(long[] range,int offset){
		DDBParam param = DDBParam.genParam500();
		param.setLimit(500);
		param.setOffset(offset);
		logger.info("starttime:"+ range[0] + "offset:" + offset);
		RetArg retArg = orderBriefService.queryOrderFormBriefDTOListByStateWithMinOrderId(0, OrderQueryType.WAITING_PAY.getOrderStateArray(), range, param);
		List<OrderFormBriefDTO> orderFormBriefDTO = RetArgUtil.get(retArg, ArrayList.class);
		if(orderFormBriefDTO == null)
			return false;
		logger.info("return size:"+ orderFormBriefDTO.size());
		for(OrderFormBriefDTO dto: orderFormBriefDTO){
			 pushService.push(dto.getUserId(), PushMessageType.order_timeout, "", "", dto.getOrderId());
		}
		param = RetArgUtil.get(retArg, DDBParam.class);
		return param.isHasNext();
	}
	@Override
	public boolean pushOrderTimeOut(long lastStartTime, long startTime) {
		long alertTime = ConstValueOfOrder.MAX_PAY_TIME - 5*60*1000;
		long[] range =  new long[2];
		range[0] = lastStartTime - alertTime;
		range[1] = startTime - alertTime;
		try{
			int i = 0;
			while(doSend(range,i)){
				i = i + 500;
				if(i>10000){
					logger.warn("order number is too larger");
					break;
				}
			}
		
			return true;
		}catch(Exception e){
			logger.error(e.toString(),e);
		}
		
		
		return false;
	}

	


}
