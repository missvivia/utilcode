package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.saleschedule.enums.BrandImgType;

@AnnonOfClass(desc = "供应商品牌图片表", tableName = "Mmall_SaleSchedule_SupplierBrandImg")
public class SupplierBrandImg implements Serializable {

	private static final long serialVersionUID = 6187177455621789790L;
	
	@AnnonOfField(desc = "供应商品牌图片表主键id", primary = true, autoAllocateId = true)
	private long imgId;
	
	@AnnonOfField(desc = "供应商品牌id Mmall_SaleSchedule_SupplierBrand的主键", policy = true)
	private long supplierBrandId;
	
	@AnnonOfField(desc = "图片的url", notNull = false, type = "VARCHAR(255)")
	private String imageUrl;
	
	@AnnonOfField(desc = "图片的index，代表是第几个形象图或者第几个橱窗图 形象图（0~2） 橱窗图（0~5）")
	private long imgIndex;
	
	@AnnonOfField(desc = "图片的类型 0 形象图（1~3个） 1 橱窗图（6个）")
	private BrandImgType brandImgType;
	
	@AnnonOfField(desc = "当类型是橱窗图的时候，图片的描述", notNull = false, type = "VARCHAR(255)")
	private String showCaseDesc;

	public long getImgId() {
		return imgId;
	}

	public void setImgId(long imgId) {
		this.imgId = imgId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public BrandImgType getBrandImgType() {
		return brandImgType;
	}

	public void setBrandImgType(BrandImgType brandImgType) {
		this.brandImgType = brandImgType;
	}

	public String getShowCaseDesc() {
		return showCaseDesc;
	}

	public void setShowCaseDesc(String showCaseDesc) {
		this.showCaseDesc = showCaseDesc;
	}

	public long getImgIndex() {
		return imgIndex;
	}

	public void setImgIndex(long imgIndex) {
		this.imgIndex = imgIndex;
	}

	public long getSupplierBrandId() {
		return supplierBrandId;
	}

	public void setSupplierBrandId(long supplierBrandId) {
		this.supplierBrandId = supplierBrandId;
	}
	
}
