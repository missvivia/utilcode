/**
 * 拣货状态枚举
 */
package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author hzzengdan
 * @date 2014-09-11
 */
public enum PickStateType implements AbstractEnumInterface<PickStateType> {
	NULL(-1, "NULL"), UNPICK(0, "未出仓"), PICKING(1, "拣货中"), PICKED(2, "已出仓");

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
	private PickStateType(int v, String d) {
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
	public PickStateType genEnumByIntValue(int intValue) {
		for (PickStateType item : PickStateType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

}
