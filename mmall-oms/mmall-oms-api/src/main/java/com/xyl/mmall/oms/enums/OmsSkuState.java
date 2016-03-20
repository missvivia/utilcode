package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author zb sku状态
 */
public enum OmsSkuState implements AbstractEnumInterface<OmsSkuState> {
	NULL(-1, "NULL"),

	WAITSYN(0, "未同步到仓库"),

	SYNED(1, "已同步到仓库");

	private final int value;

	private final String desc;

	private OmsSkuState(int v, String d) {
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
	public OmsSkuState genEnumByIntValue(int intValue) {
		for (OmsSkuState item : OmsSkuState.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

}
