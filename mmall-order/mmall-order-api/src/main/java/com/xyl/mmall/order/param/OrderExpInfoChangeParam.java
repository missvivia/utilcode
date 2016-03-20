package com.xyl.mmall.order.param;

import java.io.Serializable;

import com.xyl.mmall.order.enums.OperateUserType;

/**
 * 修改订单收货地址信息的参数
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月23日 下午4:58:05
 *
 */
public class OrderExpInfoChangeParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3272724469286089779L;

	public OrderExpInfoChangeParam() {
	}
	
	//(desc = "快递地址-省份", type = "VARCHAR(16)")
	private String province = "";

	//(desc = "快递地址-市", type = "VARCHAR(16)")
	private String city = "";

	//(desc = "快递地址-区", type = "VARCHAR(16)")
	private String section = "";

	//(desc = "快递地址-街道名", type = "VARCHAR(16)")
	private String street = "";
	
	//(desc = "快递地址-详细地址", type = "VARCHAR(64)")
	private String address = "";

	//(desc = "收件人-姓名", type = "VARCHAR(16)")
	private String consigneeName = "";
	
	//(desc = "区号", type = "VARCHAR(16)")
	private String areaCode ="";
	//(desc = "收件人-固定电话", type = "VARCHAR(32)")
	private String consigneeTel = "";

	//(desc = "收件人-手机", type = "VARCHAR(16)")
	private String consigneeMobile = "";
	
	//修改人
	private long operatorId;
	
	//修改备注
	private String comment;
	
	//操作用户类型
	private OperateUserType operateUserType;
	
	private String zipcode;
	

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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(long operatorId) {
		this.operatorId = operatorId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public OperateUserType getOperateUserType() {
		return operateUserType;
	}

	public void setOperateUserType(OperateUserType operateUserType) {
		this.operateUserType = operateUserType;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	
	
}
