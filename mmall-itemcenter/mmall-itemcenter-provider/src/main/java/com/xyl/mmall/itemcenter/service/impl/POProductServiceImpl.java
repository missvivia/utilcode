package com.xyl.mmall.itemcenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.itemcenter.dao.category.CategoryDao;
import com.xyl.mmall.itemcenter.dao.product.PoProductDao;
import com.xyl.mmall.itemcenter.dao.product.PoProductDetailDao;
import com.xyl.mmall.itemcenter.dao.product.ProductDao;
import com.xyl.mmall.itemcenter.dao.product.ProductParamOptDao;
import com.xyl.mmall.itemcenter.dao.product.ProductPicDao;
import com.xyl.mmall.itemcenter.dao.size.CustomizedSizeDao;
import com.xyl.mmall.itemcenter.dao.size.CustomizedSizeValueDao;
import com.xyl.mmall.itemcenter.dao.size.SizeColumnDao;
import com.xyl.mmall.itemcenter.dao.size.SizeTemplateDao;
import com.xyl.mmall.itemcenter.dao.size.TemplateSizeDao;
import com.xyl.mmall.itemcenter.dao.size.TemplateSizeValueDao;
import com.xyl.mmall.itemcenter.dao.sku.PoSkuDao;
import com.xyl.mmall.itemcenter.dao.sku.SkuDao;
import com.xyl.mmall.itemcenter.dao.sku.SkuSpecMapDao;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.dto.CategoryGroupDTO;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoProductFullDTO;
import com.xyl.mmall.itemcenter.dto.PoProductVo;
import com.xyl.mmall.itemcenter.dto.PoSkuVo;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductSearchResultDTO;
import com.xyl.mmall.itemcenter.dto.ScheduleAuditData;
import com.xyl.mmall.itemcenter.enums.PictureType;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.CustomizedSize;
import com.xyl.mmall.itemcenter.meta.CustomizedSizeValue;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.meta.PoProductDetail;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.meta.ProdPicMap;
import com.xyl.mmall.itemcenter.meta.Product;
import com.xyl.mmall.itemcenter.meta.SizeTemplate;
import com.xyl.mmall.itemcenter.meta.Sku;
import com.xyl.mmall.itemcenter.meta.SkuSpecMap;
import com.xyl.mmall.itemcenter.meta.TemplateSize;
import com.xyl.mmall.itemcenter.meta.TemplateSizeValue;
import com.xyl.mmall.itemcenter.param.POProductSearchParam;
import com.xyl.mmall.itemcenter.param.PoDeleteProdVO;
import com.xyl.mmall.itemcenter.param.PoProductSo;
import com.xyl.mmall.itemcenter.param.PoSkuSo;
import com.xyl.mmall.itemcenter.param.ProductAddPoVO;
import com.xyl.mmall.itemcenter.param.ProductDltPoVO;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.SkuAddPoVO;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.itemcenter.service.POSizeService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.itemcenter.util.ConstantsUtil;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;

@Service
public class POProductServiceImpl implements POProductService {
	private static final Logger logger = LoggerFactory.getLogger(POProductServiceImpl.class);

	@Autowired
	private ProductDao productDao;

	@Autowired
	private PoProductDetailDao poDetailDao;

	@Autowired
	private PoProductDao poProductDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private ProductPicDao prodPicDao;

	@Autowired
	private PoSkuDao poSkuDao;

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private CustomizedSizeDao customizedSizeDao;

	@Autowired
	private CustomizedSizeValueDao customizedSizeValueDao;

	@Autowired
	private SizeTemplateDao sizeTemplateDao;

	@Autowired
	private TemplateSizeValueDao templateSizeValueDao;

	@Autowired
	private TemplateSizeDao templateSizeDao;

	@Autowired
	private POSizeService poSizeService;

	@Autowired
	private SizeTemplateService sizeTemplateService;

	@Autowired
	private SizeColumnDao sizeColumnDao;

	@Autowired
	private SkuSpecMapDao skuSpecMapDao;

	@Autowired
	private ProductParamOptDao prodParamOptDao;

	@Autowired
	private ProductService productService;

	@Override
	public BaseSearchResult<ProductSearchResultDTO> searchProduct(POProductSearchParam searchDTO) {
		try {
			BaseSearchResult<Product> prodSearchResult = productDao.searchProduct(searchDTO);
			List<Product> productList = prodSearchResult.getList();
			List<ProductSearchResultDTO> retList = new ArrayList<ProductSearchResultDTO>();
			for (Product product : productList) {
				ProductSearchResultDTO dto = new ProductSearchResultDTO(product);
				Category cate = categoryDao.getCategoryById(dto.getLowCategoryId());
				dto.setCategoryName(cate.getName());
				String showPath = product.getShowPicPath();
				if (!StringUtils.isBlank(showPath) && showPath.indexOf(ConstantsUtil.NOS_URL2) < 0)
					showPath = ConstantsUtil.NOS_URL1 + showPath;
				dto.setShowPicPath(showPath);
				retList.add(dto);
			}
			BaseSearchResult<ProductSearchResultDTO> ret = new BaseSearchResult<ProductSearchResultDTO>();
			ret.setList(retList);
			ret.setHasNext(prodSearchResult.isHasNext());
			ret.setTotal(prodSearchResult.getTotal());
			return ret;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public BaseSearchResult<POProductDTO> searchAddPOProduct(POProductSearchParam searchDTO) {
		try {
			List<PoProduct> list = poProductDao.getProduct(searchDTO.getPoId());
			List<POProductDTO> resultList = new ArrayList<POProductDTO>();
			if (list != null && list.size() > 0) {
				List<Category> categoryList = new ArrayList<Category>();
				categoryDao.getLowestCategoryById(categoryList, searchDTO.getLowCategoryId());
				for (PoProduct product : list) {
					if (!StringUtils.isBlank(searchDTO.getProductName())
							&& !product.getProductName().equals(searchDTO.getProductName()))
						continue;
					if (!StringUtils.isBlank(searchDTO.getGoodsNo())
							&& !product.getGoodsNo().equals(searchDTO.getGoodsNo()))
						continue;
					if (categoryList.size() > 0 && searchDTO.getLowCategoryId() > 0) {
						boolean isIn = false;
						for (Category c : categoryList) {
							if (c.getId() == searchDTO.getLowCategoryId()) {
								isIn = true;
								break;
							}
						}
						if (!isIn)
							continue;
					}
					POProductDTO dto = new POProductDTO(product);
					Category cate = categoryDao.getCategoryById(dto.getLowCategoryId());
					dto.setCategoryName(cate.getName());
					resultList.add(dto);
				}

			}
			return ItemCenterUtil.genSearchResult(resultList, searchDTO.getOffset(), searchDTO.getLimit());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	/**
	 * 方法优化 (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.itemcenter.service.POProductService#getSkuDTOListBySkuId(java.util.List)
	 */
	@Override
	public List<POSkuDTO> getSkuDTOListBySkuId(List<Long> skuIds) {
		try {
			if (skuIds == null || skuIds.size() == 0) {
				return new ArrayList<POSkuDTO>();
			} else {
				// youhua
				return poSkuDao.getPoSkuList(skuIds);
				// return this.youhuaForgetSkuDTOListBySkuId(skuIds);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	private List<POSkuDTO> youhuaForgetSkuDTOListBySkuId(List<Long> skuIds) {
		List<POSkuDTO> poSkuDTOList = new ArrayList<>();
		for (Long skuId : skuIds) {
			POSkuDTO poSkuDTO = poProductDao.getPoSkuDTOByIdCache(skuId);
			if (poSkuDTO != null) {
				poSkuDTOList.add(poSkuDTO);
			}
		}
		return poSkuDTOList;
	}

	@Override
	public List<PoSku> getSkuListByPo(long poId) {
		try {
			return poSkuDao.getPoSkuListByPo(poId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<POSkuDTO> getSkuDTOListByPo(long poId) {
		try {
			return poSkuDao.getPoSkuDTOListByPo(poId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	// @CacheEvict(value = { "productDaoCache", "poProductCache", "skuDaoCache",
	// "prodPicDaoCache", "prodSizeCache", "poskuCache" }, allEntries = true)
	public void addProductToPo(long supplierId, long poId, ProductAddPoVO vo) {
		try {
			List<SkuAddPoVO> skuList = vo.getSkuList();
			if (skuList != null && skuList.size() > 0) {
				long rawProductId = vo.getProductId();
				ProductFullDTO rawProduct = productService.getProductFullDTO(rawProductId);
				String goodsNo = rawProduct.getGoodsNo();
				String colorNum = rawProduct.getColorNum();
				PoProductFullDTO productDTO = getProductFullDTO(supplierId, poId, goodsNo, colorNum);
				if (productDTO == null) {
					productDTO = new PoProductFullDTO(rawProduct);
					productDTO.setId(0);
					productDTO.setPoId(poId);
					productDTO.setProductId(rawProductId);
					productDTO.setRejectReason("");
					productDTO.setStatus(StatusType.NOTSUBMIT);
					productDTO.setAddTime(ItemCenterUtil.getTime());
					productDTO.setUTime(ItemCenterUtil.getTime());
				}

				long pid = productDTO.getId();
				if (pid <= 0) {
					PoProduct product = new PoProduct();
					ItemCenterUtil.copyData(productDTO, product);

					if (product.getSizeType() == SizeType.CUST_SIZE) {
						poProductDao.addObject(product);
						long productId = product.getId();
						copyCustomizedSize(rawProductId, productId);
					} else if (product.getSizeType() == SizeType.TMPL_SIZE) {
						long templateId = rawProduct.getSizeTemplateId();
						long newTemplateId = copyTemplateSize(templateId);
						product.setSizeTemplateId(newTemplateId);
						poProductDao.addObject(product);
					} else {
						poProductDao.addObject(product);
					}

					pid = product.getId();
					PoProductDetail detail = new PoProductDetail();
					ItemCenterUtil.copyData(productDTO, detail);
					detail.setProductId(pid);
					detail.setPoId(poId);
					poDetailDao.addObject(detail);
					List<String> showPicList = productDTO.getProdShowPicList();
					saveProductPic(pid, PictureType.PROD, showPicList);

					List<String> listPicList = productDTO.getListShowPicList();
					saveProductPic(pid, PictureType.LIST, listPicList);
				}

				for (SkuAddPoVO skuAddvo : skuList) {

					Sku sku = skuDao.getObjectById(skuAddvo.getSkuId());
					PoSku poSku = poSkuDao.getSku(poId, sku.getBarCode());
					if (poSku == null) {
						poSku = (PoSku) ItemCenterUtil.extractData(sku, PoSku.class);
						poSku.setId(0);
						poSku.setPoId(poId);
						poSku.setProductId(pid);
						poSku.setGoodsNo(goodsNo);
						poSku.setRejectReason("");
						poSku.setSkuNum(skuAddvo.getAddNum());
						poSku.setSupplierSkuNum(skuAddvo.getSupplyAddNum());
						poSku.setStatus(StatusType.NOTSUBMIT);
						poSku.setSupplierId(supplierId);
						poSkuDao.saveObject(poSku);
						if (productDTO.getSizeType() == SizeType.CUST_SIZE) {
							copyCustomizedSizeValue(productDTO.getProductId(), sku.getSizeIndex(), pid, poSku.getId());
						}
						long templateId = 0;
						long sizeId = poSku.getSizeIndex();
						if (productDTO.getSizeType() == SizeType.CUST_SIZE) {
							templateId = pid;
						} else if (productDTO.getSizeType() == SizeType.TMPL_SIZE) {
							templateId = productDTO.getSizeTemplateId();
						} else {
							templateId = sizeTemplateService.getOriginalSizeId(productDTO.getLowCategoryId());
						}
						StringBuffer sizeVal = new StringBuffer();
						if (productDTO.getSizeType() == SizeType.ORIG_SIZE) {
							sizeVal.append("均码");
						} else {
							SizeValue sizeValue = poSizeService.getSizeValue(templateId, ConstantsUtil.CHIMA_COLUMN,
									sizeId, productDTO.getSizeType());
							if (sizeValue == null)
								sizeVal.append("");
							sizeVal.append(sizeValue.getValue());

							sizeValue = poSizeService.getSizeValue(templateId, ConstantsUtil.HAOXING_COLUMN, sizeId,
									productDTO.getSizeType());
							if (sizeValue != null) {
								sizeVal.append("(").append(sizeValue.getValue()).append(")");
							}
						}
						SkuSpecMap specMap = new SkuSpecMap();
						specMap.setProductId(pid);
						specMap.setSkuId(poSku.getId());
						specMap.setSkuSpecId(1);
						specMap.setValue(sizeVal.toString());
						specMap.setViewOrder(poSku.getSizeIndex());
						specMap.setPoId(poId);
						skuSpecMapDao.addNewSkuSpecMap(specMap);
					} else {
						poSku.setSkuNum(skuAddvo.getAddNum());
						poSku.setSupplierSkuNum(skuAddvo.getSupplyAddNum());
						poSkuDao.saveObject(poSku);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	// @CacheEvict(value = { "productDaoCache", "poProductCache",
	// "prodPicDaoCache" }, allEntries = true)
	public PoProduct copyProduct(ProductFullDTO rawProduct, long poId) {
		PoProduct product = new PoProduct();
		ItemCenterUtil.copyData(rawProduct, product);
		product.setId(0);
		product.setPoId(poId);
		product.setProductId(rawProduct.getId());
		product.setRejectReason("");
		product.setStatus(StatusType.NOTSUBMIT);
		product.setAddTime(ItemCenterUtil.getTime());
		product.setUTime(ItemCenterUtil.getTime());

		if (product.getSizeType() == SizeType.CUST_SIZE) {
			poProductDao.addObject(product);
			long productId = product.getId();
			copyCustomizedSize(rawProduct.getId(), productId);
		} else if (product.getSizeType() == SizeType.TMPL_SIZE) {
			long templateId = rawProduct.getSizeTemplateId();
			long newTemplateId = copyTemplateSize(templateId);
			product.setSizeTemplateId(newTemplateId);
			poProductDao.addObject(product);
		} else {
			poProductDao.addObject(product);
		}
		long productId = product.getId();
		PoProductDetail detail = new PoProductDetail();
		ItemCenterUtil.copyData(rawProduct, detail);
		detail.setId(0);
		detail.setPoId(poId);
		detail.setProductId(productId);
		poDetailDao.addObject(detail);
		copyProdPic(rawProduct.getId(), productId);
		return product;
	}

	@Override
	@Transaction
	// @CacheEvict(value = { "productDaoCache", "poProductCache", "skuDaoCache",
	// "prodPicDaoCache", "prodSizeCache", "poskuCache" }, allEntries = true)
	public void BatchAddProductToPo(PoProduct product, PoSku poSku, long rawProductId) {
		long poId = poSku.getPoId();
		String barCode = poSku.getBarCode();
		long productId = poSku.getProductId();
		PoSku existSku = poSkuDao.getSku(poId, barCode);
		if (existSku != null) {
			poSkuDao.deleteById(existSku.getId());
			poSizeService.deleteCustomizeSizeValue(productId, poSku.getSizeIndex());
		}
		poSkuDao.addObject(poSku);
		if (product.getSizeType() == SizeType.CUST_SIZE) {
			copyCustomizedSizeValue(rawProductId, poSku.getSizeIndex(), productId, poSku.getId());
		}
		long templateId = 0;
		if (product.getSizeType() == SizeType.CUST_SIZE) {
			templateId = product.getId();
		} else if (product.getSizeType() == SizeType.TMPL_SIZE) {
			templateId = product.getSizeTemplateId();
		} else {
			templateId = sizeTemplateService.getOriginalSizeId(product.getLowCategoryId());
		}
		long sizeId = poSku.getSizeIndex();
		StringBuffer sizeVal = new StringBuffer();
		if (product.getSizeType() == SizeType.ORIG_SIZE) {
			sizeVal.append("均码");
		} else {
			SizeValue sizeValue = poSizeService.getSizeValue(templateId, ConstantsUtil.CHIMA_COLUMN, sizeId,
					product.getSizeType());
			if (sizeValue == null)
				sizeVal.append("");
			sizeVal.append(sizeValue.getValue());

			sizeValue = poSizeService.getSizeValue(templateId, ConstantsUtil.HAOXING_COLUMN, sizeId,
					product.getSizeType());
			if (sizeValue != null) {
				sizeVal.append("(").append(sizeValue.getValue()).append(")");
			}
		}
		SkuSpecMap specMap = new SkuSpecMap();
		specMap.setProductId(productId);
		specMap.setSkuId(poSku.getId());
		specMap.setSkuSpecId(1);
		specMap.setValue(sizeVal.toString());
		specMap.setPoId(poId);
		specMap.setViewOrder(poSku.getSizeIndex());
		skuSpecMapDao.addNewSkuSpecMap(specMap);
	}

	@Override
	@Transaction
	// @CacheEvict(value = { "productDaoCache", "poProductCache", "skuDaoCache",
	// "prodPicDaoCache", "prodSizeCache", "poskuCache" }, allEntries = true)
	public void deleteProductFromPo(PoDeleteProdVO req) {
		try {
			long poId = req.getPoId();
			List<ProductDltPoVO> pList = req.getData();
			for (ProductDltPoVO prodVO : pList) {
				long productId = prodVO.getProductId();
				PoProduct product = poProductDao.getObjectById(productId);
				if (product.getIsOnline() == 1) {
					List<Long> skuList = prodVO.getSkuList();
					for (long skuId : skuList) {
						poSkuDao.setSkuDeleteFlag(poId, skuId, 1);
					}
					List<PoSku> list = poSkuDao.getPoSkuListNonCache(productId);
					if (list == null || list.size() == 0) {
						poProductDao.setProductDeleteFlag(poId, productId, 1);
					}
				} else {
					SizeType sizeType = product.getSizeType();
					List<Long> skuList = prodVO.getSkuList();
					for (long skuId : skuList) {
						PoSku sku = poSkuDao.getObjectById(skuId);
						if (sku.getStatus() != StatusType.APPROVAL) {
							if (sizeType == SizeType.CUST_SIZE) {
								customizedSizeValueDao.deleteCustomizedSizeValue(productId, sku.getSizeIndex(),
										ConstantsUtil.IN_PO);
							}
							poSkuDao.deleteById(skuId);
							skuSpecMapDao.deleteBySkuId(skuId);
						}
					}
					List<PoSku> list = poSkuDao.getPoSkuListNonCache(productId);
					if (list == null || list.size() == 0) {
						customizedSizeDao.deleteCustomizedSize(productId, ConstantsUtil.IN_PO);
						long sizeTemplateId = product.getSizeTemplateId();
						if (sizeTemplateId > 0) {
							templateSizeValueDao.deleteTemplateSizeValue(sizeTemplateId);
							templateSizeDao.deleteTemplateSizeByTemplId(sizeTemplateId);
							sizeTemplateDao.deleteById(sizeTemplateId);
						}
						prodPicDao.deleteProdPicMap(productId, ConstantsUtil.IN_PO);
						poProductDao.deleteById(productId);
						poDetailDao.deleteObject(poId, productId);
					}
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	// @CacheEvict(value = { "productDaoCache", "poProductCache",
	// "prodPicDaoCache" }, allEntries = true)
	public void saveProduct(ProductSaveParam productParam) {
		try {
			long pid = productParam.getId();
			PoProduct product = poProductDao.getObjectById(pid);
			long poId = product.getPoId();
			PoProductDetail detail = poDetailDao.getPoProductDetail(poId, pid);
			long detailId = detail.getId();
			ItemCenterUtil.copyData(productParam, product);
			ItemCenterUtil.copyData(productParam, detail);
			detail.setId(detailId);

			if (productParam.getSizeType() == 1) {
				product.setSizeType(SizeType.ORIG_SIZE);
			} else if (productParam.getSizeType() == 2) {
				product.setSizeType(SizeType.TMPL_SIZE);
			} else {
				product.setSizeType(SizeType.CUST_SIZE);
			}
			detail.setCustomEditHTML(productParam.getNosURL());
			List<String> showPicList = productParam.getProdShowPicList();
			List<String> listPicList = productParam.getListShowPicList();
			if (showPicList != null && showPicList.size() > 0) {
				String thumb = showPicList.get(0);
				product.setShowPicPath(thumb);
			}
			// 保存产品主信息
			poProductDao.saveObject(product);
			poDetailDao.saveObject(detail);
			// 保存图片信息
			// prodPicDao.deleteProdPicMap(pid, ConstantsUtil.IN_PO);

			saveProductPic(pid, PictureType.PROD, showPicList);

			saveProductPic(pid, PictureType.LIST, listPicList);

			// save price info
			List<PoSku> poskuList = poSkuDao.getPoSkuList(pid);
			if (poskuList != null && poskuList.size() > 0) {
				for (PoSku sku : poskuList) {
					sku.setUTime(System.currentTimeMillis());
					sku.setMarketPrice(product.getMarketPrice());
					sku.setBasePrice(product.getBasePrice());
					sku.setSalePrice(product.getSalePrice());
				}
				poSkuDao.saveObjects(poskuList);
			}

			// List<SizeColumnParam> colList = productParam.getSizeHeader();
			// if ((colList == null || colList.size() == 0) &&
			// productParam.getSizeType() == 3) {
			// throw new ServiceException("customized size is not correct!");
			// }
			// if (colList != null && colList.size() > 0 &&
			// productParam.getSizeType() == 3) {
			// saveCustomizedSize(pid, colList);
			// poSizeService.deleteCustomizeSizeValue(pid);
			// }

			// 保存sku
			// List<SkuSaveParam> skuList = productParam.getSKUList();
			// if (skuList != null && skuList.size() > 0) {
			// List<PoSku> poSkuList = new ArrayList<PoSku>();
			// for (int i = 1; i <= skuList.size(); i++) {
			// SkuSaveParam skuParam = skuList.get(i - 1);
			// int recordIndex = skuParam.getSizeIndex();
			// if (product.getSizeType() == SizeType.CUST_SIZE) {
			// if (recordIndex <= 0) {
			// recordIndex = i;
			// skuParam.setSizeIndex(recordIndex);
			// }
			// }
			// PoSku sku = saveSku(skuParam, product,
			// productParam.getBasePrice(), productParam.getMarketPrice(),
			// productParam.getSalePrice());
			// if (sku.getSizeIndex() < 0)
			// sku.setSizeIndex(i);
			// poSkuList.add(sku);
			// }
			// poSkuDao.deleteSkuByProductId(pid);
			// for (int i = 0; i < poSkuList.size(); i++) {
			// PoSku sku = poSkuList.get(i);
			// SkuSaveParam skuParam = skuList.get(i);
			//
			// poSkuDao.addObject(sku);
			// long skuId = sku.getId();
			// if (productParam.getSizeType() == 3) {
			// List<String> sizeValue = skuParam.getCustomizedSizeValue();
			// saveCustomizedSizeValue(pid, sku.getSizeIndex(), colList,
			// sizeValue);
			// }
			// int recordIndex = sku.getSizeIndex();
			// if (recordIndex <= 0)
			// recordIndex = i;
			// SkuSpecMap map = new SkuSpecMap();
			// map.setProductId(pid);
			// map.setSkuId(skuId);
			// map.setSkuSpecId(1);
			// map.setViewOrder(recordIndex);
			//
			// if (productParam.getSizeType() == 1) {
			// long lowCategoryId = productParam.getLowCategoryId();
			// long oid = sizeTemplateService.getOriginalSizeId(lowCategoryId);
			// String val = poSizeService.getSizePrimaryValue(oid, recordIndex,
			// SizeType.ORIG_SIZE);
			// map.setValue(val);
			// } else if (productParam.getSizeType() == 2) {
			// String val = poSizeService.getSizePrimaryValue(sid, recordIndex,
			// SizeType.TMPL_SIZE);
			// map.setValue(val);
			// } else if (productParam.getSizeType() == 3) {
			// List<String> sizeValue = skuParam.getCustomizedSizeValue();
			// String val = ItemCenterUtil.getSpecOptionValue(colList,
			// sizeValue);
			// map.setValue(val);
			// }
			// skuSpecMapDao.addNewSkuSpecMap(map);
			// }
			// }
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	private boolean saveProductPic(long pid, PictureType type, List<String> pathList) {
		try {
			ProdPicMap ppMap = prodPicDao.getProdPicMapNoCache(pid, type, ConstantsUtil.IN_PO);
			ProdPicMap retMap = ItemCenterUtil.genProdPicMap(pathList);
			if (retMap != null) {
				if (ppMap != null) {
					ppMap.setPicPath(retMap.getPicPath());
					ppMap.setNosVersion(retMap.getNosVersion());
					ppMap.setUTime(new Date().getTime());
				} else {
					ppMap = new ProdPicMap();
					ppMap.setIsInPo(ConstantsUtil.IN_PO);
					ppMap.setPicType(type);
					ppMap.setProductId(pid);
					ppMap.setCTime(new Date().getTime());
					ppMap.setPicPath(retMap.getPicPath());
					ppMap.setNosVersion(retMap.getNosVersion());
					ppMap.setUTime(new Date().getTime());
					ppMap.setCTime(new Date().getTime());
				}
				prodPicDao.saveObject(ppMap);
				return true;
			} else
				return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	// @CacheEvict(value = { "productDaoCache", "poProductCache" }, allEntries =
	// true)
	public void updateProductStaus(StatusType status, String reason, String descp, List<Long> productIds) {
		try {
			if (productIds != null && productIds.size() > 0)
				poProductDao.updateProductStaus(status, reason, descp, productIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	// @CacheEvict(value = { "skuDaoCache", "poskuCache" }, allEntries = true)
	public void updateSkuStaus(StatusType status, String reason, List<Long> skuIds) {
		try {
			if (skuIds != null && skuIds.size() > 0)
				poSkuDao.updateSkuStaus(status, reason, skuIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public PoProductFullDTO getProductFullDTO(long supplierId, long poId, String goodsNo, String colorNum) {
		try {
			PoProductFullDTO dto = null;
			PoProduct p = poProductDao.getPoProduct(supplierId, poId, goodsNo, colorNum);
			if (p != null) {
				long pid = p.getId();
				PoProductDetail detail = poDetailDao.getPoProductDetail(poId, pid);
				if (detail != null) {
					dto = new PoProductFullDTO(p, detail);
					Category cate = categoryDao.getCategoryById(p.getLowCategoryId());
					dto.setCategoryName(cate.getName());
					dto.setSKUList(poSkuDao.getPoSkuDTOListByProductId(pid));

					List<ProdPicMap> picMapList = prodPicDao.getProdPicMap(pid, ConstantsUtil.IN_PO);

					if (picMapList != null && picMapList.size() > 0) {
						for (ProdPicMap picMap : picMapList) {
							List<String> picList = ItemCenterUtil.genProdPicPath(picMap);
							if (picMap.getPicType() == PictureType.LIST) {
								dto.setListShowPicList(picList);
							} else {
								dto.setProdShowPicList(picList);
							}
						}
					}
					return dto;
				}
			}
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public PoProduct getPoProduct(long supplierId, long poId, String goodsNo, String colorNum) {
		try {
			return poProductDao.getPoProduct(supplierId, poId, goodsNo, colorNum);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public PoProduct getPoProduct(long poId, long pid) {
		try {
			return poProductDao.getPoProductByPoIdAndProduct(pid, poId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public PoProduct getPoProduct(long pid) {
		try {
			return poProductDao.getObjectById(pid);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public PoSku getPoSku(long poId, String goodsNo, String barCode) {
		try {
			return poSkuDao.getSku(poId, goodsNo, barCode);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	private void copyCustomizedSize(long rawProductId, long productId) {
		List<CustomizedSize> sizeList = customizedSizeDao.getCustomizedSizeList(rawProductId, ConstantsUtil.NOT_IN_PO);
		for (CustomizedSize size : sizeList) {
			size.setId(0);
			size.setIsInPo(ConstantsUtil.IN_PO);
			size.setProductId(productId);
			customizedSizeDao.addObject(size);
		}
	}

	private void copyCustomizedSizeValue(long rawProductId, long sizeId, long productId, long skuId) {
		List<CustomizedSizeValue> list = customizedSizeValueDao.getCustomizedSizeValueList(rawProductId, sizeId,
				ConstantsUtil.NOT_IN_PO);
		for (CustomizedSizeValue value : list) {
			value.setId(0);
			value.setIsInPo(ConstantsUtil.IN_PO);
			value.setProductId(productId);
			customizedSizeValueDao.addObject(value);
		}
	}

	private long copyTemplateSize(long templateSizeId) {
		SizeTemplate temlate = sizeTemplateDao.getObjectById(templateSizeId);
		temlate.setIsInPo(ConstantsUtil.IN_PO);
		temlate.setId(0);
		sizeTemplateDao.addNewSizeTemplate(temlate);
		long newTemplateId = temlate.getId();
		List<TemplateSize> sizeList = templateSizeDao.getTemplateSizeList(templateSizeId);
		for (TemplateSize size : sizeList) {
			size.setSizeTemplateId(newTemplateId);
			templateSizeDao.addNewTemplateSize(size);
		}
		List<TemplateSizeValue> sizeValueList = templateSizeValueDao.getTemplateSizeValueList(templateSizeId);
		for (TemplateSizeValue sizeValue : sizeValueList) {
			sizeValue.setSizeTemplateId(newTemplateId);
			templateSizeValueDao.addNewTemplateSizeValue(sizeValue);
		}
		return newTemplateId;
	}

	private void copyProdPic(long rawProductId, long productId) {
		List<ProdPicMap> list = prodPicDao.getProdPicMap(rawProductId, ConstantsUtil.NOT_IN_PO);
		for (ProdPicMap map : list) {
			map.setId(0);
			map.setProductId(productId);
			map.setIsInPo(ConstantsUtil.IN_PO);
			prodPicDao.addNewProdPicMap(map);
		}
	}

	@Override
	@Cacheable(value = "searchProdById")
	public POProductDTO getProductDTO(long productId) {
		try {
			POProductDTO dto = null;
			PoProduct p = poProductDao.getObjectById(productId);
			if (p != null) {
				dto = new POProductDTO(p);
				Category cate = categoryDao.getCategoryById(p.getLowCategoryId());
				dto.setCategoryName(cate.getName());
				dto.setSKUList(poSkuDao.getPoSkuDTOListByProductId(productId));

				List<ProdPicMap> picMapList = prodPicDao.getProdPicMap(productId, ConstantsUtil.IN_PO);

				if (picMapList != null && picMapList.size() > 0) {
					for (ProdPicMap picMap : picMapList) {
						List<String> picList = ItemCenterUtil.genProdPicPath(picMap);
						if (picMap.getPicType() == PictureType.LIST) {
							dto.setListShowPicList(picList);
						} else {
							dto.setProdShowPicList(picList);
						}
					}
				}
				return dto;
			}
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Cacheable(value = "searchProdById")
	public PoProductFullDTO getProductFullDTOForMainSite(long productId) {
		try {
			PoProductFullDTO dto = null;
			PoProduct p = poProductDao.getObjectById(productId);
			if (p != null) {
				long poId = p.getPoId();
				long pid = p.getId();
				PoProductDetail detail = poDetailDao.getPoProductDetail(poId, pid);
				if (detail != null) {
					dto = new PoProductFullDTO(p, detail);
					Category cate = categoryDao.getCategoryById(p.getLowCategoryId());
					dto.setCategoryName(cate.getName());
					dto.setSKUList(poSkuDao.getPoSkuDTOListByProductId(productId));

					List<ProdPicMap> picMapList = prodPicDao.getProdPicMap(productId, ConstantsUtil.IN_PO);

					if (picMapList != null && picMapList.size() > 0) {
						for (ProdPicMap picMap : picMapList) {
							List<String> picList = ItemCenterUtil.genProdPicPath(picMap);
							if (picMap.getPicType() == PictureType.LIST) {
								dto.setListShowPicList(picList);
							} else {
								dto.setProdShowPicList(picList);
							}
						}
					}
					return dto;
				}

			}
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public PoProductFullDTO getProductFullDTO(long productId) {
		try {
			PoProductFullDTO dto = null;
			PoProduct p = poProductDao.getObjectById(productId);
			if (p != null) {
				long poId = p.getPoId();
				long pid = p.getId();
				PoProductDetail detail = poDetailDao.getPoProductDetail(poId, pid);
				if (detail != null) {
					dto = new PoProductFullDTO(p, detail);
					Category cate = categoryDao.getCategoryById(p.getLowCategoryId());
					dto.setCategoryName(cate.getName());
					dto.setSKUList(poSkuDao.getPoSkuDTOListByProductId(productId));

					List<ProdPicMap> picMapList = prodPicDao.getProdPicMap(productId, ConstantsUtil.IN_PO);

					if (picMapList != null && picMapList.size() > 0) {
						for (ProdPicMap picMap : picMapList) {
							List<String> picList = ItemCenterUtil.genProdPicPath(picMap);
							if (picMap.getPicType() == PictureType.LIST) {
								dto.setListShowPicList(picList);
							} else {
								dto.setProdShowPicList(picList);
							}
						}
					}
					return dto;
				}

			}
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public boolean isProductInPO(long po) {
		try {
			List<PoProduct> list = poProductDao.getProduct(po);
			if (list == null || list.size() == 0)
				return false;
			else
				return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	private boolean isProductAllPass(long po) {
		int count = poProductDao.getProduct(po).size();
		if (count > 0) {
			List<PoProduct> list = poProductDao.getProductExceptPass(po);
			if (list == null || list.size() == 0)
				return true;
			else
				return false;
		} else
			return false;
	}

	private boolean isSkuAllPass(long po) {
		int count = poSkuDao.getSkuCountInPo(po);
		if (count > 0) {
			List<PoSku> list = poSkuDao.getSkuExceptPass(po);
			if (list == null || list.size() == 0)
				return true;
			else
				return false;
		} else
			return false;
	}

	@Transaction
	@Override
	// @CacheEvict(value = { "productDaoCache", "poProductCache", "skuDaoCache"
	// }, allEntries = true)
	public void submitProduct(long po) {
		try {
			poProductDao.submitProduct(po);
			poSkuDao.submitSku(po);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Cacheable(value = "sameGoodsNoCache")
	public List<PoProduct> getProductListByGoodsNo(long poId, String goodsNo) {
		try {
			List<PoProduct> retList = new ArrayList<PoProduct>();
			List<PoProduct> productList = poProductDao.getProduct(poId);
			if (productList != null && productList.size() > 0) {
				for (PoProduct product : productList) {
					if (product.getGoodsNo().equals(goodsNo))
						retList.add(product);
				}
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Cacheable(value = "searchProdByPO")
	public List<CategoryGroupDTO> getProductGroupByCategoryCache(long poId) {
		try {
			return poProductDao.getProductGroupByCategory(poId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<CategoryGroupDTO> getProductGroupByCategory(long poId) {
		try {
			return poProductDao.getProductGroupByCategory(poId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	// @CacheEvict(value = "productDaoCache", allEntries = true)
	public void sortProductByCategory(long poId, List<Long> sortList) {
		try {
			if (sortList != null && sortList.size() != 0) {
				for (int i = 1; i <= sortList.size(); i++) {
					long categoryId = sortList.get(i - 1);
					poProductDao.updateProductCategorySort(poId, categoryId, i);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	// @CacheEvict(value = "productDaoCache", allEntries = true)
	public void sortProductBySingle(long poId, List<Long> sortList) {
		try {
			poProductDao.resetProductSingleSort(poId);
			for (int i = 1; i <= sortList.size(); i++) {
				long pid = sortList.get(i - 1);
				poProductDao.updateProductSingleSort(poId, pid, i);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<PoProduct> getProductListByPo(long poId) {
		try {
			return poProductDao.getProduct(poId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<POProductDTO> getProductDTOListByPo(long poId) {
		try {
			List<POProductDTO> list = poProductDao.getProductDTOListByPo(poId);
			if (list != null && list.size() > 0) {
				for (POProductDTO dto : list) {
					Category cate = categoryDao.getCategoryById(dto.getLowCategoryId());
					dto.setCategoryName(cate.getName());
					String showPath = dto.getShowPicPath();
					dto.setShowPicPath(ItemCenterUtil.genProdPicPath(showPath));
				}
			}
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Cacheable(value = "searchProdByPO")
	public List<POProductDTO> getProductDTOListByPoCache(long poId) {
		try {
			List<POProductDTO> list = poProductDao.getProductDTOListByPo(poId);
			if (list != null && list.size() > 0) {
				for (POProductDTO dto : list) {
					Category cate = categoryDao.getCategoryById(dto.getLowCategoryId());
					dto.setCategoryName(cate.getName());
					String showPath = dto.getShowPicPath();
					dto.setShowPicPath(ItemCenterUtil.genProdPicPath(showPath));
				}
			}
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<POProductDTO> getProductDTOList(long poId, List<Long> pids) {
		try {
			List<POProductDTO> list = poProductDao.getProductDTOListByPo(poId);
			List<POProductDTO> retList = new ArrayList<POProductDTO>();
			if (pids != null && pids.size() > 0) {
				for (long pid : pids) {
					for (POProductDTO dto : list) {
						if (dto.getId() == pid) {
							List<ProdPicMap> picMapList = prodPicDao.getProdPicMap(pid, ConstantsUtil.IN_PO);
							if (picMapList != null && picMapList.size() > 0) {
								for (ProdPicMap picMap : picMapList) {
									List<String> picList = ItemCenterUtil.genProdPicPath(picMap);
									if (picMap.getPicType() == PictureType.LIST) {
										dto.setListShowPicList(picList);
									} else {
										dto.setProdShowPicList(picList);
									}
								}
							}
							retList.add(dto);
						}
					}
				}
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<Integer> getProductCount(List<Long> poIds, int sameAsShop) {
		try {
			List<Integer> count = new ArrayList<Integer>();
			for (Long id : poIds) {
				int c = poProductDao.getProductCount(id, sameAsShop);
				count.add(c);
			}
			return count;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public ScheduleAuditData getItemScheduleAuditData(long poId) {
		try {
			int skuNum = 0;
			int itemNum = 0;
			int productNum = 0;
			List<PoSku> skuList = poSkuDao.getPoSkuListByPo(poId);
			List<PoSku> skuPassList = new ArrayList<PoSku>();
			List<PoSku> skuNotPassList = new ArrayList<PoSku>();
			if (skuList != null && skuList.size() > 0) {
				for (PoSku sku : skuList) {
					if (sku.getStatus() == StatusType.APPROVAL)
						skuPassList.add(sku);
					else
						skuNotPassList.add(sku);
				}
			}

			if (skuPassList != null && skuPassList.size() > 0) {
				for (PoSku sku : skuPassList) {
					int tempSkuNum = sku.getSkuNum();
					itemNum = itemNum + tempSkuNum;
					skuNum++;
				}
			}

			List<PoProduct> list = poProductDao.getProduct(poId);
			List<PoProduct> productPassList = new ArrayList<PoProduct>();
			List<PoProduct> productNotPassList = new ArrayList<PoProduct>();
			if (list != null && list.size() > 0) {
				for (PoProduct product : list) {
					if (product.getStatus() == StatusType.APPROVAL)
						productPassList.add(product);
					else
						productNotPassList.add(product);
				}
			}
			if (list != null && list.size() > 0)
				productNum = list.size();

			ScheduleAuditData data = new ScheduleAuditData();
			data.setPassItemNum(itemNum);
			data.setPassProductNum(productNum);
			data.setPassSkuNum(skuNum);
			if (list != null && list.size() > 0) {
				data.setProductPass(productNotPassList.size() == 0);
				data.setSkuPass(skuNotPassList.size() == 0);
			} else {
				data.setProductPass(false);
				data.setSkuPass(false);
			}
			return data;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<Boolean> isItemReviewPass(List<Long> poIds) {
		try {
			List<Boolean> retList = new ArrayList<Boolean>();
			if (poIds != null && poIds.size() > 0) {
				for (long poId : poIds) {
					if (isProductAllPass(poId) && isSkuAllPass(poId))
						retList.add(true);
					else
						retList.add(false);
				}
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public int getProductNumOfStatus(long poId, StatusType status) {
		try {
			return poProductDao.getProductNumOfStatus(poId, status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public int getSkuNumOfStatus(long poId, StatusType status) {
		try {
			return poSkuDao.getSkuNumOfStatus(poId, status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public int getProductNum(long poId) {
		try {
			return poProductDao.getProductNum(poId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public int getSkuNum(long poId) {
		try {
			return poSkuDao.getSkuNum(poId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<Long> getPOByBarCode(long supplierId, String barCode) {
		return poSkuDao.getPOByBarCode(supplierId, barCode);
	}

	@Override
	public POSkuDTO getPOSkuDTO(long skuId) {
		PoSku sku = poSkuDao.getObjectById(skuId);
		if (sku == null)
			return null;
		POSkuDTO dto = new POSkuDTO(sku);
		long pid = dto.getProductId();
		PoProduct p = poProductDao.getObjectById(pid);
		if (p == null)
			return null;
		dto.setBrandId(p.getBrandId());
		dto.setColorName(p.getColorName());
		dto.setProductName(p.getProductName());
		return dto;
	}

	@Override
	public List<String> getProductPic(long pid, PictureType type) {
		ProdPicMap map = prodPicDao.getProdPicMap(pid, type, ConstantsUtil.IN_PO);
		List<String> retList = ItemCenterUtil.genProdPicPath(map);
		if (type == PictureType.LIST && retList.size() < 2) {
			while (retList.size() < 2) {
				retList.add("");
			}
		}
		return retList;
	}

	@Override
	public List<String> getProductPicNoCache(long pid, PictureType type) {
		ProdPicMap map = prodPicDao.getProdPicMapNoCache(pid, type, ConstantsUtil.IN_PO);
		List<String> retList = ItemCenterUtil.genProdPicPath(map);
		if (type == PictureType.LIST && retList.size() < 2) {
			while (retList.size() < 2) {
				retList.add("");
			}
		}
		return retList;
	}

	@Override
	public List<ProdPicMap> getProductPicListCache(List<Long> pids, PictureType type) {
		List<ProdPicMap> retList = new ArrayList<ProdPicMap>();
		if (pids != null && pids.size() > 0) {
			for (long pid : pids) {
				ProdPicMap map = prodPicDao.getProdPicMap(pid, type, ConstantsUtil.IN_PO);
				if (map != null)
					retList.add(map);
			}
		}
		return retList;
	}

	@Override
	public List<ProdPicMap> getProductPicList(List<Long> pids, PictureType type) {
		List<ProdPicMap> retList = new ArrayList<ProdPicMap>();
		if (pids != null && pids.size() > 0) {
			for (long pid : pids) {
				ProdPicMap map = prodPicDao.getProdPicMapNoCache(pid, type, ConstantsUtil.IN_PO);
				if (map != null)
					retList.add(map);
			}
		}
		return retList;
	}

	@Override
	@Transaction
	public void deleteProduct(long poId, long productId) {
		try {
			// delete picture
			prodPicDao.deleteProdPicMap(productId, ConstantsUtil.IN_PO);
			customizedSizeValueDao.deleteCustomizedSizeValue(productId, ConstantsUtil.IN_PO);
			customizedSizeDao.deleteCustomizedSize(productId, ConstantsUtil.IN_PO);
			// TODO delete sizeColumn
			poSkuDao.deleteSkuByProductId(productId);
			poProductDao.deleteById(productId);
			poDetailDao.deleteObject(poId, productId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	public void productOnline(long poId) {
		try {
			poProductDao.productOnline(poId);
			poSkuDao.skuOnline(poId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Cacheable(value = "searchProdByPO")
	@Override
	public PoProduct getPoProductByPoIdAndProduct(long productId, long poId) {
		return poProductDao.getPoProductByPoIdAndProduct(productId, poId);
	}

	@Override
	public Category getCategoryBySkuId(long skuId) {
		long cid = poProductDao.getCategoryIdBySkuId(skuId);
		return categoryDao.getCategoryById(cid);
	}

	@Override
	public SkuSpecMap getSkuSpecMapBySkuIdAndPoId(long skuId, long poId) {
		return skuSpecMapDao.getSkuSpecMapBySkuIdAndPoId(skuId, poId);
	}

	@Override
	public List<PoSku> getPoSkuListByParam(PoSkuSo so) {
		// TODO Auto-generated method stub
		return poSkuDao.getPoSkuListByParam(so);
	}

	@Override
	public PoSku getSku(long poId, String barCode) {
		return poSkuDao.getSku(poId, barCode);
	}

	@Override
	public List<PoProduct> getPoProductListByName(String productName) {
		return poProductDao.getPoProductByName(productName);
	}

	@Override
	public List<PoProduct> getPoProductByGoodsNo(String goodsNo) {
		return poProductDao.getPoProductByGoodsNo(goodsNo);
	}

	@Override
	public PoProduct getProductBySkuId(long skuId) {
		PoSku sku = poSkuDao.getObjectById(skuId);
		long pid = sku.getProductId();
		return poProductDao.getObjectById(pid);
	}

	@Cacheable(value = "poskuCache")
	@Override
	public SkuSpecMap getSkuSpecMapByPoIdProductIdSkuId(long poId, long productId, long skuId) {
		return skuSpecMapDao.getSkuSpecMapByPoIdProductIdSkuId(poId, productId, skuId);
	}

	@Cacheable(value = "poskuCache")
	@Override
	public List<PoSkuVo> getPoSkuVosByParam(PoSkuSo so) {
		return poSkuDao.getPoSkuVosByParam(so);
	}

	@Override
	public Long getPoSkuVosCountByParam(PoSkuSo so) {
		return poSkuDao.getPoSkuVosCountByParam(so);
	}

	@Override
	public List<PoSku> getPoSkusByIds(List<Long> ids) {
		return poSkuDao.getPoSkusByIds(ids);
	}

	@Override
	public List<Long> getSkuIds(long poId, long pid) {
		return poSkuDao.getSkuIds(poId, pid);
	}

	@Override
	public List<PoProductVo> getPoProductosByParam(PoProductSo so) {
		return poProductDao.getPoProductosByParam(so);
	}

	@Override
	public Long getPoProductVosCountByParam(PoProductSo so) {
		return poProductDao.getPoProductVosCountByParam(so);
	}

	@Override
	public PoSku getPoSkuByBarCode(String barcode) {
		return poSkuDao.getPoSkuByBarCode(barcode);
	}

	@Override
	public List<POProductDTO> getProductDTOListByCatgory(long catgoryId) {
		try {
			List<POProductDTO> list = poProductDao.getProductDTOListByCategory(catgoryId);
			if (list != null && list.size() > 0) {
				Category cate = categoryDao.getCategoryById(catgoryId);
				for (POProductDTO dto : list) {
					dto.setCategoryName(cate.getName());
					String showPath = dto.getShowPicPath();
					dto.setShowPicPath(ItemCenterUtil.genProdPicPath(showPath));
				}
			}
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Cacheable("searchProdByPO")
	@Override
	public List<POProductDTO> getProductDTOListByCategoryAndCache(long catgoryId) {
		try {
			List<POProductDTO> list = poProductDao.getProductDTOListByCategory(catgoryId);
			if (list != null && list.size() > 0) {
				Category cate = categoryDao.getCategoryById(catgoryId);
				for (POProductDTO dto : list) {
					dto.setCategoryName(cate.getName());
					String showPath = dto.getShowPicPath();
					dto.setShowPicPath(ItemCenterUtil.genProdPicPath(showPath));
				}
			}
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}
}
