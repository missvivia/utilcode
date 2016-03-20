package com.xyl.mmall.jms.service.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class JmsMessageSendUtilForSmsOrMail {
	
	private static final Logger logger=LoggerFactory.getLogger(JmsMessageSendUtilForSmsOrMail.class);
	
	@Autowired
	@Qualifier("rabbitTemplateForSmsOrEmail")
	private RabbitTemplate rabbitTemplate;
	
	/**
	 * support direct routing
	 * @param queueName
	 * @param message
	 * @return
	 */
	public boolean sendMsg(String queueName, Message message){
		boolean successFlag=true;
		try {
			rabbitTemplate.send(queueName, message);
		} catch (Exception e) {
			successFlag = false;
			logger.error("err send msg to queue:" + queueName, e);
		}
		return successFlag;
	}
	
	/**
	 * send message which will be saved to file or database
	 * @param queueName
	 * @param messageBody
	 * @return
	 */
	public boolean sendMsgWithPersistentMsg(String queueName, String messageBody){
		boolean successFlag=true;
		try {
			MessageProperties messageProperties=new MessageProperties();
			messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
			Message message=new Message(messageBody.getBytes(), messageProperties);
			rabbitTemplate.send(queueName, message);
		} catch (Exception e) {
			successFlag = false;
			logger.error("err send msg to queue:" + queueName, e);
		}
		return successFlag;
	}
	
	/**
	 * send message which will be saved to file or database
	 * @param queueName
	 * @param messageBody
	 * @return
	 */
	public boolean sendMsg(String queueName, String messageBody){
		boolean successFlag=true;
		try {
			MessageProperties messageProperties=new MessageProperties();
			messageProperties.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
			Message message=new Message(messageBody.getBytes("utf-8"), messageProperties);
			rabbitTemplate.send(queueName, message);
		} catch (Exception e) {
			successFlag = false;
			logger.error("err send msg to queue:" + queueName, e);
		}
		return successFlag;
	}

}
