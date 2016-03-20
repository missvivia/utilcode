package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.order.meta.OrderPackageRefund;

/**
 * @author dingmingliang
 *
 */
public interface OrderPackageRefundDao extends AbstractDao<OrderPackageRefund> {

	/**
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<OrderPackageRefund> getListByOrderIdAndUserId(long orderId, long userId);
}
