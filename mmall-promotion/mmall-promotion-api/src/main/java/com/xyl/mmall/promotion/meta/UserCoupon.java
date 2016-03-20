/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * UserCoupon.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@AnnonOfClass(desc = "用户优惠券", tableName = "Mmall_Promotion_UserCoupon", dbCreateTimeName = "CreateTime")
public class UserCoupon implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "用户ID", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户ID", policy = true)
	private long userId;

	@AnnonOfField(desc = "优惠券code", type = "varchar(100)")
	private String couponCode;

	@AnnonOfField(desc = "是否有效")
	private int state;

	@AnnonOfField(desc = "有效期开始时间")
	private long validStartTime;

	@AnnonOfField(desc = "有效期结束时间")
	private long validEndTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public long getValidStartTime() {
		return validStartTime;
	}

	public void setValidStartTime(long validStartTime) {
		this.validStartTime = validStartTime;
	}

	public long getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(long validEndTime) {
		this.validEndTime = validEndTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isValid() {
		long time = System.currentTimeMillis();
		if (time >= validStartTime && time < validEndTime) {
			return true;
		}
		return false;
	}

	public UserCoupon cloneObject() {
		UserCoupon uc = new UserCoupon();
		BeanUtils.copyProperties(this, uc);
		return uc;
	}
}
