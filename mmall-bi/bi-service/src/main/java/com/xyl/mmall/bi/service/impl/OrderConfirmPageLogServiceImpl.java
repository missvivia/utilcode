package com.xyl.mmall.bi.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.core.meta.OrderConfirmPageParam;
import com.xyl.mmall.bi.service.log.BILogService;
import com.xyl.mmall.bi.util.BILogUtils;
import com.xyl.mmall.framework.util.JsonUtils;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
@Service("orderConfirmPageLogService")
public class OrderConfirmPageLogServiceImpl implements BILogService {

	private static Logger logger = LoggerFactory.getLogger(OrderConfirmPageLogServiceImpl.class);

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.bi.service.log.BILogService#logInfo(com.xyl.mmall.bi.meta.BasicLog,
	 *      java.util.Map, java.lang.String)
	 */
	@Override
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		Map<String, Object> infoMap = BILogUtils.getBasicLogMap(basicLog, logMap);
		if(StringUtils.isEmpty(otherKey)){
			logger.warn("otherKey is null for orderConfirm bi");
			return;
		}
		OrderConfirmPageParam param = JsonUtils.fromJson(otherKey, OrderConfirmPageParam.class);
		String cartIds = param.getCartIds();
		String[] skuIdArray = cartIds.split(",");
		StringBuilder skuIds = new StringBuilder(256);
		for (String skuId : skuIdArray) {
			skuIds.append(skuId).append("&");
		}
		if (skuIds.length() > 0)
			skuIds.deleteCharAt(skuIds.length() - 1);

		infoMap.put("skuList", skuIds.toString());
		infoMap.put("status", param.isHasCa() ? 1 : 2);

		logger.info(JsonUtils.toJson(infoMap));
	}

}
