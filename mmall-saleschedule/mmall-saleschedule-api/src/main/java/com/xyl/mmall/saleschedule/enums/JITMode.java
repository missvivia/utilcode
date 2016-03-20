package com.xyl.mmall.saleschedule.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 档期状态
 * 
 * @author hzzhanghui
 * 
 */
public enum JITMode implements AbstractEnumInterface<JITMode> {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 非JIT
	 */
	NONJIT(1, "非JIT"),
	/**
	 * JIT
	 */
	JIT(2, "JIT");

	private final int value;

	private final String desc;

	private JITMode(int v, String d) {
		value = v;
		desc = d;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public JITMode genEnumByIntValue(int intValue) {
		for (JITMode item : JITMode.values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return NULL;
	}

}
