/**
 * 
 */
package com.xyl.mmall.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.xyl.mmall.security.exception.UnauthenticatedAccountException;

/**
 * @author lihui
 *
 */
public class AuthenticatingExceptionFilter extends OncePerRequestFilter {

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			// 针对特殊的认证授权作物，设置response的状态代码
			if (ExceptionUtils.indexOfThrowable(e, UnauthenticatedAccountException.class) >= 0) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} else if (ExceptionUtils.indexOfThrowable(e, LockedAccountException.class) >= 0) {
				response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED);
			} else if (ExceptionUtils.indexOfThrowable(e, UnauthorizedException.class) >= 0) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			} else {
				throw e;
			}
		}
	}
}
