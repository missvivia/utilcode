package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.ProductFacade;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.enums.SupplierType;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.content.service.CategoryContentService;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.mainsite.facade.ItemProductFacade;
import com.xyl.mmall.mainsite.util.MainsiteHelper;
import com.xyl.mmall.util.AreaUtils;

@Controller
@RequestMapping("/store")
public class StoreController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

	@Autowired
	private ItemProductFacade itemProductFacade;

	@Autowired
	private ProductFacade productFacade;

	@Autowired
	private CategoryFacade categoryFacade;

	@Autowired
	private MainsiteHelper mainsiteHelper;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private CategoryContentService categoryContentService;

	@RequestMapping({ "/{businessId}" })
	public String storeHome(@PathVariable long businessId, Model model) {
		int areaId = AreaUtils.getProvinceCode();
		BusinessDTO businessDTO = businessFacade.getBusinessById(businessId);
		if (businessDTO == null || businessDTO.getIsActive() == 1) {// 1冻结
			return "/pages/404";
		}
		boolean isAllowed = itemProductFacade.isIPAllowedByBusinessId(areaId, businessId);
		// 特许经营验证
		if (businessDTO.getType() == SupplierType.SPECIALMANAGE.getIntValue()) {
			long userid = SecurityContextUtils.getUserId();
			if (userid < 0) {
				return "/pages/nopermission";
			}
			if (SecurityContextUtils.getUserName().equals(businessDTO.getBusinessAccount())) {
				isAllowed = true;
			} else {
				isAllowed = isAllowed ? itemProductFacade.isBusinessAllowed(businessId, userid) : false;
			}
		}
		List<CategoryContentDTO> categoryContentDTOs = mainsiteHelper.getCategoryListByBusinessId(businessId, areaId);
		model.addAttribute("businesser", businessDTO);// 商家信息
		model.addAttribute("allowed", isAllowed);// 是否允许进入店铺浏览
		model.addAttribute("category", categoryContentDTOs);// 内容分类
		return isAllowed ? "/pages/store/home" : "/pages/nopermission";
	}

	@RequestMapping(value = "/searchProduct")
	public @ResponseBody BaseJsonVO searchProductInStore(ProductSKUSearchParam searchParam) {
		// 根据内容分类Id设值关联的商品分类Id
		if (searchParam.getLowCategoryId() > 0 && searchParam.getRootId() > 0) {
			Map<Long, CategoryContentDTO> categoryMap = categoryContentService
					.getCategoryContentDTOMapByRootId(searchParam.getRootId());
			CategoryContentDTO categoryContentDTO = categoryMap.get(searchParam.getLowCategoryId());
			searchParam.setCategoryNormalIds(categoryContentDTO.getCategoryNormalIds());
		}
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		searchParam.setStatus(4);// 店铺搜索上架商品
		searchParam.setLowCategoryId(0);
		searchParam.setSearchType(2);// 店铺中商品搜索
		searchParam.setUserId(SecurityContextUtils.getUserId());
		baseJsonVO.setResult(productFacade.searchProductSKU(searchParam));
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}

	@RequestMapping(value = "/getBrandsByContentCategoryId")
	public @ResponseBody BaseJsonVO getBrandsByContentCategoryId(@RequestParam(value = "businessId") long businessId,
			@RequestParam(value = "contentCategoryId", required = false, defaultValue = "0") long id,
			@RequestParam(value = "rootId", required = false, defaultValue = "0") long rootId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		// 默认取商家全部品牌
		if (id == 0 || rootId == 0) {
			baseJsonVO.setResult(mainsiteHelper.getBrandByBusinessIdAndcategoryNormalIds(businessId, null));
		} else {
			Map<Long, CategoryContentDTO> categoryMap = categoryContentService.getCategoryContentDTOMapByRootId(rootId);
			CategoryContentDTO categoryContentDTO = categoryMap.get(id);
			List<Long> normarlIdList = new ArrayList<Long>();
			if (categoryContentDTO != null && StringUtils.isNotEmpty(categoryContentDTO.getCategoryNormalIds())) {
				for (String normarlId : categoryContentDTO.getCategoryNormalIds().split(",")) {
					normarlIdList.add(Long.parseLong(normarlId));
				}
			}
			baseJsonVO.setResult(mainsiteHelper.getBrandByBusinessIdAndcategoryNormalIds(businessId, normarlIdList));
		}
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}

}
