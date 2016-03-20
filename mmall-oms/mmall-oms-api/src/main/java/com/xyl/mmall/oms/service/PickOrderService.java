/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.PickOrderDTO;
import com.xyl.mmall.oms.meta.PickOrderForm;

/**
 * PickOrderService.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-24
 * @since 1.0
 */
public interface PickOrderService {

	/**
	 * 通过拣货单ID获取单条拣货单信息
	 */
	public PickOrderDTO getPickOrderByPkId(String pickOrderId, long supplierId);

	/**
	 * 更新pickorder
	 * 
	 * @param pickOrderDTO
	 * @return
	 */
	boolean updatePickOrder(PickOrderDTO pickOrderDTO);

	/**
	 * 针对一个商家生成拣货单和发货单
	 * 
	 * @param supplierId
	 * @param deadLine
	 * @return
	 */
	public List<PickOrderForm> createPickOrderFormAndShipOrder(long supplierId, long deadLine);

	/**
	 * 根据商家id获取拣货单列表
	 * 
	 * @param supplierId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<PickOrderDTO> getPickListBySupplierIdAndTime(long supplierId, long startTime, long endTime);

}
