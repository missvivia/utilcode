/*
 * @(#) 2014-12-4
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * FullPayDetailMetaVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-4
 * @since      1.0
 */
@SuppressWarnings("serial")
public class FinanceFullPayDetailMetaVO implements Serializable {
	
	/**
	 * poid
	 */
	private long poId;
	
	/**
	 * 供应商编号
	 */
	private long supplierNo;
	
	/**
	 * 公司名称
	 */
	private String companyName;
	
	/**
	 * 商标名称
	 */
	private String brandName;
	
	/**
	 * 上线时间
	 */
	private String onlineDate;
	
	/**
	 * 下线时间
	 */
	private String offlineDate;
	
	/**
	 * 订单id
	 */
	private long orderId;
	
	/**
	 * skuid
	 */
	private String barcode;
	
	/**
	 * 货号
	 */
	private String goodsNo;
	
	/**
	 * 零售价
	 */
	private BigDecimal retailPrice;
	
	/**
	 * 购买数量
	 */
	private int count;
	
	/**
	 * 零售总价
	 */
	private BigDecimal retailTotalPrice;
	
	/**
	 * 活动折扣单价
	 */
	private BigDecimal promotionDiscountAmount;
	
	/**
	 * 活动折扣总价
	 */
	private BigDecimal promotionTotalDiscountAmount;
	
	/**
	 * 供应商活动承担比例
	 */
	private BigDecimal supplierPromotionShareRatio;
	
	/**
	 * 平台活动承担比例
	 */
	private BigDecimal platformPromotionShareRatio;
	
	/**
	 * 红包折扣单价
	 */
	private BigDecimal redPacketDiscountAmount;
	
	/**
	 * 红包折扣总价
	 */
	private BigDecimal redPacketTotalDiscountAmount;
	
	/**
	 * 供应商红包承担比例
	 */
	private BigDecimal supplierRedPacketShareRatio = BigDecimal.ZERO;
	
	/**
	 * 平台红包承担比例
	 */
	private BigDecimal platformRedPacketShareRatio = new BigDecimal(100);
	
	/**
	 * 优惠券折扣单价
	 */
	private BigDecimal couponDiscountAmount;
	
	/**
	 * 优惠券折扣总价
	 */
	private BigDecimal couponTotalDiscountAmount;
	
	/**
	 * 供应商优惠券承担比率
	 */
	private BigDecimal supplierCouponShareRatio = BigDecimal.ZERO;
	
	/**
	 * 平台优惠券承担比率
	 */
	private BigDecimal platformCouponShareRatio =  new BigDecimal(100);
	
	/**
	 * 平台承担总价
	 */
	private BigDecimal platformDiscountAmount;
	
	/**
	 * 供应商承担总价
	 */
	private BigDecimal supplierDiscountAmount;
	
	/**
	 * 活动描述
	 */
	private String desc;

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public long getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(long supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getOnlineDate() {
		return onlineDate;
	}

	public void setOnlineDate(String onlineDate) {
		this.onlineDate = onlineDate;
	}

	public String getOfflineDate() {
		return offlineDate;
	}

	public void setOfflineDate(String offlineDate) {
		this.offlineDate = offlineDate;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BigDecimal getRetailTotalPrice() {
		return retailTotalPrice;
	}

	public void setRetailTotalPrice(BigDecimal retailTotalPrice) {
		this.retailTotalPrice = retailTotalPrice;
	}

	public BigDecimal getPromotionDiscountAmount() {
		return promotionDiscountAmount;
	}

	public void setPromotionDiscountAmount(BigDecimal promotionDiscountAmount) {
		this.promotionDiscountAmount = promotionDiscountAmount;
	}

	public BigDecimal getPromotionTotalDiscountAmount() {
		return promotionTotalDiscountAmount;
	}

	public void setPromotionTotalDiscountAmount(BigDecimal promotionTotalDiscountAmount) {
		this.promotionTotalDiscountAmount = promotionTotalDiscountAmount;
	}

	public BigDecimal getSupplierPromotionShareRatio() {
		return supplierPromotionShareRatio;
	}

	public void setSupplierPromotionShareRatio(BigDecimal supplierPromotionShareRatio) {
		this.supplierPromotionShareRatio = supplierPromotionShareRatio;
	}

	public BigDecimal getPlatformPromotionShareRatio() {
		return platformPromotionShareRatio;
	}

	public void setPlatformPromotionShareRatio(BigDecimal platformPromotionShareRatio) {
		this.platformPromotionShareRatio = platformPromotionShareRatio;
	}

	public BigDecimal getRedPacketDiscountAmount() {
		return redPacketDiscountAmount;
	}

	public void setRedPacketDiscountAmount(BigDecimal redPacketDiscountAmount) {
		this.redPacketDiscountAmount = redPacketDiscountAmount;
	}

	public BigDecimal getRedPacketTotalDiscountAmount() {
		return redPacketTotalDiscountAmount;
	}

	public void setRedPacketTotalDiscountAmount(BigDecimal redPacketTotalDiscountAmount) {
		this.redPacketTotalDiscountAmount = redPacketTotalDiscountAmount;
	}

	public BigDecimal getSupplierRedPacketShareRatio() {
		return supplierRedPacketShareRatio;
	}

	public void setSupplierRedPacketShareRatio(BigDecimal supplierRedPacketShareRatio) {
		this.supplierRedPacketShareRatio = supplierRedPacketShareRatio;
	}

	public BigDecimal getPlatformRedPacketShareRatio() {
		return platformRedPacketShareRatio;
	}

	public void setPlatformRedPacketShareRatio(BigDecimal platformRedPacketShareRatio) {
		this.platformRedPacketShareRatio = platformRedPacketShareRatio;
	}

	public BigDecimal getCouponDiscountAmount() {
		return couponDiscountAmount;
	}

	public void setCouponDiscountAmount(BigDecimal couponDiscountAmount) {
		this.couponDiscountAmount = couponDiscountAmount;
	}

	public BigDecimal getCouponTotalDiscountAmount() {
		return couponTotalDiscountAmount;
	}

	public void setCouponTotalDiscountAmount(BigDecimal couponTotalDiscountAmount) {
		this.couponTotalDiscountAmount = couponTotalDiscountAmount;
	}

	public BigDecimal getSupplierCouponShareRatio() {
		return supplierCouponShareRatio;
	}

	public void setSupplierCouponShareRatio(BigDecimal supplierCouponShareRatio) {
		this.supplierCouponShareRatio = supplierCouponShareRatio;
	}

	public BigDecimal getPlatformCouponShareRatio() {
		return platformCouponShareRatio;
	}

	public void setPlatformCouponShareRatio(BigDecimal platformCouponShareRatio) {
		this.platformCouponShareRatio = platformCouponShareRatio;
	}

	public BigDecimal getPlatformDiscountAmount() {
		return platformDiscountAmount;
	}

	public void setPlatformDiscountAmount(BigDecimal platformDiscountAmount) {
		this.platformDiscountAmount = platformDiscountAmount;
	}

	public BigDecimal getSupplierDiscountAmount() {
		return supplierDiscountAmount;
	}

	public void setSupplierDiscountAmount(BigDecimal supplierDiscountAmount) {
		this.supplierDiscountAmount = supplierDiscountAmount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
