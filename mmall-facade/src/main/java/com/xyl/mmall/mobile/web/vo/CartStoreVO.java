package com.xyl.mmall.mobile.web.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 对应购物车一个店铺购物记录
 * @author lhp
 *
 */
public class CartStoreVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7642154826254443008L;

	private List<CartSkuItemVO> skulist = new ArrayList<CartSkuItemVO>();
	
	private long storeId;
	private String storeName;
	
	private String storeUrl;
	
	/**
	 * 店铺起批金额
	 */
	private BigDecimal storeBatchCash;
	
	private boolean isFree;
	
	private BigDecimal totalPrice;
	
	/**
	 * 同一店铺买的商品总数
	 */
	private int totalCount;
	
	public int getTotalCount() {
		return totalCount;
	}

	
	public long getStoreId() {
		return storeId;
	}


	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<CartSkuItemVO> getSkulist() {
		return skulist;
	}

	public void setSkulist(List<CartSkuItemVO> skulist) {
		this.skulist = skulist;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	public BigDecimal getStoreBatchCash() {
		return storeBatchCash;
	}

	public void setStoreBatchCash(BigDecimal storeBatchCash) {
		this.storeBatchCash = storeBatchCash;
	}
	
	
	
	

}
