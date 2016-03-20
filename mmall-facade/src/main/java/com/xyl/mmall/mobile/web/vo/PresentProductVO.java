/**
 * 
 */
package com.xyl.mmall.mobile.web.vo;

import java.math.BigDecimal;

import com.xyl.mmall.content.dto.PresentProductDTO;

/**
 * @author hzlihui2014
 *
 */
public class PresentProductVO {

	private static final String MAINSITE_PRODUCT_DETAIL_PAGE_PREFIX = "http://023.baiwandian.cn/product/detail?id=";

	private PresentProductDTO presentProduct;

	public PresentProductVO() {
		presentProduct = new PresentProductDTO();
	}

	public PresentProductVO(PresentProductDTO presentProduct) {
		this.presentProduct = presentProduct;
	}

	/**
	 * @return the category
	 */
	public int getCategory() {
		return presentProduct.getCategory();
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return presentProduct.getImage();
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return presentProduct.getLogo();
	}

	/**
	 * @return the marketPrice
	 */
	public BigDecimal getMarketPrice() {
		return presentProduct.getMarketPrice();
	}

	/**
	 * @return the salePrice
	 */
	public BigDecimal getSalePrice() {
		return presentProduct.getSalePrice();
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return presentProduct.getProductName();
	}

	/**
	 * @return the productUrl
	 */
	public String getProductUrl() {
		return MAINSITE_PRODUCT_DETAIL_PAGE_PREFIX + presentProduct.getProductId();
	}

}
