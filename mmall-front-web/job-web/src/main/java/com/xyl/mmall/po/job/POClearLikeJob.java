package com.xyl.mmall.po.job;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.facade.ScheduleFacade;

/**
 * Auto cancel like relationship after PO being offline for 15 days later.
 * 
 * cron: 0 30 1 * * ?
 * 
 * @return
 * @author hzzhanghui
 * 
 */
@Service
@JobPath("/po/timer/cleanlike")
public class POClearLikeJob extends BaseJob {
	@Resource
	private ScheduleFacade scheduleFacade;

	@Override
	public boolean execute(JobParam param) {
		scheduleFacade.scheduelTimerUnlike();
		return true;
	}

}
