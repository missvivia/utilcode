/*
 * @(#) 2014-12-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * FinanceOrderRefundVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-10
 * @since      1.0
 */

@SuppressWarnings("serial")
public class FinanceOrderRefundVO implements Serializable {
	
	private long orderId;
	
	private List<FinanceOrderRefundPackageVO> packageVOs = new ArrayList<>();
	
	
	private BigDecimal expressRefundCash = BigDecimal.ZERO;
	
	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public List<FinanceOrderRefundPackageVO> getPackageVOs() {
		return packageVOs;
	}

	public void setPackageVOs(List<FinanceOrderRefundPackageVO> packageVOs) {
		this.packageVOs = packageVOs;
	}

	public BigDecimal getExpressRefundCash() {
		return expressRefundCash;
	}

	public void setExpressRefundCash(BigDecimal expressRefundCash) {
		this.expressRefundCash = expressRefundCash;
	}

}
