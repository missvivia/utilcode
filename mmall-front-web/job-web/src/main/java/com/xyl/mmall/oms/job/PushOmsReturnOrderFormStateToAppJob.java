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
 *         定时启动，将退货订单的状态更新推送到上层的订单退货模块
 */
@Service
@JobPath("/oms/pushRetOrderToApp")
public class PushOmsReturnOrderFormStateToAppJob extends BaseJob {

	@Autowired
	private OmsTimerFacade omsTimerFacade;

	private static final Logger logger = LoggerFactory.getLogger(PushOmsReturnOrderFormStateToAppJob.class);

	@Override
	public synchronized boolean execute(JobParam param) {
		logger.info("PushOmsReturnOrderFormStateToAppJob started");
		try {
			omsTimerFacade.pushOmsReturnOrderFormStateToApp();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("PushOmsReturnOrderFormStateToAppJob ended");
		return true;
	}
}
