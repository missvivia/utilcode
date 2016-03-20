package com.xyl.mmall.bi.service.impl;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogService;
import com.xyl.mmall.bi.util.BILogUtils;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
@Service("orderSubmitPageLogService")
public class OrderSubmitPageLogServiceImpl implements BILogService {

	@Autowired
	private OrderFacade orderFacade;

	private static Logger logger = LoggerFactory.getLogger(OrderSubmitPageLogServiceImpl.class);

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.bi.service.log.BILogService#logInfo(com.xyl.mmall.bi.meta.BasicLog,
	 *      java.util.Map, java.lang.String)
	 */
	@Override
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		if (!NumberUtils.isNumber(otherKey))
			return;

		Map<String, Object> infoMap = BILogUtils.getBasicLogMap(basicLog, logMap);

		Long userId = Long.valueOf(basicLog.getAccountId()), orderId = Long.valueOf(otherKey);
		Boolean isVisible = null;
		OrderFormDTO orderDTO = orderFacade.queryOrderForm(userId, orderId, isVisible);
		if (orderDTO == null)
			return;

		Set<Long> skuIdSet = orderDTO.mapOrderSkusBySkuId().keySet();
		StringBuilder skuIds = new StringBuilder(256);
		for (Long skuId : skuIdSet) {
			skuIds.append(skuId).append("&");
		}
		if (skuIds.length() > 0)
			skuIds.deleteCharAt(skuIds.length() - 1);

		OrderFormState orderState = orderDTO.getOrderFormState();
		OrderFormPayMethod orderPayMethod = orderDTO.getOrderFormPayMethod();
		int status = 2;
		if (orderPayMethod == OrderFormPayMethod.COD)
			status = 4;
		else if (orderState != OrderFormState.WAITING_PAY)
			status = 1;

		infoMap.put("orderId", orderId);
		infoMap.put("skuList", skuIds.toString());
		infoMap.put("status", status);

		logger.info(JsonUtils.toJson(infoMap));
	}
}
