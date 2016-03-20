package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author hzliujie
 * 2014年10月24日 上午11:19:40
 */
public enum BuHuoType implements AbstractEnumInterface<BuHuoType> {
	NORMAL(0, "正常"), BUHUO(1, "补货");

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
	private BuHuoType(int v, String d) {
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
	public BuHuoType genEnumByIntValue(int intValue) {
		for (BuHuoType item : BuHuoType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NORMAL;
	}

}
