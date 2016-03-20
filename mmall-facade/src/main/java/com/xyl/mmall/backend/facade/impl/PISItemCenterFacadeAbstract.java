package com.xyl.mmall.backend.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.util.ConvertUtil;
import com.xyl.mmall.backend.vo.CustSizeSimpleSKU;
import com.xyl.mmall.backend.vo.ProductEditVO;
import com.xyl.mmall.backend.vo.ProductParamVO;
import com.xyl.mmall.backend.vo.SizeHeaderVO;
import com.xyl.mmall.backend.vo.SizeOption;
import com.xyl.mmall.backend.vo.SizeTemplateArchitect;
import com.xyl.mmall.backend.vo.SizeVO;
import com.xyl.mmall.backend.vo.TmplSizeSimpleSKU;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.exception.AppException;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.itemcenter.dto.BaseSkuDTO;
import com.xyl.mmall.itemcenter.dto.CategoryArchitect;
import com.xyl.mmall.itemcenter.dto.ProductDTO;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.SizeColumn;
import com.xyl.mmall.itemcenter.meta.SizeTemplate;
import com.xyl.mmall.itemcenter.param.SizeColumnParam;
import com.xyl.mmall.itemcenter.param.SizeDetailPaire;
import com.xyl.mmall.itemcenter.param.SizeTable;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.itemcenter.util.ConstantsUtil;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.saleschedule.dto.BrandDTO;

public abstract class PISItemCenterFacadeAbstract {
	private static final Logger logger = LoggerFactory.getLogger(PISItemCenterFacadeAbstract.class);
	@Resource
	private DealerService dealerService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private SizeTemplateService sizeTemplateService;

	@Resource
	private BrandFacade brandFacade;

	@Resource(name = "commonFacade")
	private ItemCenterCommonFacade commonFacade;

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

	public ProductEditVO genProductVO(ProductFullDTO product) {
		ProductEditVO vo = ConvertUtil.getProductEditVO(product);
		String paramter = null;
		long supplierId = product.getSupplierId();
		Map<String, String> map = ItemCenterUtil.getEditHTML_Param(product.getCustomEditHTML(),
				product.getProductParamValue());
		String html = map.get("html");
		paramter = map.get("parameter");
		vo.setCustomEditHTML(html);

		List<Category> cList = categoryService.getCategoryListBylowId(product.getLowCategoryId());
		List<String> categories = new ArrayList<String>();
		for (Category c : cList) {
			categories.add(String.valueOf(c.getId()));
		}
		vo.setCategories(categories);
		List<CategoryArchitect> categoryList = commonFacade.getCategoryArchitect();
		vo.setCategoryList(categoryList);
		long categoryId = Long.valueOf(categories.get(categories.size() - 1));
		List<ProductParamVO> pvoList = commonFacade.getProductParamVOList(categoryId, paramter);
		vo.setProductParamList(pvoList);
		SizeVO sizeVo = genSizeVO(product.getId(), product.getLowCategoryId(), product.getSizeTemplateId(),
				product.getSupplierId());
		vo.setTemplate(sizeVo);
		List<? extends BaseSkuDTO> skuList = product.getSKUList();
		if (product.getSizeType() == SizeType.CUST_SIZE) {
			SizeTable table = getSizeTable(product.getId(), SizeType.CUST_SIZE);
			List<Long> columnIdList = new ArrayList<Long>();
			List<SizeHeaderVO> headerList = sizeVo.getSizeHeader();
			for (SizeHeaderVO header : headerList) {
				columnIdList.add(Long.valueOf(header.getId()));
			}
			List<CustSizeSimpleSKU> skuVOList = new ArrayList<CustSizeSimpleSKU>();
			for (BaseSkuDTO sku : skuList) {
				long skuId = sku.getId();
				String barCode = sku.getBarCode();
				List<String> sizeValueList = new ArrayList<String>();
				for (long colId : columnIdList) {
					String key = sku.getSizeIndex() + "+" + colId;
					sizeValueList.add(table.getValueMap().get(key));
				}
				CustSizeSimpleSKU simpSku = new CustSizeSimpleSKU();
				simpSku.setBarCode(barCode);
				simpSku.setBody(sizeValueList);
				simpSku.setId(skuId);
				skuVOList.add(simpSku);
			}
			vo.setSkuList(skuVOList);
		} else if (product.getSizeType() == SizeType.TMPL_SIZE || product.getSizeType() == SizeType.ORIG_SIZE) {
			List<TmplSizeSimpleSKU> skuVOList = new ArrayList<TmplSizeSimpleSKU>();
			for (BaseSkuDTO sku : skuList) {
				String barCode = sku.getBarCode();
				TmplSizeSimpleSKU simpSku = new TmplSizeSimpleSKU();
				simpSku.setBarCode(barCode);
				simpSku.setSizeId(sku.getSizeIndex());
				simpSku.setId(sku.getId());
				skuVOList.add(simpSku);
			}
			vo.setSkuList2(skuVOList);
		}
		BrandDTO brandDTO = brandFacade.getBrandBySupplierId(supplierId);
		if (brandDTO == null)
			throw new AppException("brand is null!");
		vo.setBrandId(String.valueOf(brandDTO.getBrand().getBrandId()));
		// vo.setBrandName(brandDTO.getBrand().getBrandNameZh());
		vo.setBrandName(brandDTO.getBrand().getBrandNameAuto());
		return vo;
	}

	protected List<SizeTemplateArchitect> getSizeTemplateList(long lowCategoryId, long supplierId) {
		List<SizeTemplateArchitect> sizeTmplAcht = new ArrayList<SizeTemplateArchitect>();

		List<SizeTemplate> sizeTmplList = sizeTemplateService.getSizeTemplateList(lowCategoryId, supplierId);
		if (sizeTmplList != null && sizeTmplList.size() > 0) {
			for (SizeTemplate sizeTemplate : sizeTmplList) {
				SizeTemplateArchitect sta = getSizeTemplateArchitect(sizeTemplate.getId());
				sta.setId(String.valueOf(sizeTemplate.getId()));
				sta.setName(sizeTemplate.getTemplateName());
				sizeTmplAcht.add(sta);
			}
		}
		return sizeTmplAcht;
	}

	protected SizeTemplateArchitect getSizeTemplateArchitect(long templateId) {
		SizeTemplateArchitect sta = new SizeTemplateArchitect();
		SizeTable table = getSizeTable(templateId, SizeType.TMPL_SIZE);
		List<Long> indexes = table.getRecordList();
		List<SizeColumnParam> headerList = table.getSizeHeader();
		List<SizeOption> sizeOpts = new ArrayList<SizeOption>();
		if (indexes != null && indexes.size() > 0) {
			for (long index : indexes) {
				SizeOption opt = new SizeOption();
				StringBuffer size = new StringBuffer();
				StringBuffer hover = new StringBuffer();
				String key1 = index + "+" + ConstantsUtil.CHIMA_COLUMN;
				String sizeVal = table.getValueMap().get(key1);
				if (sizeVal == null)
					sizeVal = "";
				size.append(sizeVal);
				String key2 = index + "+" + ConstantsUtil.HAOXING_COLUMN;
				String haoxing = table.getValueMap().get(key2);
				if (!StringUtils.isBlank(haoxing)) {
					size.append("(").append(haoxing).append(")");
				}
				for (SizeColumnParam header : headerList) {
					long colId = header.getId();
					String key = index + "+" + colId;
					if (colId != ConstantsUtil.CHIMA_COLUMN && colId != ConstantsUtil.HAOXING_COLUMN) {
						String val = table.getValueMap().get(key);
						if (!StringUtils.isBlank(val)) {
							String colName = header.getName();
							hover.append(colName).append(val);
							hover.append(" ");
						}
					}
				}
				opt.setSize(size.toString());
				opt.setShowName(hover.toString().trim());
				opt.setId(String.valueOf(index));
				sizeOpts.add(opt);
			}
		}
		sta.setList(sizeOpts);
		return sta;
	}

	protected List<SizeHeaderVO> getCustomizeSizeHeaderVOList(long productId, long lowCategoryId) {
		List<SizeHeaderVO> headerVO = null;
		long oSizeId = sizeTemplateService.getOriginalSizeId(lowCategoryId);
		headerVO = getSizeHeaderVOList(oSizeId, SizeType.ORIG_SIZE);
		return headerVO;
	}

	public List<SizeHeaderVO> getSizeHeaderVOList(long templateId, SizeType sizeType) {
		List<SizeHeaderVO> headerVO = new ArrayList<SizeHeaderVO>();
		List<Size> oList = getSizeList(templateId, sizeType);
		if (oList != null && oList.size() > 0) {
			for (Size orginalSize : oList) {
				SizeHeaderVO cshv = new SizeHeaderVO();
				cshv.setId(String.valueOf(orginalSize.getColumnId()));
				SizeColumn col = sizeTemplateService.getSizeColumn(orginalSize.getColumnId());
				cshv.setName(col.getName());
				cshv.setUnit(col.getUnit() == null ? "" : col.getUnit());
				cshv.setRequired(orginalSize.getIsRequired());
				headerVO.add(cshv);
			}
			return headerVO;
		}
		return headerVO;
	}

	// ==========================
	public abstract SizeVO genSizeVO(long productId, long lowCategoryId, long sizeTemplateId, long supplierId);

	public abstract List<Size> getSizeList(long sizeTemplateId, SizeType sizeType);

	public abstract SizeTable getSizeTable(long templatekey, SizeType sizeType);
}
