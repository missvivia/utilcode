package com.xyl.mmall.mobile.facade.param;

import java.io.Serializable;

public class MobileFeedBackAO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3521515569407374880L;
	private String message;
	private String email;
	private String phone;
	private String version;
	private String os;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}

	
	
}
