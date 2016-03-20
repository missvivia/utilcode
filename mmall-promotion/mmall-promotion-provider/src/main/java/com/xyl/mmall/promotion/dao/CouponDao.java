/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import java.util.List;

import com.xyl.mmall.promotion.meta.Coupon;

/**
 * CouponDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public interface CouponDao {

	List<Coupon> getCouponList(List<Long> ids, int state, String qvalue, int limit, int offset);

	int getCouponCount(List<Long> ids, int state, String qvalue);

	Coupon addCoupon(Coupon coupon);

	boolean updateCoupon(Coupon coupon);

	Coupon getCouponByCode(String couponCode, boolean isValidOnly);

	Coupon getCouponById(long id, boolean isValidOnly);

	List<Coupon> getRandomCoupons(String rootCode, int count);

	int getObjectLockByCode(String couponCode);

	boolean deleteRamdomCoupon(String rootCode);
}
