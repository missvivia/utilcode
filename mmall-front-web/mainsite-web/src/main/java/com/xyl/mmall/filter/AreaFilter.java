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

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.util.BasicLogUtils;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.util.AreaUtils;

/**
 * 地址过滤器.<br/>
 * 
 * @author wangfeng
 * 
 */
public class AreaFilter implements Filter {

	// private IPServiceFacade ipServiceFacade;

	private ConsigneeAddressFacade addressFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(filterConfig
				.getServletContext());
		// ipServiceFacade = (IPServiceFacade) wac.getBean("ipServiceFacade");
		addressFacade = (ConsigneeAddressFacade) wac.getBean("consigneeAddressFacade");
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
		// 先判断cookie,没有则判断ip,同时写cookie,cookie有效期暂定1天
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		if (!AreaUtils.validateSign(httpServletRequest)) {
			// ipServiceFacade.getCityCode(httpServletRequest);
			long sectionCode = 500112l;// 默认重庆 渝北区,不允许切换
			// 当小B没有设置收货地址，则默认首页的送货地址为渝北区
			long uid = 0l;
			try {
				uid = SecurityContextUtils.getUserId();
			} catch (Exception e) {
			}
			if (uid > 0l) {
				ConsigneeAddressDTO address = addressFacade.getDefaultConsigneeAddress(uid);
				if (address != null) {
					sectionCode = address.getSectionId();
				}
			}

			AreaUtils.setAreaCookie((HttpServletResponse) response, sectionCode);
			request.setAttribute(AreaUtils.COOKIE_NAME_AREA, sectionCode);
		}
		// 2.设置BI要求的cookie,用于uv统计
		BasicLogUtils.setBICookieOfUser(httpServletRequest, (HttpServletResponse) response);
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

	// public void setIpServiceFacade(IPServiceFacade ipServiceFacade) {
	// this.ipServiceFacade = ipServiceFacade;
	// }

}
