/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import java.util.Date;
import java.util.List;

import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.meta.UserCoupon;

/**
 * UserCouponService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public interface UserCouponService {
	
	/**
	 * 获取用户的优惠券列表
	 * @param userId
	 * @param isValid
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<CouponDTO> getUserCouponListByState(long userId, int state, int limit, int offset);
	
	/**
	 * 获取用户的优惠券总数
	 * @param userId
	 * @param isValid
	 * @return
	 */
	int getUserCouponCount(long userId, int state);

	/**
	 * 添加一个用户优惠券
	 * @param userCoupon
	 * @return
	 */
	UserCoupon addUserCoupon(UserCoupon userCoupon);
	
	/**
	 * 获取用户的优惠券
	 * @param userId
	 * @param userCouponId
	 * @param isValid
	 * @return
	 */
	UserCoupon getUserCoupon(long userId, long userCouponId, int state, boolean isValid);
	
	/**
	 * 
	 * @param userID
	 * @param userCouponId
	 * @param state
	 * @param isValid
	 * @return
	 */
	CouponDTO getUserCouponDTO(long userID, long userCouponId, int state, boolean isValid);
	
	/**
	 * 根据id获取用户优惠券
	 * @param userId
	 * @param userCouponId
	 * @param state
	 * @param isValid
	 * @return
	 */
	UserCoupon getUserCouponById(long userId, long userCouponId);
	
	/**
	 * 更新用户的优惠券信息
	 * @param userCoupon
	 * @return
	 */
	boolean updateUserCoupon(UserCoupon userCoupon);
	
	/**
	 * 回收用户的优惠券，在下单失败，取消订单时调用
	 * @param userId
	 * @param couponCode
	 * @param isUse 是否是使用状态回收，使用的修改为可使用，返券的改为已经失效
	 * @return
	 */
	boolean recycleUserCoupon(UserCoupon userCoupon, boolean isUse);

	/**
	 * mobile端获取用户优惠券
	 * @param userId
	 * @param timestamp
	 * @param count
	 * @return
	 */
	List<CouponDTO> getUserCouponList(long userId, long timestamp, int count);
	
	/**
	 * 根据code获取总数
	 * @param couponCode
	 * @return
	 */
	int getUserCouponCountByCode(String couponCode);
	
	/**
	 * 退款优惠券处理
	 * @param userId 用户id
	 * @param orderId 订单id
	 * @param isReturn 是否将使用的优惠券返给用户
	 * @return
	 */
	boolean refundCompensateUserCoupon(long userId, long orderId, String couponCode);
	
	/**
	 * 根据状态获取用户的优惠券
	 * @param userId
	 * @param couponCode
	 * @param state
	 * @return
	 */
	UserCoupon getUserCouponsByState(long userId, long userCouponId, int state, Boolean isValid);

	/**
	 * 获取同一个code的总数
	 * @param userId
	 * @param couponCode
	 * @return
	 */
	int getUserCouponCountByCode(long userId, String couponCode);
	
	/**
	 * 获取同一个code的总数
	 * @param userId
	 * @param couponCode
	 * @return
	 */
	int getUserCouponCountByCode(long userId, String couponCode, Date startDate, Date endDate);
	
	/**
	 * 删除用户优惠券
	 * @param id
	 * @param couponCode
	 * @return
	 */
	boolean deleteUserCoupon(long id, String couponCode);
	
	
	int getUserCouponCountBycreateTime(long userId, long time);
	
	/**
	 * 绑定优惠券，根据fixday固定优惠券的有效期,fixDay = 0,去coupon的有效区间
	 * @param userId
	 * @param couponCode
	 * @param fixDay 有效天数
	 * @param useCouponValidity 使用coupon的有效期，只有在fixDay=0时生效， true和coupon有效期一致，false取coupon的有效区间
	 * @return
	 */
	boolean bindUserCouponWithFixDay(long userId, String couponCode, int fixDay, boolean useCouponValidity);
	
	/**
	 * 绑定优惠券，根据fixday固定优惠券的有效期,fixDay = 0,去coupon的有效区间
	 * @param userId
	 * @param couponCodeList
	 * @param fixDay 有效天数
	 * @param useCouponValidity 使用coupon的有效期，只有在fixDay=0时生效， true和coupon有效期一致，false取coupon的有效区间
	 * @return
	 */
	boolean batchBindUserCouponWithFixDay(long userId, List<String> couponCodeList, int fixDay, boolean useCouponValidity);
	
	/**
	 * 按code更新已经绑定的列表
	 * @param couponCode
	 * @return
	 */
	public boolean batchUpdateBindUserCouponByCode(String couponCode);

	/**
	 * 按code删除已经绑定的列表
	 * @param couponCode
	 * @return
	 */
	public boolean batchDeleteBindUserCouponByCode(String couponCode);
}
