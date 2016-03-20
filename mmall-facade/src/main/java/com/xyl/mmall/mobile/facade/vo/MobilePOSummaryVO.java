package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobilePOSummaryVO implements  Comparable<MobilePOSummaryVO>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 361454552428498602L;
	
	//专场ID
	protected long poId;
	//图片URL
	protected String poBannerImage;
	//logoURL
	protected String brandLogoURL;
	//品牌名字
	protected String brandName;
	//品牌名字
	protected long brandId;
	//专场名字
	protected String poName;
	//折扣信息
	protected String discountDesc;
	//开始时间
	protected long startTime	;
	//结束时间
	protected long endTime;
	//剩余时间
	protected long countDownTime;
	//是否关注0 否，1是
	protected int favorite;
	//关注人数
	protected int favoriteNum;
	//状态
	protected int poStatus;
	//状态
	protected String startDate;
	
	protected int newProductCount;
	public long getPoId() {
		return poId;
	}
	public void setPoId(long poId) {
		this.poId = poId;
	}
	
	public String getPoBannerImage() {
		return poBannerImage;
	}
	public void setPoBannerImage(String poBannerImage) {
		this.poBannerImage = poBannerImage;
	}

	public String getBrandLogoURL() {
		return brandLogoURL;
	}
	public void setBrandLogoURL(String brandLogoURL) {
		this.brandLogoURL = brandLogoURL;
	}
	public String getPoName() {
		return poName;
	}
	public void setPoName(String poName) {
		this.poName = poName;
	}
	public String getDiscountDesc() {
		return discountDesc;
	}
	public void setDiscountDesc(String discountDesc) {
		this.discountDesc = discountDesc;
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
	public long getCountDownTime() {
		return countDownTime;
	}
	public void setCountDownTime(long countDownTime) {
		this.countDownTime = countDownTime;
	}
	public int getFavorite() {
		return favorite;
	}
	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	public int getFavoriteNum() {
		return favoriteNum;
	}
	public void setFavoriteNum(int favoriteNum) {
		this.favoriteNum = favoriteNum;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public long getBrandId() {
		return brandId;
	}
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	public int getPoStatus() {
		return poStatus;
	}
	public void setPoStatus(int poStatus) {
		this.poStatus = poStatus;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public int getNewProductCount() {
		return newProductCount;
	}
	public void setNewProductCount(int newProductCount) {
		this.newProductCount = newProductCount;
	}
	@Override
	public int compareTo(MobilePOSummaryVO o) {
		if(this.startTime > o.getStartTime())
			return -1;
		else if(this.startTime < o.getStartTime())
			return 1;
		return 0;
	}

}
