package com.xyl.mmall.itemcenter.dto;

import com.xyl.mmall.itemcenter.meta.PoProduct;

public class PoProductVo extends PoProduct{

	private static final long serialVersionUID = 1L;
	
	private long brandId;
	
	//档期开始时间
	private long startTime;
	
	//档期结束时间
	private long endTime;

	private String brandName;

	private String brandNameEn;

	private long saleSiteFlag;

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandNameEn() {
		return brandNameEn;
	}

	public void setBrandNameEn(String brandNameEn) {
		this.brandNameEn = brandNameEn;
	}

	public long getSaleSiteFlag() {
		return saleSiteFlag;
	}

	public void setSaleSiteFlag(long saleSiteFlag) {
		this.saleSiteFlag = saleSiteFlag;
	}

}
