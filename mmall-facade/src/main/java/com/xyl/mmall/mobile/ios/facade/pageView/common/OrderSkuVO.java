package com.xyl.mmall.mobile.ios.facade.pageView.common;

import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * OrderCartItem上显示用的基本单位
 * 
 * @author dingmingliang
 * 
 */
public class OrderSkuVO {

	@AnnonOfField(desc = "专柜价(单位)")
	private BigDecimal marketPrice;

	@AnnonOfField(desc = "原始价格/闪购价(单价)")
	private BigDecimal oriRPrice;

	@AnnonOfField(desc = "最终零售价格(单位)")
	private BigDecimal rprice;

	@AnnonOfField(desc = "活动优惠的差额(单位)")
	private BigDecimal hdSPrice;

	@AnnonOfField(desc = "优惠券优惠的差额(单位)")
	private BigDecimal couponSPrice;

	/**
	 * 最终零售价格(总和)
	 */
	private BigDecimal totalRPrice;

	/**
	 * 活动优惠+优惠券优惠的差额(总和)
	 */
	private BigDecimal totalSPrice;

	@AnnonOfField(desc = "总数量")
	private int totalCount;

	@AnnonOfField(desc = "产品Id")
	private long productId;

	@AnnonOfField(desc = "skuId")
	private long skuId;
	
	@AnnonOfField(desc = "产品的供应商Id")
	private long supplierId;

	/**
	 * Sku的快照信息
	 */
	private SkuSPVO skuSPVO;	

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getTotalRPrice() {
		return totalRPrice;
	}

	public void setTotalRPrice(BigDecimal totalRPrice) {
		this.totalRPrice = totalRPrice;
	}

	public BigDecimal getTotalSPrice() {
		return totalSPrice;
	}

	public void setTotalSPrice(BigDecimal totalSPrice) {
		this.totalSPrice = totalSPrice;
	}

	public SkuSPVO getSkuSPVO() {
		return skuSPVO;
	}

	public void setSkuSPVO(SkuSPVO skuSPVO) {
		this.skuSPVO = skuSPVO;
	}

	public BigDecimal getOriRPrice() {
		return oriRPrice;
	}

	public void setOriRPrice(BigDecimal oriRPrice) {
		this.oriRPrice = oriRPrice;
	}

	public BigDecimal getRprice() {
		return rprice;
	}

	public void setRprice(BigDecimal rprice) {
		this.rprice = rprice;
	}

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

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
	
	
}