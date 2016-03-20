package com.xyl.mmall.order.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 发票信息-商家订单维度-状态
 * 
 * @author dingmingliang
 * 
 */
public enum InvoiceInOrdSupplierState implements AbstractEnumInterface<InvoiceInOrdSupplierState> {

	/**
	 * 初始化
	 */
	INIT(0, "初始化"),
	/**
	 * 已经开票
	 */
	KP_ED(1, "已经开票"),
	/**
	 * 不开票
	 */
	UN_KP(10, "不开票"),
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
	private InvoiceInOrdSupplierState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public InvoiceInOrdSupplierState genEnumByIntValue(int intValue) {		
		return InvoiceInOrdSupplierState.genEnumByIntValueSt(intValue);
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public static InvoiceInOrdSupplierState genEnumByIntValueSt(int intValue) {
		for (InvoiceInOrdSupplierState item : InvoiceInOrdSupplierState.values()) {
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
