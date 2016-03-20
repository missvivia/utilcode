package com.xyl.mmall.codaudit.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.timer.facade.CODAuditTimerFacade;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年11月7日 下午12:47:18
 * 
 * 系统自动审核通过收货地址在地址白名单中的到付订单
 * 
 */
@JobPath("/codAuditWhitelist")
@Service
public class CODAuditWhitelistJob extends BaseJob {

	private static final Logger logger = Logger.getLogger(CODAuditWhitelistJob.class);
	
	@Resource
	private CODAuditTimerFacade codAuditTimer;
	
	@Override
	public boolean execute(JobParam param) {
		RetArg retArg = codAuditTimer.passCODAuditInWhiteList();
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
