/**
 * 
 */
package com.xyl.mmall.oms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyl.mmall.oms.dto.warehouse.WMSOrderTrace;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderUpdateDTO;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.service.OmsOrderFormService;
import com.xyl.mmall.oms.service.OmsReturnOrderFormService;
import com.xyl.mmall.oms.service.PoReturnService;
import com.xyl.mmall.oms.service.RejectPackageService;
import com.xyl.mmall.oms.service.WarehouseStockInService;
import com.xyl.mmall.oms.warehouse.WarehouseCaller;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * 默认仓储系统回调
 * 
 * @author hzzengchengyuan
 * 
 */
@Component
public class DefaultWarehouseCaller implements WarehouseCaller {
	@Autowired
	private PoReturnService poReturnService;

	@Autowired
	private OmsOrderFormService omsOrderFormService;

	@Autowired
	private OmsReturnOrderFormService omsReturnOrderFormService;

	@Autowired
	private WarehouseStockInService warehouseStockInService;

	@Autowired
	private RejectPackageService rejectPackageService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseCaller#onShipOrderStateChange(com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO)
	 */
	@Override
	public boolean onShipOrderStateChange(WMSShipOrderUpdateDTO shipOrder) throws WarehouseCallerException {
		return warehouseStockInService.warehouseStockInConfirm(shipOrder);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseCaller#onShipOutOrderStateChange(com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderUpdateDTO)
	 */
	@Override
	public boolean onShipOutOrderStateChange(WMSShipOutOrderUpdateDTO shipOutOrder) throws WarehouseCallerException {
		return poReturnService.onWmsUpdateShipOutOrder(shipOutOrder);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseCaller#onSalesOrderStateChange(com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderUpdateDTO)
	 */
	@Override
	public boolean onSalesOrderStateChange(WMSSalesOrderUpdateDTO salesOrder) throws WarehouseCallerException {
		return omsOrderFormService.salesOrderUpdate(salesOrder);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseCaller#onReturnOrderStateChange(com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderUpdateDTO)
	 */
	@Override
	public boolean onReturnOrderStateChange(WMSReturnOrderUpdateDTO wmsReturnOrderUpdateDTO)
			throws WarehouseCallerException {
		if (wmsReturnOrderUpdateDTO.getOrderType() != WMSOrderType.R_UA) {
			return omsReturnOrderFormService.wmsReturnOrderUpdate(wmsReturnOrderUpdateDTO);
		} else {
			return true;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseCaller#onOrderTraceChange(com.xyl.mmall.oms.dto.warehouse.WMSOrderTrace)
	 */
	@Override
	public boolean onOrderTraceChange(List<WMSOrderTrace> orderTraces) throws WarehouseCallerException {
		return true;
	}

}
