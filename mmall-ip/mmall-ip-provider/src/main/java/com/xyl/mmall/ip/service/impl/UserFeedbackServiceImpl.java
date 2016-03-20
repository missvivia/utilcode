package com.xyl.mmall.ip.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.ip.meta.UserFeedback;
import com.xyl.mmall.ip.service.UserFeedbackService;
import com.xyl.mmall.ip.dao.LocationCodeDao;
import com.xyl.mmall.ip.dao.UserFeedbackDao;

@Service("userFeedbackService")
public class UserFeedbackServiceImpl implements UserFeedbackService {

	@Autowired
	LocationCodeDao locationCodeDao;
	
	@Autowired
	UserFeedbackDao userFeedbackDao;
	
	@Override
	public boolean addNewFeedback(UserFeedback feedback) {
		long areaId = feedback.getAreaId();
		String areaName = locationCodeDao.getLocationNameByCode(areaId);
		feedback.setAreaName(areaName);
		return userFeedbackDao.addNewFeedback(feedback);
	}

	@Override
	public RetArg getFeedBackList(long startTime, long endTime, Long areaId,
			String system, String version, String key, DDBParam param) {
		RetArg retArg = new RetArg();
		List<UserFeedback> list = userFeedbackDao.getFeedbackList(startTime, endTime, areaId,
				system, version, key, param);
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, list);
		return retArg;
	}

	@Override
	public List<String> getAllSystems() {
		return userFeedbackDao.getAllSystems();
	}

	@Override
	public List<String> getAllVersions() {
		return userFeedbackDao.getAllVersions();
	}

}
