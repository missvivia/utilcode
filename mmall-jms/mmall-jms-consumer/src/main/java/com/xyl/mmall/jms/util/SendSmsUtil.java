package com.xyl.mmall.jms.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.xyl.mmall.jms.service.util.ResourceTextUtil;

/**
 * @author user
 * 
 */
public class SendSmsUtil {

	protected static final Logger logger = Logger.getLogger(SendSmsUtil.class);

	private static final ResourceBundle smsResourceBundle = ResourceTextUtil
			.getResourceBundleByName("content.sms");

	private static final String DEFAULT_GROUP = ResourceTextUtil
			.getTextFromResourceByKey(smsResourceBundle, "mobile.group");

	private static final String MOIBLE_URL = ResourceTextUtil
			.getTextFromResourceByKey(smsResourceBundle, "mobile.url");
	
	private static final String MOBILE_SUBCODE=ResourceTextUtil.getTextFromResourceByKey(smsResourceBundle, "mobile.subcode");

	/**
	 * 发送短信
	 * 
	 * @param mobile
	 * @param content
	 * @param isGroup
	 *            是否为群发消息
	 * @return
	 */
	public boolean sendMobileMessage(String mobile, String content,
			Integer level, String setMsgprop) {
		logger.info("send sms,mobile:" + mobile);
		mobile = StringUtils.trim(mobile);
		if (!isValidMobile(mobile)) {
			if (logger.isDebugEnabled())
				logger.debug("mobile:" + mobile + " 不是有效的移动手机号码!");
			return false;
		}

		try {
			byte[] bStr = content.getBytes("GBK");
			content = Tools.HexToStr(bStr);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		StringBuilder sb = new StringBuilder(1000);
		sb.append(MOIBLE_URL);
		sb.append(mobile);
		sb.append("&frmphone=");
		sb.append(mobile);
		sb.append("&msgprop=");
		if (StringUtils.isNotBlank(setMsgprop))
			sb.append(setMsgprop);
		else
			sb.append(DEFAULT_GROUP);
		sb.append("&message=");
		sb.append(content);
		sb.append("&corpinfo=1");
		sb.append("&showphone="+MOBILE_SUBCODE);

		if (level != null) {
			sb.append("&level=" + level);
		}

		if (logger.isDebugEnabled())
			logger.debug("mobile:" + sb.toString());
		String result = this.sendMessage(sb.toString());
		if (logger.isDebugEnabled())
			logger.debug("mobile:" + mobile + " :" + result);
		return true;
	}

	/*
	 * 是否有效的手机号码 (以13,14,15或者18开头的11位数字)
	 */
	private boolean isValidMobile(String mobile) {
		return mobile.matches("1[34578]\\d{9}");
	}

	/**
	 * 发送短信
	 * 
	 * @param urlAddress
	 * @return
	 */
	public String sendMessage(String urlAddress) {
		URL url = null;
		InputStream inputStream = null;
		String result = "";
		try {
			url = new URL(urlAddress);
			inputStream = url.openStream();
			result = IOUtils.toString(inputStream);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

}
