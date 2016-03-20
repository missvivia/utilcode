/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.promotion.dto.CouponOrderDTO;
import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.meta.CouponOrder;

/**
 * CouponOrderService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public interface CouponOrderService {

	/**
	 * 添加具体的优惠券交易
	 * 
	 * @param orders
	 * @return
	 */
	boolean addCouponOrder(CouponOrder order);

	/**
	 * 回收订单上的优惠券(包括使用优惠券和返券)
	 * 
	 * @param userLock
	 * @param userId
	 * @param orderId
	 * @return
	 */
	boolean recycleCouponOrder(CouponOrder couponOrder);
	
	/**
	 * 批量回收订单上的优惠券(包括使用优惠券和返券)
	 * 
	 * @param userLock
	 * @param userId
	 * @param orderId
	 * @return
	 */
	boolean recycleCouponOrderList(List<? extends CouponOrder> couponOrders);

	/**
	 * 处理交易成功的返券
	 * 
	 * @param couponOrder
	 * @return 0: 失败<br>
	 *         1: 成功<br>
	 *         2: 没有需要添加的返券
	 */
	int doSuccOrderOfCouponReturn(List<CouponOrder> couponOrders);

	/**
	 * 优惠券交易状态
	 * 
	 * @param couponOrder
	 * @return
	 */
	boolean updateCouponOrder(CouponOrder couponOrder);

	/**
	 * 根据订单id查询订单上使用的优惠券信息
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	CouponOrder getCouponOrderByOrderIdOfUseType(long userId, long orderId);

	/**
	 * 读取订单上的优惠券信息(使用优惠券+返券)
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	Map<CouponOrderType, List<CouponOrder>> getMapByOrderId(long userId, long orderId);

	/**
	 * 读取订单上的优惠券信息(使用优惠券+返券)
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	List<CouponOrderDTO> getListByOrderId(long userId, long orderId);

	/**
	 * 查询用户优惠券订单使用情况
	 * 
	 * @param userId
	 * @param couponCode
	 * @return
	 */
	List<CouponOrder> getCouponOrderByCodeOfUseType(long userId, String couponCode, CouponOrderType couponOrderType);
	
	/**
	 * 获取用户优惠券交易列表
	 * @param userId
	 * @return
	 */
	List<CouponOrder> getCouponOrderByUserId(long userId);
	
	/**
	 * 获取用户的
	 * @param userId
	 * @param usercouponId
	 * @param couponOrderType
	 * @return
	 */
	List<CouponOrder> getCouponOrderByUserAndCouponId(long userId, long usercouponId);
	
	
	/**
	 * 根据orderIds和用户Id获取优惠券交易列表
	 * @param userId
	 * @param orderIds
	 * @return
	 */
	List<CouponOrder> getCouponOrdersByUserIdAndOrderIds(long userId,List<Long>orderIds);
	
	
}
