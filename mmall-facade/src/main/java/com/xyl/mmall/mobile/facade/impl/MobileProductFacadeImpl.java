/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.enums.SupplierType;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.facade.ItemSPUFacade;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.vo.ItemSPUVO;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.content.service.CategoryContentService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.util.AreaCodeUtil;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.param.ProductSearchMainSiteParam;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.ItemSPUService;
import com.xyl.mmall.mainsite.facade.MainsiteItemFacade;
import com.xyl.mmall.mainsite.vo.DetailProductVO;
import com.xyl.mmall.mainsite.vo.DetailPromotionVO;
import com.xyl.mmall.mainsite.vo.SizeSpecVO;
import com.xyl.mmall.mobile.facade.MobileProductFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileChecker;
import com.xyl.mmall.mobile.facade.converter.MobileConfig;
import com.xyl.mmall.mobile.facade.vo.MobilePrdtVO;
import com.xyl.mmall.mobile.facade.vo.MobileProductSKUVO;
import com.xyl.mmall.mobile.facade.vo.MobileProductSearchVO;
import com.xyl.mmall.mobile.facade.vo.MobileSkuVO;
import com.xyl.mmall.mobile.ios.facade.pageView.cartList.MobileSKULimitVO;
import com.xyl.mmall.mobile.ios.facade.pageView.prdctlist.MobileSku;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitConfigDTO;
import com.xyl.mmall.saleschedule.service.ProductSKULimitService;

/**
 * @author hzjiangww
 *
 */
@Facade
public class MobileProductFacadeImpl implements MobileProductFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MainsiteItemFacade itemFacade;

	@Resource(name = "poCommonFacade")
	private ItemCenterCommonFacade commonFacade;

	@Autowired
	private CategoryFacade categoryFacade;

	@Resource
	private BusinessService businessService;

	@Resource
	private ItemSPUService itemSPUService;

	@Resource
	private ItemProductService itemProductService;

	@Autowired
	private ItemSPUFacade itemSPUFacade;

	@Resource
	private CategoryService categoryService;

	@Resource
	private CategoryContentService categoryContentService;

	@Resource
	private ProductSKULimitService productSKULimitService;

	/**
	 * 转换成商品详情的mobile类
	 * 
	 * @param vo1
	 * @param vo2
	 * @return
	 */
	private MobilePrdtVO genMobilePrdtVO(DetailProductVO vo1, DetailPromotionVO vo2, long appversion) {
		MobilePrdtVO prdtVO = new MobilePrdtVO();
		List<SizeSpecVO> skuVOs = vo1.getSizeSpecList();
		int inventoryCount = 0;
		int total = 0;
		String brandName = "";
		if (vo1.getBrand() != null)
			brandName = Converter.brandName(vo1.getBrand().getNameZH(), vo1.getBrand().getNameEN());
		if (skuVOs != null) {
			List<MobileSkuVO> skuList = new ArrayList<MobileSkuVO>();
			for (SizeSpecVO skuvo : skuVOs) {
				MobileSkuVO m_sku = new MobileSkuVO();
				m_sku.setCount(skuvo.getNum());
				total = total + (skuvo.getTotal() - skuvo.getNum());
				// m_sku.setSkuColor(skuvo.get);
				m_sku.setInvalidDesc(Converter.getSkuStatusInDetailPageDesc(skuvo.getType()));
				m_sku.setValidStatus(skuvo.getType() == 1 ? 1 : 3);
				m_sku.setPrdtId(Long.parseLong(vo1.getProductId()));
				m_sku.setPoPrice(Converter.doubleFormat(vo1.getSalePrice()));
				m_sku.setOriginPrice(Converter.doubleFormat(vo1.getMarketPrice()));
				m_sku.setPrdtName(vo1.getProductName());
				m_sku.setBrandName(brandName);
				m_sku.setSkuId(Long.parseLong(skuvo.getSkuId()));
				m_sku.setSkuSizeDesc(skuvo.getSize());
				skuList.add(m_sku);
				inventoryCount = inventoryCount + skuvo.getNum();
			}
			prdtVO.setSkuList(skuList);
		}
		prdtVO.setPoId(vo1.getPoId());
		prdtVO.setBrandId(vo1.getBrand().getId());
		prdtVO.setIsRecommend(vo1.getIsRecommend());
		if (appversion >= Converter.protocolVersion("1.2.0")) {
			if (vo1.getSameAsShop() == 1)
				prdtVO.setTag(MobileConfig.same_as_shop_tag, MobileConfig.same_as_shop_icon);
			prdtVO.setBrandName(vo1.getBrand().getName());
		}

		prdtVO.setCountDownTime(vo1.getSchedule().getPoCountDownTime());
		prdtVO.setImageURLs(vo1.getProdShowPicList());
		prdtVO.setDetailURL(Converter.genWebSiteLink(Converter.PRODUCT_DETAIL, vo1.getProductId(), ""));
		if (vo1.getProductSize() != null && vo1.getProductSize().getBody() != null
				&& vo1.getProductSize().getHeader() != null && vo1.getProductSize().getBody().size() != 0
				&& vo1.getProductSize().getHeader().size() != 0) {
			prdtVO.setSizeUrl(Converter.genWebSiteLink(Converter.SIZE_TABLE, vo1.getProductId(), ""));
		}
		prdtVO.setDiscountDesc(Converter.calDiscountFormat(vo1.getSalePrice(), vo1.getMarketPrice()));
		prdtVO.setEndTime(Converter.getTime() + vo1.getSchedule().getPoCountDownTime());
		if (vo1.getProdShowPicList() != null && vo1.getProdShowPicList().size() > 0)
			prdtVO.setImageURL(vo1.getProdShowPicList().get(0));

		prdtVO.setNotAvailDesc(Converter.getProductStatusInDetailPageDesc(vo1.getStatus()));
		prdtVO.setStatus(vo1.getStatus());
		if (inventoryCount == 0 && prdtVO.getStatus() == 1) {
			prdtVO.setStatus(4);
		}
		prdtVO.setOrignPrice(Converter.doubleFormat(vo1.getMarketPrice()));
		prdtVO.setPoPrice(Converter.doubleFormat(vo1.getSalePrice()));
		prdtVO.setPrdtId(Long.parseLong(vo1.getProductId()));
		prdtVO.setPrdtName(Converter.genPrdtName(vo1.getBrand().getName(), vo1.getProductName()));
		if (prdtVO.getStatus() != 3) {
			prdtVO.setSaleCount(total);
			if (prdtVO.getStatus() != 2)
				prdtVO.setInventoryCount(inventoryCount);
		}
		prdtVO.setWebSiteUrl(Converter.genWebSiteLink(Converter.SHARE_PRODUCT, vo1.getProductId(), ""));
		if (appversion >= Converter.protocolVersion("1.2.0")) {
			prdtVO.setShareTemplate(Converter.sharePrdtTemplate(String.valueOf(prdtVO.getPoPrice()), brandName,
					prdtVO.getPrdtName(), prdtVO.getWebSiteUrl(), prdtVO.getImageURL()));
		}

		if (vo2 != null)
			prdtVO.setSaleInfo(MobilePoFacadeImpl.poSaleInfo(vo2.getTipList()));
		return prdtVO;
	}

	@Override
	public BaseJsonVO getProductDetail(long userId, int areaId, long prdtId, long appversion) {

		logger.info("getProductDetail -> userId:<" + userId + ">,prdtId:<" + prdtId + ">,areaCode:<" + areaId + ">");
		try {
			MobileChecker.checkZero("PRODUCT ID", prdtId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		try {
			MobileChecker.checkZero("AREA CODE", areaId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_MATCH, e1.getMessage());
		}

		try {
			DetailProductVO vo = commonFacade.getDetailPageProducForAPP(prdtId);
			if (vo == null) {
				logger.error("product " + prdtId + " is null");
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL, "product is null");
			}
			DetailPromotionVO promotion = itemFacade.getDetailPagePromotionInfo(Long.parseLong(vo.getPoId()));
			MobilePrdtVO prdtVO = genMobilePrdtVO(vo, promotion, appversion);
			prdtVO.setIsNotArea(1);
			for (long area : vo.getSchedule().getAreaCode()) {
				if (area == areaId)
					prdtVO.setIsNotArea(0);
			}
			return Converter.genrBaseJsonVO("prdt", prdtVO);
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR, e1.getMessage());
		}

	}

	public List<MobileSku> getSkuByParameters(BasePageParamVO<MobileSku> basePageParamVO,
			MobileProductSearchVO searchParam) {
		BasePageParamVO<MobileProductSKUVO> paramVO = new BasePageParamVO<MobileProductSKUVO>();
		paramVO = basePageParamVO.copy(paramVO);
		List<MobileProductSKUVO> mobileProductSKUVOs = this.getProudctByParameters(paramVO, searchParam);
		basePageParamVO = paramVO.copy(basePageParamVO);
		List<MobileSku> iosSkus = new ArrayList<>();
		MobileSku iosSku = null;
		if (mobileProductSKUVOs != null && !mobileProductSKUVOs.isEmpty()) {
			for (MobileProductSKUVO mobileProductSKUVO : mobileProductSKUVOs) {
				iosSku = new MobileSku(mobileProductSKUVO);
				iosSkus.add(iosSku);
			}
		}

		return iosSkus;
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

	private List<CategoryContentDTO> getCategroy(List<CategoryContentDTO> categoryContentDTOs, Long categoryId) {

		if (CollectionUtil.isEmptyOfList(categoryContentDTOs)) {
			return null;
		}
		List<CategoryContentDTO> list = new ArrayList<>();
		if (categoryId != null && categoryId.longValue()>0) {
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

	@Override
	public List<MobileProductSKUVO> getProudctByParameters(BasePageParamVO<MobileProductSKUVO> basePageParamVO,
			MobileProductSearchVO searchParam) {
		List<Long> categoryIds  = new ArrayList<>();;

		long categoryRootId = this.categoryRootIdByareaId(searchParam.getAreaCode());
		if (categoryRootId == 0) {
			return null;
		}
		List<CategoryContentDTO> categoryContentDTOs = categoryContentService
				.getCategoryContentListByRootId(categoryRootId);

		Set<String> set = new HashSet<>();
		// 获取categoryId 对应的内容分类
		List<CategoryContentDTO> list = getCategroy(categoryContentDTOs, searchParam.getCategoryContentId());
		if (list == null || list.isEmpty()) {
			return null;
		}
		// 获取categorynormalids
		for (CategoryContentDTO categoryContentDTO : list) {
			getCategoryNormalIds(categoryContentDTO, set);
		}
		
		for (String str : set) {
			if (StringUtils.isNumeric(str)) {
				categoryIds.add(Long.valueOf(str));
			}
		}
		if (CollectionUtils.isEmpty(categoryIds)) {
			return null;
		}

		Set<Long> businessIds = new HashSet<Long>();
		Map<Long, String> storeNameMap = new HashMap<Long, String>();
		// 根据区域id获取商家id列表
		long areaId = searchParam.getAreaCode();
		List<Long> businessIdList = businessService.getBusinessIdByDistrictId(areaId, 3);
		if (!CollectionUtils.isEmpty(businessIdList)) {
			long uid = SecurityContextUtils.getUserId();
			for (Long id : businessIdList) {
				// 根据id获取店铺
				BusinessDTO businessDTO = businessService.getBreifBusinessById(id, 0);
				// 是否为空
				if (businessDTO == null) {
					continue;
				}
				// 是否是特许经营
				if (SupplierType.SPECIALMANAGE.getIntValue() == businessDTO.getType()) {
					// 未登录跳过
					if (uid > 0l) {
						// 判断是否允许
						if (isBusinessAllowed(id, uid)) {
							businessIds.add(id);
							storeNameMap.put(id, businessDTO.getStoreName());
						}
					}
					continue;
				}
				businessIds.add(id);
				storeNameMap.put(id, businessDTO.getStoreName());
			}
		}

		ProductSearchMainSiteParam param = new ProductSearchMainSiteParam();
		param.setBusinessIds(businessIds);
		// 获取brandIds
		Set<Long> brandIds = null;
		if (StringUtils.isNotBlank(searchParam.getBrandIds())) {
			String[] brandStr = searchParam.getBrandIds().split(",");
			brandIds = new HashSet<Long>(brandStr.length);
			for (String idStr : brandStr) {
				if (RegexUtils.isAllNumber(idStr)) {
					brandIds.add(Long.valueOf(idStr));
				}
			}
			param.setBrandIds(brandIds);
		}
		param.setCategoryIds(categoryIds);
		param.setSearchValue(searchParam.getSearchValue());
		BasePageParamVO<ProductSKUDTO> paramVO = new BasePageParamVO<ProductSKUDTO>();
		paramVO = basePageParamVO.copy(paramVO);
		// // 添加属性筛选
		// if (!CollectionUtils.isEmpty(searchParam.getParamMap())) {
		// param.setParamMap(searchParam.getParamMap());
		// }
		// // 添加规格筛选
		// if (!CollectionUtils.isEmpty(searchParam.getParamMap())) {
		// param.setParamMap(searchParam.getParamMap());
		// }
		// 设置排序
		if (StringUtils.equals("productSaleNum", searchParam.getSortColumn())) {
			param.setSortColumn("SaleNum");
		} else if (StringUtils.equals("updateTime", searchParam.getSortColumn())) {
			param.setSortColumn("UpdateTime");
		}
		if (searchParam.getIsAsc() == 1) {
			param.setIsAsc(true);
		}
		// 根据条形码进行查询
		if (StringUtils.isNotBlank(searchParam.getBarCode())) {
			ItemSPUDTO spuDTO = new ItemSPUDTO();
			spuDTO.setBarCode(searchParam.getBarCode());
			List<ItemSPUDTO> itemSPUDTOs = itemSPUService.getItemSPUList(spuDTO);
			if (!CollectionUtils.isEmpty(itemSPUDTOs)) {
				List<Long> spuIds = new ArrayList<>();
				for (ItemSPUDTO itemSPUDTO : itemSPUDTOs) {
					spuIds.add(itemSPUDTO.getId());
				}
				param.setSpuIds(spuIds);
			} else {
				return null;
			}
		}

		paramVO = itemProductService.getProductSKUList(paramVO, param);
		if (paramVO == null) {
			return null;
		}
		basePageParamVO = paramVO.copy(basePageParamVO);
		if (!CollectionUtils.isEmpty(paramVO.getList())) {
			List<MobileProductSKUVO> retList = new ArrayList<MobileProductSKUVO>(paramVO.getList().size());
			List<Long> skuIdList = new ArrayList<Long>(paramVO.getList().size());
			for (ProductSKUDTO skuDTO : paramVO.getList()) {
				MobileProductSKUVO productSKUMainsiteVO = new MobileProductSKUVO(skuDTO);
				String storeName = storeNameMap.get(skuDTO.getBusinessId());
				if (StringUtils.isBlank(storeName)) {
					continue;
				}

				productSKUMainsiteVO.setStoreName(storeName);
				retList.add(productSKUMainsiteVO);
				skuIdList.add(skuDTO.getId());
			}
			Map<Long, Integer> orderStock = null;
			try {
				orderStock = commonFacade.getOrderSkuStock(skuIdList);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				orderStock = new HashMap<Long, Integer>();
			}

			for (MobileProductSKUVO p : retList) {

				Integer orderStk = orderStock.get(p.getSkuId());
				if (orderStk == null) {
					p.setSkuNum(0);
				} else {
					p.setSkuNum(orderStk);
				}

				if (p.getIsLimited() == 1) {
					ProductSKULimitConfigDTO limitConfigDTO = productSKULimitService
							.getProductSKULimitConfigBySkuId(p.getSkuId());
					if (limitConfigDTO != null) {

						MobileSKULimitVO skuLimitVO = new MobileSKULimitVO();
						skuLimitVO.setAllowBuyNum(limitConfigDTO.getAllowedNum());
						skuLimitVO.setEndTime(limitConfigDTO.getEndTime());
						skuLimitVO.setStartTime(limitConfigDTO.getStartTime());
						skuLimitVO.setLimitDescrp(limitConfigDTO.getNote());
						p.setSkuLimitVO(skuLimitVO);
					}
				}
			}
			return retList;
		}
		return null;
	}

	private boolean isBusinessAllowed(long businessId, long uid) {
		return businessService.isUserBusinessAllowed(businessId, uid);
	}

	/**
	 * 根据单品添加商品基本信息
	 * 
	 * @param productSKUBackendVO
	 * @return
	 */
	private MobileProductSKUVO setSPUInfo(MobileProductSKUVO productSKUBackendVO) {
		long spuId = productSKUBackendVO.getItemSPUId();
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setId(spuId);
		ItemSPUVO spuVO = itemSPUFacade.getItemSPU(spuDTO);
		if (spuVO == null) {
			return null;
		}
		productSKUBackendVO.setProductName(spuVO.getSpuName());
		productSKUBackendVO.setProdBarCode(spuVO.getSpuBarCode());
		productSKUBackendVO.setBrandName(spuVO.getBrandName());
		productSKUBackendVO.setCategoryFullName(categoryService.getFullCategoryNormalName(spuVO.getCategoryNormalId()));
		productSKUBackendVO.setCategoryNormalId(spuVO.getCategoryNormalId());
		return productSKUBackendVO;
	}
}
