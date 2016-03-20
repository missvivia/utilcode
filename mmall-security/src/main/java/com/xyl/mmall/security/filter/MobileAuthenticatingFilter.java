/**
 * 
 */
package com.xyl.mmall.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.netease.print.security.authc.NEAuthenticationToken;
import com.netease.print.security.util.LoginUtils;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.security.service.MobileTokenService;
import com.xyl.mmall.security.utils.HttpUtils;

/**
 * @author lihui
 *
 */
public class MobileAuthenticatingFilter extends AccessControlFilter {

	private MobileTokenService mobileTokenService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, java.lang.Object)
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		// 总是返回false，交由onAccessDenied处理
		return false;
	}

	private boolean executeLogin(ServletRequest request, ServletResponse response, String userName, String tokenValue) {
		// 1.获取用户访问凭证
		NEAuthenticationToken token = new NEAuthenticationToken(userName, tokenValue, "");
		// 2.提交凭证
		try {
			Subject subject = getSubject(request, response);
			subject.login(token);
			return true;
		} catch (AuthenticationException ae) {
			writeFailedResultToResponse(response, MobileErrorCode.LOGIN_FAIL.getIntValue(),
					MobileErrorCode.LOGIN_FAIL.getDesc());
			return LoginUtils.onLoginFailure(token, ae, request, response);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.web.servlet.AdviceFilter#afterCompletion(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, java.lang.Exception)
	 */
	public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
		Subject subject = getSubject(request, response);
		subject.logout();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.web.filter.AccessControlFilter#onAccessDenied(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse)
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return isValidToken(request, response);
	}

	/**
	 * 判断是否为有效的token
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	private boolean isValidToken(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		Cookie id = HttpUtils.getCookieByName(httpServletRequest, "id");
		Cookie token = HttpUtils.getCookieByName(httpServletRequest, "token");
		Cookie params = HttpUtils.getCookieByName(httpServletRequest, "params");
		if (token == null && (id == null || params == null)) {
			writeFailedResultToResponse(response, MobileErrorCode.UNAUTHENTICATED_USER.getIntValue(),
					MobileErrorCode.UNAUTHENTICATED_USER.getDesc());
			return false;
		}
		return validAccessToken(id == null ? null : id.getValue(), token == null ? null : token.getValue(),
				params == null ? null : params.getValue(), request, response);
	}

	/**
	 * @param id
	 * @param token
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean validAccessToken(String id, String token, String params, ServletRequest request,
			ServletResponse response) {
		String userName = null;
		if (StringUtils.isNotBlank(token)) {
			userName = mobileTokenService.validateMobleToken(token);
		}
		if (StringUtils.isEmpty(userName)) {
			if (StringUtils.isEmpty(params)) {
				writeFailedResultToResponse(response, MobileErrorCode.INVALID_TOKEN.getIntValue(),
						MobileErrorCode.INVALID_TOKEN.getDesc());
				return false;
			} else {
				Map<String, Object> result = mobileTokenService.mobileSeamlessRelogin(id, params,
						HttpUtils.getIpAddr(WebUtils.toHttp(request)));
				if (result.containsKey("code")) {
					writeFailedResultToResponse(response, Integer.parseInt(String.valueOf(result.get("code"))),
							(String) result.get("message"));
					return false;
				} else {
					if (result.get("user") == null) {
						// result中没有user对象，为第三方登录
						userName = (String) result.get("userName");
					} else {
						// result中存在user对象，为urs登录
						@SuppressWarnings("unchecked")
						Map<String, String> userMap = JsonUtils.fromJson(JsonUtils.toJson(result.get("user")),
								HashMap.class);
						userName = userMap.get("userName");
					}
					// 获取刷新后的mobile token，并设置到cookie中
					String loginResult = (String) result.get("result");
					if (StringUtils.isNotEmpty(loginResult)) {
						String[] loginResultArr = StringUtils.split(loginResult, "&");
						if (loginResultArr != null && loginResultArr.length > 0) {
							//第一个token为mobiletoken
							String[] newToken = StringUtils.split(loginResultArr[0], "=");
							if (newToken != null && newToken.length > 0) {
								setCookieWithDomain((HttpServletResponse) response, "vstoken", newToken[1], -1,
										".163.com");
							}
							//第二个token为新的urstoken
							String[] newUrsToken = StringUtils.split(loginResultArr[1], "=");
							if (newToken != null && newToken.length > 0) {
								setCookieWithDomain((HttpServletResponse) response, "urstoken", newUrsToken[1], -1,
										".163.com");
							}
						}
					}
				}
			}
		}
		return this.executeLogin(request, response, userName, token);
	}

	private void setCookieWithDomain(HttpServletResponse response, String name, String value, int expiry, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expiry);
		cookie.setDomain(domain);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	/**
	 * @param message
	 * @param code
	 * @param response
	 * 
	 */
	private void writeFailedResultToResponse(ServletResponse response, int code, String message) {
		response.setContentType(ContentType.APPLICATION_JSON.toString());
		response.setCharacterEncoding("UTF-8");
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(code);
		result.setMessage(message);
		try {
			response.getWriter().print(JsonUtils.toJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the mobileTokenService
	 */
	public MobileTokenService getMobileTokenService() {
		return mobileTokenService;
	}

	/**
	 * @param mobileTokenService
	 *            the mobileTokenService to set
	 */
	public void setMobileTokenService(MobileTokenService mobileTokenService) {
		this.mobileTokenService = mobileTokenService;
	}

}
