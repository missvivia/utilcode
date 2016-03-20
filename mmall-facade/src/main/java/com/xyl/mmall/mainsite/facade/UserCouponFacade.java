/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mainsite.facade;

import java.util.List;

import com.xyl.mmall.mainsite.vo.CouponVO;

/**
 * UserCouponFacade.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */
public interface UserCouponFacade {
	/**
	 * 获取用户的优惠券列表
	 * @param userId
	 * @param isValid
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<CouponVO> getUserCouponList(long userId, int state, int limit, int offset);
	
	/**
	 * 获取用户的优惠券总数
	 * @param userId
	 * @param isValid
	 * @return
	 */
	int getUserCouponCount(long userId, int state);
	
	/**
	 * mobile端获取用户优惠券
	 * @param userId
	 * @param timestamp
	 * @param count
	 * @return
	 */
	List<CouponVO> getUserCouponList(long userId, long timestamp, int count);
	
	/**
	 * 绑定优惠券，根据fixday固定优惠券的有效期,fixDay = 0,去coupon的有效区间
	 * @param userId
	 * @param couponCode
	 * @param fixDay 有效天数
	 * @param useCouponValidity 使用coupon的有效期，只有在fixDay=0时生效， true和coupon有效期一致，false取coupon的有效区间
	 * @return
	 */
	boolean bindUserCouponWithFixDay(long userId, String couponCode, int fixDay, boolean useCouponValidity);
}
