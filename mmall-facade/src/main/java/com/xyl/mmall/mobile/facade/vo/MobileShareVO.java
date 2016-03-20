package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileShareVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7803477082604287969L;
	
	private String  shareURL;
	private String  shareTemplate;
	private String[] shareImage;
	private String shareLogo;
	private String shareTitle;
	private int status;
	public String getShareURL() {
		return shareURL;
	}
	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}
	public String getShareTemplate() {
		return shareTemplate;
	}
	public void setShareTemplate(String shareTemplate) {
		this.shareTemplate = shareTemplate;
	}
	public String[] getShareImage() {
		return shareImage;
	}
	public void setShareImage(String[] shareImage) {
		this.shareImage = shareImage;
	}
	public String getShareLogo() {
		return shareLogo;
	}
	public void setShareLogo(String shareLogo) {
		this.shareLogo = shareLogo;
	}
	public String getShareTitle() {
		return shareTitle;
	}
	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
