package com.xyl.mmall.order.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 发票信息-订单维度-状态
 * 
 * @author dingmingliang
 * 
 */
public enum InvoiceInOrdState implements AbstractEnumInterface<InvoiceInOrdState> {

	/**
	 * 初始化
	 */
	INIT(0, "未开票"),
	/**
	 * 开票中
	 */
	KP_ING(1, "已开票"),
	/**
	 * 不开票
	 */
	UN_KP(10, "不开票"),
	/**
	 * 不开票:实付金额为0
	 */
	UN_KP_AMOUNT0(11, "不开票:实付金额为0"), ;

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
	private InvoiceInOrdState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public InvoiceInOrdState genEnumByIntValue(int intValue) {
		for (InvoiceInOrdState item : InvoiceInOrdState.values()) {
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
