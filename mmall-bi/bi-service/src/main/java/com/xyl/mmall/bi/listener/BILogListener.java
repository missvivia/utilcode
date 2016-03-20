package com.xyl.mmall.bi.listener;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.bi.core.constant.BILogKey;
import com.xyl.mmall.bi.core.enums.BIType;
import com.xyl.mmall.bi.core.enums.ClientType;
import com.xyl.mmall.bi.core.enums.OpAction;
import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogClient;

/**
 * 消息的异步被动接收者.
 * 
 * @author wangfeng
 * 
 */
@Deprecated
public class BILogListener implements MessageListener {

	private static Logger logger = LoggerFactory.getLogger(BILogListener.class);

	@Autowired
	private BILogClient biLogClient;

	/**
	 * MessageListener回调函数.
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		try {
			// 1.接收消息
			MapMessage mapMessage = (MapMessage) message;
			String type = mapMessage.getString(BILogKey.TYPE);
			long time = mapMessage.getLong(BILogKey.TIME);
			String action = mapMessage.getString(BILogKey.ACTION);
			String accountId = mapMessage.getString(BILogKey.ACCOUNT_ID);
			String clientType = mapMessage.getString(BILogKey.CLIENT_TYPE);
			String ip = mapMessage.getString(BILogKey.IP);
			String otherKey = mapMessage.getString(BILogKey.OTHER_KEY);

			// 2.设置日志通用信息实体.
			BasicLog basicLog = new BasicLog();
			basicLog.setAccountId(accountId);
			basicLog.setAction(OpAction.UNKNOWN.genEnumByValue(action));
			basicLog.setTime(time);
			basicLog.setType(BIType.UNKNOWN.genEnumByValue(type));
			basicLog.setClientType(ClientType.NULL.genEnumByValue(clientType));
			basicLog.setIp(ip);

			Map<String, Object> logMap = getLogMap(clientType, mapMessage);
			// 3.打印日志
			biLogClient.logInfo(basicLog, logMap, otherKey);
		} catch (Exception e) {
			logger.error("处理消息时发生异常.", e);
		}
	}

	/**
	 * 根据app还是web端获取参数.
	 * 
	 * @param clientType
	 * @param mapMessage
	 * @return
	 * @throws JMSException
	 */
	private Map<String, Object> getLogMap(String clientType, MapMessage mapMessage) throws JMSException {
		Map<String, Object> logMap = new HashMap<>();
		if (clientType.equals(ClientType.WEB.getValue()) || clientType.equals(ClientType.WAP.getValue())) {
			logMap.put(BILogKey.BROWSER, mapMessage.getString(BILogKey.BROWSER));
			logMap.put(BILogKey.COOKIE, mapMessage.getString(BILogKey.COOKIE));
		} else if (clientType.equals(ClientType.APP.getValue())) {
			logMap.put(BILogKey.LONGITUDE, mapMessage.getString(BILogKey.LONGITUDE));
			logMap.put(BILogKey.LATITUDE, mapMessage.getString(BILogKey.LATITUDE));
			logMap.put(BILogKey.DEVICE_MODEL, mapMessage.getString(BILogKey.DEVICE_MODEL));
			logMap.put(BILogKey.DEVICE_RESOLUTION, mapMessage.getString(BILogKey.DEVICE_RESOLUTION));
			logMap.put(BILogKey.DEVICE_PLATFORM, mapMessage.getString(BILogKey.DEVICE_PLATFORM));
			logMap.put(BILogKey.DEVICE_NETWORK, mapMessage.getString(BILogKey.DEVICE_NETWORK));
			logMap.put(BILogKey.DEVICE_OS_VERSION, mapMessage.getString(BILogKey.DEVICE_OS_VERSION));
			logMap.put(BILogKey.DEVICE_UDID, mapMessage.getString(BILogKey.DEVICE_UDID));
			logMap.put(BILogKey.APP_CHANNEL, mapMessage.getString(BILogKey.APP_CHANNEL));
			logMap.put(BILogKey.APP_VERSION, mapMessage.getString(BILogKey.APP_VERSION));
		}
		return logMap;
	}

}
