package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.enums.InvoiceInOrdState;
import com.xyl.mmall.order.meta.InvoiceInOrd;

/**
 * @author dingmingliang
 * 
 */
public interface InvoiceInOrdDao extends AbstractDao<InvoiceInOrd> {

	/**
	 * 更新发票状态
	 * 
	 * @param obj
	 * @return
	 */
	public boolean updateState(InvoiceInOrd obj);

	/**
	 * 
	 * @param userId
	 * @param orderIdColl
	 * @return
	 */
	public List<InvoiceInOrd> getListByOrderIdsAndUserId(long userId, Collection<Long> orderIdColl);

	/**
	 * 获取发票记录
	 * 
	 * @param minOrderId
	 * @param state
	 * @param orderTimeRange
	 * @param param
	 * @return
	 */
	public List<InvoiceInOrd> getInvoiceInOrdByOrderTimeRangeWithMinOrderId(long minOrderId, InvoiceInOrdState state,
			long[] orderTimeRange, DDBParam param);
}