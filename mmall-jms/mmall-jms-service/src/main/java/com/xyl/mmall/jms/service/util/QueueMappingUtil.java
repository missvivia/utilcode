package com.xyl.mmall.jms.service.util;

public class QueueMappingUtil {

	private static final String SMS_QUEUE = "mmall.q.sms";

	private static final String MAIL_QUEUE = "mmall.q.email";

	public static String getSmsQueue() {
		return SMS_QUEUE;
	}

	public static String getMailQueue() {
		return MAIL_QUEUE;
	}

}
