package com.xyl.mmall.saleschedule.enums;

import java.io.Serializable;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * PO收藏状态
 * 
 * @author hzzhanghui
 * 
 */
public enum ScheduleLikeState implements AbstractEnumInterface<ScheduleLikeState>, Serializable {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 有效
	 */
	VALID(1, "有效"),
	/**
	 * 无效
	 */
	INVALID(2, "无效");

	private final int value;

	private final String desc;

	private ScheduleLikeState(int v, String d) {
		value = v;
		desc = d;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public ScheduleLikeState genEnumByIntValue(int intValue) {
		for (ScheduleLikeState item : ScheduleLikeState.values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return NULL;
	}

}
