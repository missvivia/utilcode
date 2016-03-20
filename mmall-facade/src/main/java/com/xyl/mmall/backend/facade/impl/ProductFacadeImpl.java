/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.backend.facade.impl;

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

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.backend.facade.ProductFacade;
import com.xyl.mmall.backend.vo.ProdParamBackendVO;
import com.xyl.mmall.backend.vo.ProdParamOptionVO;
import com.xyl.mmall.backend.vo.ProdSpeciBackendVO;
import com.xyl.mmall.backend.vo.ProductSKUBackendVO;
import com.xyl.mmall.backend.vo.ProductSKUDetailVO;
import com.xyl.mmall.backend.vo.ProductSKULimitConfigVO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.ItemSPUFacade;
import com.xyl.mmall.cms.facade.util.SyncInventoryHelper;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.vo.ItemSPUVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ItemCenterException;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;
import com.xyl.mmall.itemcenter.dto.ItemModelDTO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.dto.ModelParamOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelParameterDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpeciOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpecificationDTO;
import com.xyl.mmall.itemcenter.dto.ProdParamDTO;
import com.xyl.mmall.itemcenter.dto.ProdSpeciDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDetailDTO;
import com.xyl.mmall.itemcenter.enums.CategoryNormalLevel;
import com.xyl.mmall.itemcenter.enums.OperateProductAction;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.meta.ItemSPU;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.ItemModelService;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.service.SkuOrderStockService;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitConfigDTO;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitRecordDTO;
import com.xyl.mmall.saleschedule.service.ProductSKULimitService;

/**
 * ProductFacadeImpl.java created by yydx811 at 2015年5月14日 下午7:41:55
 * 商品facade接口实现
 *
 * @author yydx811
 */
@Facade
public class ProductFacadeImpl implements ProductFacade {

	private static Logger logger = Logger.getLogger(ProductFacadeImpl.class);

	@Resource
	private ItemProductService itemProductService;

	@Resource
	private SkuOrderStockService skuOrderStockService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private ItemModelService itemModelService;

	@Autowired
	private ItemSPUFacade itemSPUFacade;

	@Autowired
	private SyncInventoryHelper syncInventoryHelper;

	@Autowired
	private ProductService productService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private ProductSKULimitService productSKULimitService;

	@Override
	public BasePageParamVO<ProductSKUBackendVO> searchProductSKU(ProductSKUSearchParam searchParam) {
		// 设置根据商品分类搜索条件
		Map<Long, CategoryNormalDTO> categoryMap = categoryService.getCategoryNormalMapFromCache();
		if (searchParam.getLowCategoryId() > 0 && StringUtils.isEmpty(searchParam.getCategoryNormalIds())) {
			switch (CategoryNormalLevel.getEnumByValue(searchParam.getLevel())) {
			case LEVEL_FIRST:
			case LEVEL_SECOND:
				CategoryNormalDTO categoryNormalDTO = categoryMap.get(searchParam.getLowCategoryId());
				if (categoryNormalDTO != null && StringUtils.isNotEmpty(categoryNormalDTO.getSubThirdIds())) {
					searchParam.setCategoryNormalIds(categoryNormalDTO.getSubThirdIds());
				}
				break;
			case LEVEL_THIRD:
				searchParam.setCategoryNormalIds(String.valueOf(searchParam.getLowCategoryId()));
				break;
			default:
				break;
			}
		}
		BasePageParamVO<ProductSKUDTO> basePageDTOs = itemProductService.searchProductSKU(searchParam);
		// dto转为vo
		BasePageParamVO<ProductSKUBackendVO> basePageParamVO = new BasePageParamVO<ProductSKUBackendVO>(
				basePageDTOs.getTotal(), basePageDTOs.getPageSize());
		if (CollectionUtil.isEmptyOfList(basePageDTOs.getList())) {
			return basePageParamVO;
		}
		List<Long> productIds = new ArrayList<Long>();
		for (ProductSKUDTO productSKUDTO : basePageDTOs.getList()) {
			productIds.add(productSKUDTO.getId());
		}

		// 店铺搜索时库存设置
		Map<Long, SkuOrderStockDTO> skuStockMap = null;
		if (searchParam.getSearchType() == 2) {
			List<SkuOrderStockDTO> skuOrderStockDTOs = skuOrderStockService.getSkuOrderStockDTOListBySkuIds(productIds);
			skuStockMap = Maps.uniqueIndex(skuOrderStockDTOs, new Function<SkuOrderStockDTO, Long>() {
				public Long apply(SkuOrderStockDTO skuOrderStockDTO) {
					return skuOrderStockDTO.getSkuId();
				}
			});
		}

		// 限购信息设置
		Map<Long, ProductSKULimitConfigDTO> skuLimitMap = productSKULimitService.getProductSKULimitConfigDTOMap(
				searchParam.getUserId(), productIds);
		List<ProductSKUBackendVO> productSKUBackendVOs = new ArrayList<ProductSKUBackendVO>();
		for (ProductSKUDTO productSKUDTO : basePageDTOs.getList()) {
			if (skuStockMap != null && skuStockMap.get(productSKUDTO.getId()) != null) {
				productSKUDTO.setSkuNum(skuStockMap.get(productSKUDTO.getId()).getStockCount());
			}
			ProductSKUBackendVO productSKUBackendVO = new ProductSKUBackendVO(productSKUDTO);
			// 设置商品类目信息
			if (categoryMap.get(productSKUBackendVO.getCategoryNormalId()) == null) {
				productSKUBackendVO.setCategoryFullName(categoryService.getFullCategoryNormalName(productSKUBackendVO
						.getCategoryNormalId()));
			} else {
				productSKUBackendVO.setCategoryFullName(categoryMap.get(productSKUBackendVO.getCategoryNormalId())
						.getFullCategoryName());
			}
			// 设置限购信息
			if (skuLimitMap != null && skuLimitMap.get(productSKUDTO.getId()) != null) {
				productSKUBackendVO.setSkuLimitConfigVO(new ProductSKULimitConfigVO(skuLimitMap.get(productSKUDTO
						.getId())));
			}
			productSKUBackendVOs.add(productSKUBackendVO);
		}

		basePageParamVO.setList(productSKUBackendVOs);
		return basePageParamVO;
	}

	@Override
	public int deleteProducts(long businessId, List<Long> productIds) {
		if (CollectionUtil.isEmptyOfList(productIds)) {
			return -5;// 参数有误
		}
		try {
			boolean flag = itemProductService.batchDeleteProducts(businessId, productIds);
			if (!flag) {
				return -2;// 删除商品失败
			}
			flag = skuOrderStockService.bacthDeleteSkuOrderStock(productIds);
			if (!flag) {
				logger.error("delete SkuOrderStock " + productIds.toString() + " error");
				return -3;// 删除商品库存失败
			}
			flag = syncInventoryHelper.clearInventory(productIds);
			return flag ? 1 : -4;// 删除商品库存缓存失败
		} catch (ItemCenterException e) {
			logger.error("batch delete product " + productIds.toString() + " error:", e);
			return -1;
		}
	}

	@Override
	public int deleteProduct(long businessId, Long productId) {
		try {
			boolean flag = itemProductService.deleteProduct(businessId, productId);
			if (!flag) {
				return -2;// 删除商品失败
			}
			flag = skuOrderStockService.deleteSkuOrderStock(productId);
			if (!flag) {
				logger.error("delete SkuOrderStock " + productId + " error");
				return -3;// 删除商品库存失败
			}
			List<Long> poIdList = new ArrayList<Long>();
			poIdList.add(productId);
			flag = syncInventoryHelper.clearInventory(poIdList);
			return flag ? 1 : -4;// 删除商品库存缓存失败
		} catch (ItemCenterException e) {
			logger.error("delete product " + productId + " error:" + productId, e);
			return -1;
		}

	}

	@Override
	public int addProductSKUs(List<ProductSKUDTO> productSKUDTOList) {
		List<Long> skuIdList = null;
		try {
			productSKUDTOList = itemProductService.addProductSKUs(productSKUDTOList);
			List<SkuOrderStockDTO> skuOrderStockList = new ArrayList<SkuOrderStockDTO>();
			long now = System.currentTimeMillis();
			skuIdList = new ArrayList<Long>(productSKUDTOList.size());
			for (ProductSKUDTO sku : productSKUDTOList) {
				SkuOrderStockDTO item = new SkuOrderStockDTO();
				item.setCtime(now);
				item.setUpTime(now);
				item.setStockCount(sku.getSkuNum());
				item.setSkuId(sku.getId());
				skuOrderStockList.add(item);
				skuIdList.add(sku.getId());
			}

			boolean flag = false;
			try {
				flag = skuOrderStockService.saveSkuOrderStockDTOColl(skuOrderStockList);
				if (!flag) {
					logger.error("Failured to call API: skuOrderStockService.saveSkuOrderStockDTOColl()!!!");
					return 0;
				}
			} catch (Exception e) {
				logger.error("Error occured when call API: skuOrderStockService.saveSkuOrderStockDTOColl()!!!", e);
				return -1;
			}

			int result = syncInventory(skuIdList);
			return result;
		} catch (ItemCenterException e) {
			logger.error(e);
			return -1;
		}
	}

	@Override
	public long addProductSKU(ProductSKUDTO productSKUDTO) {
		try {
			productSKUDTO = itemProductService.addProductSKU(productSKUDTO);
			long now = System.currentTimeMillis();
			if (productSKUDTO == null || productSKUDTO.getId() < 1l) {
				return 0l;
			} else {
				SkuOrderStockDTO item = new SkuOrderStockDTO();
				item.setCtime(now);
				item.setUpTime(now);
				item.setStockCount(productSKUDTO.getSkuNum());
				item.setSkuId(productSKUDTO.getId());
				boolean flag = false;
				try {
					flag = skuOrderStockService.addSkuOrderStockDTO(item);
					if (!flag) {
						logger.error("Failured to call API: skuOrderStockService.saveSkuOrderStockDTOColl()!!!");
						return -1l;
					}
				} catch (Exception e) {
					logger.error("Error occured when call API: skuOrderStockService.saveSkuOrderStockDTOColl()!!!", e);
					return -1l;
				}
				List<Long> skuIdList = new ArrayList<Long>(1);
				skuIdList.add(item.getSkuId());
				return syncInventory(skuIdList) == 1 ? productSKUDTO.getId() : -3l;
			}
		} catch (ItemCenterException e) {
			logger.error(e);
			return 0l;
		}
	}

	@Override
	public ProductSKUBackendVO getProductSKUVO(ProductSKUDTO skuDTO, boolean isGetAll) {
		skuDTO = itemProductService.getProductSKUDTO(skuDTO, isGetAll);
		if (skuDTO == null) {
			return null;
		}
		ProductSKUBackendVO productSKUBackendVO = new ProductSKUBackendVO(skuDTO);
		// 添加基本信息
		productSKUBackendVO = setSPUInfo(productSKUBackendVO);
		if (!isGetAll) {
			return productSKUBackendVO;
		}
		// 转换商品规格vo
		ProdSpeciBackendVO speciBackendVO = prodSpeciConvertToVO(skuDTO.getSpeciList(), skuDTO);
		List<ProdSpeciBackendVO> speciList = new ArrayList<ProdSpeciBackendVO>(1);
		speciList.add(speciBackendVO);
		productSKUBackendVO.setSpeciList(speciList);
		// 商品限购
		ProductSKULimitConfigDTO limitConfigDTO = productSKULimitService
				.getProductSKULimitConfigBySkuId(productSKUBackendVO.getSkuId());
		if (limitConfigDTO != null) {
			productSKUBackendVO.setSkuLimitConfigVO(new ProductSKULimitConfigVO(limitConfigDTO));
		}
		// 转换商品属性vo
		ItemModelDTO modelDTO = itemModelService.getItemModel(productSKUBackendVO.getCategoryNormalId(), false);
		if (modelDTO == null) {
			return productSKUBackendVO;
		}
		productSKUBackendVO.setParamList(prodParamDTOConvertToVO(skuDTO.getParamList(), modelDTO.getId()));
		return productSKUBackendVO;
	}

	/**
	 * 根据单品添加商品基本信息
	 * 
	 * @param productSKUBackendVO
	 * @return
	 */
	private ProductSKUBackendVO setSPUInfo(ProductSKUBackendVO productSKUBackendVO) {
		long spuId = productSKUBackendVO.getItemSPUId();
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setId(spuId);
		ItemSPUVO spuVO = itemSPUFacade.getItemSPU(spuDTO);
		if (spuVO == null) {
			return null;
		}
		productSKUBackendVO.setProdBarCode(spuVO.getSpuBarCode());
		productSKUBackendVO.setBrandName(spuVO.getBrandName());
		productSKUBackendVO.setCategoryFullName(categoryService.getFullCategoryNormalName(productSKUBackendVO
				.getCategoryNormalId()));
		return productSKUBackendVO;
	}

	/**
	 * 转换商品属性
	 * 
	 * @param paramDTOList
	 * @param itemModelId
	 * @return
	 */
	private List<ProdParamBackendVO> prodParamDTOConvertToVO(List<ProdParamDTO> paramDTOList, long itemModelId) {
		List<ModelParameterDTO> modelParamList = itemModelService.getModelParamList(itemModelId, 0);
		if (CollectionUtils.isEmpty(modelParamList)) {
			return null;
		} else {
			// 属性id-optionids map
			Map<Long, Set<Long>> paramIdMap = new HashMap<Long, Set<Long>>();
			long skuId = 0, prodParamId = 0;
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
				skuId = paramDTOList.get(0).getProductSKUId();
				prodParamId = paramDTOList.get(0).getId();
			}
			List<ProdParamBackendVO> retlList = new ArrayList<ProdParamBackendVO>(modelParamList.size());
			for (ModelParameterDTO modelParam : modelParamList) {
				if (CollectionUtils.isEmpty(modelParam.getModelParamOptionList())) {
					continue;
				}
				List<ProdParamOptionVO> optionList = new ArrayList<ProdParamOptionVO>(modelParam
						.getModelParamOptionList().size());
				// 商品属性选项
				Set<Long> optionIds = paramIdMap.get(modelParam.getId());
				for (ModelParamOptionDTO modelParamOption : modelParam.getModelParamOptionList()) {
					ProdParamOptionVO optionVO = new ProdParamOptionVO();
					optionVO.setParamOptionId(modelParamOption.getId());
					optionVO.setParamOption(modelParamOption.getOptionValue());
					if (!CollectionUtils.isEmpty(optionIds) && optionIds.contains(modelParamOption.getId())) {
						optionVO.setIsCheck(1);
					} else {
						optionVO.setIsCheck(2);
					}
					optionList.add(optionVO);
				}
				ProdParamBackendVO paramBackendVO = new ProdParamBackendVO();
				paramBackendVO.setParameterId(modelParam.getId());
				paramBackendVO.setParamName(modelParam.getName());
				paramBackendVO.setOptionList(optionList);
				paramBackendVO.setSingle(modelParam.getIsSingle());
				if (skuId > 0) {
					paramBackendVO.setSkuId(skuId);
				}
				if (prodParamId > 0) {
					paramBackendVO.setProdParamId(prodParamId);
				}
				retlList.add(paramBackendVO);
			}
			return retlList;
		}
	}

	/**
	 * 转换商品规格vo
	 * 
	 * @param speciList
	 * @return
	 */
	private ProdSpeciBackendVO prodSpeciConvertToVO(List<ProdSpeciDTO> speciList, ProductSKUDTO skuDTO) {
		ProdSpeciBackendVO speciBackendVO = new ProdSpeciBackendVO();
		speciBackendVO.setAttentionNum(skuDTO.getSkuAttention());
		speciBackendVO.setSkuId(skuDTO.getId());
		// 获取库存
		SkuOrderStockDTO stockDTO = skuOrderStockService.getSkuOrderStockDTOBySkuId(skuDTO.getId());
		speciBackendVO.setProductNum(stockDTO == null ? skuDTO.getSkuNum() : stockDTO.getStockCount());
		speciBackendVO.setProdInnerCode(skuDTO.getInnerCode());
		if (!CollectionUtils.isEmpty(speciList)) {
			Map<Long, Long> speciIdMap = new HashMap<Long, Long>(speciList.size());
			StringBuffer speciFullName = new StringBuffer();
			for (ProdSpeciDTO prodSpeciDTO : speciList) {
				long key = prodSpeciDTO.getModelSpeciId();
				long value = prodSpeciDTO.getModelSpeciOptionId();
				ModelSpeciOptionDTO option = itemModelService.getSpeciOption(value, key);
				if (option == null) {
					continue;
				}
				speciFullName.append(option.getOptionValue()).append("-");
				speciIdMap.put(key, value);
			}
			if (speciFullName.length() > 0) {
				speciFullName.deleteCharAt(speciFullName.length() - 1);
			}
			speciBackendVO.setSpeciIdMap(speciIdMap);
			speciBackendVO.setSpeciOptionName(speciFullName.toString());
		}
		return speciBackendVO;
	}

	@Override
	@Transaction
	public boolean updateProductSKUStatus(long prodId, String action, long modifyUserId) {
		ProductStatusType statusType = convertActionToStatusType(action);
		if (statusType.equals(ProductStatusType.NULL)) {
			return false;
		}
		if (itemProductService.updateProductSKUStatus(prodId, statusType, modifyUserId)) {
			if (statusType.equals(ProductStatusType.OFFLINE)) {
				productSKULimitService.clearLimitCache(prodId);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean batchUpdateProductSKUStatus(List<Long> prodIds, String action, long modifyUserId) {
		ProductStatusType statusType = convertActionToStatusType(action);
		if (statusType.equals(ProductStatusType.NULL) || CollectionUtil.isEmptyOfList(prodIds)) {
			return false;
		}
		return itemProductService.batchUpdateProductSKUStatus(prodIds, convertActionToStatusType(action), modifyUserId);
	}

	/**
	 * 商品操作动作转成对应商品状态
	 * 
	 * @param action
	 * @return
	 */
	private ProductStatusType convertActionToStatusType(String action) {
		ProductStatusType statusType = ProductStatusType.NULL;
		if (action.equals(OperateProductAction.SHELVE.getName())) {
			statusType = ProductStatusType.ONLINE;
		} else if (action.equals(OperateProductAction.UNSHELVE.getName())) {
			statusType = ProductStatusType.OFFLINE;
		} else if (action.equals(OperateProductAction.AUDIT.getName())) {
			statusType = ProductStatusType.ONLINE;// 目前审核不做，状态直接改成上架 TODO
		}
		return statusType;
	}

	@Override
	public int updateProductSKUStock(long prodId, int count, long businessId) {
		ProductSKUDTO productSKUDTO = new ProductSKUDTO();
		productSKUDTO.setId(prodId);
		productSKUDTO = itemProductService.getProductSKUDTO(productSKUDTO, false);
		if (productSKUDTO == null) {
			return 0;// 商品已被删除
		}
		if (productSKUDTO.getBusinessId() != businessId) {
			return -4;// 该商品不是该商家的
		}
		productSKUDTO.setSkuNum(count);
		productSKUDTO.setIsLimited(-1);
		return updateProductSKU(productSKUDTO, null);
	}

	@Override
	public Map<ProductStatusType, Integer> countProductSKUByBusinessId(long businessId) {
		return itemProductService.countProductSKUByBusinessId(businessId);
	}

	@Override
	public int deleteProdPic(long productSKUId, long id) {
		return itemProductService.deleteProdPic(productSKUId, id);
	}

	@Override
	@Transaction
	public int updateProductSKU(ProductSKUDTO productSKUDTO, ProductSKULimitConfigDTO limitConfigDTO) {
		try {
			int res = itemProductService.updateProductSKU(productSKUDTO);
			if (res > 0) {
				// 更新商品限购
				if (productSKUDTO.getIsLimited() == 1) {
					if (limitConfigDTO == null) {
						throw new ItemCenterException("Add productSKULimitConfig error! Param is null!");
					}
					boolean isSuccess = false;
					if (limitConfigDTO.getId() > 0l) {
						isSuccess = productSKULimitService.updateProductSKULimitConfig(limitConfigDTO);
					} else {
						try {
							isSuccess = productSKULimitService.addProductSKULimitConfig(limitConfigDTO);
						} catch (Exception e) {
							ProductSKULimitConfigDTO old = productSKULimitService
									.getProductSKULimitConfigBySkuId(limitConfigDTO.getProductSKUId());
							if (old == null) {
								throw e;
							}
							limitConfigDTO.setId(old.getId());
							isSuccess = productSKULimitService.updateProductSKULimitConfig(limitConfigDTO);
						}
					}
					if (!isSuccess) {
						throw new ItemCenterException("Add productSKULimitConfig failed!");
					}
				} else if (productSKUDTO.getIsLimited() != -1) {
					if (limitConfigDTO != null) {
						productSKULimitService.deleteProductSKULimitConfig(productSKUDTO.getId());
						productSKULimitService.deleteSKULimitRecordBySkuId(productSKUDTO.getId());
						productSKULimitService.clearLimitCache(productSKUDTO.getId());
					}
				}
				// 更新库存
				if (productSKUDTO.getSkuNum() >= 0) {
					List<SkuOrderStockDTO> skuOrderStockList = new ArrayList<SkuOrderStockDTO>();
					List<Long> skuIdList = new ArrayList<Long>();
					SkuOrderStockDTO item = new SkuOrderStockDTO();
					item.setStockCount(productSKUDTO.getSkuNum());
					item.setSkuId(productSKUDTO.getId());
					skuOrderStockList.add(item);
					skuIdList.add(productSKUDTO.getId());
					for (SkuOrderStockDTO skuOrderStockDTO : skuOrderStockList) {
						res = updateSkuOrderStock(skuOrderStockDTO.getSkuId(), skuOrderStockDTO.getStockCount());
					}
					if (res == -2) {
						return res;
					}
					res = syncInventory(skuIdList);
				}
			}
			return res;
		} catch (ItemCenterException e) {
			logger.error(e);
			return -1;
		}
	}

	/**
	 * 同步库存
	 * 
	 * @param skuOrderStockList
	 * @return
	 */
	private int syncInventory(List<Long> skuIdList) {
		try {
			boolean flag = syncInventoryHelper.processInnerSKU(skuIdList);
			if (!flag) {
				logger.error("Failured to call API: SyncInventoryHelper.inventoryPo()!!!" + skuIdList.toString());
				return -3;
			}
		} catch (Exception e) {
			logger.error("Error occured when call API: SyncInventoryHelper.inventoryPo()!!!" + skuIdList.toString(), e);
			return -3;
		}
		return 1;
	}

	/**
	 * 更新库存表
	 * 
	 * @param prodId
	 * @param count
	 * @return
	 */
	private int updateSkuOrderStock(long prodId, int count) {
		try {
			boolean flag = skuOrderStockService.updateSkuOrderStock(prodId, count);
			if (!flag) {
				logger.error("Failured to call API: skuOrderStockService.updateSkuOrderStock()! productId:" + prodId
						+ " count:" + count);
				return -2;
			}
		} catch (Exception e) {
			logger.error("Error occured when call API: skuOrderStockService.updateSkuOrderStock()! productId:" + prodId
					+ " count:" + count, e);
			return -2;
		}
		return 1;
	}

	@Override
	public int updateProdParam(List<ProdParamDTO> addList, List<ProdParamDTO> delList) {
		return itemProductService.updateProdParam(addList, delList);
	}

	@Override
	public List<ProductSKUBackendVO> getProductSKUVO(List<Long> prodIds) {
		List<ProductSKUDTO> productSKUDTOs = itemProductService.getProductSKUDTOByProdIds(prodIds);
		if (CollectionUtil.isEmptyOfList(productSKUDTOs)) {
			return null;
		}
		// 取商家信息
		Set<Long> businessIdSet = new HashSet<Long>();
		for (ProductSKUDTO productSKU : productSKUDTOs) {
			businessIdSet.add(productSKU.getBusinessId());
		}
		List<BusinessDTO> businessDTOs = businessService.getBusinessDTOListByIdList(new ArrayList<Long>(businessIdSet));
		Map<Long, BusinessDTO> businessMap = Maps.uniqueIndex(businessDTOs, new Function<BusinessDTO, Long>() {
			public Long apply(BusinessDTO businessDTO) {
				return businessDTO.getId();
			}
		});

		List<ProductSKUBackendVO> productSKUBackendVOs = new ArrayList<ProductSKUBackendVO>();
		BusinessDTO businessDTO = null;
		ProductSKUDetailDTO detailDTO = null;
		;
		for (ProductSKUDTO productSKUDTO : productSKUDTOs) {
			businessDTO = businessMap.get(productSKUDTO.getBusinessId());
			if (businessDTO == null) {
				continue;
			}
			productSKUDTO.setStoreName(businessDTO.getStoreName());
			productSKUDTO.setBatchCash(businessDTO.getBatchCash());
			// 商品详情url不转成内容，返回url
			detailDTO = productSKUDTO.getDetailDTO();
			productSKUDTO.setDetailDTO(null);
			ProductSKUBackendVO productSKUBackendVO = new ProductSKUBackendVO(productSKUDTO);
			productSKUBackendVO.setProdBarCode(productSKUDTO.getBarCode());
			productSKUBackendVO.setProdDetail(convertDetailTOVO(detailDTO));
			List<ProdSpeciDTO> prodSpeciDTOs = productSKUDTO.getSpeciList();
			if (CollectionUtil.isNotEmptyOfList(prodSpeciDTOs)) {
				List<ProdSpeciBackendVO> prodSpeciBackendVOs = new ArrayList<ProdSpeciBackendVO>();

				// 组装规格值
				for (ProdSpeciDTO prodSpeciDTO : prodSpeciDTOs) {
					long speciId = prodSpeciDTO.getModelSpeciId();
					long speciOptionId = prodSpeciDTO.getModelSpeciOptionId();
					ModelSpecificationDTO specificationDTO = itemModelService.getModelSpecification(speciId);
					if (specificationDTO == null) {
						continue;
					}
					ModelSpeciOptionDTO option = itemModelService.getSpeciOption(speciOptionId, speciId);
					if (option == null) {
						continue;
					}
					ProdSpeciBackendVO prodSpeciBackendVO = new ProdSpeciBackendVO();
					prodSpeciBackendVO.setSpecificationName(specificationDTO.getName());
					prodSpeciBackendVO.setType(specificationDTO.getType());
					prodSpeciBackendVO.setSpeciOptionName(option.getOptionValue());
					prodSpeciBackendVOs.add(prodSpeciBackendVO);
				}
				productSKUBackendVO.setSpeciList(prodSpeciBackendVOs);
			}
			productSKUBackendVOs.add(productSKUBackendVO);
		}
		return productSKUBackendVOs;
	}

	private ProductSKUDetailVO convertDetailTOVO(ProductSKUDetailDTO dto) {
		ProductSKUDetailVO productSKUDetailVO = new ProductSKUDetailVO();
		productSKUDetailVO.setDetailId(dto.getId());
		productSKUDetailVO.setSkuId(dto.getProductSKUId());
		productSKUDetailVO.setProdParamJson(dto.getProdParam());
		productSKUDetailVO.setCreateTime(dto.getCreateTime());
		productSKUDetailVO.setUpdateTime(dto.getUpdateTime());
		productSKUDetailVO.setCustomEditHTML(dto.getCustomEditHTML());
		return productSKUDetailVO;
	}

	@Override
	public int countProdParamOptionInUse(long paramId, long paramOptionId) {
		return itemProductService.countProdParamOptionInUse(paramId, paramOptionId);
	}

	@Override
	public int countProdSpeciOptionInUse(long speciId, long speciOptionId) {
		return itemProductService.countProdSpeciOptionInUse(speciId, speciOptionId);
	}

	@Override
	public int countProdParamInUse(long paramId) {
		return itemProductService.countProdParamOptionInUse(paramId, 0);
	}

	@Override
	public int countProdSpeciInUse(long speciId) {
		return itemProductService.countProdSpeciOptionInUse(speciId, 0);
	}

	@Override
	public int countProductSKUBySPUId(long spuId) {
		return itemProductService.countProductSKUBySPUId(spuId);
	}

	@Override
	public int countProductSKUDTOBySearchParam(ProductSKUSearchParam param) {
		return itemProductService.countProductSKUDTOBySearchParam(param);
	}

	@Override
	public ProductSKULimitConfigVO getProductSKULimitConfig(long skuId) {
		ProductSKULimitConfigDTO skuLimitConfigDTO = productSKULimitService.getProductSKULimitConfigBySkuId(skuId);
		if (skuLimitConfigDTO == null) {
			return null;
		}
		return new ProductSKULimitConfigVO(skuLimitConfigDTO);
	}

	@Override
	public boolean recoverOrderSkuLimit(long userId, Map<Long, OrderSkuDTO> orderSkuMap) {
		Map<Long, ProductSKULimitConfigDTO> skuLimitConfigMap = productSKULimitService.getProductSKULimitConfigDTOMap(
				userId, new ArrayList<Long>(orderSkuMap.keySet()));
		Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
		ProductSKULimitConfigDTO productSKULimitConfigDTO = null;
		long nowTime = System.currentTimeMillis();
		for (OrderSkuDTO orderSkuDTO : orderSkuMap.values()) {
			productSKULimitConfigDTO = skuLimitConfigMap.get(orderSkuDTO.getSkuId());
			// 不限购或者不在限购时间里不加回购买数量
			if (productSKULimitConfigDTO == null || productSKULimitConfigDTO.getStartTime() > nowTime
					|| productSKULimitConfigDTO.getEndTime() < nowTime) {
				continue;
			}
			// 大于限购数量
			if (productSKULimitConfigDTO.getAllowedNum() + orderSkuDTO.getTotalCount() > productSKULimitConfigDTO
					.getLimitNumber()) {
				continue;
			}
			ProductSKULimitRecordDTO limitRecordDTO = productSKULimitService.getProductSKULimitRecordNoCache(userId,
					orderSkuDTO.getSkuId());
			// 判断是否在购买周期
			long endTime = limitRecordDTO.getCreateTime() + (int) productSKULimitConfigDTO.getPeriod()
					* DateUtil.ONE_DAY_MILLISECONDS;
			// 不在购买周期里
			if (limitRecordDTO == null || orderSkuDTO.getCreateTime().getTime() < limitRecordDTO.getCreateTime()
					|| endTime < nowTime) {
				continue;
			}
			skuCountMap.put(orderSkuDTO.getSkuId(), -orderSkuDTO.getTotalCount());

		}
		int result = productSKULimitService.batchChangeProductSKULimitRecords(userId, skuCountMap);
		return result > 0 ? true : false;
	}

	@Override
	public int countSyncSKUBySPU(ItemSPU spu) {
		return itemProductService.countSyncSKUBySPU(spu);
	}

	@Override
	public List<Long> getSyncSKUIdBySPU(ItemSPU spu) {
		return itemProductService.getSyncSKUIdBySPU(spu);
	}

	@Override
	public int syncSKUByIds(String ids, ItemSPU spu) {
		return itemProductService.syncSKUByIds(ids, spu);
	}
}
