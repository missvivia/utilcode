/*
 * @(#) 2014-12-29
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.promotion.meta.tcc.RedPacketOrderTCC;

/**
 * RedPacketParam.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-29
 * @since      1.0
 */
@SuppressWarnings("serial")
public class RedPacketParam implements Serializable {
	
	private List<SkuParam> skuParams;
	
	private BigDecimal usedRedPrice = BigDecimal.ZERO;
	
	private BigDecimal expressPrice = BigDecimal.ZERO;
	
	private BigDecimal expressDiscountPrice = BigDecimal.ZERO;
	
	private long userId;
	
	private int rpState;
	
	private BigDecimal realUsedRedPrice = BigDecimal.ZERO;
	
	private List<RedPacketOrderTCC> redPacketOrderTCCList = new ArrayList<>();
	
	public List<SkuParam> getSkuParams() {
		return skuParams;
	}

	public void setSkuParams(List<SkuParam> skuParams) {
		this.skuParams = skuParams;
	}

	public BigDecimal getUsedRedPrice() {
		return usedRedPrice;
	}

	public void setUsedRedPrice(BigDecimal usedRedPrice) {
		this.usedRedPrice = usedRedPrice;
	}

	public BigDecimal getExpressPrice() {
		return expressPrice;
	}

	public void setExpressPrice(BigDecimal expressPrice) {
		this.expressPrice = expressPrice;
	}

	public BigDecimal getExpressDiscountPrice() {
		return expressDiscountPrice;
	}

	public void setExpressDiscountPrice(BigDecimal expressDiscountPrice) {
		this.expressDiscountPrice = expressDiscountPrice;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getRpState() {
		return rpState;
	}

	public void setRpState(int rpState) {
		this.rpState = rpState;
	}

	public List<RedPacketOrderTCC> getRedPacketOrderTCCList() {
		return redPacketOrderTCCList;
	}

	public void setRedPacketOrderTCCList(List<RedPacketOrderTCC> redPacketOrderTCCList) {
		this.redPacketOrderTCCList = redPacketOrderTCCList;
	}

	public BigDecimal getRealUsedRedPrice() {
		return realUsedRedPrice;
	}

	public void setRealUsedRedPrice(BigDecimal realUsedRedPrice) {
		this.realUsedRedPrice = realUsedRedPrice;
	}

}
