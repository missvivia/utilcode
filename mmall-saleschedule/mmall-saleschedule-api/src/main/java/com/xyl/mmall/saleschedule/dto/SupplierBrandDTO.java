package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xyl.mmall.saleschedule.meta.SupplierBrandImg;

public class SupplierBrandDTO implements Serializable {
	private static final long serialVersionUID = 2835728554020396145L;

	private long id;
	
	private long supplierId;
	
	private String brandName;
	
	private Map<String, Object> status;
	
	private String imageUrl;
	
	private String logoUrl;
	
	private String intro;
	
	private String intro2;
	
	private String editor;
	
	private long updateTime;
	
	private String reason;
	/**
	 * 站点名称，用于cms品牌审核搜索的时候，以前是单个地区
	 * 现在是有可能是多个区域，所以改成了list
	 */
	private List<String> areaNames;
//	private String siteName;
//	
//	private long siteArea;
	
	/**
	 * 品牌橱窗图 限定6张
	 */
	private List<Map<String, String>> fixImages;
	
	/**
	 * 品牌形象图1 ~ 3张
	 */
	private List<Map<String, String>> maxImages;

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

//	public String getSiteName() {
//		return siteName;
//	}
//
//	public void setSiteName(String siteName) {
//		this.siteName = siteName;
//	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public void setShowCaseImgs(List<SupplierBrandImg> imgList) {
		fixImages = new ArrayList<>();
		for (SupplierBrandImg img : imgList) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("src", img.getImageUrl());
			data.put("desc", img.getShowCaseDesc());
			data.put("index", String.valueOf(img.getImgIndex()));
			fixImages.add(data);
		}
	}
	
	public void setVisualImgs(List<SupplierBrandImg> imgList) {
		maxImages = new ArrayList<>();
		for (SupplierBrandImg img : imgList) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("src", img.getImageUrl());
			data.put("index", String.valueOf(img.getImgIndex()));
			maxImages.add(data);
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Map<String, Object> getStatus() {
		return status;
	}

	public void setStatus(Map<String, Object> status) {
		this.status = status;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getIntro2() {
		return intro2;
	}

	public void setIntro2(String intro2) {
		this.intro2 = intro2;
	}

	public List<Map<String, String>> getFixImages() {
		return fixImages;
	}

	public void setFixImages(List<Map<String, String>> fixImages) {
		this.fixImages = fixImages;
	}

	public List<Map<String, String>> getMaxImages() {
		return maxImages;
	}

	public void setMaxImages(List<Map<String, String>> maxImages) {
		this.maxImages = maxImages;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public List<String> getAreaNames() {
		return areaNames;
	}

	public void setAreaNames(List<String> areaNames) {
		this.areaNames = areaNames;
	}

//	public long getSiteArea() {
//		return siteArea;
//	}
//
//	public void setSiteArea(long siteArea) {
//		this.siteArea = siteArea;
//	}

}
