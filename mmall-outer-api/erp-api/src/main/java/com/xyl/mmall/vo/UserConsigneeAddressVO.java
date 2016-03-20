/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.vo;

import java.io.Serializable;

import com.xyl.mmall.order.dto.ConsigneeAddressDTO;

/**
 * UserConsigneeAddressVO.java created by yydx811 at 2015年11月26日 下午3:46:58
 * 用户收货地址vo
 *
 * @author yydx811
 */
public class UserConsigneeAddressVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -8570647672571468753L;

	/** 收货地址id. */
	private long addressId;

	/** 用户id. */
	private long userId;
	
	/** 用户名. */
	private String userName;

	/** 省id. */
	private long provinceCode;

	/** 市id. */
	private long cityCode;

	/** 区id. */
	private long sectionCode;

	/** 街道id. */
	private long streetCode;

	/** 详细地址. */
	private String detailAddress;

	/** 邮编. */
	private String zipCode;

	/** 地址备注. */
	private String addressComment;

	/** 收货人姓名. */
	private String consigneeName;

	/** 收货人座机电话. */
	private String consigneeTel;

	/** 收货人手机. */
	private String mobile;

	/** 默认地址，1默认. */
	private int isDefault = 1;

	/** 创建时间. */
	private long ctime;

	public ConsigneeAddressDTO convertToDTO() {
		ConsigneeAddressDTO addressDTO = new ConsigneeAddressDTO();
		addressDTO.setUserId(userId);
		addressDTO.setProvinceId(provinceCode);
		addressDTO.setCityId(cityCode);
		addressDTO.setSectionId(sectionCode);
		addressDTO.setStreetId(streetCode);
		addressDTO.setAddress(detailAddress);
		addressDTO.setZipcode(zipCode);
		addressDTO.setAddressComment(addressComment);
		addressDTO.setConsigneeName(consigneeName);
		addressDTO.setConsigneeTel(consigneeTel);
		addressDTO.setConsigneeMobile(mobile);
		addressDTO.setIsDefault(isDefault == 1);
		return addressDTO;
	}
	
	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(long provinceCode) {
		this.provinceCode = provinceCode;
	}

	public long getCityCode() {
		return cityCode;
	}

	public void setCityCode(long cityCode) {
		this.cityCode = cityCode;
	}

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public long getStreetCode() {
		return streetCode;
	}

	public void setStreetCode(long streetCode) {
		this.streetCode = streetCode;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddressComment() {
		return addressComment;
	}

	public void setAddressComment(String addressComment) {
		this.addressComment = addressComment;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
}
