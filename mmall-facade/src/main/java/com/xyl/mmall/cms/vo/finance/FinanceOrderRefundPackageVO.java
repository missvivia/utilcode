/*
 * @(#) 2014-12-11
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * FinanceOrderRefundPackageVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-11
 * @since      1.0
 */
@SuppressWarnings("serial")
public class FinanceOrderRefundPackageVO implements Serializable {
	
	private long packageId;
	
	private String refundReason;
	
	private String refundChannel;
	
	private String refundDate;
	
	private String tradeSerial;
	
	private List<FinanceOrderRefundSkuVO> skuVOs = new ArrayList<>();

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getRefundChannel() {
		return refundChannel;
	}

	public void setRefundChannel(String refundChannel) {
		this.refundChannel = refundChannel;
	}

	public String getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
	}

	public String getTradeSerial() {
		return tradeSerial;
	}

	public void setTradeSerial(String tradeSerial) {
		this.tradeSerial = tradeSerial;
	}

	public List<FinanceOrderRefundSkuVO> getSkuVOs() {
		return skuVOs;
	}

	public void setSkuVOs(List<FinanceOrderRefundSkuVO> skuVOs) {
		this.skuVOs = skuVOs;
	}
}
