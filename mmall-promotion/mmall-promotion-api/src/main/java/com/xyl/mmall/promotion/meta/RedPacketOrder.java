/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;

/**
 * RedPacketOrder.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
@AnnonOfClass(desc = "红包使用明细", tableName = "Mmall_Promotion_RedPacketOrder", dbCreateTimeName = "CreateTime")
public class RedPacketOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@AnnonOfField(primary = true, desc = "主键ID", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "订单ID")
	private long orderId;

	@AnnonOfField(desc = "用户Id")
	private long userId;
	
	@AnnonOfField(desc = "用户红包Id")
	private long userRedPacketId;
	
	@AnnonOfField(desc = "红包ID")
	private long redPacketId;
	
	@AnnonOfField(desc = "使用金额")
	private BigDecimal cash =BigDecimal.ZERO;
	
	@AnnonOfField(desc = "订单优惠券类型")
	private RedPacketOrderType redPacketOrderType = RedPacketOrderType.USE_RED_PACKET;

	@AnnonOfField(desc = "处理情况")
	private ActivationHandlerType redPacketHandlerType = ActivationHandlerType.DEFAULT;
	
	@AnnonOfField(desc = "使用时间")
	private long usedTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getUserRedPacketId() {
		return userRedPacketId;
	}

	public void setUserRedPacketId(long userRedPacketId) {
		this.userRedPacketId = userRedPacketId;
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

	public RedPacketOrderType getRedPacketOrderType() {
		return redPacketOrderType;
	}

	public void setRedPacketOrderType(RedPacketOrderType redPacketOrderType) {
		this.redPacketOrderType = redPacketOrderType;
	}

	public ActivationHandlerType getRedPacketHandlerType() {
		return redPacketHandlerType;
	}

	public void setRedPacketHandlerType(ActivationHandlerType redPacketHandlerType) {
		this.redPacketHandlerType = redPacketHandlerType;
	}

	public long getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(long usedTime) {
		this.usedTime = usedTime;
	}
	
	public RedPacketOrder cloneObject() {
		RedPacketOrder ro = new RedPacketOrder();
		BeanUtils.copyProperties(this, ro);
		return ro;
	}
}
