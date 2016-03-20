package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.saleschedule.enums.BrandImgSize;

/**
 * 品牌图片表mata
 * @author chengximing
 *
 */
@AnnonOfClass(desc = "品牌logo图片表", tableName = "Mmall_SaleSchedule_BrandLogoImg")
public class BrandLogoImg implements Serializable {
	
	private static final long serialVersionUID = 20140911L;
	
	@AnnonOfField(desc = "品牌logo图片主键id", primary = true, autoAllocateId = true)
	private long brandImgId;
	
	@AnnonOfField(desc = "品牌id", policy = true)
	private long brandId;
	
	@AnnonOfField(desc = "图片在NOS上面的URL", type = "VARCHAR(255)")
	private String brandImgUrl;
	
	@AnnonOfField(desc = "图片尺寸")
	private BrandImgSize brandImgSize;

	public long getBrandImgId() {
		return brandImgId;
	}

	public void setBrandImgId(long brandImgId) {
		this.brandImgId = brandImgId;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getBrandImgUrl() {
		return brandImgUrl;
	}

	public void setBrandImgUrl(String brandImgUrl) {
		this.brandImgUrl = brandImgUrl;
	}

	public BrandImgSize getBrandImgSize() {
		return brandImgSize;
	}

	public void setBrandImgSize(BrandImgSize brandImgSize) {
		this.brandImgSize = brandImgSize;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
