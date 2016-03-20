package com.xyl.mmall.order.param;

import java.io.Serializable;

import com.xyl.mmall.order.dto.CODBlacklistAddressDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;

/**
 * 到付审核地址黑名单参数
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 下午2:13:05
 *
 */
public class CODWBlistAddressParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4790525609560264272L;

	//(desc = "用户Id")
	private long userId;
	
	//(desc = "快递地址-省份")
	private String province;
	
	//(desc = "快递地址-市")
	private String city;
	
	//(desc = "快递地址-区")
	private String section;
	
	//(desc = "快递地址-街道")
	private String street;
	
	//(desc = "快递地址-详细地址")
	private String address;
	
	// (desc = "收货人电话")
	private String consigneeMobile;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}
	
	public String mergeAddress() {
		StringBuffer strBuf = new StringBuffer(128);
		strBuf.append(this.getProvince())
		  .append(this.getCity())
		  .append(this.getSection())
		  .append(this.getStreet())
		  .append(this.getAddress());
		return strBuf.toString();
	}
	
	public void fillWithOrderExpInfo(OrderExpInfoDTO expInfo) {
		if(null != expInfo) {
			setUserId(expInfo.getUserId());
			setProvince(expInfo.getProvince());
			setCity(expInfo.getCity());
			setSection(expInfo.getSection());
			setStreet(expInfo.getStreet());
			setAddress(expInfo.getAddress());
			setConsigneeMobile(expInfo.getConsigneeMobile());
		}
	}
	
	public boolean hitBlack(CODBlacklistAddressDTO ba) {
		if(null == ba) {
			return false;
		}
		if(userId != ba.getUserId()) {
			return false;
		}
		if(!getConsigneeMobile().equals(ba.getConsigneeMobile())) {
			return false;
		}
		if(!getProvince().equals(ba.getProvince())) {
			return false;
		}
		if(!getCity().equals(ba.getCity())) {
			return false;
		}
		if(!getSection().equals(ba.getSection())) {
			return false;
		}
		if(!getStreet().equals(ba.getStreet())) {
			return false;
		}
		if(!getAddress().equals(ba.getAddress())) {
			return false;
		}
		return true;
	} 
}
