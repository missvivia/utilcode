/**
 * 
 */
package com.xyl.mmall.oms.warehouse;


/**
 * 抽象wms回调接口，和WarehouseAdapter一样，接口按订单类型主要抽象为四类。
 * 
 * @author hzzengchengyuan
 * 
 */
public interface WarehouseCaller extends WarehouseShipCaller, WarehouseShipOutCaller, WarehouseSalesOrderCaller,
		WarehouseReturnOrderCaller, WarehouseOrderTraceCaller {
	
}
