package com.xyl.mmall.common.facade;

import java.util.List;

import com.xyl.mmall.order.dto.TradeItemDTO;

/**
 * @author dingmingliang
 * 
 */
public interface TradeFacade {

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
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<TradeItemDTO> getTradeItemDTOList(long orderId, long userId);
	
    /**
     * 根据userid和parentid获取交易信息列表
     * 
     * @param parentId
     * @param userId
     * @return
     */
    public List<TradeItemDTO> getTradeItemDTOListByParentId(long parentId, long userId);
    
    

}
