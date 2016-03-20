package com.xyl.mmall.itemcenter.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 商品图片的类型枚举
 * 
 * @author hzhuangluqian
 *
 */
public enum PictureType implements AbstractEnumInterface<PictureType> {

	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 商品展示
	 */
	PROD(1, "商品展示"),

	/**
	 * 列表展示
	 */
	LIST(2, "列表展示"),

	/**
	 * 详情展示
	 */
	DETAIL(3, "详情展示"), ;

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
	private PictureType(int v, String d) {
		value = v;
		desc = d;
	}

	public PictureType genEnumByIntValue(int intValue) {
		for (PictureType type : PictureType.values()) {
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
