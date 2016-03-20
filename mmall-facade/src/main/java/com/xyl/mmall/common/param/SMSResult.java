package com.xyl.mmall.common.param;

/**
 * 调用短信接口返回结果
 * @author author:lhp
 *
 * @version date:2015年8月11日下午6:35:43
 */
public class SMSResult {
	
	private boolean success;
	
	private String message;
	
	private String code;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	

}
