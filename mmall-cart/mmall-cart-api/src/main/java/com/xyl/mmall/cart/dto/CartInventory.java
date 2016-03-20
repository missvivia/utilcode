package com.xyl.mmall.cart.dto;

import java.io.Serializable;

/**
 * 购物车库存
 * @author Yang,Nan
 *
 */
public class CartInventory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6461411031743626179L;
	private long skuid;
	private int count;
	public long getSkuid() {
		return skuid;
	}
	public void setSkuid(long skuid) {
		this.skuid = skuid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
