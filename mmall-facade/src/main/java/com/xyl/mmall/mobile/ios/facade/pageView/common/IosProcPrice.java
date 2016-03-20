package com.xyl.mmall.mobile.ios.facade.pageView.common;

import java.math.BigDecimal;

public class IosProcPrice {

	private long priceId;
	private int minNum;
	private BigDecimal price;
	private int maxNum;
	
	public long getPriceId() {
		return priceId;
	}
	public void setPriceId(long priceId) {
		this.priceId = priceId;
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	public int getMinNum() {
		return minNum;
	}
	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	} 
	
	
	
}
