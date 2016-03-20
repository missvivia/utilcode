package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 退货时优惠券+红包回收状态
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:08:11
 * 
 */
@Deprecated
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeprecatedReturnCouponHbRecycleState implements AbstractEnumInterface<DeprecatedReturnCouponHbRecycleState> {

	NULL(0, "未初始化"), 
	
	NONE(1, "没有使用优惠券+红包"), 
	
	DO_NOT_RETURN(2, "不退回优惠券+红包"), 
	
	WAITING_RETURN(3, "等待退回优惠券+红包"), 
	
	RETURNED(4, "已退回优惠券+红包");

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
	private DeprecatedReturnCouponHbRecycleState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public DeprecatedReturnCouponHbRecycleState genEnumByIntValue(int intValue) {
		for (DeprecatedReturnCouponHbRecycleState item : DeprecatedReturnCouponHbRecycleState.values()) {
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
