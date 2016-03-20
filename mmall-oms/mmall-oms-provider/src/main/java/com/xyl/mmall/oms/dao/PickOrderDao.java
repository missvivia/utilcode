package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.dto.PickOrderDTO;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.meta.PickOrderForm;

/**
 * @author zb
 *
 */
public interface PickOrderDao extends AbstractDao<PickOrderForm> {

	/**
	 * 获取指定的捡货单
	 * 
	 * @param pickOrderId
	 * @return
	 */
	PickOrderForm getPickOrder(String pickOrderId,long supplierId);

	/**
	 * 更新捡货单
	 * @param pickOrderDTO
	 * @return
	 */
	boolean updatePickOrder(PickOrderDTO pickOrderDTO);

	/**
	 * 更新拣货状态
	 * 
	 * @param pickOrderId
	 * @param type
	 * @return
	 */
	public boolean updatePickSkuState(String pickOrderId, PickStateType type);

	/**
	 * 根据商家id获取拣货单列表
	 * 
	 * @param supplierId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<PickOrderForm> getPickListBySupplierIdAndTime(long supplierId, long startTime, long endTime);
}
