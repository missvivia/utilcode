package com.xyl.mmall.ip.service;


/**
 * 
 * 
 * @author wangfeng
 * 
 */
public interface IPService {

	/**
	 * 根据ip获取省份.<br/>
	 * 国外ip无省份.
	 * 
	 * @param request
	 * @return
	 */
	public String getProvince(String ipAddr);
	
	/**
	 * ip解析获取市名
	 * @param ipAddr
	 * @return
	 */
	public String getCity(String ipAddr);

	/**
	 * ip解析获取全部名
	 * @param ipAddr
	 * @return
	 */
	public String getAll(String ipAddr);
}
