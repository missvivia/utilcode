/**
 * 
 */
package com.xyl.mmall.oms.warehouse;

import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderUpdateDTO;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * @author hzzengchengyuan
 *
 */
public interface WarehouseShipOutCaller {

	/**
	 * 供应商商品出仓单状态更新回调方法
	 * 
	 * @return
	 * @throws WarehouseCallerException
	 */
	boolean onShipOutOrderStateChange(WMSShipOutOrderUpdateDTO shipOutOrder) throws WarehouseCallerException;
}
