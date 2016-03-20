/**
 * 
 */
package com.xyl.mmall.oms.warehouse;

import java.util.Map;

/**
 * @author hzzengchengyuan
 * 
 */
public interface WarehouseAdapterBridge extends WarehouseAdapter {

	Object doHttpWarehouseCaller(String warehouseCode, Map<String, Object> params);
}
