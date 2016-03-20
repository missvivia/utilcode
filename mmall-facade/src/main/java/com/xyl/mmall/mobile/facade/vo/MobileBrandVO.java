package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileBrandVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7803477082604287969L;
	//品牌ID
	private long brandId;
	//品牌名称
	private String brandName;
	//正在活动的数量
	private int activePOCount;
	//创建时间，排序用
	private long createTime;
	//logo 图片
	private String logoImage;
	//描述URL
	private String descImage;
	//品牌秒数
	private String brandDesc;
	//是否收藏
	private int isFavorite;
	//收藏人数
	private int favoriteNum;
	
	private String begin;
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
	public int getActivePOCount() {
		return activePOCount;
	}
	public void setActivePOCount(int activePOCount) {
		this.activePOCount = activePOCount;
	}
	public String getLogoImage() {
		return logoImage;
	}
	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}
	public String getDescImage() {
		return descImage;
	}
	public void setDescImage(String descImage) {
		this.descImage = descImage;
	}
	public String getBrandDesc() {
		return brandDesc;
	}
	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc;
	}
	public int getIsFavorite() {
		return isFavorite;
	}
	public void setIsFavorite(int isFavorite) {
		this.isFavorite = isFavorite;
	}
	public int getFavoriteNum() {
		return favoriteNum;
	}
	public void setFavoriteNum(int favoriteNum) {
		this.favoriteNum = favoriteNum;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	
	
	
}
