package com.xyl.mmall.mobile.ios.facade.param;

import java.math.BigDecimal;

import com.netease.print.daojar.meta.base.DDBParam;

public class CouponSearchParam extends DDBParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2185836367149663317L;

	private int state = -1;
	
	private BigDecimal grossPrice;
	

	public BigDecimal getGrossPrice() {
		return grossPrice;
	}

	public void setGrossPrice(BigDecimal grossPrice) {
		this.grossPrice = grossPrice;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
