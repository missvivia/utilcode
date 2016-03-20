package com.xyl.mmall.common.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageState;

/**
 * 订单的查询方式
 * 
 * @author dingmingliang
 * 
 */
public enum OrderQueryType implements AbstractEnumInterface<OrderQueryType> {

	/**
	 * 全部订单
	 */
	ALL(0, "全部订单"),
	/**
	 * 待付款
	 */
	WAITING_PAY(1, "待付款", new OrderFormState[] { OrderFormState.WAITING_PAY }, null),
	/**
	 * 待发货
	 */
	WAITING_DELIVE(2, "待发货", new OrderFormState[] {OrderFormState.WAITING_DELIVE }, null),
 
//    WAITING_DELIVE(2, "待发货", new OrderFormState[] { OrderFormState.WAITING_COD_AUDIT,
//		 OrderFormState.WAITING_DELIVE, OrderFormState.PART_DELIVE }, null),
	/**
	 * 已发货
	 */
	ALREADY_DELIVE(3, "已发货", new OrderFormState[] {OrderFormState.ALL_DELIVE }, null),
	/**
	 * 查询
	 */
	SEARCH(4, "查询"),
	
	/**
	 * 订单Id
	 */
	ORDER_ID(5,"订单Id"),
	
	/**
	 * 用户名
	 */
	ORDER_USERNAME(6,"用户名"),
	
	/**
	 * 下单时间
	 */
	ORDER_TIME(7,"下单时间");
	
	

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	/**
	 * 订单状态列表
	 */
	private final OrderFormState[] orderStateArray;

	/**
	 * 包裹状态列表
	 */
	private final OrderPackageState[] packageStateArray;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 * @param orderStateArray
	 * @param packageStateArray
	 */
	private OrderQueryType(int v, String d, OrderFormState[] orderStateArray, OrderPackageState[] packageStateArray) {
		value = v;
		desc = d;
		this.orderStateArray = orderStateArray;
		this.packageStateArray = packageStateArray;
	}

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private OrderQueryType(int v, String d) {
		this(v, d, null, null);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OrderQueryType genEnumByIntValue(int intValue) {
		for (OrderQueryType item : OrderQueryType.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public static OrderQueryType genEnumByIntValueSt(int intValue) {
		return OrderQueryType.values()[0].genEnumByIntValue(intValue);
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

	public OrderFormState[] getOrderStateArray() {
		return orderStateArray;
	}

	public OrderPackageState[] getPackageStateArray() {
		return packageStateArray;
	}
}
