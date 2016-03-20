package com.xyl.mmall.oms.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.facade.OmsReportFacade;

/**
 * 		未收货物流报表数据生成
 *     cron: 0 30 3 * * ?
 *     @author liujie
 */
@Service
@JobPath("/oms/genReportNoReceipt")
public class OmsReportNoReceiptJob extends BaseJob {

	@Autowired
	private OmsReportFacade omsReportFacade;

	private static final Logger logger = LoggerFactory.getLogger(OmsReportNoReceiptJob.class);

	@Override
	public synchronized boolean execute(JobParam param) {
		logger.info("genReport started");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//取昨天的好秒数
			long date = sdf.parse(sdf.format(new Date())).getTime()-24*3600*1000;
			omsReportFacade.processNoReceipt(date);
 		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		logger.info("GeneratePickOrderJob ended");
		return true;
	}

}
