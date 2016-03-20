/**
 * 
 */
package com.xyl.mmall.security.utils;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.security.authc.NEAuthenticationToken;
import com.xyl.mmall.security.exception.UnauthenticatedAccountException;

/**
 * 提供登录相关功能。
 * 
 * @author lihui
 *
 */
public final class SimpleLoginUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLoginUtils.class);

	/**
	 * 第三方登录.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static boolean executeLogin(String userName, ServletRequest request, ServletResponse response)
			throws Exception {
		try {
			// 1.获取用户访问凭证
			NEAuthenticationToken token = new NEAuthenticationToken(userName, "");
			// 2.提交凭证
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			// 3.成功返回
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 跳转至指定的未授权用户错误页面。
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @throws IOException
	 */
	public static String redirectToUnauthenticated(ServletRequest request, ServletResponse response, Exception e)
			throws IOException {
		HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
		if (e instanceof UnauthenticatedAccountException) {
			httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} else if (e instanceof LockedAccountException) {
			httpServletResponse.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED);
		} else if (e instanceof UnauthorizedException) {
			httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
		} else {
			httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
}
