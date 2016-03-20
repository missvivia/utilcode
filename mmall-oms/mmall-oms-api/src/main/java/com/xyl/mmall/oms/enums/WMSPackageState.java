package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum WMSPackageState implements AbstractEnumInterface<WMSPackageState> {

	NULL(0, "NULL"),

	SEND(10, "发出"),

	WAY(20, "途中"),

	SIGNED(30, "签收"),

	UNRECEIPTED(40, "未签收"),
	
	/**
	 * 在包裹未签收时，订单需要重新入库，确认入库后的包裹状态
	 */
	RETWAREHOUSE(50,"包裹已返仓");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	WMSPackageState(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public int getIntValue() {
		return this.value;
	}

	@Override
	public WMSPackageState genEnumByIntValue(int i) {
		for (WMSPackageState state : values()) {
			if (state.getValue() == i) {
				return state;
			}
		}
		return NULL;
	}

	public static WMSPackageState genEnumNameIgnoreCase(String name) {
		for (WMSPackageState type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return NULL;
	}
}
