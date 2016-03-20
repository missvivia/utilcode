/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.order.meta.OrderOperateLog;

/**
 * OrderOperateLogDao.java created by yydx811 at 2015年6月11日 下午12:08:13
 * 订单操作日志dao
 *
 * @author yydx811
 */
public interface OrderOperateLogDao extends AbstractDao<OrderOperateLog> {

	/**
	 * 查询订单操作日志
	 * @param operateLog
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<OrderOperateLog> queryOperateLog(OrderOperateLog operateLog, String startTime, String endTime);
}
