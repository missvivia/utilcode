package com.xyl.mmall.task.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.task.dto.DeviceLocationDTO;
import com.xyl.mmall.task.exception.PushException;
import com.xyl.mmall.task.service.DriverService;
import com.xyl.mmall.task.dao.DeviceLocationDao;

/**
 * 
 * @author jiangww
 *
 */
@Service("driverService")
public class DriverServiceImpl implements DriverService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected DeviceLocationDao deviceLocationDao;

	@Resource(name = "pushSenderService")
	protected PushSenderService pushSenderService;

	@Override
	public DeviceLocationDTO addOrUpdateId(DeviceLocationDTO deviceLocationDTO) {
		return deviceLocationDao.insertOrUpdate(deviceLocationDTO);
	}

	@Override
	public HashMap<String,Object> genSign(String account) {
		try {
			return pushSenderService.GenSign(account);
		} catch (PushException e) {
			logger.error(e.toString());
		}
		return null;
	}

}
