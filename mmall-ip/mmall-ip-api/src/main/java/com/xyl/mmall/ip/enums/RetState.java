package com.xyl.mmall.ip.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum RetState implements AbstractEnumInterface<RetState> {

	NULL(0, "NULL"),
	
	STATE_TRUE(1, "返回值是true"),
	
	STATE_NOTFULLY_TRUE(2, "返回值不完全是true"),
	
	STATE_FALSE(3, "返回值是false"),
	;
	
	/**
	 * 值
	 */
	private final int value;
	
	/**
	 * 描述
	 */
	private final String desc;
	
	private RetState(int v, String d) {
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
	public RetState genEnumByIntValue(int intValue) {
		for (RetState item : RetState.values()) {
			if (item.value == intValue)
	    		return item;
		}
		return NULL;
	}
}
