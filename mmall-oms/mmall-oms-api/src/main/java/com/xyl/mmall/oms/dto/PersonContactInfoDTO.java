/**
 * 
 */
package com.xyl.mmall.oms.dto;

import java.io.Serializable;

/**
 * 个人信息封装，包括姓名、电话号码、联系地址等
 * 
 * @author hzzengchengyuan
 *
 */
public class PersonContactInfoDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 座机
	 */
	private String phone;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 邮箱
	 */
	private String emial;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 市
	 */
	private String city;

	/**
	 * 区/县
	 */
	private String district;

	/**
	 * 详细地址
	 */
	private String address;

	/**
	 * 邮编号
	 */
	private String postcode;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the emial
	 */
	public String getEmial() {
		return emial;
	}

	/**
	 * @param emial
	 *            the emial to set
	 */
	public void setEmial(String emial) {
		this.emial = emial;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	public String getFullAddress() {
		String[] addressItem = new String[] { getProvince(), getCity(), getDistrict(), getAddress() };
		StringBuilder sb = new StringBuilder();
		boolean preEmpty = true;
		for (String item : addressItem) {
			if (!preEmpty) {
				sb.append("-");
			}
			boolean currentEmpty = isBlank(item);
			if (!currentEmpty) {
				sb.append(item);
			}
			preEmpty = currentEmpty;
		}
		if (!isBlank(getName())) {
			sb.append(",").append(getName());
		}
		if (!isBlank(getMobile())) {
			sb.append(" ").append(getMobile());
		}
		if (!isBlank(getPhone())) {
			sb.append("/").append(getPhone());
		}
		return sb.toString();
	}
	
	
	private boolean isBlank (String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * @param postcode
	 *            the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

}
