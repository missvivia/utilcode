package com.xyl.mmall.saleschedule.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 档期状态
 * 
 * @author hzzhanghui
 * 
 */
public enum ScheduleState implements AbstractEnumInterface<ScheduleState>, java.io.Serializable {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * CMS新建档期的几个状态
	 */
	DRAFT(1, "待提交"), CHECKING(2, "待审核"), PASSED(3, "审核通过"), REJECTED(4, "审核未通过"),
	/**
	 * 上线,暂不使用
	 */
	//ONLINE(101, "上线"),
	/**
	 * 下线，暂不使用
	 */
	OFFLINE(102, "下线"),
	/**
	 * Backend系统添加商品后进行审核的几个状态。
	 * 3的状态在Backend系统中算是新建状态。
	 * 添加审核全部审核通过后状态变为202，表示PO可以上线了。
	 */
	//BACKEND_CHECKING(201, "审核中"), 
	BACKEND_PASSED(202, "审核通过");
	//, BACKEND_REJECTED(203, "审核未通过");

	private final int value;

	private final String desc;

	private ScheduleState(int v, String d) {
		value = v;
		desc = d;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public ScheduleState genEnumByIntValue(int intValue) {
		for (ScheduleState item : ScheduleState.values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return NULL;
	}

}