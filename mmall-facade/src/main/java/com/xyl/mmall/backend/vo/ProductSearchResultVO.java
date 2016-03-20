package com.xyl.mmall.backend.vo;

public class ProductSearchResultVO {
	private String id;

	/** 缩略图地址 */
	private String showPicPath;

	/** 类目名称 */
	private String categoryName;

	/** 商品名称 */
	private String productName;

	/** 货号 */
	private String goodsNo;

	/** 颜色 */
	private String colorName;

	/** 商品基本信息是否录入 */
	private int isBaseInfoInput;

	/** 规格图片是否录入 */
	private int isPicInfoInput;

	/** 详情信息是否录入 */
	private int isDetailInfoInput;

	/** 尺码设置是否录入 */
	private int isSizeInfoInput;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShowPicPath() {
		return showPicPath;
	}

	public void setShowPicPath(String showPicPath) {
		this.showPicPath = showPicPath;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public int getIsBaseInfoInput() {
		return isBaseInfoInput;
	}

	public void setIsBaseInfoInput(int isBaseInfoInput) {
		this.isBaseInfoInput = isBaseInfoInput;
	}

	public int getIsPicInfoInput() {
		return isPicInfoInput;
	}

	public void setIsPicInfoInput(int isPicInfoInput) {
		this.isPicInfoInput = isPicInfoInput;
	}

	public int getIsDetailInfoInput() {
		return isDetailInfoInput;
	}

	public void setIsDetailInfoInput(int isDetailInfoInput) {
		this.isDetailInfoInput = isDetailInfoInput;
	}

	public int getIsSizeInfoInput() {
		return isSizeInfoInput;
	}

	public void setIsSizeInfoInput(int isSizeInfoInput) {
		this.isSizeInfoInput = isSizeInfoInput;
	}

}
