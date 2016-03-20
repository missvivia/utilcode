/**
 * 
 */
package com.xyl.mmall.mobile.web.vo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.Gender;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class MainSiteUserVO {

	private UserProfileDTO userProfile;
	
	private String birthDay;

	public MainSiteUserVO() {
		userProfile = new UserProfileDTO();
	}

	public MainSiteUserVO(UserProfileDTO userProfile) {
		this.userProfile = userProfile;
		this.birthDay = getBirthDay();
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
	 * @return the account
	 */
	public String getUserName() {
		return userProfile.getUserName();
	}


	/**
	 * @return the birthDay
	 */
	public String getBirthDay() {
		
		return userProfile.getBirthYear()+"-"+userProfile.getBirthMonth()+"-"+userProfile.getBirthDay();
	}

	/**
	 * @param birthDay
	 *            the birthDay to set
	 */
	public void setBirthDay(String birthDate) {
		if(StringUtils.isNotBlank(birthDate)){
			String[] s = birthDate.trim().split("-");
			if(s.length == 3){
				userProfile.setBirthYear(NumberUtils.isNumber(s[0])?NumberUtils.toInt(s[0]):0);
				userProfile.setBirthMonth(NumberUtils.isNumber(s[1])?NumberUtils.toInt(s[1]):0);
				userProfile.setBirthDay(NumberUtils.isNumber(s[2])?NumberUtils.toInt(s[2]):0);
			}
		}
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return userProfile.getNickName();
	}

	/**
	 * @param nickname
	 *            the nickname to set
	 */
	public void setNickname(String nickname) {
		userProfile.setNickName(nickname);
	}

	/**
	 * @return the phone
	 */
	public String getMobile() {
//		if (StringUtils.isNotBlank(userProfile.getMobile())) {
//			return StringUtils.left(userProfile.getMobile(), 3) + "****"
//					+ StringUtils.right(userProfile.getMobile(), 4);
//		}
		return userProfile.getMobile();
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setMobile(String phone) {
		userProfile.setMobile(phone);
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return userProfile.getEmail();
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		userProfile.setEmail(email);
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return userProfile.getGender() == null ? 0 : userProfile.getGender().getIntValue();
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(int gender) {
		userProfile.setGender(Gender.getGenderByIntValue(gender));
	}
	
}
