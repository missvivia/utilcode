package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.mobile.facade.converter.Converter;
@JsonInclude(Include.NON_NULL)
public class MobileBannerImageVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7194433956981586646L;
	//轮播广告ID
	private long id;
	//1:活动、2:品牌、3:单品
	private int type;
	//图片地址
	private String imageUrl;
	//图片描述文字
	private String desc;
	//图片对应的跳转地址
	private String linkUrl;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public void setLinkUrl(int pagestr,String itemId) {
		this.linkUrl = Converter.genMobilePageLink(pagestr, itemId);
	}
	public void setLinkUrl(int pagestr,long itemId) {
		String ids= "";
		if(itemId > 0)
			ids = String.valueOf(itemId);
		this.linkUrl = Converter.genMobilePageLink(pagestr, ids);
	}

}
