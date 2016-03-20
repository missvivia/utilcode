package com.xyl.mmall.bi.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogService;
import com.xyl.mmall.bi.util.BILogUtils;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.saleschedule.dto.BrandDTO;

@Service("brandLogService")
public class BrandLogServiceImpl implements BILogService {

	private static Logger logger = LoggerFactory.getLogger(BrandLogServiceImpl.class);
	
	@Autowired
	private BrandFacade brandFacade;
	
	@Override
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		Map<String, Object> infoMap = BILogUtils.getBasicLogMap(basicLog, logMap);
		long brandId = Long.parseLong(otherKey);
		BrandDTO dto = brandFacade.getBrandByBrandId(brandId);
		infoMap.put("brandId", brandId);
		infoMap.put("brandName", dto.getBrand() != null ? dto.getBrand().getBrandNameAuto() : null);
		logger.info(JsonUtils.toJson(infoMap));
	}

}
