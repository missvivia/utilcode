package com.xyl.mmall.order.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.service.TradeInternalProxyService;
import com.xyl.mmall.order.service.TradeInternalService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
@Service("tradeInternalProxyService")
public class TradeInternalProxyServiceImpl implements TradeInternalProxyService {

	@Autowired
	protected TradeInternalService tradeInternalService;
	
	@Override
	public boolean setOnlineTradeToRefundWithTransaction(long packageId, long orderId, long userId, 
			BigDecimal cash, OrderCancelRType rtype) {
		return tradeInternalService.setOnlineTradeToRefundWithTransaction(packageId, orderId, userId, cash, rtype);
	}
	
	@Override
	public int modifyTradeCash(OrderOperateParam param) {
		return tradeInternalService.modifyTradeCash(param);
	}

}
