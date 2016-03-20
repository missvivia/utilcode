package com.xyl.mmall.order.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 订单取消的状态
 * 
 * @author dingmingliang
 * 
 */
public enum OrderCancelState implements AbstractEnumInterface<OrderCancelState> {
	
	/**
	 * 创建完毕
	 */
	CREATE(0, "创建完毕"),
	/**
	 * 处理完毕
	 */
	DONE(1,"处理完毕"),
	;

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
	private OrderCancelState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OrderCancelState genEnumByIntValue(int intValue) {
		for (OrderCancelState item : OrderCancelState.values()) {
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
