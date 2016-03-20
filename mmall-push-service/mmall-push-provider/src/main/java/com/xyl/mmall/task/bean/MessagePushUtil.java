package com.xyl.mmall.task.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netease.push.config.PusherConfiguration;
import com.netease.push.http.ClientConfiguration;
import com.netease.push.meta.PushResult;
import com.netease.push.service.Message;
import com.netease.push.service.MessagePusher;
import com.xyl.mmall.task.exception.PushException;

@Component
public class MessagePushUtil implements InitializingBean {

	private static final int CONNECTION_TIMEOUT = 20000;

	private static final int MAX_CONNECTIONS = 50;

	private static final int MAX_PREROUT = 50;

	private static final int POOLSIZE = 5;

	private static final int KEEP_ALIVE_TIME = 5;

	private static final int MAX_POOL_SIZE = POOLSIZE * 2;

	private static final long DEFAULT_TTL = 604800;

	private static final Logger logger = LoggerFactory.getLogger(MessagePushUtil.class);

	private static final boolean DEFAULT_OFFLINE = true;

	private static final int DEFAULT_BLOCKING_CAPACITY = 200;

	private static final long EXPIRE_TIME = 2592000000L;

	private MessagePusher messagePusher;

	@Autowired
	private PushPropertyConfiguration propertyConfiguration;

	/**
	 * 推送消息到消息平台
	 * 
	 * @param userUniqueKey
	 *            用户唯一标识
	 * @param content
	 *            消息内容
	 * @param platform
	 * @param title
	 * @param alert
	 * @param ttl
	 * @return
	 * @throws PushException
	 */
	public boolean putMessageForPrivate(String userUniqueKey, String content, String platform, String title,
			String alert, Long ttl) throws PushException {
		Message message = new Message(content);
		message.setTitle(title);
		message.setAlert(alert);

		Map<String, Object> filter = new HashMap<String, Object>();

		long ttlParam = ttl != null ? ttl : DEFAULT_TTL;
		PushResult result = messagePusher.pushSpecifyMessageWithRet(message, platform,
				propertyConfiguration.getDomain(), userUniqueKey, ttlParam, DEFAULT_OFFLINE, filter);

		if (result.isSuccessFul()) {
			logger.info("send msg suc! msgId = " + result.getMsgId());
			return true;
		} else {
			logger.error("send msg error! msgId = " + result.getMsgId() + ", errorReason = " + result.getErrorReason());
			return false;
		}
	}

	/**
	 * 为特定用户产生一个唯一签名
	 * 
	 * @param userUniqueKey
	 *            这个值可以是用户id，或者用户id加区域id
	 * @return
	 */
	public Map<String, Object> genSignatureForUser(String userUniqueKey) {
		long expireTime = System.currentTimeMillis() + EXPIRE_TIME;
		String nonce = messagePusher.generateNonce();
		String signature = messagePusher.generateUserAccountSign(propertyConfiguration.getDomain(), userUniqueKey,
				expireTime, nonce);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nonce", nonce);
		map.put("signature", signature);
		map.put("expire_time", expireTime);
		return map;
	}

	public HashMap<String, Object> GenSignForTest(String account) throws PushException {
		HashMap<String, Object> result = new HashMap<String, Object>();
		long expire_time = System.currentTimeMillis() + EXPIRE_TIME;
		String nonce = messagePusher.generateNonce();
		String sign = messagePusher.generateUserAccountSign(propertyConfiguration.getDomain(), account, expire_time,
				nonce);
		result.put("expire_time", expire_time);
		result.put("nonce", nonce);
		result.put("sign", sign);
		logger.info(propertyConfiguration.getDomain() + "|" + account + "|" + expire_time + "|" + nonce);
		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		PusherConfiguration pushConfig = new PusherConfiguration(propertyConfiguration.getDomain(),
				propertyConfiguration.getAppkey(), propertyConfiguration.getAppSecret(),
				propertyConfiguration.getProxyUrl());

		// 连接池配置
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setConnectionTimeout(CONNECTION_TIMEOUT);// 注意,单位是ms
		clientConfig.setMaxConnections(MAX_CONNECTIONS);
		clientConfig.setMaxPreRoute(MAX_PREROUT);
		pushConfig.setClientConfiguration(clientConfig);

		// 线程池配置
		ThreadPoolExecutor pool = new ThreadPoolExecutor(POOLSIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES,
				new ArrayBlockingQueue<Runnable>(DEFAULT_BLOCKING_CAPACITY));
		pushConfig.setAsyncPusherPool(pool);

		// 使用PusherConfiguration新建实例
		messagePusher = new MessagePusher(pushConfig);
	}

}
