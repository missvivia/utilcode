package com.xyl.mmall.order.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.timer.facade.OrderTimerFacade;

/**
 * 定时发送订单到OMS
 * 
 * @author dingmingliang
 * 
 */
@Service
@JobPath("/order/timer/pusToOms")
public class PushOrderToOmsJob extends BaseJob {

	@Autowired
	private OrderTimerFacade orderTimerFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.base.BaseJob#execute(com.xyl.mmall.base.JobParam)
	 */
	public boolean execute(JobParam param) {
		orderTimerFacade.pushOrderToOms();
		return true;
	}

}
