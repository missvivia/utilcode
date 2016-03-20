package com.xyl.mmall.common.facade.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.common.facade.TradeFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.service.TradeService;

/**
 * @author dingmingliang
 * 
 */
@Facade
public class TradeFacadeImpl implements TradeFacade {

	@Autowired
	private TradeService tradeService;

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.TradeFacade#getTradeItemDTO(long,
	 *      long)
	 */
	public TradeItemDTO getTradeItemDTO(long tradeId, long userId) {
		return tradeService.getTradeItemDTO(tradeId, userId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.TradeFacade#getTradeItemDTOList(long,
	 *      long)
	 */
	public List<TradeItemDTO> getTradeItemDTOList(long orderId, long userId) {
		return tradeService.getTradeItemDTOList(orderId, userId);
	}
	
	public List<TradeItemDTO> getTradeItemDTOListByParentId(long parentId, long userId) {
        return tradeService.getTradeItemDTOListByParentId(parentId, userId);
    }

}
