package com.xyl.mmall.bi.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogService;
import com.xyl.mmall.bi.util.BILogUtils;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.common.facade.impl.ItemCenterCommonFacadeImpl;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@Service("goodsPageLogService")
public class GoodsPageLogServiceImpl implements BILogService {

	private static Logger logger = LoggerFactory.getLogger(GoodsPageLogServiceImpl.class);

	@Resource
	private POProductService poProductService;

	@Resource
	private BrandService brandService;

	@Resource
	private ScheduleService scheduleService;

	@Resource(name = "poCommonFacade")
	private ItemCenterCommonFacade commonFacade;

	@Resource
	private CategoryService categoryService;

	@Override
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		Map<String, Object> infoMap = BILogUtils.getBasicLogMap(basicLog, logMap);
		Long pid = !StringUtils.isEmpty(otherKey) ? Long.valueOf(otherKey) : null;
		POProductDTO dto = pid != null ? poProductService.getProductDTO(pid) : null;
		if (dto == null)
			return;
		PODTO po = scheduleService.getScheduleById(dto.getPoId());
		infoMap.put("supplyId", dto.getSupplierId());
		infoMap.put("poId", dto.getPoId());
		infoMap.put("brandId", dto.getBrandId());
		infoMap.put("brandName", brandService.getBrandByBrandId(dto.getBrandId()).getBrand().getBrandNameAuto());
		infoMap.put("pid", dto.getId());
		List<POSkuDTO> skuList = (List<POSkuDTO>) dto.getSKUList();
		infoMap.put("status", commonFacade.getProductType(po, skuList));
		List<Category> clist = categoryService.getCategoryListBylowId(dto.getLowCategoryId());
		if (clist != null && clist.size() > 0) {
			Category c = clist.get(0);
			infoMap.put("category1", c.getId());
		}
		if (clist != null && clist.size() > 1) {
			Category c = clist.get(1);
			infoMap.put("category2", c.getId());
		}
		if (clist != null && clist.size() > 2) {
			Category c = clist.get(2);
			infoMap.put("category3", c.getId());
		}
		logger.info(JsonUtils.toJson(infoMap));
	}

}