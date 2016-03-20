package com.xyl.mmall.itemcenter.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 审核状态枚举
 * 
 * @author hzhuangluqian
 *
 */
public enum StatusType implements AbstractEnumInterface<StatusType> {

	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 未提交
	 */
	NOTSUBMIT(1, "未提交"),

	/**
	 * 待审核
	 */
	PENDING(2, "待审核"),

	/**
	 * 审核通过
	 */
	APPROVAL(3, "审核通过"),

	/**
	 * 审核不通过
	 */
	REJECT(4, "审核不通过"),

	/**
	 * 审核不通过
	 */
	EXPIRE(5, "失效");

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
	private StatusType(int v, String d) {
		value = v;
		desc = d;
	}

	public StatusType genEnumByIntValue(int intValue) {
		for (StatusType type : StatusType.values()) {
			if (type.value == intValue)
				return type;
		}
		return NULL;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#getIntValue()
	 */
	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

}
