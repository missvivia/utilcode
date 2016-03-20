package com.xyl.mmall.bi.core.service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.xyl.mmall.bi.core.constant.BILogKey;
import com.xyl.mmall.bi.core.meta.AppLog;
import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.core.meta.WebLog;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
@Component("biLogNotifyMessageProducer")
public class BILogNotifyMessageProducer {

	@Autowired(required = false)
	private JmsTemplate biLogJmsTemplate;

	@Autowired(required = false)
	private Destination biLogQueue;

	public void sendBILog(final BasicLog basicLog, final String otherKey) {
		if (basicLog != null)
			sendMessage(basicLog, biLogQueue);
	}

	/**
	 * 使用jmsTemplate的send/MessageCreator()发送Map类型的消息并在Message中附加属性用于消息过滤.
	 */
	private void sendMessage(final BasicLog basicLog, Destination destination) {
		biLogJmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				// 异步发送的日志信息.
				MapMessage message = session.createMapMessage();
				message.setString(BILogKey.ACCOUNT_ID, basicLog.getAccountId());
				message.setString(BILogKey.ACTION, basicLog.getAction().getValue());
				message.setString(BILogKey.TYPE, basicLog.getType().getValue());
				message.setLong(BILogKey.TIME, basicLog.getTime());
				message.setString(BILogKey.CLIENT_TYPE, basicLog.getClientType().getValue());
				message.setString(BILogKey.DEVICE_OS, basicLog.getDeviceOs());
				message.setString(BILogKey.IP, basicLog.getIp());
				if (basicLog instanceof AppLog) {
					// AppLog appLog = (AppLog) basicLog;
				} else if (basicLog instanceof WebLog) {
					WebLog webLog = (WebLog) basicLog;
					message.setString(BILogKey.BROWSER, webLog.getBrowser());
					message.setString(BILogKey.COOKIE, webLog.getCookie());
				}
				return message;
			}
		});
	}

}
