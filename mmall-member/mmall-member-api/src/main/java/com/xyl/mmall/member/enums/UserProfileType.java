/**
 * 
 */
package com.xyl.mmall.member.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author lihui
 *
 */
public enum UserProfileType implements AbstractEnumInterface<UserProfileType> {

	/**
	 * 
	 */
	NULL(-1, "未知"),
	/**
	 * 男
	 */
	URS(1, "urs"),
	/**
	 * 女
	 */
	YIXIN(2, "yixin"),
	/**
	 * 男
	 */
	SINA(3, "sina"),
	/**
	 * 女
	 */
	TENCENT(4, "tencent");

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
	 * 
	 * @param v
	 * @param d
	 */
	private UserProfileType(int value, String desc) {
		this.value = value;
		this.desc = desc;
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

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public UserProfileType genEnumByIntValue(int intValue) {
		for (UserProfileType item : UserProfileType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}
}
