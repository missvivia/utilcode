package com.xyl.mmall.cms.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.facade.FeedbackFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.ip.service.UserFeedbackService;

@Facade
public class FeedbackFacadeImpl implements FeedbackFacade {

	@Autowired
	UserFeedbackService userFeedbackService;
	
	@Override
	public RetArg getFeedBackList(long startTime, long endTime, Long areaId,
			String system, String version, String key, DDBParam param) {
		return userFeedbackService.getFeedBackList(startTime, endTime, areaId, system, version, key, param);
	}

	@Override
	public List<String> getAllSystems() {
		return userFeedbackService.getAllSystems();
	}

	@Override
	public List<String> getAllVersions() {
		return userFeedbackService.getAllVersions();
	}

}
