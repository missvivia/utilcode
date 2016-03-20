/**
 * 
 */
package com.xyl.mmall.oms.warehouse.exception;

import com.xyl.mmall.framework.exception.IReThrowException;

/**
 * @author hzzengchengyuan
 * 
 */
public class WarehouseCallerException extends RuntimeException implements IReThrowException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WarehouseCallerException() {
		super();
	}

	public WarehouseCallerException(String message) {
		super(message);
	}

	public WarehouseCallerException(String stringFormat, Object... args) {
		super(String.format(stringFormat, args));
	}

	public WarehouseCallerException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public WarehouseCallerException(Throwable throwable) {
		super(throwable);
	}
}
