package com.xyl.mmall;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:config/${spring.profiles.active}/job.properties" })
public class JobPropertyConfiguration {

	@Value("${job.secretkey}")
	private String secretKey;

	@Value("${job.backurl}")
	private String backUrl;

	@Value("${job.checksig}")
	private String checkSign;

	@Value("${spring.profiles.active}")
	private String env;

	public String getSecretKey() {
		return secretKey;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public String getCheckSign() {
		return checkSign;
	}

	public String getEnv() {
		return env;
	}
}
