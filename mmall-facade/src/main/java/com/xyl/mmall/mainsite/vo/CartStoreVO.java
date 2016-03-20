package com.xyl.mmall.mainsite.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.netease.print.common.util.CollectionUtil;

/**
 * 对应购物车一个店铺购物记录
 * 
 * @author lhp
 *
 */
public class CartStoreVO implements Comparable<CartStoreVO>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7642154826254443008L;

	private List<CartSkuItemVO> skulist = new ArrayList<CartSkuItemVO>();

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

	@Override
	public int compareTo(CartStoreVO another) {
		if (this == another)
			return 0;
		if (CollectionUtil.isEmptyOfList(this.skulist))
			return -1;
		if (CollectionUtil.isEmptyOfList(another.skulist))
			return 1;
		CartSkuItemVO thisMax = this.skulist.get(0);
		CartSkuItemVO anotherMax = another.skulist.get(0);
		if (thisMax.getCreateTime() < anotherMax.getCreateTime()) {
			return 1;
		} else if (thisMax.getCreateTime() > anotherMax.getCreateTime()) {
			return -1;
		}
		return 0;
	}

}
