/*
 * @(#) 2014-10-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * RefundDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-22
 * @since      1.0
 */
public class RefundDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 退款的po map
	 */
	private Map<Long, List<PromotionSkuItemDTO>> refundMap;
	
	/**
	 * 是否退回使用的优惠券
	 */
	private boolean isReturn;
	
	/**
	 * 退款总金额
	 */
	private BigDecimal refundTotalCash = BigDecimal.ZERO;
	
	/**
	 * 扣除活动优惠金额
	 */
	private BigDecimal promotionCash = BigDecimal.ZERO;
	
	/**
	 * 扣除优惠券优惠金额
	 */
	private BigDecimal couponCash = BigDecimal.ZERO;
	
	/**
	 * 总的原始零售价
	 */
	private BigDecimal totalOriCash = BigDecimal.ZERO;
	
	/**
	 * 是否包邮
	 */
	private boolean isExpFree;

	public Map<Long, List<PromotionSkuItemDTO>> getRefundMap() {
		return refundMap;
	}

	public void setRefundMap(Map<Long, List<PromotionSkuItemDTO>> refundMap) {
		this.refundMap = refundMap;
	}

	public boolean isReturn() {
		return isReturn;
	}

	public void setReturn(boolean isReturn) {
		this.isReturn = isReturn;
	}

	public BigDecimal getRefundTotalCash() {
		return refundTotalCash;
	}

	public void setRefundTotalCash(BigDecimal refundTotalCash) {
		this.refundTotalCash = refundTotalCash;
	}

	public BigDecimal getPromotionCash() {
		return promotionCash;
	}

	public void setPromotionCash(BigDecimal promotionCash) {
		this.promotionCash = promotionCash;
	}

	public BigDecimal getCouponCash() {
		return couponCash;
	}

	public void setCouponCash(BigDecimal couponCash) {
		this.couponCash = couponCash;
	}

	public BigDecimal getTotalOriCash() {
		return totalOriCash;
	}

	public void setTotalOriCash(BigDecimal totalOriCash) {
		this.totalOriCash = totalOriCash;
	}

	public boolean isExpFree() {
		return isExpFree;
	}

	public void setExpFree(boolean isExpFree) {
		this.isExpFree = isExpFree;
	}
}
