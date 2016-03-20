package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author hzliujie
 * 2014年10月24日 上午11:19:40
 */
public enum BociTime implements AbstractEnumInterface<BociTime> {
	FIRST_BATCH(12, "一批次"),SECOND_BATCH(14, "二批次"),THIRD_BATCH(16, "三批次");

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
	private BociTime(int v, String d) {
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
	public BociTime genEnumByIntValue(int intValue) {
		for (BociTime item : BociTime.values()) {
			if (item.value == intValue)
				return item;
		}
		return FIRST_BATCH;
	}

}
