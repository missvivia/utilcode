package com.xyl.mmall;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.netease.backend.trace.filters.TraceListener;
import com.xyl.mmall.filter.AreaFilter;
import com.xyl.mmall.filter.AreaOnlineFilter;
import com.xyl.mmall.filter.MainsiteCORSFilter;

@Configuration
@ImportResource({ "classpath:config/mvc-config.xml", "classpath:config/dubbo-config.xml" })
@ComponentScan
public class MainsiteConfig {

	/**
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean contextFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		AreaFilter arerFilter = new AreaFilter();
		registrationBean.setFilter(arerFilter);
		registrationBean.setOrder(1);
		return registrationBean;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean contextFilterRegistrationBeanForAreaOnline() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		AreaOnlineFilter areaOnlineFilter = new AreaOnlineFilter();
		registrationBean.setFilter(areaOnlineFilter);
		registrationBean.setOrder(2);
		return registrationBean;
	}

	/**
	 * WEB项目需要的trace listener。
	 * 
	 * @return TraceListener
	 */
	@Bean
	TraceListener traceListener() {
		return new TraceListener();
	}

	@Bean
	public FilterRegistrationBean mainsiteCORSFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		MainsiteCORSFilter mainsiteCORSFilter = new MainsiteCORSFilter();
		registrationBean.setFilter(mainsiteCORSFilter);
		registrationBean.setOrder(-1);
		return registrationBean;
	}

}
