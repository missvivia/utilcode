package com.xyl.mmall.framework.exception;

/**
 * 表单重复提交error
 * @author author:lhp
 *
 * @version date:2015年6月25日上午11:21:02
 */
public class FormDupSubmitException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7792967018229125660L;

	public FormDupSubmitException() {
		super();
	}

	public FormDupSubmitException(String message) {
		super(message);
	}

	public FormDupSubmitException(Throwable cause) {
		super(cause);
	}

	public FormDupSubmitException(String message, Throwable cause) {
		super(message, cause);
	}
}
