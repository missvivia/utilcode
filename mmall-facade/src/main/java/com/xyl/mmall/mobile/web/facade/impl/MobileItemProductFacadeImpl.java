/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.mobile.web.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.dto.SendDistrictDTO;
import com.xyl.mmall.cms.enums.SupplierType;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.facade.ItemSPUFacade;
import com.xyl.mmall.cms.facade.util.DistrictCodeUtil;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.vo.CategoryContentVO;
import com.xyl.mmall.cms.vo.CategoryNormalVO;
import com.xyl.mmall.cms.vo.ItemSPUVO;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.content.constants.CategoryContentLevel;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemModelDTO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.dto.ModelParamOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelParameterDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpeciOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpecificationDTO;
import com.xyl.mmall.itemcenter.dto.ProdParamDTO;
import com.xyl.mmall.itemcenter.dto.ProdSpeciDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.param.ProductSearchMainSiteParam;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.ItemModelService;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.ItemSPUService;
import com.xyl.mmall.mobile.web.facade.MobileItemProductFacade;
import com.xyl.mmall.mobile.web.vo.ProdSpeciMainSiteVO;
import com.xyl.mmall.mobile.web.vo.ProductSKUMainSiteVO;
import com.xyl.mmall.mobile.web.vo.ProductSearchMainSiteVO;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.service.SkuOrderStockService;

/**
 * ItemProductFacadeImpl.java created by yydx811 at 2015年5月27日 下午7:01:14
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@Facade("mobileItemProductFacade")
public class MobileItemProductFacadeImpl implements MobileItemProductFacade {

	private static Logger logger = Logger.getLogger(MobileItemProductFacadeImpl.class);
	
	@Autowired
	private CategoryFacade categoryFacade;
	
	@Resource
	private ItemSPUService itemSPUService;
	
	@Resource
	private ItemProductService itemProductService;
	
	@Autowired
	private ItemSPUFacade itemSPUFacade;
	
	@Resource
	private CategoryService categoryService;

	@Resource
	private CartService cartService;

	@Resource
	private SkuOrderStockService skuOrderStockService;

	@Resource(name = "poCommonFacade")
	private ItemCenterCommonFacade commonFacade;
	
	@Resource
	private BusinessService businessService;
	
	@Resource
	private ItemModelService itemModelService;
	
	@Override
	public List<ProductSKUMainSiteVO> getProudctByParameters(BasePageParamVO<ProductSKUMainSiteVO> basePageParamVO,
			ProductSearchMainSiteVO searchParam) {
		List<Long> categoryIds = null;
		if (searchParam.getCategoryContentId() > 0l) {
			CategoryContentVO contentVO = categoryFacade.getCategoryContentById(searchParam.getCategoryContentId());
			if (contentVO != null) {
				List<CategoryNormalVO> categoryNormalList = new ArrayList<CategoryNormalVO>();
				// 三级内容分类
				if (contentVO.getLevel() == CategoryContentLevel.LEVEL_THIRD.getIntValue()) {
					if (CollectionUtils.isEmpty(contentVO.getCategoryNormalVOs())) {
						return null;
					}
					if (CollectionUtils.isEmpty(contentVO.getSendDistrictDTOs())) {
						return null;
					}
					categoryNormalList.addAll(contentVO.getCategoryNormalVOs());
				} else {
					// 获取子分类，空返回null
					List<CategoryContentVO> subList = categoryFacade.getSubCategoryContentList(
							searchParam.getCategoryContentId());
					if (CollectionUtils.isEmpty(subList)) {
						return null;
					}
					// 二级内容分类
					if (contentVO.getLevel() == CategoryContentLevel.LEVEL_SECOND.getIntValue()) {
						for (CategoryContentVO c : subList) {
							c = categoryFacade.getCategoryContentById(c.getId());
							if (!CollectionUtils.isEmpty(c.getCategoryNormalVOs()) 
									&& !CollectionUtils.isEmpty(c.getSendDistrictDTOs())) {
								categoryNormalList.addAll(c.getCategoryNormalVOs());
							}
						}
					}
					// 一级内容分类
					if (contentVO.getLevel() == CategoryContentLevel.LEVEL_FIRST.getIntValue()) {
						for (CategoryContentVO c : subList) {
							List<CategoryContentVO> thirdList = categoryFacade.getSubCategoryContentList(c.getId());
							if (CollectionUtils.isEmpty(thirdList)) {
								return null;
							}
							for (CategoryContentVO c3 : thirdList) {
								c3 = categoryFacade.getCategoryContentById(c3.getId());
								if (!CollectionUtils.isEmpty(c3.getCategoryNormalVOs()) 
										&& !CollectionUtils.isEmpty(c3.getSendDistrictDTOs())) {
									categoryNormalList.addAll(c3.getCategoryNormalVOs());
								}
							}
						}
					}
				}
				if (CollectionUtils.isEmpty(categoryNormalList)) {
					return null;
				}
				// 如果是按内容分类获取list
				if (searchParam.getCategoryNormalId() < 1l) {
					categoryIds = new ArrayList<Long>(categoryNormalList.size());
					for (CategoryNormalVO c : categoryNormalList) {
						categoryIds.add(c.getCategoryId());
					}
				} else {
					categoryIds = new ArrayList<Long>(1);
					for (CategoryNormalVO c : categoryNormalList) {
						if (searchParam.getCategoryNormalId() == c.getCategoryId()) {
							categoryIds.add(c.getCategoryId());
							break;
						}
					}
					// 分类id和内容id不符
					if (CollectionUtils.isEmpty(categoryIds)) {
						return null;
					}
				}
				
			}
		}
		Set<Long> businessIds = new HashSet<Long>();
		// 根据区域id获取商家id列表
		long areaId = searchParam.getAreaCode();
		List<Long> businessIdList = businessService.getBusinessIdByDistrictId(areaId, 3);
		if (!CollectionUtils.isEmpty(businessIdList)) {
			long uid = SecurityContextUtils.getUserId();
			for (Long id : businessIdList) {
				// 根据id获取店铺
				BusinessDTO businessDTO = businessService.getBusinessById(id, 0);
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
						}
					}
					continue;
				}
				businessIds.add(id);
			}
		}
		ProductSearchMainSiteParam param = new ProductSearchMainSiteParam();
		param.setBusinessIds(businessIds);
		// 获取spuId
		List<Long> SPUIds = null;
		Set<Long> brandIds = null;
		if (StringUtils.isNotBlank(searchParam.getBrandIds())) {
			String[] brandStr = searchParam.getBrandIds().split(",");
			brandIds = new HashSet<Long>(brandStr.length);
			for (String idStr : brandStr) {
				if (RegexUtils.isAllNumber(idStr)) {
					brandIds.add(Long.valueOf(idStr));
				}
			}
		}
		SPUIds = itemSPUService.getSPUIds(categoryIds, brandIds, searchParam.getSearchValue());
		if (CollectionUtils.isEmpty(SPUIds)) {
			return null;
		}
		param.setSpuIds(SPUIds);
		BasePageParamVO<ProductSKUDTO> paramVO = new BasePageParamVO<ProductSKUDTO>();
		paramVO = basePageParamVO.copy(paramVO);
		// 添加属性筛选  ????
		if (!CollectionUtils.isEmpty(searchParam.getSpeciMap())) {
			param.setSpeciMap(searchParam.getSpeciMap());
		}
		// 添加规格筛选 ???? 为什么要重复set两遍？
		if (!CollectionUtils.isEmpty(searchParam.getParamMap())) {
			param.setParamMap(searchParam.getParamMap());
		}
		paramVO = itemProductService.getProductSKUList(paramVO, param);
		if (paramVO == null) {
			return null;
		}
		basePageParamVO = paramVO.copy(basePageParamVO);
		if (!CollectionUtils.isEmpty(paramVO.getList())) {
			List<ProductSKUMainSiteVO> retList = new ArrayList<ProductSKUMainSiteVO>(paramVO.getList().size());
			List<Long> skuIdList = new ArrayList<Long>(paramVO.getList().size());
			for (ProductSKUDTO skuDTO : paramVO.getList()) {
				ProductSKUMainSiteVO productSKUBackendVO = new ProductSKUMainSiteVO(skuDTO);
				String storeName = businessService.getBusinessNameById(skuDTO.getBusinessId(), 0);
				if (StringUtils.isBlank(storeName)) {
					continue;
				}
				productSKUBackendVO.setStoreName(storeName);
				// 添加基本信息
				productSKUBackendVO = setSPUInfo(productSKUBackendVO);
				retList.add(productSKUBackendVO);
				skuIdList.add(skuDTO.getId());
			}
//			Map<Long, Integer> cartStock = null;
			Map<Long, Integer> orderStock = null;
			// 库存
//			try {
//				cartStock = cartService.getInventoryCount(skuIdList);
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//				cartStock = new HashMap<Long, Integer>();
//			}
			try {
				orderStock = commonFacade.getOrderSkuStock(skuIdList);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				orderStock = new HashMap<Long, Integer>();
			}
			for (ProductSKUMainSiteVO p : retList) {
				Integer orderStk = orderStock.get(p.getSkuId());
				if (orderStk == null) {
					p.setSkuNum(0);
				} else {
					p.setSkuNum(orderStk);
				}
//				Integer cartStk = cartStock.get(p.getSkuId());
//				if (cartStk == null)
//					cartStk = 0;
			}
			return retList;
		}
		return null;
	}

	@Override
	public ProductSKUMainSiteVO getProductSKUVO(ProductSKUDTO skuDTO) {
		skuDTO = itemProductService.getProductSKUDTO(skuDTO, true);
		if (skuDTO == null) {
			return null;
		}
		ProductSKUMainSiteVO productSKUVO = new ProductSKUMainSiteVO(skuDTO);
		String storeName = businessService.getBusinessNameById(skuDTO.getBusinessId(), 0);
		if (StringUtils.isBlank(storeName)) {
			return null;
		}
		productSKUVO.setStoreName(storeName);
		// 添加基本信息
		productSKUVO = setSPUInfo(productSKUVO);
		SkuOrderStockDTO orderStock = skuOrderStockService.getSkuOrderStockDTOBySkuId(skuDTO.getId());
		productSKUVO.setProductNum(orderStock == null ? 0 : orderStock.getStockCount());
		// 转换商品规格vo
		productSKUVO.setSpeciList(prodSpeciConvertToVO(skuDTO.getSpeciList()));
		// 转换商品属性vo
		ItemModelDTO modelDTO = itemModelService.getItemModel(productSKUVO.getCategoryNormalId(), false);
		if (modelDTO == null) {
			return productSKUVO;
		}
		String paramJson = prodParamDTOConvertToJson(skuDTO.getParamList(), modelDTO.getId());
		if (productSKUVO.getProdDetail() != null) {
			productSKUVO.getProdDetail().setProdParamJson(paramJson);
		}
		return productSKUVO;
	}
	
	/**
	 * 根据单品添加商品基本信息
	 * @param productSKUBackendVO
	 * @return
	 */
	private ProductSKUMainSiteVO setSPUInfo(ProductSKUMainSiteVO productSKUBackendVO) {
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
		productSKUBackendVO.setCategoryFullName(
				categoryService.getFullCategoryNormalName(spuVO.getCategoryNormalId()));
		productSKUBackendVO.setCategoryNormalId(spuVO.getCategoryNormalId());
		return productSKUBackendVO;
	}
	
	/**
	 * 转换商品属性json
	 * @param paramDTOList
	 * @param itemModelId
	 * @return
	 */
	private String prodParamDTOConvertToJson(List<ProdParamDTO> paramDTOList, long itemModelId) {
		List<ModelParameterDTO> modelParamList = itemModelService.getModelParamList(itemModelId, 0);
		if (CollectionUtils.isEmpty(modelParamList)) {
			return null;
		} else {
			// 属性id-optionids map
			Map<Long, Set<Long>> paramIdMap = new HashMap<Long, Set<Long>>();
			if (!CollectionUtils.isEmpty(paramDTOList)) {
				for (ProdParamDTO prodParamDTO : paramDTOList) {
					// 取出map中属性idset
					Set<Long> keySet = paramIdMap.keySet();
					long key = prodParamDTO.getModelParamId();
					// 如果在map中取出optionids
					Set<Long> optionIds = null;
					if (keySet.contains(key)) {
						optionIds = paramIdMap.get(key);
					}
					// 如果optionids为空
					if (CollectionUtils.isEmpty(optionIds)) {
						optionIds = new HashSet<Long>();
					}
					// 添加optionid并放回map
					optionIds.add(prodParamDTO.getModelParamOptionId());
					paramIdMap.put(key, optionIds);
				}
			}
			JSONObject json = new JSONObject(modelParamList.size());
			for (ModelParameterDTO modelParam : modelParamList) {
				if (CollectionUtils.isEmpty(modelParam.getModelParamOptionList())) {
					continue;
				}
				// 商品属性选项
				Set<Long> optionIds = paramIdMap.get(modelParam.getId());
				StringBuilder option = new StringBuilder();
				for (ModelParamOptionDTO modelParamOption : modelParam.getModelParamOptionList()) {
					if (!CollectionUtils.isEmpty(optionIds) && optionIds.contains(modelParamOption.getId())) {
						option.append(modelParamOption.getOptionValue()).append(",");
					}
				}
				if (option.length() > 0) {
					option.deleteCharAt(option.length() - 1);
					json.put(modelParam.getName(), option);
				}
			}
			return json.toString();
		}
	}
	
	/**
	 * 转换商品规格vo
	 * @param speciList
	 * @return
	 */
	private List<ProdSpeciMainSiteVO> prodSpeciConvertToVO(List<ProdSpeciDTO> speciList) {
		if (!CollectionUtils.isEmpty(speciList)) {
			List<ProdSpeciMainSiteVO> retList = new ArrayList<ProdSpeciMainSiteVO>(speciList.size());
			for (ProdSpeciDTO prodSpeciDTO : speciList) {
				long key = prodSpeciDTO.getModelSpeciId();
				long value = prodSpeciDTO.getModelSpeciOptionId();
				ModelSpecificationDTO specificationDTO = itemModelService.getModelSpecification(key);
				if (specificationDTO == null) {
					continue;
				}
				ModelSpeciOptionDTO option = itemModelService.getSpeciOption(value, key);
				if (option == null) {
					continue;
				}
				ProdSpeciMainSiteVO speciVO = new ProdSpeciMainSiteVO();
				speciVO.setSpeciId(key);
				speciVO.setSpecificationName(specificationDTO.getName());
				speciVO.setSpeciOptionId(value);
				speciVO.setSpeciOption(option.getOptionValue());
				retList.add(speciVO);
			}
			return retList;
		}
		return null;
	}

	@Override
	public boolean isIPAllowedByBusinessId(int areaId, long businessId) {
		List<SendDistrictDTO> districtDTOList = businessService.getSendDistrictDTOList(businessId);
		if (!CollectionUtils.isEmpty(districtDTOList)) {
			boolean flag = false;
			String areaCode = String.valueOf(areaId);
			for (SendDistrictDTO s : districtDTOList) {
				flag = DistrictCodeUtil.isContainArea(areaCode, s);
				if (flag) {
					break;
				}
			}
			return flag;
		}
		return false;
	}

	@Override
	public boolean isBusinessAllowed(long businessId, long uid) {
		return businessService.isUserBusinessAllowed(businessId, uid);
	}
	@Override
	public ProductSKUDTO getProductSKUDTO(ProductSKUDTO skuDTO) {
		return itemProductService.getProductSKUDTO(skuDTO, false);
	}
}
