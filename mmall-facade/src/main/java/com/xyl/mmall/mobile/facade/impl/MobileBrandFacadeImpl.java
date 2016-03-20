/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.enums.SupplierType;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.content.service.CategoryContentService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.util.AreaCodeUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.ItemSPUService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mobile.facade.MobileBrandFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileChecker;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;
import com.xyl.mmall.mobile.facade.vo.MobileAreaBaseVO;
import com.xyl.mmall.mobile.facade.vo.MobileAreaListVO;
import com.xyl.mmall.mobile.facade.vo.MobileBrandVO;
import com.xyl.mmall.mobile.facade.vo.MobilePOGroupVO;
import com.xyl.mmall.mobile.facade.vo.MobileShopVO;
import com.xyl.mmall.mobile.ios.facade.param.BrandSearchVO;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.SupplierBrandFullDTO;
import com.xyl.mmall.saleschedule.enums.BrandShopStatus;
import com.xyl.mmall.saleschedule.meta.Brand;
import com.xyl.mmall.saleschedule.meta.BrandShop;
import com.xyl.mmall.saleschedule.service.BrandService;

import aj.org.objectweb.asm.Label;

/**
 * @author hzjiangww
 *
 */
@Facade
public class MobileBrandFacadeImpl implements MobileBrandFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MainBrandFacade mainBrandFacade;

	@Resource
	private BrandService brandService;

	@Autowired
	private BusinessFacade businessFacade;

	@Resource
	private POProductService poProductService;

	@Resource
	private CategoryContentService categoryContentService;

	@Resource
	private BusinessService businessService;

	@Resource
	private ItemSPUService itemSPUService;

	@Resource
	private ItemProductService itemProductService;

	public static MobileBrandVO coverBrandVO(BrandItemDTO dto) {
		MobileBrandVO vo = new MobileBrandVO();
		if (StringUtils.isBlank(dto.getBrandHead())) {
			dto.setBrandHead("#");
		}
		vo.setBegin(dto.getBrandHead());
		vo.setActivePOCount(dto.getPoCount());
		vo.setBrandId(dto.getBrandId());
		vo.setBrandDesc(null);
		vo.setBrandName(Converter.brandName(dto.getBrandNameZh(), dto.getBrandNameEn()));
		vo.setDescImage(dto.getBrandVisualImgApp());
		vo.setFavoriteNum(dto.getFavCount());
		vo.setIsFavorite(dto.isFavorited() ? 1 : 0);
		vo.setLogoImage(dto.getLogo());
		vo.setCreateTime(dto.getFavTime());
		return vo;
	}

	public static MobileShopVO coverShopVO(BrandShop dto, boolean needChangeLoc) {
		MobileShopVO vo = new MobileShopVO();
		if (needChangeLoc) {
			double[] loc = Converter.changeBD09llToGCJ02(dto.getLatitude().doubleValue(),
					dto.getLongitude().doubleValue());
			vo.setLatitude(loc[0]);
			vo.setLongitude(loc[1]);
		} else {
			vo.setLatitude(dto.getLatitude().doubleValue());
			vo.setLongitude(dto.getLongitude().doubleValue());
		}
		vo.setShopAddressDesc(dto.getShopAddr());
		vo.setShopId(dto.getBrandShopId());
		vo.setShopName(dto.getShopName());
		vo.setShopPhone(dto.getShopTel());
		// vo.setZipcode(dto.getShopZone());
		return vo;
	}

	@Override
	public BaseJsonVO getBrandList(long userId, int areaId, Integer type, MobilePageCommonAO ao) {
		logger.info("getBrandList -> type:<" + type + ">");
		try {
			MobileChecker.checkZero("AREA CODE", areaId);
			MobileChecker.checkNull("TYPE", type);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		try {
			if (type == 1) {
				ao.setPageSize(ao.getPageSize() + 1);
				DDBParam param = DDBParam.genParamX(ao.getPageSize());
				param.setOrderColumn("createDate");
				// 可以优化
				List<BrandItemDTO> brandItemDTO = brandService.getAllBrandForApp2(userId, areaId);
				if (brandItemDTO == null) {
					return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL, "no favorite found");
				}
				List<MobileBrandVO> list = new ArrayList<MobileBrandVO>();
				for (int i = 0; i < brandItemDTO.size() && i < (ao.getPageSize() - 1); i++) {
					BrandItemDTO dto = brandItemDTO.get(i);
					list.add(MobileBrandFacadeImpl.coverBrandVO(dto));
				}
				boolean hasnext = false;
				if (brandItemDTO != null && brandItemDTO.size() == ao.getPageSize())
					hasnext = true;
				return Converter.listBaseJsonVO(list, hasnext);
			} else if (type == 2) {
				// 推荐品牌
				List<BrandItemDTO> dtos = mainBrandFacade.getRecommendBrandItemList(areaId, userId, 7, true);
				List<MobileBrandVO> list = new ArrayList<MobileBrandVO>();
				for (BrandItemDTO dto : dtos) {
					list.add(MobileBrandFacadeImpl.coverBrandVO(dto));
				}
				return Converter.listBaseJsonVO(list, false);
			} else {
				return Converter.errorBaseJsonVO(MobileErrorCode.PARAM_NO_MATCH, "type:" + type);
			}

		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}

	}

	@Override
	public BaseJsonVO getBrandDetail(long userId, long brandId, int areaId, int os) {

		try {
			MobileChecker.checkZero("AREA CODE", areaId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_MATCH, e1.getMessage());
		}
		logger.info("getBrandDetail brandId:" + brandId + " area:" + areaId);
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			BrandItemDTO dto = mainBrandFacade.getBrandItemDTO(brandId, userId);

			MobileBrandVO brandVO = coverBrandVO(dto);
			if (StringUtils.isBlank(brandVO.getBrandName())) {
				Converter.errorBaseJsonVO(MobileErrorCode.DATA_NOT_MATCH);
			}
			// List<BusinessDTO> business =
			// businessFacade.getBusinessDTOByAreaIdAndBrandId((long) areaId,
			// brandId);
			BusinessDTO business = businessFacade.getBusinessByAreaIdAndBrandId((long) areaId, brandId, 1);
			if (business == null) {
				business = businessFacade.getBusinessByAreaIdAndBrandId((long) areaId, brandId, 2);
			}
			if (business != null) {
				long supplierId = business.getId();
				// PO详情
				POListDTO polist1 = mainBrandFacade.getPOList(brandId, areaId);
				POListDTO f_polist = mainBrandFacade.getPOListFuture(brandId, areaId, 4);
				if (polist1 != null && polist1.getPoList() != null) {
					List<PODTO> polist = polist1.getPoList();
					if (f_polist != null && f_polist.getPoList() != null) {
						/*
						 * List<SortUnit<PODTO>> temp = new
						 * ArrayList<SortUnit<PODTO>>(); for (PODTO a :
						 * f_polist.getPoList()) { SortUnit<PODTO> temp1 = new
						 * SortUnit
						 * <PODTO>(a.getScheduleDTO().getSchedule().getStartTime
						 * (),a); temp.add(temp1); } Collections.sort(temp);
						 * for(SortUnit<PODTO> b:temp){ polist.add(b.getT()); }
						 */

						polist.addAll(f_polist.getPoList());
					}
					// 活动收藏变品牌收藏
					List<Long> ids = new ArrayList<Long>();
					for (PODTO podto : polist) {
						ids.add(podto.getScheduleDTO().getSchedule().getId());
					}
					List<Integer> newPrdts = null;
					try {
						newPrdts = poProductService.getProductCount(ids, 1);
					} catch (Exception e) {
						e.printStackTrace();
					}

					List<MobilePOGroupVO> pogroups = MobilePoFacadeImpl.fullInPoGroup(polist, brandVO.getIsFavorite(),
							newPrdts);
					result.put("poGroupList", pogroups);
				}
				// 店铺详情
				SupplierBrandFullDTO brandInfo = mainBrandFacade.getBrandFullDTOBySupplierId(supplierId, 0l, true);
				if (brandInfo != null && brandInfo.getBasic() != null) {
					brandVO.setBrandDesc(brandInfo.getBasic().getIntro2());
				} else {
					logger.error("brandInfo is null");
				}
				// 转换类型
				HashMap<Long, List<BrandShop>> areaMap = new HashMap<Long, List<BrandShop>>();
				if (brandInfo != null && brandInfo.getShops() != null) {
					for (BrandShopDTO shopDto : brandInfo.getShops()) {
						try {
							BrandShop shop = shopDto.changeDataIntoBrandShop();
							if (BrandShopStatus.SHOP_USING.equals(shop.getStatus())) {
								List<BrandShop> b_list = areaMap.get(shop.getCity());
								if (b_list == null) {
									b_list = new ArrayList<BrandShop>();
								}
								b_list.add(shop);
								areaMap.put(shop.getCity(), b_list);
							}
						} catch (Exception e) {
							logger.error(e.toString());
							// 这边是因为里面没做空值判断，只能这样防止跳出
						}
					}
				} else {
					logger.error("brandInfo is null");
				}

				// 组装
				List<MobileAreaListVO> arealist = new ArrayList<MobileAreaListVO>();

				for (long cityId : areaMap.keySet()) {
					MobileAreaListVO a_vo = new MobileAreaListVO();
					a_vo.setCode(cityId);
					if (areaMap.get(cityId).size() > 0)
						a_vo.setName(areaMap.get(cityId).get(0).getCityName());
					List<MobileAreaBaseVO> arealist2 = new ArrayList<MobileAreaBaseVO>();
					HashSet<Long> temp = new HashSet<Long>();
					for (BrandShop b : areaMap.get(cityId)) {
						MobileAreaBaseVO sub_area = new MobileAreaBaseVO();
						if (!temp.contains(b.getDistrict())) {
							sub_area.setCode(b.getDistrict());
							sub_area.setName(b.getDistrictName());
							temp.add(b.getDistrict());
							arealist2.add(sub_area);
						}
					}
					a_vo.setSubArea(arealist2);
					arealist.add(a_vo);
				}
				result.put("areaList", arealist);
				// 获取首次店铺列表
				List<MobileShopVO> shoplist = new ArrayList<MobileShopVO>();
				if (arealist.size() > 0 && arealist.get(0).getSubArea() != null
						&& arealist.get(0).getSubArea().size() > 0) {
					List<BrandShop> blist = areaMap.get(arealist.get(0).getCode());
					boolean change = needChangeLoc(os);
					for (BrandShop l : blist) {
						// 选择区的位置
						if (l.getDistrict() == arealist.get(0).getSubArea().get(0).getCode())
							shoplist.add(MobileBrandFacadeImpl.coverShopVO(l, change));
					}
				}

				result.put("shopList", shoplist);
			} else {
				logger.error("call area no brand: brandid:" + brandId + " areacode:" + areaId);
			}

			result.put("brand", brandVO);
			return Converter.converterBaseJsonVO(result);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}

	}

	private boolean needChangeLoc(int os) {
		// ///需要转换时候用
		return false;
	}

	@Override
	public BaseJsonVO getShopByCity(long brandId, int areaId, long district, int os) {
		try {
			MobileChecker.checkZero("BRAND ID", brandId);
			MobileChecker.checkZero("AREA CODE", areaId);
			MobileChecker.checkZero("AREA CODE(city)", district);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		logger.info("getBrandDetail brandId:" + brandId + " area:" + areaId);
		try {
			BusinessDTO business = businessFacade.getBusinessByAreaIdAndBrandId((long) areaId, brandId, 1);
			SupplierBrandFullDTO brandInfo = null;
			if (business != null)
				brandInfo = mainBrandFacade.getBrandFullDTOBySupplierId(business.getId(), 0l, true);
			if (business == null || brandInfo == null) {
				business = businessFacade.getBusinessByAreaIdAndBrandId((long) areaId, brandId, 2);
				if (business != null)
					brandInfo = mainBrandFacade.getBrandFullDTOBySupplierId(business.getId(), 0l, true);
			}
			if (brandInfo != null) {
				long supplierId = business.getId();
				List<MobileShopVO> shoplist = new ArrayList<MobileShopVO>();
				boolean change = needChangeLoc(os);
				for (BrandShopDTO shopDto : brandInfo.getShops()) {
					try {
						BrandShop shop = shopDto.changeDataIntoBrandShop();
						if (BrandShopStatus.SHOP_USING.equals(shop.getStatus())) {
							if (district == shop.getDistrict()) {
								shoplist.add(MobileBrandFacadeImpl.coverShopVO(shop, change));
							}
						}
					} catch (Exception e) {
						logger.error(e.toString());
						// 这边是因为里面没做空值判断，只能这样防止跳出
					}
				}

				return Converter.listBaseJsonVO(shoplist, false);

			} else {
				return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_MATCH);
			}

		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

	@Override
	public List<Brand> getBrandListInOrderByCategory(BrandSearchVO brandSearchVO, long areaId) throws Exception {
		List<Long> categoryNormalIds = new ArrayList<>();
		;

		long categoryRootId = this.categoryRootIdByareaId(areaId);
		if (categoryRootId == 0) {
			return null;
		}
		List<CategoryContentDTO> categoryContentDTOs = categoryContentService
				.getCategoryContentListByRootId(categoryRootId);

		Set<String> set = new HashSet<>();
		// 获取categoryId 对应的内容分类
		List<CategoryContentDTO> list = getCategroy(categoryContentDTOs, brandSearchVO.getCategoryId());
		if (list == null || list.isEmpty()) {
			return null;
		}
		// 获取categorynormalids
		for (CategoryContentDTO categoryContentDTO : list) {
			getCategoryNormalIds(categoryContentDTO, set);
		}

		for (String str : set) {
			if (StringUtils.isNumeric(str)) {
				categoryNormalIds.add(Long.valueOf(str));
			}
		}
		if (CollectionUtils.isEmpty(categoryNormalIds)) {
			return null;
		}

		// 获取品牌ID列表
		// List<Long> brandIds = itemSPUService.getBrandIds(ids);
		// List<JSONObject> jsonObjects
		// =brandService.getBrandListOrderBySKUSaleNum(brandIds);
		// 从商品中获取品牌

		// 按区域获取可供店铺
		Set<Long> businessIds = new HashSet<Long>();
		long uid = SecurityContextUtils.getUserId();
		if (brandSearchVO.getBusinessId() != null) {
			BusinessDTO businessDTO = businessService.getBusinessById(brandSearchVO.getBusinessId(), 0);
			allowToSet(businessIds, uid, businessDTO);
		} else {
			List<Long> businessIdList = businessService.getBusinessIdByDistrictId(areaId, 3);
			if (!CollectionUtils.isEmpty(businessIdList)) {

				for (Long id : businessIdList) {
					// 根据id获取店铺
					BusinessDTO businessDTO = businessService.getBusinessById(id, 0);
					allowToSet(businessIds, uid, businessDTO);
				}
			}
		}

		// 根据条形码进行查询
		if (StringUtils.isNotBlank(brandSearchVO.getBarCode())) {
			ItemSPUDTO spuDTO = new ItemSPUDTO();
			spuDTO.setBarCode(brandSearchVO.getBarCode());
			List<ItemSPUDTO> itemSPUDTOs = itemSPUService.getItemSPUList(spuDTO);
			if (!CollectionUtils.isEmpty(itemSPUDTOs)) {
				List<Long> spuIds = new ArrayList<>();
				for (ItemSPUDTO itemSPUDTO : itemSPUDTOs) {
					spuIds.add(itemSPUDTO.getId());
				}
				List<ProductSKUDTO> productSKUDTOs = itemProductService.getProductSKUDTOBySpuIds(spuIds);

				// 获取有效的品牌ID,其实还要判断下内容分类是否在该区域下面
				Set<Long> brandIds = new HashSet<>();

				label :for (ProductSKUDTO productSKUDTO : productSKUDTOs) {
					if (productSKUDTO.getStatus() != ProductStatusType.ONLINE.getIntValue()) {
						continue;
					}
					for (Long businessId : businessIds) {
						if (productSKUDTO.getBusinessId() == businessId.longValue()) {
							for (Long normalId : categoryNormalIds) {
								if (productSKUDTO.getCategoryNormalId() == normalId) {
									brandIds.add(productSKUDTO.getBrandId());
									continue label;
								}
							}
						}
					}
				}
				if (CollectionUtils.isEmpty(brandIds)) {
					return null;
				}
				List<Long> res = new ArrayList<>();
				res.addAll(brandIds);
				return getBrandListByBrandIds(res);

			} else {
				return null;
			}
		}

		// 先获取品牌id
		List<Long> brandIds = itemProductService.getBrandIdsByCategoryIds(categoryNormalIds,
				brandSearchVO.getSearchValue(), businessIds);
		if (CollectionUtils.isEmpty(brandIds)) {
			return null;
		}
		return getBrandListByBrandIds(brandIds);

	}

	@SuppressWarnings("unchecked")
	private List<Brand> getBrandListByBrandIds(List<Long> brandIds) {
		List<JSONObject> jsonObjects = brandService.getBrandListOrderBySKUSaleNum(brandIds);
		for (JSONObject jsonObject : jsonObjects) {
			if (jsonObject != null && jsonObject.get("index") != null && jsonObject.get("index").equals("all")) {
				;
				return (List<Brand>) jsonObject.get("list");
			}
		}
		return null;
	}

	private void allowToSet(Set<Long> businessIds, long uid, BusinessDTO businessDTO) {
		// 是否为空
		if (businessDTO == null) {
			return;
		}
		// 是否是特许经营
		if (SupplierType.SPECIALMANAGE.getIntValue() == businessDTO.getType()) {
			// 未登录跳过
			if (uid > 0l) {
				// 判断是否允许
				if (isBusinessAllowed(businessDTO.getId(), uid)) {
					businessIds.add(businessDTO.getId());
				}
			}
			return;
		}
		businessIds.add(businessDTO.getId());
	}

	private List<CategoryContentDTO> getCategroy(List<CategoryContentDTO> categoryContentDTOs, Long categoryId) {

		if (CollectionUtil.isEmptyOfList(categoryContentDTOs)) {
			return null;
		}
		List<CategoryContentDTO> list = new ArrayList<>();
		if (categoryId != null && categoryId.longValue() > 0) {
			for (CategoryContentDTO categoryContentDTO : categoryContentDTOs) {
				if (categoryContentDTO.getId() == categoryId) {
					list.add(categoryContentDTO);
					return list;
				} else {
					if (!CollectionUtil.isEmptyOfList(categoryContentDTO.getSubCategoryContentDTOs())) {
						List<CategoryContentDTO> temp = this.getCategroy(categoryContentDTO.getSubCategoryContentDTOs(),
								categoryId);
						if (temp != null && !temp.isEmpty()) {
							list.addAll(temp);
							return list;
						}
					}
				}
			}
		} else {
			for (CategoryContentDTO categoryContentDTO : categoryContentDTOs) {
				list.add(categoryContentDTO);
			}
			return list;
		}

		return null;
	}

	private void getCategoryNormalIds(CategoryContentDTO categoryContentDTO, Set<String> set) {
		if (categoryContentDTO == null || set == null) {
			return;
		}
		if (!StringUtils.isEmpty(categoryContentDTO.getCategoryNormalIds())) {
			for (String str : categoryContentDTO.getCategoryNormalIds().split(",")) {
				set.add(str);
			}
		}
		if (!CollectionUtils.isEmpty(categoryContentDTO.getSubCategoryContentDTOs())) {
			for (CategoryContentDTO temp : categoryContentDTO.getSubCategoryContentDTOs()) {
				if (temp != null)
					getCategoryNormalIds(temp, set);
			}
		}

	}

	private long categoryRootIdByareaId(long areaId) {
		List<CategoryContentDTO> categoryContentDTOs = categoryContentService.getCategoryContentListByLevelAndRootId(0,
				-1);
		long categoryRootId = 0l;
		for (CategoryContentDTO categoryContentDTO : categoryContentDTOs) {
			if (StringUtils.isNotEmpty(categoryContentDTO.getDistrictIds())
					&& AreaCodeUtil.isContainArea(String.valueOf(areaId), categoryContentDTO.getDistrictIds())) {
				categoryRootId = categoryContentDTO.getId();
				break;
			}
		}
		return categoryRootId;
	}

	private boolean isBusinessAllowed(long businessId, long uid) {
		return businessService.isUserBusinessAllowed(businessId, uid);
	}
}