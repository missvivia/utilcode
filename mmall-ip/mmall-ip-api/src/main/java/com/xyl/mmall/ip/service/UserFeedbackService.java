package com.xyl.mmall.ip.service;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.ip.meta.UserFeedback;

public interface UserFeedbackService {

	/**
	 * 添加用户反馈
	 * @param feedback
	 * @return
	 */
	public boolean addNewFeedback(UserFeedback feedback);
	/**
	 * 获取用户反馈列表
	 * @param startTime
	 * @param endTime
	 * @param areaId
	 * @param system
	 * @param version
	 * @param key
	 * @param param
	 * @return
	 */
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
