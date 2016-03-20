package com.xyl.mmall.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netease.print.security.util.DigestUtils;
import com.xyl.mmall.constant.MmallConstant;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
public final class AreaUtils {

	private static Logger logger = LoggerFactory.getLogger(AreaUtils.class);

	public static final String COOKIE_NAME_AREA = "MMALL_AREA";

	/**
	 * 1day.
	 */
	private static final int COOKIE_EXPIRY = 1 * 24 * 3600;

	public static final String AREA_VALUE_SPLIT = "|";

	/**
	 * Cookie Value split.
	 */
	public static final String VALUE_SPLIT = "###";

	private static final String OWNER_KEY = "gIvEmE5_TOT";

	private AreaUtils() {
	}

	/**
	 * 从cookie中取出province code.<br/>
	 * 其实和#getAreaCode()是一样的,只是返回类型不同.
	 * 
	 * @return
	 */
	public static int getProvinceCode() {
		return (int) getAreaCode();
	}

	/**
	 * 从cookie中取出area code.<br/>
	 * 
	 * @return
	 */
	public static long getAreaCode() {
		long areaCode = -1l;
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		if (ra != null) {
			HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
			String cookieValue = getNonSpecCookieValue(request, COOKIE_NAME_AREA);
			String[] cookieArray = StringUtils.split(cookieValue, VALUE_SPLIT);
			if (cookieArray != null && cookieArray.length >= 3 && validateSign(request)) {
				try {
					areaCode = Long.valueOf(cookieArray[0]);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			} else {
				Object code = request.getAttribute(COOKIE_NAME_AREA);
				if (code != null && code instanceof Long)
					areaCode = (Long) code;
			}
		}
		return areaCode;
	}

	/**
	 * 设置area code cookie.
	 * @param response
	 * @param cityCode
	 * @param cityName
	 * @param districtCode
	 * @param districtName
	 */
	public static void setAreaCookie(HttpServletResponse response, long areaCode) {
		long createTime = System.currentTimeMillis();
		String sign = createSign(areaCode, createTime);

		StringBuilder areaValue = new StringBuilder(256);
		areaValue.append(areaCode);
		areaValue.append(VALUE_SPLIT);
		areaValue.append(createTime);
		areaValue.append(VALUE_SPLIT);
		areaValue.append(sign);
		setCookie(response, COOKIE_NAME_AREA, areaValue.toString(), COOKIE_EXPIRY);
	}

	/**
	 * 验证area cookie真实性.
	 * 
	 * @param request
	 * @return
	 */
	public static boolean validateSign(HttpServletRequest request) {
		String cookieValue = getNonSpecCookieValue(request, COOKIE_NAME_AREA);
		String cookieValDec = StringUtils.EMPTY;
		try {
			cookieValDec = URLDecoder.decode(cookieValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		String[] cookieArray = StringUtils.split(cookieValDec, VALUE_SPLIT);
		if (cookieArray != null && cookieArray.length >= 3) {
			String areaVal = cookieArray[0];
			long createTime = Long.valueOf(cookieArray[1]);
			String sign = cookieArray[2];
			String signValue = createSign(areaVal, createTime);
			return signValue.equalsIgnoreCase(sign);
		}
		return false;
	}

	 /**
	 * 生成签名.
	 *
	 * @param input
	 * @param createTime
	 * @return
	 */
	private static String createSign(long areaCode, long createTime) {
		String input = String.valueOf(areaCode) + OWNER_KEY;
		byte[] sign = DigestUtils.sha1(input.getBytes(), String.valueOf(createTime).getBytes(), 10);
		return Hex.encodeToString(sign);
	}

	/**
	 * 生成签名.
	 * 
	 * @param input
	 * @param createTime
	 * @return
	 */
	private static String createSign(String areaVal, long createTime) {
		String input = areaVal + OWNER_KEY;
		byte[] sign = DigestUtils.sha1(input.getBytes(), String.valueOf(createTime).getBytes(), 10);
		return Hex.encodeToString(sign);
	}

	/**
	 * 设置cookie值
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry
	 */
	private static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expiry);
		cookie.setDomain(MmallConstant.MAIN_SITE_DOMAIN);
		cookie.setPath("/");
		try {
			response.addCookie(cookie);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获得请求中指定的Cookie值.<br>
	 * 取出带特殊符号(如"@")的Cookie值
	 * 
	 * @param request
	 * @param cookieName
	 *            要获取的Cookie名称
	 * @return
	 */
	private static String getNonSpecCookieValue(HttpServletRequest request, String cookieName) {
		if (cookieName == null)
			return "";

		String cookies = request.getHeader("Cookie");
		if (cookies != null) {
			cookieName = cookieName + "=";
			int fromIndex = cookies.indexOf(cookieName);
			if (fromIndex >= 0) {
				int endIndex = cookies.indexOf(";", fromIndex);
				if (endIndex >= 0) {
					return cookies.substring(fromIndex + cookieName.length(), endIndex);
				} else {
					return cookies.substring(fromIndex + cookieName.length());
				}
			}
		}
		return "";
	}

}
