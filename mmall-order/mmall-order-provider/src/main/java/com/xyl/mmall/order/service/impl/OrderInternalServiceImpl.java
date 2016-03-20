package com.xyl.mmall.order.service.impl;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.param.OrderServiceSetStateToEPayedParam;
import com.xyl.mmall.order.service.TradeInternalService;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.order.util.OrderInstantiationUtil;

/**
 * @author dingmingliang
 * 
 */
@Service("orderInternalServiceImpl")
public class OrderInternalServiceImpl {

	@Autowired
	private OrderFormDao orderFormDao;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private TradeInternalService tradeInternalService;
	
	@Autowired
	private OrderInstantiationUtil orderInstantiationUtil;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 1.更新订单状态为支付成功<br>
	 * 2.更新交易状态为支付成功(网易宝交易)<br>
	 * 
	 * @param param
	 * @param retArg
	 *            -3: 获得订单锁失败<br>
	 *            -2: 没有网易宝交易<br>
	 *            -1: 参数错误<br>
	 *            0: 失败<br>
	 *            1: 成功<br>
	 *            2: 成功(重复更新)
	 */
	@Transaction
	public void setStateToEPayed(OrderServiceSetStateToEPayedParam param, RetArg retArg) {
		Integer retCode = null;
		boolean isSucc = true;
		for (Entry<Long, Long> entry : param.getOrderIdMap().entrySet()) {
			long tradeId = entry.getValue(), userId = param.getUserId(), orderId = entry.getKey(), currTime = System
					.currentTimeMillis();
			String orderSn = param.getOrderSn(), payOrderSn = param.getPayOrderSn();
			
			// 1.读取订单锁
			RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
			Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
			if (isContinue != Boolean.TRUE) {
				logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + orderId + " ,userId=" + userId);
				RetArgUtil.put(retArg, -3);
				return;
			}
			OrderForm order = RetArgUtil.get(retArgOfTmp, OrderForm.class);
			
			// 2.判断是否要执行更新操作
			if (order == null) {
				retCode = -1;
			} else if (!OrderFormPayMethod.isOnlinePayMethod(order.getOrderFormPayMethod())) {
				retCode = -2;
			} else if (order.getPayState() == PayState.ONLINE_PAYED
					&& order.getOrderFormState() != OrderFormState.WAITING_PAY) {
				retCode = 2;
			}
			if (retCode != null) {
				RetArgUtil.put(retArg, retCode);
				return;
			}
			
			// 3.更新交易状态
			if (isSucc) {
				int retCodeOfTradeUpdate = tradeInternalService.setTradeSuccessInEPay(tradeId, userId, orderSn, payOrderSn);
				isSucc = retCodeOfTradeUpdate > 0;
				if (!isSucc) {
					retCode = retCodeOfTradeUpdate;
				}
			}
			
			// 4.更新订单状态
			if (isSucc) {
				OrderFormState[] oldOrderFormStateArray = new OrderFormState[] { OrderFormState.WAITING_PAY };
				PayState[] oldPayStateArray = new PayState[] { PayState.ONLINE_NOT_PAY };
				order.setOrderFormState(OrderFormState.WAITING_DELIVE);
				order.setPayState(PayState.ONLINE_PAYED);
				order.setPayTime(currTime);
				isSucc = isSucc && orderFormDao.updateOrdStateAndPayState(order, oldOrderFormStateArray, oldPayStateArray);
				if (!isSucc) {
					retCode = 0;
				}
			}
		}

		// 返回结果
		retCode = retCode == null ? 1 : retCode;
		RetArgUtil.put(retArg, retCode);
		if (!isSucc)
			throw new ServiceNoThrowException("retCode=" + retCode + " , " + param.toString());
		return;
	}
}
