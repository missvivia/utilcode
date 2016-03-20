/*
 * @(#) 2014-11-14
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * RedPacketLock.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-11-14
 * @since 1.0
 */
@AnnonOfClass(tableName = "Mmall_Promotion_RedPacketLock", desc = "红包-TCC事务锁")
public class RedPacketLock implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "红包id", primary = true, primaryIndex = 1, policy = true)
	private long redPacketId;

	@AnnonOfField(desc = "红包id", primary = true, primaryIndex = 2)
	private int groupId;

	public long getRedPacketId() {
		return redPacketId;
	}

	public void setRedPacketId(long redPacketId) {
		this.redPacketId = redPacketId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

}
