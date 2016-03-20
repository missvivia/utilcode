package com.xyl.mmall.cart.dto;

import java.io.Serializable;

/**
 * 对应购物车中一条
 * 
 * @author Yang,Nan
 *
 */
public class CartItemDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 20908824694088287L;

	public static int TYPE_POINVALID = -1;

	public static int TYPE_SOLDOUT = -2;

	private long skuid;

	private int count;

	private long createTime;

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

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
