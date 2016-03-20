package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 退货状态
 * 
 */
public enum OmsReturnOrderFormState implements AbstractEnumInterface<OmsReturnOrderFormState> {

	SENDED(0, "已推送给仓库", "已推送给仓库"),

	/**
	 * 
	 */
	WMSACCEPT(1, "仓库接单", "仓库接单"),

	/**
	 * 
	 */
	REJECT(2, "拒收退货", "拒收退货"),
	/**
	 * 
	 */
	CONFIRMED(3, "已退回仓库", "仓库已确认收货"),

	/**
	 * 
	 */
	NOTICE_CONFIRMED(4, "通知上层退货模块已收货", "通知上层退货模块已收货"),
	
	;

	/** 值 */
	private final int value;

	/** 标签 */
	private final String tag;

	/** 描述 */
	private final String desc;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param t
	 * @param d
	 * @param name
	 */
	private OmsReturnOrderFormState(int v, String t, String d) {
		value = v;
		tag = t;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OmsReturnOrderFormState genEnumByIntValue(int intValue) {
		for (OmsReturnOrderFormState item : OmsReturnOrderFormState.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

	public static OmsReturnOrderFormState genEnumByStringValue(String value) {
		for (OmsReturnOrderFormState item : OmsReturnOrderFormState.values()) {
			if (item.tag.equals(value))
				return item;
		}
		return null;
	}

	public int getIntValue() {
		return value;
	}

	public String getTag() {
		return tag;
	}

	public String getDesc() {
		return desc;
	}
}
