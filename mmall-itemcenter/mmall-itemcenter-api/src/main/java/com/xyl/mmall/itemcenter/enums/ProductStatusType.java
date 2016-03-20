package com.xyl.mmall.itemcenter.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProductStatusType implements AbstractEnumInterface<ProductStatusType> {
	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	
	/**
	 * 无效
	 */
	INVAILD(0, "无效"),
	
	/**
	 * 未审核
	 */
	NOTSUBMIT(1, "未审核"),

	/**
	 * 审核中
	 */
	PENDING(2, "审核中"),

	/**
	 * 审核未通过
	 */
	REJECT(3, "审核未通过"),

	/**
	 * 已上架
	 */
	ONLINE(4, "已上架"),

	/**
	 * 已下架
	 */
	OFFLINE(5, "已下架");

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
	private ProductStatusType(int v, String d) {
		value = v;
		desc = d;
	}

	public ProductStatusType genEnumByIntValue(int intValue) {
		for (ProductStatusType type : ProductStatusType.values()) {
			if (type.value == intValue)
				return type;
		}
		return NULL;
	}
	
	public static ProductStatusType getEnumByIntValue(int intValue) {
		return ProductStatusType.values()[0].genEnumByIntValue(intValue);
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