package com.xyl.mmall.task.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.xyl.mmall.jms.enums.SmsType;
import com.xyl.mmall.jms.meta.SmsMessage;
import com.xyl.mmall.jms.service.base.JmsMessageSendUtil;
import com.xyl.mmall.jms.service.base.JmsMessageSendUtilForSmsOrMail;
import com.xyl.mmall.jms.service.util.QueueMappingUtil;
import com.xyl.mmall.task.dto.VCodeResult;
import com.xyl.mmall.task.dto.VerificationCode;
import com.xyl.mmall.task.service.VerificationCodeService;

/**
 * 验证码服务实现类.
 * 
 * @author wangfeng
 * 
 */
@Component("verificationCodeService")
public class VerificationCodeServiceImpl implements VerificationCodeService {

	/** 30min **/
	private static final int DEFAULT_EFFECTIVE_TIME = 30;

	@Autowired
	private JmsMessageSendUtilForSmsOrMail jmsMessageSendUtil;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.task.service.VerificationCodeService#sendVerificationCodeOfSms(java.lang.String,
	 *      java.lang.Integer)
	 */
	@Override
	public VerificationCode sendVerificationCodeOfSms(String mobile, Integer effectiveTime) {
		// 1.获取验证码有效时间
		int effectiveMin = getEffectiveTime(effectiveTime);
		// 2.发送验证码短信
		int verificationCode = getVerificationCode();
		String smsContent = "您的验证码为: " + verificationCode + "，" + effectiveMin + "分钟内有效，逾期后需要重新获取一个验证码。";
		
		SmsMessage smsMessage = new SmsMessage();
		smsMessage.setMobile(mobile);
		smsMessage.setContent(smsContent);
		smsMessage.setSmsType(SmsType.NORMAL);
		String message = JSON.toJSONString(smsMessage);
		jmsMessageSendUtil.sendMsg(QueueMappingUtil.getSmsQueue(), message);
		// 3.返回验证码实体
		VerificationCode entity = new VerificationCode(mobile, effectiveMin, verificationCode);
		return entity;
	}

	/**
	 * 获取过期时间.
	 * 
	 * @param effectiveTime
	 * @return
	 */
	private int getEffectiveTime(Integer effectiveTime) {
		if (effectiveTime == null)
			return DEFAULT_EFFECTIVE_TIME;
		return effectiveTime;
	}

	/**
	 * 获取验证码.
	 * 
	 * @return
	 */
	private int getVerificationCode() {
		return (int) ((Math.random() * 9 + 1) * 100000);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.task.service.VerificationCodeService#validateVerificationCode(com.xyl.mmall.task.dto.VerificationCode,
	 *      int)
	 */
	@Override
	public VCodeResult validateVerificationCode(VerificationCode entity, int userVerificationCode) {
		VCodeResult vCodeResult = new VCodeResult();
		// 1.验证签名有效性
		boolean validateCode = entity != null
				&& entity.getSign().equals(
						VerificationCode.genSignature(entity.getCredential(), entity.getExpiredTime(),
								userVerificationCode));
		if (!validateCode) {
			vCodeResult.setRetCode(VCodeResult.RETCODE_ERROR_10001);
			vCodeResult.setErrMsg("验证码错误");
			return vCodeResult;
		}

		// 2.验证时间有效性
		long currentTime = System.currentTimeMillis();
		long expiredTime = entity.getExpiredTime();
		if (currentTime > expiredTime) {
			vCodeResult.setRetCode(VCodeResult.RETCODE_ERROR_10002);
			vCodeResult.setErrMsg("超过有效期");
			return vCodeResult;
		}
		// 校验成功.
		vCodeResult.setRetCode(VCodeResult.RETCODE_SUCCESS);
		return vCodeResult;
	}

}
