package com.xyl.mmall.backend.facade.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.backend.facade.POItemFacade;
import com.xyl.mmall.backend.util.ConvertUtil;
import com.xyl.mmall.backend.vo.BatchUploadPOProd;
import com.xyl.mmall.backend.vo.CategoryVO;
import com.xyl.mmall.backend.vo.ExportPoSkuVO;
import com.xyl.mmall.backend.vo.POAddSkuVO;
import com.xyl.mmall.backend.vo.POSkuVO;
import com.xyl.mmall.backend.vo.PoProductSearchVO;
import com.xyl.mmall.backend.vo.PoProductSortVO;
import com.xyl.mmall.backend.vo.PoProductVO;
import com.xyl.mmall.backend.vo.ProductEditVO;
import com.xyl.mmall.backend.vo.SizeHeaderVO;
import com.xyl.mmall.backend.vo.SizeTemplateArchitect;
import com.xyl.mmall.backend.vo.SizeVO;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.dto.BaseSkuDTO;
import com.xyl.mmall.itemcenter.dto.CategoryGroupDTO;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductSearchResultDTO;
import com.xyl.mmall.itemcenter.enums.PictureType;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.enums.SortType;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.meta.SizeColumn;
import com.xyl.mmall.itemcenter.meta.SizeTemplate;
import com.xyl.mmall.itemcenter.meta.Sku;
import com.xyl.mmall.itemcenter.param.BatchGetPoSkuParam;
import com.xyl.mmall.itemcenter.param.POProductSearchParam;
import com.xyl.mmall.itemcenter.param.PoAddProductReqVO;
import com.xyl.mmall.itemcenter.param.PoDeleteProdVO;
import com.xyl.mmall.itemcenter.param.PoSkuSo;
import com.xyl.mmall.itemcenter.param.ProductAddPoVO;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.SizeTable;
import com.xyl.mmall.itemcenter.param.SkuAddPoVO;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.itemcenter.service.POSizeService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.itemcenter.util.CategoryComparator;
import com.xyl.mmall.itemcenter.util.ConstantsUtil;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.itemcenter.util.ProductComparator;
import com.xyl.mmall.mainsite.vo.PoProductListSearchVO;
import com.xyl.mmall.mainsite.vo.ProductListProductVO;
import com.xyl.mmall.mainsite.vo.ProductListSkuVO;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.enums.SupplyMode;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.SchedulePageService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@Facade
public class POItemFacadeImpl extends PISItemCenterFacadeAbstract implements POItemFacade {
	private static final Logger logger = LoggerFactory.getLogger(POItemFacadeImpl.class);

	private static final Map<String, Boolean> LOCK = new HashMap<String, Boolean>();

	@Resource
	private POProductService poProductService;

	@Resource
	private POSizeService poSizeService;

	@Resource
	private ProductService productService;

	@Resource
	private SizeTemplateService sizeTemplateService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private SchedulePageService schedulePageService;

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private BrandService brandService;

	@Resource
	private CartService cartService;

	@Resource(name = "poCommonFacade")
	private ItemCenterCommonFacade poCommonFacade;

	public BaseJsonVO searchProduct(PoProductSearchVO searchVO, long supplierId) {
		try {
			POProductSearchParam param = ConvertUtil.convertProdSearchVOToDTO(searchVO);
			param.setSupplierId(supplierId);
			if (searchVO.getPage() == 0) {
				POAddSkuVO list = searchAllTabProduct(param);
				BaseJsonVO retObj = new BaseJsonVO(list);
				retObj.setCode(ErrorCode.SUCCESS);
				return retObj;
			} else {
				POAddSkuVO result = searchAddTabProduct(param);
				BaseJsonVO retObj = new BaseJsonVO(result);
				retObj.setCode(ErrorCode.SUCCESS);
				return retObj;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	private POAddSkuVO searchAllTabProduct(POProductSearchParam param) {
		long supplierId = param.getSupplierId();
		long poId = param.getPoId();
		ScheduleDTO scheduleDTO = scheduleService.getScheduleById(poId).getScheduleDTO();
		SupplyMode mode = scheduleDTO.getScheduleVice().getSupplyMode();
		POAddSkuVO retVO = new POAddSkuVO();
		retVO.setScheduleType(mode.getIntValue());
		BaseSearchResult<ProductSearchResultDTO> searchResult = poProductService.searchProduct(param);
		List<ProductSearchResultDTO> dtoList = searchResult.getList();
		List<PoProductVO> cList = new ArrayList<PoProductVO>();

		// 把批量操作改成单个获取。merge保留此处
		for (ProductSearchResultDTO c : dtoList) {
			/** raw */
			PoProductVO vo = (PoProductVO) ItemCenterUtil.extractData(c, PoProductVO.class);
			long productId = c.getId();
			List<Sku> skuList = productService.getSkuList(supplierId, productId);

			SizeType sizeType = c.getSizeType();
			long templatekey = 0;
			if (sizeType == SizeType.CUST_SIZE)
				templatekey = productId;
			else if (sizeType == SizeType.TMPL_SIZE)
				templatekey = c.getSizeTemplateId();
			String goodsNo = c.getGoodsNo();
			List<POSkuVO> skuVOlist = new ArrayList<POSkuVO>();
			SizeTable table = null;
			if (sizeType != SizeType.ORIG_SIZE)
				table = sizeTemplateService.getSizeTable(templatekey, sizeType);
			for (Sku sku : skuList) {
				POSkuVO skuVO = new POSkuVO();
				skuVO.setBarCode(sku.getBarCode());
				skuVO.setSkuId(String.valueOf(sku.getId()));
				if (sizeType == SizeType.ORIG_SIZE)
					skuVO.setSize(ConstantsUtil.DEFAULT_SIZE);
				else {
					StringBuffer sizeVal = new StringBuffer();
					long index = sku.getSizeIndex();
					String key1 = index + "+" + ConstantsUtil.CHIMA_COLUMN;
					String size = table.getValueMap().get(key1);
					if (size == null)
						size = "";
					sizeVal.append(size);
					String key2 = index + "+" + ConstantsUtil.HAOXING_COLUMN;
					String haoxing = table.getValueMap().get(key2);
					if (!StringUtils.isBlank(haoxing)) {
						sizeVal.append("(").append(haoxing).append(")");
					}
					skuVO.setSize(sizeVal.toString());
				}
				String barCode = sku.getBarCode();
				BatchGetPoSkuParam skuParam = new BatchGetPoSkuParam();
				skuParam.setSkuId(sku.getId());
				skuParam.setGoodsNo(goodsNo);
				skuParam.setPoId(poId);
				skuParam.setBarCode(barCode);
				PoSku poSku = poProductService.getPoSku(poId, goodsNo, barCode);
				if (poSku == null) {
					skuVO.setAddStatus(POSkuVO.NOT_ADD);
					skuVO.setReviewStatus(StatusType.NOTSUBMIT.getIntValue());
					skuVO.setStatusName(StatusType.NOTSUBMIT.getDesc());
					skuVO.setReviewText("");
					// if (!isSkuInOtherPO(supplierId, sku.getBarCode(),
					// scheduleDTO.getSchedule())) {
					// skuVO.setAddStatus(POSkuVO.NOT_ADD);
					// skuVO.setReviewStatus(StatusType.NOTSUBMIT.getIntValue());
					// skuVO.setStatusName(StatusType.NOTSUBMIT.getDesc());
					// skuVO.setReviewText("");
					// } else {
					// skuVO.setAddStatus(POSkuVO.ADD_OTHER);
					// skuVO.setReviewStatus(StatusType.NOTSUBMIT.getIntValue());
					// skuVO.setStatusName(StatusType.NOTSUBMIT.getDesc());
					// skuVO.setReviewText("");
					// }
				} else {
					skuVO.setAddStatus(POSkuVO.HAS_ADD);
					skuVO.setStatusName(poSku.getStatus().getDesc());
					skuVO.setReviewStatus(poSku.getStatus().getIntValue());
					skuVO.setReviewText(poSku.getRejectReason());
					skuVO.setNum(poSku.getSkuNum());
					skuVO.setSupplyNum(poSku.getSupplierSkuNum());
				}
				skuVOlist.add(skuVO);
			}
			vo.setSkuList(skuVOlist);
			cList.add(vo);
		}
		retVO.setList(cList);
		retVO.setHasNext(searchResult.isHasNext());
		retVO.setTotal(searchResult.getTotal());
		POAddSkuVO addResult = searchAddTabProduct(param);
		retVO.setSkuTotal(addResult.getSkuTotal());
		return retVO;
	}

	private boolean isSkuInOtherPO(long supplierId, String barCode, Schedule currPO) {
		long curStart = currPO.getStartTime();
		long curEnd = currPO.getEndTime();
		List<Long> poList = poProductService.getPOByBarCode(supplierId, barCode);
		if (poList != null && poList.size() > 0) {
			List<Schedule> scheduleList = scheduleService.getScheduleByIdList(poList);
			boolean isInOther = false;
			for (Schedule po : scheduleList) {
				if (po == null)
					break;
				long tmpStart = po.getStartTime();
				long tmpEnd = po.getEndTime();
				if (tmpStart >= curEnd)
					continue;
				else if (tmpEnd <= curStart)
					continue;
				else {
					isInOther = true;
					break;
				}
			}
			return isInOther;
		} else
			return false;
	}

	private POAddSkuVO searchAddTabProduct(POProductSearchParam param) {
		POAddSkuVO retVO = new POAddSkuVO();
		long poId = param.getPoId();
		List<POProductDTO> plist = poProductService.getProductDTOListByPo(poId);
		ScheduleDTO scheduleDTO = scheduleService.getScheduleById(poId).getScheduleDTO();
		List<Category> list = null;
		if (param.getLowCategoryId() > 0)
			list = categoryService.getLowestCategoryById(param.getLowCategoryId());
		SupplyMode mode = scheduleDTO.getScheduleVice().getSupplyMode();
		retVO.setScheduleType(mode.getIntValue());
		List<POProductDTO> dtoList = new ArrayList<POProductDTO>();
		if (plist != null && plist.size() > 0) {
			for (POProductDTO p : plist) {
				if (!StringUtils.isBlank(param.getGoodsNo()) && !param.getGoodsNo().equals(p.getGoodsNo()))
					continue;
				else if (!StringUtils.isBlank(param.getProductName())
						&& !param.getProductName().equals(p.getProductName()))
					continue;
				else if (param.getLowCategoryId() > 0 && !isProductInCategory(list, p.getLowCategoryId()))
					continue;
				else if (StringUtils.isBlank(param.getBarCode())) {
					List<POSkuDTO> skuList = (List<POSkuDTO>) p.getSKUList();
					if (skuList != null && skuList.size() > 0) {
						for (int i = 0; i < skuList.size(); i++) {
							POSkuDTO sku = skuList.get(i);
							StatusType prodState = p.getStatus();
							StatusType skuState = sku.getStatus();
							StatusType finalState = getStaus(prodState, skuState);
							if (param.getStatus() != null && param.getStatus() != StatusType.NULL
									&& param.getStatus() != finalState) {
								skuList.remove(i);
								i--;
								continue;
							}

						}
						if (skuList.size() > 0)
							dtoList.add(p);
					}
				} else {
					List<POSkuDTO> skuList = (List<POSkuDTO>) p.getSKUList();
					if (skuList != null && skuList.size() > 0) {
						for (int i = 0; i < skuList.size(); i++) {
							POSkuDTO sku = skuList.get(i);
							StatusType prodState = p.getStatus();
							StatusType skuState = sku.getStatus();
							StatusType finalState = getStaus(prodState, skuState);
							if (param.getStatus() != StatusType.NULL && param.getStatus() != finalState) {
								skuList.remove(i);
								i--;
								continue;
							}
							if (!param.getBarCode().equals(sku.getBarCode())) {
								skuList.remove(i);
								i--;
								continue;
							}
						}
						if (skuList.size() > 0)
							dtoList.add(p);
					}
				}
			}
		}

		int skuNum = 0;
		if (dtoList != null && dtoList.size() > 0) {
			for (POProductDTO p : dtoList) {
				List<POSkuDTO> skuList = (List<POSkuDTO>) p.getSKUList();
				skuNum = skuNum + skuList.size();
			}
		}
		retVO.setSkuTotal(skuNum);
		int offset = param.getOffset();
		int limit = param.getLimit();
		int totalSize = dtoList.size();
		boolean hasNext = false;
		if (totalSize + 1 > offset + limit)
			hasNext = true;
		List<POProductDTO> resultList = null;
		if (hasNext && dtoList != null && dtoList.size() > 0)
			resultList = dtoList.subList(offset, offset + limit);
		else if (dtoList != null && dtoList.size() > 0)
			resultList = dtoList.subList(offset, totalSize);
		else
			resultList = new ArrayList<POProductDTO>();
		List<PoProductVO> cList = new ArrayList<PoProductVO>();
		for (POProductDTO c : resultList) {
			StatusType prodState = c.getStatus();
			PoProductVO vo = (PoProductVO) ItemCenterUtil.extractData(c, PoProductVO.class);
			vo.setReviewStatus(c.getStatus().getIntValue());
			List<POSkuDTO> skuList = (List<POSkuDTO>) c.getSKUList();
			List<POSkuVO> skuVOlist = new ArrayList<POSkuVO>();
			for (POSkuDTO sku : skuList) {
				POSkuVO skuVO = new POSkuVO();
				skuVO.setBarCode(sku.getBarCode());
				skuVO.setSkuId(String.valueOf(sku.getId()));
				skuVO.setSize(sku.getSize());
				skuVO.setAddStatus(POSkuVO.HAS_ADD);
				skuVO.setReviewStatus(sku.getStatus().getIntValue());
				skuVO.setNum(sku.getSkuNum());
				skuVO.setSupplyNum(sku.getSupplierSkuNum());
				StatusType skuState = sku.getStatus();
				StatusType finalState = getStaus(prodState, skuState);
				skuVO.setStatusName(finalState.getDesc());
				if (finalState == StatusType.REJECT) {
					if (!StringUtils.isBlank(c.getRejectReason()) && !StringUtils.isBlank(sku.getRejectReason()))
						skuVO.setReviewText(c.getRejectReason() + ";" + sku.getRejectReason());
					else
						skuVO.setReviewText(c.getRejectReason() + sku.getRejectReason());
				}
				skuVOlist.add(skuVO);
			}
			vo.setSkuList(skuVOlist);
			cList.add(vo);
		}

		retVO.setList(cList);
		retVO.setHasNext(hasNext);
		retVO.setTotal(totalSize);
		return retVO;
	}

	private boolean isProductInCategory(List<Category> list, long pCatetoryId) {
		boolean isIn = false;
		if (list != null && list.size() > 0) {
			for (Category c : list) {
				if (c.getId() == pCatetoryId) {
					isIn = true;
					break;
				}
			}
		}
		return isIn;
	}

	private StatusType getStaus(StatusType superState, StatusType subState) {
		if (superState == StatusType.REJECT || subState == StatusType.REJECT)
			return StatusType.REJECT;
		else if (superState == StatusType.APPROVAL && subState == StatusType.APPROVAL)
			return StatusType.APPROVAL;
		else if (superState == StatusType.NOTSUBMIT && subState == StatusType.NOTSUBMIT)
			return StatusType.NOTSUBMIT;
		else
			return StatusType.PENDING;
	}

	@Override
	public BaseJsonVO addProductToPo(PoAddProductReqVO reqVO) {
		long supplierId = reqVO.getSupplierId();
		String key = "addProductToPo_" + supplierId;
		while (LOCK.get(key) != null && LOCK.get(key)) {
			logger.info("supplierId::" + key + " locked!!! wait");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("supplierId::" + key + " unlocked!!! continue");
		}
		LOCK.put(key, true);
		try {
			long poId = reqVO.getPoId();

			StringBuffer sb = new StringBuffer();
			List<ProductAddPoVO> list = reqVO.getData();
			if (list != null && list.size() > 0) {
				for (ProductAddPoVO vo : list) {
					String goodsNo = vo.getGoodsNo();
					String colorNum = vo.getColorNum();
					List<SkuAddPoVO> skuList = vo.getSkuList();
					if (skuList != null && skuList.size() > 0) {
						for (int i = 0; i < skuList.size(); i++) {
							SkuAddPoVO svo = skuList.get(i);
							String barCode = svo.getBarCode();
							if (svo.getAddNum() < 0 || svo.getSupplyAddNum() < 0) {
								skuList.remove(i);
								i--;
								sb.append("货号：" + goodsNo + "，色号：" + colorNum + "，条形码：" + barCode + " 添加失败；");
								continue;
							}
							if (svo.getAddNum() + svo.getSupplyAddNum() <= 0) {
								skuList.remove(i);
								i--;
								sb.append("货号：" + goodsNo + "，色号：" + colorNum + "，条形码：" + barCode + " 添加失败；");
								continue;
							}
						}
					}
					if (skuList != null && skuList.size() > 0) {
						try {
							poProductService.addProductToPo(supplierId, poId, vo);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							sb.append("货号：" + goodsNo + "，色号：" + colorNum + " 添加失败；");
						}
					}
				}
			}
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.SUCCESS);
			retObj.setMessage(sb.toString());
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		} finally {
			LOCK.remove(key);
		}
	}

	@Override
	public BaseJsonVO deleteProductFromPo(PoDeleteProdVO reqVO) {
		try {
			poProductService.deleteProductFromPo(reqVO);
			List<Long> poIds = new ArrayList<Long>();
			poIds.add(reqVO.getPoId());

			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public BaseJsonVO saveProduct(ProductSaveParam productSaveParam) {
		try {
			poCommonFacade.operaProductSaveParam(productSaveParam);
			poProductService.saveProduct(productSaveParam);
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public ProductEditVO getProductVO(long poId, long productId) {
		try {
			PoProductFullDTO productDTO = poProductService.getProductFullDTO(productId);
			ProductEditVO vo = genProductVO(productDTO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", String.valueOf(productDTO.getPoId()));
			vo.setSchedule(map);
			return vo;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public BaseJsonVO getPoCategory(long poId, boolean isCache) {
		try {
			BaseJsonListResultVO retVO = new BaseJsonListResultVO();
			List<CategoryGroupDTO> prodResult = null;
			if (isCache)
				prodResult = poProductService.getProductGroupByCategoryCache(poId);
			else
				prodResult = poProductService.getProductGroupByCategory(poId);
			Collections.sort(prodResult, new CategoryComparator(true));
			List<CategoryVO> clist = new ArrayList<CategoryVO>();
			for (CategoryGroupDTO p : prodResult) {
				CategoryVO categoryVO = new CategoryVO();
				long categoryId = p.getLowCategoryId();
				Category category = categoryService.getCategoryById(categoryId);
				categoryVO.setId(String.valueOf(category.getId()));
				categoryVO.setName(category.getName());
				categoryVO.setCount(p.getTotal());
				clist.add(categoryVO);
			}
			retVO.setList(clist);
			retVO.setHasNext(false);
			retVO.setTotal(clist.size());

			BaseJsonVO retObj = new BaseJsonVO(retVO);
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public BaseJsonVO sortPoProduct(PoProductSortVO param) {
		try {
			long poId = param.getScheduleId();
			int type = param.getType();
			schedulePageService.updatePOPagePrdListOrderType(poId, type);
			if (type == 1) {
				List<Long> sortList = param.getSeq();
				poProductService.sortProductByCategory(poId, sortList);
			} else if (type == 2) {
				List<Long> sortList = param.getSeq();
				poProductService.sortProductBySingle(poId, sortList);
			}
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public BaseJsonListResultVO getProductList(PoProductListSearchVO param) {
		try {
			BaseJsonListResultVO retVO = new BaseJsonListResultVO();
			BaseJsonListResultVO result = getPoProductDTOList(param);
			List<ProductListProductVO> list = new ArrayList<ProductListProductVO>();
			List<POProductDTO> retList = (List<POProductDTO>) result.getList();
			if (retList != null && retList.size() > 0) {
				List<Long> skuIdList = getSkuIdList(retList);
				if (skuIdList != null && skuIdList.size() > 0) {
					BrandDTO brand = null;
					for (POProductDTO product : retList) {
						ProductListProductVO pvo = new ProductListProductVO();
						pvo.setId(String.valueOf(product.getId()));
						pvo.setBrandId(String.valueOf(product.getBrandId()));
						pvo.setThumb(product.getShowPicPath());
						if (brand == null)
							brand = brandService.getBrandByBrandId(product.getBrandId());
						pvo.setBrandName(brand.getBrand().getBrandNameAuto());
						pvo.setColorName(product.getColorName());
						pvo.setListShowPicList(poProductService.getProductPicNoCache(product.getId(), PictureType.LIST));
						pvo.setMarketPrice(product.getMarketPrice());
						pvo.setProductName(product.getProductName());
						pvo.setSalePrice(product.getSalePrice());
						List<POSkuDTO> skulist = (List<POSkuDTO>) product.getSKUList();
						List<ProductListSkuVO> skuVOList = new ArrayList<ProductListSkuVO>();
						if (skulist != null && skulist.size() > 0) {
							for (POSkuDTO sku : skulist) {
								ProductListSkuVO skuVO = new ProductListSkuVO();
								skuVO.setId(sku.getId());
								String size = sku.getSize();
								int s = size.indexOf("(");
								int e = size.lastIndexOf(")");
								if (s > 0 && e > s) {
									skuVO.setSize(size.substring(0, s));
								} else
									skuVO.setSize(size);
								skuVO.setNum(sku.getSkuNum());
								skuVOList.add(skuVO);
							}
						}
						pvo.setSkuList(skuVOList);
						list.add(pvo);
					}
				}
			}
			retVO.setList(list);
			retVO.setHasNext(result.isHasNext());
			retVO.setTotal(result.getTotal());
			return retVO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public ScheduleDTO getScheduleDTO(long poId) {
		return scheduleService.getScheduleById(poId).getScheduleDTO();
	}

	@Override
	public Map<String, List<String>> batchUploadProductInfo(List<BatchUploadPOProd> retList, long supplierId,
			long poId, int poType) {
		Map<String, List<String>> retMsgMap = new HashMap<String, List<String>>();
		List<String> validCheck = new ArrayList<String>();
		List<String> dbMsg = new ArrayList<String>();

		Map<Long, ProductFullDTO> dbMap = new HashMap<Long, ProductFullDTO>();
		if (retList != null && retList.size() > 0) {
			for (BatchUploadPOProd obj : retList) {
				String barcode = obj.getBarCode();
				Sku sku = productService.getSku(supplierId, barcode);
				if (sku == null) {
					validCheck.add("第" + obj.getRowNum() + "行，条形码" + barcode + "商品在商品库中不存在。");
					continue;
				} else {
					ProductFullDTO rawProduct = dbMap.get(sku.getProductId());
					if (rawProduct == null) {
						rawProduct = productService.getProductFullDTOById(sku.getProductId());
						dbMap.put(sku.getProductId(), rawProduct);
					}
					if (rawProduct == null) {
						validCheck.add("第" + obj.getRowNum() + "行，条形码" + barcode + "商品在商品库中不存在。");
						continue;
					}
					PoSku poSku = poProductService.getSku(poId, barcode);
					if (poSku != null) {
						validCheck.add("第" + obj.getRowNum() + "行，条形码" + barcode + "已经在档期添加该商品，不能重复添加。");
						continue;
					}
					PoProduct poproduct = poProductService.getPoProduct(supplierId, poId, rawProduct.getGoodsNo(),
							rawProduct.getColorNum());
					if (poproduct == null) {
						poproduct = poProductService.copyProduct(rawProduct, poId);
					}
					poSku = (PoSku) ItemCenterUtil.extractData(sku, PoSku.class);
					poSku.setId(0);
					poSku.setPoId(poId);
					poSku.setProductId(poproduct.getId());
					poSku.setGoodsNo(rawProduct.getGoodsNo());
					poSku.setRejectReason("");
					poSku.setSkuNum(obj.getAddNum());
					if (poType == 2)
						poSku.setSupplierSkuNum(obj.getSupplyNum());
					else
						poSku.setSupplierSkuNum(0);
					poSku.setStatus(StatusType.NOTSUBMIT);
					poSku.setSupplierId(supplierId);
					poProductService.BatchAddProductToPo(poproduct, poSku, rawProduct.getId());
				}
			}
		}

		if (validCheck.size() > 0)
			retMsgMap.put("validMsg", validCheck);
		if (dbMsg.size() > 0)
			retMsgMap.put("saveDBMsg", dbMsg);
		return retMsgMap;
	}

	@Override
	public List<ExportPoSkuVO> getExportSkuVO(long supplierId, long poId) {
		List<ExportPoSkuVO> retList = new ArrayList<ExportPoSkuVO>();
		List<PoSku> list = poProductService.getSkuListByPo(poId);
		if (list != null && list.size() > 0) {
			for (PoSku sku : list) {
				ExportPoSkuVO skuVO = new ExportPoSkuVO();
				skuVO.setBarCode(sku.getBarCode());
				skuVO.setSkuNum(sku.getSkuNum());
				skuVO.setSupplyNum(sku.getSupplierSkuNum());
				retList.add(skuVO);
			}
		}
		return retList;
	}

	private BaseJsonListResultVO getPoProductDTOList(PoProductListSearchVO param) {
		BaseJsonListResultVO retVO = new BaseJsonListResultVO();
		List<POProductDTO> retList = new ArrayList<POProductDTO>();
		long poId = param.getScheduleId();
		List<POProductDTO> filterList = poProductService.getProductDTOListByPo(poId);
		BigDecimal priceFrom = param.getPriceFrom();
		if (priceFrom == null)
			priceFrom = new BigDecimal(0);
		BigDecimal priceTo = param.getPriceTo();
		if (priceTo == null)
			priceTo = new BigDecimal(0);
		long catgoryId = param.getCategoryId();
		List<POProductDTO> pList = new ArrayList<POProductDTO>();
		for (POProductDTO p : filterList) {
			if (p.getStatus() != StatusType.APPROVAL)
				continue;
			List<POSkuDTO> skuList = (List<POSkuDTO>) p.getSKUList();
			if (skuList == null || skuList.size() == 0)
				continue;
			boolean isApproval = true;
			for (POSkuDTO sku : skuList) {
				if (sku.getStatus() != StatusType.APPROVAL) {
					isApproval = false;
					break;
				}
			}
			if (!isApproval)
				continue;
			if (catgoryId == 0 || p.getLowCategoryId() == catgoryId) {
				BigDecimal salePrice = p.getSalePrice();
				if (priceFrom.intValue() == 0 && priceTo.intValue() == 0)
					pList.add(p);
				else if (priceFrom.intValue() > 0 && priceTo.intValue() > 0) {
					if (priceFrom.compareTo(salePrice) <= 0 && priceTo.compareTo(salePrice) >= 0)
						pList.add(p);
				} else {
					if (priceTo.compareTo(salePrice) >= 0)
						pList.add(p);
				}
			}
		}
		boolean hasNext = false;
		if (pList.size() > 0) {
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(2);
			nf.setRoundingMode(RoundingMode.UP);
			for (POProductDTO p : pList) {
				double sale = p.getSalePrice().doubleValue();
				double market = p.getMarketPrice().doubleValue();
				if (sale == 0) {
					p.setDiscount(new BigDecimal("0"));
				} else {
					double discount = sale / market;
					p.setDiscount(new BigDecimal(nf.format(discount)));
				}
			}
			boolean isAsc = !param.isDesc();
			int sort = param.getOrder();
			if (sort == 0) {
				SortType sortType = SortType.DEFAULT;
				int defaultSortType = schedulePageService.getPOPagePrdListOrderType(poId);
				Collections.sort(pList, new ProductComparator(isAsc, sortType, defaultSortType));
			} else if (sort == 1) {
				SortType sortType = SortType.PRICE;
				Collections.sort(pList, new ProductComparator(isAsc, sortType));
			} else if (sort == 2) {
				SortType sortType = SortType.SALE;
				Collections.sort(pList, new ProductComparator(isAsc, sortType));
			} else if (sort == 3) {
				SortType sortType = SortType.DISCOUNT;
				Collections.sort(pList, new ProductComparator(isAsc, sortType));
			}
			int limit = param.getLimit();
			int copyLimit = limit;
			int offset = param.getOffset();
			long lastId = param.getLastId();
			if (lastId > 0) {
				for (int i = 0; i < pList.size(); i++) {
					POProductDTO product = pList.get(i);
					if (product.getId() == lastId) {
						offset = i + 1;
						break;
					}
				}
			}
			if (offset <= pList.size() - 1) {
				for (int i = offset; i < pList.size(); i++) {
					POProductDTO product = pList.get(i);
					retList.add(product);
					copyLimit--;
					if (copyLimit == 0)
						break;
				}
			}
			if ((limit + offset) < pList.size())
				hasNext = true;
		}
		retVO.setList(retList);
		retVO.setHasNext(hasNext);
		retVO.setTotal(pList.size());
		return retVO;
	}

	private List<Long> getSkuIdList(List<POProductDTO> list) {
		List<Long> retList = new ArrayList<Long>();
		for (POProductDTO product : list) {
			List<? extends BaseSkuDTO> skulist = product.getSKUList();
			if (skulist != null && skulist.size() > 0) {
				retList.addAll(getSkuList(skulist));
			}
		}
		return retList;
	}

	private List<Long> getSkuList(List<? extends BaseSkuDTO> skuList) {
		List<Long> retList = new ArrayList<Long>();
		for (BaseSkuDTO sku : skuList) {
			retList.add(sku.getId());
		}
		return retList;
	}

	private List<SizeTemplateArchitect> getSizeTemplateList(long sizeTemplateId, long lowCategoryId, long supplierId) {
		List<SizeTemplateArchitect> sizeTmplAcht = getSizeTemplateList(lowCategoryId, supplierId);
		if (sizeTemplateId > 0) {
			SizeTemplate sizeTemplate = sizeTemplateService.getSizeTemplate(sizeTemplateId);
			SizeTemplateArchitect sta = getSizeTemplateArchitect(sizeTemplate.getId());
			sta.setId(String.valueOf(sizeTemplate.getId()));
			sta.setName(sizeTemplate.getTemplateName());
			sizeTmplAcht.add(sta);
		}
		return sizeTmplAcht;
	}

	@Override
	public List<Size> getSizeList(long sizeTemplateId, SizeType sizeType) {
		return poCommonFacade.getSizeList(sizeTemplateId, sizeType);
	}

	@Override
	public SizeVO genSizeVO(long productId, long lowCategoryId, long sizeTemplateId, long supplierId) {
		try {
			SizeVO sizeVO = new SizeVO();
			List<SizeTemplateArchitect> sizeTmplAcht = getSizeTemplateList(sizeTemplateId, lowCategoryId, supplierId);
			sizeVO.setSizeTemplate(sizeTmplAcht);

			List<SizeHeaderVO> headerVO = getCustomizeSizeHeaderVOList(productId, lowCategoryId);
			sizeVO.setSizeHeader(headerVO);
			return sizeVO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public List<PoSku> getPoSkuListByParam(PoSkuSo so) {
		return poProductService.getPoSkuListByParam(so);
	}

	@Override
	public SizeTable getSizeTable(long templatekey, SizeType sizeType) {
		return poSizeService.getSizeTable(templatekey, sizeType);
	}
}
