/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.security.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.constant.MmallConstant;

/**
 * MmallLoginUtils.java created by yydx811 at 2015年12月7日 上午9:39:31
 * 登录判断工具类
 *
 * @author yydx811
 */
public class MmallLoginUtils {

	private static Logger logger = LoggerFactory.getLogger(MmallLoginUtils.class);
	
	private static final String randomData = "0xCAFEBABE";

	/**
	 * 判断cms登录
	 * @param request
	 * @param response
	 * @return
	 */
	public static boolean isCMSLogined(HttpServletRequest request, HttpServletResponse response) {
		// 获取cookie
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			clearCMSLoginCookie(response);
			clearMainsiteLoginCookie(response);
			return false;
		}
		// 获取cms登录cookie
		String cmsUserName = null, cmsSuss = null, cmsExpire = null;
		for (Cookie cookie : cookies) {
			if (StringUtils.equals(cookie.getName(), MmallConstant.XYL_CMS_FAILED)) {
				if (StringUtils.isNotBlank(cookie.getValue())) {
					return proxyFailed(response);
				}
			} else if (StringUtils.equals(cookie.getName(), MmallConstant.XYL_CMS_USERNAME)) {
				cmsUserName = cookie.getValue();
			} else if (StringUtils.equals(cookie.getName(), MmallConstant.XYL_CMS_SESS)) {
				cmsSuss = cookie.getValue();
			} else if (StringUtils.equals(cookie.getName(), MmallConstant.XYL_CMS_EXPIRES)) {
				cmsExpire = cookie.getValue();
			}
		}
		// 判断cms登录是否合法
		if (StringUtils.isBlank(cmsUserName) || StringUtils.isBlank(cmsSuss) || StringUtils.isBlank(cmsExpire)) {
			return proxyFailed(response);
		}
		// cms判断
		if (!StringUtils.endsWith(cmsUserName, MmallConstant.CMS_ACCOUNT_SUFFIX)) {
			cmsUserName += MmallConstant.CMS_ACCOUNT_SUFFIX;
		}

		// token判断
		String expires = "";
		try {
			long expiresTime = Long.valueOf(cmsExpire);
			if (expiresTime > System.currentTimeMillis()) {
				DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss z", Locale.US);
				df.setTimeZone(TimeZone.getTimeZone("GMT"));
				expires = df.format(new Date(expiresTime));
			} else {
				return proxyFailed(response);
			}
		} catch (Exception e) {
        	logger.error("Proxy userLogin error, format expiresTime error!", e);
			return proxyFailed(response);
		}
        String token = "";
        try {
        	token = CookieAuthenUtils.generateToken(cmsUserName, randomData, expires);
            if (!StringUtils.equals(token, cmsSuss)) {
    			return proxyFailed(response);
            }
        } catch (Exception e) {
        	logger.error("Proxy userLogin error, generate token error!", e);
			return proxyFailed(response);
        }
        return true;
	}

	/**
	 * cms登录失败，清除cms登录信息
	 * @param response
	 * @return
	 */
	public static void clearCMSLoginCookie(HttpServletResponse response) {
		Cookie xylSess = CookieUtils.createCookie(MmallConstant.XYL_CMS_SESS, null);
		xylSess.setMaxAge(0);
		response.addCookie(xylSess);

		Cookie exp = CookieUtils.createCookie(MmallConstant.XYL_CMS_EXPIRES, null);
		exp.setMaxAge(0);
		response.addCookie(exp);

	    Cookie xylUN = CookieUtils.createCookie(MmallConstant.XYL_CMS_USERNAME, null);
	    xylUN.setMaxAge(0);
        response.addCookie(xylUN);
	}

	/**
	 * 主站登录失败，清除mainsite登录信息
	 * @param response
	 * @return
	 */
	public static void clearMainsiteLoginCookie(HttpServletResponse response) {
		Cookie xylSess = CookieUtils.createCookie(MmallConstant.XYL_MAINSITE_SESS, null);
		xylSess.setMaxAge(0);
		response.addCookie(xylSess);
		
		Cookie exp = CookieUtils.createCookie(MmallConstant.XYL_MAINSITE_EXPIRES, null);
		exp.setMaxAge(0);
		response.addCookie(exp);

	    Cookie xylUN = CookieUtils.createCookie(MmallConstant.XYL_MAINSITE_USERNAME, null);
	    xylUN.setMaxAge(0);
        response.addCookie(xylUN);
        
        Cookie xylProxy = CookieUtils.createCookie(MmallConstant.XYL_MAINSITE_PROXY, null);
        xylProxy.setMaxAge(0);
        response.addCookie(xylProxy);
	}

	/**
	 * 商家登录失败，清除backend登录信息
	 * @param response
	 * @return
	 */
	public static void clearBackendLoginCookie(HttpServletResponse response) {
		Cookie xylSess = CookieUtils.createCookie(MmallConstant.XYL_BACKEND_SESS, null);
		xylSess.setMaxAge(0);
		response.addCookie(xylSess);

		Cookie exp = CookieUtils.createCookie(MmallConstant.XYL_BACKEND_EXPIRES, null);
		exp.setMaxAge(0);
		response.addCookie(exp);
		
	    Cookie xylUN = CookieUtils.createCookie(MmallConstant.XYL_BACKEND_USERNAME, null);
	    xylUN.setMaxAge(0);
        response.addCookie(xylUN);
	}

	/**
	 * 代理登录失败，清除cms，mainsite登录信息
	 * @param response
	 * @return
	 */
	public static boolean proxyFailed(HttpServletResponse response) {
		clearCMSLoginCookie(response);
		clearMainsiteLoginCookie(response);
		return false;
	}
}
