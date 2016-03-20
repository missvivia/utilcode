package com.xyl.mmall.task.dao;

import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.task.dto.DeviceLocationDTO;
import com.xyl.mmall.task.meta.DeviceLocation;

/**
 * 
 * @author jiangww
 *
 */
public interface DeviceLocationDao extends AbstractDao<DeviceLocation> {
	public List<DeviceLocation> getPushUserList(Map<String,String[]> map,long startID);
	
	public List<DeviceLocation> getAllAreaCode();
	
	public List<DeviceLocation> getPushUserByArea(long areaCode,long startID);
	
	public List<DeviceLocation> getPushUserByAccount(long userId);

	public DeviceLocationDTO insertOrUpdate(DeviceLocationDTO deviceLocationDTO);
}
