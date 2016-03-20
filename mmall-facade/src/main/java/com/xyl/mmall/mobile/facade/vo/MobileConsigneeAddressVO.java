package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 第一级类
 * 
 * @author jiangww
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileConsigneeAddressVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2648639603532678277L;

	// 地址Id
	protected long addressId;

	// 省份
	protected long provinceId;

	// 城市
	protected long cityId;

	// 区域
	protected long districtId;
	protected long streetId;
	// 省份
	protected String provinceName;

	// 城市
	protected String cityName;

	// 区域
	protected String districtName;
	protected String streetName;

	// 地址详细（包含省市区的描述）
	protected String addressSuffix;

	// 收货人
	protected String name;

	// 手机
	protected String phone;

	private String tel;
	// 是否为默认地址0，否，1，是
	protected int isDefault;

	// 邮政编码
	protected String zipcode;

	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
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

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public long getStreetId() {
		return streetId;
	}

	public void setStreetId(long streetId) {
		this.streetId = streetId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getAddressSuffix() {
		return addressSuffix;
	}

	public void setAddressSuffix(String addressSuffix) {
		this.addressSuffix = addressSuffix;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}


}
