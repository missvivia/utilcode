package com.xyl.mmall.po.job;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.facade.ScheduleFacade;

/**
 * 档期失效时把所有相关商品也置为失效的Job.
 * 
 * cron: 0 0 10 * * ?  每天上午10点
 * 
 * @return
 * @author hzzhanghui
 * 
 */
@Service
@JobPath("/po/prd/invalid")
public class POPrdInvalidJob extends BaseJob {
	@Resource
	private ScheduleFacade scheduleFacade;

	@Override
	public boolean execute(JobParam param) {
		scheduleFacade.invalidPrdsForExpiredPO(true);
		scheduleFacade.invalidPrdsForExpiredPO(false);
		return true;
	}

}
