package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;
import com.xyl.mmall.order.constant.ConstValueOfOrder;

/**
 * 退货状态
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:08:11
 * 
 */
@Deprecated
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeprecatedReturnState implements AbstractEnumInterface<DeprecatedReturnState> {

	/** 未申请(初始化) */
	INIT(4, "未申请", "未申请(初始化)"),

	/** 用户发起申请 */
	APPLY_RETURN(5, "已申请", "客户发起退货请求"),

	/** 等待仓库确认收货入仓 */
	WAITING_CONFIRM(6, "待收货", "等待仓库确认收货入仓"),

	/**
	 * 等待客服审核是否可以退款 WAITING_RETURN_AUDIT(7, "待退款", "等待客服审核是否可以退款"),
	 */

	/** 客服操作退款 */
	FINISH_RETURN(8, "已退款", "客服操作退款"),

	/** 客服操作拒绝退款 */
	REFUSED(9, "拒绝", "客服操作拒绝退款"),

	/** 系统取消 */
	CANCELED(10, "已取消", ConstValueOfOrder.SEP_RP_APPLY_DAY + "天未收到包裹，系统自动取消"),

	/** 异常退货情况 */
	ABNOMAL(11, "异常件待退款", "仓库收货数据与用户申请数据不一致");

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
	private DeprecatedReturnState(int v, String t, String d) {
		value = v;
		tag = t;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public DeprecatedReturnState genEnumByIntValue(int intValue) {
		for (DeprecatedReturnState item : DeprecatedReturnState.values()) {
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
