package com.xyl.mmall;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.netease.backend.trace.filters.TraceListener;

@Configuration
@ImportResource({"classpath:config/mvc-config.xml","classpath:config/dubbo-config.xml"})
@ComponentScan
public class JobConfig {

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
