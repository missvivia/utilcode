package com.xyl.mmall.itemcenter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.itemcenter.dao.category.CategoryDao;
import com.xyl.mmall.itemcenter.dao.product.PoProductDao;
import com.xyl.mmall.itemcenter.dao.product.ProductDao;
import com.xyl.mmall.itemcenter.dao.product.ProductDetailDao;
import com.xyl.mmall.itemcenter.dao.product.ProductParamDao;
import com.xyl.mmall.itemcenter.dao.product.ProductParamOptDao;
import com.xyl.mmall.itemcenter.dao.product.ProductParamOptDaoImpl;
import com.xyl.mmall.itemcenter.dao.product.ProductPicDao;
import com.xyl.mmall.itemcenter.dao.product.ProductPriceDao;
import com.xyl.mmall.itemcenter.dao.size.CustomizedSizeDao;
import com.xyl.mmall.itemcenter.dao.size.CustomizedSizeValueDao;
import com.xyl.mmall.itemcenter.dao.size.SizeAssistDao;
import com.xyl.mmall.itemcenter.dao.size.SizeColumnDao;
import com.xyl.mmall.itemcenter.dao.sku.SkuDao;
import com.xyl.mmall.itemcenter.dao.sku.SkuSpecMapDao;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.dto.BaseSkuDTO;
import com.xyl.mmall.itemcenter.dto.ExcelExportProduct;
import com.xyl.mmall.itemcenter.dto.ProductDTO;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductParamDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSearchResultDTO;
import com.xyl.mmall.itemcenter.enums.PictureType;
import com.xyl.mmall.itemcenter.enums.ProdDetailType;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.CustomizedSize;
import com.xyl.mmall.itemcenter.meta.CustomizedSizeValue;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.meta.ProdPicMap;
import com.xyl.mmall.itemcenter.meta.Product;
import com.xyl.mmall.itemcenter.meta.ProductDetail;
import com.xyl.mmall.itemcenter.meta.ProductParamOption;
import com.xyl.mmall.itemcenter.meta.ProductParameter;
import com.xyl.mmall.itemcenter.meta.ProductPrice;
import com.xyl.mmall.itemcenter.meta.SizeAssist;
import com.xyl.mmall.itemcenter.meta.SizeColumn;
import com.xyl.mmall.itemcenter.meta.Sku;
import com.xyl.mmall.itemcenter.param.BatchUploadPicParam;
import com.xyl.mmall.itemcenter.param.BatchUploadSizeParam;
import com.xyl.mmall.itemcenter.param.BatchUploadSizeSku;
import com.xyl.mmall.itemcenter.param.ChangeProductNameParam;
import com.xyl.mmall.itemcenter.param.POProductSearchParam;
import com.xyl.mmall.itemcenter.param.ProdParamParam;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.ProductSearchParam;
import com.xyl.mmall.itemcenter.param.ProductUniqueParam;
import com.xyl.mmall.itemcenter.param.SizeColumnParam;
import com.xyl.mmall.itemcenter.param.SizeTable;
import com.xyl.mmall.itemcenter.param.SkuSaveParam;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.itemcenter.util.ConstantsUtil;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;

@Service
public class ProductServiceImpl implements ProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductDetailDao detailDao;

	@Autowired
	private PoProductDao poProductDao;

	@Autowired
	private ProductPicDao prodPicDao;

	@Autowired
	private ProductParamDao productParamDao;

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private SkuSpecMapDao skuSpecMapDao;

	@Autowired
	private SizeColumnDao sizeColumnDao;

	@Autowired
	private CustomizedSizeDao customizeSizeDao;

	@Autowired
	private ProductParamOptDao prodParamOptDao;

	@Autowired
	private SizeTemplateService sizeTemplateService;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private SizeAssistDao sizeAssistDao;

	@Autowired
	private CustomizedSizeValueDao customSizeValueDao;

	@Autowired
	private ProductPriceDao productPriceDao;
	
	@Override
	@Transaction
	public int saveProduct(ProductSaveParam productParam) {
		try {
			long supplierId = productParam.getSupplierId();
			Product product = saveProductBaseInfo(productParam);
			long pid = product.getId();
			saveProductSize(pid, productParam);

			// 保存sku
			List<SkuSaveParam> skuList = productParam.getSKUList();
			List<Sku> srcSku = skuDao.getSkuList(supplierId, pid);
			// saveProductInitialSku(supplierId, pid, product.getSizeType(),
			// skuList, srcSku);
			List<SizeColumnParam> header = productParam.getSizeHeader();
			for (int i = 1; i <= skuList.size(); i++) {
				SkuSaveParam skuParam = skuList.get(i - 1);
				if (srcSku != null && srcSku.size() > 0) {
					for (Sku ssku : srcSku) {
						if (ssku.getBarCode().equals(skuParam.getBarCode())) {
							skuParam.setId(ssku.getId());
							List<String> sizeValueList = skuParam.getCustomizedSizeValue();
							if (header != null && header.size() > 0) {
								for (SizeColumnParam head : header) {
									if (head.getId() != 1) {
										CustomizedSizeValue svl = customSizeValueDao.getCustomizedSizeValue(pid,
												head.getId(), ssku.getSizeIndex(), ConstantsUtil.NOT_IN_PO);
										if (svl == null)
											sizeValueList.add("");
										else
											sizeValueList.add(svl.getValue());
									}
								}
							}
							srcSku.remove(ssku);
							break;
						}
					}
				}
			}

			List<SkuSaveParam> newSkuList = new ArrayList<SkuSaveParam>();
			int index = 1;
			if (srcSku != null && srcSku.size() > 0) {
				for (Sku ssku : srcSku) {
					SkuSaveParam param = new SkuSaveParam();
					param.setBarCode(ssku.getBarCode());
					param.setId(ssku.getId());
					param.setSizeIndex(index);
					List<String> sizeValueList = new ArrayList<String>();
					if (header != null && header.size() > 0) {
						for (SizeColumnParam head : header) {
							CustomizedSizeValue svl = customSizeValueDao.getCustomizedSizeValue(pid, head.getId(),
									ssku.getSizeIndex(), ConstantsUtil.NOT_IN_PO);
							if (svl == null)
								sizeValueList.add("");
							else
								sizeValueList.add(svl.getValue());
						}
					}

					param.setCustomizedSizeValue(sizeValueList);
					newSkuList.add(param);
					index++;
				}
			}

			for (int i = 1; i <= skuList.size(); i++) {
				SkuSaveParam skuParam = skuList.get(i - 1);
				skuParam.setSizeIndex(index);
				newSkuList.add(skuParam);
				index++;
			}
			productParam.setSKUList(newSkuList);
			saveProductSKU(pid, productParam);
			return ErrorCode.SUCCESS.getIntValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	public int SingleSaveProduct(ProductSaveParam productParam) {
		try {
			long supplierId = productParam.getSupplierId();
			Product product = saveProductBaseInfo(productParam);
			long pid = product.getId();
			sizeTemplateService.deleteCustomizeSizeValue(pid);
			saveProductSize(pid, productParam);
			// 保存sku
			List<SkuSaveParam> skuList = productParam.getSKUList();
			List<Sku> srcSku = skuDao.getSkuList(supplierId, pid);
			saveProductInitialSku(supplierId, pid, product.getSizeType(), skuList, srcSku);
			if (srcSku != null && srcSku.size() > 0) {
				for (Sku sku : srcSku) {
					boolean isDet = true;
					for (SkuSaveParam skuParam : skuList) {
						if (sku.getBarCode().equals(skuParam.getBarCode())) {
							isDet = false;
							break;
						}
					}
					if (isDet) {
						skuDao.deleteById(sku.getId());
						if (productParam.getSizeType() == SizeType.CUST_SIZE.getIntValue()) {
							sizeTemplateService.deleteCustomizeSizeValueBySku(pid, sku.getSizeIndex());
						}
					}
				}
			}
			saveProductSKU(pid, productParam);
			return ErrorCode.SUCCESS.getIntValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	private Product saveProductBaseInfo(ProductSaveParam productParam) {
		Product product = (Product) ItemCenterUtil.extractData(productParam, Product.class);
		ProductDetail detail = (ProductDetail) ItemCenterUtil.extractData(productParam, ProductDetail.class);
		long pid = product.getId();
		long supplierId = productParam.getSupplierId();
		List<SkuSaveParam> skuList = productParam.getSKUList();
		if (skuList == null)
			throw new ServiceException("没有条码");

		List<String> barCodes = new ArrayList<String>();
		if (skuList != null && skuList.size() > 0) {
			for (SkuSaveParam sku : skuList) {
				if (barCodes.contains(sku.getBarCode()))
					throw new ServiceException("条形码重复");
				else
					barCodes.add(sku.getBarCode());
			}
		}

		List<Sku> skuIds = skuDao.getExistSkuIds(supplierId, barCodes);
		if (skuIds != null && skuIds.size() > 0) {
			for (Sku sku : skuIds) {
				if (pid != sku.getProductId())
					throw new ServiceException("条形码重复");
			}
		}

		if (productParam.getIsRecommend() == 1)
			detail.setRecommend(true);
		else
			detail.setRecommend(false);
		if (productParam.getSizeType() == 1) {
			product.setSizeType(SizeType.ORIG_SIZE);
		} else if (productParam.getSizeType() == 2) {
			product.setSizeType(SizeType.TMPL_SIZE);
		} else {
			product.setSizeType(SizeType.CUST_SIZE);
		}
		// TODO
		int infoFlag = productParam.getInfoFlag();
		int base = 8;
		int size = 4;
		int pic = 2;
		int html = 1;
		infoFlag = infoFlag | base;
		infoFlag = infoFlag | size;
		if (!StringUtils.isBlank(detail.getCustomEditHTML()))
			infoFlag = infoFlag | html;
		detail.setCustomEditHTML(productParam.getNosURL());

		List<String> showPicList = productParam.getProdShowPicList();
		List<String> listPicList = productParam.getListShowPicList();
		if (showPicList != null && showPicList.size() > 0 && listPicList != null && listPicList.size() > 0)
			infoFlag = infoFlag | pic;
		product.setInfoFlag(infoFlag);

		if (showPicList != null && showPicList.size() > 0) {
			String thumb = showPicList.get(0);
			product.setShowPicPath(thumb);
		}
		// 保存商品参数信息
		if (pid > 0) {
			Product sproduct = productDao.getObjectById(pid);
			if (sproduct != null) {
				product.setAddTime(sproduct.getAddTime());
				product.setUpdateTime(new Date().getTime());
			} else {
				product.setAddTime(new Date().getTime());
				product.setUpdateTime(new Date().getTime());
			}

		} else {
			product.setAddTime(new Date().getTime());
			product.setUpdateTime(new Date().getTime());
		}
		// 保存产品主信息
		productDao.saveObject(product);
		pid = product.getId();
		detail.setProductId(pid);
		detailDao.saveObject(detail);
		// 保存图片信息
		if (showPicList != null && showPicList.size() > 0)
			saveProductPic(pid, PictureType.PROD, showPicList);

		if (listPicList != null && listPicList.size() > 0)
			saveProductPic(pid, PictureType.LIST, listPicList);
		return product;
	}

	private void saveProductSize(long pid, ProductSaveParam productParam) {
		customizeSizeDao.deleteCustomizedSize(pid, ConstantsUtil.NOT_IN_PO);

		List<SizeColumnParam> colList = productParam.getSizeHeader();
		if (colList != null && colList.size() > 0 && productParam.getSizeType() == 3) {
			saveCustomizedSize(pid, colList);
		}
	}

	private void saveProductInitialSku(long supplierId, long pid, SizeType sizeType, List<SkuSaveParam> skuList,
			List<Sku> srcSku) {
		for (int i = 1; i <= skuList.size(); i++) {
			SkuSaveParam skuParam = skuList.get(i - 1);
			int recordIndex = skuParam.getSizeIndex();
			if (sizeType == SizeType.CUST_SIZE) {
				if (recordIndex <= 0) {
					recordIndex = i;
					skuParam.setSizeIndex(recordIndex);
				}
			}
			if (srcSku != null && srcSku.size() > 0) {
				for (Sku ssku : srcSku) {
					if (ssku.getBarCode().equals(skuParam.getBarCode())) {
						skuParam.setId(ssku.getId());
						srcSku.remove(ssku);
						break;
					}
				}
			}
		}
	}

	private void saveProductSKU(long pid, ProductSaveParam productParam) {
		long supplierId = productParam.getSupplierId();
		List<SizeColumnParam> colList = productParam.getSizeHeader();
		List<SkuSaveParam> skuList = productParam.getSKUList();
		for (int i = 1; i <= skuList.size(); i++) {
			SkuSaveParam skuParam = skuList.get(i - 1);
			int recordIndex = skuParam.getSizeIndex();
			saveSku(skuParam, pid, supplierId, productParam.getBasePrice(), productParam.getMarketPrice(),
					productParam.getSalePrice());
			if (productParam.getSizeType() == 3) {
				List<String> sizeValue = skuParam.getCustomizedSizeValue();
				saveCustomizedSizeValue(pid, recordIndex, colList, sizeValue);
			}
		}
	}

	@Override
	@Transaction
	public String batchSaveSize(long supplierId, List<String> header, BatchUploadSizeParam param) {
		try {
			StringBuffer msg = new StringBuffer();
			String colorName = param.getColorNum();
			String goodsNo = param.getGoodNo();
			Product product = getProductByUniq(supplierId, goodsNo, colorName);
			if (product != null) {
				long pid = product.getId();
				List<Sku> skuList = skuDao.getSkuList(supplierId, pid);
				if (product.getSizeType() == SizeType.CUST_SIZE) {
					long lowCategoryId = product.getLowCategoryId();
					long oSizeId = sizeTemplateService.getOriginalSizeId(lowCategoryId);
					List<Size> oList = sizeTemplateService.getSizeList(oSizeId, SizeType.ORIG_SIZE);
					List<SizeColumnParam> columnParamList = new ArrayList<SizeColumnParam>();
					if (oList != null && oList.size() > 0) {
						for (Size orginalSize : oList) {
							SizeColumnParam cshv = new SizeColumnParam();
							cshv.setId(orginalSize.getColumnId());
							SizeColumn col = sizeTemplateService.getSizeColumn(orginalSize.getColumnId());
							cshv.setName(col.getName());
							cshv.setUnit(col.getUnit() == null ? "" : col.getUnit());
							cshv.setRequired(orginalSize.getIsRequired());
							columnParamList.add(cshv);
						}
					}
					if (header.size() >= columnParamList.size()) {
						boolean isValid = true;
						for (int i = 0; i < columnParamList.size(); i++) {
							SizeColumnParam col = columnParamList.get(i);
							String headerName = header.get(i);
							if (!headerName.equals(col.getName())) {
								isValid = false;
								break;
							}
						}
						if (isValid) {
							List<BatchUploadSizeSku> sizeSkuList = param.getSkuSizeList();
							if (sizeSkuList != null && sizeSkuList.size() > 0) {
								for (BatchUploadSizeSku sizeSku : sizeSkuList) {
									String barCode = sizeSku.getBarCode();
									int recordIndex = -1;
									if (skuList != null && skuList.size() > 0) {
										for (int i = 0; i < skuList.size(); i++) {
											Sku sku = skuList.get(i);
											String temBarCode = sku.getBarCode();
											if (temBarCode.equals(barCode)) {
												recordIndex = sku.getSizeIndex();
												break;
											}
										}
									}
									if (recordIndex >= 0) {
										List<String> sizeValue = sizeSku.getSizeValue();
										saveCustomizedSizeValue(pid, recordIndex, columnParamList, sizeValue);
									} else {
										// TODO barCode不存在
										msg.append("货号：" + goodsNo + "，色号：" + colorName + "，条形码：" + barCode
												+ " 不存在，该记录尺寸导入失败；");
									}

								}
							}
							return msg.toString();
						} else {
							// TODO 自定义字段不正确
							return ("货号：" + goodsNo + "，色号：" + colorName + " 的尺寸字段名非法，该商品尺寸导入失败；");
						}
					} else {
						// TODO 自定义字段不正确
						return ("货号：" + goodsNo + "，色号：" + colorName + " 的尺寸字段个数不符合，该商品尺寸导入失败；");
					}
				} else {
					// TODO 商品尺码类型不正确
					return ("货号：" + goodsNo + "，色号：" + colorName + " 的尺寸类型不是自定义，该商品尺寸导入失败；");
				}
			} else {
				// TODO 商品不存在
				return ("货号：" + goodsNo + "，色号：" + colorName + " 不存在，该商品尺寸导入失败；");
			}
		} catch (Exception e) {
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	public void batchSaveCustomHtml(long supplierId, String goodsNo, String colorNum, String html) {
		try {
			Product p = productDao.getProduct(supplierId, goodsNo, colorNum);
			long pid = p.getId();
			detailDao.updateCustomHtml(supplierId, pid, html);
			productDao.updateCustomHtmlState(supplierId, goodsNo, colorNum, 1);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<Long> getProductIdListByUniq(List<ProductUniqueParam> params) {
		List<Long> retList = new ArrayList<Long>();
		if (params != null && params.size() > 0) {
			for (ProductUniqueParam param : params) {
				long supplierId = param.getSupplierId();
				String goodNo = param.getGoodNo();
				String colorNum = param.getColorNum();
				Long pid = productDao.getProductId(supplierId, goodNo, colorNum);
				retList.add(pid);
			}
		}
		return retList;
	}

	@Override
	public Product getProductByUniq(long supplierId, String goodsNo, String colorNum) {
		return productDao.getProduct(supplierId, goodsNo, colorNum);
	}

	@Override
	public Product getProduct(long pid) {
		return productDao.getProduct(pid);
	}

	@Override
	public ProductFullDTO getProductFullDTOById(long pid) {
		Product product = productDao.getProduct(pid);
		if (product == null)
			return null;
		ProductDetail detail = detailDao.getProductDetail(product.getSupplierId(), pid);
		if (detail == null)
			return null;
		return new ProductFullDTO(product, detail);

	}

	@Override
	public ProductFullDTO getProductFullDTOByUniq(long supplierId, String goodsNo, String colorNum) {
		Product product = productDao.getProduct(supplierId, goodsNo, colorNum);
		if (product == null)
			return null;
		long pid = product.getId();
		ProductDetail detail = detailDao.getProductDetail(supplierId, pid);
		if (detail == null)
			return null;
		return new ProductFullDTO(product, detail);

	}

	@Override
	@Transaction
	public void deleteProduct(long supplierId, long productId) {
		try {
			// delete picture
			prodPicDao.deleteProdPicMap(productId, ConstantsUtil.NOT_IN_PO);
			sizeTemplateService.deleteCustomizeSizeValue(productId);
			customizeSizeDao.deleteCustomizedSize(productId, ConstantsUtil.NOT_IN_PO);
			// TODO delete sizeColumn
			skuDao.deleteSkuList(supplierId, productId);
			productDao.deleteById(productId);
			detailDao.deleteDetailByPid(productId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	public void deleteProducts(long supplierId, List<Long> ids) {
		try {
			if (ids != null && ids.size() > 0) {
				for (long id : ids) {
					deleteProduct(supplierId, id);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public BaseSearchResult<ProductSearchResultDTO> searchProduct(ProductSearchParam searchDTO) {
		try {
			BaseSearchResult<Product> prodSearchResult = productDao.searchProduct(searchDTO);
			List<Product> productList = prodSearchResult.getList();
			List<ProductSearchResultDTO> retList = new ArrayList<ProductSearchResultDTO>();
			for (Product product : productList) {
				ProductSearchResultDTO dto = new ProductSearchResultDTO(product);
				Category cate = categoryDao.getCategoryById(dto.getLowCategoryId());
				dto.setCategoryName(cate.getName());
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
	public List<ExcelExportProduct> searchExportProduct(ProductSearchParam searchParam) {
		try {
			long supplierId = searchParam.getSupplierId();
			List<Product> productList = productDao.searchExportProduct(searchParam);
			List<ExcelExportProduct> retList = excelExportProductTransfer(supplierId, productList);
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<ExcelExportProduct> searchExportProduct(long supplierId, List<Long> pids) {
		try {
			List<Product> productList = productDao.searchExportProduct(supplierId, pids);
			List<ExcelExportProduct> retList = excelExportProductTransfer(supplierId, productList);
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	private List<ExcelExportProduct> excelExportProductTransfer(long supplierId, List<Product> productList) {
		List<ExcelExportProduct> retList = new ArrayList<ExcelExportProduct>();
		if (productList != null && productList.size() > 0) {
			for (Product meta : productList) {
				long productId = meta.getId();
				ProductDetail detail = detailDao.getProductDetail(supplierId, productId);
				Map<String, String> map = ItemCenterUtil.getEditHTML_Param(detail.getCustomEditHTML(), null);
				String paramValue = map.get("parameter");

				List<ProdParamParam> paramValueList = JsonUtils.parseArray(paramValue, ProdParamParam.class);
				List<ProductParamDTO> paramList = new ArrayList<ProductParamDTO>();
				if (paramValueList != null && paramValueList.size() > 0) {
					for (ProdParamParam paramVal : paramValueList) {
						long paramId = paramVal.getId();
						String paramVl = paramVal.getValue();
						ProductParameter paramter = productParamDao.getObjectById(paramId);
						ProductParamDTO param = new ProductParamDTO(paramter);
						if (paramter.getDetailType() == ProdDetailType.SINGLE_SELECT) {
							if (!StringUtils.isBlank(paramVl)) {
								long optId = Long.valueOf(paramVl);
								List<ProductParamOption> optList = prodParamOptDao.getOptionList(paramId);
								for (ProductParamOption opt : optList) {
									if (opt.getId() == optId) {
										param.setParamValue(opt.getValue());
										break;
									}
								}
							}

						} else if (paramter.getDetailType() == ProdDetailType.MULTI_SELECT) {
							if (!StringUtils.isBlank(paramVl)) {
								List<Long> paramValues = JsonUtils.parseArray(paramVl, Long.class);
								List<ProductParamOption> optList = prodParamOptDao.getOptionList(paramId);
								StringBuffer sb = new StringBuffer();
								if (paramValues != null && paramValues.size() > 0) {
									for (long sOptId : paramValues) {
										for (ProductParamOption opt : optList) {
											if (opt.getId() == sOptId) {
												sb.append(opt.getValue()).append(";");
											}
										}
									}
									param.setParamValue(sb.toString().trim());
								} else
									continue;
							}

						} else {
							param.setParamValue(paramVl);
						}
						paramList.add(param);
					}
				}

				List<Sku> skuList = skuDao.getSkuList(supplierId, productId);
				SizeType sizeType = meta.getSizeType();
				long templateKey = 0;
				if (sizeType == SizeType.TMPL_SIZE)
					templateKey = meta.getSizeTemplateId();
				else if (sizeType == SizeType.CUST_SIZE) {
					templateKey = meta.getId();
				}
				SizeTable table = null;
				if (sizeType != SizeType.ORIG_SIZE)
					table = sizeTemplateService.getSizeTable(templateKey, sizeType);
				if (skuList != null && skuList.size() > 0) {
					for (Sku sku : skuList) {
						ProductFullDTO prod = new ProductFullDTO(meta, detail);
						ExcelExportProduct dto = new ExcelExportProduct(prod);
						dto.setParamList(paramList);
						dto.setBarCode(sku.getBarCode());
						if (sizeType == SizeType.ORIG_SIZE)
							dto.setSpec(ConstantsUtil.DEFAULT_SIZE);
						else {
							long index = sku.getSizeIndex();
							String key1 = index + "+" + ConstantsUtil.CHIMA_COLUMN;
							String size = table.getValueMap().get(key1);
							if (size == null)
								size = "";
							dto.setSpec(size);
						}
						retList.add(dto);
					}
				}
			}
		}
		return retList;
	}

	@Override
	public boolean saveProductPic(long pid, PictureType type, List<String> pathList) {
		try {
			ProdPicMap ppMap = prodPicDao.getProdPicMapNoCache(pid, type, ConstantsUtil.NOT_IN_PO);
			ProdPicMap retMap = ItemCenterUtil.genProdPicMap(pathList);
			if (retMap != null) {
				if (ppMap != null) {
					ppMap.setPicPath(retMap.getPicPath());
					ppMap.setNosVersion(retMap.getNosVersion());
					ppMap.setUTime(new Date().getTime());
				} else {
					ppMap = new ProdPicMap();
					ppMap.setIsInPo(ConstantsUtil.NOT_IN_PO);
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
	public boolean batchChangeProductName(ChangeProductNameParam param) {
		try {
			List<Long> pidList = param.getPid();
			if (pidList != null && pidList.size() > 0) {
				for (long pid : pidList) {
					Product product = productDao.getProduct(param.getSupplierId(), pid);
					if (product != null) {
						String pname = product.getProductName();
						String newName = pname;
						if (!StringUtils.isBlank(param.getReplace()) && !StringUtils.isBlank(param.getReplacement())) {
							if (pname.indexOf(param.getReplace()) >= 0) {
								newName = pname.replaceAll(param.getReplace(), param.getReplacement());
							}
						}
						if (!StringUtils.isBlank(param.getHeader())) {
							newName = param.getHeader() + newName;
						}
						if (!StringUtils.isBlank(param.getTailer())) {
							newName = newName + param.getTailer();
						}
						productDao.updateProductName(param.getSupplierId(), pid, newName);
					}

				}
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	public String batchSaveProductPic(long supplierId, long pid, BatchUploadPicParam param) {
		String goodsNo = param.getGoodsNo();
		String colorNum = param.getColorNum();
		try {
			List<String> showList = param.getProdShowPicList();
			boolean input1 = saveProductPic(pid, PictureType.PROD, showList);
			List<String> listList = param.getListShowPicList();
			boolean input2 = saveProductPic(pid, PictureType.LIST, listList);
			if (showList != null && showList.size() > 0) {
				String thumb = showList.get(0);
				productDao.updateProductThumb(supplierId, goodsNo, colorNum, thumb);
			}
			if (input1 || input2)
				productDao.updatePicState(supplierId, goodsNo, colorNum, 1);
			return null;
		} catch (Exception e) {
			return "文件夹“" + goodsNo + "+" + "colorNum" + "”图片导入失败。出错原因如下：保存失败.";
		}
	}

	private Sku saveSku(SkuSaveParam skuParam, long pid, long supplierId, BigDecimal basePrice, BigDecimal marketPrice,
			BigDecimal salePrice) {
		Sku sku = (Sku) ItemCenterUtil.extractData(skuParam, Sku.class);
		sku.setProductId(pid);
		sku.setSupplierId(supplierId);
		sku.setBasePrice(basePrice);
		sku.setMarketPrice(marketPrice);
		sku.setSalePrice(salePrice);
		return skuDao.saveObject(sku);
	}

	@Override
	@Transaction
	public void saveCustomizedSizeValue(long pid, int recordIndex, List<SizeColumnParam> colList, List<String> sizeValue) {
		sizeTemplateService.deleteCustomizeSizeValueBySku(pid, recordIndex);
		for (int j = 0; j < colList.size(); j++) {
			String value = null;
			SizeColumnParam column = colList.get(j);
			if (column.getIsRequired() && sizeValue.size() < j + 1)
				throw new ServiceException("customized size is not correct!");
			else if (!column.getIsRequired() && sizeValue.size() < j + 1) {
				continue;
			} else if (column.getIsRequired() && sizeValue.size() >= j + 1) {
				value = sizeValue.get(j);
				if (StringUtils.isBlank(value) || "null".equals(value))
					throw new ServiceException("customized size is not correct!");
				else
					sizeTemplateService.addCustomizedSizeValue(pid, recordIndex, column.getId(), value);
			} else {
				value = sizeValue.get(j);
				if (StringUtils.isBlank(value) || "null".equals(value))
					continue;
				else
					sizeTemplateService.addCustomizedSizeValue(pid, recordIndex, column.getId(), value);
			}

		}
	}

	@Override
	@Transaction
	public void batchUploadSizeAddSku(Sku sku, List<SizeColumnParam> colList, List<String> sizeValue) {
		long pid = sku.getProductId();
		int index = skuDao.getMaxIndexOfSku(pid) + 1;
		sku.setSizeIndex(index);
		skuDao.addObject(sku);
		for (int j = 0; j < colList.size(); j++) {
			String value = null;
			SizeColumnParam column = colList.get(j);
			if (column.getIsRequired() && sizeValue.size() < j + 1)
				throw new ServiceException("customized size is not correct!");
			else if (!column.getIsRequired() && sizeValue.size() < j + 1) {
				continue;
			} else if (column.getIsRequired() && sizeValue.size() >= j + 1) {
				value = sizeValue.get(j);
				if (StringUtils.isBlank(value) || "null".equals(value))
					throw new ServiceException("customized size is not correct!");
				else
					sizeTemplateService.addCustomizedSizeValue(pid, index, column.getId(), value);
			} else {
				value = sizeValue.get(j);
				if (StringUtils.isBlank(value) || "null".equals(value))
					continue;
				else
					sizeTemplateService.addCustomizedSizeValue(pid, index, column.getId(), value);
			}
		}
	}

	private void saveCustomizedSize(long pid, List<SizeColumnParam> colList) {
		customizeSizeDao.deleteCustomizedSize(pid, ConstantsUtil.NOT_IN_PO);
		for (int j = 0; j < colList.size(); j++) {
			SizeColumnParam column = colList.get(j);
			if (column.getId() <= 0) {
				SizeColumn tempCol = (SizeColumn) ItemCenterUtil.extractData(column, SizeColumn.class);
				sizeColumnDao.addNewSizeColumn(tempCol);
				column.setId(tempCol.getId());
			}
			CustomizedSize custmSize = new CustomizedSize();
			custmSize.setProductId(pid);
			custmSize.setColIndex(j);
			custmSize.setColumnId(column.getId());
			custmSize.setIsRequired(column.isRequired());
			// customizeSizeDao.saveCustomizedSize(custmSize);
			customizeSizeDao.addObject(custmSize);
		}
	}

	@Override
	public List<ProductParamDTO> getProductParamList(long categoryId) {
		try {
			List<ProductParamDTO> retList = new ArrayList<ProductParamDTO>();
			Category c = categoryDao.getCategoryById(categoryId);
			String paramValue = c.getParameter();
			if (!StringUtils.isBlank(paramValue)) {
				List<Long> list = JsonUtils.parseArray(paramValue, Long.class);
				if (list != null && list.size() > 0) {
					for (long id : list) {
						ProductParameter param = productParamDao.getObjectById(id);
						ProductParamDTO dto = new ProductParamDTO(param);
						if (param.getDetailType() != ProdDetailType.TEXT
								&& param.getDetailType() != ProdDetailType.TEXT_AREA) {
							List<ProductParamOption> optList = getOptionList(param.getId());
							dto.setOptionList(optList);
						} else
							dto.setOptionList(new ArrayList<ProductParamOption>());
						retList.add(dto);
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
	public ProductParamDTO getProductParamDTO(long id) {
		try {
			ProductParameter param = productParamDao.getObjectById(id);
			ProductParamDTO dto = new ProductParamDTO(param);
			if (param.getDetailType() != ProdDetailType.TEXT && param.getDetailType() != ProdDetailType.TEXT_AREA) {
				List<ProductParamOption> optList = getOptionList(param.getId());
				dto.setOptionList(optList);
			} else
				dto.setOptionList(new ArrayList<ProductParamOption>());
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public ProductDTO getProductDTO(long productId) {
		try {
			Product product = productDao.getProduct(productId);
			if (product == null)
				return null;
			long supplierId = product.getSupplierId();
			ProductDTO dto = new ProductDTO(product);
			List<Sku> skuList = skuDao.getSkuList(supplierId, productId);
			List<BaseSkuDTO> skuDTOList = new ArrayList<BaseSkuDTO>();
			if (skuList != null && skuList.size() > 0) {
				for (Sku sku : skuList) {
					BaseSkuDTO skuDTO = new BaseSkuDTO(sku);
					skuDTOList.add(skuDTO);
				}
			}
			dto.setSKUList(skuDTOList);

			List<String> showList = new ArrayList<String>();
			List<String> listList = new ArrayList<String>();
			List<ProdPicMap> picList = prodPicDao.getProdPicMap(productId, ConstantsUtil.NOT_IN_PO);
			if (picList != null && picList.size() > 0) {
				for (ProdPicMap picMap : picList) {
					String picPath = picMap.getPicPath();
					if (!StringUtils.isBlank(picPath)) {
						String[] pathAry = picPath.split(ProdPicMap.split);
						for (int i = 0; i < pathAry.length; i++) {
							String showPath = pathAry[i];
							showPath = ItemCenterUtil.genProdPicPath(showPath);
							if (picMap.getPicType() == PictureType.PROD)
								showList.add(showPath);
							else
								listList.add(showPath);
						}
					}
				}
			}
			dto.setProdShowPicList(showList);
			dto.setListShowPicList(listList);
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public ProductFullDTO getProductFullDTO(long productId) {
		try {
			Product product = productDao.getProduct(productId);
			if (product == null)
				return null;
			long supplierId = product.getSupplierId();
			long pid = product.getId();
			ProductDetail detail = detailDao.getProductDetail(supplierId, pid);
			if (detail == null)
				return null;
			ProductFullDTO dto = new ProductFullDTO(product, detail);
			List<Sku> skuList = skuDao.getSkuList(supplierId, productId);
			List<BaseSkuDTO> skuDTOList = new ArrayList<BaseSkuDTO>();
			if (skuList != null && skuList.size() > 0) {
				for (Sku sku : skuList) {
					BaseSkuDTO skuDTO = new BaseSkuDTO(sku);
					skuDTOList.add(skuDTO);
				}
			}
			dto.setSKUList(skuDTOList);

			List<String> showList = new ArrayList<String>();
			List<String> listList = new ArrayList<String>();
			List<ProdPicMap> picList = prodPicDao.getProdPicMap(productId, ConstantsUtil.NOT_IN_PO);
			if (picList != null && picList.size() > 0) {
				for (ProdPicMap picMap : picList) {
					String picPath = picMap.getPicPath();
					if (!StringUtils.isBlank(picPath)) {
						String[] pathAry = picPath.split(ProdPicMap.split);
						for (int i = 0; i < pathAry.length; i++) {
							String showPath = pathAry[i];
							showPath = ItemCenterUtil.genProdPicPath(showPath);
							if (picMap.getPicType() == PictureType.PROD)
								showList.add(showPath);
							else
								listList.add(showPath);
						}
					}
				}
			}
			dto.setProdShowPicList(showList);
			dto.setListShowPicList(listList);
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<ProductParamOption> getOptionList(long optId) {
		return prodParamOptDao.getOptionList(optId);
	}

	@Override
	public List<Product> getProductListByGoodsNo(long supplierId, String goodsNo) {
		try {
			return productDao.getProductListByGoodsNo(supplierId, goodsNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<ProductFullDTO> getProductFullDTOListByGoodsNo(long supplierId, String goodsNo) {
		try {
			List<ProductFullDTO> retList = new ArrayList<ProductFullDTO>();
			List<Product> pList = productDao.getProductListByGoodsNo(supplierId, goodsNo);
			if (pList != null && pList.size() > 0) {
				for (Product product : pList) {
					long pid = product.getId();
					ProductDetail detail = detailDao.getProductDetail(supplierId, pid);
					if (detail == null)
						continue;
					ProductFullDTO dto = new ProductFullDTO(product, detail);
					retList.add(dto);
				}
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<Sku> getSkuList(long supplierId, long productId) {
		try {
			return skuDao.getSkuList(supplierId, productId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Cacheable(value = "prodSizeCache")
	public SizeAssist getSizeAssist(long id) {
		return sizeAssistDao.getObjectById(id);
	}

	@Override
	public SizeAssist getSizeAssistNoCache(long id) {
		return sizeAssistDao.getObjectById(id);
	}

	@Override
	public SizeAssist saveSizeAssist(SizeAssist sizeAssist) {
		logger.info(String.valueOf(sizeAssist.getBody().length()));
		return sizeAssistDao.saveObject(sizeAssist);
	}

	@Override
	public BaseSearchResult<SizeAssist> getSizeAssistList(long supplierId, int limit, int offset) {
		return sizeAssistDao.getSizeAssistList(supplierId, limit, offset);
	}

	@Override
	public int deleteSizeAssistById(long supplerId, long id) {
		try {
			List<Product> list = productDao.getListBySizeAssistId(supplerId, id);
			if (list != null && list.size() > 0)
				return ErrorCode.DELETE_FAILED.getIntValue();
			List<PoProduct> list2 = poProductDao.getListBySizeAssistId(supplerId, id);
			if (list2 != null && list2.size() > 0)
				return ErrorCode.DELETE_FAILED.getIntValue();
			sizeAssistDao.deleteById(id);
			return ErrorCode.SUCCESS.getIntValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ErrorCode.SERVICE_EXCEPTION.getIntValue();
		}
	}

	@Override
	public Map<String, Long> getProductParamOptMap(long lowCategoryId) {
		Map<String, Long> retMap = new HashMap<String, Long>();
		Category c = categoryDao.getCategoryById(lowCategoryId);

		String paramValue = c.getParameter();
		if (!StringUtils.isBlank(paramValue)) {
			List<Long> list = JsonUtils.parseArray(paramValue, Long.class);
			if (list != null && list.size() > 0) {
				for (long paramId : list) {
					List<ProductParamOption> optList = ProductParamOptDaoImpl.OPT_MAP.get(paramId);
					if (optList != null && optList.size() > 0) {
						for (ProductParamOption opt : optList) {
							retMap.put(opt.getValue(), opt.getId());
						}
					}
				}
			}
		}
		return retMap;
	}

	@Override
	public List<Map<String, Object>> getProductParamExport(List<Long> categoryList) {
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		List<Category> subList = categoryDao.getCategoryList(categoryList);
		for (Category cc : subList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("category", cc);
			List<ProductParamDTO> attrHeader = new ArrayList<ProductParamDTO>();
			String paramValue = cc.getParameter();

			List<Long> paramValueList = JsonUtils.parseArray(paramValue, Long.class);
			if (paramValueList != null && paramValueList.size() > 0) {
				for (Long paramVal : paramValueList) {
					ProductParameter paramter = productParamDao.getObjectById(paramVal);
					ProductParamDTO paire = new ProductParamDTO(paramter);
					List<ProductParamOption> optList = prodParamOptDao.getOptionList(paramter.getId());
					paire.setOptionList(optList);
					attrHeader.add(paire);
				}
			}
			map.put("paramList", attrHeader);
			retList.add(map);
		}
		return retList;
	}

	// TODO
	@Override
	public List<ProductDTO> searchProduct(POProductSearchParam searchDTO) {
		try {
			BaseSearchResult<Product> prodSearchResult = productDao.searchProduct(searchDTO);
			List<Product> productList = prodSearchResult.getList();
			List<ProductDTO> retList = new ArrayList<ProductDTO>();
			if (productList != null && productList.size() > 0) {
				for (Product product : productList) {
					ProductDTO dto = new ProductDTO(product);
					long supplierId = product.getSupplierId();
					long productId = product.getId();
					List<Sku> skuList = skuDao.getSkuList(supplierId, productId);
					List<BaseSkuDTO> baseList = new ArrayList<BaseSkuDTO>();
					if (skuList != null && skuList.size() > 0) {
						for (Sku sku : skuList) {
							BaseSkuDTO baseSku = new BaseSkuDTO(sku);
							baseList.add(baseSku);
						}
					}
					dto.setSKUList(baseList);
					retList.add(dto);
				}
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public Sku getSku(long supplierId, String goodsNo, String colorNum, String barCode) {
		Product p = productDao.getProduct(supplierId, goodsNo, colorNum);
		if (p == null)
			return null;
		else {
			return skuDao.getSku(supplierId, p.getId(), barCode);
		}
	}

	@Override
	public Sku getSku(long supplierId, String barCode) {
		return skuDao.getSkuByBarCode(supplierId, barCode);
	}

	@Override
	public Map<String, String> getGoodsNoAndColorNum(long supplierId, String barcode) {
		Map<String, String> retMap = new HashMap<String, String>();
		Sku sku = skuDao.getSkuByBarCode(supplierId, barcode);
		if (sku != null) {
			Product p = productDao.getProduct(sku.getProductId());
			retMap.put("goodsNo", p.getGoodsNo());
			retMap.put("colorNum", p.getColorNum());
			retMap.put("lowCategoryId", String.valueOf(p.getLowCategoryId()));
		}
		return retMap;
	}

	@Override
	public Map<String, Long> getProductParamForBat(long paramId) {
		Map<String, Long> retMap = new HashMap<String, Long>();
		List<ProductParamOption> list = prodParamOptDao.getOptionList(paramId);
		if (list != null && list.size() > 0) {
			for (ProductParamOption opt : list) {
				retMap.put(opt.getValue(), opt.getId());
			}
		}
		return retMap;
	}

	@Override
	public ProductParameter getProductParam(long paramId) {
		return productParamDao.getObjectById(paramId);
	}

	@Cacheable("proPriceCache")
	@Override
	public List<ProductPriceDTO> getProductPriceDTOByProductId(long productId) {
		return productPriceDao.getProductPriceDTOByProductId(productId);
	}

	@Override
	public Map<String,List<ProductPriceDTO>> getProductPriceDTOByProductIds(List<Long> productIds){
		if(productIds==null||productIds.size()==0){
			return null;
		}
		List<ProductPrice> productPrices = productPriceDao.getProductPriceByProductIds(productIds);
		Map<String, List<ProductPriceDTO>> productMap = new HashMap<String, List<ProductPriceDTO>>();
		List<ProductPriceDTO> productPriceList = null;
		if (productPrices != null &&  productPrices.size() > 0) {
			for(ProductPrice productPrice:productPrices){
				productPriceList = productMap.get(String.valueOf(productPrice.getProductId()));
				if(productPriceList==null){
					productPriceList = new ArrayList<ProductPriceDTO>();
					 productMap.put(String.valueOf(productPrice.getProductId()), productPriceList);
				}
				productPriceList.add(new ProductPriceDTO(productPrice));
			}
		}
		return productMap;
	}
}