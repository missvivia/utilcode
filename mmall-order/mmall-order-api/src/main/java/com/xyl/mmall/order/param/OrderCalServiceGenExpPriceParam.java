package com.xyl.mmall.order.param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author dingmingliang
 * 
 */
public class OrderCalServiceGenExpPriceParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20150129L;

	/**
	 * 用户Id
	 */
	private long userId;

	/**
	 * 用户购买的Sku信息
	 */
	private List<SkuParam> skuParamList;

	/**
	 * 邮费
	 */
	private BigDecimal expPrice;

	/**
	 * 活动是否包邮
	 */
	private boolean isFreeExpPrice;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<SkuParam> getSkuParamList() {
		return skuParamList;
	}

	public void setSkuParamList(List<SkuParam> skuParamList) {
		this.skuParamList = skuParamList;
	}

	public BigDecimal getExpPrice() {
		return expPrice;
	}

	public void setExpPrice(BigDecimal expPrice) {
		this.expPrice = expPrice;
	}

	public boolean isFreeExpPrice() {
		return isFreeExpPrice;
	}

	public void setFreeExpPrice(boolean isFreeExpPrice) {
		this.isFreeExpPrice = isFreeExpPrice;
	}
}
