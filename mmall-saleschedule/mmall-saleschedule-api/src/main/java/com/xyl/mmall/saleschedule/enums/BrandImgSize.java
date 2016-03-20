package com.xyl.mmall.saleschedule.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum BrandImgSize implements AbstractEnumInterface<BrandImgSize> {

	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	
	SIZE_BRAND_LOGO(0, "品牌logo 尺寸最小的"),
	
	SIZE_BRAND_VISUAL_WEB(1, "品牌Web形象图 尺寸中等"),
	
	SIZE_BRAND_VISUAL_APP(2, "品牌App形象图 尺寸最大的"),
	;
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
	private BrandImgSize(int v, String d) {
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
    public BrandImgSize genEnumByIntValue(int intValue) {
	    for (BrandImgSize item : BrandImgSize.values()) {
	    	if (item.value == intValue)
	    		return item;
	    }
	    return NULL;
    }
}
