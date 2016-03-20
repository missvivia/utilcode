/*
 * @(#)CouponOrderType.java 2014-4-25
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * CouponOrderType.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-4-25
 * @since      1.0
 */
public enum CouponOrderType implements AbstractEnumInterface<CouponOrderType> {
	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 使用优惠券
	 */
	USE_COUPON(0, "使用优惠券"),
	/**
	 * 返券
	 */
	RETURN_COUPON(1, "返券"), ;

	private final int value;

	private final String desc;

	private CouponOrderType(int v, String d) {
		value = v;
		desc = d;
	}

	public CouponOrderType genEnumByIntValue(int intValue) {
		for (CouponOrderType item : CouponOrderType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}
