package com.xyl.mmall.order.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 用户申请退货/系统或客服退款操作时，与订单退货数量关联的的状态
 * 
 * 需求：订单中所有包裹的全部商品均退货成功，则订单已使用的优惠券退回给用户
 * 
 * 实现：1. 用户申请退货时更新状态（ReturnForm.applyedNumState）
 * 	   2. 系统或客服退款操作时更新状态（ReturnForm.confirmedNumState）
 * 	   3. 定时器扫描（ReturnForm.confirmedNumState）
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月23日 下午8:12:50
 *
 */
public enum ReturnOrderSkuNumState implements AbstractEnumInterface<ReturnOrderSkuNumState> {
	
	APPLY_INIT(0, "申请 - 初始化状态", true), 
	
	APPLY_PACKAGE_PART_RETURN(1, "申请 - 有包裹部分退货", true), 
	
	APPLY_PACKAGE_FULL_RETURN(2, "申请 - 包裹（非订单中的所有包裹）全部退货", true), 
	
	APPLY_ORDER_FULL_RETURN(3, "申请 - 包裹（订单中的所有包裹）全部退货", true), 
	
	CONFIRM_INIT(4, "退款 - 初始化状态", false), 
	
	CONFIRM_PACKAGE_PART_RETURN(5, "退款 - 有包裹部分退货", false), 
	
	CONFIRM_PACKAGE_FULL_RETURN(6, "退款 - 包裹（非订单中的所有包裹）全部退货", false), 
	
	CONFIRM_ORDER_FULL_RETURN(7, "退款 - 包裹（订单中的所有包裹）全部退货", false);

	/** 值 */
	private final int value;

	/** 标签 */
	private final String tag;
	
	/** 是否是申请阶段 */
	private final boolean applySituation;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param t
	 * @param d
	 * @param name
	 */
	private ReturnOrderSkuNumState(int v, String t, boolean isApply) {
		value = v;
		tag = t;
		applySituation = isApply;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}

	@Override
	public ReturnOrderSkuNumState genEnumByIntValue(int intValue) {
		for(ReturnOrderSkuNumState state : ReturnOrderSkuNumState.values()) {
			if(intValue == state.value) {
				return state;
			}
		}
		return null;
	}

	public String getTag() {
		return tag;
	}

	public boolean isApplySituation() {
		return applySituation;
	}

}
