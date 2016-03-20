package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileShopVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7816282061475809153L;
	//实体店ID
	private long shopId;
	//实体店名称
	private String shopName;
	//shop1-2-302
	private String shopAddressDesc;
	//电话
	private String shopPhone;
	//邮编
	private String zipcode;
	//纬度
	private double latitude;
	//经度
	private double longitude;
	public long getShopId() {
		return shopId;
	}
	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopAddressDesc() {
		return shopAddressDesc;
	}
	public void setShopAddressDesc(String shopAddressDesc) {
		this.shopAddressDesc = shopAddressDesc;
	}
	public String getShopPhone() {
		return shopPhone;
	}
	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	
	
}
