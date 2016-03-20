/**
 * 
 */
package com.xyl.mmall.security.service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.util.WebUtils;

import com.netease.print.security.authc.NEAuthenticationToken;
import com.netease.print.security.authc.URSOAuthServiceImpl;
import com.netease.print.security.util.URSOAuthUtils;

/**
 * @author lihui
 *
 */
public class URSOAuthServiceInternalTestingImpl extends URSOAuthServiceImpl {

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		final HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		Cookie[] credentials = URSOAuthUtils.getCredentials(httpServletRequest);
		String userName = request.getParameter("username");
		NEAuthenticationToken token = new NEAuthenticationToken(userName, credentials, "");
		return token;
	}
	
	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
		String nes = getTokenValue(request);
		if (!StringUtils.hasText(nes))
			return false;
		return true;
	}
}
