/**
 * 
 */
package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author hzzengdan
 * @date 2014-09-17
 */
public enum StockOperType implements AbstractEnumInterface<StockOperType> {
	NULL(-1, "NULL"), NO_JIT(0, "满足订单，消减库存"), IS_JIT(1, "WMS入库通知，增加库存");

	private final int value;

	private final String desc;

	private StockOperType(int v, String d) {
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
	public StockOperType genEnumByIntValue(int intValue) {
		for (StockOperType item : StockOperType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}
}
