package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 出库单状态
 * 
 * @author hzzengchengyuan
 */
public enum WMSShipOutOrderState implements AbstractEnumInterface<WMSShipOutOrderState> {
	NULL(0, "NULL"),

	WMSACCEPT(1, "仓库接单"),
	
	DONE(2, "完成入库");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	WMSShipOutOrderState(int value, String desc) {
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
	public WMSShipOutOrderState genEnumByIntValue(int i) {
		for (WMSShipOutOrderState state : values()) {
			if (state.getValue() == i) {
				return state;
			}
		}
		return null;
	}

	public static WMSShipOutOrderState genEnumNameIgnoreCase(String name) {
		for (WMSShipOutOrderState type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
