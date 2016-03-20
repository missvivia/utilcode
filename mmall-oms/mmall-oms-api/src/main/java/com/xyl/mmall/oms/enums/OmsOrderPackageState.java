package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * OMS订单的状态
 * 
 * @author zb
 * 
 */
public enum OmsOrderPackageState implements AbstractEnumInterface<OmsOrderPackageState> {

	SHIP(0, "已发货", "已发货"),

	DONE(10, "妥投", "妥投"), // 初始状态
	
	REJECT(20, "拒收", "拒收"),
	
	LOST(30,"丢失","丢失");

	/** 值 */
	private final int value;

	/** 描述 */
	private final String desc;

	/**
	 * 后台状态的名称
	 */
	private final String name;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 * @param name
	 */
	private OmsOrderPackageState(int v, String d, String name) {
		value = v;
		desc = d;
		this.name = name;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OmsOrderPackageState genEnumByIntValue(int value) {
		for (OmsOrderPackageState item : OmsOrderPackageState.values()) {
			if (item.value == value)
				return item;
		}
		return null;
	}

	public static OmsOrderPackageState genEnumByDesc(String desc) {
		for (OmsOrderPackageState item : OmsOrderPackageState.values()) {
			if (item.desc.equals(desc))
				return item;
		}
		return null;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public String getName() {
		return name;
	}
}
