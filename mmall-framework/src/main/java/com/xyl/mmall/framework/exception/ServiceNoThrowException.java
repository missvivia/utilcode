package com.xyl.mmall.framework.exception;

/**
 * Service层公用的Exception.不会rethrow到上层
 * 
 * @author yangnan
 * 
 */
public class ServiceNoThrowException extends RuntimeException implements IPrintInfoLog {

	private static final long serialVersionUID = 1088922702934998277L;

	public ServiceNoThrowException() {
		super();
	}

	public ServiceNoThrowException(String message) {
		super(message);
	}

	public ServiceNoThrowException(Throwable cause) {
		super(cause);
	}

	public ServiceNoThrowException(String message, Throwable cause) {
		super(message, cause);
	}

}
