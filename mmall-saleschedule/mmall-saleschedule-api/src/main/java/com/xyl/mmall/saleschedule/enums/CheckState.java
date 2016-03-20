package com.xyl.mmall.saleschedule.enums;

import java.io.Serializable;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 档期商品审核状态
 * 
 * @author hzzhanghui
 * 
 */
public enum CheckState implements AbstractEnumInterface<CheckState>, Serializable {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 未提交
	 */
	DRAFT(1, "待提交"),
	/**
	 * 审核中
	 */
	CHECKING(2, "待审核"),
	/**
	 * 审核通过
	 */
	PASSED(3, "审核通过"),
	/**
	 * 审核未通过
	 */
	REJECTED(4, "审核未通过");

	private final int value;

	private final String desc;

	private CheckState(int v, String d) {
		value = v;
		desc = d;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public CheckState genEnumByIntValue(int intValue) {
		for (CheckState item : CheckState.values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return NULL;
	}

}