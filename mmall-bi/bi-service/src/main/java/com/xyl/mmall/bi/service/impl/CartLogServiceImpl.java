package com.xyl.mmall.bi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogService;
import com.xyl.mmall.bi.util.BILogUtils;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.service.POProductService;

@Service("cartLogService")
public class CartLogServiceImpl implements BILogService {

	private static Logger logger = LoggerFactory.getLogger(CartLogServiceImpl.class);
	
	@Resource
	private POProductService poProductService;

	
	@Override
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		Map<String, Object> infoMap = BILogUtils.getBasicLogMap(basicLog, logMap);
		long skuid = Long.parseLong(otherKey);
		List<Long> idlist = new ArrayList<Long>();
		idlist.add(skuid);
		List<POSkuDTO> skuDTOList = poProductService.getSkuDTOListBySkuId(idlist);
		infoMap.put("skuid", skuid);
		if(skuDTOList!=null && skuDTOList.size()>0)
			infoMap.put("poid", skuDTOList.get(0).getPoId());
		logger.info(JsonUtils.toJson(infoMap));
	}

}
