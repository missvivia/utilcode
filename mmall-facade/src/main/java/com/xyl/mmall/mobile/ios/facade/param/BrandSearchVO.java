package com.xyl.mmall.mobile.ios.facade.param;

public class BrandSearchVO {
	
	private Long categoryId;
	
	private Long businessId;
	
	/** 搜索值. */
	private String searchValue;
	
	/** 条形码 */
	private String barCode;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	
	

}
