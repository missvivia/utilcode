package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.enums.OrderPackageRefundTaskState;
import com.xyl.mmall.order.meta.OrderPackageRefundTask;

/**
 * @author dingmingliang
 * 
 */
public interface OrderPackageRefundTaskDao extends AbstractDao<OrderPackageRefundTask> {

	/**
	 * 更新包裹退款任务记录的retryFlag字段(附带retryCount++)
	 * 
	 * @param obj
	 * @param retryFlagOfOld
	 * @return
	 */
	public boolean updateRetryFlag(OrderPackageRefundTask obj, long retryFlagOfOld);
	
	
	/**
	 * 读取包裹退款任务记录
	 * 
	 * @param minId
	 * @param state
	 * @param param
	 * @return
	 */
	public List<OrderPackageRefundTask> getListByStateWithMinId(long minId, OrderPackageRefundTaskState state,
			DDBParam param);
}
