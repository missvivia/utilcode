/**
 * 推广内容TAB标示枚举
 */
package com.xyl.mmall.cms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author liujie
 * @date 2014-09-25
 */
public enum PromotionContentTab implements AbstractEnumInterface<PromotionContentTab> {
	INDEX(1, "首页"), WOMAN(2, "女装"), MAN(3, "男装"),CHILD(5,"童装");

	private final int value;

	private final String desc;

	private PromotionContentTab(int v, String d) {
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
	public PromotionContentTab genEnumByIntValue(int intValue) {
		for (PromotionContentTab item : PromotionContentTab.values()) {
			if (item.value == intValue)
				return item;
		}
		return INDEX;
	}

	
	public static String getDescByInt(int intValue){
		for (PromotionContentTab item : PromotionContentTab.values()) {
			if (item.value == intValue)
				return item.getDesc();
		}
		return INDEX.desc;
	}
}
