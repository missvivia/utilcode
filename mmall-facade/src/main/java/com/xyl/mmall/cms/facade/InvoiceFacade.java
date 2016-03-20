package com.xyl.mmall.cms.facade;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;

/**
 * @author dingmingliang
 * 
 */
public interface InvoiceFacade {

	/**
	 * 查询商家的订单发票信息
	 * 
	 * @param supplierId
	 * @param orderTimeRange
	 * @param state
	 * @param param
	 * @return RetArg.ArrayList: List(InvoiceInOrdSupplierDTO)<br>
	 *         RetArg.DDBParam
	 */
	public RetArg getInvoiceInOrdSupplierByTimeRangeAndState(long supplierId, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param);

	/**
	 * 根据发票的抬头查询
	 * 
	 * @param supplierId
	 * @param title
	 * @param orderTimeRange
	 * @param state
	 * @param param
	 * @return RetArg.ArrayList: List(InvoiceInOrdSupplierDTO)<br>
	 *         RetArg.DDBParam
	 */
	public RetArg getInvoiceInOrdSupplierByTitle(long supplierId, String title, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param);

	/**
	 * 查询供应商订单发票信息
	 * 
	 * @param supplierId
	 * @param orderId
	 * @param state
	 * @return
	 */
	public List<InvoiceInOrdSupplierDTO> getInvoiceInOrdSupplierByOrderId(long supplierId, long orderId,
			InvoiceInOrdSupplierState state);

	/**
	 * 更新发票的快递信息+开票状态(已开票)
	 * 
	 * @param obj
	 * @return
	 */
	public boolean updateExpInfoAndState(InvoiceInOrdSupplierDTO obj);
}
