package com.xyl.mmall.mainsite.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Yang,Nan
 * 购物车信息
 */
public class CartInfoVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3973244752882209771L;

	/**
	 * 购物车剩余时间毫秒数
	 */
	private long leftTime;
	
	private transient long updateTime;
	
	private boolean isFree;
	
	private BigDecimal totalPrice;
	
	private String disCountUrl;
	
	/**
	 * 商品数
	 */
	private int productNum;
	
	/**
	 * 店铺数
	 */
	private int storeNum;

	public long getLeftTime() {
		return leftTime;
	}

	public void setLeftTime(long leftTime) {
		this.leftTime = leftTime;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getDisCountUrl() {
		return disCountUrl;
	}

	public void setDisCountUrl(String disCountUrl) {
		this.disCountUrl = disCountUrl;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getProductNum() {
		return productNum;
	}

	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}

	public int getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(int storeNum) {
		this.storeNum = storeNum;
	}
	
	
	
}
