package com.xyl.mmall.oms.warehouse.adapter.ems;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum EmsTraceOperateType implements AbstractEnumInterface<EmsTraceOperateType> {
	TYPE_50(50, "安排投递"),

	TYPE_10(10, "妥投"), 
	
	TYPE_40(40, "封发"), 
	
	TYPE_20(20, "未妥投"), 
	
	TYPE_00(00, "收寄"), 
	
	TYPE_41(41, "开拆");

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
	 */
	private EmsTraceOperateType(int v, String d) {
		value = v;
		desc = d;
	}

	@Override
	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public EmsTraceOperateType genEnumByIntValue(int intValue) {
		for (EmsTraceOperateType item : EmsTraceOperateType.values()) {
			if (item.value == intValue)
				return item;
		}
		return TYPE_00;
	}
	
	public static EmsTraceOperateType getEnumByIntValue(int intValue) {
		for (EmsTraceOperateType item : EmsTraceOperateType.values()) {
			if (item.value == intValue)
				return item;
		}
		return TYPE_00;
	}

}
