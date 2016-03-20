package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;
import com.xyl.mmall.order.meta.InvoiceInOrdSupplier;

/**
 * @author dingmingliang
 * 
 */
public interface InvoiceInOrdSupplierDao extends AbstractDao<InvoiceInOrdSupplier> {

	/**
	 * 根据订单Id和userId查询
	 * 
	 * @param orderId
	 * @param userId
	 * @param param
	 * @return
	 */
	public List<InvoiceInOrdSupplier> queryInvoiceByOrderIdAndUserId(long orderId, long userId, DDBParam param);

	/**
	 * 根据订单Id查询
	 * 
	 * @param orderId
	 * @return
	 */
	public List<InvoiceInOrdSupplier> queryInvoiceByOrderIdAndUserId(long orderId);

	/**
	 * 更新快递信息+state
	 * 
	 * @param obj
	 * @return
	 */
	public boolean updateExpInfoAndState(InvoiceInOrdSupplier obj);

	/**
	 * 统计数量
	 * 
	 * @param supplierId
	 * @param state
	 * @return
	 */
	public int getCount(long supplierId, InvoiceInOrdSupplierState state);

	/**
	 * 查询商家的订单发票信息
	 * 
	 * @param supplierId
	 * @param orderTimeRange
	 * @param state
	 * @return
	 */
	public List<InvoiceInOrdSupplier> getListByOrderTimeRangeAndState(long supplierId, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param);

	/**
	 * 根据发票的抬头查询
	 * 
	 * @param supplierId
	 * @param title
	 * @param orderTimeRange
	 * @param state
	 * @param param
	 * @return
	 */
	public List<InvoiceInOrdSupplier> getListByTitle(long supplierId, String title, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param);
}
