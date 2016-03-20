package com.xyl.mmall.mainsite.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.promotion.service.PromotionService;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleLikeService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@Facade
public class MainBrandFacadeImpl implements MainBrandFacade {

	@Resource
	private BrandService brandService;

	@Resource
	private BusinessService businessService;

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private ScheduleLikeService scheduleLikeService;

	@Resource
	private LocationFacade locationFacade;
	
	@Resource
	private CategoryService categoryService;

	@Override
	public SupplierBrandFullDTO getBrandFullDTO(long supplierBrandId) {
		return brandService.getSupplierBrandFullDetails(supplierBrandId, true, true);
	}

	@Override
	public List<BrandShopDTO> getBrandShops(long areaId, long brandId) {
		Business business = businessService.getBusinessByAreaIdAndBrandId(areaId, brandId);
		if (business != null) {
			return brandService.getBrandShops(business.getId());
		} else {
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.MainBrandFacade#getBrandFullDTOBySupplierId(long)
	 */
	//@Cacheable(value = "mainSiteBrandCache")
	@Override
	public SupplierBrandFullDTO getBrandFullDTOBySupplierId(long supplierId) {
		return brandService.getSupplierBrandFullDTOBySupplierId(supplierId);
	}

	@Override
	public SupplierBrandFullDTO getBrandFullDTOBySupplierId(long supplierId, long userId, boolean bMobile) {
		return brandService.getSupplierBrandFullDTOBySupplierId(supplierId, userId, true, bMobile);
	}

	// @Override
	// public SupplierBrandFullDTO
	// getBrandFullDTOByBySupplierIdWithOutbrandShops(
	// long supplierId) {
	// return brandService.getSupplierBrandFullDTOBySupplierId(supplierId,
	// false);
	// }

	@Override
	public boolean getBrandCollectionState(long userId, long brandId) {
		return brandService.getBrandCollectionState(userId, brandId);
	}

	@Override
	public boolean addBrandCollection(long userId, long brandId) {
		return brandService.addBrandCollection(userId, brandId);
	}

	@Override
	public boolean removeBrandCollection(long userId, long brandId) {
		return brandService.removeBrandCollection(userId, brandId);
	}

	@Override
	public RetArg getBrandUserFavListByUserId(DDBParam param, long userId, long areaId) {
		return brandService.getUserFavBrandList(param, 0, userId, areaId, false, false);
	}

	@Override
	public RetArg getFavbrandList(DDBParam param, long time, long userId, long areaId) {
		return brandService.getUserFavBrandList(param, time, userId, areaId, true, false);
	}

	@Override
	public RetArg getFavbrandListApp(DDBParam param, long time, long userId, long areaId) {
		return brandService.getUserFavBrandList(param, time, userId, areaId, true, true);
	}

	@Override
	public RetArg getAllBrandItemList(DDBParam param, long time) {
		return brandService.getAllBrandItemList(param, time);
	}

	private long getSaleSiteFlag(long saleSiteCode) {
		List<LocationCode> provinceList = locationFacade.getAllProvince();
		for (LocationCode province : provinceList) {
			if (province.getCode() == saleSiteCode) {
				return province.getSiteFlag();
			}
		}

		return -1;
	}

	@Cacheable(value = "brandPOListCache")
	@Override
	public POListDTO getPOList(long brandId, long areaId) {
		long saleSiteFlag = getSaleSiteFlag(areaId);
		POListDTO poList = scheduleService.getScheduleList(brandId, saleSiteFlag);
		
		fillPOPromotion(poList);
		return poList;
	}
	
	@Resource
	protected PromotionService promotionService;
	
	private void fillPOPromotion(POListDTO poList) {
		if (poList == null || poList.getPoList() == null || poList.getPoList().size() <= 0) {
			return;
		}
		
		List<Long> poIdList = new ArrayList<Long>();
		for (PODTO poDTO : poList.getPoList()) {
			poIdList.add(poDTO.getScheduleDTO().getSchedule().getId());
		}
		try {
			Map<Long, String> poPromotionMap = promotionService.getPromotionTipMap(poIdList, true);
			if (poPromotionMap != null) {
				for (PODTO poDTO : poList.getPoList()) {
					String promotionDesc = poPromotionMap.get(poDTO.getScheduleDTO().getSchedule().getId());
					poDTO.setPromotionDesc(promotionDesc);
				}
			}
		} catch (Exception e) {
			
		}
	}

	@Cacheable(value = "brandPOListCache")
	@Override
	public POListDTO getPOListFuture(long brandId, long areaId, int dayAfter) {
		long saleSiteFlag = getSaleSiteFlag(areaId);
		return scheduleService.getScheduleListFuture(brandId, saleSiteFlag, dayAfter);
	}

	@Override
	public RetArg getAllBrandList(DDBParam param, long userId, String begin, long areaId) {
		return brandService.getAllBrandItemListWithDetails(param, 0, userId, begin, false, areaId);
	}

	@Override
	public RetArg getAllBrandListApp(DDBParam param, long userId, long time, long areaId) {
		return brandService.getAllBrandItemListWithDetails(param, time, userId, "ALL", true, areaId);
	}

	@Override
	public RetArg getBrandListAppByIndex(DDBParam param, long userId, long time, long areaId, String index) {
		return brandService.getAllBrandItemListWithDetails(param, time, userId, index, true, areaId);
	}

	@Override
	public boolean isPOFavoredByUser(long poId, long userId) {
		return scheduleLikeService.isLike(userId, poId);
	}

	// 暂时按照排序推荐
	@Override
	public List<BrandItemDTO> getRecommendBrandItemList(long areaId, long userId, int count, boolean isApp) {
		List<BrandItemDTO> list = brandService.getRecommendBrandItemList(areaId, userId, count, isApp);
		return list;
	}

	@Override
	public BrandItemDTO getBrandItemDTO(long brandId, long userId) {
		return brandService.getBrandItemDTOByBrandId(brandId, userId);
	}

	@Override
	public boolean isBrandFavoredByUser(long brandId, long userId) {
		return brandService.isBrandInFavList(userId, brandId);
	}

	@Override
	public List<BrandItemDTO> getAllBrandForApp(DDBParam param, long brandIdAfter, long userId, long areaId) {
		return brandService.getAllBrandForApp(param, brandIdAfter, userId, areaId);
	}

	@Override
	public List<BrandShopDTO> getBrandShopListBySupplierId(long supplierId) {
		return brandService.getBrandShopListBySupplierId(supplierId);
	}

	@Override
	public List<BrandItemDTO> getAllBrandForApp3G(long userId, long areaId) {
		return brandService.getAllBrandForApp2(userId, areaId);
	}

	@Override
	public List<JSONObject> getBrandListInOrderByCategory(long categoryId, long areaId) {
		Category c = categoryService.getCategoryById(categoryId);
		if (c != null) {
			String brandValue = c.getBrand();
			if (!StringUtils.isBlank(brandValue)) {
				List<Long> list = JsonUtils.parseArray(brandValue, Long.class);
				if (list != null && list.size() > 0) {
					return brandService.getBrandListInOrderByCategory(areaId, list);
				}
			}
		}
		return new ArrayList<JSONObject>();
	}

}
