package com.xyl.mmall.cms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.cms.meta.Area;

/**
 * 
 * @author hzchaizhf
 * @create 2014年9月15日
 *
 */
public interface AreaDao extends AbstractDao<Area> {
	

	/**
	 * 获取所有站点信息表
	 * 
	 * @param 
	 * @return
	 */
	public List<Area> getAreaList();

	/**
	 * 根据id站点信息
	 * 
	 * @param userId
	 * @return
	 */
	public Area getAreaById(long id);
	
	/**
	 * 根据id列表获取站点列表信息
	 * @param idList
	 * @return
	 */
	public List<Area> getAreadByIdList(List<Long> idList);
}
