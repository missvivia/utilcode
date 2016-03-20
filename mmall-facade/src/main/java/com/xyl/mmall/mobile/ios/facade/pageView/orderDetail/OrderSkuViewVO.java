package com.xyl.mmall.mobile.ios.facade.pageView.orderDetail;

import java.math.BigDecimal;

import com.xyl.mmall.order.dto.OrderSkuDTO;

public class OrderSkuViewVO {

	private long skuId;

	private String thumb;

	private String name;

	private BigDecimal rprice;
	
	private int count;
	
	private BigDecimal totalRPrice;

	public OrderSkuViewVO() {
		// TODO Auto-generated constructor stub
	}
	public OrderSkuViewVO(OrderSkuDTO skuDTO) {
		// TODO Auto-generated constructor stub
		this.skuId = skuDTO.getSkuId();
		this.thumb = skuDTO.getSkuSPDTO().getPicUrl();
		this.name = skuDTO.getSkuSPDTO().getProductName();
		this.rprice = skuDTO.getRprice();
		this.count = skuDTO.getTotalCount();
		this.totalRPrice = skuDTO.getRprice().multiply(new BigDecimal(skuDTO.getTotalCount()));
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getRprice() {
		return rprice;
	}

	public void setRprice(BigDecimal rprice) {
		this.rprice = rprice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BigDecimal getTotalRPrice() {
		return totalRPrice;
	}

	public void setTotalRPrice(BigDecimal totalRPrice) {
		this.totalRPrice = totalRPrice;
	}
	
	
	
}
