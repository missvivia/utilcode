package com.xyl.mmall.framework.exception;

public class AppException extends RuntimeException implements IPrintInfoLog, IReThrowException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4335053134865275741L;

	public AppException() {
		super();
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(Throwable cause) {
		super(cause);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}
}
