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
 * 退货申请页面
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年11月9日 下午6:29:20
 *
 */
@Service("returnGoodsApplyLogService")
public class ReturnGoodsApplyLogServiceImpl implements BILogService {

	private static Logger logger = LoggerFactory.getLogger(ReturnGoodsApplyLogServiceImpl.class);
	
	@Override
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		Map<String, Object> infoMap = BILogUtils.getBasicLogMap(basicLog, logMap);
		infoMap.put("refundType", otherKey);
		logger.info(JsonUtils.toJson(infoMap));
	}

}
