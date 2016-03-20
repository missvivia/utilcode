/**
 * 
 */
package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 用户快递信息
 * 
 * @author lwl
 * @data 2014年4月23日 下午2:02:40
 */
@AnnonOfClass(tableName = "Mmall_Order_ConsigneeAddress", desc = "收获地址信息")
public class ConsigneeAddress implements Serializable {

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

	@AnnonOfField(desc = "地址备注", type = "VARCHAR(256)", safeHtml = true)
	private String addressComment;

	@AnnonOfField(desc = "收件人-姓名", type = "VARCHAR(16)", safeHtml = true)
	private String consigneeName;

	@AnnonOfField(desc = "区号", type = "VARCHAR(8)", notNull = false, safeHtml = true)
	private String areaCode;

	@AnnonOfField(desc = "收件人-固定电话", type = "VARCHAR(32)", notNull = false, safeHtml = true)
	private String consigneeTel;

	@AnnonOfField(desc = "收件人-手机", type = "VARCHAR(16)", notNull = false, safeHtml = true)
	private String consigneeMobile;

	@AnnonOfField(desc = "是否默认收货地址", defa = "false")
	private boolean isDefault;

	@AnnonOfField(desc = "创建时间")
	private long ctime;
	
	@AnnonOfField(desc = "添加来源，0cms，1backend，2mainsite，3mobile")
	private int addFrom;

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

	/**
	 * @return the provinceId
	 */
	public long getProvinceId() {
		return provinceId;
	}

	/**
	 * @param provinceId
	 *            the provinceId to set
	 */
	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	/**
	 * @return the cityId
	 */
	public long getCityId() {
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the sectionId
	 */
	public long getSectionId() {
		return sectionId;
	}

	/**
	 * @param sectionId
	 *            the sectionId to set
	 */
	public void setSectionId(long sectionId) {
		this.sectionId = sectionId;
	}

	/**
	 * @return the streetId
	 */
	public long getStreetId() {
		return streetId;
	}

	/**
	 * @param streetId
	 *            the streetId to set
	 */
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

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
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

	public String getAddressComment() {
		return addressComment;
	}

	public void setAddressComment(String addressComment) {
		this.addressComment = addressComment;
	}

	public int getAddFrom() {
		return addFrom;
	}

	public void setAddFrom(int addFrom) {
		this.addFrom = addFrom;
	}
}
