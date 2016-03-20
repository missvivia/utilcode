/*
 * @(#) 2014-9-26
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * PlatformType.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-26
 * @since      1.0
 */
public enum PlatformType implements AbstractEnumInterface<PlatformType> {
	
	ALL(0, "所有"), MOBILE(1, "移动端"), PC(2, "PC端"), WAP(3, "WAP端");
	
	private int type;
	
	private String desc;
	
	PlatformType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	@Override
	public int getIntValue() {
		return type;
	}

	@Override
	public PlatformType genEnumByIntValue(int intValue) {
		for (PlatformType platformType : PlatformType.values()) {
			if (platformType.getType() == intValue) {
				return platformType;
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
