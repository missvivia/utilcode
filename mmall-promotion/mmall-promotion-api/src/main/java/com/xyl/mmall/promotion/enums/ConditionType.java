/*
 * @(#)ConditionKey.java 2014-4-9
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * ConditionKey.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-4-9
 * @since      1.0
 */
public enum ConditionType implements AbstractEnumInterface<ConditionType> {
	
	BASIC_PRICE_SATISFY(1, "订单价格满"),
	
	BASIC_COUNT_SATISFY(2, "订单数量满"),
	
	BASIC_BRANDSCHEDULE_SATISFY(3, "指定品牌档期");
	
	private int value;
	
	private String desc;
	
	
	ConditionType(int value, String desc) {
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
	public ConditionType genEnumByIntValue(int intValue) {
		for (ConditionType key : ConditionType.values()) {
			if (key.getValue() == intValue) {
				return key;
			}
		}
		return null;
	}
	
}
