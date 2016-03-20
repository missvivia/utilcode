package com.xyl.mmall.order.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.aop.OperateLog;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.framework.util.HttpUtil;
import com.xyl.mmall.order.api.util.TradeApiUtil;
import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.dao.OrderOperateLogDao;
import com.xyl.mmall.order.dao.TradeItemDao;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.OperateLogType;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.TradeItem;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.service.TradeInternalService;
import com.xyl.mmall.order.util.OrderInstantiationUtil;

/**
 * @author dingmingliang
 * 
 */
@Service
public class TradeInternalServiceImpl implements TradeInternalService {

	@Autowired
	private TradeItemDao tradeItemDao;
	
	@Autowired
	private OrderInstantiationUtil orderInstantiationUtil;
	
	@Autowired
	private OrderOperateLogDao orderOperateLogDao;
	
	@Autowired
	private OrderFormDao orderForm;

	/**
	 * 支付的Host
	 */
	@Value("${pay.host}")
	private String hostOfPay = "pay.xiupin.163.com";

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.TradeService#setTradeSuccessInEPay(long,
	 *      long, java.lang.String, java.lang.String)
	 */
	public int setTradeSuccessInEPay(long tradeId, long userId, String orderSn, String payOrderSn) {
		// 1.读取交易数据
		TradeItemDTO tradeDTO = getTradeItemDTO(tradeId, userId);
		if (tradeDTO == null) {
			logger.info("tradeDTO == null, tradeId=" + tradeId);
			return -1;
		}
		// 判断是否重复通知
		TradeItemPayMethod pm = tradeDTO.getTradeItemPayMethod();
		boolean isOnlinePayMethod = TradeItemPayMethod.isOnlinePayMethod(pm);
		if (isOnlinePayMethod && tradeDTO.getPayState() == PayState.ONLINE_PAYED) {
			return 2;
		}
		// 判断支付方式是否正确
		if (!isOnlinePayMethod || tradeDTO.getPayState() == PayState.ONLINE_CHANGE) {
			logger.info("tradeDTO param invalid, tradeId=" + tradeId);
			return -2;
		}

		// 2.更新交易状态(网易宝交易)
		boolean isSucc = true;
		tradeDTO.setOrderSn(payOrderSn);
		tradeDTO.setPayOrderSn(payOrderSn);
		tradeDTO.setPayState(PayState.ONLINE_PAYED);
		tradeDTO.setPayTime(System.currentTimeMillis());
		isSucc = isSucc && tradeItemDao.setTradeSuccessInEPay(tradeDTO, new PayState[] { PayState.ONLINE_NOT_PAY });

		// 返回结果
		return isSucc ? 1 : 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.TradeService#getTradeItemDTO(long,
	 *      long)
	 */
	public TradeItemDTO getTradeItemDTO(long tradeId, long userId) {
		TradeItem trade = new TradeItem();
		trade.setUserId(userId);
		trade.setTradeId(tradeId);
		trade = tradeItemDao.getObjectByPrimaryKeyAndPolicyKey(trade);
		if (trade == null)
			return null;

		TradeItemDTO tradeDTO = new TradeItemDTO(trade);
		return tradeDTO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.TradeInternalService#cancelTrade(long,
	 *      long, com.xyl.mmall.order.enums.OrderCancelRType)
	 */
	@Transaction
	public boolean cancelTrade(long orderId, long userId, OrderCancelRType rtype) {
		// 1.读取在线支付交易
		List<TradeItem> tradeColl = tradeItemDao.getListByOrderId(orderId, userId);

		boolean isSucc = true;
		// 2.调用在线支付交易的关闭接口,并设置交易状态为关闭
		TradeItem tradeOfOnlineAndUnpay = TradeApiUtil.getTradeOfOnlineAndUnpay(tradeColl);
		isSucc = isSucc && (tradeOfOnlineAndUnpay == null || setOnlineTradeToClose(tradeOfOnlineAndUnpay));
		// 3.调用在线支付交易的取消接口,并设置交易状态为退款
		long packageId = 0L;
		TradeItem tradeOfOnlineAndPayed = TradeApiUtil.getTradeOfOnlineAndPayed(tradeColl);
		isSucc = isSucc
				&& (tradeOfOnlineAndPayed == null || setOnlineTradeToRefund(tradeOfOnlineAndPayed, rtype, packageId));
		// 4.关闭COD交易
		TradeItem tradeOfCODAndUnpay = TradeApiUtil.getTradeOfCODAndUnpay(tradeColl);
		isSucc = isSucc && (tradeOfCODAndUnpay == null || setCODTradeToClose(tradeOfCODAndUnpay));
		// 5.设置0元交易为退款
//		TradeItem tradeOfZero = TradeApiUtil.getTradeOfZeroPayed(tradeColl);
//		isSucc = isSucc && (tradeOfZero == null || setZeroTradeToRefund(tradeOfZero));

		// 返回结果
		if (!isSucc)
			throw new ServiceNoThrowException("orderId=" + orderId);
		return isSucc;
	}

			
	/**
	 * 关闭0元交易
	 * 
	 * @param trade
	 * @return
	 */
	private boolean setZeroTradeToRefund(TradeItem trade) {
		if (trade == null)
			return false;

		boolean isSucc = true;
		// 1.设置交易状态为退款交易
		trade.setPayState(PayState.ZERO_REFUNDED);
		isSucc = isSucc && tradeItemDao.updatePayState(trade, new PayState[] { PayState.ZERO_PAYED });

		return isSucc;
	}

	/**
	 * 设置在线支付的交易为关闭
	 * 
	 * @param trade
	 * @return
	 */
	private boolean setOnlineTradeToClose(TradeItem trade) {
		if (trade == null)
			return false;

		boolean isSucc = true;
		// 1.设置交易状态为关闭交易
		trade.setPayState(PayState.ONLINE_CLOSE);
		isSucc = isSucc && tradeItemDao.updatePayState(trade, new PayState[] { PayState.ONLINE_NOT_PAY });
		// 2.发送关闭交易的请求
//		isSucc = isSucc && sendRequestForOnlineTradeToClose(trade);

		return isSucc;
	}

	/**
	 * 向支付系统,发送关闭在线交易的请求
	 * 
	 * @param trade
	 * @return
	 */
	private boolean sendRequestForOnlineTradeToClose(TradeItem trade) {
		if (trade == null)
			return false;

		StringBuilder url = new StringBuilder(256);
		url.append(hostOfPay);
		url.append("/closetrade/async?1=1");
		url.append("&tradeSerialId=").append(trade.getTradeId());
		url.append("&sp=").append(trade.getSpSource().getIntValue());
		url.append("&paymethod=").append(trade.getTradeItemPayMethod().getIntValue());

		String returnValue = HttpUtil.getContent(url.toString());
		try {
			int state = Integer.parseInt(returnValue);
			return state == 100;
		} catch (NumberFormatException e) {

		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.TradeInternalService#setOnlineTradeToRefundWithTransaction(long,
	 *      long, long, java.math.BigDecimal,
	 *      com.xyl.mmall.order.enums.OrderCancelRType)
	 */
	@Transaction
	public boolean setOnlineTradeToRefundWithTransaction(long packageId, long orderId, long userId, BigDecimal cash,
			OrderCancelRType rtype) {
		boolean isSucc = true;
		// 1.读取在线支付交易
		List<TradeItem> tradeColl = tradeItemDao.getListByOrderId(orderId, userId);
		// 2.调用在线支付交易的取消接口,并设置交易状态为退款
		TradeItem tradeOfOnlineAndPayed = TradeApiUtil.getTradeOfOnlineAndPayed(tradeColl);
		TradeItem tradeOfOnlineAndRefund = TradeApiUtil.getTradeOfOnlineAndRefund(tradeColl);
		TradeItem trade = tradeOfOnlineAndPayed != null ? tradeOfOnlineAndPayed : tradeOfOnlineAndRefund;
		isSucc = isSucc && (trade == null || setOnlineTradeToRefund(trade, rtype, packageId, cash));
		// 3.返回
		if (!isSucc)
			throw new ServiceNoThrowException("orderId=" + orderId);
		return isSucc;
	}

	/**
	 * 设置在线支付的交易为退款
	 * 
	 * @param trade
	 * @param rtype
	 * @param packageId
	 * @return
	 */
	private boolean setOnlineTradeToRefund(TradeItem trade, OrderCancelRType rtype, long packageId) {
		return setOnlineTradeToRefund(trade, rtype, packageId, null);
	}

	/**
	 * 设置在线支付的交易为退款
	 * 
	 * @param trade
	 * @param rtype
	 * @param packageId
	 * @return
	 */
	private boolean setOnlineTradeToRefund(TradeItem trade, OrderCancelRType rtype, long packageId, BigDecimal cash) {
		if (trade == null)
			return false;

		boolean isSucc = true;
		cash = cash == null ? trade.getCash() : cash;
		// 1.设置交易状态为退款交易
		if (trade.getPayState() != PayState.ONLINE_REFUNDED) {
			trade.setPayState(PayState.ONLINE_REFUNDED);
			isSucc = isSucc && tradeItemDao.updatePayState(trade, new PayState[] { PayState.ONLINE_PAYED });
		}
		// 2.发送退款交易的请求
//		isSucc = isSucc && sendRequestForOnlineTradeToRefund(trade, rtype, packageId, cash);

		return isSucc;
	}

	/**
	 * 向支付系统,发送退款在线交易的请求
	 * 
	 * @param trade
	 * @param rtype
	 * @param packageId
	 * @param cash
	 * @return
	 */
	private boolean sendRequestForOnlineTradeToRefund(TradeItem trade, OrderCancelRType rtype, long packageId,
			BigDecimal cash) {
		if (trade == null)
			return false;

		StringBuilder url = new StringBuilder(256);
		url.append(hostOfPay);
		url.append("/pay/refundtrade?1=1");
		url.append("&tradeSerialId=").append(trade.getTradeId());
		url.append("&sp=").append(trade.getSpSource().getIntValue());
		url.append("&paymethod=").append(trade.getTradeItemPayMethod().getIntValue());
		url.append("&tradeId=").append(trade.getTradeId());
		url.append("&userId=").append(trade.getUserId());
		url.append("&cash=").append(cash);
		url.append("&ctime=").append(trade.getCtime());
		url.append("&packageId=").append(packageId);
		url.append("&original=").append(rtype == OrderCancelRType.ORI ? true : false);

		String returnValue = HttpUtil.getContent(url.toString());
		try {
			int state = Integer.parseInt(returnValue);
			return state == 100;
		} catch (NumberFormatException e) {
			logger.error(url.toString() + "  ,returnValue=" + returnValue);
		}
		return false;
	}

	/**
	 * 关闭COD的交易
	 * 
	 * @param trade
	 * @return
	 */
	private boolean setCODTradeToClose(TradeItem trade) {
		if (trade == null)
			return false;

		boolean isSucc = true;
		// 1.设置交易状态为关闭交易
		trade.setPayState(PayState.COD_CLOSE);
		isSucc = isSucc && tradeItemDao.updatePayState(trade, new PayState[] { PayState.COD_NOT_PAY });
		return isSucc;
	}

	@Override
	@Transaction
	@OperateLog(clientType="web")
	public int modifyTradeCash(OrderOperateParam param) {
		// 1.读取订单锁
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(param.getOrderId(), param.getUserId());
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" +param.getOrderId() + " ,userId=" + param.getUserId());
			return -1;
		}
		OrderForm orderFrom = RetArgUtil.get(retArgOfTmp, OrderForm.class);
		List<TradeItem> tradeColl = tradeItemDao.getListByOrderId(param.getOrderId(), param.getUserId());
		TradeItem tradeOfUnpay = null;
		boolean isPayOnline = true;
		//货到付款
		if(orderFrom.getOrderFormPayMethod().equals(OrderFormPayMethod.COD)||orderFrom.getOrderFormPayMethod().equals(OrderFormPayMethod.POS)){
			// 读取货到付款交易
			 tradeOfUnpay = TradeApiUtil.getTradeOfCODAndUnpay(tradeColl);
			 isPayOnline = false;
		}else{
			// 读取在线支付交易
			tradeOfUnpay = TradeApiUtil.getTradeOfOnlineAndUnpay(tradeColl);
		}
		if(param.getOperateUserType().equals(OperateUserType.BACKERNDER)){
			if(orderFrom.getBusinessId()!=param.getBusinessId()){
				logger.error("have no permission to modified cash");
				return -2;
			}
			//在线付款订单状态不是等待付款不允许修改
			if(isPayOnline&&!orderFrom.getOrderFormState().equals(OrderFormState.WAITING_PAY)){
				return -3;
			}
			//货到付款订单状态不是待发货不允许修改
			if(!isPayOnline&&!orderFrom.getOrderFormState().equals(OrderFormState.WAITING_DELIVE)){
				return -3;			
			}
		}
		tradeOfUnpay.setCash(param.getTotalCash());
		tradeItemDao.updateObjectByKey(tradeOfUnpay);
		//修改订单金额
		orderFrom.setCartRPrice(param.getTotalCash());
		orderForm.updateObjectByKey(orderFrom);
		param.setOperateLogType(OperateLogType.ORDER_CASH);//OperateLogAspect记录日志区分
		return 1;
	}
}
