/*
 * @(#) 2015-1-23
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * ActivationRecord.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2015-1-23
 * @since 1.0
 */
@SuppressWarnings("serial")
@AnnonOfClass(desc = "活动领取记录", tableName = "Mmall_Promotion_ActivationRecord")
public class ActivationRecord implements Serializable {

	@AnnonOfField(primary = true, desc = "主键ID", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户ID", policy = true)
	private long userId;

	@AnnonOfField(desc = "是否免单", defa = "0")
	private boolean free;

	@AnnonOfField(desc = "优惠券", notNull = false, defa = "")
	private String couponCode;

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

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

}
