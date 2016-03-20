/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.PickSkuDTO;
import com.xyl.mmall.oms.meta.PickSkuItemForm;

/**
 * PickSkuService.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-24
 * @since 1.0
 */
public interface PickSkuService {
	/**
	 * 获取拣货单sku详情
	 * 
	 * @author hzzengdan
	 * @date 2014-09-09
	 */
	public List<PickSkuDTO> getPickSkuDetail(String pickId, long supplierId);

	/**
	 * 通过poOrderId获取sku列表
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<PickSkuItemForm> getPickSkuListByPoOrder(String poOrderId, String pickOrderId, String pickStartTime,
			String pickEndTime);

	/**
	 * 通过poOrderId获取sku列表
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<PickSkuItemForm> getPickSkuByPoOrderId(String poOrderId, long supplierId);

	/**
	 * 通过poOrderId获取未拣货sku列表
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<PickSkuItemForm> getUnPickSkuByPoOrderId(String poOrderId);

	/**
	 * @param createTime
	 * @param limit
	 * @return
	 */
	public List<PickSkuItemForm> getUnPickListByCreateTime(long createTime, int limit);
	
	
	/**
	 * @param poOrderId
	 * @param supplierId
	 * @return
	 */
	public List<PickSkuItemForm> getPickSkuByPoOrderIdAndOriSupplierId(String poOrderId, long supplierId);
}
