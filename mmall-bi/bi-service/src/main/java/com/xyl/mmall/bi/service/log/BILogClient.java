package com.xyl.mmall.bi.service.log;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xyl.mmall.bi.core.meta.BasicLog;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
@Component("biLogClient")
public class BILogClient {

	private static Logger logger = LoggerFactory.getLogger(BILogClient.class);

	/**
	 * 使用日志业务类打印日志.
	 * 
	 * @param basicLog
	 * @param ohterKey
	 */
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		try {
			BILogService biLogService = BILogServiceFactory.getInstance(basicLog.getClientType(), basicLog.getType());
			if (biLogService != null)
				biLogService.logInfo(basicLog, logMap, otherKey);
		} catch (Exception e) {
			logger.error("logInfo:" + basicLog.toString() + "; otherKey=" + otherKey, e);
		}
	}

}
