package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;
import com.xyl.mmall.order.jackson.OperateUserTypeJsonDeserializer;

/**
 * 操作订单用户类型
 * @author author:lhp
 *
 * @version date:2015年5月26日下午5:08:20
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = OperateUserTypeJsonDeserializer.class)
public enum OperateUserType implements AbstractEnumInterface<OperateUserType>{
	
	SYSTEMER(0,"系统"),
	
	CMSER(1,"cms管理员"),
	
	BACKERNDER(2, "backend管理员"), 
	
	USER(3, "用户"), 
	
	ERP(4, "ERP");
	
	/** 值 */
	private final int value;

	/** 描述 */
	private final String desc;
	
	private OperateUserType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public OperateUserType genEnumByIntValue(int paramInt) {
		for (OperateUserType item : OperateUserType.values()) {
			if (item.value == paramInt)
				return item;
		}
		return null;
	}

	@Override
	public int getIntValue() {
		return value;
	}

}
