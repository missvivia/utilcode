package com.xyl.mmall.task.service;

import com.xyl.mmall.task.dto.VCodeResult;
import com.xyl.mmall.task.dto.VerificationCode;

/**
 * 验证码 interface service.
 * 
 * @author wangfeng
 * 
 */
public interface VerificationCodeService {

	/**
	 * 发送验证码到用户手机.
	 * 
	 * @param mobile
	 *            手机号.
	 * @param effectiveTime
	 *            有效时间,以分为单位,默认30分钟.
	 * @return
	 */
	public VerificationCode sendVerificationCodeOfSms(String mobile, Integer effectiveTime);

	/**
	 * 校验验证码.
	 * 
	 * @param entity
	 * @param userVerificationCode
	 *            用户输入的校验码.
	 * @return
	 */
	public VCodeResult validateVerificationCode(VerificationCode entity, int userVerificationCode);

}
