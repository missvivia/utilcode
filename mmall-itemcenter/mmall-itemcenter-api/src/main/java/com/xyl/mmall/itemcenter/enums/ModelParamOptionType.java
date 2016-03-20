/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.enums;

/**
 * ModelSpeciOptionType.java created by yydx811 at 2015年5月10日 上午12:39:33
 * 属性操作样式
 *
 * @author yydx811
 */
public enum ModelParamOptionType {
	
	/** 单选. */
	SINGLE(1, "single"),
	
	/** 多选. */
	MULTI(2, "multi");

	/** 值. */
	private final int value;

	/** 描述. */
	private final String desc;

	private ModelParamOptionType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public int getIntValue() {
		return value;
	}
	
	public String getDesc() {
		return desc;
	}

	public static ModelParamOptionType genEnumByIntValue(int intValue) {
		for (ModelParamOptionType item : ModelParamOptionType.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

}
