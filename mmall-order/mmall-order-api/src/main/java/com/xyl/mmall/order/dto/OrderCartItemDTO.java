package com.xyl.mmall.order.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderCartItem;

/**
 * 订单明细
 * 
 * @author dingmingliang
 * 
 */
public class OrderCartItemDTO extends OrderCartItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderCartItemDTO(OrderCartItem obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public OrderCartItemDTO() {
	}

	/**
	 * 购物车的详细数据源
	 */
	private List<? extends OrderSkuDTO> orderSkuDTOList;

	/**
	 * 订单明细-普通商品Sku
	 */
	private OrderSkuCartItemDTO orderSkuCartItemDTO;
	
	/**
	 * 店铺名称
	 */
	private String storeName;
	
	/**
	 * 店铺url
	 */
	private String storeUrl;
	
	private long storeId;
	

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public OrderSkuCartItemDTO getOrderSkuCartItemDTO() {
		return orderSkuCartItemDTO;
	}

	public void setOrderSkuCartItemDTO(OrderSkuCartItemDTO orderSkuCartItemDTO) {
		this.orderSkuCartItemDTO = orderSkuCartItemDTO;
	}

	public List<? extends OrderSkuDTO> getOrderSkuDTOList() {
		return orderSkuDTOList;
	}

	public void setOrderSkuDTOList(List<? extends OrderSkuDTO> orderSkuDTOList) {
		this.orderSkuDTOList = orderSkuDTOList;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}
	
	
}
