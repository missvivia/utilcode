package com.xyl.mmall.order.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 取消oms订单的任务状态
 * 
 * @author dingmingliang
 * 
 */
public enum CancelOmsOrderTaskState implements AbstractEnumInterface<CancelOmsOrderTaskState> {

	/**
	 * 创建完毕
	 */
	CREATE(0, "创建完毕"),
	/**
	 * 取消oms订单成功
	 */
	CANCEL_OMS_SUCC(1, "取消oms订单成功"),
	/**
	 * 取消oms订单成功(不需要取消)
	 */
	CANCEL_OMS_UNNEED(2, "取消oms订单成功(不需要取消)"),
	/**
	 * 取消oms订单失败
	 */
	CANCEL_OMS_FAIL(3, "取消oms订单失败"),
	/**
	 * 回退订单状态到取消前
	 */
	REVERT_CANCEL_ORDER(4, "回退订单状态到取消前"),
	/**
	 * 取消order订单失败
	 */
	CANCEL_ORDER_SUCC(6, "取消order订单成功"),
	/**
	 * 取消order订单失败
	 */
	CANCEL_ORDER_FAIL(7, "取消order订单失败"), ;

	/**
	 * 已经处理完毕的任务状态列表
	 */
	private static CancelOmsOrderTaskState[] THE_END_ARRAY = new CancelOmsOrderTaskState[] { REVERT_CANCEL_ORDER,
			CANCEL_ORDER_SUCC, CANCEL_ORDER_FAIL };

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
	private CancelOmsOrderTaskState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public CancelOmsOrderTaskState genEnumByIntValue(int intValue) {
		return CancelOmsOrderTaskState.genEnumByIntValueSt(intValue);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public static CancelOmsOrderTaskState genEnumByIntValueSt(int intValue) {
		for (CancelOmsOrderTaskState item : CancelOmsOrderTaskState.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
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

	/**
	 * 判断任务状态是否是已经处理完毕的
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isTheEndState(CancelOmsOrderTaskState state) {
		return CollectionUtil.isInArray(THE_END_ARRAY, state);
	}

	/**
	 * 返回还没处理完的任务状态列表
	 * 
	 * @return
	 */
	public static CancelOmsOrderTaskState[] getUnTheEndStateArray() {
		List<CancelOmsOrderTaskState> list = new ArrayList<>(); 
		list.addAll(Arrays.asList(CancelOmsOrderTaskState.values()));
		for (CancelOmsOrderTaskState state : THE_END_ARRAY) {
			list.remove(state);
		}
		return list.toArray(new CancelOmsOrderTaskState[0]);
	}
}
