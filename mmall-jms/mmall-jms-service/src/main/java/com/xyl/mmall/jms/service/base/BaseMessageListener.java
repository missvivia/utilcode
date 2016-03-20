package com.xyl.mmall.jms.service.base;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

/**
 * 消息base listener
 * 
 * @author hzzhaozhenzuo
 *
 * @param <T>
 */
public abstract class BaseMessageListener<T> implements MessageListenerInf<T> {

	@Autowired
	private JmsMessageSendUtil messageSendUtil;

	private static final Logger logger = LoggerFactory.getLogger(BaseMessageListener.class);

	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public BaseMessageListener() {
		clazz = (Class<T>) ((ParameterizedType) super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	public void onMessage(Message message) {
		String body=null;
		try {
			body = new String(message.getBody(), "utf-8");
			this.handleMessage(JSON.parseObject(body, clazz));
		} catch (UnsupportedEncodingException e) {
			logger.error("handle msg err,body:"+body, e);
		}catch(com.alibaba.fastjson.JSONException e){
			logger.error("json parse err,msg:"+body, e);
		}catch(Exception e){
			logger.error("err,msg:"+body,e);
		}
	}

	/**
	 * 子类需要重写此方法，作为业务逻辑 注意：
	 * 
	 * @param message
	 * @return
	 */
	public abstract boolean handleMessage(T message);

}
