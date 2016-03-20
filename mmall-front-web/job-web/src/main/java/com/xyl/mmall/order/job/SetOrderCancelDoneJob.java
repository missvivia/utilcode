package com.xyl.mmall.order.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.timer.facade.OrderTimerFacade;

/**
 * 关闭已经处理完毕的取消订单任务
 * 
 * @author dingmingliang
 * 
 */
@Service
@JobPath("/order/timer/setCancelDone")
public class SetOrderCancelDoneJob extends BaseJob {

	@Autowired
	private OrderTimerFacade orderTimerFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.base.BaseJob#execute(com.xyl.mmall.base.JobParam)
	 */
	public boolean execute(JobParam param) {
		orderTimerFacade.setOrderCancelToDone();
		return true;
	}

}
