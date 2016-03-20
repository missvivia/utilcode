/*
 * @(#) 2014-11-5
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.promotion.enums.ShareChannel;

/**
 * RedPacketShareRecord.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-11-5
 * @since 1.0
 */
@AnnonOfClass(desc = "红包分享记录", tableName = "Mmall_Promotion_RedPacketShareRecord", dbCreateTimeName = "CreateTime")
public class RedPacketShareRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "红包ID", policy = true)
	private long redPacketId;

	@AnnonOfField(desc = "红包分组ID")
	private int groupId;

	@AnnonOfField(desc = "分享渠道")
	private ShareChannel shareChannel;

	@AnnonOfField(desc = "分享渠道值", type = "varchar(50)")
	private String shareChannelValue;

	@AnnonOfField(desc = "红包开始生效时间")
	private long startTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public ShareChannel getShareChannel() {
		return shareChannel;
	}

	public void setShareChannel(ShareChannel shareChannel) {
		this.shareChannel = shareChannel;
	}

	public String getShareChannelValue() {
		return shareChannelValue;
	}

	public void setShareChannelValue(String shareChannelValue) {
		this.shareChannelValue = shareChannelValue;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}
