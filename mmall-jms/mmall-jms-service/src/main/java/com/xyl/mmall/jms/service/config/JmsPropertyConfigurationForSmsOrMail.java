package com.xyl.mmall.jms.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:config/${spring.profiles.active}/mmallJmsForSmsOrMail.properties" })
public class JmsPropertyConfigurationForSmsOrMail {

	@Value("${smsmail.clientConfig.host}")
	private String host;

	@Value("${smsmail.clientConfig.port}")
	private int port;

	@Value("${smsmail.clientConfig.productId}")
	private String productId;

	@Value("${smsmail.clientConfig.accessKey}")
	private String accessKey;

	@Value("${smsmail.clientConfig.accessSecret}")
	private String accessSecret;

	@Value("${smsmail.clientConfig.authmachanism}")
	private String authmachanism;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAuthmachanism() {
		return authmachanism;
	}

	public void setAuthmachanism(String authmachanism) {
		this.authmachanism = authmachanism;
	}
}
