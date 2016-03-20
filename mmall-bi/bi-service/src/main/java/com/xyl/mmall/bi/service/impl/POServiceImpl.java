package com.xyl.mmall.bi.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogService;
import com.xyl.mmall.bi.util.BILogUtils;
import com.xyl.mmall.framework.util.JsonUtils;

@Service("poService")
public class POServiceImpl implements BILogService {

	private static Logger logger = LoggerFactory.getLogger(POServiceImpl.class);
	
	@Override
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		Map<String, Object> infoMap = BILogUtils.getBasicLogMap(basicLog, logMap);
		String[] args = otherKey.split("~");
		infoMap.put("supplyId", args[0]);
		infoMap.put("poId", args[1]);
		infoMap.put("brandId", args[2]);
		infoMap.put("branName", args[3]);
		infoMap.put("status", args[4]);
		logger.info(JsonUtils.toJson(infoMap));
	}

}
