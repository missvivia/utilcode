package com.xyl.mmall.order.service;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.TradeItemPayMethod;

/**
 * @author dingmingliang
 * 
 */
public interface TradeService {

	/**
	 * 查询交易
	 * 
	 * @param tradeId
	 * @param userId
	 * @return
	 */
	public TradeItemDTO getTradeItemDTO(long tradeId, long userId);

	/**
	 * 查询交易
	 * 
	 * @param tradeId
	 * @param userId
	 * @return
	 */
	public TradeItemDTO getTradeItemDTO(long tradeId);

	/**
	 * 查询交易
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<TradeItemDTO> getTradeItemDTOList(long orderId, long userId);
	
    /**
     * 根据userid和parentid查询交易信息
     * 
     * @param parentId
     * @param userId
     * @return
     */
    public List<TradeItemDTO> getTradeItemDTOListByParentId(long parentId, long userId);

	/**
	 * 查询符合条件的交易数据
	 * 
	 * @param tradeId
	 * @param payMethod
	 * @param payStateArray
	 * @param payTimeRange
	 * @param param
	 * @return RetArg.ArrayList: List(TradeItemDTO)<br>
	 *         RetArg.DDBParam
	 */
	public RetArg getTradeItemDTOListWithMinTradeId(long tradeId, TradeItemPayMethod payMethod,
			PayState[] payStateArray, long[] payTimeRange, DDBParam param);
	
	/**
	 * 获取在线支付的trade
	 * @param orderId
	 * @param userId
	 * @return
	 */
	TradeItemDTO getOnlinePayTradeItemDTO(long orderId, long userId);

}
