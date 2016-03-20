package com.xyl.mmall.mainsite.vo;

import java.util.List;

import com.xyl.mmall.cart.dto.CartItemDTO;


public class InputParam{
	public long skuId;
	public int diff;
	public List<Long> ids;
	public List<Long> selectedIds;
	/**
	 * 进货单购物数量改变使用
	 */
	public List<CartItemDTO>cartItemDTOs;
	
	/**
	 * 再次购买时使用
	 */
	public long orderId;
	
	public List<CartItemDTO> getCartItemDTOs() {
		return cartItemDTOs;
	}

	public void setCartItemDTOs(List<CartItemDTO> cartItemDTOs) {
		this.cartItemDTOs = cartItemDTOs;
	}

	public InputParam(){
		
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	
}