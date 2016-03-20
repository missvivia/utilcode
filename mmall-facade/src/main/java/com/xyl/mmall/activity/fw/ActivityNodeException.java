package com.xyl.mmall.activity.fw;

/**
 * Base RuntimeException for errors that occur when executing activity node
 * operations.
 * 
 * @author hzzhanghui
 */
@SuppressWarnings("serial")
public class ActivityNodeException extends ActivityException {

	public ActivityNodeException(String message) {
		super(message);
	}

	public ActivityNodeException(String message, Throwable cause) {
		super(message, cause);
	}

}
