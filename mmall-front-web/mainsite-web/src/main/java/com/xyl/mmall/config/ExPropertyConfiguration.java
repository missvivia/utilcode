package com.xyl.mmall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:config/${spring.profiles.active}/content.properties" })
public class ExPropertyConfiguration {

	@Value("${ex.get.token.url}")
	private String tokenUrl;

	@Value("${ex.init.url}")
	private String initUrl;
	
	@Value("${ex.customeservice.status}")
	private String customeServiceStatusUrl;
	
	@Value("${ex.newleaveMessage.url}")
	private String newLeaveMessageUrl;

	public String getTokenUrl() {
		return tokenUrl;
	}

	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	public String getInitUrl() {
		return initUrl;
	}

	public void setInitUrl(String initUrl) {
		this.initUrl = initUrl;
	}

	public String getCustomeServiceStatusUrl() {
		return customeServiceStatusUrl;
	}

	public void setCustomeServiceStatusUrl(String customeServiceStatusUrl) {
		this.customeServiceStatusUrl = customeServiceStatusUrl;
	}

	public String getNewLeaveMessageUrl() {
		return newLeaveMessageUrl;
	}

	public void setNewLeaveMessageUrl(String newLeaveMessageUrl) {
		this.newLeaveMessageUrl = newLeaveMessageUrl;
	}

}
