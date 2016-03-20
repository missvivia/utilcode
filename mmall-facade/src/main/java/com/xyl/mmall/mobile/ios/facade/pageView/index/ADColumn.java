package com.xyl.mmall.mobile.ios.facade.pageView.index;
public class ADColumn {
 
	@ExcelAnnotation(value=1)
	private Long id;
	@ExcelAnnotation(value=3)
	private String picSrc;
	@ExcelAnnotation(value=2)
	private Long categoryId;
	/**
	 * type =1 为商店 2为商品
	 */
	@ExcelAnnotation(value=0)
	private Short type;
	
	private String storeName;
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPicSrc() {
		return picSrc;
	}
	public void setPicSrc(String picSrc) {
		this.picSrc = picSrc;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	} 
	
	
	
}
