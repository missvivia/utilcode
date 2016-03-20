/**
 * 
 */
package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 商家退供单状态
 * 
 * @author hzzengchengyuan
 *
 */
public enum PoReturnOrderState implements AbstractEnumInterface<PoReturnOrderState> {

	NULL(-1, "NULL"), NEW(0, "创建"), CONFIRM(1, "已确认"), SHIPPED(2, "已发货"), RECEIPTED(3, "已收货");

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
	 */
	private PoReturnOrderState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public PoReturnOrderState genEnumByIntValue(int intValue) {
		for (PoReturnOrderState item : PoReturnOrderState.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
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
	
	public static PoReturnOrderState genEnumByCodeIgnoreCase(String code){
		for (PoReturnOrderState item : PoReturnOrderState.values()) {
			if (item.name().equalsIgnoreCase(code))
				return item;
		}
		return NULL;
	}

}
