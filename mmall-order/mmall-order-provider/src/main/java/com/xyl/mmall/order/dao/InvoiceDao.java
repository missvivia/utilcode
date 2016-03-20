package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.order.meta.Invoice;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年6月5日下午1:25:20
 */
public interface InvoiceDao extends AbstractDao<Invoice>{

	/**
	 * 更新发票状态
	 * @param obj
	 * @return
	 */
	public boolean updateState(Invoice obj);
	
	/**
	 * 根据订单Id查发票
	 * @param orderId
	 * @return
	 */
	public List<Invoice> queryInvoiceByOrderId(long orderId);
}
