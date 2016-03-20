package com.xyl.mmall.backend.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 档期添加商品时候的全部商品Tab中的ProductVO
 * 
 * @author hzhuangluqian
 *
 */
public class PoProductVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1158038524988200276L;

	/** productId */
	private String id;

	/** 货号 */
	private String goodsNo;

	/** 颜色 */
	private String colorName;

	private String productName;

	/** 类目 */
	private String categoryName;

	/** 正品价 */
	private BigDecimal marketPrice;

	/** 销售价 */
	private BigDecimal salePrice;

	/** 供货价 */
	private BigDecimal basePrice;

	/** 缩略图地址 */
	private String showPicPath;

	/** 审核状态 */
	private int reviewStatus;

	/** sku信息列表 */
	private List<POSkuVO> skuList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public String getShowPicPath() {
		return showPicPath;
	}

	public void setShowPicPath(String showPicPath) {
		this.showPicPath = showPicPath;
	}

	public List<POSkuVO> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<POSkuVO> skuList) {
		this.skuList = skuList;
	}

	public int getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(int reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
}
