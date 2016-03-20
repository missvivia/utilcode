package com.xyl.mmall.mobile.ios.facade.pageView.index;

import java.util.List;
public class SkuClassify {
	@ExcelAnnotation(value=5)
	private String title;
	@ExcelAnnotation(value=4)
	private Short type;
	private List<MobileIndexSku> skus;
	
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<MobileIndexSku> getSkus() {
		return skus;
	}
	public void setSkus(List<MobileIndexSku> skus) {
		this.skus = skus;
	}
	
	
}
