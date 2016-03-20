package com.xyl.mmall.ip.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum ExpressType implements AbstractEnumInterface<ExpressType> {

	NULL(0, "NULL"),
	
	TYPE_EMS(1, "EMS"),
	
	TYPE_SF(2, "顺丰速递")
	;
	
	/**
	 * 值
	 */
	private final int value;
	
	/**
	 * 描述
	 */
	private final String desc;
	
	private ExpressType(int v, String d) {
		this.value = v;
		this.desc = d;
	}
	
	public String getDesc() {
		return desc;
	}
	
	@Override
    public int getIntValue() {
	    return value;
    }
	
	@Override
	public ExpressType genEnumByIntValue(int intValue) {
		for (ExpressType item : ExpressType.values()) {
			if (item.value == intValue)
	    		return item;
		}
		return NULL;
	}
}
