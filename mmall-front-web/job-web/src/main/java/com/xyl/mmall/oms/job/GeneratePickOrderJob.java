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
 *         定时启动，生成拣货单
 */
@Service
@JobPath("/oms/genPickOrder")
public class GeneratePickOrderJob extends BaseJob {

	@Autowired
	private OmsTimerFacade omsTimerFacade;

	private static final Logger logger = LoggerFactory.getLogger(GeneratePickOrderJob.class);

	@Override
	public synchronized boolean execute(JobParam param) {
		logger.info("GeneratePickOrderJob started");
		try {
			omsTimerFacade.generatePickOrder();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("GeneratePickOrderJob ended");
		return true;
	}

}
