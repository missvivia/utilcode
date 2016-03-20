/**
 * 
 */
package com.xyl.mmall.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * @author hzlihui2014
 *
 */
public class MainsiteCORSFilter implements Filter {

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		String origin = req.getHeader("Origin");
		if (StringUtils.isNotBlank(origin) && "http://m.023.baiwandian.cn".equals(origin)
				|| "http://m.023.baiwandian.cn".equals(origin)) {
			res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			res.setHeader("Access-Control-Max-Age", "3600");
			res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Cookie, Accept");
			res.setHeader("Access-Control-Allow-Credentials", "true");
			res.setHeader("Access-Control-Allow-Origin", origin);
		}
		if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
			return;
		}
		chain.doFilter(request, response);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

}
