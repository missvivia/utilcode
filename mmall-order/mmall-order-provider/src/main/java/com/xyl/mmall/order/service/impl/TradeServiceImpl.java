package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.dao.TradeItemDao;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.TradeItem;
import com.xyl.mmall.order.service.TradeService;

/**
 * @author dingmingliang
 * 
 */
@Service("tradeService")
public class TradeServiceImpl implements TradeService {

	@Autowired
	private TradeItemDao tradeItemDao;

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
	 * @see com.xyl.mmall.order.service.TradeService#getTradeItemDTO(long)
	 */
	public TradeItemDTO getTradeItemDTO(long tradeId) {
		TradeItem tradeItem = tradeItemDao.getObjectById(tradeId);
		if (tradeItem == null) {
			return null;
		}
		return new TradeItemDTO(tradeItem);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.TradeService#getTradeItemDTOList(long,
	 *      long)
	 */
	public List<TradeItemDTO> getTradeItemDTOList(long orderId, long userId) {
		List<TradeItem> itemList = tradeItemDao.getListByOrderId(orderId, userId);
		List<TradeItemDTO> itemDTOList = convertToTradeItemDTOList(itemList);
		return itemDTOList;
	}
	
    public List<TradeItemDTO> getTradeItemDTOListByParentId(long parentId, long userId)
    {
        List<TradeItem> itemList = tradeItemDao.getListByParentId(parentId, userId);
        List<TradeItemDTO> itemDTOList = convertToTradeItemDTOList(itemList);
        return itemDTOList;
    }

	/**
	 * @param objList
	 * @return
	 */
	private List<TradeItemDTO> convertToTradeItemDTOList(List<TradeItem> objList) {
		List<TradeItemDTO> dtoList = new ArrayList<TradeItemDTO>();
		if (CollectionUtil.isEmptyOfCollection(objList))
			return dtoList;
		for (TradeItem item : objList) {
			dtoList.add(new TradeItemDTO(item));
		}
		return dtoList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.TradeService#getTradeItemDTOListWithMinTradeId(long,
	 *      com.xyl.mmall.order.enums.TradeItemPayMethod,
	 *      com.xyl.mmall.framework.enums.PayState[], long[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg getTradeItemDTOListWithMinTradeId(long minTradeId, TradeItemPayMethod payMethod,
			PayState[] payStateArray, long[] payTimeRange, DDBParam param) {
		List<TradeItem> tradeList = tradeItemDao.getTradeItemDTOListWithMinTradeId(minTradeId, payMethod, payStateArray, payTimeRange, param);
		List<TradeItemDTO> tradeDTOList = convertToTradeItemDTOList(tradeList);
		
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, tradeDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	@Override
	public TradeItemDTO getOnlinePayTradeItemDTO(long orderId, long userId) {
		List<TradeItemDTO> dtos = getTradeItemDTOList(orderId, userId);
		if (CollectionUtils.isEmpty(dtos)) {
			return null;
		}
		
		for (TradeItemDTO dto : dtos) {
			if (TradeItemPayMethod.isOnlinePayMethod(dto.getTradeItemPayMethod())) {
				return dto;
			}
		}
		
		return null;
	}
}
