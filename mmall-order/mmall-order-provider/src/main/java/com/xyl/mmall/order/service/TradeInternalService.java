package com.xyl.mmall.order.service;

import java.math.BigDecimal;

import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.param.OrderOperateParam;

/**
 * @author dingmingliang
 * 
 */
public interface TradeInternalService {

	/**
	 * 1.更新交易状态为支付成功(网易宝专用)
	 * 
	 * @param tradeId
	 * @param userId
	 * @param orderSn
	 * @param payOrderSn
	 * @return -2: 支付方式不正确<br>
	 *         -1: 交易不存在<br>
	 *         0: 失败<br>
	 *         1: 成功<br>
	 *         2: 成功(重复通知)
	 */
	public int setTradeSuccessInEPay(long tradeId, long userId, String orderSn, String payOrderSn);

	/**
	 * 取消交易(普通服务使用)<br>
	 * 1.关闭/退款 在线支付交易<br>
	 * 2.关闭 COD 交易
	 * 
	 * @param orderId
	 * @param userId
	 * @param rtype
	 *            退款方式
	 * @return
	 */
	public boolean cancelTrade(long orderId, long userId, OrderCancelRType rtype);

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
