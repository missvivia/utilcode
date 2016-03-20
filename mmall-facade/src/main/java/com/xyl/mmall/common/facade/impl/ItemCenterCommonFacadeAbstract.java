package com.xyl.mmall.common.facade.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.util.ProductParamComparator;
import com.xyl.mmall.backend.vo.BrandVO;
import com.xyl.mmall.backend.vo.ProdParamOption;
import com.xyl.mmall.backend.vo.ProductParamVO;
import com.xyl.mmall.backend.vo.SizeAssistVO;
import com.xyl.mmall.backend.vo.SizeHeaderVO;
import com.xyl.mmall.backend.vo.SizeTmplTableVO;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.util.ItemCenterUtils;
import com.xyl.mmall.framework.exception.AppException;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.util.NOSUtil;
import com.xyl.mmall.itemcenter.dto.BaseSkuDTO;
import com.xyl.mmall.itemcenter.dto.CategoryArchitect;
import com.xyl.mmall.itemcenter.dto.CategoryVO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductDTO;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductParamDTO;
import com.xyl.mmall.itemcenter.enums.ProdDetailType;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.ProductParamOption;
import com.xyl.mmall.itemcenter.meta.SizeAssist;
import com.xyl.mmall.itemcenter.param.ProdParamParam;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.SizeColumnParam;
import com.xyl.mmall.itemcenter.param.SizeTable;
import com.xyl.mmall.itemcenter.param.SkuSaveParam;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.itemcenter.util.ConstantsUtil;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.mainsite.util.MainsiteHelper;
import com.xyl.mmall.mainsite.util.SkuSpecComparator;
import com.xyl.mmall.mainsite.vo.BaseNameValueVO;
import com.xyl.mmall.mainsite.vo.DetailColorVO;
import com.xyl.mmall.mainsite.vo.DetailPOVO;
import com.xyl.mmall.mainsite.vo.DetailProductVO;
import com.xyl.mmall.mainsite.vo.SizeSpecVO;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.service.SkuOrderStockService;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.meta.Brand;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

public abstract class ItemCenterCommonFacadeAbstract {
	private static final Logger logger = LoggerFactory.getLogger(ItemCenterCommonFacadeAbstract.class);

	@Resource
	private SizeTemplateService sizeTemplateService;

	@Resource
	private CartService cartService;

	@Resource
	private SkuOrderStockService skuOrderStockService;

	@Resource
	private ProductService productService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private BrandFacade brandFacade;

	@Resource
	private BrandService brandService;

	@Resource
	private POProductService poProductService;

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private BusinessService businessService;

	@Autowired
	private MainsiteHelper mainsiteHelper;

	public DetailProductVO getDetailPageProduct(long pid, long poId) {
		try {
			int status = 3;
			ProductFullDTO product = getProductDTO(poId, pid);
			DetailProductVO productVO = loadData(product);
			productVO.setStatus(status);
			List<SizeSpecVO> specList = productVO.getSizeSpecList();
			if (poId > 0) {
				PODTO po = scheduleService.getScheduleById(poId);
				DetailPOVO poVO = getPOVO(po);
				productVO.setSchedule(poVO);
				productVO.setStatus(getProductTypePrivate(po, specList));
				if (productVO.getStatus() != 2 || productVO.getStatus() != 3)
					getStock(specList);
				int skuNum = 0;
				int skuStock = 0;
				if (specList != null && specList.size() > 0) {
					for (SizeSpecVO sku : specList) {
						int num1 = sku.getTotal();
						skuNum = skuNum + num1;
						int stock = sku.getStock();
						skuStock = skuStock + stock;
					}
				}
				productVO.setSellNum(skuNum - skuStock);
			}
			return productVO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	public DetailProductVO getDetailPageProduct(long pid, boolean isPo, boolean isCache) {
		try {
			int status = 3;
			DetailProductVO productVO = null;
			if (!isCache) {
				ProductFullDTO product = getProductDTO(isPo, pid);
				productVO = loadData(product);
			} else if (isPo) {
				productVO = mainsiteHelper.getProductData(pid);
			} else {
				ProductFullDTO product = getProductDTOCache(isPo, pid);
				productVO = loadData(product);
			}
			productVO.setStatus(status);
			List<SizeSpecVO> specList = productVO.getSizeSpecList();
			if (isPo) {
				getStock(specList);
				long poId = Long.valueOf(productVO.getPoId());
				PODTO po = scheduleService.getScheduleById(poId);
				DetailPOVO poVO = getPOVO(po);
				productVO.setSchedule(poVO);
				productVO.setStatus(getProductTypePrivate(po, specList));
				int skuNum = 0;
				int skuStock = 0;
				if (specList != null && specList.size() > 0) {
					for (SizeSpecVO sku : specList) {
						int num1 = sku.getTotal();
						skuNum = skuNum + num1;
						int stock = sku.getStock();
						skuStock = skuStock + stock;
					}
				}
				productVO.setSellNum(skuNum - skuStock);
			}
			return productVO;
		} catch (Exception e) {
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	public DetailProductVO getDetailPageProducForAPP(long pid) {
		try {
			int status = 3;
			ProductFullDTO product = getProductDTO(true, pid);
			DetailProductVO productVO = loadData(product);
			productVO.setStatus(status);
			List<SizeSpecVO> specList = productVO.getSizeSpecList();
			getStock(specList);
			long poId = ((PoProductFullDTO) product).getPoId();
			PODTO po = scheduleService.getScheduleById(poId);
			DetailPOVO poVO = getPOVOPure(po);
			productVO.setSchedule(poVO);
			productVO.setStatus(getProductTypePrivate(po, specList));
			return productVO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	public List<ProductParamDTO> getProductParamList(long categoryId, String paramValue) {
		try {
			List<ProductParamDTO> retList = productService.getProductParamList(categoryId);

			if (!StringUtils.isBlank(paramValue)) {
				List<ProdParamParam> paramValueList = JsonUtils.parseArray(paramValue, ProdParamParam.class);
				if (paramValueList != null && paramValueList.size() > 0) {
					for (ProdParamParam paramVal : paramValueList) {
						long paramId = paramVal.getId();
						String paramVl = paramVal.getValue();
						for (ProductParamDTO dto : retList) {
							long tparamId = dto.getId();
							if (tparamId == paramId) {
								dto.setParamValue(paramVl);
								break;
							}
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

	private List<BaseNameValueVO> _getProductParamVOList(long categoryId, String paramValue) {
		try {
			List<BaseNameValueVO> retList = new ArrayList<BaseNameValueVO>();

			if (!StringUtils.isBlank(paramValue)) {
				List<ProdParamParam> paramValueList = JsonUtils.parseArray(paramValue, ProdParamParam.class);
				if (paramValueList != null && paramValueList.size() > 0) {
					for (ProdParamParam paramVal : paramValueList) {
						long paramId = paramVal.getId();
						String paramVl = paramVal.getValue();
						if (!StringUtils.isBlank(paramVl)) {
							ProductParamDTO dto = productService.getProductParamDTO(paramId);

							BaseNameValueVO pvo = new BaseNameValueVO();
							pvo.setName(dto.getName());
							pvo.setType(dto.getDetailType().getIntValue());
							if (dto.getDetailType() == ProdDetailType.SINGLE_SELECT) {
								long optId = Long.valueOf(paramVl);
								List<ProductParamOption> optList = dto.getOptionList();
								for (ProductParamOption opt : optList) {
									if (opt.getId() == optId) {
										pvo.setValue(opt.getValue());
									}
								}
							} else if (dto.getDetailType() == ProdDetailType.MULTI_SELECT) {
								List<Long> paramValues = JsonUtils.parseArray(paramVl, Long.class);
								List<ProductParamOption> optList = dto.getOptionList();
								StringBuffer sb = new StringBuffer();
								if (paramValues != null && paramValues.size() > 0) {
									for (long sOptId : paramValues) {
										for (ProductParamOption opt : optList) {
											if (opt.getId() == sOptId) {
												sb.append(opt.getValue()).append("，");
											}
										}
									}
									pvo.setValue(sb.toString().trim());
								} else
									continue;

							} else {
								pvo.setValue(paramVl);
							}
							retList.add(pvo);
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

	public Map<Long, Integer> getOrderSkuStock(List<Long> skuIdList) {
		Map<Long, Integer> retMap = new HashMap<Long, Integer>();
		List<SkuOrderStockDTO> list = skuOrderStockService.getSkuOrderStockDTOListBySkuIds(skuIdList);
		if (list != null && list.size() > 0) {
			for (SkuOrderStockDTO orderStock : list) {
				long skuId = orderStock.getSkuId();
				int stock = orderStock.getStockCount();
				retMap.put(skuId, stock);
			}
		}
		return retMap;
	}

	public void getStock(List<SizeSpecVO> specList) {

		if (specList != null && specList.size() > 0) {
			Map<Long, Integer> cartStock = null;
			Map<Long, Integer> orderStock = null;
			List<Long> skuIdList = new ArrayList<Long>();
			if (specList != null && specList.size() > 0) {
				for (SizeSpecVO size : specList) {
					long skuId = Long.valueOf(size.getSkuId());
					skuIdList.add(skuId);
				}
			}
			try {
				cartStock = cartService.getInventoryCount(skuIdList);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				cartStock = new HashMap<Long, Integer>();
			}
			try {
				orderStock = getOrderSkuStock(skuIdList);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				orderStock = new HashMap<Long, Integer>();
			}
			for (SizeSpecVO size : specList) {
				long skuId = Long.valueOf(size.getSkuId());
				Integer tmpCartStock = cartStock.get(skuId);
				if (tmpCartStock == null) {
					tmpCartStock = 0;
				}
				size.setNum(tmpCartStock);
				Integer tmpOrderStock = orderStock.get(skuId);
				if (tmpOrderStock == null) {
					tmpOrderStock = 0;
				}
				size.setStock(tmpOrderStock);
				size.setType(ItemCenterUtils.getStockType(tmpCartStock, tmpOrderStock));
			}
		}
	}

	public List<ProductParamVO> getProductParamVOList(long categoryId, String paramValue) {
		try {
			List<ProductParamVO> retList = new ArrayList<ProductParamVO>();
			List<ProductParamDTO> list = getProductParamList(categoryId, paramValue);
			for (ProductParamDTO p : list) {
				ProductParamVO pvo = new ProductParamVO();
				pvo.setId(String.valueOf(p.getId()));
				pvo.setName(p.getName());
				pvo.setIsRequired(p.getIsRequired());
				pvo.setType(p.getDetailType().getIntValue());
				pvo.setValue(p.getParamValue());
				List<ProductParamOption> optList = p.getOptionList();
				List<ProdParamOption> optVOList = new ArrayList<ProdParamOption>();
				if (optList != null) {
					for (ProductParamOption opt : optList) {
						ProdParamOption optVO = new ProdParamOption();
						optVO.setOptId(String.valueOf(opt.getId()));
						optVO.setOptValue(opt.getValue());
						optVOList.add(optVO);
					}
				}
				pvo.setList(optVOList);
				retList.add(pvo);
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	public DetailProductVO transferDetailProductVO(ProductSaveParam productSave) {
		DetailProductVO vo = new DetailProductVO();
		ItemCenterUtil.copyData(productSave, vo);
		Brand brand = brandFacade.getBrandBySupplierId(productSave.getSupplierId()).getBrand();
		BrandVO brandVO = new BrandVO();
		brandVO.setId(brand.getBrandId());
//		brandVO.setName(brand.getBrandNameAuto());
		brandVO.setNameZH(brand.getBrandNameZh());
		brandVO.setNameEN(brand.getBrandNameEn());
		vo.setBrand(brandVO);
		long supplierId = productSave.getSupplierId();
		Business tmpSupplier = businessService.getBusinessById(supplierId, -1);
		vo.setCompanyName(tmpSupplier.getCompanyName());

		vo.setPreview(1);
		List<SkuSaveParam> skuList = productSave.getSKUList();
		List<SizeSpecVO> sizeSpecList = new ArrayList<SizeSpecVO>();
		if (productSave.getSizeType() == SizeType.ORIG_SIZE.getIntValue()) {
			for (SkuSaveParam sku : skuList) {
				SizeSpecVO sizeSpec = new SizeSpecVO();
				sizeSpec.setType(1);
				sizeSpec.setSize(ConstantsUtil.DEFAULT_SIZE);
				sizeSpec.setSizeTips(null);
				sizeSpecList.add(sizeSpec);
			}
		} else if (productSave.getSizeType() == SizeType.TMPL_SIZE.getIntValue()) {
			SizeTable table = getSizeTable(productSave.getSizeTemplateId(), SizeType.TMPL_SIZE);
			long categoryId = productSave.getLowCategoryId();
			if (isClothProduct(categoryId)) {
				vo.setProductSize(ItemCenterUtils.sizeTableVOTransfer(table));
				long helperId = productSave.getSizeAssistId();
				if (helperId > 0) {
					SizeAssistVO assistVO = getSizeAssistVO(helperId);
					vo.setHelper(assistVO);
				}
				if (productSave.getIsShowSizePic())
					vo.setIsShowSizePic(1);
				else
					vo.setIsShowSizePic(0);
			}
			List<SizeColumnParam> headerList = table.getSizeHeader();
			for (SkuSaveParam sku : skuList) {
				SizeSpecVO sizeSpec = new SizeSpecVO();
				sizeSpec.setType(1);
				List<BaseNameValueVO> sizeHover = new ArrayList<BaseNameValueVO>();
				StringBuffer sizeVal = new StringBuffer();
				long index = sku.getSizeIndex();
				sizeSpec.setSkuId(String.valueOf(sku.getId()));
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
				sizeSpec.setSize(sizeVal.toString());

				for (SizeColumnParam column : headerList) {
					long colId = column.getId();
					if (colId != ConstantsUtil.CHIMA_COLUMN && colId != ConstantsUtil.HAOXING_COLUMN) {
						String key = index + "+" + colId;
						String val = table.getValueMap().get(key);
						if (!StringUtils.isBlank(val)) {
							BaseNameValueVO nv = new BaseNameValueVO();
							nv.setName(column.getName());
							nv.setValue(table.getValueMap().get(key));
							sizeHover.add(nv);
						}
					}
				}
				sizeSpec.setSizeTips(sizeHover);
				sizeSpecList.add(sizeSpec);
			}
		} else {
			long categoryId = productSave.getLowCategoryId();
			if (isClothProduct(categoryId)) {
				long helperId = productSave.getSizeAssistId();
				if (helperId > 0) {
					SizeAssistVO assistVO = getSizeAssistVO(helperId);
					vo.setHelper(assistVO);
				}
				if (productSave.getIsShowSizePic())
					vo.setIsShowSizePic(1);
				else
					vo.setIsShowSizePic(0);

				SizeTmplTableVO sizeVO = new SizeTmplTableVO();
				List<SizeHeaderVO> header = new ArrayList<SizeHeaderVO>();
				List<SizeColumnParam> headerParamList = productSave.getSizeHeader();
				if (headerParamList != null && headerParamList.size() > 0) {
					for (SizeColumnParam headerParam : headerParamList) {
						SizeHeaderVO headerVO = new SizeHeaderVO();
						headerVO.setName(headerParam.getName());
						headerVO.setId(String.valueOf(headerParam.getId()));
						headerVO.setRequired(headerParam.getIsRequired());
						headerVO.setUnit(headerParam.getUnit());
						header.add(headerVO);
					}
				}
				sizeVO.setHeader(header);
				List<List<String>> body = new ArrayList<List<String>>();
				if (skuList != null && skuList.size() > 0) {
					for (SkuSaveParam skuParam : skuList) {
						List<String> sizeValue = skuParam.getCustomizedSizeValue();
						body.add(sizeValue);
					}
				}
				sizeVO.setBody(body);
				vo.setProductSize(sizeVO);
			}

			List<SizeColumnParam> sizeList = productSave.getSizeHeader();
			for (SkuSaveParam sku : skuList) {
				SizeSpecVO sizeSpec = new SizeSpecVO();
				sizeSpec.setType(1);
				List<BaseNameValueVO> sizeHover = new ArrayList<BaseNameValueVO>();
				List<String> sizeValues = sku.getCustomizedSizeValue();
				StringBuffer sizeVal = new StringBuffer();
				for (int i = 0; i < sizeList.size(); i++) {
					SizeColumnParam tmplSize = sizeList.get(i);
					String sizeValue = sizeValues.get(i);
					if (tmplSize.getId() == 1) {
						if (StringUtils.isBlank(sizeVal.toString())) {
							sizeVal.append(sizeValue);
						} else {
							sizeVal.insert(0, sizeValue);
						}
					} else if (tmplSize.getId() == 2) {
						sizeVal.append("(").append(sizeValue).append(")");
					} else {
						if (sizeValue != null) {
							BaseNameValueVO nv = new BaseNameValueVO();
							nv.setName(tmplSize.getName());
							nv.setValue(sizeValue);
							sizeHover.add(nv);
						}
					}
				}
				sizeSpec.setSize(sizeVal.toString());
				sizeSpec.setSizeTips(sizeHover);
				sizeSpecList.add(sizeSpec);
			}
		}
		vo.setSizeSpecList(sizeSpecList);
		vo.setStatus(3);

		String paramStr = JsonUtils.toJson(productSave.getProductParamList());
		List<ProductParamDTO> dtoParamList = getProductParamList2(productSave.getLowCategoryId(), paramStr);
		List<BaseNameValueVO> paramList = ItemCenterUtils.productParamVOTransfer(dtoParamList);
		Collections.sort(paramList, new ProductParamComparator());
		vo.setProductDetail(paramList);
		return vo;
	}

	/**
	 * 把一个category转化成CategoryArchitect
	 * 
	 * @param category
	 * @return
	 */
	public CategoryArchitect genCategoryArchitect(Category category) {
		List<CategoryArchitect> cList = new ArrayList<CategoryArchitect>();
		CategoryArchitect ca = new CategoryArchitect();
		ca.setId(String.valueOf(category.getId()));
		ca.setName(category.getName());
		List<Category> list = categoryService.getSubCategoryList(category.getId());
		if (list == null || list.size() == 0) {
			ca.setList(cList);
			return ca;
		} else {
			for (Category c : list) {
				CategoryArchitect nca = genCategoryArchitect(c);
				cList.add(nca);
			}
			ca.setList(cList);
			return ca;
		}
	}

	/**
	 * 生成类目结构
	 * 
	 * @return
	 */
	public List<CategoryArchitect> getCategoryArchitect() {
		try {
			List<Category> list = categoryService.getSubCategoryList(0);
			List<CategoryArchitect> cList = new ArrayList<CategoryArchitect>();
			for (Category c : list) {
				CategoryArchitect ca = genCategoryArchitect(c);
				cList.add(ca);
			}
			return cList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	public void operaProductSaveParam(ProductSaveParam productSaveParam) {
		try {
			BrandDTO brandDTO = brandFacade.getBrandBySupplierId(productSaveParam.getSupplierId());
			if (brandDTO == null)
				throw new AppException("brand is null!");
			long brandId = brandDTO.getBrand().getBrandId();
			productSaveParam.setBrandId(brandId);
			if (!StringUtils.isBlank(productSaveParam.getCustomEditHTML())
					|| (productSaveParam.getProductParamList() != null && productSaveParam.getProductParamList().size() > 0)) {
				Map<String, String> map = new HashMap<String, String>();
				String parameter = JsonUtils.toJson(productSaveParam.getProductParamList());
				map.put("parameter", parameter);
				map.put("html", productSaveParam.getCustomEditHTML());
				String saveStr = JsonUtils.toJson(map);
				ByteArrayInputStream is = new ByteArrayInputStream(saveStr.getBytes());
				String fileName = productSaveParam.getSupplierId() + "_" + productSaveParam.getGoodsNo() + "_"
						+ productSaveParam.getColorNum();
				String url = NOSUtil.uploadFile(is, fileName);
				productSaveParam.setNosURL(url);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	public List<DetailColorVO> getDetailPageColorList(long supplierId, String goodsNo) {
		try {
			List<DetailColorVO> retList = new ArrayList<DetailColorVO>();

			List<? extends ProductDTO> list = getProductListByGoodsNo(supplierId, goodsNo);
			for (ProductDTO product : list) {
				DetailColorVO color = new DetailColorVO();
				String showPath = product.getShowPicPath();
				if (showPath != null && showPath.indexOf(ConstantsUtil.NOS_URL2) < 0)
					showPath = ConstantsUtil.NOS_URL1 + showPath;
				color.setProductId(String.valueOf(product.getId()));
				color.setThumb(showPath);
				// TODO
				color.setProductURL("/detail?id=" + product.getId());
				color.setColorName(product.getColorName());
				retList.add(color);
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	private int getProductTypePrivate(PODTO po, List<SizeSpecVO> specList) {
		long poEndTime = po.getScheduleDTO().getSchedule().getEndTime();
		long poStartTime = po.getScheduleDTO().getSchedule().getStartTime();
		boolean isOver = true;
		int status = 1;
		if (specList != null && specList.size() > 0) {
			for (SizeSpecVO svo : specList) {
				if (svo.getType() != 3) {
					isOver = false;
					break;
				}
			}
		}
		if (poStartTime > new Date().getTime())
			status = 3;
		else if (poEndTime < new Date().getTime())
			status = 2;
		else if (isOver)
			status = 4;
		return status;
	}

	public int getProductType(PODTO po, List<POSkuDTO> skuList) {
		List<SizeSpecVO> specList = getSkuStock(skuList);
		long poEndTime = po.getScheduleDTO().getSchedule().getEndTime();
		long poStartTime = po.getScheduleDTO().getSchedule().getStartTime();
		boolean isOver = true;
		int status = 1;
		if (specList != null && specList.size() > 0) {
			for (SizeSpecVO svo : specList) {
				if (svo.getType() != 3) {
					isOver = false;
					break;
				}
			}
		}
		if (poStartTime > new Date().getTime())
			status = 3;
		else if (poEndTime < new Date().getTime())
			status = 2;
		else if (isOver)
			status = 4;
		return status;
	}

	// =======================================================================

	public DetailProductVO loadData(ProductFullDTO dto) {
		DetailProductVO vo = new DetailProductVO();
		ItemCenterUtil.copyData(dto, vo);
		if (vo.getProdShowPicList() == null)
			vo.setProdShowPicList(new ArrayList<String>());
		vo.setLength(dto.getLenth());
		vo.setIsRecommend(dto.isRecommend() ? 1 : 0);
		long pid = dto.getId();
		Map<String, String> map = ItemCenterUtil.getEditHTML_Param(dto.getCustomEditHTML(), dto.getProductParamValue());
		String html = map.get("html");
		String paramter = map.get("parameter");
		vo.setCustomEditHTML(html);
		List<BaseNameValueVO> prodParamList = _getProductParamVOList(dto.getLowCategoryId(), paramter);
		Collections.sort(prodParamList, new ProductParamComparator());
		vo.setProductDetail(prodParamList);
		long supplierId = dto.getSupplierId();
		Business tmpSupplier = businessService.getBusinessById(supplierId, -1);
		vo.setCompanyName(tmpSupplier.getCompanyName());
		BrandDTO brand = brandService.getBrandByBrandId(dto.getBrandId());
		BrandVO brandVO = new BrandVO();
		brandVO.setId(brand.getBrand().getBrandId());
//		brandVO.setName(brand.getBrand().getBrandNameAuto());
		brandVO.setNameZH(brand.getBrand().getBrandNameZh());
		brandVO.setNameEN(brand.getBrand().getBrandNameEn());
		vo.setBrand(brandVO);
		vo.setProductId(String.valueOf(pid));

		List<? extends BaseSkuDTO> skuList = dto.getSKUList();
		List<SizeSpecVO> specList = new ArrayList<SizeSpecVO>();
		long tmplateId = 0;
		if (dto.getSizeType() == SizeType.CUST_SIZE) {
			tmplateId = dto.getId();
		} else if (dto.getSizeType() == SizeType.TMPL_SIZE) {
			tmplateId = dto.getSizeTemplateId();
		}
		if (dto.getSizeType() != SizeType.ORIG_SIZE) {
			SizeTable table = getSizeTable(tmplateId, dto.getSizeType());
			List<SizeColumnParam> headerList = table.getSizeHeader();

			for (BaseSkuDTO sku : skuList) {
				SizeSpecVO spec = new SizeSpecVO();
				List<BaseNameValueVO> sizeHover = new ArrayList<BaseNameValueVO>();
				StringBuffer sizeVal = new StringBuffer();
				long index = sku.getSizeIndex();
				spec.setSkuId(String.valueOf(sku.getId()));
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
				spec.setSize(sizeVal.toString());

				for (SizeColumnParam column : headerList) {
					long colId = column.getId();
					if (colId != ConstantsUtil.CHIMA_COLUMN && colId != ConstantsUtil.HAOXING_COLUMN) {
						String key = index + "+" + colId;
						String val = table.getValueMap().get(key);
						if (!StringUtils.isBlank(val)) {
							BaseNameValueVO nv = new BaseNameValueVO();
							nv.setName(column.getName());
							nv.setValue(table.getValueMap().get(key));
							sizeHover.add(nv);
						}
					}
				}
				spec.setSizeTips(sizeHover);
				if (sku instanceof POSkuDTO)
					spec.setTotal(((POSkuDTO) sku).getSkuNum() + ((POSkuDTO) sku).getSupplierSkuNum());
				spec.setType(3);
				specList.add(spec);
			}
			if (isClothProduct(dto.getLowCategoryId())) {
				SizeTmplTableVO sizeVO = ItemCenterUtils.sizeTableVOTransfer(table);
				vo.setProductSize(sizeVO);
				vo.setHelper(getSizeAssistVO(dto.getSizeAssistId()));
				if (dto.isShowSizePic()) {
					vo.setIsShowSizePic(1);
				}
			}
		} else {
			for (BaseSkuDTO sku : skuList) {
				SizeSpecVO spec = new SizeSpecVO();
				spec.setSkuId(String.valueOf(sku.getId()));
				spec.setSize(ConstantsUtil.DEFAULT_SIZE);
				spec.setSizeTips(null);
				if (sku instanceof POSkuDTO)
					spec.setTotal(((POSkuDTO) sku).getSkuNum() + ((POSkuDTO) sku).getSupplierSkuNum());
				spec.setType(3);
				specList.add(spec);
			}
		}
		Collections.sort(specList, new SkuSpecComparator());
		vo.setSizeSpecList(specList);
		return vo;
	}

	private SizeAssistVO getSizeAssistVO(long id) {
		if (id <= 0) {
			return null;
		} else {
			SizeAssist assist = productService.getSizeAssist(id);
			if (assist == null) {
				return null;
			} else {
				if (!StringUtils.isBlank(assist.getBody())) {
					try {
						String html = ItemCenterUtil.InputStreamTOString(assist.getBody());
						assist.setBody(html);
					} catch (Exception e) {
						logger.error("old assist version####");
						assist.setBody(null);
					}
				}
				return ItemCenterUtils.getSizeAssistVO(assist);
			}
		}
	}

	private boolean isClothProduct(long categoryId) {
		Category c = categoryService.getFirstCategoryByLowestId(categoryId);
		if (c.getId() == 1 || c.getId() == 2 || c.getId() == 3)
			return true;
		else
			return false;
	}

	private List<ProductParamDTO> getProductParamList2(long categoryId, String paramValue) {
		try {
			List<ProductParamDTO> retList = new ArrayList<ProductParamDTO>();
			List<ProductParamDTO> tmpList = productService.getProductParamList(categoryId);

			if (!StringUtils.isBlank(paramValue)) {
				List<ProdParamParam> paramValueList = JsonUtils.parseArray(paramValue, ProdParamParam.class);
				if (paramValueList != null && paramValueList.size() > 0) {
					for (ProdParamParam paramVal : paramValueList) {
						long paramId = paramVal.getId();
						String paramVl = paramVal.getValue();
						for (ProductParamDTO dto : tmpList) {
							long tparamId = dto.getId();
							if (tparamId == paramId) {
								dto.setParamValue(paramVl);
								retList.add(dto);
								break;
							}
						}
					}
				}
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public List<CategoryVO> getCategoryList() {
		// 一级分类
		List<Category> firstList = categoryService.getCategoryListByLevel(1);
		if (firstList != null && firstList.size() > 0) {
			List<CategoryVO> retList = new ArrayList<CategoryVO>(firstList.size());
			for (Category category : firstList) {
				CategoryVO categoryVO = new CategoryVO(category);
				// 二级分类
				List<Category> secondList = categoryService.getSubCategoryList(categoryVO.getId());
				if (secondList != null && secondList.size() > 0) {
					List<CategoryVO> subList = new ArrayList<CategoryVO>(secondList.size());
					for (Category sub : secondList) {
						subList.add(new CategoryVO(sub));
					}
					categoryVO.setSubList(subList);
				} else {
					categoryVO.setSubList(new ArrayList<CategoryVO>(0));
				}
				retList.add(categoryVO);
			}
			return retList;
		} else {
			return new ArrayList<CategoryVO>(0);
		}
	}
	
	// abstract

	public abstract ProductFullDTO getProductDTO(long poId, long pid);

	public abstract ProductFullDTO getProductDTO(boolean isPo, long pid);

	public abstract ProductFullDTO getProductDTOCache(boolean isPo, long pid);

	public abstract List<Size> getSizeList(long tmplateId, SizeType SizeType);

	public abstract List<? extends ProductDTO> getProductListByGoodsNo(long supplierId, String goodsNo);

	public abstract DetailPOVO getPOVO(PODTO po);

	public abstract DetailPOVO getPOVOPure(PODTO po);

	public abstract List<SizeSpecVO> getSkuStock(List<POSkuDTO> skuList);

	public abstract SizeTable getSizeTable(long templatekey, SizeType sizeType);
}
