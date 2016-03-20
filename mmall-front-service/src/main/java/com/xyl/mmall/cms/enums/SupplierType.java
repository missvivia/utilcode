package com.xyl.mmall.cms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum SupplierType implements AbstractEnumInterface<SupplierType>{

	MANUFACTURER(0, "生产厂家"), AGENT(1, "代理商"),WHOLESALER(2, "综合批发商"),SPECIALMANAGE(3, "特许经营");

	private final int value;

	private final String desc;
	
	public String getDesc() {
		return desc;
	}

	private SupplierType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	@Override
	public SupplierType genEnumByIntValue(int paramInt) {
		for (SupplierType item : SupplierType.values()) {
			if (item.value == paramInt)
				return item;
		}
		return null;
	}

	@Override
	public int getIntValue() {
		return value;
	}
	
	public static String getDescByInt(int intValue){
		for (SupplierType item : SupplierType.values()) {
			if (item.value == intValue)
				return item.getDesc();
		}
		return null;
	}

}
