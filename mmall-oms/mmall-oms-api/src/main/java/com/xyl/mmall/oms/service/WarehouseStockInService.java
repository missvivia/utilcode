package com.xyl.mmall.oms.service;

import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO;

/**
 * 
 * @author hzliujie 2014年10月22日 下午3:46:54
 */
public interface WarehouseStockInService {

	/**
	 * 仓库确认回调
	 * 
	 * @param wMSShipOrderUpdateDTO
	 * @return
	 */
	public boolean warehouseStockInConfirm(WMSShipOrderUpdateDTO wMSShipOrderUpdateDTO);
}
