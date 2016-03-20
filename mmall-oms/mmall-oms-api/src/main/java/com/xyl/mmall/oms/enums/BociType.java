package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author hzliujie
 * 2014年10月24日 上午11:19:40
 */
public enum BociType implements AbstractEnumInterface<BociType> {
	FIRST_SINGLE(1, "一批次单品"),FIRST_DOUBLE(2, "一批次双品"),SECOND_SINGLE(3, "二批次单品"),SECOND_DOUBLE(4, "二批次单品"),THIRD_SINGLE(5, "三批次单品"),THIRD_DOUBLE(6, "三批次双品");

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
	private BociType(int v, String d) {
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
	public BociType genEnumByIntValue(int intValue) {
		for (BociType item : BociType.values()) {
			if (item.value == intValue)
				return item;
		}
		return FIRST_SINGLE;
	}

}
