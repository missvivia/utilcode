/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.enums;

/**
 * ModelSpeciOptionType.java created by yydx811 at 2015年5月10日 上午12:39:33
 * 规格选项类型
 *
 * @author yydx811
 */
public enum ModelSpeciOptionType {
	
	/** 文本. */
	TXT(1, "txt"),
	
	/** 图片. */
	PIC(2, "pic");

	/** 值. */
	private final int value;

	/** 描述. */
	private final String desc;

	private ModelSpeciOptionType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public int getIntValue() {
		return value;
	}
	
	public String getDesc() {
		return desc;
	}

	public static ModelSpeciOptionType genEnumByIntValue(int intValue) {
		for (ModelSpeciOptionType item : ModelSpeciOptionType.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

}
