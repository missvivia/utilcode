/**
 * 
 */
package com.xyl.mmall.member.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author lihui
 *
 */
public enum AccountStatus implements AbstractEnumInterface<AccountStatus> {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 正常
	 */
	NORMAL(0, "正常"),
	/**
	 * 已冻结
	 */
	LOCKED(1, "已冻结"),
	/**
	 * 已停用
	 */
	SUSPEND(2, "已停用");

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
	private AccountStatus(int value, String desc) {
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
	public AccountStatus genEnumByIntValue(int intValue) {
		for (AccountStatus item : AccountStatus.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

}
