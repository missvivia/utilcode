package com.xyl.mmall.saleschedule.enums;

import java.io.Serializable;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 档期Banner类型。如上新、预告等
 * 
 * @author hzzhanghui
 * 
 */
public enum ScheduleBannerType implements AbstractEnumInterface<ScheduleBannerType>, Serializable {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 上新
	 */
	DRAFT(1, "上新"),
	/**
	 * 预告
	 */
	CHECKING(2, "预告");

	private final int value;

	private final String desc;

	private ScheduleBannerType(int v, String d) {
		value = v;
		desc = d;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public ScheduleBannerType genEnumByIntValue(int intValue) {
		for (ScheduleBannerType item : ScheduleBannerType.values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return NULL;
	}

}
