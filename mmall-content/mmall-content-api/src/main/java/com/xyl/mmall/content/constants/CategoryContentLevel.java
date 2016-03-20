package com.xyl.mmall.content.constants;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum CategoryContentLevel implements AbstractEnumInterface<CategoryContentLevel> {

	NULL(0, "NULL"),
	
	LEVEL_FIRST(1, "一级内容分类"),
	
	LEVEL_SECOND(2, "二级内容分类"),
	
	LEVEL_THIRD(3, "三级内容分类"),
	;
	/**
	 * 值
	 */
	private final int value;
	
	/**
	 * 描述
	 */
	private final String desc;
	
	private CategoryContentLevel(int v, String d) {
		value = v;
		desc = d;
	}
	
	public String getDesc() {
		return desc;
	}
	
	@Override
    public int getIntValue() {
	    return value;
    }
	
	@Override
	public CategoryContentLevel genEnumByIntValue(int intValue) {
		for (CategoryContentLevel item : CategoryContentLevel.values()) {
			if (item.value == intValue)
	    		return item;
		}
		return NULL;
	}
}