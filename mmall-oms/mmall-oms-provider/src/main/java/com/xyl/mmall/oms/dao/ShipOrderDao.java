/**
 */
package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.enums.ShipStateType;
import com.xyl.mmall.oms.meta.ShipOrderForm;

/**
 */
public interface ShipOrderDao extends AbstractDao<ShipOrderForm> {

	/**
	 * 更新仓库反馈信息
	 * 
	 * @param shipOrder
	 * @return
	 */
	public boolean updateWarehoureInfo(ShipOrderForm shipOrder);

	/**
	 * 根据发货单ID获取单条发货单
	 */
	public ShipOrderForm getShipOrderByShipId(String shipOrderId, long supplierId);

	/**
	 * 根据发货单ID获取单条发货单
	 */
	public ShipOrderForm getShipOrderByShipId(String shipOrderId);

	/**
	 * 根据拣货单列表获取发货单
	 * 
	 * @param pickOrderId
	 * @return
	 */
	public ShipOrderForm getShipOrderByPickOrderId(String pickOrderId, long supplierId);

	/**
	 * @param createTime
	 * @param state
	 * @param limit
	 * @return
	 */
	public List<ShipOrderForm> getListByCreateTime(long createTime, ShipStateType state, int limit);

	/**
	 * 更新状态
	 * 
	 * @param shipOrderId
	 * @param shipState
	 * @return
	 */
	public boolean updateShipStateType(String shipOrderId, ShipStateType shipState);

	/**
	 * 根据supplierId获取发货单
	 * 
	 * @param supplierId
	 * @return
	 */
	public List<ShipOrderForm> getListBySupplierId(long supplierId);

	/**
	 * @param minOrderId
	 * @param omsOrderFormStateArray
	 * @param orderTimeRange
	 * @param limit
	 * @return
	 */
	public List<ShipOrderForm> getShipOrderFormListByStateWithMinOrderId(String minOrderId,
			ShipStateType[] shipStateTypeArray, long[] createTimeRange, int limit);

	/**
	 * @param startCollectTime
	 * @param endCollectTime
	 * @param offset 
	 * @param limit 
	 * @return
	 */
	public List<ShipOrderForm> getListByCollectTime(long startCollectTime, long endCollectTime, int limit, int offset);
	
	
	/**
	 * @param supplierId
	 * @param startCollectTime
	 * @param endCollectTime
	 * @return
	 */
	public List<ShipOrderForm> getListByCollectTime(long supplierId, long startCollectTime, long endCollectTime);

}
