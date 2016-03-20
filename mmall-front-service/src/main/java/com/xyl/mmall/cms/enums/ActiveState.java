/**
 * 推广内容状态标示枚举
 */
package com.xyl.mmall.cms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author hzliujie
 * 2014年10月25日 上午11:04:19
 */
public enum ActiveState implements AbstractEnumInterface<ActiveState> {
	UNLOCK(0, "激活"), LOCK(1, "冻结");

	private final int value;

	private final String desc;

	private ActiveState(int v, String d) {
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
	public ActiveState genEnumByIntValue(int intValue) {
		for (ActiveState item : ActiveState.values()) {
			if (item.value == intValue)
				return item;
		}
		return UNLOCK;
	}

	
	public static String getDescByInt(int intValue){
		for (ActiveState item : ActiveState.values()) {
			if (item.value == intValue)
				return item.getDesc();
		}
		return UNLOCK.desc;
	}
}
