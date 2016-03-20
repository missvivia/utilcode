package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;
import com.xyl.mmall.order.constant.ConstValueOfOrder;

/**
 * 退货包裹状态
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:08:11
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ReturnPackageState implements AbstractEnumInterface<ReturnPackageState> {

	/** 未申请(初始化) */
	INIT(0, "未申请", "未申请(初始化)"),

	/** 用户发起申请 */
	APPLY_RETURN(1, "已申请", "客户发起退货请求"),

	/** 等待仓库确认收货入仓 */
	WAITING_CONFIRM(2, "待收货", "等待仓库确认收货入仓"),

	/** 异常退货情况（非COD订单） */
	ABNORMAL_WAITING_AUDIT(3, "异常件待退款", "仓库收货数据与用户申请数据不一致"),

	/** 客服操作拒绝退款（非COD订单） */
	ABNORMAL_REFUSED(4, "拒绝", "客服操作拒绝退款"),

	/** 系统/客服操作退款（非COD订单） */
	FINISH_RETURN(5, "待退款", "系统/客服操作退款"),

	/** 退款到账 */
	FINALLY_RETURNED_TO_USER(6, "已退款", "退款到账"),

	/** 异常退货情况（COD订单） */
	ABNORMAL_COD_WAITING_AUDIT(7, "异常件待退款", "仓库收货数据与用户申请数据不一致"),

	/** 客服操作拒绝退款 （COD订单） */
	ABNORMAL_COD_REFUSED(8, "拒绝", "客服操作拒绝退款"),

	/** 等待财务操作退款（COD订单） */
	CW_WAITING_RETURN(9, "待退款", "等待财务操作退款"),

	/** 财务完成操作退款（COD订单） */
	FINALLY_COD_RETURNED_TO_USER(10, "已退款", "财务完成操作退款"),

	/** 系统取消 */
	CANCELLED(11, "已取消", "订单发货" + ConstValueOfOrder.SEP_RP_APPLY_DAY + "天后仓库未收到退货包裹，系统自动取消");

	/** 值 */
	private final int value;

	/** 标签 */
	private final String tag;

	/** 描述 */
	private final String desc;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param t
	 * @param d
	 * @param name
	 */
	private ReturnPackageState(int v, String t, String d) {
		value = v;
		tag = t;
		desc = d;
	}

	// 退货退款管理：“全部”标签
	public static ReturnPackageState[] stateArrayOfAll() {
		return new ReturnPackageState[] { ReturnPackageState.WAITING_CONFIRM,
				ReturnPackageState.ABNORMAL_WAITING_AUDIT, ReturnPackageState.ABNORMAL_REFUSED,
				ReturnPackageState.FINISH_RETURN, ReturnPackageState.FINALLY_RETURNED_TO_USER,
				ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT, ReturnPackageState.ABNORMAL_COD_REFUSED,
				ReturnPackageState.CW_WAITING_RETURN, ReturnPackageState.FINALLY_COD_RETURNED_TO_USER,
				ReturnPackageState.CANCELLED };
	}

	// 退货退款管理：“待收货”标签
	public static ReturnPackageState[] stateArrayOfWaitingConfirm() {
		return new ReturnPackageState[] { ReturnPackageState.WAITING_CONFIRM };
	}

	// 退货退款管理：“异常件待退款”标签
	public static ReturnPackageState[] stateArrayOfAbormalWaitingReturn() {
		return new ReturnPackageState[] { ReturnPackageState.ABNORMAL_WAITING_AUDIT,
				ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT };
	}

	// 退货退款管理：“正常件待退款”标签
	public static ReturnPackageState[] stateArrayOfNormalWaitingReturn() {
		return new ReturnPackageState[] { ReturnPackageState.FINISH_RETURN, ReturnPackageState.CW_WAITING_RETURN };
	}

	// 退货退款管理：“拒绝”标签
	public static ReturnPackageState[] stateArrayOfRefused() {
		return new ReturnPackageState[] { ReturnPackageState.ABNORMAL_REFUSED, ReturnPackageState.ABNORMAL_COD_REFUSED };
	}

	// 退货退款管理：“已退款”标签
	public static ReturnPackageState[] stateArrayOfReturned() {
		return new ReturnPackageState[] { ReturnPackageState.FINALLY_RETURNED_TO_USER,
				ReturnPackageState.FINALLY_COD_RETURNED_TO_USER };
	}

	// 退货退款管理：“已取消”标签
	public static ReturnPackageState[] stateArrayOfCancelled() {
		return new ReturnPackageState[] { ReturnPackageState.CANCELLED };
	}

	// 货到付款退款管理：“待退款”标签
	public static ReturnPackageState[] stateArrayOfCODWaitingReturn() {
		return new ReturnPackageState[] { ReturnPackageState.CW_WAITING_RETURN };
	}

	// 货到付款退款管理：“已退款”标签
	public static ReturnPackageState[] stateArrayOfCODReturned() {
		return new ReturnPackageState[] { ReturnPackageState.FINALLY_COD_RETURNED_TO_USER };
	}

	// 货到付款退款管理：“全部”标签
	public static ReturnPackageState[] stateArrayOfCODAll() {
		return new ReturnPackageState[] { ReturnPackageState.CW_WAITING_RETURN,
				ReturnPackageState.FINALLY_COD_RETURNED_TO_USER };
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public ReturnPackageState genEnumByIntValue(int intValue) {
		for (ReturnPackageState item : ReturnPackageState.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

	public int getIntValue() {
		return value;
	}

	public String getTag() {
		return tag;
	}

	public String getDesc() {
		return desc;
	}
}
