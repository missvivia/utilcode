package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 退货单状态
 * 
 * @author hzzengchengyuan
 */
public enum WMSReturnOrderState implements AbstractEnumInterface<WMSReturnOrderState> {
	NULL(0, "NULL"),

	DONE(10, "已退回仓库");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	WMSReturnOrderState(int value, String desc) {
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
	public WMSReturnOrderState genEnumByIntValue(int i) {
		for (WMSReturnOrderState state : values()) {
			if (state.getValue() == i) {
				return state;
			}
		}
		return null;
	}

	public static WMSReturnOrderState genEnumNameIgnoreCase(String name) {
		for (WMSReturnOrderState type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
