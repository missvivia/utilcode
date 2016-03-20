package com.xyl.mmall.order.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 订单取消的来源
 * 
 * @author dingmingliang
 * 
 */
public enum OrderCancelSource implements AbstractEnumInterface<OrderCancelSource> {

	/**
	 * 用户取消
	 */
	USER(0, "用户取消"),
	/**
	 * 客服取消
	 */
	KF(1, "客服取消"),
	/**
	 * 超时系统取消
	 */
	OT_SYS(2, "超时系统取消"),
	/**
	 * 所有包裹都被取消
	 */
	ALL_PACKAGE_CANCEL(3, "所有包裹都被取消"),

	/**
	 * 商家取消
	 */
	BUSINESSER(4,"商家取消"),
	
	/**
	 * erp取消
	 */
	ERP(5, "erp取消");

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
	private OrderCancelSource(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OrderCancelSource genEnumByIntValue(int intValue) {
		for (OrderCancelSource item : OrderCancelSource.values()) {
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
