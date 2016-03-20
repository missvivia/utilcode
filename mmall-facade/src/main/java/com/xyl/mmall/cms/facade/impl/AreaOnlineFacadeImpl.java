package com.xyl.mmall.cms.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.cms.facade.AreaOnlineFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.ip.meta.AreaOnline;
import com.xyl.mmall.ip.service.AreaOnlineService;

@Facade("areaOnlineFacade")
public class AreaOnlineFacadeImpl implements AreaOnlineFacade{
	
	@Autowired
	private AreaOnlineService areaOnlineService;

	@Override
	public boolean areaExist(long areaid) {
		return areaOnlineService.areaExist(areaid);
	}

	@Override
	public List<AreaOnline> getAreaOnlineByStatus(int status) {
		return areaOnlineService.getAreaOnlineByStatus(status);
	}

}
