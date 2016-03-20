/**
 * 
 */
package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.OrderTrace;

/**
 * @author hzzengchengyuan
 *
 */
public interface OrderTraceDao extends AbstractDao<OrderTrace> {
	/**
	 * 判断轨迹是否存在，唯一性判断条件：expressCompany+expressNO+operate+time
	 * @param trace
	 * @return
	 */
	public boolean exist(OrderTrace trace);
	
	/**
	 * 根据快递公司和快递单号查询快递物流信息
	 * @param expressCompany
	 * @param expressNO
	 * @return
	 */
	public List<OrderTrace> getTrace(String expressCompany, String expressNO);
}
