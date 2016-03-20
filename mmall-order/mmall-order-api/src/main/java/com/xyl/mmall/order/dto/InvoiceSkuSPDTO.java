package com.xyl.mmall.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Sku的快照信息(发票模块专用)
 * 
 * @author dingmingliang
 * 
 */
public class InvoiceSkuSPDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * skuId
	 */
	private long skuId;
	
	/**
	 * orderSkuId
	 */
	private long orderSkuId;

	/**
	 * 购买数量
	 */
	private int count;

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 商品颜色
	 */
	private String colorName;

	/**
	 * Sku规格描述<br>
	 * Key:规格名称,Value:规格值
	 */
	private Map<String, String> skuSpecValueMap;

	/**
	 * 品牌名
	 */
	private String brandName;

	/**
	 * 零售单价
	 */
	private BigDecimal rprice;	

	public long getOrderSkuId() {
		return orderSkuId;
	}

	public void setOrderSkuId(long orderSkuId) {
		this.orderSkuId = orderSkuId;
	}

	public BigDecimal getRprice() {
		return rprice;
	}

	public void setRprice(BigDecimal rprice) {
		this.rprice = rprice;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public Map<String, String> getSkuSpecValueMap() {
		return skuSpecValueMap;
	}

	public void setSkuSpecValueMap(Map<String, String> skuSpecValueMap) {
		this.skuSpecValueMap = skuSpecValueMap;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
}
