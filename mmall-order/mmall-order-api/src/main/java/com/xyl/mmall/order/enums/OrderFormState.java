package com.xyl.mmall.order.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;
import com.xyl.mmall.order.jackson.OrderFormStateJsonDeserializer;

/**
 * 订单的状态
 * 
 * @author dingmingliang
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = OrderFormStateJsonDeserializer.class)
public enum OrderFormState implements AbstractEnumInterface<OrderFormState> {
	
	/** 未付款 */
	WAITING_PAY(0, "等待付款", "未付款"),

	/** 已接单未发货 */
	WAITING_DELIVE(6, "待发货", "已接单未发货"),
	
	/** 全部包裹已发货 */
	ALL_DELIVE(10, "已发货", "全部包裹已发货"),
	
	/** 已取消 */
	CANCEL_ED(21, "已取消", "已取消"),
	
	/** 交易结束 */
	FINISH_TRADE(11,"交易完成","正常交易结束"),
	
	/** 投递结束(所有包裹都不在投递状态了) */
	FINISH_DELIVE(15, "交易完成", "投递结束(所有包裹都不在投递状态了)"),
	
	/** 即将出库(假状态) */
	FAKE_JJCK(-1, "即将出库", "即将出库"),

	/** 等待审核(货到付款) */
	WAITING_COD_AUDIT(1, "待审核", "等待审核(货到付款)"),

	/** 部分包裹已发货 */
	PART_DELIVE(9, "已发货", "部分包裹已发货"),

	/** 订单取消中 */
	CANCEL_ING(20, "取消中", "订单取消中"),

	/** 所有包裹取消 */
	CANCEL_ED_ALLOP(22, "已取消", "所有包裹取消"),

	/** 准备向OMS发送取消订单 */
	WAITING_CANCEL_OMSORDER(23, "取消中", "准备向OMS发送取消订单"),

	/** 审核未通过(货到付款) */
	COD_AUDIT_REFUSE(25, "审核未通过(货到付款)", "审核未通过(货到付款)"), 
	
	/** 等待向OMS发送信息(可以取消) */
	WAITING_SEND_ORDER(2, "待发货", "等待向OMS发送信息(可以取消)"),

	/** [状态作废]开始向OMS发送信息(不可以取消) */
	@Deprecated
	START_SEND_ORDER(5, "待发货", "开始向OMS发送信息(不可以取消)");
	
	/**
	 * 允许订单取消的订单状态
	 */
	private static OrderFormState[] CAN_CANCEL_ARRAY = new OrderFormState[] { OrderFormState.WAITING_PAY,
			OrderFormState.WAITING_COD_AUDIT, OrderFormState.COD_AUDIT_REFUSE, OrderFormState.WAITING_SEND_ORDER,
			OrderFormState.WAITING_DELIVE };

	/**
	 * 可能是支付成功的订单状态(不包含取消)
	 */
	private static OrderFormState[] MAYBE_PAYED_ARRAY = new OrderFormState[] { WAITING_SEND_ORDER, WAITING_DELIVE,
			PART_DELIVE, ALL_DELIVE, FINISH_DELIVE };

	/**
	 * oms取消失败后,回滚取消逻辑后,允许重新设置的的订单状态
	 */
	private static OrderFormState[] REVERT_OMS_CANCEL_NEWSTATE_ARRAY = new OrderFormState[] { WAITING_DELIVE };

	/** 值 */
	private final int value;

	/** 描述 */
	private final String desc;

	/**
	 * 后台状态的名称
	 */
	private final String name;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 * @param name
	 */
	private OrderFormState(int v, String d, String name) {
		value = v;
		desc = d;
		this.name = name;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OrderFormState genEnumByIntValue(int value) {
		for (OrderFormState item : OrderFormState.values()) {
			if (item.value == value)
				return item;
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public static OrderFormState genEnumByIntValueSt(int value) {
		return OrderFormState.values()[0].genEnumByIntValue(value);
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public String getName() {
		return name;
	}

	/**
	 * 订单是否是取消状态
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isCancel(OrderFormState state) {
		boolean isOrderCancel = state == OrderFormState.CANCEL_ED || state == OrderFormState.CANCEL_ING
				|| state == OrderFormState.CANCEL_ED_ALLOP;
		return isOrderCancel;
	}

	/**
	 * 判断订单是否可以取消
	 * 
	 * @param state
	 * @return
	 */
	public static boolean canCancel(OrderFormState state) {
		return CollectionUtil.isInArray(CAN_CANCEL_ARRAY, state);
	}

	/**
	 * 返回: 可能是支付成功的订单状态(不包含取消)
	 * 
	 * @return
	 */
	public static OrderFormState[] getMaybePayedArray() {
		return Arrays.asList(MAYBE_PAYED_ARRAY).toArray(new OrderFormState[0]);
	}

	/**
	 * CMS到付审核操作：是否支持“通过”操作
	 * 
	 * @param state
	 * @return
	 */
	public static boolean canExecCODPass(OrderFormState state) {
		return (null != state && state == OrderFormState.WAITING_COD_AUDIT);
	}

	/**
	 * CMS到付审核操作：是否支持“拒绝”操作
	 * 
	 * @param state
	 * @return
	 */
	public static boolean canExecCODReject(OrderFormState state) {
		return (null != state && state == OrderFormState.WAITING_COD_AUDIT);
	}

	/**
	 * CMS到付审核操作：是否支持“撤销”操作
	 * 
	 * @param state
	 * @return
	 */
	public static boolean canExecCODCancelReject(OrderFormState state) {
		return (null != state && state == OrderFormState.COD_AUDIT_REFUSE);
	}

	/**
	 * oms取消失败后,回滚取消逻辑后,允许重新设置的的订单状态
	 * 
	 * @return
	 */
	public static OrderFormState[] getRevertOmsCancelNewStateArray() {
		return Arrays.asList(REVERT_OMS_CANCEL_NEWSTATE_ARRAY).toArray(new OrderFormState[0]);
	}

	/**
	 * state是否属于 oms取消失败后,回滚取消逻辑后,允许重新设置的的订单状态
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isInRevertOmsCancelNewStateArray(OrderFormState state) {
		return CollectionUtil.isInArray(REVERT_OMS_CANCEL_NEWSTATE_ARRAY, state);
	}
	
	
	public static OrderFormState getNextState(OrderFormState state) {
		switch (state) {
			case WAITING_PAY:
				return WAITING_DELIVE;
			case WAITING_DELIVE:
				return ALL_DELIVE;
			case ALL_DELIVE:
				return FINISH_TRADE;
		default:
			return CANCEL_ED;
		}
	}
}
