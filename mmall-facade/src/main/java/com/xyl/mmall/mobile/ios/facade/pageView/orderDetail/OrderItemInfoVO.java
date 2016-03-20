package com.xyl.mmall.mobile.ios.facade.pageView.orderDetail;

import java.util.List;

public class OrderItemInfoVO {
	
	
	private long orderId;
	
	private String storeName;
	
	private long storeId;
	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	private List<OrderSkuViewVO> orderSkuList;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public List<OrderSkuViewVO> getOrderSkuList() {
		return orderSkuList;
	}

	public void setOrderSkuList(List<OrderSkuViewVO> orderSkuList) {
		this.orderSkuList = orderSkuList;
	}

	
	
	
}
