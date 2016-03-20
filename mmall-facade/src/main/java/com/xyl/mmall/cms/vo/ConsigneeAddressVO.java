/**
 * 
 */
package com.xyl.mmall.cms.vo;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class ConsigneeAddressVO {

	private ConsigneeAddressDTO consigneeAddress;

	private boolean blackListAddress = false;

	public ConsigneeAddressVO() {
		this.setConsigneeAddress(new ConsigneeAddressDTO());
	}

	public ConsigneeAddressVO(ConsigneeAddressDTO consigneeAddress) {
		this.setConsigneeAddress(consigneeAddress);
	}

	/**
	 * @return the consigneeAddress
	 */
	@JsonIgnore
	public ConsigneeAddressDTO getConsigneeAddress() {
		return consigneeAddress;
	}

	/**
	 * @param consigneeAddress
	 *            the consigneeAddress to set
	 */
	public void setConsigneeAddress(ConsigneeAddressDTO consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	/**
	 * @return the blackListAddress
	 */
	public boolean isBlackListAddress() {
		return blackListAddress;
	}

	/**
	 * @param blackListAddress
	 *            the blackListAddress to set
	 */
	public void setBlackListAddress(boolean blackListAddress) {
		this.blackListAddress = blackListAddress;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return StringUtils.trimToEmpty(consigneeAddress.getProvince());
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return StringUtils.trimToEmpty(consigneeAddress.getCity());
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return StringUtils.trimToEmpty(consigneeAddress.getSection());
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return StringUtils.trimToEmpty(consigneeAddress.getStreet());
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return consigneeAddress.getAddress();
	}

	/**
	 * @return the consigneeName
	 */
	public String getConsigneeName() {
		return consigneeAddress.getConsigneeName();
	}

	/**
	 * @return the consigneeTel
	 */
	public String getConsigneeTel() {
		return consigneeAddress.getConsigneeTel();
	}

	/**
	 * @return the consigneeMobile
	 */
	public String getConsigneeMobile() {
		return consigneeAddress.getConsigneeMobile();
	}

	/**
	 * @return the defaultAddress
	 */
	public boolean isDefaultAddress() {
		return consigneeAddress.isDefault();
	}

}
