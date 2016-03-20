package com.xyl.mmall.mainsite.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xyl.mmall.cms.facade.util.EncryptUtil;
import com.xyl.mmall.ip.util.IPUtils;

/**
 * 千人千面实用类。
 * 主要是记录每个IP的登陆时间并写进cookie。
 * 
 * @author hzzhanghui
 * 
 */
public final class QrqmUtils {

	private QrqmUtils() {
	}

	private static final String IP_COOKIE_NAME = "CLIENT_ID";
	
	private static final String DYING_POLIST = "CLIENT_BAK";

	private static final String ID_SPLIT = "_";

	//private static final int IP_COOKIE_EXPIRE = 60 * 60 * 24 * 8; // 8 days
	
	private static final int DYING_POLIST_EXPIRE = 60 * 60 * 24 * 30; // 1 month

	//private static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

	//private static final Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");
	
	private static void writeClientIDToCookie(String ip, HttpServletResponse response) {
		long ipLong = -1l;
		try {
			ipLong = ipToLong(ip);
		} catch (Exception e) {
			// invalid ip, won't write to cookie
			return;
		}
		String val = ipLong + ID_SPLIT + now();
		val = EncryptUtil.encrypt(val); // encrypt
		Cookie cookie = new Cookie(IP_COOKIE_NAME, val);
		//cookie.setMaxAge(IP_COOKIE_EXPIRE);
		cookie.setMaxAge(calIPCookieExpire());
		cookie.setDomain(".163.com");
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	private static int calIPCookieExpire() {
		long now = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 7);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		
		long diff = c.getTimeInMillis() - now;
		return (int)(diff/1000);
	}

	private static String readClientIDFromCookie(HttpServletRequest request) {
		String cookieVal = getNonSpecCookieValue(request, IP_COOKIE_NAME);
		if (cookieVal == null || "".equals(cookieVal.trim())) {
			return null;
		}
		cookieVal = EncryptUtil.decrypt(cookieVal);

		return cookieVal;
	}
	
	public static void writeDyingPOListToCookie(UserLoginBean bean) {
		if (bean != null && bean.dyingPOIdList != null && bean.dyingPOIdList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Long id : bean.dyingPOIdList) {
				sb.append(id).append(ID_SPLIT);
			}
			String val = sb.toString();
			val = EncryptUtil.encrypt(val); // encrypt
			Cookie cookie = new Cookie(DYING_POLIST, val);
			cookie.setMaxAge(DYING_POLIST_EXPIRE);
			cookie.setDomain(".163.com");
			cookie.setPath("/");
			bean.response.addCookie(cookie);
			bean.response = null;
		}
	}
	
	private static String readDyingPOIDListFromCookie(HttpServletRequest request) {
		String cookieVal = getNonSpecCookieValue(request, DYING_POLIST);
		if (cookieVal == null || "".equals(cookieVal.trim())) {
			return null;
		}
		cookieVal = EncryptUtil.decrypt(cookieVal);

		return cookieVal;
	}

	private static String getClientIP(String cookieVal) {
		String[] arr = cookieVal.split(ID_SPLIT);
		if (arr != null && arr.length == 2) {
			long ipLong = Long.parseLong(arr[0]);
			return longToIp(ipLong);
		}

		return null;
	}

	private static long getClientIpLastLogin(String cookieVal) {
		String[] arr = cookieVal.split(ID_SPLIT);
		if (arr != null && arr.length == 2) {
			long lastLogin = Long.parseLong(arr[1]);
			return lastLogin;
		}

		return -1L;
	}

	private static String getNonSpecCookieValue(HttpServletRequest request, String cookieName) {
		if (cookieName == null || "".equals(cookieName.toString())) {
			return null;
		}

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
		return null;
	}

	private static long ipToLong(String ipString) {
		long result = 0;
		java.util.StringTokenizer token = new java.util.StringTokenizer(ipString, ".");
		result += Long.parseLong(token.nextToken()) << 24;
		result += Long.parseLong(token.nextToken()) << 16;
		result += Long.parseLong(token.nextToken()) << 8;
		result += Long.parseLong(token.nextToken());
		return result;
	}

	private static String longToIp(long ipLong) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipLong >>> 24);
		sb.append(".");
		sb.append(String.valueOf((ipLong & 0x00FFFFFF) >>> 16));
		sb.append(".");
		sb.append(String.valueOf((ipLong & 0x0000FFFF) >>> 8));
		sb.append(".");
		sb.append(String.valueOf(ipLong & 0x000000FF));
		return sb.toString();
	}

	public static UserLoginBean getCurIPLastLoginTime(HttpServletRequest request, HttpServletResponse response) {
		return _getCurIPLastLoginTime(request, response);
	}
	
	public static UserLoginBean getCurIPLastLoginTimeWithoutResp(HttpServletRequest request) {
		return _getCurIPLastLoginTime(request, null);
	}
	
	private static UserLoginBean _getCurIPLastLoginTime(HttpServletRequest request, HttpServletResponse response) {
		try {
			UserLoginBean bean = new UserLoginBean();
	
			String ip = IPUtils.getIpAddr(request);
			bean.ip = ipToLong(ip);
			String cookieVal = readClientIDFromCookie(request);
			if (cookieVal == null || "".equals(cookieVal.trim())) { // first login
				if (response != null) {
					writeClientIDToCookie(ip, response);
				}
			} else {
				try {
					String cookieIp = getClientIP(cookieVal);
					if (cookieIp != null && ip.equals(cookieIp)) {
						long lastLogin = getClientIpLastLogin(cookieVal);
						bean.lastLogin = lastLogin;
					}
				} catch (Exception e) {
					// maybe someone hacked the cookie
				}
			}
			
			String dyingPOListVal = readDyingPOIDListFromCookie(request);
			if (dyingPOListVal != null && !"".equals(dyingPOListVal.trim())) {
				if (bean.dyingPOIdList == null) {
					bean.dyingPOIdList = new ArrayList<Long>();
				}
				String[] idArr = dyingPOListVal.split(ID_SPLIT);
				for (String id : idArr) {
					try {
						bean.dyingPOIdList.add(Long.parseLong(id));
					} catch (NumberFormatException e) {
						// maybe someone hacked the cookie
					}
				}
			}
	
			if (response != null) {
				bean.response = response;
			}
			
			return bean;
		} catch (Exception e) {
			return new UserLoginBean();
		}
	}
	
	private static long now() {
		return System.currentTimeMillis();
	}

//	private static boolean isIPv4Valid(String ip) {
//		return pattern.matcher(ip).matches();
//	}

	public static class UserLoginBean implements java.io.Serializable {
		private static final long serialVersionUID = -3925988696440682752L;

		public long ip;

		public long lastLogin = -1;
		
		public List<Long> dyingPOIdList = new ArrayList<Long>();
		
		public HttpServletResponse response;

		@Override
		public String toString() {
			return "UserLoginBean [ip=" + ip + ", lastLogin=" + lastLogin + "]";
		}
	}
}
