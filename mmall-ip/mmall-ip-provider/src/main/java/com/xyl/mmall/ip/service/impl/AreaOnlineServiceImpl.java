package com.xyl.mmall.ip.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.ip.meta.AreaOnline;
import com.xyl.mmall.ip.service.AreaOnlineService;
import com.xyl.mmall.ip.dao.AreaOnlineDao;

@Service("areaOnlineService")
public class AreaOnlineServiceImpl implements AreaOnlineService {
	
	private static final int STATUS_VALID=1;

	@Autowired
	private AreaOnlineDao areaOnlineDao;
	
	private static final Logger logger=LoggerFactory.getLogger(AreaOnlineServiceImpl.class);

	@Override
	public boolean areaExist(long areaid) {
		logger.info("get area from cache,areaid:"+areaid);
		AreaOnline areaOnline=areaOnlineDao.getAreaOnlineById(areaid);
		return (areaOnline!=null && areaOnline.getStatus()==STATUS_VALID)?true:false;
	}

	@Override
	@Cacheable(value = "areaOnlineCache")
	public List<AreaOnline> getAreaOnlineByStatus(int status) {
		return areaOnlineDao.getAreaOnlineByStatus(status);
	}

}
