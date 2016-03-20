/**
 * 
 */
package com.xyl.mmall.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.netease.print.security.authc.AccountValidator;
import com.netease.print.security.authc.NEAccessUser;
import com.netease.print.security.authc.NEAuthenticationToken;
import com.netease.print.security.filter.NEAuthenticatingFilter;
import com.netease.print.security.user.SimpleUserType;
import com.netease.print.security.util.FullUserNameUtils;
import com.netease.print.security.util.URSAuthcUtils;
import com.netease.print.security.util.URSOAuthUtils;

/**
 * @author lihui
 *
 */
public class NEAuthenticatingMainSiteFilter extends NEAuthenticatingFilter {

	private String mobileLoginPath;

	private String mobilePathPattern;
	
	private AccountValidator accountValidator;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.web.filter.AccessControlFilter#getLoginUrl()
	 */
	public String getLoginUrl() {
		// 重写获取login url的方法。根据请求的path决定
		// 是否返回正常的login页面或者mobile的login页面。
		// 由于在redirectToLogin中已经保存request，可以直接获取被保存的reqeust
		try {
			SavedRequest savedRequest = WebUtils.getSavedRequest(null);
			// 判断是否为mobile页面的请求
			return isMobilePageRequst(savedRequest) && StringUtils.isNotBlank(mobileLoginPath) ? mobileLoginPath
					: super.getLoginUrl();
		} catch (Exception e) {
			// application 初始化时将check是否存在login URL，放过该异常
			if (e instanceof UnavailableSecurityManagerException) {
				return super.getLoginUrl();
			}
			throw e;
		}
	}

	protected String getTokenValue(ServletRequest request) {
		// 必须按照下面的顺序,读取认证信息
		// 检查是否为普通URS登录cookie.NTES_SESS
		String tokenValue = super.getTokenValue(request);
		if (StringUtils.isBlank(tokenValue)) {
			// 检查是否有相应的cookie.NTES_OSESS
			final HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
			tokenValue = URSOAuthUtils.getCredentialsFromRequest(httpServletRequest);
			if (StringUtils.isBlank(tokenValue)) {
				tokenValue = URSOAuthUtils.getCredentialsFromCookie(httpServletRequest);
			}
		}
		return tokenValue;
	}
	
	/**
	 * 创建用户访问凭证.
	 */
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		final HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		Cookie[] credentials = URSAuthcUtils.getCredentials(httpServletRequest);
		SimpleUserType simpleUserType = SimpleUserType.URS;
		String userName = null;
		if (credentials[0] == null) {
			credentials = URSOAuthUtils.getCredentials(httpServletRequest);
			userName = accountValidator.getLoginUsername(credentials);
			simpleUserType = SimpleUserType.getSimpleUserTypeOfUrsOAuth(userName);
		} else {
			userName = accountValidator.getLoginUsername(credentials);
		}
		NEAuthenticationToken token = new NEAuthenticationToken(userName, credentials, simpleUserType);
		return token;
	}
	
	/**
	 * 判断是否为mobile页面的请求
	 * 
	 * @param savedRequest
	 * @return
	 */
	private boolean isMobilePageRequst(SavedRequest savedRequest) {
		if (null == savedRequest) {
			return false;
		}
		String requestURI = savedRequest.getRequestURI();
		PathMatcher pathMatcher = new AntPathMatcher();
		return StringUtils.isNotBlank(requestURI) && StringUtils.isNotBlank(mobilePathPattern)
				&& pathMatcher.match(mobilePathPattern, requestURI);
	}

	/**
	 * 是否已登录.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isLoggedIn(ServletRequest request, ServletResponse response) {
		boolean loggedIn = false;
		Subject currentUser = getSubject(request, response);
		if (currentUser != null && currentUser.isAuthenticated()) {
			NEAccessUser neAccessUser = (NEAccessUser) currentUser.getPrincipal();
			final HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
			HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
			Cookie[] credentialsOfURS = URSAuthcUtils.getCredentials(httpServletRequest);
			Cookie[] credentialsOfURSOAuth = URSOAuthUtils.getCredentials(httpServletRequest);
			if (neAccessUser != null && neAccessUser.getUseType() == SimpleUserType.URS) {
				// 1.URS用户检查用户名和当前获取的凭证是否能够对应
				loggedIn = isLoggedInOfURS(neAccessUser, credentialsOfURS, httpServletRequest, httpServletResponse);
			} else if (neAccessUser != null && neAccessUser.getUseType() != SimpleUserType.URS
					&& credentialsOfURSOAuth[0] != null) {
				// 2.URS OAuth用户
				loggedIn = isLoggedInOfURS(neAccessUser, credentialsOfURSOAuth, httpServletRequest, httpServletResponse);
			}
			//如果当前session用户和URS cookie不匹配，退出当前session用户
			if(!loggedIn){
				currentUser.logout();
			}
		}
		return loggedIn;
	}

	/**
	 * 判断URS或者URS OAuth用户是否已登录.
	 * 
	 * @param currentUser
	 * @param neAccessUser
	 * @param cookies
	 * @return
	 */
	private boolean isLoggedInOfURS(NEAccessUser neAccessUser, Cookie[] cookies, HttpServletRequest httpServletRequest,
			HttpServletResponse HttpServletResponse) {
		// 1.是否测试状态
		if (accountValidator.isTestMode())
			return true;

		boolean loggedIn = false;
		String userName = FullUserNameUtils.getShortUserName(neAccessUser.getVistorName());
		if (cookies[0] != null)
			loggedIn = accountValidator.isValidCredential(userName, cookies);
		return loggedIn;
	}
	
	/**
	 * @return the mobilePathPattern
	 */
	public String getMobilePathPattern() {
		return mobilePathPattern;
	}

	/**
	 * @param mobilePathPattern
	 *            the mobilePathPattern to set
	 */
	public void setMobilePathPattern(String mobilePathPattern) {
		this.mobilePathPattern = mobilePathPattern;
	}

	/**
	 * @return the mobileLoginPath
	 */
	public String getMobileLoginPath() {
		return mobileLoginPath;
	}

	/**
	 * @param mobileLoginPath
	 *            the mobileLoginPath to set
	 */
	public void setMobileLoginPath(String mobileLoginPath) {
		this.mobileLoginPath = mobileLoginPath;
	}
	
	public void setAccountValidator(AccountValidator accountValidator) {
		super.setAccountValidator(accountValidator);
		this.accountValidator = accountValidator;
	}
}
