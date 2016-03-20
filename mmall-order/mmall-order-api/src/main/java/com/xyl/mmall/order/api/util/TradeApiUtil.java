package com.xyl.mmall.order.api.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.TradeItem;

/**
 * 交易的Api
 * 
 * @author dingmingliang
 * 
 */
public class TradeApiUtil {

	/**
	 * 筛选出 在线支付+未付款 的交易
	 * 
	 * @param tradeColl
	 * @return
	 */
	public static TradeItem getTradeOfOnlineAndUnpay(Collection<? extends TradeItem> tradeColl) {
		// 有效的现金交易付款状态
		PayState[] payStateArray = new PayState[] { PayState.ONLINE_NOT_PAY };
		// 返回结果
		Map<PayState, List<TradeItem>> resultMap = getTradeOfOnline(tradeColl, payStateArray);
		return CollectionUtil.getFirstObjectOfCollection(CollectionUtil.getValueOfMap(resultMap, payStateArray[0]));
	}

	/**
	 * 筛选出 在线支付+已付款 的交易
	 * 
	 * @param tradeColl
	 * @return
	 */
	public static TradeItem getTradeOfOnlineAndPayed(Collection<? extends TradeItem> tradeColl) {
		// 有效的现金交易付款状态
		PayState[] payStateArray = new PayState[] { PayState.ONLINE_PAYED };
		// 返回结果
		Map<PayState, List<TradeItem>> resultMap = getTradeOfOnline(tradeColl, payStateArray);
		return CollectionUtil.getFirstObjectOfCollection(CollectionUtil.getValueOfMap(resultMap, payStateArray[0]));
	}

	/**
	 * 筛选出 在线支付+(部分or全部)已退款 的交易
	 * 
	 * @param tradeColl
	 * @return
	 */
	public static TradeItem getTradeOfOnlineAndRefund(Collection<? extends TradeItem> tradeColl) {
		// 有效的现金交易付款状态
		PayState[] payStateArray = new PayState[] { PayState.ONLINE_REFUNDED };
		// 返回结果
		Map<PayState, List<TradeItem>> resultMap = getTradeOfOnline(tradeColl, payStateArray);
		return CollectionUtil.getFirstObjectOfCollection(CollectionUtil.getValueOfMap(resultMap, payStateArray[0]));
	}

	/**
	 * 筛选出 货到付款+未付款 的交易
	 * 
	 * @param tradeColl
	 * @return
	 */
	public static TradeItem getTradeOfCODAndUnpay(Collection<? extends TradeItem> tradeColl) {
		// 有效的现金交易付款状态
		PayState[] payStateArray = new PayState[] { PayState.COD_NOT_PAY };
		// 返回结果
		Map<PayState, List<TradeItem>> resultMap = getTradeOfCOD(tradeColl, payStateArray);
		return CollectionUtil.getFirstObjectOfCollection(CollectionUtil.getValueOfMap(resultMap, payStateArray[0]));
	}

	/**
	 * 筛选出 0元交易+已付款 的交易
	 * 
	 * @param tradeColl
	 * @return
	 */
	public static TradeItem getTradeOfZeroPayed(Collection<? extends TradeItem> tradeColl) {
		// 有效的现金交易付款状态
		PayState[] payStateArray = new PayState[] { PayState.ZERO_PAYED };
		// 有效的Zero交易支付方式
		TradeItemPayMethod[] payMethodArray = new TradeItemPayMethod[] { TradeItemPayMethod.ZERO };
		Map<PayState, List<TradeItem>> resultMap = getTradeMap(tradeColl, payMethodArray, payStateArray);
		return CollectionUtil.getFirstObjectOfCollection(CollectionUtil.getValueOfMap(resultMap, payStateArray[0]));
	}

	/**
	 * 筛选出 在线支付 的交易
	 * 
	 * @param tradeColl
	 * @param payStateArray
	 * @return
	 */
	private static Map<PayState, List<TradeItem>> getTradeOfOnline(Collection<? extends TradeItem> tradeColl,
			PayState[] payStateArray) {
		// 有效的Online交易支付方式
		TradeItemPayMethod[] payMethodArray = TradeItemPayMethod.getOnlineArray();
		return getTradeMap(tradeColl, payMethodArray, payStateArray);
	}

	/**
	 * 筛选出 COD 的交易
	 * 
	 * @param tradeColl
	 * @param payStateArray
	 * @return
	 */
	public static Map<PayState, List<TradeItem>> getTradeOfCOD(Collection<? extends TradeItem> tradeColl,
			PayState[] payStateArray) {
		// 有效的COD交易支付方式
		TradeItemPayMethod[] payMethodArray = new TradeItemPayMethod[] { TradeItemPayMethod.COD };
		return getTradeMap(tradeColl, payMethodArray, payStateArray);
	}

	/**
	 * 筛选出 符合条件 的交易
	 * 
	 * @param tradeColl
	 * @param payMethodArray
	 * @param payStateArray
	 * @return
	 */
	private static Map<PayState, List<TradeItem>> getTradeMap(Collection<? extends TradeItem> tradeColl,
			TradeItemPayMethod[] payMethodArray, PayState[] payStateArray) {
		if (CollectionUtil.isEmptyOfCollection(tradeColl))
			return null;

		// 筛选交易
		Map<PayState, List<TradeItem>> resultMap = new LinkedHashMap<>();
		for (TradeItem tradeItem : tradeColl) {
			TradeItemPayMethod payMethod = tradeItem.getTradeItemPayMethod();
			PayState payState = tradeItem.getPayState();
			if (CollectionUtil.isInArray(payMethodArray, payMethod)
					&& CollectionUtil.isInArray(payStateArray, payState)) {
				CollectionUtil.putValueOfListMap(resultMap, payState, tradeItem, false);
			}
		}
		return resultMap;
	}
	
	
	/**
	 * 筛选出 在线支付+未付款 的交易  
	 * 
	 * @param tradeColl
	 * @return
	 */
	public static List<TradeItem> getTradeListOfOnlineAndUnpay(Collection<? extends TradeItem> tradeColl) {
		// 有效的现金交易付款状态
		PayState[] payStateArray = new PayState[] { PayState.ONLINE_NOT_PAY };
		// 返回结果
		Map<PayState, List<TradeItem>> resultMap = getTradeOfOnline(tradeColl, payStateArray);
		return CollectionUtil.getValueOfMap(resultMap, payStateArray[0]);
	}
	
	/**
	 * 筛选出 在线支付+已付款 的交易
	 * 
	 * @param tradeColl
	 * @return
	 */
	public static List<TradeItem> getTradeListOfOnlineAndPayed(Collection<? extends TradeItem> tradeColl) {
		// 有效的现金交易付款状态
		PayState[] payStateArray = new PayState[] { PayState.ONLINE_PAYED };
		// 返回结果
		Map<PayState, List<TradeItem>> resultMap = getTradeOfOnline(tradeColl, payStateArray);
		return CollectionUtil.getValueOfMap(resultMap, payStateArray[0]);
	}
	
	/**
	 * 筛选出 0元交易+已付款 的交易
	 * 
	 * @param tradeColl
	 * @return
	 */
	public static List<TradeItem> getTradeListOfZeroPayed(Collection<? extends TradeItem> tradeColl) {
		// 有效的现金交易付款状态
		PayState[] payStateArray = new PayState[] { PayState.ONLINE_PAYED };
		// 有效的Zero交易支付方式
		TradeItemPayMethod[] payMethodArray = new TradeItemPayMethod[] { TradeItemPayMethod.ZERO };
		Map<PayState, List<TradeItem>> resultMap = getTradeMap(tradeColl, payMethodArray, payStateArray);
		return CollectionUtil.getValueOfMap(resultMap, payStateArray[0]);
	}

}
