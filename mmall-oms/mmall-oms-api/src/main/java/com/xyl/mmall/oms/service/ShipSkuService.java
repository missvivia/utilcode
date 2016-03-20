/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.ShipSkuDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;

/**
 * ShipSkuService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-24
 * @since      1.0
 */
public interface ShipSkuService {
	/**
	 * 获取发货单sku详情
	 * 
	 * @author hzzengdan
	 * @date 2014-09-09
	 */
	public List<ShipSkuDTO> getShipSkuDetail(String shipId);
	
	/**
	 * 更新一个对象
	 * @param shipSkuDTO
	 * @return
	 */
	boolean updateShipSkuItem(ShipSkuDTO shipSkuDTO);
	
	/**
	 * 添加一个对象
	 * @param itemForm
	 * @return
	 */
	ShipSkuItemForm addShipSkuItem(ShipSkuItemForm itemForm);
	
	/**
	 * 通过poOrderId获取shipSku列表
	 * @param poOrderId
	 * @return
	 */
	public List<ShipSkuItemForm> getShipSkuListByPoOrderId(String poOrderId);

	
	/**
	 * 通过发货单获取发货总数
	 * @param shipOrderId
	 * @return
	 */
	public int getTotalCountByShipOrderId(String shipOrderId,long supplierId);
	
	/**
	 * 通过发货单获取sku种类
	 * @param shipOrderId
	 * @return
	 */
	public int getTotalSkuTypeByShipOrderId(String shipOrderId,long supplierId);
	
	/**
	 * 获取发货单对应的入库单
	 * @param shipOrderId
	 * @return
	 */
	public WMSShipOrderDTO getWmsShipOrderDTOByShipOrderId(String shipOrderId,long supplierId);
	
	/**
	 * 报错发货单列表
	 * @param list
	 * @param shipOrderId
	 * @param jitFlagType
	 * @return
	 */
	public boolean saveShipSkuList(List<ShipSkuDTO> list, String shipOrderId, JITFlagType jitFlagType);
	
	
	/**
	 * @param poOrderId
	 * @param supplierId
	 * @return
	 */
	public List<ShipSkuItemForm> getShipSkuListByPoOrderId(long poOrderId,long supplierId);
	
	/**
	 * @param shipOrderId
	 * @return
	 */
	public List<ShipSkuItemForm> getShipSkuList(String shipOrderId);
	
}
