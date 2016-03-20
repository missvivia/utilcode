package com.xyl.mmall.jms.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.jms.meta.MailMessage;
import com.xyl.mmall.jms.service.base.BaseMessageListener;
import com.xyl.mmall.jms.util.SendEmailUtil;

public class MaillMessageListener extends BaseMessageListener<MailMessage>{

	@Autowired
	private SendEmailUtil sendEmailUtil;
	
	private static final Logger logger=LoggerFactory.getLogger(MaillMessageListener.class);

	@Override
	public boolean handleMessage(MailMessage message) {
		logger.info("send mail:"+message.getTtitle());
		return sendEmailUtil.sendEmail(message.getTtoArr(), message.getTtitle(), message.getTcontent(), message.getMailType());
	}

}
