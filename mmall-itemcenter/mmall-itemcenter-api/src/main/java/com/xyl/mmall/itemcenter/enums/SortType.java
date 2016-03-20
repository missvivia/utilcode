package com.xyl.mmall.itemcenter.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum SortType implements AbstractEnumInterface<SortType> {
	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 默认排序
	 */
	DEFAULT(0, "默认排序"),

	/**
	 * 按价格排序
	 */
	PRICE(1, "按价格排序"),

	/**
	 * 按销售量排序
	 */
	SALE(2, "按销售量排序"),

	/**
	 * 按折扣排序
	 */
	DISCOUNT(3, "按折扣排序"), ;

	/**
	 * 值
	 */
	private final int value;

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
	private SortType(int v, String d) {
		value = v;
		desc = d;
	}

	public SortType genEnumByIntValue(int intValue) {
		for (SortType type : SortType.values()) {
			if (type.value == intValue)
				return type;
		}
		return NULL;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#getIntValue()
	 */
	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}
