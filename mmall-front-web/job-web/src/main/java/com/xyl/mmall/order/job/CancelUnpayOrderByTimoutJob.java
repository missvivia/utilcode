package com.xyl.mmall.order.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.timer.facade.OrderTimerFacade;

/**
 * 定时处理超时未付的订单
 * 
 * @author dingmingliang
 * 
 */
@Service
@JobPath("/order/timer/cancelUnpayOrderByTimout")
public class CancelUnpayOrderByTimoutJob extends BaseJob {

	@Autowired
	private OrderTimerFacade orderTimerFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.base.BaseJob#execute(com.xyl.mmall.base.JobParam)
	 */

	public boolean execute(JobParam param) {
		orderTimerFacade.cancelUnpayOrderByTimout();
		return true;
	}
	
	@Override
	@Scheduled(cron="0 0/1 * * * ? ")
	public void executeJob(){
		orderTimerFacade.cancelUnpayOrderByTimout();
	}

}
