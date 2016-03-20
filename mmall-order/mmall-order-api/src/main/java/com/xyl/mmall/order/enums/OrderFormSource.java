package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 订单的来源
 * 
 * @author dingmingliang
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderFormSource implements AbstractEnumInterface<OrderFormSource> {
	
	/**
	 * 主站
	 */
	PC(0, "主站"),
	/**
	 * 移动-安卓
	 */
	MOBILE_ANDROID(1,"移动-安卓"),
	/**
	 * 移动-IOS
	 */
	MOBILE_IOS(2,"移动-IOS");

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
	private OrderFormSource(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OrderFormSource genEnumByIntValue(int intValue) {
		for (OrderFormSource item : OrderFormSource.values()) {
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
