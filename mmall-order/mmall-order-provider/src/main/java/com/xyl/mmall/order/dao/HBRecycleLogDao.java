package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.meta.HBRecycleLog;

public interface HBRecycleLogDao extends AbstractDao<HBRecycleLog> {

	/**
	 * 找出退款成功、红包未回收的退货包裹记录
	 * 
	 * @param minRetPkgId
	 * @param ddbParam
	 * @return
	 */
	List<HBRecycleLog> getReturnedButNotRecycledObjects(long minRetPkgId, DDBParam ddbParam);
	
	/**
	 * 更新状态：退款成功、红包未回收的退货包裹记录
	 * 
	 * @param hbRecycleLog
	 * @return
	 */
	boolean recycleHb(HBRecycleLog hbRecycleLog);
}
