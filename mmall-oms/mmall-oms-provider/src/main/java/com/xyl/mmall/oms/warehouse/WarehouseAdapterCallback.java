/**
 * 
 */
package com.xyl.mmall.oms.warehouse;

import java.util.Map;

import com.xyl.mmall.oms.enums.WarehouseType;

/**
 * 目标仓库系统(如：ems仓储系统、顺丰仓储系统、百世物流仓储系统等)在一些业务功能上的回调函数。 如：ems仓储系统通过该回调及时通知OMS订单实时状态
 * 
 * @author hzzengchengyuan
 */
public interface WarehouseAdapterCallback {

	/**
	 * 处理目标仓储系统的回调请求。处理完成后将结果通过{@link WarehouseCaller}通知到OMS
	 * 
	 * @param params
	 * @return TODO
	 */
	Object onCallback(Map<String, Object> params);

	/**
	 * 返回仓储系统类型
	 * 
	 * @return
	 */
	WarehouseType getWarehouseType();
}
