package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author zb
 *
 */
public enum ReturnType implements AbstractEnumInterface<ReturnType> {
	NULL(-1, "NULL"), ONE(0, "一退"), TWO(1, "二退"), THREE(2, "三退");

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
	private ReturnType(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#getIntValue()
	 */
	@Override
	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public ReturnType genEnumByIntValue(int intValue) {
		for (ReturnType item : ReturnType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}
	
	public static ReturnType genEnumByCodeIgnoreCase(String code){
		for (ReturnType item : ReturnType.values()) {
			if (item.name().equalsIgnoreCase(code))
				return item;
		}
		return NULL;
	}

}
