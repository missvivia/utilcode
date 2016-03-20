package com.xyl.mmall.mobile.web.facade;

import javax.servlet.http.HttpServletRequest;

/**
 * IP相关服务.
 * 
 * @author wangfeng
 * 
 */
public interface MobileIPServiceFacade {

	/**
	 * 根据ip获取省份.<br/>
	 * 国外ip无省份.
	 * 
	 * @param request
	 * @return
	 */
	public String getProvinceShortName(HttpServletRequest request);

	/**
	 * 根据省份code获取省份名称.
	 * 
	 * @param provinceCode
	 * @return
	 */
	public String getProvinceName(long provinceCode);

	/**
	 * 根据ip获取省份code.
	 * 
	 * @param request
	 * @return
	 */
	public long getProvinceCode(HttpServletRequest request);

	/**
	 * 根据省份名称获取省份code.
	 * 
	 * @param provinceShortName
	 * @return
	 */
	public long getProvinceCode(String provinceShortName);

	/**
	 * 根据ip获取市code
	 * @param request
	 * @return
	 */
	public long getCityCode(HttpServletRequest request);
	
	/**
	 * 根据ip获取市名
	 * @param request
	 * @return
	 */
	public String getCityShortName(HttpServletRequest request);
	
	/**
	 * 根据市名获取市code
	 * @param cityShortName
	 * @return
	 */
	public long getCityCode(String cityShortName);
}
