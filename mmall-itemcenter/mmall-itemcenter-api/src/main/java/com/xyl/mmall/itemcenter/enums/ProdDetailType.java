package com.xyl.mmall.itemcenter.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 商品详情的类型枚举
 * 
 * @author hzhuangluqian
 *
 */
public enum ProdDetailType implements AbstractEnumInterface<ProdDetailType> {

	/**
	 * NULL
	 */
	NULL(-1, "NULL"),

	/**
	 * SELECT下拉框
	 */
	SINGLE_SELECT(1, "SINGLE_SELECT"),

	/**
	 * 文字输入
	 */
	TEXT(2, "TEXT"),

	/**
	 * 文本域
	 */
	TEXT_AREA(3, "TEXT_AREA"),

	/**
	 * 多选
	 */
	MULTI_SELECT(4, "MULTI_SELECT");

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
	private ProdDetailType(int v, String d) {
		value = v;
		desc = d;
	}

	public ProdDetailType genEnumByIntValue(int intValue) {
		for (ProdDetailType type : ProdDetailType.values()) {
			if (type.value == intValue)
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
}
