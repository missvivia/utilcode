package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 入库单状态
 * 
 * @author hzzengchengyuan
 */
public enum WMSShipOrderState implements AbstractEnumInterface<WMSShipOrderState> {
	NULL(0, "NULL"),

	WMSACCEPT(1, "仓库接单"),

	WMSREJECT(2, "仓库拒单"),

	DONE(3, "完成入库");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	WMSShipOrderState(int value, String desc) {
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
	public WMSShipOrderState genEnumByIntValue(int i) {
		for (WMSShipOrderState state : values()) {
			if (state.getValue() == i) {
				return state;
			}
		}
		return null;
	}

	public static WMSShipOrderState genEnumNameIgnoreCase(String name) {
		for (WMSShipOrderState type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
