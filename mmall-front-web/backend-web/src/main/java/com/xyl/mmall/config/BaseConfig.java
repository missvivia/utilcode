package com.xyl.mmall.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.netease.backend.trace.filters.TraceListener;

/**
 * @author Yang,Nan
 *
 */
@Configuration
@ImportResource(value = { "classpath:config/mvc-config.xml", "classpath:config/dubbo-config.xml"})
public class BaseConfig {
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(-1);
		factory.setMaxRequestSize(-1);
		return factory.createMultipartConfig();
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

}
