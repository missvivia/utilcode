/**
 * 
 */
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
 * @author hzzengchengyuan
 *
 */
@Service
@JobPath("/oms/calcuateFreightJob")
public class CalcuateFreightJob  extends BaseJob {
	
	@Autowired
	private OmsTimerFacade omsTimerFacade;

	private static final Logger logger = LoggerFactory.getLogger(CalcuateFreightJob.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.base.BaseJob#execute(com.xyl.mmall.base.JobParam)
	 */
	@Override
	public boolean execute(JobParam param) {
		logger.info("CalcuateFreightJob started");
		try {
			omsTimerFacade.calcuateFreight();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		logger.info("CalcuateFreightJob ended");
		return true;
	}

}
