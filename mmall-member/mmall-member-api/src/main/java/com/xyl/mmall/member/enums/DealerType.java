/**
 * 
 */
package com.xyl.mmall.member.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author lihui
 *
 */
public enum DealerType implements AbstractEnumInterface<DealerType> {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 商户
	 */
	OWNER(0, "商户"),
	/**
	 * 店员
	 */
	EMPLOYEE(1, "店员");

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
	private DealerType(int value, String desc) {
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
	public DealerType genEnumByIntValue(int intValue) {
		for (DealerType item : DealerType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

}
