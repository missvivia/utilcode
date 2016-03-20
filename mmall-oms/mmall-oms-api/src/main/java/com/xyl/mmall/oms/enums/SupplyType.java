package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author zb<br>
 *         供货模式
 */
public enum SupplyType implements AbstractEnumInterface<SupplyType> {
	NULL(-1, "NULL"), SELF(0, "自供"), TOGETHOR(1, "共同供货");

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
	private SupplyType(int v, String d) {
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
	public SupplyType genEnumByIntValue(int intValue) {
		for (SupplyType item : SupplyType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

}
