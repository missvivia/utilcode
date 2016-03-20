package com.xyl.mmall.excelparse;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum ExcelBool implements ExcelEnumInterface<ExcelBool> {

	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 件
	 */
	TRUE(1, "是"),

	/**
	 * 束
	 */
	FALSE(0, "否");

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
	private ExcelBool(int v, String d) {
		value = v;
		desc = d;
	}

	public ExcelBool genEnumByIntValue(int intValue) {
		for (ExcelBool type : ExcelBool.values()) {
			if (type.value == intValue)
				return type;
		}
		return null;
	}

	public ExcelBool getEnumByDesc(String descValue) {
		for (ExcelBool type : ExcelBool.values()) {
			if (type.desc.equals(descValue))
				return type;
		}
		return NULL;
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

	public String toString() {
		return desc;
	}
}
