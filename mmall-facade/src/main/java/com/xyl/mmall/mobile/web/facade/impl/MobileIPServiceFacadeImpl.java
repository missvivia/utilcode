package com.xyl.mmall.mobile.web.facade.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.ip.service.IPService;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.ip.util.IPUtils;
import com.xyl.mmall.mobile.web.facade.MobileIPServiceFacade;


/**
 * 
 * 
 * @author wangfeng
 * 
 */
@Facade("mobileIpServiceFacade")
public class MobileIPServiceFacadeImpl implements MobileIPServiceFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(MobileIPServiceFacadeImpl.class);

	private static final String DEFAULT_PROVINCE = "火星";
	
	private static final String DEFAULT_CITY = "重庆";

	private Map<String, String> provinceShotMap = new HashMap<>();

	@Autowired
	private IPService ipService;

	@Autowired
	private LocationService locationService;

	@PostConstruct
	public void init() {
		// 1.省份映射
		// 将内蒙古映射到北京.
		Map<String, String> provinceShotMapOfTmp = new HashMap<>();
		provinceShotMapOfTmp.put("内蒙古", "北京");
		provinceShotMap = provinceShotMapOfTmp;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.IPServiceFacade#getProvinceShortName(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getProvinceShortName(HttpServletRequest request) {
		String ipAddr = IPUtils.getIpAddr(request);
		return ipService.getProvince(ipAddr);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.IPServiceFacade#getProvinceName(long)
	 */
	@Override
	public String getProvinceName(long provinceCode) {
		Map<Long, String> provinceCodeNameMap = locationService.getProvinceCodeNameMap();
		return provinceCodeNameMap.get(provinceCode);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.IPServiceFacade#getProvinceCode(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public long getProvinceCode(HttpServletRequest request) {
		// 1.根据ip地址获取省份名称
		String provinceShortName = getProvinceShortName(request);
		// 2.进行省份映射转换
		if (provinceShotMap.containsKey(provinceShortName)) {
			provinceShortName = provinceShotMap.get(provinceShortName);
		}
		long provinceCode = getProvinceCode(provinceShortName);
		// 3.打印获取不到provinceCode时候的日志信息
		if (provinceCode <= 0L) {
			String ipAddr = IPUtils.getIpAddr(request);
			String remoteAddr = request.getRemoteAddr();
			Map<String, String> headersMap = getHeadersMap(request);
			LOGGER.info("#getProvinceCode(): provinceCode=" + provinceCode + "; ipAddr=" + ipAddr + "; remoteAddr="
					+ remoteAddr + "; headers=" + JsonUtils.toJson(headersMap));
		}
		return provinceCode;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.IPServiceFacade#getProvinceCode(java.lang.String)
	 */
	@Override
	public long getProvinceCode(String provinceShortName) {
		// 1.设置默认省份
		if (StringUtils.isBlank(provinceShortName))
			provinceShortName = DEFAULT_PROVINCE;
		// 2.根据省份获取code
		long provinceCode = -1L;
		Map<Long, String> provinceCodeNameMap = locationService.getProvinceCodeNameMap();
		for (Entry<Long, String> entry : provinceCodeNameMap.entrySet()) {
			String provinceName = entry.getValue();
			if (StringUtils.startsWith(provinceName, provinceShortName)) {
				provinceCode = entry.getKey();
				break;
			}
		}
		return provinceCode;
	}

	// get request headers
	private Map<String, String> getHeadersMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}

	@Override
	public long getCityCode(HttpServletRequest request) {
		// 1.根据ip地址获取市名称
		String cityShortName = getCityShortName(request);

		long cityCode = getCityCode(cityShortName);
		// 2.打印获取不到cityCode时候的日志信息
		if (cityCode == 0L) {
			String ipAddr = IPUtils.getIpAddr(request);
			String remoteAddr = request.getRemoteAddr();
			Map<String, String> headersMap = getHeadersMap(request);
			LOGGER.info("#getCityCode(): cityCode=" + cityCode + "; ipAddr=" + ipAddr + "; remoteAddr="
					+ remoteAddr + "; headers=" + JsonUtils.toJson(headersMap));
		}
		return cityCode;
	}

	@Override
	public String getCityShortName(HttpServletRequest request) {
		String ipAddr = IPUtils.getIpAddr(request);
		return ipService.getCity(ipAddr);
	}

	@Override
	public long getCityCode(String cityShortName) {
		// 1.设置默认市
		if (StringUtils.isBlank(cityShortName))
			cityShortName = DEFAULT_CITY;
		// 2.根据市获取code
		long cityCode = 0L;
		Map<Long, String> cityCodeNameMap = locationService.getCityCodeNameMap();
		for (Entry<Long, String> entry : cityCodeNameMap.entrySet()) {
			String cityName = entry.getValue();
			if (StringUtils.startsWith(cityName, cityShortName)) {
				cityCode = entry.getKey();
				break;
			}
		}
		return cityCode;
	}
}
