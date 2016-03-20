package com.xyl.mmall.saleschedule.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 供货方式
 * 
 * @author hzzhanghui
 * 
 */
public enum SupplyMode implements AbstractEnumInterface<SupplyMode> {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 商家自供货
	 */
	SELF(1, "商家自供货"),
	/**
	 * 共同供货
	 */
	TOGETHER(2, "共同供货");

	private final int value;

	private final String desc;

	private SupplyMode(int v, String d) {
		value = v;
		desc = d;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public SupplyMode genEnumByIntValue(int intValue) {
		for (SupplyMode item : SupplyMode.values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return NULL;
	}

}
