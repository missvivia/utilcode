package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum RejectPackageState implements AbstractEnumInterface<RejectPackageState> {
	NULL(0, "NULL"), 
	
	/**
	 * 已经将拒收快件请求推送给仓库
	 */
	SENDWMS(10, "返仓请求已发出"), 
	
	/**
	 * 仓库已经收到拒收快件 
	 */
	RECEIVED(20, "仓库接收到快件");

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
	 */
	private RejectPackageState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#getIntValue()
	 */
	@Override
	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public RejectPackageState genEnumByIntValue(int intValue) {
		for (RejectPackageState item : RejectPackageState.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

	public static RejectPackageState genEnumByCodeIgnoreCase(String code) {
		for (RejectPackageState item : RejectPackageState.values()) {
			if (item.name().equalsIgnoreCase(code))
				return item;
		}
		return NULL;
	}

}
