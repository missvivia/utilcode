package com.xyl.mmall.ip.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.netease.backend.ipservice.common.HttpIPData;
import com.netease.backend.ipservice.ipQuery.IpQuery;
import com.xyl.mmall.ip.service.IPService;

/**
 * 
 * @author wangfeng
 * 
 */
@Service("ipService")
public class IPServiceImpl implements IPService {

	private static final Logger logger = LoggerFactory.getLogger(IPServiceImpl.class);

	private static final String DEFAULT_IP_ADDRESS = "60.191.67.195";
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.ip.service.IPService#getProvince(java.lang.String)
	 */
	@Override
	public String getProvince(String ipAddr) {
		HttpIPData ipResult = null;
		try {
			if (StringUtils.isNotBlank(ipAddr) && !StringUtils.equals("127.0.0.1", ipAddr))
				ipResult = IpQuery.getIPData(ipAddr);
			else
				ipResult = IpQuery.getIPData(DEFAULT_IP_ADDRESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (ipResult != null && ipResult.getResCode() == 200) {
			return ipResult.getProvince();
		}
		return "";
	}

	public static void main(String args[]) {
		HttpIPData ipResult;
		try {
			ipResult = IpQuery.getIPData("103.41.52.91");
//			ipResult = IpQuery.getIPData("192.168.25.2");
			
			System.out.println(ipResult.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public String getCity(String ipAddr) {
		HttpIPData ipResult = null;
		try {
			if (StringUtils.isNotBlank(ipAddr) && !StringUtils.equals("127.0.0.1", ipAddr))
				ipResult = IpQuery.getIPData(ipAddr);
			else
				ipResult = IpQuery.getIPData(DEFAULT_IP_ADDRESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (ipResult != null && ipResult.getResCode() == 200) {
			String provinceName = ipResult.getProvince();
			if (StringUtils.startsWith(provinceName, "北京") || StringUtils.startsWith(provinceName, "天津")
					|| StringUtils.startsWith(provinceName, "上海") || StringUtils.startsWith(provinceName, "重庆")) {
				return  provinceName;
			}
			return ipResult.getCity();
		}
		return "";
	}

	@Override
	public String getAll(String ipAddr) {
		HttpIPData ipResult = null;
		try {
			if (StringUtils.isNotBlank(ipAddr) && !StringUtils.equals("127.0.0.1", ipAddr))
				ipResult = IpQuery.getIPData(ipAddr);
			else
				ipResult = IpQuery.getIPData(DEFAULT_IP_ADDRESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (ipResult != null && ipResult.getResCode() == 200) {
			return ipResult.getCountry() + " " + ipResult.getProvince() + "省 " + ipResult.getCity() + "市";
		}
		return "";
	}
	
}
