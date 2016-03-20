/**
 * 
 */
package com.xyl.mmall.member.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author lihui
 *
 */
public enum AuthzCategory implements AbstractEnumInterface<AuthzCategory> {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 商家后台管理
	 */
	VIS(0, "商家后台管理"),
	/**
	 * 运维平台管理
	 */
	CMS(1, "运维平台管理"),
	/**
	 * 主站系统
	 */
	MAINSITE(2, "主站系统"),
	/**
	 * 手机后台系统
	 */
	MOBILE(3, "手机后台系统"),
	/**
	 * 手机后台系统
	 */
	WAP(4, "wap系统");

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
	private AuthzCategory(int value, String desc) {
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
	public AuthzCategory genEnumByIntValue(int intValue) {
		for (AuthzCategory item : AuthzCategory.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}
}
