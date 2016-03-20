/**
 * 
 */
package com.xyl.mmall.member.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author lihui
 *
 */
public enum AgentType implements AbstractEnumInterface<AgentType> {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 系统管理员
	 */
	ROOT(0, "系统管理员"),
	/**
	 * 普通管理员
	 */
	ADMIN(1, "管理员"),
	/**
	 * 普通用户
	 */
	USER(2, "用户");
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
	private AgentType(int value, String desc) {
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
	public AgentType genEnumByIntValue(int intValue) {
		for (AgentType item : AgentType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

}
