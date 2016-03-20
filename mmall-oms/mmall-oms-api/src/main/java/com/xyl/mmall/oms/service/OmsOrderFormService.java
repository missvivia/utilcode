package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderUpdateDTO;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.meta.OmsOrderForm;

/**
 * @author zb
 *
 */
public interface OmsOrderFormService {
	public long getCancelSkuCountInPoId(long poId);

	/**
	 * 通知仓库发货
	 * 
	 * @param omsOrderForm
	 * @return
	 */
	public boolean send(OmsOrderForm omsOrderForm);

	/**
	 * 取消一个订单，0取消失败，1取消成功可回收内存，2取消成功不能回收内存。3已成功取消
	 * 
	 * @param userOrderFormId
	 * @param userId
	 * @return
	 */
	public int cancelOrderForm(long userOrderFormId, long userId);

	/**
	 * 0其它情况(订单处于非1非2状态)，1未生成拣货单取消(取消成功可回收内存)，2已生成拣货单取消(取消成功不能回收内存)
	 * 
	 * @param userOrderFormId
	 * @param userId
	 * @return
	 */
	public int canCancelOrder(long userOrderFormId, long userId);

	/**
	 * 销售订单状态更新
	 * 
	 * @param wmsSalesOrderUpdateDTO
	 * @return
	 */
	public boolean salesOrderUpdate(WMSSalesOrderUpdateDTO wmsSalesOrderUpdateDTO);

	/**
	 * 将未同步的sku信息推送给仓库
	 */
	public void pushSkuToWarehose();

	/**
	 * 更新订单状态
	 * 
	 * @param orderId
	 * @param state
	 * @return
	 */
	public boolean updateOmsOrderFormState(long orderId, OmsOrderFormState state);

	/**
	 * 查询poId的sku总数
	 * 
	 * @param poId
	 * @return
	 */
	public int getTotalSoldByPoId(long poId);

	/**
	 * @param minOrderId
	 * @param omsOrderFormState
	 * @param limit
	 * @return
	 */
	public List<OmsOrderForm> getOmsOrderFormListByStateWithMinOrderId(long minOrderId,
			OmsOrderFormState omsOrderFormState, int limit);

	/**
	 * @param minOrderId
	 * @param omsOrderFormStateArray
	 * @param orderTimeRange
	 * @param limit
	 * @return
	 */
	public List<OmsOrderForm> getOmsOrderFormListByStateWithMinOrderId(long minOrderId,
			OmsOrderFormState[] omsOrderFormStateArray, long[] orderTimeRange, int limit);

	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<OmsOrderForm> getOmsOrderFormListByTimeRange(long startTime, long endTime);

	/**
	 * 
	 * @param omsOrderId
	 * @return
	 */
	OmsOrderForm getOmsOrderFormByOrderId(long omsOrderId);

	/**
	 * @param omsOrderForm
	 * @return
	 */
	public boolean sendCancelCommandToWarehouse(OmsOrderForm omsOrderForm);

	/**
	 * @param userOrderFormId
	 * @param userId
	 * @return
	 */
	public List<OmsOrderForm> getOmsOrderFormByUserOrderFormId(long userOrderFormId, long userId);

}
