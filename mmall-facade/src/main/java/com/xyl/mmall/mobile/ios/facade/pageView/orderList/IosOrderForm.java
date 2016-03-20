package com.xyl.mmall.mobile.ios.facade.pageView.orderList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;

public class IosOrderForm {
	/**
	 * 订单Id
	 */
	private long orderId;

	/**
	 * 订单parentId
	 */
	private long parentId;

	/**
	 * 订单结算价
	 */
	private BigDecimal cartRPrice = BigDecimal.ZERO;

	/**
	 * 店铺名字
	 */
	private String storeName;

	/**
	 * 店铺ID
	 */
	private long storeId;


	/**
	 * 明细
	 */
	private List<IosOrderSku> orderSkuList;
	
	



	public IosOrderForm() {

	}

	public IosOrderForm(OrderFormDTO orderFormDTO) {
		this.orderId = orderFormDTO.getOrderId();
		this.storeId = orderFormDTO.getBusinessId();
		this.parentId = orderFormDTO.getParentId();
		this.cartRPrice = orderFormDTO.getCartRPrice();
		List<OrderSkuDTO> orderSkuDTOs = orderFormDTO.getOrderSkuDTOListOfOrdGift();

		if (orderSkuDTOs != null) {
			orderSkuList = new ArrayList<>();
			IosOrderSku iosOrderSku = null;
			for (OrderSkuDTO orderSkuDTO : orderSkuDTOs) {
				iosOrderSku = new IosOrderSku();
				iosOrderSku.setSkuId(orderSkuDTO.getSkuId());
				if (orderSkuDTO.getSkuSPDTO() != null) {
					iosOrderSku.setThumb(orderSkuDTO.getSkuSPDTO().getPicUrl());
					iosOrderSku.setName(orderSkuDTO.getSkuSPDTO().getProductName());
				}
				orderSkuList.add(iosOrderSku);
			}
		}
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public BigDecimal getCartRPrice() {
		return cartRPrice;
	}

	public void setCartRPrice(BigDecimal cartRPrice) {
		this.cartRPrice = cartRPrice;
	}

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


	public List<IosOrderSku> getOrderSkuList() {
		return orderSkuList;
	}

	public void setOrderSkuList(List<IosOrderSku> orderSkuList) {
		this.orderSkuList = orderSkuList;
	}

}
