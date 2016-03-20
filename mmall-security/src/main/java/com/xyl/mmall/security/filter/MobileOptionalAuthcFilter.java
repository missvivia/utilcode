/**
 * 
 */
package com.xyl.mmall.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import com.netease.print.security.authc.NEAuthenticationToken;
import com.xyl.mmall.security.service.MobileTokenService;
import com.xyl.mmall.security.utils.HttpUtils;

/**
 * @author lihui
 *
 */
public class MobileOptionalAuthcFilter extends AccessControlFilter {

	private MobileTokenService mobileTokenService;

	/**
	 * 尝试根据请求中所带的token等信息进行认证。 由于非强制需要登录，即使认证失败，仍返回true。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean doOptionalAuthc(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		Cookie token = HttpUtils.getCookieByName(httpServletRequest, "token");
		if (token == null) {
			// 未包含token等信息，无需登录
			return true;
		}
		String userName = mobileTokenService.validateMobleToken(token.getValue());
		if (StringUtils.isEmpty(userName)) {
			// 包含token等信息，但为无效或已过期，无需登录
			return true;
		}
		// 1.获取用户访问凭证
		NEAuthenticationToken authenticationToken = new NEAuthenticationToken(userName, token, "");
		// 2.提交凭证
		try {
			Subject subject = getSubject(request, response);
			subject.login(authenticationToken);
		} catch (AuthenticationException ae) {
			// 由于非强制认证，登录失败也不会返回错误信息
			return true;
		}
		return true;
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
	 * @see org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, java.lang.Object)
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		// 总是返回false，交由onAccessDenied处理
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.web.filter.AccessControlFilter#onAccessDenied(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse)
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return doOptionalAuthc(request, response);
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
