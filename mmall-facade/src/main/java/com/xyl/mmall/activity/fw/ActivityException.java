package com.xyl.mmall.activity.fw;

/**
 * Base RuntimeException for errors that occur when executing activity
 * operations.
 * 
 * @author hzzhanghui
 */
@SuppressWarnings("serial")
public class ActivityException extends RuntimeException {

	public ActivityException(String message) {
		super(message);
	}

	public ActivityException(Throwable cause) {
		super(cause);
	}

	public ActivityException(String message, Throwable cause) {
		super(message, cause);
	}

}
