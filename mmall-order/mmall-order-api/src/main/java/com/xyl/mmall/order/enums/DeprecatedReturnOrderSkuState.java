package com.xyl.mmall.order.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 仓库收到的退货中OrderSku的状态
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月23日 下午8:12:50
 *
 */
@Deprecated
public enum DeprecatedReturnOrderSkuState implements AbstractEnumInterface<DeprecatedReturnOrderSkuState> {
	
	UNKNOWN(0, "未知行为", "当已收货数量>退货数量时"), 
	
	NOT_CONFIRMED(1, "未收货", "当已收货数量为0时"), 
	
	PART_CONFIRMED(2, "部分收货", "当已收货数量＜退货数量时"), 
	
	ALL_CONFIRMED(3, "已收货", "当已收货数量=退货数量时"), 
	
	RETURNED(4, "已退款", "客服已退款");

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
	private DeprecatedReturnOrderSkuState(int v, String t, String d) {
		value = v;
		tag = t;
		desc = d;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}

	@Override
	public DeprecatedReturnOrderSkuState genEnumByIntValue(int intValue) {
		for(DeprecatedReturnOrderSkuState state : DeprecatedReturnOrderSkuState.values()) {
			if(intValue == state.value) {
				return state;
			}
		}
		return null;
	}

	public String getTag() {
		return tag;
	}

	public String getDesc() {
		return desc;
	}
}
