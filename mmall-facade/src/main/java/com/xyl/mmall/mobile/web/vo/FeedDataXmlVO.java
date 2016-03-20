package com.xyl.mmall.mobile.web.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class FeedDataXmlVO {

	private String pName;
	
	private String cName;
	
	private String imgUrl;
	
	private String price;
	
	private String originalPrice;
	
	private String pid;
	
	private String inventoryNum;
	
	private String city;
	
	private List<Long> skuIdList;

	@XmlElement
	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	@XmlElement
	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	@XmlElement
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@XmlElement
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@XmlElement
	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	@XmlElement
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@XmlElement
	public String getInventoryNum() {
		return inventoryNum;
	}

	public void setInventoryNum(String inventoryNum) {
		this.inventoryNum = inventoryNum;
	}

	@XmlElement
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<Long> getSkuIdList() {
		return skuIdList;
	}

	public void setSkuIdList(List<Long> skuIdList) {
		this.skuIdList = skuIdList;
	}

}
