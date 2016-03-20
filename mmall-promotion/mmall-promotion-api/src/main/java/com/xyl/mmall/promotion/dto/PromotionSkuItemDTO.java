/*
 * 2014-9-25
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * PromotionSkuItemDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-25
 * @since      1.0
 */
public class PromotionSkuItemDTO implements Comparable<PromotionSkuItemDTO>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * skuid
	 */
	private long skuId;
	
	/**
	 * 用户ID
	 */
	private long userId;
	
	/**
	 * 购买数量
	 */
	private int count;
	
	/**
	 * 商家Id
	 */
	private long businessId;
	
	/**
	 * 原始零售价
	 */
	private BigDecimal oriRetailPrice = BigDecimal.ZERO;
	
	/**
	 * 活动计算后的均摊价，初始状态设置和oriRetailPrice一样
	 */
	private BigDecimal retailPrice = BigDecimal.ZERO;
	
	/**
	 *  购物车显示价 初始状态设置和oriRetailPrice一样
	 */
	private BigDecimal cartPrice = BigDecimal.ZERO;
	
	/**
	 * ----------以下计算结果输出----------
	 */
	
	/**
	 * 活动优惠金额
	 */
	private BigDecimal promotionDiscountAmount = BigDecimal.ZERO;
	
	/**
	 * 优惠券优惠金额
	 */
	private BigDecimal couponDiscountAmount = BigDecimal.ZERO;
	
	/**
	 * 红包使用金额
	 */
	private BigDecimal hbPrice = BigDecimal.ZERO;
	
	   /** 店铺订单起批金额 . */
    private BigDecimal batchCash;

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public BigDecimal getCartPrice() {
		return cartPrice;
	}

	public void setCartPrice(BigDecimal cartPrice) {
		this.cartPrice = cartPrice;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public BigDecimal getOriRetailPrice() {
		return oriRetailPrice;
	}

	public void setOriRetailPrice(BigDecimal oriRetailPrice) {
		this.oriRetailPrice = oriRetailPrice;
	}

	public BigDecimal getHbPrice() {
		return hbPrice;
	}

	public void setHbPrice(BigDecimal hbPrice) {
		this.hbPrice = hbPrice;
	}
	
	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	@Override
	public int compareTo(PromotionSkuItemDTO o) {
		if (this.getCount() > o.getCount()) {
			return -1;
		} else if (this.getCount() < o.getCount()) {
			return 1;
		}
		return 0;
	}

	
    public BigDecimal getBatchCash()
    {
        return batchCash;
    }
    
    public void setBatchCash(BigDecimal batchCash)
    {
        this.batchCash = batchCash;
    }
	
}
