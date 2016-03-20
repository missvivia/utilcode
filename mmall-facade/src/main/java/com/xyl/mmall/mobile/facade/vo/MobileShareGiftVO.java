package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
@Deprecated
public class MobileShareGiftVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7803477082604287969L;
	//分享抢红包地址
	private String giftShareURL;
	//分享抢红包模板
	private String giftShareTemplate;
	//状态 0正常，1过期，2已分享
	private int status;
	public String getGiftShareURL() {
		return giftShareURL;
	}
	public void setGiftShareURL(String giftShareURL) {
		this.giftShareURL = giftShareURL;
	}
	public String getGiftShareTemplate() {
		return giftShareTemplate;
	}
	public void setGiftShareTemplate(String giftShareTemplate) {
		this.giftShareTemplate = giftShareTemplate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
