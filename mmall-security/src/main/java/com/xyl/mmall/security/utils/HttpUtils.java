/*
 * @(#)HttpUtils.java 2014-2-18
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.security.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.netease.print.common.util.CollectionUtil;

/**
*
 */
public class HttpUtils {

	private static final int HTTP_CONNECT_TIMEOUT = 3 * 1000;

	private static final int HTTP_SOCKET_TIMEOUT = 5 * 1000;

	public static String getContent(String uri) throws Exception {
		HttpClient client = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CONNECT_TIMEOUT)
				.setSocketTimeout(HTTP_SOCKET_TIMEOUT).build();
		HttpGet get = new HttpGet(uri);
		get.setConfig(requestConfig);
		HttpResponse response = client.execute(get);
		StatusLine line = response.getStatusLine();
		if (line.getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		}
		return null;
	}

	/**
	 * 获得请求中指定的Cookie对象
	 * 
	 * @param request
	 * @param cookieName
	 *            要获取的Cookie名称
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if (CollectionUtil.isNotEmptyOfArray(cookies)) {
			for (Cookie c : cookies) {
				if (c.getName().equals(cookieName))
					cookie = c;
			}
		}
		return cookie;
	}

	/**
	 * 获取请求包里的真实IP地址(跳过前端代理)
	 * 
	 * @param request
	 *            请求包对象
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		} else {
			// 如果通过了多级反向代理的话
			// 取X-Forwarded-For中第一个非unknown的有效IP字符串
			String[] subIp = ip.split(",");
			if (subIp.length > 0) {
				for (String s : subIp) {
					if (!"unknown".equalsIgnoreCase(s))
						return s;
				}
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
