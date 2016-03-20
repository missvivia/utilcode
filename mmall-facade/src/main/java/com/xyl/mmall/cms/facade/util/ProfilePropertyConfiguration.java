package com.xyl.mmall.cms.facade.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfilePropertyConfiguration {

	@Value("${spring.profiles.active}")
	private String env;

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

}
