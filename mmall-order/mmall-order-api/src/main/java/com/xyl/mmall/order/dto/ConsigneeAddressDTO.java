package com.xyl.mmall.order.dto;

import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.ConsigneeAddress;

/**
 * 收件人
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午12:22:32
 *
 */
public class ConsigneeAddressDTO extends ConsigneeAddress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "快递地址-省份名", inDB = false)
	private String province;

	@AnnonOfField(desc = "快递地址-市名", inDB = false)
	private String city;

	@AnnonOfField(desc = "快递地址-区名", inDB = false)
	private String section;

	@AnnonOfField(desc = "快递地址-街道名", inDB = false)
	private String street;

	/**
	 * 默认构造函数
	 */
	public ConsigneeAddressDTO() {
	}

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public ConsigneeAddressDTO(ConsigneeAddress obj) {
		ReflectUtil.convertObj(this, obj, false);
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
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * @param section
	 *            the section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String mergeAddress() {
		StringBuffer strBuf = new StringBuffer(128);
		if(null != province) {
			strBuf.append(province);
		}
		if(null != city) {
			strBuf.append(city);
		}
		if(null != section) {
			strBuf.append(section);
		}
		if(null != street) {
			strBuf.append(street);
		}
		if(null != getAddress()) {
			strBuf.append(getAddress());
		}
		return strBuf.toString();
	}
}
