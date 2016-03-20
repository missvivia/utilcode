package com.xyl.mmall.oms.dto;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 收货地址
 * 
 * @author dingmingliang
 * 
 */
public class OmsConsigneeAddressParam implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "快递地址-省份ID", unsigned = false)
	private long provinceId;

	@AnnonOfField(desc = "快递地址-市ID", unsigned = false)
	private long cityId;

	@AnnonOfField(desc = "快递地址-区ID", unsigned = false)
	private long sectionId;

	@AnnonOfField(desc = "快递地址-街道ID")
	private long streetId;

	@AnnonOfField(desc = "快递地址-详细地址", type = "VARCHAR(64)", safeHtml = true)
	private String address;

	@AnnonOfField(desc = "快递地址-邮政编号", type = "VARCHAR(8)", notNull = false, safeHtml = true)
	private String zipcode;

	@AnnonOfField(desc = "收件人-姓名", type = "VARCHAR(16)", safeHtml = true)
	private String consigneeName;

	@AnnonOfField(desc = "收件人-固定电话", type = "VARCHAR(32)", notNull = false, safeHtml = true)
	private String consigneeTel = "--";

	@AnnonOfField(desc = "收件人-手机", type = "VARCHAR(16)", notNull = false, safeHtml = true)
	private String consigneeMobile;

	@AnnonOfField(desc = "快递地址-省份名", inDB = false)
	private String province;

	@AnnonOfField(desc = "快递地址-市名", inDB = false)
	private String city;

	@AnnonOfField(desc = "快递地址-区名", inDB = false)
	private String section;

	@AnnonOfField(desc = "快递地址-街道名", inDB = false)
	private String street;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

}
