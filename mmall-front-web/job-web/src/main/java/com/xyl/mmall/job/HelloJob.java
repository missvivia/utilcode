package com.xyl.mmall.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;

@JobPath("/hello")
@Service
public class HelloJob extends BaseJob{
	
	private static final Logger logger=LoggerFactory.getLogger(HelloJob.class);

	@Override
	public boolean execute(JobParam param) {
		logger.info("execute job");
		return true;
	}

}
