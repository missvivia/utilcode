/**
 * 
 */
package com.xyl.mmall.mobile.facade.vo;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.UserProfileType;
import com.xyl.mmall.member.meta.UserProfile;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileUserVO {

	private UserProfileDTO userProfile;

	private String userAttr;

	public MobileUserVO(UserProfileDTO userProfile) {
		this.userProfile = userProfile;
	}

	/**
	 * @return the userProfile
	 */
	@JsonIgnore
	public UserProfile getUserProfile() {
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
	 * @return the email
	 */
	public String getEmail() {
		return userProfile.getEmail();
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return userProfile.getMobile();
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return userProfile.getTel();
	}

	/**
	 * @return the sex
	 */
	public int getGender() {
		return userProfile.getGender() == null ? 0 : userProfile.getGender().getIntValue();
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
	 * @return the userImageURL
	 */
	public String getUserImageURL() {
		return userProfile.getUserImageURL();
	}

	/**
	 * @return the userAttr
	 */
	public String getUserAttr() {
		return userAttr;
	}

	/**
	 * @param userAttr
	 *            the userAttr to set
	 */
	public void setUserAttr(String userAttr) {
		this.userAttr = userAttr;
	}

	/**
	 * @return the loginType
	 */
	public int getloginType() {
		String[] userNameArr = StringUtils.split(userProfile.getUserName(), "@");
		if (userNameArr != null && userNameArr.length == 2) {
			for (UserProfileType type : UserProfileType.values()) {
				if (StringUtils.contains(userNameArr[1], type.getDesc())) {
					return type.getIntValue();
				}
			}
		}
		return UserProfileType.URS.getIntValue();
	}
}
