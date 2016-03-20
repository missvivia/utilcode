package com.xyl.mmall.cms.vo;

/**
 * @author lhp
 * @version 2015年12月3日 下午2:17:52
 * 
 */
public class ValetUserVO {

	/**
	 * 小b 账号
	 */
	private String account;

	/**
	 * 小b Id
	 */
	private long userId;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 收货地址
	 */
	private String address;

	/**
	 * 1 有效 其余无效
	 */
	private int isActive;

	public ValetUserVO() {

	}

	public ValetUserVO(UserProfileVO userProfileVO) {
		this.setAccount(userProfileVO.getAccount());
		this.setPhone(userProfileVO.getMobileNumber());
		this.setNickName(userProfileVO.getNick());
		this.setEmail(userProfileVO.getEmailAddress());
		this.setUserId(userProfileVO.getUid());
		this.setIsActive(userProfileVO.getIsActive());
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

}
