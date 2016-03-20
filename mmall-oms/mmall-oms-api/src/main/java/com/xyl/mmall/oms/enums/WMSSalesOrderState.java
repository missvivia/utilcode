/**
 * 
 */
package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 销售订单状态
 * 
 * @author hzzengchengyuan
 * 
 */
public enum WMSSalesOrderState implements AbstractEnumInterface<WMSSalesOrderState> {
	NULL(0, "NULL"),

	RECVSUCCESS(1, "接单成功"),

	RECVFAILED(2, "接单失败"),

	PRINT(3, "打单"),

	PICK(4, "拣货"),

	CHECK(5, "复核"),

	PACKAGE(6, "打包"),

	SHIP(7, "发货"),

	CANCEL(8, "取消发货"),
	
	/**
	 * 该状态位只用于标识该订单有包裹状态的更新
	 */
	WAYUPDATE(9,"包裹途中状态更新");
	
	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;
	
	WMSSalesOrderState(int value, String desc) {
		this.value = value;
		this.desc = desc;
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
	public WMSSalesOrderState genEnumByIntValue(int i) {
		for (WMSSalesOrderState state : values()) {
			if (state.getValue() == i) {
				return state;
			}
		}
		return null;
	}
	
	public static WMSSalesOrderState genEnumNameIgnoreCase(String name) {
		for (WMSSalesOrderState type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
