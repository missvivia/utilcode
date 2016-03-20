package com.xyl.mmall.cart.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyl.mmall.cart.clean.CartCleanCodeInfo;
import com.xyl.mmall.cart.clean.CartCleanKeyUtil;
import com.xyl.mmall.cart.clean.CartRDBOperUtil;
import com.xyl.mmall.cart.clean.RDBResult;
import com.xyl.mmall.cart.clean.meta.CartCleanAreaPoint;
import com.xyl.mmall.cart.dao.CartCleanAreaPointDao;
import com.xyl.mmall.cart.dao.CartDao;
import com.xyl.mmall.cart.dao.impl.nkv.CartCleanConfigLoad;
import com.xyl.mmall.cart.dto.CartItemDTO;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.cart.util.CollectionUtil;
import com.xyl.mmall.constant.NkvConstant;

/**
 * 购物车清除实现类 用于内部，不对外公开 职责目前如下： 1.添加或更新用户购物车的有效时间到cache中
 * 2.为job公开一个方法，实现清理购物车中超时的sku
 * 
 * @author hzzhaozhenzuo
 *
 */
@Component
public class CartCleanCacheOperImpl implements CartCleanCacheOperInf {

	@Autowired
	private CartCleanConfigLoad cartCleanConfigLoad;

	@Autowired
	private CartRDBOperUtil cartRDBOperUtil;

	@Autowired
	private CartDao cartDao;

	private static Logger logger = LoggerFactory.getLogger(CartCleanCacheOperImpl.class);

	@Autowired
	private CartCleanAreaPointDao cartCleanAreaPointDao;

	@Autowired
	private CartService cartService;

	/**
	 * 添加或更新用户购物车的有效时间到cache中 目前需要做以下步聚：
	 * 1.添加或更新以用户id+区域为一个key，value是当前对应购物车的有效时间的cache元素
	 * 2.将此key/value值对存放到【清除区域】中 (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cart.service.impl.CartCleanCacheOperInf#addCartUpdateTimeToCache(long,
	 *      long, java.util.Date)
	 */
	@Override
	public boolean addCartUpdateTimeToCache(long userId, int areaId, Date updateTime) {

		// 1.将此key/value值对存放到【清除区域】中
		boolean flag = this.putToCleanAreaOfCart(userId, areaId, updateTime.getTime(), NkvConstant.DISTRIBUTE_FOR_CART_CLEAN);
		logger.info("flag:" + flag);
		return flag;
	}

	/**
	 * 将用户对应的购物车有效时间放入【清除区域】中
	 * 前缀为:NKV_CART_CLEAN_AREA_TIME_PREFIX，如果是按地理区域分成的话，还需要加上地理区域id
	 * 之后分成n个区域的话，分别在后面加上相应的区域下标 如：NKV_CART_CLEAN_AREA_TIME_PREFIX|0 表示第一个区域块
	 * 
	 * @param userId
	 * @param areaId
	 * @param updateTime
	 * @param distributeProcess
	 */
	private boolean putToCleanAreaOfCart(long userId, int areaId, long updateTime, boolean distributeProcess) {
		Integer curPos = this.getCurPosition(areaId);
		if (curPos == null) {
			curPos = CartCleanCodeInfo.AREA_CLEAN_NO_PROCESSING;
		}

		int areaOfCache = curPos;

		areaOfCache = areaOfCache - 1;
		if (areaOfCache < 0) {
			areaOfCache = cartCleanConfigLoad.getAreaNumberOfCache() - 1;
		}
		
		
		//此处对每个大区域，再根据具体规则进行切分，防止每个map太大
		String areaKey = CartCleanKeyUtil.getKeyOfCleanAreaForCar(areaOfCache, areaId, distributeProcess,userId);
		
		String cartFieldInMap = CartCleanKeyUtil.getOrginalCartUpdateTimeKey(userId, areaId);
		return cartRDBOperUtil.putOrReplaceForRDBOfMap(areaKey, cartFieldInMap, updateTime);
	}

	@Override
	public synchronized boolean cleanOverTimeCartForJob(int areaIdPassedByJob, int posProcessedByCurJob) {
		int curPos = this.getCurPosition(areaIdPassedByJob);
		if (curPos != posProcessedByCurJob) {
			logger.error("the position modifed by another thread,areaId:" + areaIdPassedByJob + ",distributeProcess:"
					+ NkvConstant.DISTRIBUTE_FOR_CART_CLEAN + ",posProcessedByCurJob:" + posProcessedByCurJob);
			return false;
		}
		String[] areaKeys = CartCleanKeyUtil.getKeyOfCleanAreasToClean(posProcessedByCurJob, areaIdPassedByJob,
				NkvConstant.DISTRIBUTE_FOR_CART_CLEAN);
		
		if(areaKeys==null || areaKeys.length<=0){
			logger.error("areaKeys is null,areaIdPassedByJob:"+areaIdPassedByJob+",posProcessedByCurJob:"+posProcessedByCurJob);
			return false;
		}
		
		boolean allSuccessFlag=true;
		
		for(String splitKey:areaKeys){
			if(!this.processOneSplitOfArea(splitKey, areaIdPassedByJob, posProcessedByCurJob)){
				allSuccessFlag=false;
			}
		}

		return allSuccessFlag;
	}
	
	private boolean processOneSplitOfArea(String splitKey,int areaIdPassedByJob, int posProcessedByCurJob){
		RDBResult result = cartRDBOperUtil.getAllFromRDBOfMap(splitKey);
		if (!result.isSearchSuccess()) {
			logger.error("search rdb cache error,areaId:" + areaIdPassedByJob + ",distributeProcess:"
					+ NkvConstant.DISTRIBUTE_FOR_CART_CLEAN + ",posProcessedByCurJob:" + posProcessedByCurJob);
			return false;
		}

		if (result.getResult() == null || result.getResult().isEmpty()) {
			logger.info("no data to clean,areaId:" + areaIdPassedByJob + ",distributeProcess:"
					+ NkvConstant.DISTRIBUTE_FOR_CART_CLEAN + ",posProcessedByCurJob:" + posProcessedByCurJob);
			return true;
		}

		// begin clean
		Map<byte[], byte[]> dataMap = result.getResult();
		boolean allSuccessFlag = true;
		for (Entry<byte[], byte[]> entry : dataMap.entrySet()) {
			String cartKey = new String(entry.getKey());
			String updateTimeStr = new String(entry.getValue());
			long updateTime = Long.valueOf(updateTimeStr);

			long userId = CartCleanKeyUtil.getUserIdFromOginalCartUpdateTimeKey(cartKey);
			int areaIdFromFieldKey=CartCleanKeyUtil.getAreaIdFromOginalCartUpdateTimeKey(cartKey);

			// 用户原有购物车有效时间
			long originalUpdateTime = cartDao.getCartTime(userId, areaIdFromFieldKey);

			// 判断用户购物车更新时间是否等于清除区域中保存的更新时间
			if (!isEqWithOriginalCartUpdateTime(updateTime, originalUpdateTime)) {
				logger.info("not eq with orginal cart updateTime,userId:" + userId + ",areaId:" + areaIdFromFieldKey);
				// 移除这个无效元素
				if (!this.cleanCartCleanAreaByAreaAndFieldKey(splitKey, cartKey)) {
					logger.error("cannot remove the field from clean area userId:" + userId + ",areaId:" + areaIdFromFieldKey
							+ ",splitKey:" + splitKey + ",cartKey:" + cartKey);
				}
				continue;
			}

			// 是否过期
			if (!this.isExpireForCartUpdateTime(updateTime)) {
				continue;
			}

			if (!this.cleanOverTimeCartFromAreaCache(splitKey, cartKey, userId, areaIdFromFieldKey)) {
				logger.error("can not remove the overTime cartItem" + userId + ",areaId:" + areaIdFromFieldKey);
				allSuccessFlag = false;
			}
		}
		return allSuccessFlag;
	}

	private boolean isExpireForCartUpdateTime(long updateTime) {
		int validateTime = cartCleanConfigLoad.getMinuteOfCleanCart();
		// boolean
		// res=(System.currentTimeMillis()-updateTime)>(validateTime*60*1000);
		boolean res = (System.currentTimeMillis() - updateTime) > (validateTime * 60 * 1000);
		return res;
	}

	private boolean isEqWithOriginalCartUpdateTime(long updateTimeInAreaCache, long originalUpdateTime) {
		return originalUpdateTime == updateTimeInAreaCache;
	}

	/**
	 * 将valid有效的购物车内容移到超时购物车中
	 * 
	 * @param cartKey
	 *            这里的key为:【清除区域】某个桶中，对应存储某个用户购物车的更新时间的key值。
	 * @param userId
	 *            要被清除购物车的所属用户
	 * @return
	 */
	private boolean cleanOverTimeCartFromAreaCache(String areaKey, String cartField, long userId, int areaId) {
		logger.info("=========try to remove the overTime cartItem,areaKey:" + areaKey + "cartKey:" + cartField
				+ ",userId:" + userId + ",areaId:" + areaId);

		// 获取有效购物车sku列表
		List<CartItemDTO> validCartItemList = cartDao.getCart(NkvConstant.NKV_CART_VALID, userId, areaId);
		if (validCartItemList == null || validCartItemList.isEmpty()) {
			logger.info("no valid cartItems found," + cartField + ",userId:" + userId + ",areaId:" + areaId);
			return true;
		}

		// 将有效购物车sku放入到超时购物车队列
		int addToOverTimeNum = cartDao.addItems(NkvConstant.NKV_CART_OVERTIME, userId, areaId,
				CollectionUtil.ListToSkuMap(validCartItemList));
		boolean addToOverTimeCartFlag = addToOverTimeNum > 0 ? true : false;
		if (!addToOverTimeCartFlag) {
			logger.error("cannot add the valid cartItems to overTime cart,areaKey:" + areaKey + "cartKey:" + cartField
					+ ",userId:" + userId);
			return false;
		}

		// 将有效购物车sku移除
		boolean removeValidFlag = cartService.deleteValidCart(userId, areaId);
		if (!removeValidFlag) {
			logger.error("cannot remove the valid cartItems,,areaKey:" + areaKey + "cartKey:" + cartField + ",userId:"
					+ userId);
			return false;
		}

		// 将清除区域中对应的key移除
		boolean removeFlag = this.cleanCartCleanAreaByAreaAndFieldKey(areaKey, cartField);
		if (!removeFlag) {
			logger.error("cannot remove the element from cart clean area,areaKey:" + areaKey + "cartKey:" + cartField
					+ ",userId:" + userId);
		}
		return true;
	}

	/**
	 * 移除清除区域中对应cartField的元素
	 * 
	 * @param areaKey 要移除的key
	 * @param cartField
	 * @return
	 */
	private boolean cleanCartCleanAreaByAreaAndFieldKey(String areaKey, String cartField) {
		return cartRDBOperUtil.delFieldAndValueForRDBOfMap(areaKey, cartField);
	}

	/**
	 * 职责： 将任务指针设置为当前传入的posProcessedByCurJob 注意：默认要求初始化一条记录：point为-1
	 * 
	 * 区域index位置从0开始，到 number-1 number为区域总数
	 * 
	 * 这部分存储结构为一个hash 首先会有一个外层key为：NKV_CART_CLEAN_POINT 根据这个key可以得到hash结构
	 * hash结构：里面存储两个元素,第一个元素存储对应pos指针位置,第二个元素存储对应是否已经将此区域清除成功 (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cart.service.impl.CartCleanCacheOperInf#pointToNext(int,
	 *      boolean)
	 */
	public synchronized boolean setUpPoint(int areaId,int posProcessedByCurJob, int oldPoint) {
		String pointKey = CartCleanKeyUtil.getPointKey(areaId, NkvConstant.DISTRIBUTE_FOR_CART_CLEAN);
		return cartCleanAreaPointDao.updatePointAndStatus(pointKey, posProcessedByCurJob,
				CartCleanCodeInfo.AREA_CLEAN_PROCESSING, oldPoint);
	}

	/**
	 * 得到之后处理过的位置指针及当前job需要处理的区域位置指针 只是读取，不设置值
	 * 
	 * @param areaId
	 * @return arr[0]为上一次处理的区域位置,arr[1]为当前需要处理的位置
	 */
	public synchronized int[] getPositionShouldProcessedByCurrentJob(int areaId) {
		int[] arr = new int[2];

		// 上一次处理过的位置
		arr[0] = this.getCurPosition(areaId);

		// 将要处理的位置
		int posNext = arr[0] + 1;
		if (posNext > cartCleanConfigLoad.getAreaNumberOfCache() - 1) {
			posNext = 0;
		}
		arr[1] = posNext;
		return arr;
	}

	/**
	 * 获取指针当前位置
	 * 
	 * @param areaId
	 * @return null则代表服务器查询出错或者是未初始化指针
	 */
	private Integer getCurPosition(int areaId) {
		String pointKey = CartCleanKeyUtil.getPointKey(areaId, NkvConstant.DISTRIBUTE_FOR_CART_CLEAN);
		CartCleanAreaPoint point = cartCleanAreaPointDao.queryPointByDistributeKey(pointKey);
		if (point == null) {
			logger.error("no point record found,pointKey:" + pointKey + ",areaId:" + areaId + "distributeProcess:"
					+ NkvConstant.DISTRIBUTE_FOR_CART_CLEAN);
			throw new RuntimeException("please init the point record for cart area clean,pointKey:" + pointKey
					+ ",areaId:" + areaId + "distributeProcess:" + NkvConstant.DISTRIBUTE_FOR_CART_CLEAN);
		}
		return point.getPoint();
	}

	/**
	 * 将对应区域的处理状态设置为成功或失败 (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cart.service.impl.CartCleanCacheOperInf#pointFlagToSuccessOrFail(boolean,
	 *      int, boolean, int)
	 */
	@Override
	public boolean pointFlagToSuccessOrFail(boolean cleanSuccessFlag, int areaId, int posProcessedByCurJob, int oldPoint) {
		// get the old value
		String pointKey = CartCleanKeyUtil.getPointKey(areaId, NkvConstant.DISTRIBUTE_FOR_CART_CLEAN);
		int status = cleanSuccessFlag ? CartCleanCodeInfo.AREA_CLEAN_SUCCESS : CartCleanCodeInfo.AREA_CLEAN_FAIL;
		return cartCleanAreaPointDao.updatePointAndStatus(pointKey, posProcessedByCurJob, status, oldPoint);
	}


}
