/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import java.util.Date;
import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.promotion.meta.UserCoupon;

/**
 * UserCouponDao.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
public interface UserCouponDao extends AbstractDao<UserCoupon> {

	List<UserCoupon> getUserCouponListByState(long userId, int state, int limit, int offset);

	int getUserCouponCount(long userId, int state);
	
	int getUserCouponCountBycreateTime(long userId, long time);

	UserCoupon addUserCoupon(UserCoupon userCoupon);

	List<UserCoupon> getUserCouponList(long userId, long timestamp, int count);

	boolean updateUserCoupon(UserCoupon userCoupon);

	int getUserCouponCountByCode(String couponCode);

	UserCoupon getUserCouponsByState(long userId, long userCouponId, int state, Boolean isValid);

	int getUserCouponCountByCode(long userId, String couponCode, Date startDate, Date endDate);

	boolean deleteUserCoupon(long userId, String couponCode);
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public boolean batchUpdateBindUserCouponByCode(String couponCode);

	/**
	 * 
	 * @param code
	 * @return
	 */
	public boolean batchDeleteBindUserCouponByCode(String couponCode);
}
