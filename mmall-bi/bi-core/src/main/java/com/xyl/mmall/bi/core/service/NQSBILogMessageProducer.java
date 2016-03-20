package com.xyl.mmall.bi.core.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.netease.cloud.nqs.client.Message;
import com.xyl.mmall.bi.core.constant.BILogKey;
import com.xyl.mmall.bi.core.meta.ApiConsumerLog;
import com.xyl.mmall.bi.core.meta.AppLog;
import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.core.meta.CMSLog;
import com.xyl.mmall.bi.core.meta.OrderLog;
import com.xyl.mmall.bi.core.meta.WebLog;
import com.xyl.mmall.framework.util.JsonUtils;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
public class NQSBILogMessageProducer {

	private NQSTemplate nqsTemplate;

	private String queueName;

	public void sendBILog(final BasicLog basicLog, final String otherKey) {
		if (basicLog != null)
			sendMessage(basicLog, otherKey);
	}

	/**
	 * 使用NQS发送消息日志.
	 * 
	 * @param basicLog
	 */
	private void sendMessage(final BasicLog basicLog, final String otherKey) {
		Map<String, Object> messageMap = new LinkedHashMap<>();

		if (basicLog instanceof AppLog) {
			AppLog appLog = (AppLog) basicLog;
			messageMap.put(BILogKey.TIME, basicLog.getTime());
			messageMap.put(BILogKey.ACTION, basicLog.getAction().getValue());
			messageMap.put(BILogKey.TYPE, basicLog.getType());
			messageMap.put(BILogKey.ACCOUNT_ID, basicLog.getAccountId());
			messageMap.put(BILogKey.CLIENT_TYPE, basicLog.getClientType().getValue());
			messageMap.put(BILogKey.DEVICE_TYPE, basicLog.getDeviceType());
			messageMap.put(BILogKey.DEVICE_OS, basicLog.getDeviceOs());
			messageMap.put(BILogKey.IP, basicLog.getIp());
			messageMap.put(BILogKey.PROVINCE_CODE, basicLog.getProvinceCode());
			messageMap.put(BILogKey.OTHER_KEY, otherKey);
			messageMap.put(BILogKey.REFERER, appLog.getReferer());
		} else if (basicLog instanceof WebLog) {
			WebLog webLog = (WebLog) basicLog;
			messageMap.put(BILogKey.TIME, basicLog.getTime());
			messageMap.put(BILogKey.ACTION, basicLog.getAction().getValue());
			messageMap.put(BILogKey.TYPE, basicLog.getType());
			messageMap.put(BILogKey.ACCOUNT_ID, basicLog.getAccountId());
			messageMap.put(BILogKey.CLIENT_TYPE, basicLog.getClientType().getValue());
			messageMap.put(BILogKey.DEVICE_TYPE, basicLog.getDeviceType());
			messageMap.put(BILogKey.DEVICE_OS, basicLog.getDeviceOs());
			messageMap.put(BILogKey.IP, basicLog.getIp());
			messageMap.put(BILogKey.PROVINCE_CODE, basicLog.getProvinceCode());
			messageMap.put(BILogKey.OTHER_KEY, otherKey);
			messageMap.put(BILogKey.BROWSER, webLog.getBrowser());
			messageMap.put(BILogKey.COOKIE, webLog.getCookie());
			messageMap.put(BILogKey.REFERER, webLog.getReferer());
		} else if (basicLog instanceof OrderLog) {
			messageMap.put(BILogKey.TIME, basicLog.getTime());
			messageMap.put(BILogKey.CLIENT_TYPE, basicLog.getClientType().getValue());
			messageMap.put(BILogKey.TYPE, basicLog.getType().getValue());
			messageMap.put(BILogKey.OTHER_KEY, otherKey);
		} else if (basicLog instanceof CMSLog) {
			messageMap.put(BILogKey.TIME, basicLog.getTime());
			messageMap.put(BILogKey.CLIENT_TYPE, basicLog.getClientType().getValue());
			messageMap.put(BILogKey.TYPE, basicLog.getType().getValue());
			messageMap.put(BILogKey.OTHER_KEY, otherKey);
		} else if (basicLog instanceof ApiConsumerLog) {
			ApiConsumerLog apiConsumerLog = (ApiConsumerLog) basicLog;
			messageMap.put(BILogKey.TIME, apiConsumerLog.getTime());
			messageMap.put(BILogKey.ACTION, apiConsumerLog.getAction().getValue());
			messageMap.put(BILogKey.TYPE, apiConsumerLog.getType());
			messageMap.put(BILogKey.PARAMETERS, apiConsumerLog.getParameters());
		}

		String messageString = JsonUtils.toJson(messageMap);
		Message message = new Message(messageString.getBytes());
		nqsTemplate.send(queueName, message);
	}

	public void setNqsTemplate(NQSTemplate nqsTemplate) {
		this.nqsTemplate = nqsTemplate;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

}
