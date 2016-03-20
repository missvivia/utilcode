package com.xyl.mmall.common.param;

import java.math.BigDecimal;

public class SMSParam {
	
	private String mobile;//手机号
	
	private long orderNo;//订单号
	
	private BigDecimal price;//订单价格
	
	private String shopName;//店铺
	
	public SMSParam(){
		
	};
	
	public SMSParam(long orderNo){
		this.orderNo = orderNo;
	};
	

	public SMSParam(String mobile, long orderNo, BigDecimal price,
			String shopName) {
		this.mobile = mobile;
		this.orderNo = orderNo;
		this.price = price;
		this.shopName = shopName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	

}
