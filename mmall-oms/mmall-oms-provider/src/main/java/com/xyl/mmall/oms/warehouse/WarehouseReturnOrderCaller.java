/**
 * 
 */
package com.xyl.mmall.oms.warehouse;

import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderUpdateDTO;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * @author hzzengchengyuan
 *
 */
public interface WarehouseReturnOrderCaller {
	/**
	 * 退货单状态更新回调方法
	 * 
	 * @param returnOrder
	 * @return
	 * @throws WarehouseCallerException
	 */
	boolean onReturnOrderStateChange(WMSReturnOrderUpdateDTO returnOrder) throws WarehouseCallerException;
}
