package com.xyl.mmall.bi.service.log;

import java.util.Map;

import com.xyl.mmall.bi.core.meta.BasicLog;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
public interface BILogService {

	/**
	 * 打印日志.
	 * 
	 * @param basicLog
	 * @param logMap
	 */
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey);

}
