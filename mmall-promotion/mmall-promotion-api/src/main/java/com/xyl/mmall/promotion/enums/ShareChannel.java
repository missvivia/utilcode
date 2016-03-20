/*
 * @(#) 2014-11-5
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * ShareChannel.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-11-5
 * @since      1.0
 */
public enum ShareChannel implements AbstractEnumInterface<ShareChannel> {
	
	BY_ORDER(1, "下单")
	
	;

	private final int value;

	private final String desc;

	private ShareChannel(int v, String d) {
		value = v;
		desc = d;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}

	@Override
	public ShareChannel genEnumByIntValue(int intValue) {
		for (ShareChannel channel : ShareChannel.values()) {
			if (channel.getIntValue() == intValue) {
				return channel;
			}
		}
		return null;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

}
