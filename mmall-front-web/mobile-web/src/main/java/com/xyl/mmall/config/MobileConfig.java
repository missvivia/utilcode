package com.xyl.mmall.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.xyl.mmall.filter.AreaFilter;

@Configuration
@ComponentScan
public class MobileConfig {

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
	

}
