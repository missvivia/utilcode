package com.xyl.mmall.task.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.netease.push.service.PushConst;
import com.xyl.mmall.jms.enums.MailType;
import com.xyl.mmall.jms.enums.SmsType;
import com.xyl.mmall.jms.meta.MailMessage;
import com.xyl.mmall.jms.meta.SmsMessage;
import com.xyl.mmall.jms.service.base.JmsMessageSendUtilForSmsOrMail;
import com.xyl.mmall.jms.service.util.QueueMappingUtil;
import com.xyl.mmall.task.dto.MessageDTO;
import com.xyl.mmall.task.enums.PlatformType;
import com.xyl.mmall.task.exception.PushException;
import com.xyl.mmall.task.meta.DeviceLocation;
import com.xyl.mmall.task.service.PushService;
import com.xyl.mmall.task.bean.MessagePushUtil;
import com.xyl.mmall.task.bean.PushSendFilter;
import com.xyl.mmall.task.bean.PushSenderConfig;
import com.xyl.mmall.task.bean.SmsSendFilter;
import com.xyl.mmall.task.dao.DeviceLocationDao;

/**
 * 
 * @author jiangww
 *
 */
@Service("pushService")
public class PushServiceImpl implements PushService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "pushSenderService")
	protected PushSenderService pushSenderService;

	@Autowired
	protected DeviceLocationDao deviceLocationDao;

	@Autowired
	private MessagePushUtil messagePushUtil;

	@Autowired
	private JmsMessageSendUtilForSmsOrMail jmsMessageSendUtil;
	
	@Autowired
	private PushSendFilter pushSendFilter;
	
	@Autowired
	private SmsSendFilter smsSendFilter;
	
	@Override
	public boolean push(long userId, String alertTitle, String title,
			String message, String appUrl){
		return pushMessage(userId,alertTitle,title,message,appUrl,0);
	}
	@Override
	public boolean pushByArea(long userId, int type, String title, String message, long keyId, long areaCode) {
		return _pushMessage(userId,type,title,message,keyId,areaCode);
	}
	
	private boolean pushMessage(long userId, String alertTitle, String title,
			String message, String appUrl,long areaCode) {
		List<DeviceLocation> devices = deviceLocationDao
				.getListByUserId(userId);
		if (devices == null || devices.size() == 0)
			return false;
		if (StringUtils.isBlank(alertTitle) && StringUtils.isBlank(title))
			return false;
		if (StringUtils.isBlank(alertTitle))
			alertTitle = title;
		if (StringUtils.isBlank(title))
			title = alertTitle;
		if (StringUtils.isBlank(message))
			message = alertTitle;

		PushSenderConfig pushSenderConfig = new PushSenderConfig();
		pushSenderConfig.setTaskId(-1);
		pushSenderConfig.setPlatform(PushConst.ALL_PLATFORM);
		// 这里考虑道ios 和正常的表述不一样
		pushSenderConfig.setAlert(message);
		pushSenderConfig.setTitle(alertTitle);
		pushSenderConfig.setContent(title);
		pushSenderConfig.setSummary(appUrl);

		boolean success = true;
		for (DeviceLocation d : devices) {
			try {
				if(areaCode >0 && areaCode!=d.getAreaCode())
					continue;
				if (StringUtils.isNotBlank(d.getPlatformType()))
					pushSenderService.pushMessage(d.getDeviceId(),
							pushSenderConfig);
			} catch (Exception e) {
				success = false;
				logger.error(e.toString());
			}
		}
		return success;
	}
	
	private boolean _pushMessage(long userId, int type, String title, String message,
			long keyId,long areaCode) {
		String url = null;
		if (keyId != 0l)
			url = MessageCover.genAppUrl(type, String.valueOf(keyId));
		String alterTitle = MessageCover.genTitle(type);
		if (StringUtils.isBlank(alterTitle))
			return false;
		if (StringUtils.isBlank(title))
			title = alterTitle;
		if (StringUtils.isBlank(message))
			message = MessageCover.genMessage(type, keyId);
		return pushMessage(userId, alterTitle, title, message, url,areaCode);
	}
	
	/**
	 * 只支持比较重要的业务短信
	 * 不支持push
	 * (non-Javadoc)
	 * @see com.xyl.mmall.task.service.PushService#pushForAll(long, int, long, java.util.Map)
	 */
	@Override
	public boolean pushForAll(long userId, int bizTypeId, long keyId,Map<String, Object> otherParamMap) {
		return this.sendSmsInner(userId, bizTypeId, keyId,otherParamMap);
	}

	/**
	 * 只发送push消息
	 */
	@Override
	public boolean push(long userId, int type, String title, String message,
			long keyId) {
		return _pushMessage( userId,  type,  title,  message, keyId,0);
	}

	private boolean sendSmsInner(long userId, int bizTypeId, long keyId,Map<String, Object> otherParamMap) {
		logger.info("send sms to queue,userId:" + userId + ",keyId:" + keyId);
		SmsMessage smsMessage = new SmsMessage();
		smsMessage.setUserId(userId);
		smsMessage.setBizTypeId(bizTypeId);
		smsMessage.setBizUniqueKey(keyId);
		smsMessage.setObtainContentByBiz(true);
		smsMessage.setOtherParamMap(otherParamMap);
		smsMessage.setSmsType(SmsType.NORMAL);
		String messageStr = JSON.toJSONString(smsMessage);
		return jmsMessageSendUtil.sendMsg(QueueMappingUtil.getSmsQueue(),
				messageStr);
	}

	public PushSenderService getPushSenderService() {
		return pushSenderService;
	}

	public void setPushSenderService(PushSenderService pushSenderService) {
		this.pushSenderService = pushSenderService;
	}

	@Override
	public boolean pushMessageForPrivate(String userKey, PlatformType platform,
			String content) {
		boolean successFlag = true;
		try {
			successFlag = messagePushUtil.putMessageForPrivate(userKey,
					content, platform.getValue(), null, null, null);
		} catch (PushException e) {
			logger.error("send push msg error,userKey:" + userKey + ",reson:"
					+ e.getCause());
			successFlag = false;
		}
		return successFlag;
	}

	@Override
	public Map<String, Object> getSignatureByUserKey(String userUniqueKey) {
		return messagePushUtil.genSignatureForUser(userUniqueKey);
	}

	@Override
	public boolean putMessageForPrivate(String userKey, MessageDTO message) {
		boolean successFlag = true;
		try {
			successFlag = messagePushUtil.putMessageForPrivate(userKey,
					message.getContent(), message.getPlatform().getValue(),
					message.getTitle(), message.getAlert(), message.getTtl());
		} catch (PushException e) {
			logger.error("send push msg error,userKey:" + userKey + ",reson:"
					+ e.getCause());
			successFlag = false;
		}
		return successFlag;
	}
	
	/**
	 * 订阅短信接口
	 * (non-Javadoc)
	 * @see com.xyl.mmall.task.service.PushService#sendSms(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendSms(String mobile, String content) {
		logger.info("send sms,mobile:" + mobile);
		SmsMessage smsMessage = new SmsMessage();
		smsMessage.setMobile(mobile);
		smsMessage.setContent(content);
		smsMessage.setObtainContentByBiz(false);
		smsMessage.setSmsType(SmsType.SUBSCRIBE);
		String message = JSON.toJSONString(smsMessage);
		return jmsMessageSendUtil.sendMsg(QueueMappingUtil.getSmsQueue(),
				message);
	}

	@Override
	public boolean sendMail(MailType mailType, String toAddress, String title,
			String content) {
		logger.info("sendMail,toAddress:" + toAddress);
		MailMessage mailMessage = new MailMessage();
		mailMessage.setTcontent(content);
		mailMessage.setTtitle(title);
		String[] arr = new String[1];
		arr[0] = toAddress;
		mailMessage.setTtoArr(arr);
		mailMessage.setMailType(mailType);
		mailMessage.setObtainContentByBiz(false);
		String msg = JSON.toJSONString(mailMessage);
		return jmsMessageSendUtil.sendMsg(QueueMappingUtil.getMailQueue(), msg);
	}

	@Override
	public boolean sendSmsImportant(String mobile, String content) {
		logger.info("send sms important,mobile:" + mobile);
		SmsMessage smsMessage = new SmsMessage();
		smsMessage.setMobile(mobile);
		smsMessage.setContent(content);
		smsMessage.setObtainContentByBiz(false);
		smsMessage.setSmsType(SmsType.NORMAL);
		String message = JSON.toJSONString(smsMessage);
		return jmsMessageSendUtil.sendMsg(QueueMappingUtil.getSmsQueue(),
				message);
	}

}
