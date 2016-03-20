package com.xyl.mmall.bi.core.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.netease.cloud.nqs.client.ClientConfig;
import com.netease.cloud.nqs.client.Message;
import com.netease.cloud.nqs.client.MessageSessionFactory;
import com.netease.cloud.nqs.client.SimpleMessageSessionFactory;
import com.netease.cloud.nqs.client.consumer.ConsumerConfig;
import com.netease.cloud.nqs.client.consumer.MessageConsumer;
import com.netease.cloud.nqs.client.exception.MessageClientException;
import com.netease.cloud.nqs.client.producer.MessageProducer;
import com.netease.cloud.nqs.client.producer.ProducerConfig;
import com.xyl.mmall.bi.core.util.NQSUtils;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
public class NQSTemplate {

	private static Logger logger = LoggerFactory.getLogger(NQSBILogMessageProducer.class);

	private ClientConfig clientConfig;

	private Map<String, MessageProducer> messageProducerMap = new ConcurrentHashMap<>();

	public void send(String queueName, Message message) {
		doSend(queueName, message);
	}

	protected void doSend(String queueName, Message message) {
		Assert.notNull(message, "message must not be null");
		MessageProducer producer = null;
		try {
			producer = doCreateProducer(queueName);
			producer.sendMessage(message);
		} catch (MessageClientException e) {
			logger.error(e.getMessage(), e);
			NQSUtils.closeMessageProducer(producer);
			if (hasMessageProducer(queueName))
				messageProducerMap.remove(queueName);
		}
	}

	protected MessageProducer doCreateProducer(String queueName) throws MessageClientException {
		MessageProducer producer = messageProducerMap.get(queueName);
		if (producer != null)
			return producer;

		MessageSessionFactory simpleMessageSessionFactory = new SimpleMessageSessionFactory(clientConfig);
		ProducerConfig producerConfig = new ProducerConfig();
		producerConfig.setProductId(clientConfig.getProductId());
		producerConfig.setQueueName(queueName);
		producer = simpleMessageSessionFactory.createProducer(producerConfig);
		messageProducerMap.put(queueName, producer);
		return producer;
	}

	protected boolean hasMessageProducer(String queueName) {
		return messageProducerMap.containsKey(queueName);
	}

	public MessageConsumer doCreateMessageConsumer(String queueName) throws MessageClientException {
		MessageSessionFactory simpleMessageSessionFactory = new SimpleMessageSessionFactory(clientConfig);
		ConsumerConfig consumerConfig = new ConsumerConfig();
		consumerConfig.setProductId(clientConfig.getProductId());
		consumerConfig.setQueueName(queueName);
		// 是否需要向服务器ack消息,默认不需要
		consumerConfig.setRequireAck(true);
		// 消费者群组,集群模式下用,这里设置方便后台定位问题
		consumerConfig.setGroup("mmall_consumer_group");
		MessageConsumer consumer = simpleMessageSessionFactory.createConsumer(consumerConfig);
		return consumer;
	}

	public void setClientConfig(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

}
