/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;

import com.xyl.mmall.member.dto.UserProfileDTO;

/**
 * UserProfileVO.java created by yydx811 at 2015年6月23日 下午6:18:40 用户信息vo
 *
 * @author yydx811
 */
public class UserProfileVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 489845399922769427L;

	/** 用户id. */
	private long uid;

	/** 账号. */
	private String account;

	/** 昵称. */
	private String nick;

	/** 电话号码. */
	private String mobileNumber;

	/** 电子邮箱. */
	private String emailAddress;

	/** 性别. */
	private Integer sex = -1;

	/** 生日. */
	private String birth;

	/** 最后登录ip. */
	private String lastLoginIp;

	/** 最后登录地址. */
	private String lastLoginAddress;

	/** 许可证号. */
	private String licence;

	/** 用户头像. */
	private String userPhoto;

	/** 密码. */
	private String password;

	/** 是否更新密码. */
	private int isModifyPass;

	/** 是否可用，1可用，其余无效. */
	private int isActive;

	/** PlatformType, 0运营，1backend，2mainsite，3手机，4wap，5erp，6地推. */
	private int platType;

	/** 是否已领取新手券，1已领，0未领取. */
	private int noobCoupon;

	/** 0普通用户，1中烟用户。 */
	private int userType;

	public UserProfileVO() {
	}

	public UserProfileVO(UserProfileDTO obj) {
		this.uid = obj.getUserId();
		this.account = obj.getUserName();
		this.nick = obj.getNickName();
		this.mobileNumber = obj.getMobile();
		this.emailAddress = obj.getEmail();
		this.licence = obj.getLicence();
		this.userPhoto = obj.getUserImageURL();
		if (obj.getGender() != null) {
			this.sex = obj.getGender().getIntValue();
		}
		this.birth = obj.getBirthYear() + "-"
				+ (obj.getBirthMonth() < 10 ? "0" + obj.getBirthMonth() : obj.getBirthMonth()) + "-"
				+ (obj.getBirthDay() < 10 ? "0" + obj.getBirthDay() : obj.getBirthDay());
		this.isActive = obj.getIsValid();
		this.platType = obj.getPlatformType();
		this.noobCoupon = obj.getHasNoobCoupon();
		this.userType = obj.getUserType();
	}

	public UserProfileDTO convertToDTO() {
		UserProfileDTO profileDTO = new UserProfileDTO();
		profileDTO.setUserId(uid);
		profileDTO.setUserName(account);
		profileDTO.setNickName(nick);
		profileDTO.setMobile(mobileNumber);
		profileDTO.setEmail(emailAddress);
		profileDTO.setLicence(licence);
		profileDTO.setUserImageURL(userPhoto);
		profileDTO.setIsValid(isActive);
		profileDTO.setPlatformType(platType);
		profileDTO.setHasNoobCoupon(noobCoupon);
		profileDTO.setUserType(userType);
		return profileDTO;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginAddress() {
		return lastLoginAddress;
	}

	public void setLastLoginAddress(String lastLoginAddress) {
		this.lastLoginAddress = lastLoginAddress;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIsModifyPass() {
		return isModifyPass;
	}

	public void setIsModifyPass(int isModifyPass) {
		this.isModifyPass = isModifyPass;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getPlatType() {
		return platType;
	}

	public void setPlatType(int platType) {
		this.platType = platType;
	}

	public int getNoobCoupon() {
		return noobCoupon;
	}

	public void setNoobCoupon(int noobCoupon) {
		this.noobCoupon = noobCoupon;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

}
