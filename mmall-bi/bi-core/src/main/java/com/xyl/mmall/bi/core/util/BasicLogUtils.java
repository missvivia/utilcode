package com.xyl.mmall.bi.core.util;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.codec.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.security.util.DigestUtils;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.bi.core.enums.BIType;
import com.xyl.mmall.bi.core.enums.ClientType;
import com.xyl.mmall.bi.core.enums.OpAction;
import com.xyl.mmall.bi.core.meta.ApiConsumerLog;
import com.xyl.mmall.bi.core.meta.AppLog;
import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.core.meta.WebLog;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
public final class BasicLogUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicLogUtils.class);

	/** bi组需要的cookie **/
	private static final String DA_USER_COOKIE_NAME = "_da_ntes_uid";

	private static final int COOKIE_MAX_AGE = 365 * 24 * 60 * 60;

	private static final String DA_DOMAIN = ".baiwandian.cn";

	/** 省份code cookie. */
	private static final String COOKIE_NAME_AREA = "MMALL_AREA";

	/**
	 * Cookie Value split.
	 */
	public static final String VALUE_SPLIT = "###";

	private static final String OWNER_KEY = "gIvEmE5_TOT";

	private BasicLogUtils() {
	}

    public static String getAccountId()
    {
        long userId = -1;
        try
        {
            userId = SecurityContextUtils.getUserId();
        }
        catch (Exception e)
        {
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("", 3);
            }
        }
        if (userId != -1)
            return String.valueOf(userId);
        return "";
    }

	public static void setBasicLogInfo(BasicLog basicLog, BILog biLog) {
		String clientTypeValue = biLog.clientType();
		String type = biLog.type();
		String action = biLog.action();

		basicLog.setAccountId(getAccountId());
		basicLog.setType(BIType.UNKNOWN.genEnumByValue(type));
		basicLog.setTime(System.currentTimeMillis());
		basicLog.setClientType(ClientType.NULL.genEnumByValue(clientTypeValue));

		OpAction opAction = OpAction.UNKNOWN.genEnumByValue(action);
		basicLog.setAction(opAction);
		setReferer(basicLog, biLog);
		setUserAgentInfo(basicLog);
		setProvinceCode(basicLog);
	}

	/**
	 * 设置web端通用信息.
	 * 
	 * @param webLog
	 */
	public static void setWebLogInfo(WebLog webLog) {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		if (ra != null) {
			HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
			UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
			Browser browser = userAgent.getBrowser();
			Cookie[] cookieArray = request.getCookies();
			boolean isUserCookieExisit = false;
			StringBuilder cookieValue = new StringBuilder(256);
			// 1.先尝试从cookie中取标识bi user的值
			if (!CollectionUtil.isEmptyOfArray(cookieArray)) {
				for (Cookie cookie : cookieArray) {
					String cookieName = cookie.getName();
					if (DA_USER_COOKIE_NAME.equals(cookie.getName())) {
						cookieValue.append(cookieName);
						cookieValue.append("=");
						cookieValue.append(cookie.getValue());
						cookieValue.append(";");
						isUserCookieExisit = true;
						break;
					}
				}
			}
			// 2.没的话再尝试从request中取
			if (!isUserCookieExisit) {
				Object userCookie = request.getAttribute(DA_USER_COOKIE_NAME);
				if (userCookie != null && userCookie instanceof String) {
					cookieValue.append(DA_USER_COOKIE_NAME);
					cookieValue.append("=");
					cookieValue.append(String.valueOf(userCookie));
					cookieValue.append(";");
				}
			}
			webLog.setCookie(cookieValue.toString());
			webLog.setBrowser(browser.getName());
		}
	}

	public static void setWebLogInfo(WebLog webLog, BILog biLog) {
		setBasicLogInfo(webLog, biLog);
		setWebLogInfo(webLog);
	}
	
	public static void setApiConsumerLogInfo(ApiConsumerLog apiConsumerLog, BILog biLog)
	{
	    setBasicLogInfo(apiConsumerLog, biLog);
	    setApiConsumerLogInfo(apiConsumerLog);
	}
	
    public static void setApiConsumerLogInfo(ApiConsumerLog apiConsumerLog)
    {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null)
        {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes)
                    .getRequest();
            Enumeration<String> parameterNames = request.getParameterNames();
            StringBuilder parameters = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            while (parameterNames.hasMoreElements())
            {
                String parameterName = parameterNames.nextElement();
                String [] parameterValues = request.getParameterValues(parameterName);
                StringBuilder parameterValue = new StringBuilder();
                for (String value : parameterValues)
                {
                    parameterValue.append(value + " ");
                }
                parameters.append(parameterName + " : " + parameterValue).append(newLine);
            }
            apiConsumerLog.setParameters(parameters.toString().trim());
        }
    }

	/**
	 * 设置app端通用信息.暂时没用到，applog与web一致.
	 * 
	 * @param appLog
	 */
	public static void setAppLogInfo(AppLog appLog) {

	}

	public static void setAppLogInfo(AppLog appLog, BILog biLog) {
		setBasicLogInfo(appLog, biLog);
//		setAppLogInfo(appLog);
	}

	/**
	 * 设置浏览器和电脑相关信息.
	 * 
	 * @param basicLog
	 */
	public static void setUserAgentInfo(BasicLog basicLog) {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		if (ra != null) {
			HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
			// ipAddr
			basicLog.setIp(getIpAddr(request));
			// deviceOs
			UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
			OperatingSystem os = userAgent.getOperatingSystem();
			basicLog.setDeviceOs(os.getName());
			// deviceType
			DeviceType deviceType = os.getDeviceType();
			basicLog.setDeviceType(deviceType.getName());
		}
	}

	/**
	 * 设置referer
	 * 
	 * @param basicLog
	 * @param biLog
	 */
    public static void setReferer(BasicLog basicLog, BILog biLog)
    {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra != null)
        {
            String action = biLog.action();
            OpAction opAction = OpAction.UNKNOWN.genEnumByValue(action);
            HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
            if (opAction == OpAction.PAGE)
            {
                // 用户请求来源url
                String referer = request.getHeader("Referer");
                if (StringUtils.isNotBlank(referer))
                    basicLog.setReferer(referer);
                // 请求url
                // basicLog.setRequestURL(getFullURL(request));
            }
            else if (opAction == OpAction.CLICK)
            {
                // 请求url
                basicLog.setReferer(getFullURL(request));
            }
            else if(opAction == OpAction.API_ACC)
            {
                basicLog.setReferer(getFullURL(request));
            }
        }
    }

	private static String getFullURL(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		if (StringUtils.isNotBlank(queryString)) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

	/**
	 * 设置省份code.
	 * 
	 * @param basicLog
	 */
	private static void setProvinceCode(BasicLog basicLog) {
		long provinceCode = -1L;
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		if (ra != null) {
			HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
			String cookieValue = getNonSpecCookieValue(request, COOKIE_NAME_AREA);
			String[] cookieArray = StringUtils.split(cookieValue, VALUE_SPLIT);
			if (cookieArray != null && cookieArray.length >= 3 && validateSign(request)) {
				provinceCode = Long.valueOf(cookieArray[0]);
			} else {
				Object areaCode = request.getAttribute(COOKIE_NAME_AREA);
				if (areaCode != null && areaCode instanceof Long)
					provinceCode = (long) areaCode;
			}
		}
		basicLog.setProvinceCode(String.valueOf(provinceCode));
	}

	/**
	 * 验证area cookie真实性.
	 * 
	 * @param request
	 * @return
	 */
	private static boolean validateSign(HttpServletRequest request) {
		String cookieValue = getNonSpecCookieValue(request, COOKIE_NAME_AREA);
		String[] cookieArray = StringUtils.split(cookieValue, VALUE_SPLIT);
		if (cookieArray != null && cookieArray.length >= 3) {
			long areaCode = Long.valueOf(cookieArray[0]);
			long createTime = Long.valueOf(cookieArray[1]);
			String sign = cookieArray[2];
			String signValue = createSign(areaCode, createTime);
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

	/**
	 * 获取ip地址.
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-From-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// 如果通过了多级反向代理的话
		// 取X-Forwarded-For中最后一个非unknown的有效IP字符串(具体根据sa配置的nginx规则设置)
		if (StringUtils.isNotBlank(ip) && ip.indexOf(',') > -1) {
			String[] subIp = ip.split(",");
			if (subIp.length > 0) {
				for (String s : subIp) {
					if (!"unknown".equalsIgnoreCase(s))
						ip = StringUtils.trimToEmpty(s);
				}
			}
		}
		return ip;
	}

	/**
	 * 设置bi要求的用于user判断唯一值.
	 * 
	 * @param request
	 * @param response
	 */
	public static void setBICookieOfUser(HttpServletRequest request, HttpServletResponse response) {
		boolean isUserCookieExisit = false;
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (DA_USER_COOKIE_NAME.equals(cookie.getName())) {
					isUserCookieExisit = true;
					cookie.setPath("/");
					cookie.setDomain(DA_DOMAIN);
					cookie.setMaxAge(COOKIE_MAX_AGE);
					response.addCookie(cookie);
					break;
				}
			}
		}
		if (!isUserCookieExisit) {
			String cookieUser = RandomStringUtils.randomAlphanumeric(24);
			Cookie cookie = new Cookie(DA_USER_COOKIE_NAME, cookieUser);
			cookie.setPath("/");
			cookie.setDomain(DA_DOMAIN);
			cookie.setMaxAge(COOKIE_MAX_AGE);
			response.addCookie(cookie);
			// 防止同一个请求获取不到值的情况.
			request.setAttribute(DA_USER_COOKIE_NAME, cookieUser);
		}
	}

}
