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
 *         定时启动，将发货单推送到仓库
 */
@Service
@JobPath("/oms/pushOmsShipOrderToWarehose")
public class PushOmsShipOrderToWarehoseJob extends BaseJob {

	@Autowired
	private OmsTimerFacade omsTimerFacade;

	private static final Logger logger = LoggerFactory.getLogger(PushOmsShipOrderToWarehoseJob.class);

	@Override
	public synchronized boolean execute(JobParam param) {
		logger.info("PushOmsShipOrderToWarehoseJob started");
		try {
			omsTimerFacade.pushOmsShipOrderToWarehose();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("PushOmsShipOrderToWarehoseJob ended");
		return true;
	}

}
