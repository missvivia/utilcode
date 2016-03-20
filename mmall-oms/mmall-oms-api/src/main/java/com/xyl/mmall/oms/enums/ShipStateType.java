/**
 * 发货状态枚举类
 */
package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author hzzengdan
 * @date 2014-09-11
 */
public enum ShipStateType implements AbstractEnumInterface<ShipStateType> {

	NULL(-1, "NULL"),

//	UNSHIPPED(0, "未发货"),
//
//	SHIPPED(1, "已发货"),

	RECEIVED(2, "已入库，仓库已接收"),

	UNSEDN(3, "未入库，未推送给仓库"),

	SEND(4, "未入库，已推送给仓库"),

	CANCEL(5, "未入库，超时取消");

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
	private ShipStateType(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public ShipStateType genEnumByIntValue(int intValue) {
		for (ShipStateType item : ShipStateType.values()) {
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

}
