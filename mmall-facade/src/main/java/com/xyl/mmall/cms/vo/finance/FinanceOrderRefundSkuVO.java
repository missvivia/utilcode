/*
 * @(#) 2014-12-11
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * FinanceOrderRefundSkuVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-11
 * @since      1.0
 */
@SuppressWarnings("serial")
public class FinanceOrderRefundSkuVO implements Serializable {
	
	private String productName;
	
	private long poId;
	
	private String categoryName;
	
	private BigDecimal salePrice = BigDecimal.ZERO;
	
	private BigDecimal retailPrice = BigDecimal.ZERO;
	
	private int refundCount;
	
	private BigDecimal refundCash = BigDecimal.ZERO;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
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

	public BigDecimal getRefundCash() {
		return refundCash;
	}

	public void setRefundCash(BigDecimal refundCash) {
		this.refundCash = refundCash;
	}
}
