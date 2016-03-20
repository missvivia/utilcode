/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.meta;

/**
 * 仓库基本信息
 * 
 * @author hzzengchengyuan
 *
 */
public class BaseWarehouseInfo {
	/**
	 * 货主编码
	 */
	private String ownerCode;

	/**
	 * 货主名称
	 */
	private String ownerName;

	/**
	 * 仓库在WMS里的唯一标示
	 */
	private String appKey;

	/**
	 * Sign加密方式ase64(MD5(content+keyValue))，keyValue由WMS提供
	 */
	private String keyValue;

	/**
	 * 仓库服务url
	 */
	private String serviceUrl;

	/**
	 * @return the ownerCode
	 */
	public String getOwnerCode() {
		return ownerCode;
	}

	/**
	 * @param ownerCode
	 *            the ownerCode to set
	 */
	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName
	 *            the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * @return the appKey
	 */
	public String getAppKey() {
		return appKey;
	}

	/**
	 * @param appKey
	 *            the appKey to set
	 */
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	/**
	 * @return the keyValue
	 */
	public String getKeyValue() {
		return keyValue;
	}

	/**
	 * @param keyValue
	 *            the keyValue to set
	 */
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * @return the serviceUrl
	 */
	public String getServiceUrl() {
		return serviceUrl;
	}

	/**
	 * @param serviceUrl
	 *            the serviceUrl to set
	 */
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

}