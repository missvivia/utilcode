package com.xyl.mmall.jms.param;

import java.io.Serializable;

public class CancelSmsSo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String phone;

	private String code;

	private String message;

	private String subnum;

	private String mode;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubnum() {
		return subnum;
	}

	public void setSubnum(String subnum) {
		this.subnum = subnum;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
