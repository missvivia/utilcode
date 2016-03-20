package com.xyl.mmall.jms.service.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

public class VistoreSimpleMessageListenerContainer extends
		SimpleMessageListenerContainer {

	private static final Logger logger = LoggerFactory
			.getLogger(VistoreSimpleMessageListenerContainer.class);

	public void logsAfterInit() {
		StringBuilder logInfoBuffer=new StringBuilder(32);
		String[] queueNames=super.getQueueNames();
		for(String queueName:queueNames){
			logInfoBuffer.append(queueName+",");
		}
		logger.info("====queueNames:" + logInfoBuffer.toString() + ",autoStart:"
				+ super.isAutoStartup() + ",active:" + super.isActive());
	}

}
