package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 包裹的状态
 * 
 * @author dingmingliang
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderPackageState implements AbstractEnumInterface<OrderPackageState> {

	/** 未向OMS发送信息 */
	INIT(0, "待发货", "未接收到OMS的发货回调"),

	/** [状态作废]已接单未发货 */
	@Deprecated
	WAITING_DELIVE(1, "待发货", "已接单未发货"),

	/** 已发货(等待签收) */
	WAITING_SIGN_IN(5, "已发货", "已发货(等待签收)"),

	/** 货品已签收 */
	SIGN_IN(6, "交易完成", "货品已签收"),

	/** [状态作废]包裹被拒收 */
	@Deprecated
	SIGN_FAIL(7, "已发货", "包裹未妥投"),

	/** [状态作废]货品被拒收 */
	@Deprecated
	SIGN_REFUSED(8, "交易完成", "货品被拒收"),

	/** 超时未配送 */
	OUT_TIME(9, "待发货", "超时未配送"),

	/** 退货申请中 */
	RP_APPLY(13, "交易完成", "退货申请中"),

	/** 退货完成 */
	RP_DONE(14, "交易完成", "退货完成"),

	/** 包裹取消-超时未配送 */
	CANCEL_OT(15, "已取消-缺货", "包裹取消-超时未配送"),

	/** 包裹取消-拒收 */
	CANCEL_SR(16, "已取消-拒收", "包裹取消-拒收"),

	/** 包裹取消-订单取消-未付款 */
	CANCEL_OC_UNPAY(17, "已取消", "包裹取消-订单取消-未付款"),

	/** 包裹取消-丢件 */
	CANCEL_LOST(18, "已取消-丢件", "包裹取消-丢件"),

	/** 包裹取消-订单取消-已付款 */
	CANCEL_OC_PAYED(19, "已取消", "包裹取消-订单取消-已付款"), ;

	/**
	 * 取消状态列表
	 */
	private static OrderPackageState[] CANCEL_ARRAY = new OrderPackageState[] { CANCEL_OT, CANCEL_SR, CANCEL_OC_UNPAY,
			CANCEL_LOST, CANCEL_OC_PAYED };
	
	/**
	 * 订单取消状态列表
	 */
	private static OrderPackageState[] CANCEL_ORDER_ARRAY = new OrderPackageState[] { CANCEL_OC_UNPAY, CANCEL_OC_PAYED };

	/**
	 * 可以退运费的状态列表
	 */
	private static OrderPackageState[] REFUND_EXP_ARRAY = new OrderPackageState[] { CANCEL_OT, CANCEL_OC_UNPAY,
			CANCEL_LOST, CANCEL_OC_PAYED };

	/**
	 * 投递结束状态列表
	 */
	private static OrderPackageState[] FINISH_DELIVE_ARRAY = new OrderPackageState[] { SIGN_IN, RP_APPLY, RP_DONE };

	/**
	 * 取消+退货的列表
	 */
	public static OrderPackageState[] TYPE1_ARRAY = new OrderPackageState[] { CANCEL_OT, CANCEL_SR, CANCEL_OC_UNPAY,
			CANCEL_LOST, CANCEL_OC_PAYED, RP_APPLY, RP_DONE };

	/**
	 * 取消+退货的列表
	 */
	public static OrderPackageState[] TYPE2_ARRAY = new OrderPackageState[] { CANCEL_OT, CANCEL_SR, CANCEL_LOST,
			CANCEL_OC_PAYED, RP_DONE };

	/**
	 * 是否已经取消
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isCancel(OrderPackageState state) {
		return CollectionUtil.isInArray(CANCEL_ARRAY, state);
	}
	
	/**
	 * 是否已经取消(订单取消)
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isCancelByOrder(OrderPackageState state) {
		return CollectionUtil.isInArray(CANCEL_ORDER_ARRAY, state);
	}

	/**
	 * 是否属于可以退运费的包裹状态
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isCanRefundExp(OrderPackageState state) {
		return CollectionUtil.isInArray(REFUND_EXP_ARRAY, state);
	}

	/**
	 * 取消的列表
	 * 
	 * @return
	 */
	public static OrderPackageState[] getCancelArray() {
		OrderPackageState[] src = CANCEL_ARRAY;
		int length = src.length;
		OrderPackageState[] dest = new OrderPackageState[length];
		System.arraycopy(src, 0, dest, 0, length);
		return dest;
	}

	/**
	 * 是否已经投递结束
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isFinishDelive(OrderPackageState state) {
		return CollectionUtil.isInArray(FINISH_DELIVE_ARRAY, state);
	}

	/**
	 * 取消+退货的列表
	 * 
	 * @return
	 */
	public static OrderPackageState[] getType1Array(OrderPackageState[] src) {
		if (src == null || src.length == 0) {
			return null;
		}
		int length = src.length;
		OrderPackageState[] dest = new OrderPackageState[length];
		System.arraycopy(src, 0, dest, 0, length);
		return dest;
	}

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
	private OrderPackageState(int v, String d, String name) {
		value = v;
		desc = d;
		this.name = name;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OrderPackageState genEnumByIntValue(int intValue) {
		for (OrderPackageState item : OrderPackageState.values()) {
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

	public String getName() {
		return name;
	}

}
