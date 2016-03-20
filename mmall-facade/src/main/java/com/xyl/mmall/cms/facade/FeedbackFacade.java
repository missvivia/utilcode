package com.xyl.mmall.cms.facade;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;

public interface FeedbackFacade {
	
	public RetArg getFeedBackList(long startTime, long endTime, Long areaId,
			String system, String version, String key, DDBParam param);
	/**
	 * 返回用户反馈中的所有的系统
	 * @return
	 */
	public List<String> getAllSystems();
	/**
	 * 返回用户反馈中的所有的版本
	 * @return
	 */
	public List<String> getAllVersions();
}
