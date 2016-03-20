package com.xyl.mmall.saleschedule.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 档期和商品对应的关系
 * @author hzzhanghui
 *
 */
public enum ScheduleSkuRelaType implements AbstractEnumInterface<ScheduleSkuRelaType> {

	NULL(-1, "NULL"),
	TYPE_AUDIT(1, "档期审核对应的SKU"),
	TYPE_ALL_LIST(2, "商品列表对应的SKU"),
	TYPE_SHOW_WND(3, "橱窗对应的SKU"),
	TYPE_DIY(4, "品购页DIY区域对应的SKU");

	private final int value;

	private final String desc;

	private ScheduleSkuRelaType(int v, String d) {
		value = v;
		desc = d;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public ScheduleSkuRelaType genEnumByIntValue(int intValue) {
		for (ScheduleSkuRelaType item : ScheduleSkuRelaType.values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return NULL;
	}

}

