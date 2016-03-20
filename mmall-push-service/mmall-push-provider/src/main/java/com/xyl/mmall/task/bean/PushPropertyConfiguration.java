package com.xyl.mmall.task.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:config/${spring.profiles.active}/pushConfig.properties" })
public class PushPropertyConfiguration {
	
	@Value("${domain}")
	private String domain;
	
	@Value("${appSecret}")
	private String appSecret;
	
	@Value("${appkey}")
	private String appkey;
	
	@Value("${proxyUrl}")
	private String proxyUrl;

	public String getDomain() {
		return domain;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public String getAppkey() {
		return appkey;
	}

	public String getProxyUrl() {
		return proxyUrl;
	}

}
