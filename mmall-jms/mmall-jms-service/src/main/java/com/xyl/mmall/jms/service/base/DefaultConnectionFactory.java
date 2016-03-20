package com.xyl.mmall.jms.service.base;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.cloud.nqs.client.ClientConfig;
import com.netease.cloud.nqs.client.auth.AuthBackend;
import com.netease.cloud.nqs.client.auth.AuthBackendFactory;
import com.xyl.mmall.jms.service.config.JmsPropertyConfiguration;

public class DefaultConnectionFactory extends CachingConnectionFactory implements InitializingBean{
	
	@Autowired
	private JmsPropertyConfiguration jmsPropertyConfiguration;

	@Override
	public void afterPropertiesSet() throws Exception {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setHost(jmsPropertyConfiguration.getHost());
		clientConfig.setPort(jmsPropertyConfiguration.getPort());
		clientConfig.setProductId(String.valueOf(jmsPropertyConfiguration.getProductId()));
		clientConfig.setAccessKey(jmsPropertyConfiguration.getAccessKey());
		clientConfig.setAccessSecret(jmsPropertyConfiguration.getAccessSecret());
		clientConfig.setAuthMechanism(jmsPropertyConfiguration.getAuthmachanism());
		AuthBackend authBackend = AuthBackendFactory.createAuthBackend(clientConfig);
		super.setUsername(authBackend.getUsername());
		super.setPassword(authBackend.getPassword());
		super.setVirtualHost(clientConfig.getProductId());
		super.setHost(clientConfig.getHost());
		super.setPort(clientConfig.getPort());
	}

}
