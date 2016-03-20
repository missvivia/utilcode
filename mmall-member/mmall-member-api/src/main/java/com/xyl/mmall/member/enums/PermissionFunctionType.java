/**
 * 
 */
package com.xyl.mmall.member.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author lihui
 *
 */
public enum PermissionFunctionType implements AbstractEnumInterface<PermissionFunctionType> {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 左边导航条权限
	 */
	LEFTNAVE(0, "leftNav"),
	/**
	 * 操作权限
	 */
	OPERATION(2, "operation");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	private PermissionFunctionType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public PermissionFunctionType genEnumByIntValue(int intValue) {
		for (PermissionFunctionType item : PermissionFunctionType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
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

	public String getDesc() {
		return desc;
	}

}
