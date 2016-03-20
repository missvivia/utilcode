package com.xyl.mmall.framework.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 订单渠道
 * 
 * @author zb
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SpSource implements AbstractEnumInterface<SpSource> {

	/**
	 * 
	 */
	NULL(-1, ""),
	
	/**
	 * 普通订单
	 */
	MMALL(4, "普通订单"),

	/**
	 * 代客下单
	 */
	PROXY(7, "代客下单");

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
	 * 
	 * @param v
	 * @param d
	 */
	private SpSource(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public SpSource genEnumByIntValue(int intValue) {
		for (SpSource item : SpSource.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#getIntValue()
	 */
	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}
