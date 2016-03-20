package com.xyl.mmall.order.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 订单取消的退款方式
 * 
 * @author dingmingliang
 * 
 */
public enum OrderCancelRType implements AbstractEnumInterface<OrderCancelRType> {

	/**
	 * 原路退回
	 */
	ORI(0, "原路退回"),
	/**
	 * 非原路退回
	 */
	UN_ORI(1, "第三方支付"),	
	/**
	 * 未知(可能是分批取消)
	 */
	ALL_PACKAGE_CANCELLED(2, "原路退回(所有包裹取消)"), 
	/**
	 * 未知(可能是分批取消)
	 * 优惠券已回收
	 */
	ALL_PACKAGE_CANCELLED_COUPON_RECYCLED(3, "原路退回(所有包裹取消)"), 
	/**
	 * 未知(可能是分批取消)
	 * 没有任何优惠券
	 */
	ALL_PACKAGE_CANCELLED_NO_COUPON(4, "原路退回(所有包裹取消)"), 
	/**
	 * 未知(可能是分批取消)
	 * 订单有多个包裹的情况下，只要有一个包裹被签收或者是被拒收的，都不退回订单使用的优惠券
	 */
	ALL_PACKAGE_CANCELLED_NOT_RECYCLE_FOR_R_SR(5, "原路退回(所有包裹取消)");

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
	private OrderCancelRType(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OrderCancelRType genEnumByIntValue(int intValue) {
		for (OrderCancelRType item : OrderCancelRType.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

	/**
	 * @param intValue
	 * @return
	 */
	public static OrderCancelRType genEnumByIntValueSt(int intValue) {
		return OrderCancelRType.values()[0].genEnumByIntValue(intValue);
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
