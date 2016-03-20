/**
 * 
 */
package com.xyl.mmall.oms.warehouse;

import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * @author hzzengchengyuan
 *
 */
public interface WarehouseShipCaller {
	
	/**
	 * 入库单状态更新回调方法。处理失败后返回false或抛出异常
	 * @param shipOrder
	 * @return
	 */
	boolean onShipOrderStateChange(WMSShipOrderUpdateDTO shipOrder) throws WarehouseCallerException;
}
