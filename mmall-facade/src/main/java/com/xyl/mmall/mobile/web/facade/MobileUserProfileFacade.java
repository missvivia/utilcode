/**
 * 
 */
package com.xyl.mmall.mobile.web.facade;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.mobile.web.vo.MainSiteUserVO;
import com.xyl.mmall.task.dto.VerificationCode;

/**
 * 用户基本信息Facade
 * 
 * @author lihui
 *
 */
public interface MobileUserProfileFacade {

	/**
	 * 根据用户ID获取用户基本信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户基本信息
	 */
	MainSiteUserVO getUserProfile(long userId);

	/**
	 * 保存指定用户的个人信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param userVO
	 *            用户基本信息
	 * @return 更新后的用户基本信息
	 */
	BaseJsonVO saveUserProfile(long userId, MainSiteUserVO userVO);

	/**
	 * 绑定指定用户的手机号码
	 * 
	 * @param userId
	 *            用户ID
	 * @param phoneNum
	 *            手机号码
	 * @param code
	 *            验证码
	 * @param verificationCode
	 *            前端返回的验证数据
	 * @return
	 */
	BaseJsonVO savePhoneNum(long userId, String phoneNum, String code, VerificationCode verificationCode);

	/**
	 * 用户获取绑定手机的手机验证码
	 * 
	 * @param userId
	 *            用户ID
	 * @param phoneNum
	 *            手机号码
	 * @return
	 */
	BaseJsonVO getVerifyCode(long userId, String phoneNum);

	/**
	 * 绑定手机
	 * @param userProfile
	 * @return
	 */
	public boolean bindMobile(UserProfileDTO userProfile);
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public UserProfileDTO getUserProfile(String userName);
}
