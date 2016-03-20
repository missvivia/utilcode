package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.order.meta.OrderLogistics;

/**
 * 订单物流Dao
 * @author author:lhp
 *
 * @version date:2015年6月1日上午10:39:24
 */
public interface OrderLogisticsDao extends AbstractDao<OrderLogistics> {
	
	public List<OrderLogistics> getOrderLogisticsByOrderIds(long orderId);

}
