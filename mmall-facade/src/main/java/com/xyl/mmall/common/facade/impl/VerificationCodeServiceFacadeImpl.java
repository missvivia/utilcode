package com.xyl.mmall.common.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.common.facade.VerificationCodeServiceFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.task.dto.VCodeResult;
import com.xyl.mmall.task.dto.VerificationCode;
import com.xyl.mmall.task.service.VerificationCodeService;

/**
 * 验证码相关服务.
 * 
 * @author wangfeng
 * 
 */
@Facade("verificationCodeServiceFacade")
public class VerificationCodeServiceFacadeImpl implements VerificationCodeServiceFacade {

	@Autowired
	private VerificationCodeService verificationCodeService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.VerificationCodeServiceFacade#sendVerificationCodeOfSms(java.lang.String,
	 *      java.lang.Integer)
	 */
	@Override
	public VerificationCode sendVerificationCodeOfSms(String mobile, Integer effectiveTime) {
		return verificationCodeService.sendVerificationCodeOfSms(mobile, effectiveTime);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.VerificationCodeServiceFacade#validateVerificationCode(com.xyl.mmall.task.dto.VerificationCode,
	 *      int)
	 */
	@Override
	public VCodeResult validateVerificationCode(VerificationCode entity, int userVerificationCode) {
		return verificationCodeService.validateVerificationCode(entity, userVerificationCode);
	}

}
