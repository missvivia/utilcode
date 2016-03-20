package com.xyl.mmall.order.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 订单服务-TCC事务锁的类型
 * 
 * @author dingmingliang
 * 
 */
public enum OrderTCCLockType implements AbstractEnumInterface<OrderTCCLockType> {

	/**
	 * 添加订单
	 */
	ADD_ORDER(0, "添加订单"),
	/**
	 * 取消订单
	 */
	CALLOFF_ORDER(1, "取消订单"),
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
	private OrderTCCLockType(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OrderTCCLockType genEnumByIntValue(int intValue) {
		for (OrderTCCLockType item : OrderTCCLockType.values()) {
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
