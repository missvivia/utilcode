package com.xyl.mmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.netease.backend.trace.filters.TraceListener;

@Configuration
@ImportResource(value = { "classpath:config/mvc-config.xml","classpath:config/dubbo-config.xml","classpath:config/${spring.profiles.active}/ehcache-bean.xml"})
public class BaseConfig {

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
