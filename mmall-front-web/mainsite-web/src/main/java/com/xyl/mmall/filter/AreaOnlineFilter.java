package com.xyl.mmall.filter;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xyl.mmall.cms.facade.AreaOnlineFacade;
import com.xyl.mmall.jms.service.util.ResourceTextUtil;
import com.xyl.mmall.util.AreaUtils;

/**
 * 上线区域过滤器
 * 
 * @author hzzhaozhenzuo
 *
 */
public class AreaOnlineFilter implements Filter {

	private AreaOnlineFacade areaOnlineFacade;

	private static final ResourceBundle resourceBundle = ResourceTextUtil
			.getResourceBundleByName("config.content");

	// /(r|pub|res|src)/.*|
	private static final String ACCESS_RESOUCE_PATTERN = ResourceTextUtil
			.getTextFromResourceByKey(resourceBundle, "areaonline.access");
	
	private static final String SPECIAL_ACCESS_RESOUCE_PATTERN = ResourceTextUtil
			.getTextFromResourceByKey(resourceBundle, "special.access");

	private static final Pattern pattern = Pattern
			.compile(ACCESS_RESOUCE_PATTERN);

	private static final String REDIRECT_PAGE = "/hint/nogoods";
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext wac = WebApplicationContextUtils
				.getWebApplicationContext(filterConfig.getServletContext());
		areaOnlineFacade = (AreaOnlineFacade) wac.getBean("areaOnlineFacade");
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 先判断cookie,没有则判断ip,同时写cookie,cookie有效期暂定1天
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String requestUrl = httpServletRequest.getRequestURI();
		long curSupplierAreaId = AreaUtils.getAreaCode();
//		if (areaOnlineFacade.areaExist(curSupplierAreaId)) {
//			chain.doFilter(request, response);
//			return;
//		}

		// 处理未上线区域
//		if (!this.canAccess(requestUrl,httpServletRequest)) {
//			// 不在可访问列表，需要跳到特定页
//			WebUtils.issueRedirect(httpServletRequest, httpServletResponse,
//					REDIRECT_PAGE);
//			return;
//		}
		chain.doFilter(request, response);
	}
	
	private boolean canAccess(String requestUrl,HttpServletRequest request) {
		//特殊处理
		if(requestUrl.equals(SPECIAL_ACCESS_RESOUCE_PATTERN)){
			String paramSnapshot=request.getParameter("isSnapShot");
			if(!StringUtils.isEmpty(paramSnapshot)){
				return Boolean.TRUE.toString().equalsIgnoreCase(paramSnapshot);
			}
		}
		
		String refer=request.getHeader("referer");
		if(!StringUtils.isEmpty(refer)){
			if(refer.indexOf("back.baiwandian.cn")>0 || refer.indexOf("sj.baiwandian.cn")>0){
				return true;
			}
		}
		
		Matcher matcher = pattern.matcher(requestUrl);
		return matcher.matches();
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
