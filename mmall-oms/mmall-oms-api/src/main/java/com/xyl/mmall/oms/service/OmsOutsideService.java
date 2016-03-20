/**
 * 
 */
package com.xyl.mmall.oms.service;

import java.util.Map;

/**
 * @author hzzengchengyuan
 *
 */
public interface OmsOutsideService {
	/**
	 * 处理基于HTTP的仓储系统(EMS,SF)回调（主动通知）
	 * @return TODO
	 * 
	 * @return
	 */
	Object httpWarehouseCaller(String warehouseCode, Map<String, Object> params);
	
}
