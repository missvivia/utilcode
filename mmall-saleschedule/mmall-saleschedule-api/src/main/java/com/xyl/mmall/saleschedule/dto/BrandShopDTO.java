package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.saleschedule.enums.BrandShopStatus;
import com.xyl.mmall.saleschedule.meta.BrandShop;

public class BrandShopDTO implements Serializable {

	private static final long serialVersionUID = -7440668370801230680L;

	private long brandShopId;
	
	private long supplierBrandId;
	
	private String shopName;
	
	/**
	 * province里面存放id和name的键值对
	 */
	private JSONObject province;
	
	/**
	 * city里面存放id和name的键值对
	 */
	private JSONObject city;
	/**
	 * district里面也存放id和name的键值对
	 */
	private JSONObject district;
	/**
	 * 品牌门店街道
	 */
	private JSONObject street;
	
	private String shopAddr;
	
	private String shopZone;
	
	private String shopTel;
	
	private String shopContact;
	
	private BrandShopStatus status;
	
	private BigDecimal longitude;
	
	private BigDecimal latitude;
	
	private long statusUpdateDate;
	
	private long shopUpdateDate;
	
	public BrandShop changeDataIntoBrandShop() {
		BrandShop shop = new BrandShop();
		shop.setBrandShopId(brandShopId);
		shop.setSupplierBrandId(supplierBrandId);
		shop.setShopName(shopName);
		shop.setProvince(province.getLongValue("id"));
		shop.setCity(city.getLongValue("id"));
		shop.setDistrict(district.getLongValue("id"));
		shop.setStreet(street.getLongValue("id"));
		shop.setProvinceName(province.getString("name"));
		shop.setCityName(city.getString("name"));
		shop.setDistrictName(district.getString("name"));
		shop.setStreetName(street.getString("name"));
		shop.setShopAddr(shopAddr);
		shop.setShopZone(shopZone);
		shop.setShopContact(shopContact);
		shop.setShopTel(shopTel);
		shop.setStatus(status);
		shop.setLatitude(latitude);
		shop.setLongitude(longitude);
		shop.setLatitude(latitude);
		shop.setStatusUpdateDate(statusUpdateDate);
		shop.setShopUpdateDate(shopUpdateDate);
		return shop;
	}

	public long getBrandShopId() {
		return brandShopId;
	}

	public void setBrandShopId(long brandShopId) {
		this.brandShopId = brandShopId;
	}

	public long getSupplierBrandId() {
		return supplierBrandId;
	}

	public void setSupplierBrandId(long supplierBrandId) {
		this.supplierBrandId = supplierBrandId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public JSONObject getCity() {
		return city;
	}

	public void setCity(JSONObject city) {
		this.city = city;
	}

	public JSONObject getDistrict() {
		return district;
	}

	public void setDistrict(JSONObject district) {
		this.district = district;
	}

	public String getShopAddr() {
		return shopAddr;
	}

	public void setShopAddr(String shopAddr) {
		this.shopAddr = shopAddr;
	}

	public String getShopZone() {
		return shopZone;
	}

	public void setShopZone(String shopZone) {
		this.shopZone = shopZone;
	}

	public String getShopTel() {
		return shopTel;
	}

	public void setShopTel(String shopTel) {
		this.shopTel = shopTel;
	}

	public String getShopContact() {
		return shopContact;
	}

	public void setShopContact(String shopContact) {
		this.shopContact = shopContact;
	}

	public BrandShopStatus getStatus() {
		return status;
	}

	public void setStatus(BrandShopStatus status) {
		this.status = status;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public long getStatusUpdateDate() {
		return statusUpdateDate;
	}

	public void setStatusUpdateDate(long statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}

	public long getShopUpdateDate() {
		return shopUpdateDate;
	}

	public void setShopUpdateDate(long shopUpdateDate) {
		this.shopUpdateDate = shopUpdateDate;
	}

	public JSONObject getProvince() {
		return province;
	}

	public void setProvince(JSONObject province) {
		this.province = province;
	}

	public JSONObject getStreet() {
		return street;
	}

	public void setStreet(JSONObject street) {
		this.street = street;
	}
	
}
