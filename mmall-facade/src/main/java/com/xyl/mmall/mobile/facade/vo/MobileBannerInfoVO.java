package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileBannerInfoVO implements Serializable {

	
	private static final long serialVersionUID = 1687267854578572481L;
	//广告栏的文字（带颜色），见说明5
	private String	bannerTitle;
	//广告栏的URL
	private String bannerURL;
	//轮播图片栏对象的list
	private List<MobileBannerImageVO> imageBanners;
	public String getBannerTitle() {
		return bannerTitle;
	}
	public void setBannerTitle(String bannerTitle) {
		this.bannerTitle = bannerTitle;
	}
	public String getBannerURL() {
		return bannerURL;
	}
	public void setBannerURL(String bannerURL) {
		this.bannerURL = bannerURL;
	}
	public List<MobileBannerImageVO> getImageBanners() {
		return imageBanners;
	}
	public void setImageBanners(List<MobileBannerImageVO> imageBanners) {
		this.imageBanners = imageBanners;
	}
	
}
