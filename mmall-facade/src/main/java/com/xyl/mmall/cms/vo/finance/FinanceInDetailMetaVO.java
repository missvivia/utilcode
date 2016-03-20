/*
 * @(#) 2014-12-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * FinanceInDetailMetaVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-10
 * @since      1.0
 */
@SuppressWarnings("serial")
public class FinanceInDetailMetaVO implements Serializable {
	
	private long poId;
	
	private String onlineDate;
	
	private String offlineDate;
	
	private long supplierId;
	
	private String companyName;
	
	private String brandName;
	
	private String poFollowerUserName;
	
	private String barcode;
	
	private String goodsNo;
	
	private String goodsName;
	
	private BigDecimal marketPrice;
	
	private BigDecimal retailPrice;
	
	private int arrivedCount;
	
	private int receivableCount;
	
	private int refundCount;
	
	private int notNormalCount;
	
	private int realReceiveCount;
	
	private String warehouse;

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

	public String getOfflineDate() {
		return offlineDate;
	}

	public void setOfflineDate(String offlineDate) {
		this.offlineDate = offlineDate;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
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

	public String getPoFollowerUserName() {
		return poFollowerUserName;
	}

	public void setPoFollowerUserName(String poFollowerUserName) {
		this.poFollowerUserName = poFollowerUserName;
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

	public int getArrivedCount() {
		return arrivedCount;
	}

	public void setArrivedCount(int arrivedCount) {
		this.arrivedCount = arrivedCount;
	}

	public int getReceivableCount() {
		return receivableCount;
	}

	public void setReceivableCount(int receivableCount) {
		this.receivableCount = receivableCount;
	}

	public int getRealReceiveCount() {
		return realReceiveCount;
	}

	public void setRealReceiveCount(int realReceiveCount) {
		this.realReceiveCount = realReceiveCount;
	}

	public int getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(int refundCount) {
		this.refundCount = refundCount;
	}

	public int getNotNormalCount() {
		return notNormalCount;
	}

	public void setNotNormalCount(int notNormalCount) {
		this.notNormalCount = notNormalCount;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	
}
