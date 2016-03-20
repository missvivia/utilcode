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
 *         定时启动，将超时未确认入库单取消
 */
@Service
@JobPath("/oms/cancelTimeOutShipOrder")
public class CancelTimeOutShipOrderJob extends BaseJob {

	@Autowired
	private OmsTimerFacade omsTimerFacade;

	private static final Logger logger = LoggerFactory.getLogger(CancelTimeOutShipOrderJob.class);

	@Override
	public synchronized boolean execute(JobParam param) {
		logger.info("cancelTimeOutShipOrder started");
		try {
			omsTimerFacade.cancelTimeOutShipOrder();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("cancelTimeOutShipOrder ended");
		return true;
	}

}
