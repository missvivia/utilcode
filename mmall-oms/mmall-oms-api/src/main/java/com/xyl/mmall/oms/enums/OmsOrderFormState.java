package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * OMS订单的状态
 * 
 * @author zb
 * 
 */
public enum OmsOrderFormState implements AbstractEnumInterface<OmsOrderFormState> {

	OMSRECVFAILED(9, "OMS接收失败", "OMS接收失败"),

	TOSEND(10, "等待将订单推送给仓库", "等待将订单推送给仓库"), // 初始状态

	SENT(11, "成功将订单推送给仓库", "成功将订单推送给仓库"),

	RECVFAILED(12, "RECVFAILED", "接单失败"),

	/** 仓库打单 */
	RECVSUCCESS(13, "RECVSUCCESS", "仓库接单成功"),

	/** 仓库打单 */
	PRINT(14, "PRINT", "仓库打单"),

	/** 仓库拣货 */
	PICK(15, "PICK", "仓库拣货"),

	/** 仓库复核 */
	CHECK(16, "CHECK", "仓库复核"),

	/** 仓库打包 */
	PACKAGE(17, "PACKAGE", "仓库打包"),

	/** 仓库发货 */
	SHIP(18, "SHIP", "仓库反馈已经发货"),

	/** 取消发货 */
	CANCEL(19, "CANCEL", "取消发货"),

	/**
	 * 已通知上层的订单模块发货成功
	 */
	NOTICE_SHIP(20, "通知订单模块发货成功", "通知订单模块发货成功"),

	/**
	 * 未拣货就取消了
	 */
	UNPICK_CANCEL(21, "未拣货取消", "未拣货取消"),

	TIMEOUT_CANCEL(22, "超时取消", "超时取消"),

	OMSRECVFAILED_CANCEL(23, "oms接收失败取消", "oms接收失败取消");
	
	/**
	 * 已经生成捡货单推送给仓库的取消状态
	 */
	public static OmsOrderFormState[] PICKED_CANCEL = new OmsOrderFormState[]{CANCEL,TIMEOUT_CANCEL};
	
	/**
	 * 已经汇总到仓库的订单状态
	 */
	public static OmsOrderFormState[] COLLECT_WAREHOUSE = new OmsOrderFormState[]{SENT,RECVSUCCESS,PRINT,PICK,CHECK,PACKAGE,SHIP,CANCEL,NOTICE_SHIP
		,TIMEOUT_CANCEL,OMSRECVFAILED_CANCEL};
	
	/**
	 * 已发货给用户的订单状态
	 */
	public static OmsOrderFormState[] SHIPPED_USER = new OmsOrderFormState[]{SHIP,NOTICE_SHIP};
	
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
	private OmsOrderFormState(int v, String d, String name) {
		value = v;
		desc = d;
		this.name = name;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public OmsOrderFormState genEnumByIntValue(int value) {
		for (OmsOrderFormState item : OmsOrderFormState.values()) {
			if (item.value == value)
				return item;
		}
		return null;
	}

	public static OmsOrderFormState genEnumByDesc(String desc) {
		for (OmsOrderFormState item : OmsOrderFormState.values()) {
			if (item.desc.equals(desc))
				return item;
		}
		return null;
	}
	
	/**
	 * 是否是已经生成捡货单推送给仓库的取消状态
	 * @param state
	 * @return
	 */
	public static boolean isPickCancel (OmsOrderFormState state) {
		for(OmsOrderFormState tmp : PICKED_CANCEL) {
			if(tmp == state) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否是已经汇总到仓库的订单状态
	 * @param state
	 * @return
	 */
	public static boolean isCollectWarehouse(OmsOrderFormState state) {
		for(OmsOrderFormState tmp : COLLECT_WAREHOUSE) {
			if(tmp == state) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否是已发货给用户的订单状态
	 * @param state
	 * @return
	 */
	public static boolean isShippedUser(OmsOrderFormState state) {
		for(OmsOrderFormState tmp : SHIPPED_USER) {
			if(tmp == state) {
				return true;
			}
		}
		return false;
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
