/**
 * 
 */
package com.xyl.mmall.oms.warehouse;

import java.util.List;

import com.xyl.mmall.oms.dto.warehouse.WMSOrderTrace;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * @author hzzengchengyuan
 *
 */
public interface WarehouseOrderTraceCaller {
	/**
	 * 物流运单轨迹变化更新，所有的订单轨迹处理成功返回true，否则返回false
	 * 
	 * @param orderTrace
	 * @return
	 * @throws WarehouseCallerException
	 */
	boolean onOrderTraceChange(List<WMSOrderTrace> orderTraces) throws WarehouseCallerException;
}
