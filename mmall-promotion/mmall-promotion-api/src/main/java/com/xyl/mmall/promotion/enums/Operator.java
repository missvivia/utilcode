/*
 * @(#)Operator.java 2014-4-10
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * Operator.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-4-10
 * @since      1.0
 */
public enum Operator implements AbstractEnumInterface<Operator>  {
	
	EQUAL(1, "="),
	
	UNEQUAL(2, "!="),
	
	GREATER_THAN(3, ">"),
	
	LESS_THAN(4, "<"),
	
	GREATER_EQUAL_THAN(5, ">="),
	
	LESS_EQUAL_THAN(6, "<="),
	
	LIKE(7, "like"),
	
	CONTAINS(8, "contains"),
	
	IN(9, "in");
	
	private int value;
	
	private String desc;
	
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

	Operator(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	@Override
	public int getIntValue() {
		return this.value;
	}

	@Override
	public Operator genEnumByIntValue(int intValue) {
		for (Operator operator : Operator.values()) {
			if (operator.getValue() == intValue) {
				return operator;
			}
		}
		return null;
	}
}
