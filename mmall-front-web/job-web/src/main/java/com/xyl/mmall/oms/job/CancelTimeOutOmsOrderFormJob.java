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
 *         定时启动，将超时未配送订单取消
 */
@Service
@JobPath("/oms/cancelTimeOutOmsOrderFormJob")
public class CancelTimeOutOmsOrderFormJob extends BaseJob {

	@Autowired
	private OmsTimerFacade omsTimerFacade;

	private static final Logger logger = LoggerFactory.getLogger(CancelTimeOutOmsOrderFormJob.class);

	@Override
	public synchronized boolean execute(JobParam param) {
		logger.info("cancelTimeOutOmsOrderFormJob started");
		try {
			omsTimerFacade.cancelTimeOutOrderForm();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("cancelTimeOutOmsOrderFormJob ended");
		return true;
	}

}
