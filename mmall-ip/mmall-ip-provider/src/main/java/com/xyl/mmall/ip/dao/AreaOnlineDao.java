package com.xyl.mmall.ip.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.ip.meta.AreaOnline;

public interface AreaOnlineDao extends AbstractDao<AreaOnline>{
	
	public AreaOnline getAreaOnlineById(long areaId);

	/**
	 * 根据状态获取上线区域
	 * @param status -1全部，1上线，0未上线
	 * @return
	 */
	public List<AreaOnline> getAreaOnlineByStatus(int status);
}
