/*
 * @(#) 2014-11-19
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * UserReceiveRecord.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-11-19
 * @since 1.0
 */
@SuppressWarnings("serial")
@AnnonOfClass(desc = "红包领取记录", tableName = "Mmall_Promotion_UserReceiveRecord", dbCreateTimeName = "CreateTime")
public class UserReceiveRecord implements Serializable {

	@AnnonOfField(primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户ID")
	private long userId;

	@AnnonOfField(desc = "红包ID", policy = true)
	private long redPacketId;

	@AnnonOfField(desc = "红包分组ID")
	private int groupId;

	@AnnonOfField(desc = "领取金额")
	private BigDecimal cash;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

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

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}
}
