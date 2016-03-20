package com.xyl.mmall.task.dto;

import java.io.Serializable;

/**
 * 验证码验证结果.
 * 
 * @author wangfeng
 * 
 */
public class VCodeResult implements Serializable {

	private static final long serialVersionUID = -767251292260447374L;

	/** 成功. */
	public static final int RETCODE_SUCCESS = 0;

	/** 验证码错误. */
	public static final int RETCODE_ERROR_10001 = -10001;

	/** 验证码已经过期. */
	public static final int RETCODE_ERROR_10002 = -10002;

	private int retCode;

	private String errMsg;

	public VCodeResult() {
		super();
	}

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}