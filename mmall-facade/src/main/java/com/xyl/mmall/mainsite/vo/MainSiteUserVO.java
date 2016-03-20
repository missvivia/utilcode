/**
 * 
 */
package com.xyl.mmall.mainsite.vo;

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

	public MainSiteUserVO() {
		userProfile = new UserProfileDTO();
	}

	public MainSiteUserVO(UserProfileDTO userProfile) {
		this.userProfile = userProfile;
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
	public String getAccount() {
		return userProfile.getUserName();
	}

	/**
	 * @return the birthYear
	 */
	public int getBirthYear() {
		return userProfile.getBirthYear();
	}

	/**
	 * @param birthYear
	 *            the birthYear to set
	 */
	public void setBirthYear(int birthYear) {
		userProfile.setBirthYear(birthYear);
	}

	/**
	 * @return the birthMonth
	 */
	public int getBirthMonth() {
		return userProfile.getBirthMonth();
	}

	/**
	 * @param birthMonth
	 *            the birthMonth to set
	 */
	public void setBirthMonth(int birthMonth) {
		userProfile.setBirthMonth(birthMonth);
	}

	/**
	 * @return the birthDay
	 */
	public int getBirthDay() {
		return userProfile.getBirthDay();
	}

	/**
	 * @param birthDay
	 *            the birthDay to set
	 */
	public void setBirthDay(int birthDay) {
		userProfile.setBirthDay(birthDay);
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
	public String getPhone() {
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
	public void setPhone(String phone) {
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
	
	public String getLicence() {
		return userProfile.getLicence();
	}
	
	public void setLicence(String licence) {
		userProfile.setLicence(licence);
	}
}
