package com.xyl.mmall.oms.dto;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author hzzengchengyuan
 *
 */
public enum RegioLevel implements AbstractEnumInterface<RegioLevel> {
	ZERO(0, "全国", "中华人民共和国"),

	ONE(1, "省直辖市", "一级行政区，包括省、直辖市、自治区、特别行政区"),

	TWO(2, "市", "二级行政区，包括地级市"),

	THREE(3, "县/区", "三级行政区，区县"),

	FOUR(4, "乡镇/街道", "四级行政区，乡镇，街道");

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
	private RegioLevel(int v, String n, String d) {
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

	@Override
	public RegioLevel genEnumByIntValue(int intValue) {
		for (RegioLevel type : values()) {
			if (type.getValue() == intValue) {
				return type;
			}
		}
		return null;
	}

}
