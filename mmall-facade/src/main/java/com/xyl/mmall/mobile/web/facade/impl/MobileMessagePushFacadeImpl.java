package com.xyl.mmall.mobile.web.facade.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.jms.enums.MailType;
import com.xyl.mmall.mobile.web.facade.MobileMessagePushFacade;
import com.xyl.mmall.task.dto.MessageDTO;
import com.xyl.mmall.task.enums.PlatformType;
import com.xyl.mmall.task.service.PushService;

@Facade("mobileMessagePushFacade")
public class MobileMessagePushFacadeImpl implements MobileMessagePushFacade{
	
	@Autowired
	private PushService pushService;
	
	@Override
	public boolean pushMessageForPrivate(String userKey, PlatformType platform, String content) {
		return pushService.pushMessageForPrivate(userKey, platform, content);
	}

	@Override
	public boolean putMessageForPrivate(String userKey, MessageDTO message) {
		return pushService.putMessageForPrivate(userKey, message);
	}

	@Override
	public Map<String, Object> getSignatureByUserKey(String userUniqueKey) {
		return pushService.getSignatureByUserKey(userUniqueKey);
	}

	@Override
	public boolean sendSms(String mobile, String content) {
		return pushService.sendSms(mobile, content);
	}

	@Override
	public boolean sendMail(MailType mailType,String toAddress, String title, String content) {
		return pushService.sendMail(mailType,toAddress, title, content);
	}

	@Override
	public boolean pushForAll(long userId, int bizTypeId, long bizUniqueId,
			Map<String, Object> otherParamMap) {
		return pushService.pushForAll(userId, bizTypeId, bizUniqueId, otherParamMap);
	}

	@Override
	public boolean sendSmsImportant(String mobile, String content) {
		return pushService.sendSmsImportant(mobile, content);
	}
}
