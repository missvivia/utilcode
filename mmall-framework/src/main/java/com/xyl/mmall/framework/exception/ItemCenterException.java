package com.xyl.mmall.framework.exception;

import com.xyl.mmall.framework.enums.ErrorCode;

public class ItemCenterException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2604442727703156892L;
	private int errorCode;
	private String msg;
	public ItemCenterException(){
		super();
	}
	
	public ItemCenterException(String message) {
		super(message);
	}
	
	public ItemCenterException(ErrorCode errorCode){
		super();
		this.errorCode = errorCode.getIntValue();
		this.msg = errorCode.getDesc();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
