package com.xyl.mmall.mobile.web.vo.order;

import java.util.List;

import com.xyl.mmall.mobile.ios.facade.pageView.common.OrderCartItemVO;

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
