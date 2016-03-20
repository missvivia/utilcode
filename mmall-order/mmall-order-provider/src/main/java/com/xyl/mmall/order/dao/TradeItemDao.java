package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.TradeItem;

/**
 * @author dingmingliang
 * 
 */
public interface TradeItemDao extends AbstractDao<TradeItem> {

	/**
	 * 根据OrderId+UserId,读取相关的交易记录
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<TradeItem> getListByOrderId(long orderId, long userId);
	
	/**
	 * 根据parentid和userid查询交易信息列表
	 * @param parentId
	 * @param userId
	 * @return
	 */
	public List<TradeItem> getListByParentId(long parentId, long userId);

	/**
	 * @param tradeId
	 * @param payMethod
	 * @param payStateArray
	 * @param payTimeRange
	 * @param param
	 * @return
	 */
	public List<TradeItem> getTradeItemDTOListWithMinTradeId(long tradeId, TradeItemPayMethod payMethod,
			PayState[] payStateArray, long[] payTimeRange, DDBParam param);

	/**
	 * 更新交易的支付状态
	 * 
	 * @param tradeItem
	 * @param oldPayStateArray
	 * @return
	 */
	public boolean updatePayState(TradeItem tradeItem, PayState[] oldPayStateArray);

	/**
	 * 更新网易宝交易的支付状态为成功
	 * 
	 * @param tradeItem
	 *            更新payState,orderSn,payOrderSn,payTime
	 * @param oldPayStateArray
	 * @return
	 */
	public boolean setTradeSuccessInEPay(TradeItem tradeItem, PayState[] oldPayStateArray);
}
