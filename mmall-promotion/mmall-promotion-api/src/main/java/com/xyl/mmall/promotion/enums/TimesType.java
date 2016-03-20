/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * TimesType.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-10
 * @since      1.0
 */
public enum TimesType implements AbstractEnumInterface<TimesType> {
	
	TOTAL(1, "总次数"), EACH(2, "每个次数");
	
	private int type;
	
	private String desc;
	
	TimesType(int type, String desc) {
		this.type = type;
		this.desc = desc;
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

	@Override
	public int getIntValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TimesType genEnumByIntValue(int intValue) {
		// TODO Auto-generated method stub
		return null;
	}

}
