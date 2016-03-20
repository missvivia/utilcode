package com.xyl.mmall.ip.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.ip.meta.UserFeedback;

public interface UserFeedbackDao extends AbstractDao<UserFeedback> {

	/**
	 * 添加用户反馈
	 * @param feedback
	 */
	public boolean addNewFeedback(UserFeedback feedback);
	/**
	 * 用户反馈的结果条件查询
	 * @param startTime
	 * @param endTime
	 * @param areaId
	 * @param system
	 * @param version
	 * @param key
	 * @param param
	 * @return
	 */
	public List<UserFeedback> getFeedbackList(long startTime, long endTime, long areaId,
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
