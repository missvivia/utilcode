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
 *         定时启动，将订单包裹状态的更新通知给上层的订单模块
 */
@Service
@JobPath("/oms/pushOrderPkgToApp")
public class PushOmsOrderPackageStateToAppJob extends BaseJob {

	@Autowired
	private OmsTimerFacade omsTimerFacade;

	private static final Logger logger = LoggerFactory.getLogger(PushOmsOrderPackageStateToAppJob.class);

	@Override
	public synchronized boolean execute(JobParam param) {
		logger.info("PushOmsOrderPackageStateToAppJob started");
		try {
			omsTimerFacade.pushOmsOrderPackageToApp();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("PushOmsOrderPackageStateToAppJob ended");
		return true;
	}

}
