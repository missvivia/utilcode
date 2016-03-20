package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 退货时的红包回收状态
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:08:11
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum HBRecycleState implements AbstractEnumInterface<HBRecycleState> {

	INIT(0, "未初始化"), 
	
	NONE(1, "不回收红包"), 
	
	WAITING_RECYCLING(2, "等待回收红包"), 
	
	RECYCLED(3, "已回收");

	/** 值 */
	private final int value;

	/** 描述 */
	private final String desc;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 * @param name
	 */
	private HBRecycleState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public HBRecycleState genEnumByIntValue(int intValue) {
		for (HBRecycleState item : HBRecycleState.values()) {
			if (item.value == intValue)
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
}
