package com.xyl.mmall.task.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.task.dto.PushManagementDTO;
import com.xyl.mmall.task.meta.PushManagement;

/**
 * 
 * @author jiangww
 *
 */
public interface PushManagementDao extends AbstractDao<PushManagement> {
	
	/**
	 * 根据条件筛选
	 * 
	 * @param pushManagementAo
	 * @return
	 */
	public List<PushManagement> getPushConfigList(PushManagementDTO pushManagementAo,DDBParam param);
	
	/**
	 * 获得指定时间段执行的任务
	 * 
	 * @param pushManagementAo
	 * @return
	 */
	public List<PushManagement> getPushTaskList(long startTime,long endTime);
	

	/**
	 * 更新
	 * 
	 * @param userId
	 * @param pushManagementDTO
	 * @return
	 */
	public boolean updatePushManagement(long id, PushManagementDTO pushManagementDTO);
	
	/**
	 * 更新
	 * 
	 * @param userId
	 * @param pushManagementDTO
	 * @return
	 */
	public boolean updatePushSuccess(long id, int success,String error);
}
