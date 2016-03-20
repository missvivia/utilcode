/*
 * @(#) 2014-12-29
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * RedPacketRefundDetail.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-12-29
 * @since 1.0
 */
@SuppressWarnings("serial")
@AnnonOfClass(desc = "优惠券", tableName = "Mmall_Promotion_RedPacketRefundDetail", dbCreateTimeName = "CreateTime")
public class RedPacketRefundDetail implements Serializable {

	@AnnonOfField(desc = "订单id", primary = true, primaryIndex = 1)
	private long orderId;

	@AnnonOfField(desc = "包裹id", primary = true, primaryIndex = 2)
	private long packageId;

	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;

	@AnnonOfField(desc = "退红包金额", defa = "0.00")
	private BigDecimal cash;

	@AnnonOfField(desc = "不产金额", defa = "0.00")
	private BigDecimal compensateCash;

	@AnnonOfField(desc = "是否处理")
	private boolean refund;

	@AnnonOfField(desc = "是否补偿")
	private boolean compensate;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
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

	public boolean isRefund() {
		return refund;
	}

	public void setRefund(boolean refund) {
		this.refund = refund;
	}

	public boolean isCompensate() {
		return compensate;
	}

	public void setCompensate(boolean compensate) {
		this.compensate = compensate;
	}

	public BigDecimal getCompensateCash() {
		return compensateCash;
	}

	public void setCompensateCash(BigDecimal compensateCash) {
		this.compensateCash = compensateCash;
	}

}
