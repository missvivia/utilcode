package com.xyl.mmall.itemcenter.dto;

public class OrderSkuSnapShotDTO {
	/** 所属的商品名 */
	private String productName;

	/** 所属的商品颜色 */
	private String colorName;

	/** 尺码 */
	private String size;

	/** 缩略图地址 */
	private String thumb;

	/**
	 * 产品链接
	 */
	private String productLinkUrl;

	/** 品牌id */
	private long brandId;

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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getProductLinkUrl() {
		return productLinkUrl;
	}

	public void setProductLinkUrl(String productLinkUrl) {
		this.productLinkUrl = productLinkUrl;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
}
