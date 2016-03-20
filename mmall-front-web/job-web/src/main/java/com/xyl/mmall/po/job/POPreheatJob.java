package com.xyl.mmall.po.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.facade.ScheduleFacade;

/**
 * This job is used to preheat for every province, all channles for each province.
 * 
 * Cron expression: 0 0 8 * * ?    Every day 08:00:00
 * 
 * @return
 * @author hzzhanghui
 * 
 */
@Service
@JobPath("/po/timer/popreheat")
public class POPreheatJob extends BaseJob {
	@Resource
	private ScheduleFacade scheduleFacade;

	@Override
	public boolean execute(JobParam param) {
		//scheduleFacade.clearCache();
		
		List<IdNameBean> provinceList = scheduleFacade.getAllProvince();
		for (IdNameBean province : provinceList) {
			long saleSiteCode = Long.parseLong(province.getId());
			scheduleFacade.getScheduleListForChl(null, 1, saleSiteCode, 0, 0, 0);
			scheduleFacade.getScheduleListForChl(null, 2, saleSiteCode, 0, 0, 0);
			scheduleFacade.getScheduleListForChl(null, 3, saleSiteCode, 0, 0, 0);
			scheduleFacade.getScheduleListForChl(null, 5, saleSiteCode, 0, 0, 0);
		}
		
		return true;
	}

}
