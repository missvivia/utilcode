/**
 * 
 */
package com.xyl.mmall.oms.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.dto.PageableList;
import com.xyl.mmall.oms.dto.PoRetrunOrderQueryParamDTO;
import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.meta.ReturnPoOrderForm;

/**
 * @author hzzengchengyuan
 *
 */
public interface ReturnPoOrderFormDao extends AbstractDao<ReturnPoOrderForm> {
	
	/**
	 * 根据组合条件统计
	 * @param params
	 * @return
	 */
	long countByParams(PoRetrunOrderQueryParamDTO params);
	
	/**
	 * @param returnOrderId
	 * @param supplierId
	 * @return
	 */
	ReturnPoOrderForm getByIdAndSupplierId(long returnOrderId, long supplierId);

	/**
	 * @param params
	 * @return
	 */
	PageableList<ReturnPoOrderForm> querReturn(PoRetrunOrderQueryParamDTO params);
	
	/**
	 * @param orderId
	 * @param state
	 * @param oldState
	 * @return
	 */
	boolean updateState(long orderId, PoReturnOrderState state, PoReturnOrderState oldState);
	
	/**
	 * 更新退供单物流信息和状态，where condition: poReturnOrder.poReturnOrderId && oldState
	 * @param oldState
	 * @param poReturnOrder
	 * @return
	 */
	boolean update(PoReturnOrderState oldState, ReturnPoOrderForm poReturnOrder);
}
