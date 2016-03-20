/*
 * @(#) 2014-12-3
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.cms.vo.finance;

import java.math.BigDecimal;

/**
 * FullPayMetaVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-3
 * @since      1.0
 */
public class FinanceFullPayMetaVO {
	
	/**
	 * po单号
	 */
	private long poId;
	
	/**
	 * 下线时间
	 */
	private String offLineTime;
	
	/**
	 * 平牌名称
	 */
	private String brandName;
	
	/**
	 * 仓库名称
	 */
	private String warehouseName;
	
	/**
	 * 来货数量
	 */
	private int inCount;
	
	/**
	 * 退货数量
	 */
	private int refundCount;
	
	/**
	 * 实收数量
	 */
	private int actualCount;
	
	/**
	 * 采购金额
	 */
	private BigDecimal purchaseAmount = BigDecimal.ZERO;
	
	/**
	 * 分成比例
	 */
	private BigDecimal shareRatio = BigDecimal.ZERO;
	
	/**
	 * 活动折扣金额
	 */
	private BigDecimal promotionDiscountAmount = BigDecimal.ZERO;
	
	/**
	 * 优惠券折扣金额
	 */
	private BigDecimal couponDiscountAmount = BigDecimal.ZERO;
	
	/**
	 * 红包折扣金额
	 */
	private BigDecimal redPacketDiscountAmount = BigDecimal.ZERO;
	
	/**
	 * 来货退货差异
	 */
	private int inRefundDiffer;
	
	/**
	 * 应付金额
	 */
	private BigDecimal shouldPayAmount = BigDecimal.ZERO;
	
	/**
	 * 预付金额
	 */
	private BigDecimal prepayAmount = BigDecimal.ZERO;
	
	/**
	 * 已付金额
	 */
	private BigDecimal hasPayAmount = BigDecimal.ZERO;
	
	/**
	 * 实付金额
	 */
	private BigDecimal realPayAmount = BigDecimal.ZERO;

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public String getOffLineTime() {
		return offLineTime;
	}

	public void setOffLineTime(String offLineTime) {
		this.offLineTime = offLineTime;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public int getInCount() {
		return inCount;
	}

	public void setInCount(int inCount) {
		this.inCount = inCount;
	}

	public int getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(int refundCount) {
		this.refundCount = refundCount;
	}

	public int getActualCount() {
		return actualCount;
	}

	public void setActualCount(int actualCount) {
		this.actualCount = actualCount;
	}

	public BigDecimal getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(BigDecimal purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

	public BigDecimal getShareRatio() {
		return shareRatio;
	}

	public void setShareRatio(BigDecimal shareRatio) {
		this.shareRatio = shareRatio;
	}

	public BigDecimal getPromotionDiscountAmount() {
		return promotionDiscountAmount;
	}

	public void setPromotionDiscountAmount(BigDecimal promotionDiscountAmount) {
		this.promotionDiscountAmount = promotionDiscountAmount;
	}

	public BigDecimal getCouponDiscountAmount() {
		return couponDiscountAmount;
	}

	public void setCouponDiscountAmount(BigDecimal couponDiscountAmount) {
		this.couponDiscountAmount = couponDiscountAmount;
	}

	public BigDecimal getRedPacketDiscountAmount() {
		return redPacketDiscountAmount;
	}

	public void setRedPacketDiscountAmount(BigDecimal redPacketDiscountAmount) {
		this.redPacketDiscountAmount = redPacketDiscountAmount;
	}

	public int getInRefundDiffer() {
		return inRefundDiffer;
	}

	public void setInRefundDiffer(int inRefundDiffer) {
		this.inRefundDiffer = inRefundDiffer;
	}

	public BigDecimal getShouldPayAmount() {
		return shouldPayAmount;
	}

	public void setShouldPayAmount(BigDecimal shouldPayAmount) {
		this.shouldPayAmount = shouldPayAmount;
	}

	public BigDecimal getPrepayAmount() {
		return prepayAmount;
	}

	public void setPrepayAmount(BigDecimal prepayAmount) {
		this.prepayAmount = prepayAmount;
	}

	public BigDecimal getHasPayAmount() {
		return hasPayAmount;
	}

	public void setHasPayAmount(BigDecimal hasPayAmount) {
		this.hasPayAmount = hasPayAmount;
	}

	public BigDecimal getRealPayAmount() {
		return realPayAmount;
	}

	public void setRealPayAmount(BigDecimal realPayAmount) {
		this.realPayAmount = realPayAmount;
	}
}
