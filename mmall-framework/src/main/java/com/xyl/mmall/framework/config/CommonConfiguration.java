/**
 * 
 */
package com.xyl.mmall.framework.config;

import javax.servlet.DispatcherType;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.netease.backend.trace.filters.WebTraceFilter;
import com.xyl.mmall.framework.listener.SessionTrackingConfigListener;

/**
 * 系统通用配置。
 * 
 * @author lihui
 *
 */
@Configuration
public class CommonConfiguration {

	/**
	 * 字符编码过滤器配置。
	 * 
	 * @return CharacterEncodingFilter
	 */
	@Bean
	FilterRegistrationBean characterEncodingFilter() {
		FilterRegistrationBean characterEncodingFilter = new FilterRegistrationBean();
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		characterEncodingFilter.setFilter(filter);
		characterEncodingFilter.setOrder(-1);
		return characterEncodingFilter;
	}

	/**
	 * Dubbo Trace 需要的Filter
	 * 
	 * @return
	 */
	@Bean
	FilterRegistrationBean webTraceFilter() {
		FilterRegistrationBean traceFilter = new FilterRegistrationBean();
		traceFilter.setFilter(new WebTraceFilter());
		traceFilter.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
		return traceFilter;
	}

	/**
	 * 去除URL中的jsessionid，只出现在cookie中。
	 * 
	 * @return
	 */
	@Bean
	public SessionTrackingConfigListener sessionTrackingConfigListener() {
		SessionTrackingConfigListener listener = new SessionTrackingConfigListener();
		return listener;
	}
}
