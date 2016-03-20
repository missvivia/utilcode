package com.xyl.mmall.order.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.timer.facade.InvoiceTimerFacade;

/**
 * 将订单发票转成商家的订单发票的任务
 * 
 * @author dingmingliang
 * 
 */
@Service
@JobPath("/order/timer/convertOrdInvoToSuppInvo")
public class ConvertOrdInvoToSuppInvoJob extends BaseJob {

	@Autowired
	private InvoiceTimerFacade invoiceTimerFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.base.BaseJob#execute(com.xyl.mmall.base.JobParam)
	 */
	public boolean execute(JobParam param) {
		invoiceTimerFacade.convertOrdInvoToSuppInvo();
		return true;
	}

}
