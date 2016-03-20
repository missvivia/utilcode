package com.xyl.mmall.jms.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:config/${spring.profiles.active}/mmallJms.properties" })
public class JmsPropertyConfiguration {

	@Value("${nqs.clientConfig.host}")
	private String host;

	@Value("${nqs.clientConfig.port}")
	private int port;

	@Value("${nqs.clientConfig.productId}")
	private String productId;

	@Value("${nqs.clientConfig.accessKey}")
	private String accessKey;

	@Value("${nqs.clientConfig.accessSecret}")
	private String accessSecret;

	@Value("${nqs.clientConfig.authmachanism}")
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
