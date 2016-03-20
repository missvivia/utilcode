/**
 * 
 */
package com.xyl.mmall.mobile.web.facade.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.PhoneNumberUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.mobile.web.facade.MobileUserProfileFacade;
import com.xyl.mmall.mobile.web.vo.MainSiteUserVO;
import com.xyl.mmall.task.dto.VCodeResult;
import com.xyl.mmall.task.dto.VerificationCode;
import com.xyl.mmall.task.service.VerificationCodeService;

/**
 * @author lihui
 *
 */
@Facade("mobileUserProfileFacade")
public class MobileUserProfileFacadeImpl implements MobileUserProfileFacade {

	private static final String VERIFICATION_CODE_LAST_SEND_TIME = "verificationCode.lastSendTime";

	@Resource
	private UserProfileService userProfileService;

	@Autowired
	private VerificationCodeService verificationCodeService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.UserProfileFacade#getUserProfile(long)
	 */
	@Override
	public MainSiteUserVO getUserProfile(long userId) {
		UserProfileDTO userProfile = userProfileService.findUserProfileById(userId);
		return new MainSiteUserVO(userProfile);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.UserProfileFacade#saveUserProfile(long,
	 *      com.xyl.mmall.mainsite.vo.MainSiteUserVO)
	 */
	@Override
	public BaseJsonVO saveUserProfile(long userId, MainSiteUserVO userVO) {
		BaseJsonVO result = new BaseJsonVO();
		UserProfileDTO userProfile = userVO.getUserProfile();
		userProfile.setUserId(userId);
		userProfile = userProfileService.updateUserProfile(userProfile);
		result.setResult(new MainSiteUserVO(userProfile));
		result.setCode(userProfile == null ? ResponseCode.RES_ERROR : ResponseCode.RES_SUCCESS);
		result.setMessage(userProfile == null ? "基本信息保存失败！" : "基本信息保存成功！");
		return result;
	}


	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.UserProfileFacade#getVerifyCode(long,
	 *      java.lang.String)
	 */
	@Override
	public BaseJsonVO getVerifyCode(long userId, String phoneNum) {
		BaseJsonVO result = new BaseJsonVO();
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		Object lastSendTime = session.getAttribute(VERIFICATION_CODE_LAST_SEND_TIME);
		// 同一账号验证码发送间隔必须大于1分钟
		if (lastSendTime != null && System.currentTimeMillis() - ((Long) lastSendTime).longValue() < 60 * 1000) {
			result.setCode(201);
			result.setMessage("验证码发送过于频繁，请稍后再试！");
			return result;
		}
		if (PhoneNumberUtil.isMobilePhone(phoneNum)) {
			VerificationCode verificationCode = verificationCodeService.sendVerificationCodeOfSms(phoneNum, 10);
			session.setAttribute(VERIFICATION_CODE_LAST_SEND_TIME, Long.valueOf(System.currentTimeMillis()));
			result.setCode(verificationCode == null ? ResponseCode.RES_ERROR : ResponseCode.RES_SUCCESS);
			result.setMessage(verificationCode == null ? "验证码发送失败！" : "验证码发送成功！");
			result.setResult(verificationCode);
		} else {
			result.setCode(ResponseCode.RES_ERROR);
			result.setMessage("手机号码错误！");
		}
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.UserProfileFacade#savePhoneNum(long,
	 *      java.lang.String, java.lang.String,
	 *      com.xyl.mmall.task.dto.VerificationCode)
	 */
	@Override
	public BaseJsonVO savePhoneNum(long userId, String phoneNum, String code, VerificationCode verificationCode) {
		BaseJsonVO result = new BaseJsonVO();
		String newPhoneNum = null;
		if (StringUtils.isNumeric(code)) {
			VCodeResult codeResult = verificationCodeService.validateVerificationCode(verificationCode,
					Integer.parseInt(code));
			if (VCodeResult.RETCODE_SUCCESS == codeResult.getRetCode()
					&& phoneNum.equals(verificationCode.getCredential())) {
				UserProfileDTO userProfile = userProfileService.upsertUserMobileAndEmail(userId, phoneNum, null);
				newPhoneNum = null == userProfile ? null : userProfile.getMobile();
			}
		}
		result.setCode(newPhoneNum == null ? ResponseCode.RES_ERROR : ResponseCode.RES_SUCCESS);
		result.setMessage(newPhoneNum == null ? "验证码错误！" : "手机号码绑定成功！");
		return result;
	}

	@Override
	public boolean bindMobile(UserProfileDTO userProfile) {
		return userProfileService.updateUserBaseInfo(userProfile).getUserId() > 0l;
	}

	@Override
	public UserProfileDTO getUserProfile(String userName) {
		return userProfileService.findUserProfileByUserName(userName);
	}
}
