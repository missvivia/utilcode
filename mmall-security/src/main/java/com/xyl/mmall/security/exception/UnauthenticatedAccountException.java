/**
 * 
 */
package com.xyl.mmall.security.exception;

import org.apache.shiro.authc.AccountException;

/**
 * @author lihui
 *
 */
public class UnauthenticatedAccountException extends AccountException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new UnauthenticatedAccountException.
	 */
	public UnauthenticatedAccountException() {
		super();
	}

	/**
	 * Constructs a new UnauthenticatedAccountException.
	 *
	 * @param message
	 *            the reason for the exception
	 */
	public UnauthenticatedAccountException(String message) {
		super(message);
	}

	/**
	 * Constructs a new UnauthenticatedAccountException.
	 *
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public UnauthenticatedAccountException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new UnauthenticatedAccountException.
	 *
	 * @param message
	 *            the reason for the exception
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public UnauthenticatedAccountException(String message, Throwable cause) {
		super(message, cause);
	}
}
