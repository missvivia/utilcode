/**
 * 入库单状态枚举
 */
package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author hzliujie
 * @date 2014-10-21
 */
public enum WarehouseStockState implements AbstractEnumInterface<WarehouseStockState> {
    INIT(0, "待提交"),SUBMIT(1, "已提交"),DONE(2, "已处理"),CANCEL(3, "已取消"),ERROR(4, "出错"),REMOTE_CANCEL(5, "已撤销");
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
	private WarehouseStockState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
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
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public WarehouseStockState genEnumByIntValue(int intValue) {
		for (WarehouseStockState item : WarehouseStockState.values()) {
			if (item.value == intValue)
				return item;
		}
		return INIT;
	}
}