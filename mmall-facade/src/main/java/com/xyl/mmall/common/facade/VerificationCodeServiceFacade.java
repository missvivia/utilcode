package com.xyl.mmall.common.facade;

import com.xyl.mmall.task.dto.VCodeResult;
import com.xyl.mmall.task.dto.VerificationCode;

/**
 * 验证码相关服务.
 * 
 * @author wangfeng
 * 
 */
public interface VerificationCodeServiceFacade {

	/**
	 * 发送验证码到用户手机.<br/>
	 * VerificationCode信息传给web端,校验的时候和验证码一起回传.
	 * 
	 * @param mobile
	 *            手机号.
	 * @param effectiveTime
	 *            有效时间,以分为单位,默认30分钟.
	 * @return
	 */
	public VerificationCode sendVerificationCodeOfSms(String mobile, Integer effectiveTime);

	/**
	 * 校验验证码.<br/>
	 * 用户输入验证码的时候校验一次外,提交整个表单的接口还要再校验一次.
	 * 
	 * @param entity
	 * @param userVerificationCode
	 *            用户输入的校验码.
	 * @return
	 */
	public VCodeResult validateVerificationCode(VerificationCode entity, int userVerificationCode);

}
