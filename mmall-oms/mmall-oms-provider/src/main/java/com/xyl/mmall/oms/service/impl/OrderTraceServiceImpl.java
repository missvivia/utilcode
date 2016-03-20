/**
 * 
 */
package com.xyl.mmall.oms.service.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.oms.dao.OrderTraceDao;
import com.xyl.mmall.oms.dto.warehouse.WMSOrderTrace;
import com.xyl.mmall.oms.meta.OrderTrace;
import com.xyl.mmall.oms.service.OrderTraceService;
import com.xyl.mmall.oms.warehouse.WarehouseOrderTraceCaller;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;
import com.xyl.mmall.oms.warehouse.util.DateUtils;

/**
 * @author hzzengchengyuan
 *
 */
@Service("orderTraceService")
public class OrderTraceServiceImpl implements OrderTraceService, WarehouseOrderTraceCaller {
	@Autowired
	private OrderTraceDao orderLogisticsTraceDao;

	public void trace(OrderTrace trace) {
		if (!orderLogisticsTraceDao.exist(trace)) {
			orderLogisticsTraceDao.addObject(trace);
		}
	}

	@Override
	public OrderTrace[] getTrace(String expressCompany, String expressNO) {
		List<OrderTrace> traces = orderLogisticsTraceDao.getTrace(expressCompany, expressNO);
		if (traces != null && traces.size() > 0) {
			OrderTrace[] results = new OrderTrace[traces.size()];
			traces.toArray(results);
			Arrays.sort(results, new Comparator<OrderTrace>() {
				@Override
				public int compare(OrderTrace o1, OrderTrace o2) {
					return (int) (DateUtils.parseToLongtime(o1.getTime()) - DateUtils.parseToLongtime(o2.getTime()));
				}
			});
			return results;
		} else {
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.warehouse.WarehouseOrderTraceCaller#onOrderTraceChange(java.util.List)
	 */
	@Override
	@Transaction
	public boolean onOrderTraceChange(List<WMSOrderTrace> orderTraces) throws WarehouseCallerException {
		if (orderTraces != null) {
			for (WMSOrderTrace wmsTrace : orderTraces) {
				OrderTrace trace = new OrderTrace();
				trace.setExpressCompany(wmsTrace.getLogisticCompany());
				trace.setExpressNO(wmsTrace.getLogisticNo());
				trace.setTime(wmsTrace.getTimestamp());
				trace.setOperater(wmsTrace.getOperater());
				trace.setOperaterPhone(wmsTrace.getOperaterPhone());
				trace.setOperate(wmsTrace.getOperate());
				trace.setOperateDesc(wmsTrace.getOperateDesc());
				trace.setChildOperate(wmsTrace.getChildOperate());
				trace.setChildOperateDesc(wmsTrace.getChildOperateDesc());
				trace.setOperateOrg(wmsTrace.getOperateOrg());
				trace.setNote(wmsTrace.getDesc());
				trace(trace);
			}
		}
		return true;
	}

}
