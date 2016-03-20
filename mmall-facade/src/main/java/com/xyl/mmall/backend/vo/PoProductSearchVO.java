package com.xyl.mmall.backend.vo;

import java.io.Serializable;

public class PoProductSearchVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1490290095754036258L;

	private long poId;

	/** 最低类目id */
	private long lowCategoryId;

	/** 商品名称 */
	private String productName;

	/** 货号 */
	private String goodsNo;

	/** 条形码1 */
	private String barCode;

	private int status;

	/** 上次查询的最后一个id值 */
	private long lastId;

	/** 查询数量 */
	private int limit;

	private int offset;

	private int page;

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public long getLowCategoryId() {
		return lowCategoryId;
	}

	public void setLowCategoryId(long lowCategoryId) {
		this.lowCategoryId = lowCategoryId;
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

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getLastId() {
		return lastId;
	}

	public void setLastId(long lastId) {
		this.lastId = lastId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
