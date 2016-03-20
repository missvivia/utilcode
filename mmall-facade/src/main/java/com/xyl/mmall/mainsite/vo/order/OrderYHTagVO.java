package com.xyl.mmall.mainsite.vo.order;

/**
 * 订单优惠Tag
 * 
 * @author dingmingliang
 * 
 */
public class OrderYHTagVO {

	/**
	 * 优惠类型(0:免邮)
	 */
	private int type;

	/**
	 * 优惠描述
	 */
	private String desc;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
