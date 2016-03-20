package com.xyl.mmall.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.content.service.CategoryContentService;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.util.AreaCodeUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.mobile.ios.facade.pageView.common.MobileCategoryVO;
import com.xyl.mmall.mobile.ios.facade.pageView.store.StoreDetailVo;
import com.xyl.mmall.mobile.util.MobileHelper;
import com.xyl.mmall.mobile.web.facade.MobileItemFacade;
import com.xyl.mmall.mobile.web.facade.MobileItemProductFacade;
import com.xyl.mmall.util.AreaUtils;

@Controller
@RequestMapping("/m/store")
public class MobileStoreController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MobileStoreController.class);

	@Autowired
	private MobileItemProductFacade mobileItemProductFacade;

	@Autowired
	private MobileHelper mobileHelper;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private CategoryContentService categoryContentService;

	@Autowired
	private MobileItemFacade mobileItemFacade;
	

	/**
	 * by
	 */
	@RequestMapping({ "/storeInfo" })
	@ResponseBody
	public BaseJsonVO storeHome(@RequestParam(value = "businessId", required = true) long businessId) throws Exception {
		int areaId = AreaUtils.getProvinceCode();
		BaseJsonVO baseJsonVO = mobileHelper.verifyView(null, businessId,areaId);
		if(baseJsonVO.getCode() != ErrorCode.SUCCESS.getIntValue()){
			return baseJsonVO;
		}
		
		BaseJsonVO mobileBaseJsonVO = new BaseJsonVO(ErrorCode.SUCCESS);
		BusinessDTO businessDTO = businessFacade.getBusinessById(businessId);
		if (businessDTO == null) {
			mobileBaseJsonVO = new BaseJsonVO(ErrorCode.STORE_DOES_NOT_EXIST);
			return mobileBaseJsonVO;
		}

		mobileBaseJsonVO = new BaseJsonVO(ErrorCode.SUCCESS);
		StoreDetailVo storeDetailVo = new StoreDetailVo();
		BeanUtils.copyProperties(storeDetailVo,businessDTO);
		mobileBaseJsonVO.setResult(storeDetailVo);

		return mobileBaseJsonVO;
	}

	@RequestMapping({ "/category" })
	@ResponseBody
	public BaseJsonVO category(@RequestParam(value = "businessId", required = true) long businessId) throws Exception {
		int areaId = AreaUtils.getProvinceCode();
		BaseJsonVO mobileBaseJsonVO = null;
		List<MobileCategoryVO> categoryContentDTOs = mobileHelper.getViewAbleCategoryListByBusinessId(businessId,
				areaId);
		mobileBaseJsonVO = new BaseJsonVO(ErrorCode.SUCCESS);
		mobileBaseJsonVO.setResult(categoryContentDTOs);

		return mobileBaseJsonVO;
	}

	
	@RequestMapping(value = "/verifyView", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO verifyView(@RequestParam(value = "skuId", required = false) Long skuId,@RequestParam(value = "businessId", required = false) Long businessId) {
		int areaId = AreaUtils.getProvinceCode();
		return mobileHelper.verifyView(skuId, businessId, areaId);
	}

	@RequestMapping(value = "/searchProduct")
	public @ResponseBody BaseJsonVO searchProductInStore(ProductSKUSearchParam searchParam) {

		BaseJsonVO baseJsonVO = mobileHelper.verifyView(null, searchParam.getBusinessId(),AreaUtils.getProvinceCode());
		if(baseJsonVO.getCode() != ErrorCode.SUCCESS.getIntValue()){
			return baseJsonVO;
		}
		
		searchParam.setStatus(4);// 店铺搜索上架商品
//		searchParam.setLowCategoryId(0);
		searchParam.setSearchType(2);// 店铺中商品搜索

		if (searchParam.getLimit() == 0) {
			searchParam.setLimit(8);
		}

		if (searchParam.getLowCategoryId() > 0) {
			long categoryRootId = 0l;
			List<CategoryContentDTO> categoryContentDTOs = categoryContentService
					.getCategoryContentListByLevelAndRootId(0, -1);
			for (CategoryContentDTO categoryContentDTO : categoryContentDTOs) {
				if (StringUtils.isNotEmpty(categoryContentDTO.getDistrictIds()) && AreaCodeUtil.isContainArea(
						String.valueOf(AreaUtils.getProvinceCode()), categoryContentDTO.getDistrictIds())) {
					categoryRootId = categoryContentDTO.getId();
					break;
				}
			}
			Map<Long, CategoryContentDTO> categoryMap = categoryContentService
					.getCategoryContentDTOMapByRootId(categoryRootId);
			CategoryContentDTO categoryContentDTO = categoryMap.get(searchParam.getLowCategoryId());
			searchParam.setCategoryNormalIds(categoryContentDTO.getCategoryNormalIds());
		}

		return mobileItemFacade.searchProductSKU(searchParam);
	}

}
