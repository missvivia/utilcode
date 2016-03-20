/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.ShipOrderDTO;
import com.xyl.mmall.oms.enums.ShipStateType;
import com.xyl.mmall.oms.meta.ShipOrderForm;

/**
 * ShipOrderService.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-24
 * @since 1.0
 */
public interface ShipOrderService {

	/**
	 * 获取发货单
	 * 
	 * @author hzzengdan
	 * @date 2014-09-09
	 */
	public ShipOrderDTO getShipOrder(String shipId, long supplierId);

	/**
	 * 根据拣货单获取发货单
	 * 
	 * @param pickOrderId
	 * @return
	 */
	public ShipOrderForm getShipOrderByPickOrderId(String pickOrderId, long supplierId);

	/**
	 * 提交一个发货单
	 * 
	 * @param shipOrderId
	 * @return
	 */
	public boolean pushShipOrderToWarehose(String shipOrderId, long supplierId);

	/**
	 * @param createTime
	 * @param state
	 * @param limit
	 * @return
	 */
	public List<ShipOrderForm> getListByCreateTime(long createTime, ShipStateType state, int limit);

	/**
	 * 根据supplierId获取发货单
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<ShipOrderForm> getShipOrderBySupplierId(long supplierId);

	/**
	 * 取消超时未确认的入库单
	 */
	public void cancelTimeOutShipOrder();

	/**
	 * @param startCollectTime
	 * @param endCollectTime
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
