/**
 * 
 */
package com.xyl.mmall.oms.util;

import com.xyl.mmall.oms.enums.WarehouseType;

/**
 * 一些需要依赖其他模块服务api的功能暂时用util代替，以防有些逻辑代码遗漏，后续以其他模块Service API代替
 * 
 * @author hzzengchengyuan
 *
 */
public class CommonServiceUtil {
	/**
	 * 根据仓库id获取仓库类别
	 * @param poId
	 * @return
	 */
	public static WarehouseType getWarehouseTypeByPoId(long warehouseId) {
		return WarehouseType.EMS;
	}
	
}
