package com.xyl.mmall.order.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 到付审核状态
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:08:11
 * 
 */
public enum CODAuditState implements AbstractEnumInterface<CODAuditState> {

	/** 等待审核 */
	WAITING(6, "等待审核"),

	/** 审核通过 */
	PASSED(7, "审核通过"),

	/** 审核被拒绝 */
	REFUSED(8, "审核被拒绝"), 
	
	/** 审核被拒绝 */
	CANCELED(9, "已取消");

	/** 值 */
	private final int value;

	/** 描述 */
	private final String desc;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 * @param name
	 */
	private CODAuditState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public CODAuditState genEnumByIntValue(int intValue) {
		for (CODAuditState item : CODAuditState.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}
