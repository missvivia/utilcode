package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobilePoVO extends MobilePOSummaryVO implements Serializable{
		
	private static final long serialVersionUID = -487700508495212338L;

	//专场活动信息
	private List<String> poInfo;
	
	private MobileShareVO shareTemplate;
	//主站信息
	private String websiteURL;
	//是否再地区
	private int isNotArea;
	
	public List<String> getPoInfo() {
		return poInfo;
	}
	public void setPoInfo(List<String> poInfo) {
		this.poInfo = poInfo;
	}
	public String getWebsiteURL() {
		return websiteURL;
	}
	public void setWebsiteURL(String websiteURL) {
		this.websiteURL = websiteURL;
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
	
	
	
}
