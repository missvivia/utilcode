/**
 * 推广内容类型枚举
 */
package com.xyl.mmall.cms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author hzliujie
 * 2014年11月1日 下午1:42:16
 */
public enum PromotionContentType implements AbstractEnumInterface<PromotionContentType> {
	WEB(0, "WEB显示"), MOBILE(1, "手机显示");

	private final int value;

	private final String desc;

	private PromotionContentType(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#getIntValue()
	 */
	@Override
	public int getIntValue() {
		// TODO Auto-generated method stub
		return value;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public PromotionContentType genEnumByIntValue(int intValue) {
		for (PromotionContentType item : PromotionContentType.values()) {
			if (item.value == intValue)
				return item;
		}
		return WEB;
	}

	
	public static String getDescByInt(int intValue){
		for (PromotionContentType item : PromotionContentType.values()) {
			if (item.value == intValue)
				return item.getDesc();
		}
		return WEB.desc;
	}
}
