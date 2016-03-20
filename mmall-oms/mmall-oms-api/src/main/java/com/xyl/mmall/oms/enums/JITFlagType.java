/**
 * JIT标示枚举
 */
package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author hzzengdan
 * @date 2014-09-11
 */
public enum JITFlagType implements AbstractEnumInterface<JITFlagType> {
	NULL(-1, "NULL"), IS_JIT(1, "JIT模式");

	private final int value;

	private final String desc;

	private JITFlagType(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#getIntValue()
	 */
	@Override
	public int getIntValue() {
		// TODO Auto-generated method stub
		return value;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public JITFlagType genEnumByIntValue(int intValue) {
		for (JITFlagType item : JITFlagType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

}
