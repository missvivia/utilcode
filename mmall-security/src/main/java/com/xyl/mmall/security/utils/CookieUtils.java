/**
 * ==================================================================
 * Copyright (c) JTI Co.ltd Hangzhou, 2012-2016
 * 
 * 杭州杰唐信息技术有限公司拥有该文件的使用、复制、修改和分发的许可权
 * 如果你想得到更多信息，请访问 <http://www.jtang.com.cn>
 *
 * JTang Co.ltd Hangzhou owns permission to use, copy, modify and
 * distribute this documentation.
 * For more information, please see <http://www.jtang.com.cn>
 * ==================================================================
 */

package com.xyl.mmall.security.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xyl.mmall.constant.MmallConstant;

/**
 * CookieUtils.java created by skh at 2015年4月14日 下午4:07:01
 * 
 *
 * @author skh
 * @version 1.0
 */

public class CookieUtils 
{
	public static final String DO_MAIN = ".baiwandian.cn";
	
    public static Cookie [] getCredentials(HttpServletRequest request) {
        Cookie cookie = getCookieByName(request, MmallConstant.XYL_MAINSITE_SESS);
        Cookie [] cookieArray = null;
        if (cookie != null) {
            cookieArray = new Cookie [1];
            cookieArray[0] = cookie;
        }
        return cookieArray;
    }

    public static Cookie [] getCMSCredentials(HttpServletRequest request) {
        Cookie cookie = getCookieByName(request, MmallConstant.XYL_CMS_SESS);
        Cookie [] cookieArray = null;
        if (cookie != null) {
            cookieArray = new Cookie [1];
            cookieArray[0] = cookie;
        }
        return cookieArray;
    }

    public static Cookie [] getBackendCredentials(HttpServletRequest request) {
        Cookie cookie = getCookieByName(request, MmallConstant.XYL_BACKEND_SESS);
        Cookie [] cookieArray = null;
        if (cookie != null) {
            cookieArray = new Cookie [1];
            cookieArray[0] = cookie;
        }
        return cookieArray;
    }
    
    public static Cookie getCookieByName(HttpServletRequest request, String key) {
        Cookie [] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }
    
    public static void cleanCookieWithDomain(HttpServletResponse response, String name,
            String domain) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    public static Cookie createCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setDomain(DO_MAIN);
		return cookie;
	}
    
    public static Cookie createCookie(String name, String value, String domain) {
    	Cookie cookie = new Cookie(name, value);
    	cookie.setPath("/");
    	cookie.setDomain(domain);
    	return cookie;
    }
}
