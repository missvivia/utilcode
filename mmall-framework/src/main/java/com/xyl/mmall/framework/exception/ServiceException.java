package com.xyl.mmall.framework.exception;

/**
 * Service层公用的Exception.
 * 
 * @author yangnan
 * 
 */
public class ServiceException extends RuntimeException implements IPrintInfoLog,IReThrowException {

	private static final long serialVersionUID = 1088922702934998277L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
