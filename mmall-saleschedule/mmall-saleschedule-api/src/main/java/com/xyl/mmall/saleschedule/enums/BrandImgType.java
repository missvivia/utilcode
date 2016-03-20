package com.xyl.mmall.saleschedule.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum BrandImgType implements AbstractEnumInterface<BrandImgType> {

	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 品牌形象图片类型
	 */
	IMG_BRANDIMAGE(0, "品牌形象"),
	/**
	 * 品牌橱窗图片类型
	 */
	IMG_BRANDSHOWCASE(1, "品牌橱窗")
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
	private BrandImgType(int v, String d) {
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
    public BrandImgType genEnumByIntValue(int intValue) {
	    for (BrandImgType item : BrandImgType.values()) {
	    	if (item.value == intValue)
	    		return item;
	    }
	    return NULL;
    }
}
