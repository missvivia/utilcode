/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * DistributeRule.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public enum DistributeRule implements AbstractEnumInterface<DistributeRule> {
	
	NULL(0, ""), EQUALLY(1, "平分"), RANDOM(2, "随机");
	
	private int type;
	
	private String desc;
	
	DistributeRule(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	@Override
	public int getIntValue() {
		return type;
	}

	@Override
	public DistributeRule genEnumByIntValue(int intValue) {
		for (DistributeRule distributeRule : DistributeRule.values()) {
			if (distributeRule.getType() == intValue) {
				return distributeRule;
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
