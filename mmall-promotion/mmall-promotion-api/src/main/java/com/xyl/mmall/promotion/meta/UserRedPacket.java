/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.promotion.enums.RedPacketWayType;

/**
 * UserRedPacket.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-10
 * @since 1.0
 */
@AnnonOfClass(desc = "用户红包", tableName = "Mmall_Promotion_UserRedPacket", dbCreateTimeName = "CreateTime")
public class UserRedPacket implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键ID", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "红包详情ID")
	private long redPacketDetailId;

	@AnnonOfField(desc = "用户ID")
	private long userId;

	@AnnonOfField(desc = "红包金额")
	private BigDecimal cash;

	@AnnonOfField(desc = "使用金额")
	private BigDecimal usedCash;

	@AnnonOfField(desc = "剩余金额")
	private BigDecimal remainCash;

	@AnnonOfField(desc = "有效开始时间")
	private long validStartTime;

	@AnnonOfField(desc = "有效结束时间")
	private long validEndTime;

	@AnnonOfField(desc = "红包途径")
	private RedPacketWayType redPacketWayType = RedPacketWayType.RED_PACKET_PROMOTION;

	@AnnonOfField(desc = "状态", hasDefault = true, defa = "0")
	private int state;

	@AnnonOfField(desc = "是否可见", defa = "1")
	private boolean visible;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRedPacketDetailId() {
		return redPacketDetailId;
	}

	public void setRedPacketDetailId(long redPacketDetailId) {
		this.redPacketDetailId = redPacketDetailId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public BigDecimal getUsedCash() {
		return usedCash;
	}

	public void setUsedCash(BigDecimal usedCash) {
		this.usedCash = usedCash;
	}

	public BigDecimal getRemainCash() {
		return remainCash;
	}

	public void setRemainCash(BigDecimal remainCash) {
		this.remainCash = remainCash;
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

	public RedPacketWayType getRedPacketWayType() {
		return redPacketWayType;
	}

	public void setRedPacketWayType(RedPacketWayType redPacketWayType) {
		this.redPacketWayType = redPacketWayType;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isValid() {
		long time = System.currentTimeMillis();
		if (time >= validStartTime && time < validEndTime) {
			return true;
		}
		return false;
	}

	public UserRedPacket cloneObject() {
		UserRedPacket urp = new UserRedPacket();
		BeanUtils.copyProperties(this, urp);
		return urp;
	}
}
