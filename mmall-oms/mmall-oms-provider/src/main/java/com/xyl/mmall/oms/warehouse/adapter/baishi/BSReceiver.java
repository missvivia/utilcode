/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.baishi;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * 收件人信息
 * @author hzzengchengyuan
 * 
 */
@XmlAccessorType (XmlAccessType.FIELD)
@MapClass(WMSSalesOrderDTO.class)
public class BSReceiver implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;
	/**
	 * 个人姓名.是否可以为空：N
	 */
	@MapField("receiverName")
	private String name;

	/**
	 * 邮编.是否可以为空：Y
	 */
	@MapField("receiverPostCode")
	private String postalCode;

	/**
	 * 电话，包括区号、电话号码及分机号，中间用“-”分隔.是否可以为空：Y
	 */
	@MapField("receiverPhone")
	private String phoneNumber;

	/**
	 * 移动电话.是否可以为空：Y
	 */
	@MapField("receiverMobile")
	private String mobileNumber;

	/**
	 * 所在省，如浙江省、北京等.是否可以为空：Y
	 */
	@MapField("receiverProvince")
	private String province;

	/**
	 * 所在市，如杭州市、上海市等.是否可以为空：Y
	 */
	@MapField("receiverCity")
	private String city;

	/**
	 * 所在县（区），注意有些市下面是没有区的，如：义乌市.是否可以为空：Y
	 */
	@MapField("receiverDistrict")
	private String district;

	/**
	 * 详细地址.是否可以为空：Y
	 */
	@MapField("receiverAddress")
	private String shippingAddress;

	/**
	 * 邮件.是否可以为空：Y
	 */
	private String email;

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
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode
	 *            the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
	 * @return the shippingAddress
	 */
	public String getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * @param shippingAddress
	 *            the shippingAddress to set
	 */
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}