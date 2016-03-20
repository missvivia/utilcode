/*
 * @(#) 2014-12-4
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * CouponLock.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-4
 * @since      1.0
 */
@AnnonOfClass(desc = "优惠券code锁", tableName = "Mmall_Promotion_CouponLock")
public class CouponLock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@AnnonOfField(desc="优惠券code", primary=true)
	private String couponCode;

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
}
