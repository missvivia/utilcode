package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.enums.CancelOmsOrderTaskState;
import com.xyl.mmall.order.meta.CancelOmsOrderTask;

/**
 * @author dingmingliang
 * 
 */
public interface CancelOmsOrderTaskDao extends AbstractDao<CancelOmsOrderTask> {

	/**
	 * 查询某个状态的CancelOmsOrderTask列表
	 * 
	 * @param minOrderId
	 * @param taskStateArray
	 * @param param
	 * @return
	 */
	public List<CancelOmsOrderTask> queryCancelOmsOrderTaskListWithMinOrderId(long minOrderId,
			CancelOmsOrderTaskState[] taskStateArray, DDBParam param);

	/**
	 * 查询某个CancelOmsOrderTask记录
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public CancelOmsOrderTask queryCancelOmsOrderTask(long orderId, long userId);

	/**
	 * 更新CancelOmsOrderTask.state字段
	 * 
	 * @param cancelTask
	 * @param oldState
	 * @return
	 */
	public boolean updateCancelOmsOrderTaskState(CancelOmsOrderTask cancelTask, CancelOmsOrderTaskState oldState);
}
