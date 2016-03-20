package com.xyl.mmall.ip.service;

import java.util.List;

import com.xyl.mmall.ip.meta.AreaOnline;

public interface AreaOnlineService {

	/**
	 * 查看指定区域是否存在
	 * 
	 * @param areaid
	 * @return
	 */
	public boolean areaExist(long areaid);

	/**
	 * 根据状态获取上线区域
	 * @param status -1全部，1上线，0未上线
	 * @return
	 */
	public List<AreaOnline> getAreaOnlineByStatus(int status);
}
