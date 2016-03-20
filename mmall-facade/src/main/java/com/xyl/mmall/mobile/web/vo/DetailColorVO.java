package com.xyl.mmall.mobile.web.vo;

/**
 * 商品详情页的颜色标签vo
 * 
 * @author hzhuangluqian
 *
 */
public class DetailColorVO {
	/** 商品id */
	private String productId;

	/** 缩略图地址 */
	private String thumb;

	/** 商品详情页地址 */
	private String productURL;

	private String colorName;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getProductURL() {
		return productURL;
	}

	public void setProductURL(String productURL) {
		this.productURL = productURL;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

}
