package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobilePrdtVO extends MobilePrdtSummaryVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7816282061475809153L;

	// 商品图片列表
	private List<String> imageURLs;

	// 描述（不在当期的需要提示的文字，如明日上线）
	private String notAvailDesc;

	// 活动信息
	private List<String> saleInfo;

	// 主站url
	private String webSiteUrl;
	
	private String sizeUrl;
	
	private String poId;
	private long brandId;
	private String brandName;
	private MobileShareVO shareTemplate;
	
	// 详细参数列表URL
	private String detailURL;

	// Sku信息
	private List<MobileSkuVO> skuList;

	// 是否再地区
	private int isNotArea;

	public List<String> getImageURLs() {
		return imageURLs;
	}

	public void setImageURLs(List<String> imageURLs) {
		this.imageURLs = imageURLs;
	}

	public String getNotAvailDesc() {
		return notAvailDesc;
	}

	public void setNotAvailDesc(String notAvailDesc) {
		this.notAvailDesc = notAvailDesc;
	}

	public List<String> getSaleInfo() {
		return saleInfo;
	}

	public void setSaleInfo(List<String> saleInfo) {
		this.saleInfo = saleInfo;
	}

	public String getWebSiteUrl() {
		return webSiteUrl;
	}

	public void setWebSiteUrl(String webSiteUrl) {
		this.webSiteUrl = webSiteUrl;
	}

	public String getDetailURL() {
		return detailURL;
	}

	public void setDetailURL(String detailURL) {
		this.detailURL = detailURL;
	}

	public List<MobileSkuVO> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<MobileSkuVO> skuList) {
		this.skuList = skuList;
	}

	public int getIsNotArea() {
		return isNotArea;
	}

	public void setIsNotArea(int isNotArea) {
		this.isNotArea = isNotArea;
	}

	public MobileShareVO getShareTemplate() {
		return shareTemplate;
	}

	public void setShareTemplate(MobileShareVO shareTemplate) {
		this.shareTemplate = shareTemplate;
	}

	public String getSizeUrl() {
		return sizeUrl;
	}

	public void setSizeUrl(String sizeUrl) {
		this.sizeUrl = sizeUrl;
	}

	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	

}
