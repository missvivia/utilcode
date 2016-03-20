package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 退货时的运费补贴红包状态
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:08:11
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ReturnExpHbState implements AbstractEnumInterface<ReturnExpHbState> {

	INIT(0, "未初始化"), 
	
	NONE(1, "不发放补贴红包"), 
	
	WAITING_DISTRIBUTING(2, "等待退回红包"), 
	
	DISTRIBUTED(3, "已发放");

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
	private ReturnExpHbState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public ReturnExpHbState genEnumByIntValue(int intValue) {
		for (ReturnExpHbState item : ReturnExpHbState.values()) {
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
