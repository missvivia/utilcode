package com.xyl.mmall.jms.service.base;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.cloud.nqs.client.ClientConfig;
import com.netease.cloud.nqs.client.auth.AuthBackend;
import com.netease.cloud.nqs.client.auth.AuthBackendFactory;
import com.xyl.mmall.jms.service.config.JmsPropertyConfigurationForSmsOrMail;

/**
 * 仅供短信或邮件发送的连接类
 * @author hzzhaozhenzuo
 *
 */
public class DefaultConnectionFactoryForSmsOrMail extends CachingConnectionFactory implements InitializingBean{
	
	@Autowired
	private JmsPropertyConfigurationForSmsOrMail jmsPropertyConfigurationForSmsOrMail;

	@Override
	public void afterPropertiesSet() throws Exception {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setHost(jmsPropertyConfigurationForSmsOrMail.getHost());
		clientConfig.setPort(jmsPropertyConfigurationForSmsOrMail.getPort());
		clientConfig.setProductId(String.valueOf(jmsPropertyConfigurationForSmsOrMail.getProductId()));
		clientConfig.setAccessKey(jmsPropertyConfigurationForSmsOrMail.getAccessKey());
		clientConfig.setAccessSecret(jmsPropertyConfigurationForSmsOrMail.getAccessSecret());
		clientConfig.setAuthMechanism(jmsPropertyConfigurationForSmsOrMail.getAuthmachanism());
		AuthBackend authBackend = AuthBackendFactory.createAuthBackend(clientConfig);
		super.setUsername(authBackend.getUsername());
		super.setPassword(authBackend.getPassword());
		super.setVirtualHost(clientConfig.getProductId());
		super.setHost(clientConfig.getHost());
		super.setPort(clientConfig.getPort());
	}

}
