/**
 * 单品还是多品
 */
package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author zb
 */
public enum PickMoldType implements AbstractEnumInterface<PickMoldType> {
	NULL(-1, "NULL"), SINGLE(0, "单品"), MANY(1, "多品");

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
	private PickMoldType(int v, String d) {
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
	public PickMoldType genEnumByIntValue(int intValue) {
		for (PickMoldType item : PickMoldType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

}
