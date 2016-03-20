package com.xyl.mmall.excelparse;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum ExcelSex implements ExcelEnumInterface<ExcelSex> {
	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 男
	 */
	NAN(1, "男"),

	/**
	 * 女
	 */
	NV(2, "女"),

	/**
	 * 男童
	 */
	NAN_TONG(3, "男童"),
	/**
	 * 女童
	 */
	NV_TONG(4, "女童"),
	/**
	 * 中性
	 */
	ZHON_GXING(5, "中性");

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
	private ExcelSex(int v, String d) {
		value = v;
		desc = d;
	}

	public ExcelSex genEnumByIntValue(int intValue) {
		for (ExcelSex type : ExcelSex.values()) {
			if (type.value == intValue)
				return type;
		}
		return null;
	}

	public ExcelSex getEnumByDesc(String descValue) {
		for (ExcelSex type : ExcelSex.values()) {
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
