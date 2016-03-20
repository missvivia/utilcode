package com.xyl.mmall.task.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.task.service.MobileConfigService;
import com.xyl.mmall.task.dao.MobileConfigDao;

/**
 * 
 * @author jiangww
 *
 */
@Service("configService")
public class MobileConfigServiceImpl implements MobileConfigService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected MobileConfigDao mobileConfigDao;

	@Override
	public Map<String, String> getAllConfig() {
		// TODO Auto-generated method stub
		return mobileConfigDao.getAllMobileConfig();
	}

	

}
