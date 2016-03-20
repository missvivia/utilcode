package com.xyl.mmall.saleschedule.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.meta.ScheduleChannel;

/**
 * Schedule操作DAO
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleChannelDao extends AbstractDao<ScheduleChannel> {

	/**
	 * 新增
	 * 
	 * @param channel
	 *            待新增的频道
	 * @return 新增成功返回true。否则返回false。新增成功后入参channel带有id。
	 */
	boolean saveScheduleChannel(ScheduleChannel chl);

	/**
	 * 更新
	 * 
	 * @param channel
	 *            带有id的待更新频道
	 * @return 更新成功返回true。否则返回false
	 */
	boolean updateScheduleChannel(ScheduleChannel chl);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return 删除成功返回true，否则返回false。
	 */
	boolean deleteScheduleChannelById(long id);

	/**
	 * 查询
	 * 
	 * @param id
	 * @return 如果数据库有数据返回channel对象，否则返回null
	 */
	ScheduleChannel getScheduleChannelById(long id);

	/**
	 * 查询所有channel列表
	 * 
	 * @return 返回所有channel列表
	 */
	List<ScheduleChannel> getScheduleChannelList();
}
