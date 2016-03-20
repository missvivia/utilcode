package com.xyl.mmall.saleschedule.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;

/**
 * 档期副表DAO
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleViceDao extends AbstractDao<ScheduleVice> {

	/**
	 * 新增
	 * 
	 * @return 新增成功返回true。否则返回false。
	 */
	boolean saveScheduleVice(ScheduleVice bean);

	/**
	 * 更新
	 * 
	 * @param bean
	 * @return
	 */
	boolean updateScheduleVice(ScheduleVice bean);

	/**
	 * 查询
	 * 
	 * @return
	 */
	ScheduleVice getScheduleViceByScheduleId(long scheduleId);

	/**
	 * 删除
	 * 
	 * @param scheduleId
	 * @return
	 */
	boolean deleteScheduleViceByScheduleId(long scheduleId);
	
	/**
	 * 
	 * @param vice
	 * @return
	 */
	boolean updateSchedulePageIdAndBannerId(ScheduleVice vice);
	
	/**
	 * 
	 * @param poFollowerUserName
	 * @param poFollowerUserId
	 * @param poId
	 * @return
	 */
	boolean updatePOFollowerUser(String poFollowerUserName, Long poFollowerUserId, long poId);
	
}
