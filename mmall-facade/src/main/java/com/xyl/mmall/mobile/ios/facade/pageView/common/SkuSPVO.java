package com.xyl.mmall.mobile.ios.facade.pageView.common;

import java.util.Map;

/**
 * Sku的快照信息
 * 
 * @author dingmingliang
 * 
 */
public class SkuSPVO {

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 商品图片Url
	 */
	private String picUrl;

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
	 * 品牌迷你页链接
	 */
	private String brandLinkUrl;


	public String getBrandLinkUrl() {
		return brandLinkUrl;
	}

	public void setBrandLinkUrl(String brandLinkUrl) {
		this.brandLinkUrl = brandLinkUrl;
	}


	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
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
}
