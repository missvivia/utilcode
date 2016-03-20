package com.xyl.mmall.promotion.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 组单使用的Sku信息(包含购买数量)
 * 
 * @author dingmingliang
 * 
 */
public class SkuParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * SkuId
	 */
	private long skuId;

	/**
	 * 销售价
	 */
	private BigDecimal salePrice;

	/**
	 * 活动优惠的差额(单位)
	 */
	private BigDecimal hdSPrice;

	/**
	 * 优惠券优惠的差额(单位)
	 */
	private BigDecimal couponSPrice;
	
	/**
	 * 红包优惠的差额(单位)
	 */
	private BigDecimal redSPrice;

	/**
	 * 购买个数
	 */
	private int count;

	// ---------------------------------

	/**
	 * 原始价格
	 */
	private BigDecimal oriRPrice;


	public BigDecimal getHdSPrice() {
		return hdSPrice;
	}

	public void setHdSPrice(BigDecimal hdSPrice) {
		this.hdSPrice = hdSPrice;
	}

	public BigDecimal getCouponSPrice() {
		return couponSPrice;
	}

	public void setCouponSPrice(BigDecimal couponSPrice) {
		this.couponSPrice = couponSPrice;
	}

	public BigDecimal getOriRPrice() {
		return oriRPrice;
	}

	public void setOriRPrice(BigDecimal oriRPrice) {
		this.oriRPrice = oriRPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getRedSPrice() {
		return redSPrice;
	}

	public void setRedSPrice(BigDecimal redSPrice) {
		this.redSPrice = redSPrice;
	}
}