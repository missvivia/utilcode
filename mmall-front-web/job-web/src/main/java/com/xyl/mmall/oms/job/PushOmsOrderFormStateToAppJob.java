package com.xyl.mmall.oms.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.timer.facade.OmsTimerFacade;

/**
 * @author zb<br>
 *         定时启动，将订单状态的更新通知给上层的订单模块
 */
@Service
@JobPath("/oms/pushOrderToApp")
public class PushOmsOrderFormStateToAppJob extends BaseJob {

	@Autowired
	private OmsTimerFacade omsTimerFacade;

	private static final Logger logger = LoggerFactory.getLogger(PushOmsOrderFormStateToAppJob.class);

	@Override
	public synchronized boolean execute(JobParam param) {
		logger.info("PushOmsOrderFormStateToAppJob started");
		try {
			omsTimerFacade.pushOmsOrderFormStateToApp();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("PushOmsOrderFormStateToAppJob ended");
		return true;
	}

}
