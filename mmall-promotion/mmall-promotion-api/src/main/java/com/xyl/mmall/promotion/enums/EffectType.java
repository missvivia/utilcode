/*
 * @(#) 2014-10-12
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * EffectType.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-12
 * @since      1.0
 */
public enum EffectType  implements AbstractEnumInterface<EffectType> {
	
	ACTIVATION_MINUS(1, "直降"),
	
	ACTIVATION_DISCOUNT(2, "折"),
	
	ACTIVATION_EXPRESS_FREE(3, "包邮"),
	
	ACTIVATION_RED_PACKETS_PRESENT(4, "返红包"),
	
	ACTIVATION_COUPON_PRESENT(5, "返优惠券");
	
	private int value;
	
	private String desc;
	
	
	EffectType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}


	public void setValue(int value) {
		this.value = value;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int getIntValue() {
		return this.value;
	}
	
	@Override
	public EffectType genEnumByIntValue(int intValue) {
		for (EffectType key : EffectType.values()) {
			if (key.getValue() == intValue) {
				return key;
			}
		}
		return null;
	}

}
