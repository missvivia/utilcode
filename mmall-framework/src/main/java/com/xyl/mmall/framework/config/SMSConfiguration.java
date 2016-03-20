package com.xyl.mmall.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:config/${spring.profiles.active}/sms.properties" })
public class SMSConfiguration {

	@Value("${sms.sendgoodssingle.url}")
	private String smsGoodsSglUrl;
	
	@Value("${sms.sendcode.url}")
	private String smsSendCodeURL;

	@Value("${sms.checkcode.url}")
	private String smsCheckCodeURL;

	public String getSmsGoodsSglUrl() {
		return smsGoodsSglUrl;
	}

	public void setSmsGoodsSglUrl(String smsGoodsSglUrl) {
		this.smsGoodsSglUrl = smsGoodsSglUrl;
	}

	public String getSmsSendCodeURL() {
		return smsSendCodeURL;
	}

	public String getSmsCheckCodeURL() {
		return smsCheckCodeURL;
	}
}
