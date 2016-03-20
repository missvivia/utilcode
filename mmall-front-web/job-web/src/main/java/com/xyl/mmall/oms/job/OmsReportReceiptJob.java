package com.xyl.mmall.oms.job;

import java.util.Calendar;
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
 * 		收货物流报表数据生成
 *     cron: 0 30 1 * * ?
 *     @author liujie
 */
@Service
@JobPath("/oms/genReportReceipt")
public class OmsReportReceiptJob extends BaseJob {

	@Autowired
	private OmsReportFacade omsReportFacade;

	private static final Logger logger = LoggerFactory.getLogger(OmsReportReceiptJob.class);

	@Override
	public synchronized boolean execute(JobParam param) {
		logger.info("genReport started");
		try {
			long date = getDayTimeZeroMillis() - 24*3600*1000;
			return omsReportFacade.processReceipt(date);
 		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		
	}
	
	public static long getDayTimeZeroMillis() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

}
