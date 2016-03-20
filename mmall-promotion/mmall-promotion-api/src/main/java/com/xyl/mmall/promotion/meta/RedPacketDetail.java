/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * RedPacketDetail.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-10
 * @since 1.0
 */
@AnnonOfClass(desc = "红包详情", tableName = "Mmall_Promotion_RedPacketDetail", dbCreateTimeName = "CreateTime")
public class RedPacketDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键ID", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "红包ID", policy = true)
	private long redPacketId;

	@AnnonOfField(desc = "红包金额")
	private BigDecimal cash;

	@AnnonOfField(desc = "分数，如果是0，表示已经领取完")
	private int copies;

	@AnnonOfField(desc = "有效期开始时间")
	private long validStartTime;

	@AnnonOfField(desc = "有效期结束时间")
	private long validEndTime;

	@AnnonOfField(desc = "组别id")
	private int groupId;

	@AnnonOfField(desc = "是否可见", defa = "1")
	private boolean visible;

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

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	public long getValidStartTime() {
		return validStartTime;
	}

	public void setValidStartTime(long validStartTime) {
		this.validStartTime = validStartTime;
	}

	public long getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(long validEndTime) {
		this.validEndTime = validEndTime;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
