/**
 * 
 */
package com.xyl.mmall.oms.warehouse;

import com.xyl.mmall.oms.dto.warehouse.WMSResponseDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;

/**
 * 抽象和wms主动对接的接口，接口按订单类型主要抽象为四类：入库单（商家商品入库）、出库单（商家商品出库）、销售订单（用户订单出库）、退货单（用户退货）
 * 
 * @author hzzengchengyuan
 * 
 */
public interface WarehouseAdapter {
	/**
	 * 根据供应商发货单配送入库(商家商品入库)，是否真正入库成功异步回调确认.
	 * 
	 * @param shipOrder
	 * @return
	 */
	WMSResponseDTO ship(WMSShipOrderDTO shipOrder);
	
	/**
	 * 取消入库单
	 * @param shipOrder
	 * @return
	 */
	WMSResponseDTO cancelShip(WMSShipOrderDTO shipOrder);

	/**
	 * 供应商商品出库单（商家商品出仓）
	 * 
	 * @param shipOrder
	 * @return
	 */
	WMSResponseDTO shipOut(WMSShipOutOrderDTO shipOrder);

	/**
	 * 用户销售订单发货
	 * 
	 * @param userOrder
	 * @return
	 */
	WMSResponseDTO send(WMSSalesOrderDTO userOrder);

	/**
	 * 取消用户销售订单发货
	 * 
	 * @param userOrder
	 * @return
	 */
	WMSResponseDTO cancelSend(WMSSalesOrderDTO userOrder);

	/**
	 * 用户订单退货申请
	 * 
	 * @param userOrder
	 * @return
	 */
	WMSResponseDTO returnSales(WMSReturnOrderDTO order);

	/**
	 * 同步商品信息到wms，如果商品信息不存在则创建，否则则更新之
	 * @param sku
	 * @return
	 */
	WMSResponseDTO syncSku(WMSSkuDetailDTO sku);

}
