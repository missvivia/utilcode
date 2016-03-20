/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * CouponType.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public enum CodeType implements AbstractEnumInterface<CodeType> {
	
	PUBLIC(1, "公码"), RANDOM(2, "随机券");
	
	
	private int type;
	
	private String desc;
	
	CodeType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	@Override
	public int getIntValue() {
		return type;
	}

	@Override
	public CodeType genEnumByIntValue(int intValue) {
		for (CodeType codeType : CodeType.values()) {
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
