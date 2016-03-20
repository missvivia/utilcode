/**
 * 
 */
package com.xyl.mmall.oms.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.service.OmsOutsideService;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge;

/**
 * @author hzzengdan
 *
 */
@Service
public class OmsOutsideServiceImpl implements OmsOutsideService {

	@Autowired
	private WarehouseAdapterBridge warehouseAdapterBridge;
	
	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.service.OmsOutsideService#httpWarehouseCaller(java.lang.String, java.util.Map, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Object httpWarehouseCaller(String warehouseCode, Map<String, Object> params) {
		return this.warehouseAdapterBridge.doHttpWarehouseCaller(warehouseCode, params);
	}

}
