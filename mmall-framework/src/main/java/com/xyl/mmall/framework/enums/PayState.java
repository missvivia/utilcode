package com.xyl.mmall.framework.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 支付状态
 * 
 * @author dingmingliang
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PayState implements AbstractEnumInterface<PayState> {

	/**
	 * 在线支付-未付款
	 */
	ONLINE_NOT_PAY(20, "在线支付-未付款"),
	/**
	 * 货到付款未支付
	 */
	COD_NOT_PAY(21, "货到付款-未支付"),
	/**
	 * 货到付款拒绝支付
	 */
	COD_REFUSE_PAY(22, "货到付款-拒绝支付"),
	/**
	 * 在线支付-修改为其他支付方式
	 */
	ONLINE_CHANGE(23, "在线支付-修改为其他支付方式"),
	/**
	 * 在线支付-已支付
	 */
	ONLINE_PAYED(30, "在线支付-已支付"),
	/**
	 * 货到付款已支付
	 */
	COD_PAYED(31, "货到付款-已支付"),
	/**
	 * 0元订单
	 */
	ZERO_PAYED(32, "0元订单"),
	/**
	 * 在线支付-已经退款
	 */
	ONLINE_REFUNDED(40, "在线支付-已退款"),
	/**
	 * 0元订单-已经退款
	 */
	ZERO_REFUNDED(42, "在线支付-已退款"),
	/**
	 * 在线支付-已关闭
	 */
	ONLINE_CLOSE(50, "在线支付-已关闭"),
	/**
	 * 货到付款-已关闭
	 */
	COD_CLOSE(51, "货到付款-已关闭"), ;

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
	private PayState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public PayState genEnumByIntValue(int intValue) {
		for (PayState item : PayState.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}
	
	public static PayState genEnumByIntValueSt(int intValue){
		return PayState.values()[0].genEnumByIntValue(intValue);
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
