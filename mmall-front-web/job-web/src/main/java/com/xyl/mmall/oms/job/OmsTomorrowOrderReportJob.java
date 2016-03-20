package com.xyl.mmall.oms.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.facade.OmsReportFacade;

/**
 * 现在是每天下午四点半运行这个job
 * @author chengximing
 *
 */
@Service
@JobPath("/oms/genTomorrowOrderReport")
public class OmsTomorrowOrderReportJob extends BaseJob {

	@Autowired
	private OmsReportFacade omsReportFacade;
	
	private static final Logger logger = LoggerFactory.getLogger(OmsTomorrowOrderReportJob.class);
	
	@Override
	public boolean execute(JobParam param) {
		logger.info("OmsTomorrowOrderReportJob started");
		if (omsReportFacade.syncDataforTomorrowOrder()) {
			logger.info("OmsTomorrowOrderReportJob ended");
			return true;
		} else {
			logger.error("OmsTomorrowOrderReportJob does not run normally");
			return false;
		}
	}

}
