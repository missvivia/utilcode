/**
 * 
 */
package com.xyl.mmall.oms.warehouse;

import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderUpdateDTO;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * @author hzzengchengyuan
 *
 */
public interface WarehouseSalesOrderCaller {

	/**
	 * 发货单状态更新回调方法。处理失败后返回false或抛出异常
	 * 
	 * @param salesOrder
	 * @return
	 */
	boolean onSalesOrderStateChange(WMSSalesOrderUpdateDTO salesOrder) throws WarehouseCallerException;
}
