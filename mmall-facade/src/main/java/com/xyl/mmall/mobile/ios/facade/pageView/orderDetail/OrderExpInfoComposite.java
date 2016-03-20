package com.xyl.mmall.mobile.ios.facade.pageView.orderDetail;

import org.apache.commons.beanutils.BeanUtils;

public class OrderExpInfoComposite {

	private long userId;
	private long provinceId;
	private long cityId;
	private long sectionId;
	private long streetId;
	private String province;
	private String city;
	private String section;
	private String street;
	private String address;
	private String zipcode = "";
	private String consigneeName;
	private String consigneeTel = "";
	private String consigneeMobile = "";
	private String addressComment;
	private String areaCode;
	
	public OrderExpInfoComposite() {
		// TODO Auto-generated constructor stub
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public long getSectionId() {
		return sectionId;
	}
	public void setSectionId(long sectionId) {
		this.sectionId = sectionId;
	}
	public long getStreetId() {
		return streetId;
	}
	public void setStreetId(long streetId) {
		this.streetId = streetId;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getConsigneeTel() {
		return consigneeTel;
	}
	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}
	public String getConsigneeMobile() {
		return consigneeMobile;
	}
	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}
	public String getAddressComment() {
		return addressComment;
	}
	public void setAddressComment(String addressComment) {
		this.addressComment = addressComment;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	
}
