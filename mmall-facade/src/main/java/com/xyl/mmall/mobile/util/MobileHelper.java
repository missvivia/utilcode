package com.xyl.mmall.mobile.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.enums.SupplierType;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.vo.CategoryContentVO;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.mobile.ios.facade.pageView.common.MobileCategoryVO;
import com.xyl.mmall.mobile.web.facade.MobileItemProductFacade;

@Component
public class MobileHelper {

	@Resource
	private CategoryFacade categoryFacade;

	@Resource
	private ItemProductService itemProductService;
	
	@Autowired
	private MobileItemProductFacade mobileItemProductFacade;

	@Autowired
	private BusinessFacade businessFacade;

	public List<CategoryContentDTO> getCategoryList(long curSupplierAreaId, Boolean hasAllDate, Long categoryId)
			throws Exception {
		return categoryFacade.getCategoryContentListByAreaId(curSupplierAreaId, hasAllDate, categoryId);
	}

	public CategoryContentVO getCategoryById(Long categoryId) throws Exception {
		return categoryFacade.getBriefCategoryContentById(categoryId);
	}

	// 类目全部返回，只是将字段isvisible设为不可见
	public List<CategoryContentDTO> getCategoryListByBusinessId(long businessId, long curSupplierAreaId)
			throws Exception {
		List<Long> categoryIds = itemProductService.getCategoryNormalIdsByBusinessId(businessId);
		List<CategoryContentDTO> categoryContentDTOs = getCategoryList(curSupplierAreaId, true, null);
		if (CollectionUtil.isEmptyOfList(categoryContentDTOs)) {
			return null;
		}

		for (CategoryContentDTO categoryContentDTO : categoryContentDTOs) {
			if (CollectionUtil.isEmptyOfList(categoryContentDTO.getSubCategoryContentDTOs())) {
				continue;
			}
			for (CategoryContentDTO secondCategoryContentDTO : categoryContentDTO.getSubCategoryContentDTOs()) {
				if (CollectionUtil.isEmptyOfList(secondCategoryContentDTO.getSubCategoryContentDTOs())) {
					continue;
				}
				for (CategoryContentDTO thirdCategoryContentDTO : secondCategoryContentDTO
						.getSubCategoryContentDTOs()) {
					String[] categoryArray = StringUtils.split(thirdCategoryContentDTO.getCategoryNormalIds(), ",");
					for (String categoryId : categoryArray) {
						if (categoryIds.contains(Long.parseLong(categoryId))) {
							thirdCategoryContentDTO.setIsvisible(true);
							secondCategoryContentDTO.setIsvisible(true);
							categoryContentDTO.setIsvisible(true);
							break;
						}
					}
				}
			}

		}
		return categoryContentDTOs;

	}

	public List<MobileCategoryVO> getViewAbleCategoryListByBusinessId(long businessId, long curSupplierAreaId)
			throws Exception {
		List<Long> categoryIds = itemProductService.getCategoryNormalIdsByBusinessId(businessId);
		List<CategoryContentDTO> categoryContentDTOs = getCategoryList(curSupplierAreaId, true, null);
		if (CollectionUtil.isEmptyOfList(categoryContentDTOs)) {
			return null;
		}
		List<MobileCategoryVO> categorys = getViewAbleCategory(categoryContentDTOs, categoryIds);

		return categorys;

	}

	private List<MobileCategoryVO> getViewAbleCategory(List<CategoryContentDTO> categoryContentDTOs,
			List<Long> categoryIds) {
		List<MobileCategoryVO> first = new ArrayList<>();
		List<MobileCategoryVO> second = null;
		List<MobileCategoryVO> third = null;
		MobileCategoryVO categoryVO = null;

		for (CategoryContentDTO cate : categoryContentDTOs) {
			if (CollectionUtil.isEmptyOfList(cate.getSubCategoryContentDTOs())) {
				continue;
			}
			second = new ArrayList<>();
			for (CategoryContentDTO secondCategoryContentDTO : cate.getSubCategoryContentDTOs()) {
				if (CollectionUtil.isEmptyOfList(secondCategoryContentDTO.getSubCategoryContentDTOs())) {
					continue;
				}
				third = new ArrayList<>();

				for (CategoryContentDTO thirdCategoryContentDTO : secondCategoryContentDTO
						.getSubCategoryContentDTOs()) {
					String[] categoryArray = StringUtils.split(thirdCategoryContentDTO.getCategoryNormalIds(), ",");
					for (String categoryId : categoryArray) {
						if (categoryIds.contains(Long.parseLong(categoryId))) {
							thirdCategoryContentDTO.setIsvisible(true);
							secondCategoryContentDTO.setIsvisible(true);
							cate.setIsvisible(true);
							categoryVO = new MobileCategoryVO(thirdCategoryContentDTO);
							third.add(categoryVO);
							break;
						}
					}
				}

				if (!CollectionUtil.isEmptyOfList(third)) {
					categoryVO = new MobileCategoryVO(secondCategoryContentDTO);
					categoryVO.setSubCategoryContentDTOs(third);
					second.add(categoryVO);
				}
			}

			if (!CollectionUtil.isEmptyOfList(second)) {
				categoryVO = new MobileCategoryVO(cate);
				categoryVO.setSubCategoryContentDTOs(second);
				first.add(categoryVO);
			}

		}
		return first;
	}

	@Cacheable(value = "categoryDTOCache", key = "#curSupplierAreaId")
	public List<CategoryContentDTO> getCategoryList(long curSupplierAreaId) {
		return categoryFacade.getCategoryContentListByAreaId(curSupplierAreaId);
	}

	public BaseJsonVO verifyView(Long skuId,Long businessId,int areaId) {

		BaseJsonVO mobileBaseJsonVO = new BaseJsonVO();

		if (businessId == null || businessId.longValue() == 0) {
			if (skuId == null || skuId.longValue() == 0) {
				mobileBaseJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "参数错误！");
				return mobileBaseJsonVO;
			}
			ProductSKUDTO skuDTO = new ProductSKUDTO();
			skuDTO.setId(skuId);
//			skuDTO.setStatus(ProductStatusType.ONLINE.getIntValue());
			skuDTO = mobileItemProductFacade.getProductSKUDTO(skuDTO);
			if (skuDTO != null) {
				businessId = skuDTO.getBusinessId();
			} else {
				mobileBaseJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "商品信息不存在！");
				return mobileBaseJsonVO;
			}
		}

		BusinessDTO businessDTO = businessFacade.getBusinessById(businessId);
		if (businessDTO == null) {
			mobileBaseJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "店铺不存在！");
			return mobileBaseJsonVO;
		}
		boolean isAllowed = mobileItemProductFacade.isIPAllowedByBusinessId(areaId, businessId);
		// 特许经营验证
		if (businessDTO.getType() == SupplierType.SPECIALMANAGE.getIntValue()) {
			long userid = SecurityContextUtils.getUserId();
			if (userid < 0) {
				mobileBaseJsonVO.setCodeAndMessage(ResponseCode.RES_ENOTAUTH, "用户未登录！");
				return mobileBaseJsonVO;
			}
			if (SecurityContextUtils.getUserName().equals(businessDTO.getBusinessAccount())) {
				isAllowed = true;
			} else {
				isAllowed = isAllowed ? mobileItemProductFacade.isBusinessAllowed(businessId, userid) : false;

			}
		}

		if (!isAllowed) {
			mobileBaseJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "对不起，您所在区域暂时还没开通，敬请期待！");
			return mobileBaseJsonVO;
		}

		return new BaseJsonVO(ErrorCode.SUCCESS);
	}

}