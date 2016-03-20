package com.xyl.mmall.task.dao;

import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.task.meta.MobileConfig;

/**
 * 
 * @author jiangww
 *
 */
public interface MobileConfigDao extends AbstractDao<MobileConfig> {
	public MobileConfig getMobileConfig (String ConfigVersion);
	
	public Map<String,String> getAllMobileConfig ();
}
