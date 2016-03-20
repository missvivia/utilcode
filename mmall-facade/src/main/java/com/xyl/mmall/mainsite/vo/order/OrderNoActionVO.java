package com.xyl.mmall.mainsite.vo.order;

import java.util.List;

/**
 * @author dingmingliang
 * 
 */
public class OrderNoActionVO {

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 订单明细
	 */
	private List<OrderCartItemVO> cartList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<OrderCartItemVO> getCartList() {
		return cartList;
	}

	public void setCartList(List<OrderCartItemVO> cartList) {
		this.cartList = cartList;
	}
}
