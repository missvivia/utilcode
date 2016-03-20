package com.xyl.mmall.content.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.timer.facade.ContentNCSTimerFacade;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年11月7日 下午12:45:08
 * 
 * NCS索引定时器
 * 
 */
@JobPath("/contentncs")
@Service
public class ContentNCSJob extends BaseJob {

	private static final Logger logger = Logger.getLogger(ContentNCSJob.class);
	
	@Resource
	private ContentNCSTimerFacade contentNCSTimer;
	
	@Override
	public boolean execute(JobParam param) {
		RetArg retArg = contentNCSTimer.dispatch();
		Boolean result = RetArgUtil.get(retArg, Boolean.class);
		String log = RetArgUtil.get(retArg, String.class);
		if(null != result && Boolean.TRUE == result) {
			logger.info(log);
			return true;
		}
		logger.warn(log);
		return false;
	}

}
