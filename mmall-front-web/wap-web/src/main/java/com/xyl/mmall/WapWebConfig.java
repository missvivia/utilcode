/**
 * 
 */
package com.xyl.mmall;

import java.awt.image.BufferedImage;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import com.netease.backend.trace.filters.TraceListener;
import com.xyl.mmall.filter.AreaFilter;

/**
 * @author hzlihui2014
 *
 */
@Configuration
@ImportResource({"classpath:config/mvc-config.xml","classpath:config/dubbo-config.xml"})
@ComponentScan
public class WapWebConfig {

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
//	@Bean
//	public FilterRegistrationBean contextFilterRegistrationBeanForAreaOnline() {
//		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//		AreaOnlineFilter areaOnlineFilter = new AreaOnlineFilter();
//		registrationBean.setFilter(areaOnlineFilter);
//		registrationBean.setOrder(1);
//		return registrationBean;
//	}
	
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
    public HttpMessageConverters customConverters() {
		HttpMessageConverter<BufferedImage> bufferedImage = new BufferedImageHttpMessageConverter();
        return new HttpMessageConverters(bufferedImage);
    }
}
