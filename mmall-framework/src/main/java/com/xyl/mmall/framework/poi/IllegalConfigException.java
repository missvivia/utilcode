/**
 * 
 */
package com.xyl.mmall.framework.poi;

/**
 * @author jmy
 *
 */
@SuppressWarnings("serial")
public class IllegalConfigException extends Exception {
    public IllegalConfigException() {
        super();
    }

    public IllegalConfigException(String message) {
        super(message);
    }

    public IllegalConfigException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public IllegalConfigException(Throwable cause) {
        super(cause);
    }

    protected IllegalConfigException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
