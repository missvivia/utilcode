/*
 * @(#) 2014-12-6
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * RefundDetailMetaVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-6
 * @since      1.0
 */
@SuppressWarnings("serial")
public class FinanceRefundDetailMetaVO implements Serializable {
	
	/**
	 * po单id
	 */
	private long poId;
	
	/**
	 * 上线时间
	 */
	private String onlineDate;
	
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
	 * 商务助理
	 */
	private String assistant;
	
	/**
	 * 条码
	 */
	private String barcode;
	
	/**
	 * 货号
	 */
	private String goodsNo;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 正品价
	 */
	private BigDecimal marketPrice;
	
	/**
	 * 零售价
	 */
	private BigDecimal retailPrice;
	
	/**
	 * 退货数量
	 */
	private int refundCount;
	
	/**
	 * 退货签收时间
	 */
	private String signRefundDate;
	
	/**
	 * 接收仓库
	 */
	private String warehouse;
	
	/**
	 * 退货类型
	 */
	private String refundType;

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public String getOnlineDate() {
		return onlineDate;
	}

	public void setOnlineDate(String onlineDate) {
		this.onlineDate = onlineDate;
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

	public String getAssistant() {
		return assistant;
	}

	public void setAssistant(String assistant) {
		this.assistant = assistant;
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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public int getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(int refundCount) {
		this.refundCount = refundCount;
	}

	public String getSignRefundDate() {
		return signRefundDate;
	}

	public void setSignRefundDate(String signRefundDate) {
		this.signRefundDate = signRefundDate;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
	
	
}
