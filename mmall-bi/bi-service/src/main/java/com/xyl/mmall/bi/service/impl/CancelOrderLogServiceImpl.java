package com.xyl.mmall.bi.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogService;
import com.xyl.mmall.bi.util.BILogUtils;
import com.xyl.mmall.framework.util.JsonUtils;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
@Service("cancelOrderLogService")
public class CancelOrderLogServiceImpl implements BILogService {

	private static Logger logger = LoggerFactory.getLogger(CancelOrderLogServiceImpl.class);

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.bi.service.log.BILogService#logInfo(com.xyl.mmall.bi.meta.BasicLog,
	 *      java.util.Map, java.lang.String)
	 */
	@Override
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		Map<String, Object> infoMap = BILogUtils.getBasicLogMap(basicLog, logMap);
		
		Long orderId = Long.valueOf(otherKey);
		
		infoMap.put("ordereId", orderId);
		
		logger.info(JsonUtils.toJson(infoMap));
	}

}
