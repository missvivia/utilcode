package com.xyl.mmall.order.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum OperateLogType implements AbstractEnumInterface<OperateLogType>{
	
	ORDER_STATE(1,"订单状态"),
	
	ORDER_CASH(2,"订单金额"),
	
	ORDER_ADDRESS(3,"收货地址"),
	
	ORDER_INVOICE(4,"发票"),
	
	ORDER_LOGISTICS(5,"物流"),
	
	ORDER_COMMENT(6,"备注"),
	
	ORDER_PAY(7,"支付");
	

	/** 值 */
	private final int value;

	/** 描述 */
	private final String desc;
	
	private OperateLogType(int value,String desc){
		this.value = value;
		this.desc = desc;
	}

	@Override
	public OperateLogType genEnumByIntValue(int paramInt) {
		for (OperateLogType item : OperateLogType.values()) {
			if (item.value == paramInt)
				return item;
		}
		return null;
	}

	@Override
	public int getIntValue() {
		return value;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
	
	
	
}
