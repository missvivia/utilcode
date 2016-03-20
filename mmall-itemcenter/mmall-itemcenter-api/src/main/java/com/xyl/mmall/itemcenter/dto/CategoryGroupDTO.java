package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;

public class CategoryGroupDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8532833439492761849L;

	/** 类目id */
	private long lowCategoryId;

	/** 类目的排序 */
	private int categoryIndex;

	/** 该类目下商品总数 */
	private int total;

	public long getLowCategoryId() {
		return lowCategoryId;
	}

	public void setLowCategoryId(long lowCategoryId) {
		this.lowCategoryId = lowCategoryId;
	}

	public int getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(int categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
