package com.xyl.mmall.backend.facade.impl;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.facade.ItemCenterFacade;
import com.xyl.mmall.backend.util.ConvertUtil;
import com.xyl.mmall.backend.vo.BacthUploadProduct;
import com.xyl.mmall.backend.vo.BatchUploadSize;
import com.xyl.mmall.backend.vo.ExportProductBodyVO;
import com.xyl.mmall.backend.vo.ProductEditVO;
import com.xyl.mmall.backend.vo.ProductParamVO;
import com.xyl.mmall.backend.vo.ProductSearchResultVO;
import com.xyl.mmall.backend.vo.ProductSearchVO;
import com.xyl.mmall.backend.vo.SizeAssistAxis;
import com.xyl.mmall.backend.vo.SizeAssistVO;
import com.xyl.mmall.backend.vo.SizeHeaderVO;
import com.xyl.mmall.backend.vo.SizeTempSearchResultVO;
import com.xyl.mmall.backend.vo.SizeTemplateArchitect;
import com.xyl.mmall.backend.vo.SizeTemplateEditVO;
import com.xyl.mmall.backend.vo.SizeTmplSearchVO;
import com.xyl.mmall.backend.vo.SizeTmplTableVO;
import com.xyl.mmall.backend.vo.SizeVO;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.common.util.ItemCenterUtils;
import com.xyl.mmall.excelparse.ExcelParseExceptionInfo;
import com.xyl.mmall.excelparse.ExcelParseExeption;
import com.xyl.mmall.excelparse.ExcelUtils;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.exception.AppException;
import com.xyl.mmall.framework.exception.ItemCenterException;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.util.NOSUtil;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.dto.CategoryArchitect;
import com.xyl.mmall.itemcenter.dto.ExcelExportProduct;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductSearchResultDTO;
import com.xyl.mmall.itemcenter.dto.SizeTemplateDTO;
import com.xyl.mmall.itemcenter.enums.ProdDetailType;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.Product;
import com.xyl.mmall.itemcenter.meta.ProductParameter;
import com.xyl.mmall.itemcenter.meta.SizeAssist;
import com.xyl.mmall.itemcenter.meta.SizeColumn;
import com.xyl.mmall.itemcenter.meta.SizeTemplate;
import com.xyl.mmall.itemcenter.meta.Sku;
import com.xyl.mmall.itemcenter.param.BatchUploadPicParam;
import com.xyl.mmall.itemcenter.param.BatchUploadSizeParam;
import com.xyl.mmall.itemcenter.param.BatchUploadSizeSku;
import com.xyl.mmall.itemcenter.param.ChangeProductNameParam;
import com.xyl.mmall.itemcenter.param.ProdParamParam;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.ProductSearchParam;
import com.xyl.mmall.itemcenter.param.SizeColumnParam;
import com.xyl.mmall.itemcenter.param.SizeTable;
import com.xyl.mmall.itemcenter.param.SizeTemplateSaveParam;
import com.xyl.mmall.itemcenter.param.SizeTemplateSearchParam;
import com.xyl.mmall.itemcenter.param.SkuSaveParam;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.service.BrandService;

@Facade
public class ItemCenterFacadeImpl extends PISItemCenterFacadeAbstract implements ItemCenterFacade {
	private static final Logger logger = LoggerFactory.getLogger(ItemCenterFacadeImpl.class);

	private static Map<Long, Map<String, Long>> PARAM_OPT_MAP = new HashMap<Long, Map<String, Long>>();

	private static Map<Long, ProductParameter> PARAM_MAP = new HashMap<Long, ProductParameter>();

	private final BigDecimal DEFAULT_ADDEDTAX = new BigDecimal(17);

	private final String HAXIS_NAME = "体重kg";

	private final String VAXIS_NAME = "身高cm";

	private final String HAXIS_VALUE = "[45,47.5,50,52.5,55,57.5,60,62.5,65,67.5,70,72.5,75,77.5,80,82.5,85,87.5,90]";

	private final String VAXIS_VALUE = "[155,160,165,170,175,180,185,190]";

	@Resource
	private CategoryService categoryService;

	@Resource
	private SizeTemplateService sizeTemplateService;

	@Resource
	private ProductService productService;

	@Resource
	private DealerService dealerService;

	@Resource
	private BrandService brandService;

	@Resource
	private BrandFacade brandFacade;

	@Resource(name = "commonFacade")
	private ItemCenterCommonFacade commonFacade;

	@Override
	public long getSupplierId(long loginId) {
		try {
			DealerDTO dealer = dealerService.findDealerById(loginId);
			if (dealer == null)
				throw new AppException(ErrorCode.USER_CHECKED_ERROR.getDesc());
			return dealer.getSupplierId();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public Map<String, String> getBrandName(long supplierId) {
		Map<String, String> retMap = new HashMap<String, String>();
		BrandDTO brandDTO = brandFacade.getBrandBySupplierId(supplierId);
		if (brandDTO == null)
			return null;
		retMap.put("English", brandDTO.getBrand().getBrandNameEn());
		retMap.put("China", brandDTO.getBrand().getBrandNameZh());
		return retMap;
	}

	@Override
	public long getSuperCategory(long lowCategory) {
		return categoryService.getFirstCategoryByLowestId(lowCategory).getId();
	}

	@Override
	public ProductEditVO getProductVO(long productId, long supplierId) {
		try {
			ProductEditVO vo = null;
			if (productId <= 0) {
				vo = getNewProductVO(supplierId);
			} else {
				ProductFullDTO productDTO = productService.getProductFullDTO(productId);
				if (productDTO == null)
					vo = getNewProductVO(supplierId);
				else
					vo = genProductVO(productDTO);
			}
			return vo;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public BaseJsonVO deleteProduct(long supplierId, long pid) {
		try {
			productService.deleteProduct(supplierId, pid);
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public BaseJsonVO deleteProducts(long supplierId, List<Long> ids) {
		try {
			productService.deleteProducts(supplierId, ids);
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public SizeTemplateEditVO getSizeTemplateVO(long sizeTemplateId) {
		try {
			if (sizeTemplateId <= 0) {
				return getNewSizeTemplateEditVO();
			}
			SizeTemplate sizeTemplate = sizeTemplateService.getSizeTemplate(sizeTemplateId);
			return genSizeTemplateVO(sizeTemplate);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	private ProductEditVO getNewProductVO(long supplierId) {
		ProductEditVO productVO = new ProductEditVO();
		List<CategoryArchitect> categoryList = commonFacade.getCategoryArchitect();
		productVO.setCategoryList(categoryList);
		if (categoryList.isEmpty())
			new AppException("categoryArchitect is empty");
		List<String> categories = new ArrayList<String>();
		ItemCenterUtils.genSelectCategories(categories, categoryList.get(0));
		productVO.setCategories(categories);
		if (categories.isEmpty())
			new AppException("category is empty");
		long lowCategoryId = Long.valueOf(categories.get(categories.size() - 1));
		SizeVO sizeVo = genSizeVO(0, lowCategoryId, 0, supplierId);
		productVO.setTemplate(sizeVo);

		long categoryId = Long.valueOf(categories.get(categories.size() - 1));
		List<ProductParamVO> pvoList = commonFacade.getProductParamVOList(categoryId, null);
		productVO.setProductParamList(pvoList);
		BrandDTO brandDTO = brandFacade.getBrandBySupplierId(supplierId);
		if (brandDTO == null)
			throw new AppException("brand is null!");
		productVO.setBrandId(String.valueOf(brandDTO.getBrand().getBrandId()));
		productVO.setBrandName(brandDTO.getBrand().getBrandNameAuto());
		return productVO;
	}

	private SizeTemplateEditVO getNewSizeTemplateEditVO() {
		SizeTemplateEditVO editVO = new SizeTemplateEditVO();
		List<CategoryArchitect> categoryList = commonFacade.getCategoryArchitect();
		if (categoryList.isEmpty())
			new AppException("categoryArchitect is empty");
		editVO.setCategoryList(categoryList);
		List<String> categories = new ArrayList<String>();
		ItemCenterUtils.genSelectCategories(categories, categoryList.get(0));
		if (categories.isEmpty())
			new AppException("category is empty");
		editVO.setCategories(categories);
		long lowCategoryId = Long.valueOf(categories.get(categories.size() - 1));
		SizeTmplTableVO sizeTableVO = genSizeTemplateTable(lowCategoryId, 0);
		editVO.setSizeTable(sizeTableVO);
		return editVO;
	}

	@Override
	public BaseJsonVO SingleSaveProduct(ProductSaveParam productSaveParam) {
		Product p = productService.getProductByUniq(productSaveParam.getSupplierId(), productSaveParam.getGoodsNo(),
				productSaveParam.getColorNum());
		if (p != null && p.getId() != productSaveParam.getId()) {
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.PROD_REPLICATION);
			retObj.setMessage("该商品的货号、色号与已录入的商品重复！");
			return retObj;
		}
		try {
			commonFacade.operaProductSaveParam(productSaveParam);
			productService.SingleSaveProduct(productSaveParam);
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
			commonFacade.operaProductSaveParam(productSaveParam);
			productService.saveProduct(productSaveParam);
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public BaseJsonVO searchProduct(ProductSearchVO searchVO) {
		try {
			BaseJsonListResultVO retVO = new BaseJsonListResultVO();
			ProductSearchParam param = null;
			try {
				param = ConvertUtil.convertProdSearchVOToDTO(searchVO);
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
				return ItemCenterExceptionHandler.errorBaseJsonVO(ErrorCode.OBJECT_TRANSFER_FAILED);
			}
			BaseSearchResult<ProductSearchResultDTO> searchResult = productService.searchProduct(param);
			List<ProductSearchResultDTO> dtoList = searchResult.getList();
			List<ProductSearchResultVO> cList = new ArrayList<ProductSearchResultVO>();
			if (dtoList != null && dtoList.size() > 0)
				for (ProductSearchResultDTO c : dtoList) {
					ProductSearchResultVO vo = new ProductSearchResultVO();
					vo.setCategoryName(c.getCategoryName());
					vo.setColorName(c.getColorName());
					vo.setGoodsNo(c.getGoodsNo());
					vo.setId(String.valueOf(c.getId()));
					vo.setProductName(c.getProductName());
					vo.setShowPicPath(c.getShowPicPath());
					if ((c.getInfoFlag() & 8) > 0)
						vo.setIsBaseInfoInput(1);
					else
						vo.setIsBaseInfoInput(0);

					if ((c.getInfoFlag() & 1) > 0)
						vo.setIsDetailInfoInput(1);
					else
						vo.setIsDetailInfoInput(0);

					if ((c.getInfoFlag() & 2) > 0)
						vo.setIsPicInfoInput(1);
					else
						vo.setIsPicInfoInput(0);

					if ((c.getInfoFlag() & 4) > 0)
						vo.setIsSizeInfoInput(1);
					else
						vo.setIsSizeInfoInput(0);
					cList.add(vo);
				}
			retVO.setList(cList);
			retVO.setHasNext(searchResult.isHasNext());
			retVO.setTotal(searchResult.getTotal());
			BaseJsonVO retObj = new BaseJsonVO(retVO);
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public List<ExportProductBodyVO> searchExportProduct(long supplierId, List<Long> pids) {
		try {
			BrandDTO brandDTO = brandFacade.getBrandBySupplierId(supplierId);
			List<ExportProductBodyVO> retList = new ArrayList<ExportProductBodyVO>();
			List<ExcelExportProduct> list = productService.searchExportProduct(supplierId, pids);
			if (list != null && list.size() > 0) {
				for (ExcelExportProduct dto : list) {
					long supId = categoryService.getFirstCategoryByLowestId(dto.getLowCategoryId()).getId();
					if (supId == 1 || supId == 2 || supId == 3 || supId == 5) {
						ExportProductBodyVO vo = ConvertUtil.exportProductTransfer(dto, supId);
						vo.setSuperCategoryId(supId);
						vo.setBrandName(brandDTO.getBrand().getBrandNameAuto());
						retList.add(vo);
					}
				}
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<ExportProductBodyVO> searchExportProduct(ProductSearchVO searchVO) {
		try {
			BrandDTO brandDTO = brandFacade.getBrandBySupplierId(searchVO.getSupplierId());
			ProductSearchParam param = null;
			try {
				param = ConvertUtil.convertProdSearchVOToDTO(searchVO);
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
				return null;
			}

			List<ExportProductBodyVO> retList = new ArrayList<ExportProductBodyVO>();
			List<ExcelExportProduct> list = productService.searchExportProduct(param);
			if (list != null && list.size() > 0) {
				for (ExcelExportProduct dto : list) {
					long supId = categoryService.getFirstCategoryByLowestId(dto.getLowCategoryId()).getId();
					if (supId == 1 || supId == 2 || supId == 3 || supId == 5) {
						ExportProductBodyVO vo = ConvertUtil.exportProductTransfer(dto, supId);
						vo.setSuperCategoryId(supId);
						vo.setBrandName(brandDTO.getBrand().getBrandNameAuto());
						retList.add(vo);
					}

				}
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public BaseJsonVO searchSizeTemplate(SizeTmplSearchVO searchVO) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			BaseJsonListResultVO retVO = new BaseJsonListResultVO();
			SizeTemplateSearchParam param = (SizeTemplateSearchParam) ItemCenterUtil.extractData(searchVO,
					SizeTemplateSearchParam.class);
			BaseSearchResult<SizeTemplateDTO> searchResult = sizeTemplateService.searchSizeTemplate(param);
			List<SizeTemplateDTO> dtoList = searchResult.getList();
			List<SizeTempSearchResultVO> cList = new ArrayList<SizeTempSearchResultVO>();
			if (dtoList != null && dtoList.size() > 0)
				for (SizeTemplateDTO c : dtoList) {
					SizeTempSearchResultVO vo = (SizeTempSearchResultVO) ItemCenterUtil.extractData(c,
							SizeTempSearchResultVO.class);
					vo.setModifyTime(sdf.format(new Date(c.getLastModifyTime())));
					cList.add(vo);
				}
			retVO.setList(cList);
			retVO.setHasNext(searchResult.isHasNext());
			retVO.setTotal(searchResult.getTotal());
			if (cList != null && cList.size() > 0)
				retVO.setLastId(cList.get(cList.size() - 1).getId());
			else
				retVO.setLastId(0);
			BaseJsonVO retObj = new BaseJsonVO(retVO);
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	private SizeTmplTableVO genSizeTemplateTable(long lowCategoryId, long sizeTemplateId) {
		try {
			SizeTmplTableVO retVO = new SizeTmplTableVO();
			if (sizeTemplateId <= 0) {
				long oSizeId = sizeTemplateService.getOriginalSizeId(lowCategoryId);
				List<SizeHeaderVO> headerVO = getSizeHeaderVOList(oSizeId, SizeType.ORIG_SIZE);
				retVO.setHeader(headerVO);
				retVO.setBody(new ArrayList<List<String>>());
				return retVO;
			}

			SizeTable table = getSizeTable(sizeTemplateId, SizeType.TMPL_SIZE);
			List<SizeColumnParam> header = table.getSizeHeader();
			List<Long> recordList = table.getRecordList();
			List<SizeHeaderVO> headerVO = new ArrayList<SizeHeaderVO>();
			List<List<String>> body = new ArrayList<List<String>>();
			for (SizeColumnParam head : header) {
				SizeHeaderVO vo = new SizeHeaderVO();
				vo.setId(String.valueOf(head.getId()));
				vo.setName(head.getName());
				vo.setUnit(head.getUnit());
				vo.setRequired(head.getIsRequired());
				headerVO.add(vo);
			}

			if (recordList != null && recordList.size() > 0) {
				for (Long record : recordList) {
					List<String> values = new ArrayList<String>();
					for (SizeColumnParam head : header) {
						String key = record + "+" + head.getId();
						values.add(table.getValueMap().get(key));
					}
					body.add(values);
				}
			}
			retVO.setHeader(headerVO);
			retVO.setBody(body);
			return retVO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public SizeTmplTableVO getSizeTemplateTable(long lowCategoryId, long sizeTemplateId) {
		try {
			SizeTmplTableVO retVO = new SizeTmplTableVO();
			List<SizeHeaderVO> headerVO = null;
			long oldCategory = 0;
			if (sizeTemplateId <= 0) {
				long oSizeId = sizeTemplateService.getOriginalSizeId(lowCategoryId);
				headerVO = getSizeHeaderVOList(oSizeId, SizeType.ORIG_SIZE);
				retVO.setHeader(headerVO);
				retVO.setBody(new ArrayList<List<String>>());
				return retVO;
			} else {
				SizeTemplate sizeTemplate = sizeTemplateService.getSizeTemplate(sizeTemplateId);
				oldCategory = sizeTemplate.getLowCategoryId();
				if (oldCategory == lowCategoryId) {
					SizeTable table = getSizeTable(sizeTemplateId, SizeType.TMPL_SIZE);
					List<SizeColumnParam> header = table.getSizeHeader();
					List<Long> recordList = table.getRecordList();
					headerVO = new ArrayList<SizeHeaderVO>();
					List<List<String>> body = new ArrayList<List<String>>();
					for (SizeColumnParam head : header) {
						SizeHeaderVO vo = new SizeHeaderVO();
						vo.setId(String.valueOf(head.getId()));
						vo.setName(head.getName());
						vo.setUnit(head.getUnit());
						vo.setRequired(head.getIsRequired());
						headerVO.add(vo);
					}

					if (recordList != null && recordList.size() > 0) {
						for (Long record : recordList) {
							List<String> values = new ArrayList<String>();
							for (SizeColumnParam head : header) {
								String key = record + "+" + head.getId();
								values.add(table.getValueMap().get(key));
							}
							body.add(values);
						}
					}
					retVO.setHeader(headerVO);
					retVO.setBody(body);
					return retVO;
				} else {
					long oSizeId = sizeTemplateService.getOriginalSizeId(lowCategoryId);
					headerVO = getSizeHeaderVOList(oSizeId, SizeType.ORIG_SIZE);
					retVO.setHeader(headerVO);
					retVO.setBody(new ArrayList<List<String>>());
					return retVO;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public BaseJsonVO saveSizeTemplate(SizeTemplateSaveParam sizeTemplate) {
		try {
			sizeTemplateService.saveSizeTemplate(sizeTemplate);
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public BaseJsonVO batchChangeProductName(ChangeProductNameParam param) {
		try {
			productService.batchChangeProductName(param);
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public BaseJsonVO deleteSizeTemplate(long supplierId, long sizeTemplateId) throws ItemCenterException {
		try {
			int result = sizeTemplateService.deleteSizeTemplate(supplierId, sizeTemplateId);
			return ItemCenterExceptionHandler.getAjaxJsonVO(result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public SizeAssistVO getSizeAssistVO(long id) {
		if (id <= 0) {
			return null;
		} else {
			SizeAssist assist = productService.getSizeAssistNoCache(id);
			if (assist == null) {
				SizeAssistVO vo = getNewSizeAssistVO();
				return vo;
			} else {
				if (!StringUtils.isBlank(assist.getBody())) {
					try {
						String html = ItemCenterUtil.InputStreamTOString(assist.getBody());
						assist.setBody(html);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						assist.setBody(null);
					}
				}

				return ItemCenterUtils.getSizeAssistVO(assist);
			}
		}
	}

	@Override
	public BaseJsonVO searchSizeAssists(long supplierId, int limit, int offset) {
		try {
			BaseJsonListResultVO retVO = new BaseJsonListResultVO();
			BaseSearchResult<SizeAssist> result = productService.getSizeAssistList(supplierId, limit, offset);
			List<SizeAssist> cList = result.getList();
			List<SizeAssistVO> retList = new ArrayList<SizeAssistVO>();
			if (cList != null && cList.size() > 0) {
				for (SizeAssist assist : cList) {
					if (!StringUtils.isBlank(assist.getBody())) {
						try {
							String html = ItemCenterUtil.InputStreamTOString(assist.getBody());
							assist.setBody(html);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							assist.setBody(null);
						}
					}
					SizeAssistVO vo = ItemCenterUtils.getSizeAssistVO(assist);
					retList.add(vo);
				}
			}
			retVO.setList(retList);
			retVO.setHasNext(result.isHasNext());
			retVO.setTotal(result.getTotal());
			if (cList != null && cList.size() > 0)
				retVO.setLastId(cList.get(cList.size() - 1).getId());
			else
				retVO.setLastId(0);
			BaseJsonVO retObj = new BaseJsonVO(retVO);
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			BaseJsonVO retObj = new BaseJsonVO(new BaseJsonListResultVO());
			retObj.setCode(ErrorCode.SERVICE_ERROR);
			return retObj;
		}

	}

	@Override
	public SizeAssistVO getNewSizeAssistVO() {
		SizeAssistVO vo = new SizeAssistVO();
		vo.setName("");
		vo.setBody(new ArrayList<List<?>>());
		SizeAssistAxis haxis = new SizeAssistAxis();
		haxis.setName(HAXIS_NAME);
		List<?> haxisValue = JsonUtils.parseArray(HAXIS_VALUE, double.class);
		haxis.setList(haxisValue);
		vo.setHaxis(haxis);

		SizeAssistAxis vaxis = new SizeAssistAxis();
		vaxis.setName(VAXIS_NAME);
		List<?> vaxisValue = JsonUtils.parseArray(VAXIS_VALUE, double.class);
		vaxis.setList(vaxisValue);
		vo.setVaxis(vaxis);
		return vo;
	}

	@Override
	public BaseJsonVO savesizeAssist(SizeAssistVO vo) {
		try {
			SizeAssist assist = new SizeAssist();
			assist.setId(Long.valueOf(vo.getId()));
			assist.setName(vo.getName());
			assist.setSupplierId(vo.getSupplierId());
			SizeAssistAxis haxis = vo.getHaxis();
			assist.setHaxisName(haxis.getName());
			String haxisValue = JsonUtils.toJson(haxis.getList());
			assist.setHaxisValue(haxisValue);
			SizeAssistAxis vaxis = vo.getVaxis();
			assist.setVaxisName(vaxis.getName());
			String vaxisValue = JsonUtils.toJson(vaxis.getList());
			assist.setVaxisValue(vaxisValue);
			String body = JsonUtils.toJson(vo.getBody());
			if (!StringUtils.isBlank(body)) {
				ByteArrayInputStream is = new ByteArrayInputStream(body.getBytes());
				String fileName = vo.getSupplierId() + "_SizeAssit_" + vo.getName();
				String url = NOSUtil.uploadFile(is, fileName);
				assist.setBody(url);
			}
			productService.saveSizeAssist(assist);
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public BaseJsonVO deleteSizeAssist(long supplierId, long id) {
		try {
			int result = productService.deleteSizeAssistById(supplierId, id);
			return ItemCenterExceptionHandler.getAjaxJsonVO(result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@Override
	public Map<String, List<String>> batchUploadProductInfo(List<BacthUploadProduct> exlProducts, long supplierId) {
		try {
			List<String> msg = new ArrayList<String>();
			Map<String, List<String>> retMsgMap = new HashMap<String, List<String>>();
			BrandDTO brandDTO = brandFacade.getBrandBySupplierId(supplierId);
			if (brandDTO == null)
				throw new AppException("brand is null!");
			long brandId = brandDTO.getBrand().getBrandId();

			List<ProductSaveParam> list = new LinkedList<ProductSaveParam>();
			if (exlProducts != null && exlProducts.size() > 0) {
				for (BacthUploadProduct exlProduct : exlProducts) {
					ProductSaveParam productParam = transferExcelProductInfo(exlProduct);
					try {
						productParam.setProductParamList(transferParameter(exlProduct.getRowNum(),
								exlProduct.getProperties()));
					} catch (ExcelParseExeption e) {
						List<ExcelParseExceptionInfo> errInfos = e.getInfoList();
						if (errInfos != null && errInfos.size() > 0) {
							StringBuffer m = new StringBuffer();
							m.append("第").append(exlProduct.getRowNum()).append("行").append("，");
							m.append("条形码").append(exlProduct.getBarCode()).append("，");
							for (ExcelParseExceptionInfo errInfo : errInfos) {
								errInfo.getColumnName();
								errInfo.getErrMsg();
								m.append("字段“").append(errInfo.getColumnName()).append("”").append("，")
										.append(errInfo.getErrMsg()).append(";");
							}
							msg.add(m.toString());
						}
						continue;
					}

					int index = list.indexOf(productParam);
					if (index < 0) {
						list.add(productParam);
					} else {
						ProductSaveParam param = list.get(index);
						List<SkuSaveParam> skuList = param.getSKUList();
						skuList.addAll(productParam.getSKUList());
					}
				}
			}
			if (list.size() > 0) {
				for (ProductSaveParam param : list) {
					param.setBrandId(brandId);
					param.setSupplierId(supplierId);
					ProductFullDTO product = productService.getProductFullDTOByUniq(supplierId, param.getGoodsNo(),
							param.getColorNum());
					if (product != null) {
						param.setId(product.getId());
						Map<String, String> map = ItemCenterUtil.getEditHTML_Param(product.getCustomEditHTML(),
								product.getProductParamValue());
						String html = map.get("html");
						param.setCustomEditHTML(html);
						String paramValue = map.get("parameter");
						if (!StringUtils.isBlank(paramValue)) {
							List<ProdParamParam> paramValueList = JsonUtils
									.parseArray(paramValue, ProdParamParam.class);
							if (paramValueList == null)
								paramValueList = new ArrayList<ProdParamParam>();
							List<ProdParamParam> newParamList = param.getProductParamList();
							if (newParamList != null && newParamList.size() > 0) {
								for (ProdParamParam prodParam : newParamList) {
									int indx = paramValueList.indexOf(prodParam);
									if (indx >= 0) {
										paramValueList.remove(indx);
									}
									paramValueList.add(prodParam);
								}
							}
							param.setProductParamList(paramValueList);
						}
						param.setInfoFlag(product.getInfoFlag());
					}
				}
			}

			if (list != null && list.size() > 0) {
				for (ProductSaveParam param : list) {
					BaseJsonVO result = saveProduct(param);
					if (result.getCode() != ErrorCode.SUCCESS.getIntValue()) {
						msg.add("商品货号：" + param.getGoodsNo() + "，商品色号：" + param.getColorNum() + "，导入失败；");
					}
				}
			}
			if (msg.size() > 0)
				retMsgMap.put("saveDBMsg", msg);
			return retMsgMap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public Map<String, List<String>> batchSaveSkuSize(long supplierId, List<String> header,
			List<BatchUploadSize> skuSizeList) {
		try {
			List<String> msg = new ArrayList<String>();
			Map<String, List<String>> retMsgMap = new HashMap<String, List<String>>();
			List<BatchUploadSizeParam> list = new LinkedList<BatchUploadSizeParam>();
			if (skuSizeList != null && skuSizeList.size() > 0) {
				for (BatchUploadSize exlSize : skuSizeList) {
					BatchUploadSizeParam sizeParam = transferExcelSize(exlSize);
					int index = list.indexOf(sizeParam);
					if (index < 0) {
						list.add(sizeParam);
					} else {
						BatchUploadSizeParam param = list.get(index);
						List<BatchUploadSizeSku> skuList = param.getSkuSizeList();
						skuList.addAll(sizeParam.getSkuSizeList());
					}
				}
			}
			if (list != null && list.size() > 0) {
				for (BatchUploadSizeParam param : list) {
					String colorNum = param.getColorNum();
					String goodsNo = param.getGoodNo();
					Product product = productService.getProductByUniq(supplierId, goodsNo, colorNum);
					if (product != null) {
						long pid = product.getId();
						List<Sku> skuList = productService.getSkuList(supplierId, pid);
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
								boolean isValid = true;
								if (header.size() == columnParamList.size()) {
									for (int i = 0; i < columnParamList.size(); i++) {
										SizeColumnParam col = columnParamList.get(i);
										String headerName = header.get(i);
										if (!headerName.equals(col.getName())) {
											isValid = false;
											break;
										}
									}
								} else
									isValid = false;
								if (isValid) {
									List<BatchUploadSizeSku> sizeSkuList = param.getSkuSizeList();
									if (sizeSkuList != null && sizeSkuList.size() > 0) {
										for (BatchUploadSizeSku sizeSku : sizeSkuList) {
											int rowNum = sizeSku.getRowNum();
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
												try {
													productService.saveCustomizedSizeValue(pid, recordIndex,
															columnParamList, sizeValue);
												} catch (Exception e) {
													logger.error(e.getMessage(), e);
													msg.add("第" + rowNum + "行，条形码" + barCode + "，商品尺寸导入失败。");
													continue;
												}
											} else {
												Map<String, String> map = productService.getGoodsNoAndColorNum(
														supplierId, barCode);
												if (map.isEmpty()) {
													List<String> sizeValue = sizeSku.getSizeValue();
													Sku sku = new Sku();
													sku.setBarCode(barCode);
													sku.setBasePrice(product.getBasePrice());
													sku.setMarketPrice(product.getMarketPrice());
													sku.setProductId(product.getId());
													sku.setSalePrice(product.getSalePrice());
													sku.setSupplierId(supplierId);
													try {
														productService.batchUploadSizeAddSku(sku, columnParamList,
																sizeValue);
													} catch (Exception e) {
														logger.error(e.getMessage(), e);
														msg.add("第" + rowNum + "行，条形码" + barCode + "，商品尺寸导入失败。");
														continue;
													}
												} else {
													msg.add("第" + rowNum + "行，条形码" + barCode
															+ "，字段“条形码”与其他货号色号商品已有条形码重复。");
												}
											}
										}
									}
								} else {
									List<BatchUploadSizeSku> sizeSkuList = param.getSkuSizeList();
									if (sizeSkuList != null && sizeSkuList.size() > 0) {
										for (BatchUploadSizeSku sizeSku : sizeSkuList) {
											int rowNum = sizeSku.getRowNum();
											String barCode = sizeSku.getBarCode();
											msg.add("第" + rowNum + "行，条形码" + barCode + "，该类目商品尺码表编号为" + oSizeId
													+ "，导入的尺码表字段与编号" + oSizeId + "的尺码表字段不一致。");
										}
									}
								}
							}
						} else {
							List<BatchUploadSizeSku> sizeSkuList = param.getSkuSizeList();
							if (sizeSkuList != null && sizeSkuList.size() > 0) {
								for (BatchUploadSizeSku sizeSku : sizeSkuList) {
									int rowNum = sizeSku.getRowNum();
									String barCode = sizeSku.getBarCode();
									msg.add("第" + rowNum + "行，该货号色号为" + goodsNo + "+" + colorNum
											+ "的商品已使用尺码模板，如果需要导入，请先编辑取消尺码模板的使用。");
								}
							}
						}
					} else {
						List<BatchUploadSizeSku> sizeSkuList = param.getSkuSizeList();
						if (sizeSkuList != null && sizeSkuList.size() > 0) {
							for (BatchUploadSizeSku sizeSku : sizeSkuList) {
								int rowNum = sizeSku.getRowNum();
								String barCode = sizeSku.getBarCode();
								msg.add("第" + rowNum + "行，该货号色号为" + goodsNo + "+" + colorNum
										+ "的商品在商品库中还没有对应的商品，请先导入其商品资料。");
							}
						}
					}
				}
				if (msg.size() > 0)
					retMsgMap.put("saveDBMsg", msg);
			}
			return retMsgMap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public Map<String, List<String>> batchSaveCustomHtml(long supplierId, Map<String, String> map) {
		try {
			Map<String, List<String>> retMsgMap = new HashMap<String, List<String>>();
			Map<String, String> resultMap = new HashMap<String, String>();
			List<String> msg = new ArrayList<String>();
			if (map != null) {
				for (String goodsNo : map.keySet()) {
					List<ProductFullDTO> pList = productService.getProductFullDTOListByGoodsNo(supplierId, goodsNo);
					if (pList != null && pList.size() > 0) {
						for (ProductFullDTO p : pList) {
							String colorNum = p.getColorNum();
							String html = map.get(goodsNo);
							if (!StringUtils.isBlank(html)) {
								Map<String, String> nosMap = ItemCenterUtil.getEditHTML_Param(p.getCustomEditHTML(),
										p.getProductParamValue());
								String nosParamter = nosMap.get("parameter");
								Map<String, String> saveMap = new HashMap<String, String>();
								saveMap.put("parameter", nosParamter);
								saveMap.put("html", html);
								String saveStr = JsonUtils.toJson(saveMap);

								ByteArrayInputStream is = new ByteArrayInputStream(saveStr.getBytes());
								String fileName = supplierId + "_" + goodsNo;
								String url = NOSUtil.uploadFile(is, fileName);
								resultMap.put(goodsNo + "_" + colorNum, url);
							}
						}
					} else {
						msg.add("商品货号：" + goodsNo + "，导入失败，商品库中还没有对应的商品，请先导入其商品资料。");
					}

				}
			}
			if (resultMap != null) {
				for (String goodsNoAndColor : resultMap.keySet()) {
					String goodsNo = goodsNoAndColor.split("_")[0];
					String colorNum = goodsNoAndColor.split("_")[1];
					String html = resultMap.get(goodsNoAndColor);
					try {
						productService.batchSaveCustomHtml(supplierId, goodsNo, colorNum, html);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						msg.add("商品货号：" + goodsNo + "，导入失败；");
						continue;
					}
				}
				if (msg.size() > 0)
					retMsgMap.put("saveDBMsg", msg);
			}
			return retMsgMap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public Map<String, List<String>> batchSaveProductPic(long supplierId, List<BatchUploadPicParam> paramList) {
		try {
			Map<String, List<String>> retMsgMap = new HashMap<String, List<String>>();
			List<String> msg = new ArrayList<String>();
			if (paramList != null && paramList.size() > 0) {
				for (BatchUploadPicParam param : paramList) {
					String goodsNo = param.getGoodsNo();
					String colorNum = param.getColorNum();
					ProductFullDTO product = productService.getProductFullDTOByUniq(supplierId, goodsNo, colorNum);
					if (product == null) {
						msg.add("文件夹“" + goodsNo + "+" + colorNum + "”图片导入失败。出错原因如下：该货号色号为" + goodsNo + "+" + colorNum
								+ "的商品在商品库中还没有对应的商品，请先导入其商品资料。");
						continue;
					}
					long pid = product.getId();
					try {
						String s = productService.batchSaveProductPic(supplierId, pid, param);
						if (!StringUtils.isBlank(s)) {
							msg.add(s);
						}

						List<String> htmlPicList = param.getHtmlPicList();
						if (htmlPicList != null && htmlPicList.size() > 0) {
							StringBuffer html = new StringBuffer();
							for (String htmlPic : htmlPicList) {
								html.append("<img src=\"");
								html.append(htmlPic);
								html.append("\"/>");
							}
							Map<String, String> nosMap = ItemCenterUtil.getEditHTML_Param(product.getCustomEditHTML(),
									product.getProductParamValue());
							nosMap.put("html", html.toString());
							String saveStr = JsonUtils.toJson(nosMap);
							ByteArrayInputStream is = new ByteArrayInputStream(saveStr.getBytes());
							String fileName = supplierId + "_" + goodsNo + "_" + colorNum;
							String url = NOSUtil.uploadFile(is, fileName);
							productService.batchSaveCustomHtml(supplierId, goodsNo, colorNum, url);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						msg.add("文件夹“" + goodsNo + "+" + colorNum + "”，图片导入失败。出错原因如下：保存失败。");
						continue;
					}
				}
			}
			if (msg.size() > 0)
				retMsgMap.put("saveDBMsg", msg);
			return retMsgMap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public List<Map<String, Object>> getProductParamExport(List<Long> categoryList) {
		return productService.getProductParamExport(categoryList);
	}

	private ProductSaveParam transferExcelProductInfo(BacthUploadProduct exlProduct) {
		ProductSaveParam productParam = new ProductSaveParam();
		productParam.setAccessory(exlProduct.getAccessory());
		productParam.setAfterMarket("");
		productParam.setAirContraband(0);
		productParam.setBasePrice(new BigDecimal(0));
		productParam.setBig(0);
		productParam.setCareLabel(exlProduct.getCareLabel());
		productParam.setColorName(exlProduct.getColorName());
		productParam.setColorNum(exlProduct.getColorNum());
		productParam.setIsRecommend(0);
		productParam.setConsumptionTax(0);
		productParam.setCustomEditHTML("");
		productParam.setFragile(0);
		productParam.setGoodsNo(exlProduct.getGoodsNo());
		productParam.setHeight("");

		productParam.setIsShowSizePic(false);
		productParam.setLenth("");
		productParam.setLowCategoryId(exlProduct.getLowCategoryId());
		productParam.setMarketPrice(exlProduct.getMarketPrice());
		productParam.setProducing(exlProduct.getProducing());
		productParam.setProductDescp(exlProduct.getProductDescp());
		productParam.setProductName(exlProduct.getProductName());
		productParam.setSalePrice(exlProduct.getSalePrice());
		productParam.setSameAsShop(exlProduct.getSameAsShop().getIntValue());

		productParam.setSizeAssistId(0);
		productParam.setSizeTemplateId(0);
		productParam.setSizeType(SizeType.CUST_SIZE.getIntValue());
		List<SkuSaveParam> skuList = new ArrayList<SkuSaveParam>();
		String barCode = exlProduct.getBarCode();
		String sizeSpec = exlProduct.getSpec();
		SkuSaveParam sku = new SkuSaveParam();
		sku.setBarCode(barCode);
		List<String> customizedSizeValue = new ArrayList<String>();
		customizedSizeValue.add(sizeSpec);
		sku.setCustomizedSizeValue(customizedSizeValue);
		skuList.add(sku);
		productParam.setSKUList(skuList);
		productParam.setUnit(1);
		productParam.setValuables(0);
		productParam.setWeight("");
		productParam.setWidth("");
		productParam.setWirelessTitle(exlProduct.getWirelessTitle());
		List<SizeHeaderVO> headerVO = getCustomizeSizeHeaderVOList(0, exlProduct.getLowCategoryId());
		List<SizeColumnParam> sizeHeader = new ArrayList<SizeColumnParam>();
		if (headerVO != null && headerVO.size() > 0) {
			for (SizeHeaderVO vo : headerVO) {
				SizeColumnParam param = vo.transfer();
				sizeHeader.add(param);
			}
		}
		productParam.setSizeHeader(sizeHeader);
		// 转换可变参数到list中
		return productParam;
	}

	private List<ProdParamParam> transferParameter(int rowNum, Map<Long, String> dataProperties)
			throws ExcelParseExeption {
		List<ExcelParseExceptionInfo> errList = new ArrayList<ExcelParseExceptionInfo>();
		boolean isThrow = false;
		List<ProdParamParam> paramList = new ArrayList<>(dataProperties.size());
		for (Long id : dataProperties.keySet()) {
			String optVal = dataProperties.get(id);
			ProductParameter param = PARAM_MAP.get(id);
			if (param == null) {
				param = productService.getProductParam(id);
				PARAM_MAP.put(id, param);
			}
			if (param.getDetailType() == ProdDetailType.TEXT || param.getDetailType() == ProdDetailType.TEXT_AREA) {
				if (optVal.length() > 180) {
					isThrow = true;
					ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, param.getName(),
							ExcelUtils.INVALID);
					errList.add(expInfo);
					continue;
				}
				ProdParamParam tmpParam = new ProdParamParam();
				tmpParam.setId(id);
				tmpParam.setValue(optVal);
				paramList.add(tmpParam);
			} else if (param.getDetailType() == ProdDetailType.SINGLE_SELECT) {
				Map<String, Long> optMap = PARAM_OPT_MAP.get(id);
				if (optMap == null) {
					optMap = productService.getProductParamForBat(id);
					PARAM_OPT_MAP.put(id, optMap);
				}
				Long optId = optMap.get(optVal);
				if (optId == null) {
					isThrow = true;
					ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, param.getName(),
							ExcelUtils.INVALID);
					errList.add(expInfo);
					continue;
				}
				ProdParamParam tmpParam = new ProdParamParam();
				tmpParam.setId(id);
				tmpParam.setValue(String.valueOf(optId));
				paramList.add(tmpParam);
			} else {
				Map<String, Long> optMap = PARAM_OPT_MAP.get(id);
				if (optMap == null) {
					optMap = productService.getProductParamForBat(id);
					PARAM_OPT_MAP.put(id, optMap);
				}
				String[] contentArgs = optVal.split(";");
				Long[] ids = new Long[contentArgs.length];
				boolean isBreak = false;
				for (int idx = 0; idx < contentArgs.length; idx++) {
					String arg = contentArgs[idx];
					Long argId = optMap.get(arg);
					if (argId != null) {
						ids[idx] = argId;
					} else {
						isThrow = true;
						ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, param.getName(),
								ExcelUtils.INVALID);
						errList.add(expInfo);
						isBreak = true;
						break;
					}
				}
				if (isBreak) {
					continue;
				}
				ProdParamParam tmpParam = new ProdParamParam();
				tmpParam.setId(id);
				tmpParam.setValue(JsonUtils.toJson(ids));
				paramList.add(tmpParam);
			}
		}
		if (isThrow) {
			throw new ExcelParseExeption(errList);
		}
		return paramList;
	}

	private BatchUploadSizeParam transferExcelSize(BatchUploadSize excelSize) {
		BatchUploadSizeParam sizeParam = new BatchUploadSizeParam();
		sizeParam.setColorNum(excelSize.getColorNum());
		sizeParam.setGoodNo(excelSize.getGoodNo());
		List<BatchUploadSizeSku> skuList = new ArrayList<BatchUploadSizeSku>();
		BatchUploadSizeSku sku = new BatchUploadSizeSku();
		sku.setBarCode(excelSize.getBarCode());
		sku.setSizeValue(excelSize.getSizeValue());
		sku.setRowNum(excelSize.getRowNum());
		skuList.add(sku);
		sizeParam.setSkuSizeList(skuList);
		return sizeParam;
	}

	private SizeTemplateEditVO genSizeTemplateVO(SizeTemplate sizeTemplate) {
		SizeTemplateEditVO vo = new SizeTemplateEditVO();
		vo.setRemindText(sizeTemplate.getRemindText());
		vo.setTemplateName(sizeTemplate.getTemplateName());
		List<Category> cList = categoryService.getCategoryListBylowId(sizeTemplate.getLowCategoryId());
		List<String> categories = new ArrayList<String>();
		for (Category c : cList) {
			categories.add(String.valueOf(c.getId()));
		}
		vo.setCategories(categories);
		List<CategoryArchitect> categoryList = commonFacade.getCategoryArchitect();
		vo.setCategoryList(categoryList);
		SizeTmplTableVO sizeTableVO = genSizeTemplateTable(sizeTemplate.getLowCategoryId(), sizeTemplate.getId());
		vo.setSizeTable(sizeTableVO);
		return vo;
	}

	@Override
	public Map<String, Long> getProductParamOptMap(long lowCategoryId) {
		return productService.getProductParamOptMap(lowCategoryId);
	}

	@Override
	public List<Size> getSizeList(long sizeTemplateId, SizeType sizeType) {
		return commonFacade.getSizeList(sizeTemplateId, sizeType);
	}

	@Override
	public SizeVO genSizeVO(long productId, long lowCategoryId, long sizeTemplateId, long supplierId) {
		try {
			SizeVO sizeVO = new SizeVO();
			List<SizeTemplateArchitect> sizeTmplAcht = getSizeTemplateList(lowCategoryId, supplierId);
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
	public Map<String, String> getGoodsNoAndColorNum(long supplierId, String barcode) {
		return productService.getGoodsNoAndColorNum(supplierId, barcode);
	}

	@Override
	public SizeTable getSizeTable(long templatekey, SizeType sizeType) {
		return sizeTemplateService.getSizeTable(templatekey, sizeType);
	}

	@Override
	public Long getLowCategoryId(long supplierId, String goodsNo, String colorNum) {
		Product p = productService.getProductByUniq(supplierId, goodsNo, colorNum);
		if (p == null)
			return null;
		else
			return p.getLowCategoryId();
	}
}
