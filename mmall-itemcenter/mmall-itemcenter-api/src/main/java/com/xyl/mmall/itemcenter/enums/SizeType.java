package com.xyl.mmall.itemcenter.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 尺码类型枚举
 * 
 * @author hzhuangluqian
 *
 */
public enum SizeType implements AbstractEnumInterface<SizeType> {
	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 默认尺码
	 */
	ORIG_SIZE(1, "默认尺码"),

	/**
	 * 自定义尺码
	 */
	CUST_SIZE(3, "自定义尺码"),

	/**
	 * 尺码模板
	 */
	TMPL_SIZE(2, "尺码模板"), ;

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
	private SizeType(int v, String d) {
		value = v;
		desc = d;
	}

	public SizeType genEnumByIntValue(int intValue) {
		for (SizeType type : SizeType.values()) {
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
