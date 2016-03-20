package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;

import com.xyl.mmall.itemcenter.enums.StatusType;

/**
 * 档期审核的商品筛选
 * 
 * @author hzhuangluqian
 *
 */
public class ProductReviewParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 88097018777375034L;

	/** 商品名称 */
	private String productName;

	/** 货号 */
	private String goodsNo;

	/** 条形码 */
	private String barCode;

	private long stime;

	private long etime;
	
	/** 搜索的个数 */
	private int limit;

	/** 偏移量 */
	private int offset;

	/** 上一页最后一个id */
	private int lastId;

	/** 审核状态 */
	private StatusType statusType;

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

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getLastId() {
		return lastId;
	}

	public void setLastId(int lastId) {
		this.lastId = lastId;
	}

	public StatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(StatusType statusType) {
		this.statusType = statusType;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public long getStime() {
		return stime;
	}

	public void setStime(long stime) {
		this.stime = stime;
	}

	public long getEtime() {
		return etime;
	}

	public void setEtime(long etime) {
		this.etime = etime;
	}
}
