package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 订单快递地址信息
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(desc = "订单快递地址信息", tableName = "Mmall_Order_OrderExpInfo", dbCreateTimeName = "CreateTime")
public class OrderExpInfo implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "订单Id", primary = true)
	private long orderId;

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

	@AnnonOfField(desc = "快递地址-省份", type = "VARCHAR(16)")
	private String province;

	@AnnonOfField(desc = "快递地址-市", type = "VARCHAR(16)")
	private String city;

	@AnnonOfField(desc = "快递地址-区", type = "VARCHAR(32)")
	private String section;

	@AnnonOfField(desc = "快递地址-街道名", type = "VARCHAR(32)")
	private String street;

	@AnnonOfField(desc = "快递地址-详细地址", type = "VARCHAR(64)")
	private String address;

	@AnnonOfField(desc = "快递地址-邮政编号", type = "VARCHAR(8)")
	private String zipcode = "";

	@AnnonOfField(desc = "收件人-姓名", type = "VARCHAR(16)")
	private String consigneeName;

	@AnnonOfField(desc = "收件人-固定电话", type = "VARCHAR(32)")
	private String consigneeTel = "";

	@AnnonOfField(desc = "收件人-手机", type = "VARCHAR(16)")
	private String consigneeMobile = "";
	
	@AnnonOfField(desc = "地址备注", type = "VARCHAR(256)", safeHtml = true)
	private String addressComment;

	@AnnonOfField(desc = "区号", type = "VARCHAR(8)", notNull = false, safeHtml = true)
	private String areaCode;
	
	@AnnonOfField(desc = "修改前的地址 json格式", type = "VARCHAR(512)")
	private String modifiedValue;

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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

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
	

	public String getModifiedValue() {
		return modifiedValue;
	}

	public void setModifiedValue(String modifiedValue) {
		this.modifiedValue = modifiedValue;
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

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}