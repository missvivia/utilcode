package com.xyl.mmall.mobile.ios.facade.pageView.cartList;

import java.math.BigDecimal;
import java.util.List;

public class IosCartStore {

	private long storeId;
	private String storeName;
	private BigDecimal storeBatchCash;
	private List<IosCartItem> cartItems;
	
	public List<IosCartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<IosCartItem> cartItems) {
		this.cartItems = cartItems;
	}
	public long getStoreId() {
		return storeId;
	}
	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public BigDecimal getStoreBatchCash() {
		return storeBatchCash;
	}
	public void setStoreBatchCash(BigDecimal storeBatchCash) {
		this.storeBatchCash = storeBatchCash;
	}
	
	
	
}
