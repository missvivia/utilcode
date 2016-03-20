/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.framework.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * PlatformType.java created by yydx811 at 2015年8月18日 下午3:20:58 
 * 平台类型
 * 
 * @author yydx811
 */
public enum PlatformType implements AbstractEnumInterface<PlatformType> {

	CMS(0, "cms管理员"),

	BACKERND(1, "backend管理员"),

	MAINSITE(2, "用户"),

	MOBILE(3, "手机"),
	
	WAP(4, "wap"),
	
	ERP(5, "erp"),
	
	PUSH(6, "地推");

	/** 值 */
	private final int value;

	/** 描述 */
	private final String desc;

	private PlatformType(int value, String desc) {
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
	public PlatformType genEnumByIntValue(int paramInt) {
		for (PlatformType item : PlatformType.values()) {
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
