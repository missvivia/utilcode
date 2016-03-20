/*
 * @(#)CouponOrderType.java 2014-4-25
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * CouponOrderType.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-4-25
 * @since      1.0
 */
public enum RedPacketOrderType implements AbstractEnumInterface<RedPacketOrderType> {
	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 使用红包
	 */
	USE_RED_PACKET(0, "使用红包"),
	/**
	 * 返红包
	 */
	RETURN_RED_PACKET(1, "返红包"),
	
	/**
	 * 红包抵扣邮费
	 */
	USE_RED_PACKET_FOR_EXPRESS(2, "红包抵扣邮费"),
	
	/**
	 * 红包使用与产品和快递费用
	 */
	USE_RED_PACKET_COMPOSE(3, "0和2组合使用"),
	
	/**
	 * 退货红包补偿
	 */
	RETURN_RED_PACKET_COMPOSE(4, "退货补偿红包");

	private final int value;

	private final String desc;

	private RedPacketOrderType(int v, String d) {
		value = v;
		desc = d;
	}

	public RedPacketOrderType genEnumByIntValue(int intValue) {
		for (RedPacketOrderType item : RedPacketOrderType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}
