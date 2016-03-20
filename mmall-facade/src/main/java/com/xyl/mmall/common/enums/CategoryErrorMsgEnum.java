package com.xyl.mmall.common.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum CategoryErrorMsgEnum implements AbstractEnumInterface<CategoryErrorMsgEnum>{
	
	SUCCESS(0,"成功"),
	
	SUB_CATEGORY_NOT_EXIST_ERROR(1, "父类目不存在"),
	
	CONTENT_NORMAL_CATEGORY_RELATED_ERROR(2, "关联失败，只有第三级内容分类能关联商品类目"),
	
	CATEGORY_NAME_NULL_ERROR(3,"内容分类名称为空!"),
	
	CATEGORY_MODIFIED_ERROR(4,"当前分类下包含子分类，无法修改上级分类!"),
	
	CATEGORY_SITE_NULL_ERROR(5,"	站点为空!"),
	
	CATEGORY_LEVEL_BEYOND_ERROR(6,"	内容分类等级不能超过3级!"),
	
	CATEGORY_SITE_REPEAT_ERROR(7,"	站点重复或站点有包含关系!"),
	
	OTHER_ERROR(-1,"某些原因导致的操作失败");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;
	

	private CategoryErrorMsgEnum(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	@Override
	public CategoryErrorMsgEnum genEnumByIntValue(int intValue) {
		for (CategoryErrorMsgEnum item : CategoryErrorMsgEnum.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

	@Override
	public int getIntValue() {
		return value;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public static String getDescByIntValue(int intValue) {
		for (CategoryErrorMsgEnum item : CategoryErrorMsgEnum.values()) {
			if (item.value == intValue)
				return item.getDesc();
		}
		return null;
	}

}
