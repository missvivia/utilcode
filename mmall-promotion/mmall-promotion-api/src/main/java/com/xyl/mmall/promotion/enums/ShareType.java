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
public enum ShareType implements AbstractEnumInterface<ShareType> {
	
	IS_SHARE(1, "分享"), NOT_SHARE(2, "非分享");
	
	private int type;
	
	private String desc;
	
	ShareType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	@Override
	public int getIntValue() {
		return type;
	}

	@Override
	public ShareType genEnumByIntValue(int intValue) {
		for (ShareType shareType : ShareType.values()) {
			if (shareType.getType() == intValue) {
				return shareType;
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
