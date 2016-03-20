package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 包裹退款总计(不包含订单取消和退货导致的退款)
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_OrderPackageRefund", desc = "包裹退款总计")
public class OrderPackageRefund implements Serializable {

	private static final long serialVersionUID = 20150113L;

	@AnnonOfField(desc = "id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "包裹id")
	private long packageId;

	@AnnonOfField(desc = "订单id")
	private long orderId;

	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;

	@AnnonOfField(desc = "总金额")
	private BigDecimal totalCash = BigDecimal.ZERO;

	@AnnonOfField(desc = "红包金额")
	private BigDecimal redCash = BigDecimal.ZERO;

	@AnnonOfField(desc = "现金金额")
	private BigDecimal realCash = BigDecimal.ZERO;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public BigDecimal getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(BigDecimal totalCash) {
		this.totalCash = totalCash;
	}

	public BigDecimal getRedCash() {
		return redCash;
	}

	public void setRedCash(BigDecimal redCash) {
		this.redCash = redCash;
	}

	public BigDecimal getRealCash() {
		return realCash;
	}

	public void setRealCash(BigDecimal realCash) {
		this.realCash = realCash;
	}
}
