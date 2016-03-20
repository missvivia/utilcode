package com.xyl.mmall.mainsite.vo;

public class ProductListSkuVO {
	/** skuId */
	private long id;

	/** 尺码 */
	private String size;

	/** 入库数量 */
	private int num;

	/** 购物车库存 */
	private int cartStock;

	/** 订单库存 */
	private int orderStock;

	/** 1：正常， 2：有机会，3：售罄 */
	private int state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCartStock() {
		return cartStock;
	}

	public void setCartStock(int cartStock) {
		this.cartStock = cartStock;
	}

	public int getOrderStock() {
		return orderStock;
	}

	public void setOrderStock(int orderStock) {
		this.orderStock = orderStock;
	}

}
