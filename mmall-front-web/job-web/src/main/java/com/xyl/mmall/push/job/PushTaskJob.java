package com.xyl.mmall.push.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobCodeInfo;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;

@JobPath("/push/scan")
@Service
public class PushTaskJob extends BaseJob{
	
	@Autowired
	private MobilePushManageFacade mobilePushManageFacade;
	
	private static final Logger logger=LoggerFactory.getLogger(PushTaskJob.class);

	@Override
	public boolean execute(JobParam param) {
		Date dateStart = (Date) param.getParamMap().get(JobCodeInfo.START_TIME_PARAM);
		Date dateEnd = (Date) param.getParamMap().get(JobCodeInfo.END_TIME_PARAM);
		
		logger.info("===start:"+dateStart+",end:"+dateEnd);
		return mobilePushManageFacade.pushRun(dateStart.getTime(), dateEnd.getTime());
	}

}
