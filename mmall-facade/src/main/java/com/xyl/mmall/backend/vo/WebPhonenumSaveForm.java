package com.xyl.mmall.backend.vo;

import java.io.Serializable;

public class WebPhonenumSaveForm implements Serializable{

	/**
	 * 序列
	 */
	private static final long serialVersionUID = -3860273378444880525L;
	
	private String phoneNO;
	
	private String verificationCode;

	public String getPhoneNO() {
		return phoneNO;
	}

	public void setPhoneNO(String phoneNO) {
		this.phoneNO = phoneNO;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
}