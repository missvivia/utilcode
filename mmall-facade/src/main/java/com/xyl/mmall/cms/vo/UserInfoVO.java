/**
 * 
 */
package com.xyl.mmall.cms.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.member.dto.UserLoginInfoDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.Gender;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class UserInfoVO {

	private UserProfileDTO userProfile;

	private UserLoginInfoDTO userLoginInfo;

	private boolean blackListUser;

	private BigDecimal redPocketBalance;

	public UserInfoVO() {
		this.userProfile = new UserProfileDTO();
	}

	public UserInfoVO(UserProfileDTO userProfile) {
		this.setUserProfile(userProfile);
	}

	/**
	 * @return the userProfile
	 */
	@JsonIgnore
	public UserProfileDTO getUserProfile() {
		return userProfile;
	}

	/**
	 * @param userProfile
	 *            the userProfile to set
	 */
	public void setUserProfile(UserProfileDTO userProfile) {
		this.userProfile = userProfile;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userProfile.getUserId();
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userProfile.getUserName();
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return userProfile.getNickName();
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return userProfile.getMobile();
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return userProfile.getEmail();
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return userProfile.getGender() == null ? Gender.NULL.getDesc() : userProfile.getGender().getDesc();
	}

	/**
	 * @return activateTime
	 */
	public long getActivateTime() {
		return userProfile.getRegTime();
	}

	/**
	 * @return the birthDay
	 */
	public String getBirthDay() {
		if (0 != userProfile.getBirthYear() && 0 != userProfile.getBirthMonth() && 0 != userProfile.getBirthDay()) {
			return new StringBuilder(10).append(userProfile.getBirthYear()).append("-")
					.append(userProfile.getBirthMonth()).append("-").append(userProfile.getBirthDay()).toString();
		}
		return null;
	}

	/**
	 * @return the userLoginInfo
	 */
	@JsonIgnore
	public UserLoginInfoDTO getUserLoginInfo() {
		return userLoginInfo;
	}

	/**
	 * @param userLoginInfo
	 *            the userLoginInfo to set
	 */
	public void setUserLoginInfo(UserLoginInfoDTO userLoginInfo) {
		this.userLoginInfo = userLoginInfo;
	}

	/**
	 * @return lastLoginTime
	 */
	public long getLastLoginTime() {
		return userLoginInfo == null ? 0L : userLoginInfo.getLoginTime();
	}

	/**
	 * @return lastLoginIp
	 */
	public String getLastLoginIp() {
		return userLoginInfo == null ? null : userLoginInfo.getLoginIp();
	}

	/**
	 * @return the blackListUser
	 */
	public boolean isBlackListUser() {
		return blackListUser;
	}

	/**
	 * @param blackListUser
	 *            the blackListUser to set
	 */
	public void setBlackListUser(boolean blackListUser) {
		this.blackListUser = blackListUser;
	}

	/**
	 * @return the redPocketBalance
	 */
	public BigDecimal getRedPocketBalance() {
		return redPocketBalance;
	}

	/**
	 * @param redPocketBalance
	 *            the redPocketBalance to set
	 */
	public void setRedPocketBalance(BigDecimal redPocketBalance) {
		this.redPocketBalance = redPocketBalance;
	}

	/**
	 * @return the loginProvince
	 */
	public String getLoginProvince() {
		return userLoginInfo == null ? null : userLoginInfo.getLoginProvince();
	}

}
