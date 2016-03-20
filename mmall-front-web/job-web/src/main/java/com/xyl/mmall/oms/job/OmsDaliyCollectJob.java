package com.xyl.mmall.oms.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.facade.OmsDaliyCollectFacade;

/**
 */
@Service
@JobPath("/oms/daliyCollectJob")
public class OmsDaliyCollectJob extends BaseJob {

	@Autowired
	private OmsDaliyCollectFacade omsDaliyCollectFacade;

	private static final Logger logger = LoggerFactory.getLogger(OmsDaliyCollectJob.class);

	@Override
	public synchronized boolean execute(JobParam param) {
		logger.info("daliyCollectJob started");
		try {
			omsDaliyCollectFacade.collectShipData();
 		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		logger.info("daliyCollectJob ended");
		return true;
	}

}
