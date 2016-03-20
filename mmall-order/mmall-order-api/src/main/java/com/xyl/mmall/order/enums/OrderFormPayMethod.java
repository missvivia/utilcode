package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 订单的支付方式
 * 
 * @author dingmingliang
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderFormPayMethod implements AbstractEnumInterface<OrderFormPayMethod> {

	/**
	 * 支付宝支付<br>
	 * (页面要求将支付宝放在第一位)
	 */
	ALIPAY(2, "在线支付", "支付宝支付"),
	/**
	 * 网易宝支付
	 */
	EPAY(0, "在线支付", "网易宝支付"),
	/**
	 * 货到付款(Cash On Delivery)
	 */
	COD(1, "货到付款", "货到现金付款"),
	
	/**
	 * 货到POS机付款
	 */
	POS(3,"货到POS机付款","货到POS机付款");

	/**
	 * 在线支付方式的列表
	 */
	private static OrderFormPayMethod[] ONLINE_ARRAY = new OrderFormPayMethod[] { OrderFormPayMethod.ALIPAY,
			OrderFormPayMethod.EPAY };

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	/**
	 * 真实名称
	 */
	private final String realName;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private OrderFormPayMethod(int v, String d, String rn) {
		value = v;
		desc = d;
		realName = rn;
	}

	/**
	 * 判断支付方式是否是在线支付
	 * 
	 * @param pm
	 * @return
	 */
	public static boolean isOnlinePayMethod(OrderFormPayMethod pm) {
		return CollectionUtil.isInArray(ONLINE_ARRAY, pm);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OrderFormPayMethod genEnumByIntValue(int intValue) {
		for (OrderFormPayMethod item : OrderFormPayMethod.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

	/**
	 * @param intValue
	 * @return
	 */
	public static OrderFormPayMethod genEnumByIntValueSt(int intValue) {
		return OrderFormPayMethod.values()[0].genEnumByIntValue(intValue);
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

	public String getRealName() {
		return realName;
	}

}
