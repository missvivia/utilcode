/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.OrderFormOMSDTO;
import com.xyl.mmall.oms.dto.PoOrderDTO;
import com.xyl.mmall.oms.dto.PoSkuDetailCountDTO;
import com.xyl.mmall.oms.meta.PoOrderForm;

/**
 * PoOrderService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-24
 * @since      1.0
 */
public interface PoOrderService {
	/**
	 * 获取po单
	 * 
	 * @author hzzengdan
	 * @date 2014-09-03
	 */
	public List<PoOrderDTO> getPoOrdersList(String poOrderId, String createSTime, String startETime, String openSTIME,
			String openEtime, long supplierId);
	
	
	/**
	 * 获取PO详情页
	 * @param poOrderId
	 * @return
	 */
	public PoSkuDetailCountDTO getPoOrderDetail(String poOrderId);
	
	/**
	 * 根据多个poOrderId获取po
	 * @param poOrderId
	 * @param createStartTime
	 * @param createEndTime
	 * @param openSaleStartTime
	 * @param openSaleEndTime
	 * @param stopSaleStartTime
	 * @param stopSaleEndTime
	 * @return
	 */
	List<PoOrderForm> getPickOrderListByMultiplePoOrder(String[] poOrderId, String createStartTime,
			String createEndTime, String openSaleStartTime, String openSaleEndTime, long supplierId);
	
	/**
	 * 获取poOrderBean
	 * @param poOrderId
	 * @return
	 */
	public PoOrderForm getPoOrderById(String poOrderId);
	
	
	/**
	 * 从web系统接收一个订单，按照poid分拣
	 * @param omsOrderForm 订单对象
	 * @param toCreatePoOrderFormList 需要初始化的poorder列表
	 * @return
	 */
	public boolean savePoOrderForm(OrderFormOMSDTO omsOrderForm,List<PoOrderForm> toCreatePoOrderFormList);
	
	/**
	 * 获取po结束后还未生成二退数据的po列表<br>
	 * command&?=? and endTime+incTime>compareTime
	 * @param incTime
	 * @param compareTime
	 * @return
	 */
	public List<PoOrderForm> getNotGen2ReturnAfterPoEnd(long incTime,long compareTime);
	
	/**
	 * 
	 * @return
	 */
	public boolean addPoOrderCommand(long poOrderId, long command);
	
	public boolean addPoOrderGen2ReturnCommand(long poOrderId);
}
