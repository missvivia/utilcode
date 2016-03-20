package com.xyl.mmall.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.util.BasicLogUtils;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.util.AreaUtils;

/**
 * 地址过滤器.<br/>
 * 
 */
@Order(1)
public class AreaFilter implements Filter {

	private LocationFacade locationFacade;

	private ConsigneeAddressFacade addressFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext wac = WebApplicationContextUtils
				.getWebApplicationContext(filterConfig.getServletContext());
		locationFacade = (LocationFacade) wac.getBean("locationFacade");
		addressFacade = (ConsigneeAddressFacade) wac.getBean("consigneeAddressFacade");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 先判断cookie,没有则判断ip,同时写cookie,cookie有效期暂定1天
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		if (!AreaUtils.validateSign(httpServletRequest)) {

			long cityCode = 0, sectionCode = 0;// 默认重庆 渝北区,不允许切换
			long uid = 0l;
			try {
				uid = SecurityContextUtils.getUserId();
			} catch (Exception e) {
			}
			if (uid > 0l) {
				List<ConsigneeAddressDTO>  list = addressFacade.listAddress(uid);
				if(!CollectionUtils.isEmpty(list)){
					ConsigneeAddressDTO address = list.get(0);
					if (address != null) {
						sectionCode = address.getSectionId();
						cityCode = address.getCityId();

					}
				}
				
				String sb = setCookieVal(cityCode, sectionCode);
				AreaUtils.setAreaCookie((HttpServletResponse) response, sb);
			}
			
			// request.setAttribute(AreaUtils.COOKIE_NAME_AREA, sectionCode);
		}
		// 2.设置BI要求的cookie,用于uv统计
		BasicLogUtils.setBICookieOfUser(httpServletRequest, (HttpServletResponse) response);
		chain.doFilter(request, response);
	}

	private String setCookieVal(long cityCode, long sectionCode) {
		String cityName = "";
		String sectionName = "";
		if (cityCode <= 0 || sectionCode <= 0) {
			cityName = "重庆市";
			cityCode = -5001;
			sectionName = "渝北区";
			sectionCode = 500112l;// 默认重庆 渝北区,不允许切换
		} else {
			LocationCode city = locationFacade.getLocationCodeByCode(cityCode);
			LocationCode section = locationFacade.getLocationCodeByCode(sectionCode);
			if (city != null && section != null) {
				cityName = city.getLocationName();
				sectionName = section.getLocationName();
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append(cityName).append(AreaUtils.AREA_VALUE_SPLIT).append(cityCode).append(AreaUtils.AREA_VALUE_SPLIT)
				.append(sectionName).append(AreaUtils.AREA_VALUE_SPLIT).append(sectionCode);
		return sb.toString();
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
