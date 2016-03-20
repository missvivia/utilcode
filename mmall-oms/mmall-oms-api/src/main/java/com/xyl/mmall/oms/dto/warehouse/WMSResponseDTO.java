/**
 * 
 */
package com.xyl.mmall.oms.dto.warehouse;

import java.io.Serializable;

/**
 * @author hzzengchengyuan
 * 
 */
public class WMSResponseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int FLAG_SUCESS = 1;

	public static final int FLAG_FAILURE = 0;

	public static final int FLAG_EXCEPTION = -1;

	/**
	 * 标识。-1:异常(调用仓库接口出现异常)，0:失败(调用仓库接口正常但是返回结果为失败)， 1:成功(正常调用并返回正确结果)
	 */
	private int flag;

	/**
	 * 返回消息
	 */
	private String message;

	private Throwable exception;

	public WMSResponseDTO(int flag) {
		this(flag, null);
	}

	public WMSResponseDTO(int flag, String message) {
		this(flag, message, null);
	}

	public WMSResponseDTO(int flag, String message, Throwable exception) {
		this.flag = flag;
		this.message = message;
		this.exception = exception;
	}

	/**
	 * @return the flag
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public boolean isSucess() {
		return getFlag() == FLAG_SUCESS;
	}

	public boolean isFailure() {
		return getFlag() == FLAG_FAILURE;
	}

	public boolean isException() {
		return getFlag() == FLAG_EXCEPTION;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public String toString() {
		return isSucess() ? "SUCESS" : isException() ? "EXCEPTION" : "FAILURE";
	}
}
