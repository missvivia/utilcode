package com.xyl.mmall.exception;

public class ExcelFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5497157398612180395L;

	public ExcelFormatException() {
		super();
	}

	public ExcelFormatException(String message) {
		super(message);
	}

	public ExcelFormatException(Throwable cause) {
		super(cause);
	}

	public ExcelFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
