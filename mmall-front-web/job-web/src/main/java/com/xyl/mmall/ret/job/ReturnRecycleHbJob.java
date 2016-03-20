package com.xyl.mmall.ret.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.timer.facade.ReturnTimerFacade;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年11月7日 下午12:46:50
 * 
 * 退款成功，回收红包
 * 
 */
@JobPath("/returnRecycleHb")
@Service
public class ReturnRecycleHbJob extends BaseJob {
	
	private static final Logger logger = Logger.getLogger(ReturnRecycleHbJob.class);
	
	@Resource
	private ReturnTimerFacade retTimer;
	
	@Override
	public boolean execute(JobParam param) {
		RetArg retArg = retTimer.recycleHb();
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
