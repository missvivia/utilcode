package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.meta.OmsOrderForm;

/**
 * @author zb
 *
 */
public interface OmsOrderFormDao extends AbstractDao<OmsOrderForm> {
	/**
	 * 更新订单状态
	 * 
	 * @param omsOrderFormId
	 * @param oldOmsOrderFormState
	 * @param newOmsOrderFormState
	 * @return
	 */
	public boolean updateOrderFormState(long omsOrderFormId, OmsOrderFormState oldOmsOrderFormState,
			OmsOrderFormState newOmsOrderFormState);

	/**
	 * 更新订单状态
	 * 
	 * @param omsOrderFormId
	 * @param newOmsOrderFormState
	 * @return
	 */
	public boolean updateOrderFormState(long omsOrderFormId, OmsOrderFormState newOmsOrderFormState);

	/**
	 * 更新一个订单的发货信息
	 * 
	 * @param omsOrderFormId
	 * @param currentTimeMillis
	 * @param ship
	 * @return
	 */
	public boolean updateShipTimeAndState(long omsOrderFormId, long currentTimeMillis, OmsOrderFormState ship);

	/**
	 * @param userOrderFormId
	 * @param userId
	 * @return
	 */
	public List<OmsOrderForm> getListByUserOrderFormId(long userOrderFormId, long userId);

	/**
	 * @param userOrderFormId
	 * @param userId
	 * @return
	 */
	public List<OmsOrderForm> getListByUserOrderFormId(long userOrderFormId);

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
	 * 计算产生订单单量（已经汇总的订单加上取消的订单）
	 * @param beginTime
	 * @param endTime
	 * @param warehouseIdList
	 * @return
	 */
	public int getTotalOrderCountByTime(long beginTime, long endTime, List<Long> warehouseIdList);

}
