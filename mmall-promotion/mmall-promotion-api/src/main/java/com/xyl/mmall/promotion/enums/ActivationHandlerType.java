/*
 * @(#)CouponHandlerType.java 2014-4-25
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * CouponHandlerType.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-4-25
 * @since      1.0
 */
public enum ActivationHandlerType implements AbstractEnumInterface<ActivationHandlerType> {
	DEFAULT(0, "未处理"),
	
	RECYCLE(1, "回收"),
	
	GRANT(2, "发放"),
	
	CANCEL_RESET(3, "交易取消重置") ;

	private final int value;

	private final String desc;

	private ActivationHandlerType(int v, String d) {
		value = v;
		desc = d;
	}

	public ActivationHandlerType genEnumByIntValue(int intValue) {
		for (ActivationHandlerType item : ActivationHandlerType.values()) {
			if (item.value == intValue)
				return item;
		}
		return DEFAULT;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}
