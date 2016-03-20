package com.xyl.mmall.itemcenter.enums;


public enum CategoryNormalLevel {

	NULL(0, "NULL"),
	
	LEVEL_FIRST(1, "一级商品分类"),
	
	LEVEL_SECOND(2, "二级商品分类"),
	
	LEVEL_THIRD(3, "三级商品分类"),
	;
	/**
	 * 值
	 */
	private final int value;
	
	/**
	 * 描述
	 */
	private final String desc;
	
	private CategoryNormalLevel(int v, String d) {
		value = v;
		desc = d;
	}
	
	public String getDesc() {
		return desc;
	}
	
    public int getIntValue() {
	    return value;
    }
	
	public static CategoryNormalLevel getEnumByValue(int intValue) {
		for (CategoryNormalLevel item : CategoryNormalLevel.values()) {
			if (item.value == intValue)
	    		return item;
		}
		return NULL;
	}
}
