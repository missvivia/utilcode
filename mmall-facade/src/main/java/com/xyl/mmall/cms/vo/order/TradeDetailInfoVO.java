package com.xyl.mmall.cms.vo.order;

import java.util.List;

import com.xyl.mmall.order.dto.TradeItemDTO;

/**
 * CMS订单详情：交易详情
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月10日 上午10:11:20
 *
 */
public class TradeDetailInfoVO {
	
	private TradeBasicInfo trade = new TradeBasicInfo();
	
	public TradeDetailInfoVO fillWihtInfos(TradeItemDTO tradeItem, 
			List<TradeItemDTO> allTradesInSameOrder, String userName
			) {
		trade.fillTrade(userName, tradeItem, allTradesInSameOrder);
		return this;
	}
	
	public TradeBasicInfo getTrade() {
		return trade;
	}

	public void setTrade(TradeBasicInfo trade) {
		this.trade = trade;
	}

}
