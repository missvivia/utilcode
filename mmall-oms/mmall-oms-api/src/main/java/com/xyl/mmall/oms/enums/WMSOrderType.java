/**
 * 
 */
package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 仓储订单类型
 * 
 * @author hzzengchengyuan
 *
 */
public enum WMSOrderType implements AbstractEnumInterface<WMSOrderType> {

	NULL(0, "NULL"),

	SI_S(11, "单品入库单"),

	SI_M(12, "多品入库单"),

	SI_PS(13, "补货单品入库单"),
	
	SI_PM(14, "补货多品入库单"),
	
	SO(20, "退供出库单"),

	SALES(30, "销售订单"),

	RETURN(40, "退货入库单"),
	
	R_UA(41, "未妥投入库单");

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
	private WMSOrderType(int v, String d) {
		value = v;
		desc = d;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public int getIntValue() {
		return this.value;
	}

	@Override
	public WMSOrderType genEnumByIntValue(int i) {
		for (WMSOrderType type : values()) {
			if (type.getValue() == i) {
				return type;
			}
		}
		return NULL;
	}

	public static WMSOrderType genEnumByDesc(String desc) {
		for (WMSOrderType type : values()) {
			if (type.desc.equals(desc)) {
				return type;
			}
		}
		return NULL;
	}

	public static WMSOrderType genEnumNameIgnoreCase(String name) {
		for (WMSOrderType type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return NULL;
	}

}
