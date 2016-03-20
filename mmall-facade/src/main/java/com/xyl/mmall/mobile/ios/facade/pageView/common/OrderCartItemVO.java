package com.xyl.mmall.mobile.ios.facade.pageView.common;

import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 订单明细
 */
public class OrderCartItemVO {

	@AnnonOfField(desc = "销售价")
	private BigDecimal retailPrice;

	@AnnonOfField(desc = "原价")
	private BigDecimal originalPrice;

	@AnnonOfField(desc = "购买数量")
	private int count;
	
	/**
	 * 最终零售价格(总和)
	 */
	private BigDecimal totalRPrice;

	/**
	 * 活动优惠+优惠券优惠的差额(总和)
	 */
	private BigDecimal totalSPrice;

	/**
	 * 明细
	 */
	private List<OrderSkuVO> orderSkuList;	
	
	/**
	 * 店铺名称
	 */
	private String storeName;
	
	private long storeId;
	
	
	

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public List<OrderSkuVO> getOrderSkuList() {
		return orderSkuList;
	}

	public void setOrderSkuList(List<OrderSkuVO> orderSkuList) {
		this.orderSkuList = orderSkuList;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}