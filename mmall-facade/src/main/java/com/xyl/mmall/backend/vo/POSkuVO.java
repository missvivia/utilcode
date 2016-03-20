package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * 档期添加商品时候的全部商品Tab中的skuVO
 * 
 * @author hzhuangluqian
 *
 */
public class POSkuVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7751756272617367742L;

	public static final int NOT_ADD = 0;

	public static final int HAS_ADD = 1;

	public static final int ADD_OTHER = 2;

	public static final int NOT_SUBMIT = 1;

	public static final int PENDING = 2;

	public static final int APPROVAL = 3;

	public static final int REJECT = 4;

	private String skuId;

	/** 条形码 */
	private String barCode;

	/** 尺码 */
	private String size;

	/** sku数量 */
	private long num;

	/** 品牌商供应数量 */
	private long supplyNum;

	/** 添加状态 */
	private int addStatus;

	private String statusName;

	/** 审核状态 */
	private int reviewStatus;

	/** 审核状态 */
	private String reviewText;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public int getAddStatus() {
		return addStatus;
	}

	public void setAddStatus(int addStatus) {
		this.addStatus = addStatus;
	}

	public int getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(int reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public long getSupplyNum() {
		return supplyNum;
	}

	public void setSupplyNum(long supplyNum) {
		this.supplyNum = supplyNum;
	}

}
