package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author hzzengchengyuan
 *
 */
public enum WMSExpressCompany implements AbstractEnumInterface<WMSExpressCompany> {
	NULL(0, "NULL", "NULL"),
	
	EMS(1, "EMS", "EMS"),
	
	SF(2, "SF", "顺丰");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 快递公司简称
	 */
	private final String name;

	/**
	 * 描述
	 */
	private final String desc;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private WMSExpressCompany(int v, String n, String d) {
		value = v;
		desc = d;
		name = n;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public String getName() {
		return name;
	}

	@Override
	public int getIntValue() {
		return this.value;
	}

	public static WMSExpressCompany genEnumNameIgnoreCase(String name) {
		for (WMSExpressCompany type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}

	@Override
	public WMSExpressCompany genEnumByIntValue(int intValue) {
		for (WMSExpressCompany type : values()) {
			if (type.getValue() == intValue) {
				return type;
			}
		}
		return null;
	}
}