package com.xyl.mmall.order.service;

import java.math.BigDecimal;

import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.param.OrderOperateParam;

/**
 * @author dingmingliang
 * 
 */
public interface TradeInternalProxyService {

	/**
	 * 设置在线支付的交易为退款
	 * 
	 * @param packageId
	 *            0: 所有包裹都退
	 * @param orderId
	 * @param userId
	 * @param cash
	 *            null: 在线支付交易全额退款
	 * @param rtype
	 * @return
	 */
	public boolean setOnlineTradeToRefundWithTransaction(long packageId, long orderId, long userId, BigDecimal cash,
			OrderCancelRType rtype);

	
	/**
	 * 修改交易金额
	 * @param param
	 * @return
	 */
	public int modifyTradeCash(OrderOperateParam param);
	
	
}
