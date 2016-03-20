/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * BinderType.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
public enum BinderType implements AbstractEnumInterface<BinderType> {

	USER_BINDER(1, "导入用户绑定"), 
	
	SYSTEM_BINDER(2, "系统自动绑定"), 
	
	DISTRIBUTE_BINDER(3, "绑定分发"),
	
	NULL(0, "不绑定");
	
	private int type;
	
	private String desc;
	
	BinderType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	@Override
	public int getIntValue() {
		return type;
	}

	@Override
	public BinderType genEnumByIntValue(int intValue) {
		for (BinderType codeType : BinderType.values()) {
			if (codeType.getType() == intValue) {
				return codeType;
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
