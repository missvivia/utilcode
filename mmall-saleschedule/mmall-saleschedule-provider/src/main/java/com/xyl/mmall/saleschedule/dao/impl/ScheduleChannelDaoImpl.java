package com.xyl.mmall.saleschedule.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.saleschedule.dao.ScheduleChannelDao;
import com.xyl.mmall.saleschedule.meta.ScheduleChannel;

/**
 * ScheduleChannel表操作DAO。
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class ScheduleChannelDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ScheduleChannel> implements
		ScheduleChannelDao {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleChannelDaoImpl.class);

	@Override
	public boolean saveScheduleChannel(ScheduleChannel chl) {
		logger.debug("saveScheduleChannel: " + chl);
		try {
			chl = addObject(chl);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean updateScheduleChannel(ScheduleChannel chl) {
		logger.debug("updateScheduleChannel: " + chl);
		try {
			return updateObjectByKey(chl);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean deleteScheduleChannelById(long id) {
		logger.debug("deleteScheduleChannelById: " + id);
		try {
			return deleteById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public ScheduleChannel getScheduleChannelById(long id) {
		logger.debug("getScheduleChannelById: " + id);
		try {
			return getObjectById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Cacheable(value = "poCache")
	@Override
	public List<ScheduleChannel> getScheduleChannelList() {
		return super.getAll();
	}
}
