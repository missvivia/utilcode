package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 仓库类别
 * 
 * @author hzzengchengyuan
 */
public enum WarehouseType implements AbstractEnumInterface<WarehouseType> {

	NULL(0, "NULL"), 
	
	EMS(1, "EMS"), 
	
	BAISHI(2, "百世物流"), 
	
	SF(3, "顺丰");
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
	private WarehouseType(int v, String d) {
		value = v;
		desc = d;
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
	public WarehouseType genEnumByIntValue(int i) {
		for (WarehouseType type : values()) {
			if (type.getValue() == i) {
				return type;
			}
		}
		return null;
	}

	public static WarehouseType genEnumByDesc(String desc) {
		for (WarehouseType type : values()) {
			if (type.desc.equals(desc)) {
				return type;
			}
		}
		return NULL;
	}
	
	public static WarehouseType genEnumNameIgnoreCase(String name) {
		for (WarehouseType type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return NULL;
	}
}
