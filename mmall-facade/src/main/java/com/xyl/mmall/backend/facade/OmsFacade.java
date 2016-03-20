/**
 * 
 */
package com.xyl.mmall.backend.facade;

import java.util.Map;

/**
 * @author hzzengchengyuan
 *
 */
public interface OmsFacade {
	
	/**
	 * 处理基于HTTP的仓储系统(EMS,SF)回调（主动通知）
	 * 
	 * @param warehouseCode
	 * @param params
	 * @return
	 */
	Object httpWarehouseCaller(String warehouseCode, Map<String, Object> params);
	
}
