package com.xyl.mmall.order.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.param.TryAddOrderByTCCParam;
import com.xyl.mmall.order.result.TryAddOrderByTCCResult;

/**
 * 订单服务中采用TCC模型的功能<br>
 * 下单+取消订单
 * 
 * @author dingmingliang
 * 
 */
public interface OrderTCCService {

	/**
	 * 添加订单(TCC模型)-try步骤<br>
	 * 1.修改SkuOrderStock<br>
	 * 2.添加OrderFormTCC,TradeItemTCC,SkuOrderStockTCC<br>
	 * 3.添加OrderPackage,OrderExpInfo,OrderCartItem,OrderSkuCartItem,OrderSku<br>
	 * 
	 * @param tranId
	 *            TCC事务Id
	 * @param param
	 *            以TCC模型,向DB添加订单数据需要的参数
	 * @return
	 */
	//public TryAddOrderByTCCResult tryAddOrderByTCC(long tranId, TryAddOrderByTCCParam param);
	public TryAddOrderByTCCResult tryAddOrderByTCC(long tranId, Map<Long, TryAddOrderByTCCParam> paramMap);

	/**
	 * 添加订单(TCC模型)-confirm步骤<br>
	 * 1.OrderFormTCC->OrderForm,TradeItemTCC->TradeItem<br>
	 * 2.删除OrderFormTCC,TradeItemTCC,SkuOrderStockTCC<br>
	 * 3.向网易宝发起交易
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean confirmAddOrderByTCC(long tranId);

	/**
	 * 添加订单(TCC模型)-cancel步骤<br>
	 * 1.修改SkuOrderStock<br>
	 * 2.删除OrderFormTCC,TradeItemTCC,SkuOrderStockTCC
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean cancelAddOrderByTCC(long tranId);

	/**
	 * 取消订单(TCC模型)-try步骤<br>
	 * 1.更新订单状态:取消中<br>
	 * 2.添加订单状态UpdateOrderStateTCC,订单取消原因OrderCancelInfoTCC,销售数据SkuOrderStockTCC<br>
	 * 
	 * @param tranId
	 * @param orderCancelInfoDTO
	 * @param canRecyleStock
	 *            是否允许回收库存
	 * @return
	 */
	//public boolean tryCallOffOrderByTCC(long tranId, OrderCancelInfoDTO orderCancelInfoDTO, boolean canRecyleStock);
	public boolean tryCallOffOrderByTCC(long tranId, List<OrderCancelInfoDTO> orderCancelInfoDTOs, boolean canRecyleStock);

	/**
	 * 取消订单(TCC模型)-confirm步骤<br>
	 * 1.更新订单状态:已取消<br>
	 * 2.OrderCancelInfoTCC->OrderCancelInfo,SkuOrderStockTCC->SkuOrderStock<br>
	 * 3.删除UpdateOrderStateTCC,OrderCancelInfoTCC,SkuOrderStockTCC<br>
	 * 4.取消/关闭交易<br>
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean confirmCallOffOrderByTCC(long tranId);

	/**
	 * 取消订单(TCC模型)-cancel步骤<br>
	 * 1.更新订单状态:原始订单状态<br>
	 * 2.删除UpdateOrderStateTCC,OrderCancelInfoTCC,SkuOrderStockTCC<br>
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean cancelCallOffOrderByTCC(long tranId);
}
