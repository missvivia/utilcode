package com.xyl.mmall.service;

import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.ip.util.IPUtils;
import com.xyl.mmall.mobile.facade.converter.MobileOS;
import com.xyl.mmall.mobile.facade.param.MobileHeaderAO;
import com.xyl.mmall.security.utils.HttpUtils;

/**
 * 
 * @author jiangww
 *
 */
public class MobileHeaderProcess {
	//private Logger logger = LoggerFactory.getLogger(getClass());
	public static long getUserId() {
		long userid = SecurityContextUtils.getUserId();
		if (userid <= 0l)
			userid = 0l;
		return userid;
	}
	
	public static String getUserName() {
		String userid = SecurityContextUtils.getUserName();
		if (userid == null)
			userid = "";
		return userid;
	}

	public static int getOS(MobileHeaderAO ao) {
		return MobileOS.getOs(ao.getOs()).getIntValue();
	}
	
	public static String getToken(HttpServletRequest request) {
		Cookie token = HttpUtils.getCookieByName(request, "token");
		return token.getValue();
	}

	public static int getAreaCode(MobileHeaderAO ao) {
		try {
			if (StringUtils.isNotBlank(ao.getAreaCode())) {
				if (NumberUtils.isNumber(ao.getAreaCode())) {
					return Integer.parseInt(ao.getAreaCode());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 获取头或者cookie 里的信息
	 * 
	 * @param request
	 * @return
	 */
	public static MobileHeaderAO extraFromHeader(HttpServletRequest request) {
		MobileHeaderAO ao = new MobileHeaderAO();
		if (request != null && request.getHeaderNames() != null) {
			ao.setIp(IPUtils.getIpAddr(request));
			String param = request.getHeader("AppInfo");
			String referer = request.getHeader("Referer");
			try {
				fillInHeader(ao, param);
				ao.setReferer(referer);
				/*if(protocolVersion(ao.getProtocolVersion()) > protocolVersion("1.1.1")){
					throw new Exception("higher protocolVersion");
				}*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		request.setAttribute("ao", ao);
		return ao;
	}

	private static void fillInHeader(MobileHeaderAO ao, String param) throws Exception {
		if (StringUtils.isNotBlank(param)) {
			String[] params = param.split("&");
			for (String key : params) {
				if (key.contains("=")) {
					int start = key.indexOf('=');
					String name = key.substring(0, start);
					start = start + 1;
					if (key.length() > start) {
						String value = key.substring(start, key.length());
						value = URLDecoder.decode(value, "utf-8");
						BeanUtils.setProperty(ao, name, value);
					}
				}
			}
		}
	}

}
