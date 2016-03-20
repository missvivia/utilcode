/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

/**
 * 商品搜索vo
 */
public class MobileProductSearchVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 7575229322517270771L;

	/** 内容分类id. */
	private long categoryContentId;

	/** 品牌id. */
	private String brandIds;

	/** 区域id. */
	private long areaCode;

	/** 排序字段，目前只有ProductSaleNum. */
	private String sortColumn;

	/** 升降序，1升序，其余降序. */
	private int isAsc;

	private String barCode;
	
	/** 搜索值. */
	private String searchValue;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public long getCategoryContentId() {
		return categoryContentId;
	}

	public void setCategoryContentId(long categoryContentId) {
		this.categoryContentId = categoryContentId;
	}

	public String getBrandIds() {
		return brandIds;
	}

	public void setBrandIds(String brandIds) {
		this.brandIds = brandIds;
	}

	public long getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(long areaCode) {
		this.areaCode = areaCode;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public int getIsAsc() {
		return isAsc;
	}

	public void setIsAsc(int isAsc) {
		this.isAsc = isAsc;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
}
