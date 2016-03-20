package com.xyl.mmall.order.dao.tcc;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.framework.interfaces.TCCDaoInterface;
import com.xyl.mmall.order.meta.tcc.OrderTCCLock;

/**
 * 订单服务-TCC事务锁
 * 
 * @author dingmingliang
 * 
 */
public interface OrderTCCLockDao extends AbstractDao<OrderTCCLock>, TCCDaoInterface<OrderTCCLock> {

	/**
	 * 根据OrderId获得OrderTCCLock(读取任意一条)
	 * 
	 * @param orderId
	 * @return
	 */
	public OrderTCCLock getObjectByOrderId(long orderId);
}
