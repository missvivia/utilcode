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
public enum TradeItemPayMethod implements AbstractEnumInterface<TradeItemPayMethod> {

	/**
	 * 网易宝支付
	 */
	EPAY(0, "网易宝支付"),
	/**
	 * 货到付款(Cash On Delivery)
	 */
	COD(1, "货到付款"),
	/**
	 * 0元订单(不必支付)
	 */
	ZERO(2, "0元订单"),
	/**
	 * 支付宝(网易宝转接)
	 */
	ALIPAY(3, "支付宝");

	/**
	 * 在线支付方式的列表
	 */
	private static final TradeItemPayMethod[] ONLINE_ARRAY = new TradeItemPayMethod[] { EPAY, ALIPAY };

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
	private TradeItemPayMethod(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public TradeItemPayMethod genEnumByIntValue(int intValue) {
		for (TradeItemPayMethod item : TradeItemPayMethod.values()) {
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

	/**
	 * 判断支付方式是否是在线支付
	 * 
	 * @param pm
	 * @return
	 */
	public static boolean isOnlinePayMethod(TradeItemPayMethod pm) {
		return CollectionUtil.isInArray(ONLINE_ARRAY, pm);
	}

	/**
	 * 返回在线支付的支付方式列表
	 * 
	 * @return
	 */
	public static TradeItemPayMethod[] getOnlineArray() {
		TradeItemPayMethod[] array = new TradeItemPayMethod[ONLINE_ARRAY.length];
		int idx = -1;
		for (TradeItemPayMethod pm : ONLINE_ARRAY) {
			idx++;
			array[idx] = pm;
		}
		return array;
	}
}
