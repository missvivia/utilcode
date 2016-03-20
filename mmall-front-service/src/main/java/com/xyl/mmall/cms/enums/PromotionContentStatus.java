/**
 * 推广内容状态标示枚举
 */
package com.xyl.mmall.cms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author liujie
 * @date 2014-09-25
 */
public enum PromotionContentStatus implements AbstractEnumInterface<PromotionContentStatus> {
	ONLINE(1, "上线"), OFFLINE(2, "下线");

	private final int value;

	private final String desc;

	private PromotionContentStatus(int v, String d) {
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
	public PromotionContentStatus genEnumByIntValue(int intValue) {
		for (PromotionContentStatus item : PromotionContentStatus.values()) {
			if (item.value == intValue)
				return item;
		}
		return OFFLINE;
	}

	
	public static String getDescByInt(int intValue){
		for (PromotionContentStatus item : PromotionContentStatus.values()) {
			if (item.value == intValue)
				return item.getDesc();
		}
		return OFFLINE.desc;
	}
}
