/*
 * @(#) 2014-10-13
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * RedPacketWayType.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-13
 * @since      1.0
 */
public enum RedPacketWayType implements AbstractEnumInterface<RedPacketWayType> {
		/**
		 * NULL
		 */
		NULL(-1, "NULL"),
		
		/**
		 * 推广红包，用户主动领取
		 */
		RED_PACKET_PROMOTION(0, "推广红包"),
		
		/**
		 * 返利红包
		 */
		RED_PACKET_REBATE(0, "返利红包"),
		/**
		 * 邮费补贴红包
		 */
		RED_PACKET_EXP_BONUS(1, "邮费补贴红包");

		private final int value;

		private final String desc;

		private RedPacketWayType(int v, String d) {
			value = v;
			desc = d;
		}

		public RedPacketWayType genEnumByIntValue(int intValue) {
			for (RedPacketWayType item : RedPacketWayType.values()) {
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
