package com.xyl.mmall.timer.facade;

/**
 * 发票相关的定时器
 * 
 * @author dingmingliang
 * 
 */
public interface InvoiceTimerFacade {

	/**
	 * 将订单发票转成商家的订单发票
	 */
	public void convertOrdInvoToSuppInvo();
	
}