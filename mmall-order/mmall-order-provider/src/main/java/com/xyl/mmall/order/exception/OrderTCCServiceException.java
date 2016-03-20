package com.xyl.mmall.order.exception;

import com.xyl.mmall.framework.exception.IPrintErrorLog;

/**
 * OrderTCCService的异常
 * 
 * @author dingmingliang
 * 
 */
public class OrderTCCServiceException extends RuntimeException implements IPrintErrorLog{

	private static final long serialVersionUID = 20140909L;

	/**
	 * 
	 */
	public OrderTCCServiceException() {
		super();
	}

	/**
	 * @param mess
	 */
	public OrderTCCServiceException(String mess) {
		super(mess);
	}

	/**
	 * @param mess
	 * @param tranId
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public static OrderTCCServiceException genOrderTCCServiceException(String mess, long tranId, Long userId,
			Long orderId) {
		mess = mess + " tranId=" + tranId + ", userId=" + userId + ", orderId=" + orderId;
		return new OrderTCCServiceException(mess);
	}
}
