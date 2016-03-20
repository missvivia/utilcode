package com.xyl.mmall.photomgr.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 用户状态。 0-无效 1-有效
 * 
 * @author zhzhanghui
 * 
 */
public enum AlbumUserState implements AbstractEnumInterface<AlbumUserState> {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 无效
	 */
	INVALID(0, "无效"),
	/**
	 * 有效
	 */
	VALID(1, "有效");

	private final int value;

	private final String desc;

	private AlbumUserState(int v, String d) {
		value = v;
		desc = d;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public AlbumUserState genEnumByIntValue(int intValue) {
		for (AlbumUserState item : AlbumUserState.values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return NULL;
	}

}
