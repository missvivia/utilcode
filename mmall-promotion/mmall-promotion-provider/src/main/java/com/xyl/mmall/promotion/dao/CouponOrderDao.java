/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.meta.CouponOrder;

/**
 * CouponOrderDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public interface CouponOrderDao {

	CouponOrder addCouponOrder(CouponOrder couponOrder);

	List<CouponOrder> getCouponOrderByCodeOfUseType(long userId, String couponCode, CouponOrderType couponOrderType);

	boolean updateCouponOrder(CouponOrder couponOrder);
	
	List<CouponOrder> getObjectByOrderAndUserId(long orderId, long userId);

	Map<CouponOrderType, List<CouponOrder>> getMapByOrderId(long userId, long orderId);

	List<CouponOrder> getCouponOrderByUserId(long userId);

	List<CouponOrder> getCouponOrderByUserAndCouponId(long userId, long usercouponId);
	
	/**
	 * 根据orderIds和用户Id获取优惠券交易列表
	 * @param userId
	 * @param orderIds
	 * @return
	 */
	List<CouponOrder> getCouponOrdersByUserIdAndOrderIds(long userId,List<Long>orderIds);
}
