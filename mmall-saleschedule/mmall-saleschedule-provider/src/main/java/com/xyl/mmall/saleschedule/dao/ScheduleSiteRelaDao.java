package com.xyl.mmall.saleschedule.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;

/**
 * 档期销售站点关系DAO
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleSiteRelaDao extends AbstractDao<ScheduleSiteRela> {

	/**
	 * 新增
	 * 
	 * @return 新增成功返回true。否则返回false。
	 */
	boolean saveScheduleSiteRela(ScheduleSiteRela bean);

	/**
	 * 批量新增
	 * 
	 * @param beanList
	 * @return
	 */
	boolean batchSaveScheduleSiteRela(List<ScheduleSiteRela> beanList);

	/**
	 * 删除
	 * 
	 * @param scheduleId 档期id
	 * @return
	 */
	boolean deleteScheduleSiteRelaByScheduleId(long scheduleId);
	
	/**
	 * Update show order.
	 * 
	 * @param showOrder
	 * @param scheduleId
	 * @param saleSiteId
	 * @return
	 */
	boolean updateScheduleSiteRelaShowOrder(int showOrder, long scheduleId, long saleSiteId);
	
	/**
	 * Update show order.
	 * 
	 * @param showOrder
	 * @param scheduleId
	 * @return
	 */
	boolean updateScheduleSiteRelaShowOrder(int showOrder, long scheduleId);
	
	/**
	 * Update po startTime.
	 * 
	 * @param poStartTime
	 * @param scheduleId
	 * @return
	 */
	boolean updateScheduleSiteRelaPOStartTime(long poStartTime, long scheduleId);
	
	/**
	 * 查询
	 * 
	 * @param scheduleId 档期id
	 * @return 永远不会返回null
	 */
	List<ScheduleSiteRela> getScheduleSiteRelaList(long scheduleId);
	
	/**
	 * Query with no cache
	 * 
	 * @param scheduleId
	 * @return
	 */
	List<ScheduleSiteRela> getScheduleSiteRelaListWithNoCache(List<Long> scheduleIdList);
	
	/**
	 * Query by saleSiteId and poStartTime.
	 * 
	 * @param saleSiteId
	 * @param poStartTime
	 * @return
	 */
	List<ScheduleSiteRela> getScheduleSiteRelaList(long saleSiteId, long poStartTime);
	
	/**
	 * @param saleSiteId
	 * @param poId
	 * @return
	 */
	int getScheduleShowOrder(long saleSiteId, long poId);
	
	/**
	 * Get all from DB.
	 * 
	 * @return
	 */
	List<ScheduleSiteRela> getAllScheduleSiteRelaList();
}
