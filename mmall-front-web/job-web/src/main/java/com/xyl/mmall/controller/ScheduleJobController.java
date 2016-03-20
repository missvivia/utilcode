package com.xyl.mmall.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * 
 * @author hzzhanghui
 *
 */
@Controller
public class ScheduleJobController {
	
	// PO facade
	@Resource
	private ScheduleFacade scheduleFacade;
	
	/**
	 * Make PO status offline.
	 * 
	 * cron: 0 0 1 * * ?
	 * @return
	 */
	@RequestMapping(value = "/po/timer/offline")
	@ResponseBody
	public BaseJsonVO scheduleTimerOffline() {
		scheduleFacade.scheduleTimerOffline();
		
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}
	
	/**
	 * Auto cancel like relationship after PO being offline for 15 days later.
	 * cron: 0 30 1 * * ?
	 * @return
	 */
	@RequestMapping(value = "/po/timer/unlike")
	@ResponseBody
	public BaseJsonVO scheduelTimerUnlike() {
		scheduleFacade.scheduelTimerUnlike();
		
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}
}
