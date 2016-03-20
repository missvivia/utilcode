package com.xyl.mmall.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.meta.JobHistory;

public interface JobHistoryDao extends AbstractDao<JobHistory>{
	
	/**
	 * 根据每次产生的job唯一值查找
	 * @param jobUniqueId
	 * @return
	 */
	public JobHistory queryByJobUniqueId(String jobUniqueId);
	
	/**
	 * 更新job历史记录
	 * @param id
	 * @param processStatusUpdate
	 * @param costTime job运行花费时间
	 * @param errorDesc job错误描述
	 * @return
	 */
	public boolean updateStatusByJobUniqueId(String jobUniqueId,int processStatusUpdate,long costTime,String errorDesc);
	
}
