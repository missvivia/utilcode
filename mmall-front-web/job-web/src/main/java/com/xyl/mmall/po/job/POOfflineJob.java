package com.xyl.mmall.po.job;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.facade.ScheduleFacade;

/**
 * Make PO status offline.
 * 
 * cron: 0 0 1 * * ?
 * 
 * @return
 * @author hzzhanghui
 * 
 */
@Service
@JobPath("/po/timer/offline")
public class POOfflineJob extends BaseJob {
	@Resource
	private ScheduleFacade scheduleFacade;

	@Override
	public boolean execute(JobParam param) {
		scheduleFacade.scheduleTimerOffline();
		return true;
	}

}
