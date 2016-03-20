package com.xyl.mmall.framework.exception;

/**
 * Service层公用的Exception.
 * 
 * @author yangnan
 * 
 */
public class ParamNullException extends Exception implements IPrintInfoLog,IReThrowException {

	private static final long serialVersionUID = 1088922702934998277L;

	public ParamNullException() {
		super();
	}

	public ParamNullException(String message) {
		super(message);
	}

	public ParamNullException(Throwable cause) {
		super(cause);
	}

	public ParamNullException(String message, Throwable cause) {
		super(message, cause);
	}

}
