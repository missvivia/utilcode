package com.xyl.mmall.cms.vo;

/**
 * CMS系统中 商品资料审核 每条商品记录VO
 * 
 * @author hzhuangluqian
 *
 */
public class ProductReviewVO {
	/** 商品id */
	private String id;

	/** 缩略图路径 */
	private String thumb;

	/** 商品名称 */
	private String productName;

	/** 类目名称 */
	private String categoryName;

	/** 货号 */
	private String goodsNo;

	/** 颜色 */
	private String colorName;

	/** 档期id */
	private String poId;

	/** 提交时间 */
	private String submitTime;

	/** 审核理由 */
	private String reason;

	private int productStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
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

	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(int productStatus) {
		this.productStatus = productStatus;
	}

}
