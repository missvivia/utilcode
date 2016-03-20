package com.xyl.mmall.itemcenter.dto;

import com.xyl.mmall.itemcenter.meta.PoSku;

/**
 * 显示给前台的简单vo对象
 * @author hzzhaozhenzuo
 *
 */
public class PoSkuVo extends PoSku{

	private static final long serialVersionUID = 1L;
	
	private long brandId;
	
	private long startTime;

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
