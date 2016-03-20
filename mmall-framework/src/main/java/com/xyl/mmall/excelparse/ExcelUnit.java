package com.xyl.mmall.excelparse;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum ExcelUnit implements ExcelEnumInterface<ExcelUnit> {

	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 件
	 */
	JIAN(1, "件"),

	/**
	 * 个
	 */
	GE(2, "个"),

	/**
	 * 双
	 */
	SHUANG(3, "双"),
	/**
	 * 双
	 */
	ZHI(4, "支"),
	/**
	 * 双
	 */
	FU(5, "副"),
	/**
	 * 套
	 */
	TAO(6, "套"),
	/**
	 * 台
	 */
	TAI(7, "台"),
	/**
	 * 本
	 */
	BEN(8, "本"),
	/**
	 * 片
	 */
	PIAN(9, "片"),
	/**
	 * 盒
	 */
	HE(10, "盒"),
	/**
	 * 束
	 */
	SHU(11, "束");

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
	private ExcelUnit(int v, String d) {
		value = v;
		desc = d;
	}

	public ExcelUnit genEnumByIntValue(int intValue) {
		for (ExcelUnit type : ExcelUnit.values()) {
			if (type.value == intValue)
				return type;
		}
		return null;
	}

	public ExcelUnit getEnumByDesc(String descValue) {
		for (ExcelUnit type : ExcelUnit.values()) {
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
