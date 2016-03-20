/**
 * 
 */
package com.xyl.mmall.member.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.member.enums.Gender;

/**
 * @author lihui
 *
 */
@AnnonOfClass(tableName = "Mmall_Member_UserProfile", desc = "主站用户信息")
public class UserProfile implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true, policy = true)
	private long userId;

	@AnnonOfField(desc = "用户名", type = "VARCHAR(64)", uniqueKey = true)
	private String userName;

	@AnnonOfField(desc = "注册时间")
	private long regTime;

	@AnnonOfField(desc = "昵称", type = "VARCHAR(64)", notNull = false, safeHtml = true)
	private String nickName;

	@AnnonOfField(desc = "电子邮箱", type = "VARCHAR(32)", notNull = false, safeHtml = true)
	private String email;

	@AnnonOfField(desc = "手机号码", type = "VARCHAR(16)", notNull = false, safeHtml = true)
	private String mobile;

	@AnnonOfField(desc = "联系电话", type = "VARCHAR(32)", notNull = false, safeHtml = true)
	private String tel;

	@AnnonOfField(desc = "性别(-1:未选择,0:男,1:女)", notNull = false, unsigned = false)
	private Gender gender;

	@AnnonOfField(desc = "出生年份", notNull = false)
	private int birthYear;

	@AnnonOfField(desc = "出生月份", notNull = false)
	private int birthMonth;

	@AnnonOfField(desc = "出生日期", notNull = false)
	private int birthDay;

	@AnnonOfField(desc = "用户头像地址", type = "VARCHAR(255)", notNull = false, safeHtml = true)
	private String userImageURL;

	@AnnonOfField(desc = "许可证号", defa = "", notNull = false)
	private String licence;

	@AnnonOfField(desc = "是否可用，1可用，其余无效", notNull = true, defa = "1")
	private int isValid;

	@AnnonOfField(desc = "PlatformType, 0运营，1backend，2mainsite，3手机，4wap，5erp，6地推", notNull = true, defa = "0")
	private int platformType;

	@AnnonOfField(desc = "是否已领取新手券，1已领，0未领取", defa = "1")
	private int hasNoobCoupon;

	@AnnonOfField(desc = "0普通用户，1中烟用户", defa = "0")
	private int userType;

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the regTime
	 */
	public long getRegTime() {
		return regTime;
	}

	/**
	 * @param regTime
	 *            the regTime to set
	 */
	public void setRegTime(long regTime) {
		this.regTime = regTime;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName
	 *            the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel
	 *            the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * @return the birthYear
	 */
	public int getBirthYear() {
		return birthYear;
	}

	/**
	 * @param birthYear
	 *            the birthYear to set
	 */
	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	/**
	 * @return the birthMonth
	 */
	public int getBirthMonth() {
		return birthMonth;
	}

	/**
	 * @param birthMonth
	 *            the birthMonth to set
	 */
	public void setBirthMonth(int birthMonth) {
		this.birthMonth = birthMonth;
	}

	/**
	 * @return the birthDay
	 */
	public int getBirthDay() {
		return birthDay;
	}

	/**
	 * @param birthDay
	 *            the birthDay to set
	 */
	public void setBirthDay(int birthDay) {
		this.birthDay = birthDay;
	}

	/**
	 * @return the userImageURL
	 */
	public String getUserImageURL() {
		return userImageURL;
	}

	/**
	 * @param userImageURL
	 *            the userImageURL to set
	 */
	public void setUserImageURL(String userImageURL) {
		this.userImageURL = userImageURL;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public int getPlatformType() {
		return platformType;
	}

	public void setPlatformType(int platformType) {
		this.platformType = platformType;
	}

	public int getHasNoobCoupon() {
		return hasNoobCoupon;
	}

	public void setHasNoobCoupon(int hasNoobCoupon) {
		this.hasNoobCoupon = hasNoobCoupon;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

}
