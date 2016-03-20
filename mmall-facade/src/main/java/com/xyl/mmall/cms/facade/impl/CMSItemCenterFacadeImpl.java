package com.xyl.mmall.cms.facade.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.backend.vo.ItemReviewPassVO;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.CMSItemCenterFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.vo.ExportMaterialVO;
import com.xyl.mmall.cms.vo.ItemReviewRejectVO;
import com.xyl.mmall.cms.vo.ProductReviewSearchVO;
import com.xyl.mmall.cms.vo.ProductReviewVO;
import com.xyl.mmall.cms.vo.SkuReviewVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.dto.BaseSkuDTO;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoProductVo;
import com.xyl.mmall.itemcenter.dto.PoSkuVo;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.meta.SkuSpecMap;
import com.xyl.mmall.itemcenter.param.PoProductSo;
import com.xyl.mmall.itemcenter.param.PoSkuSo;
import com.xyl.mmall.itemcenter.param.ProductReviewParam;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.meta.Brand;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@Facade
public class CMSItemCenterFacadeImpl implements CMSItemCenterFacade {
	@Resource
	private POProductService poProductService;

	@Resource
	private DealerService dealerService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private BusinessService businessService;

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private BrandService brandService;

	@Autowired
	private ScheduleFacade scheduleFacade;

	private static final Logger logger = LoggerFactory.getLogger(CMSItemCenterFacadeImpl.class);

	@Override
	public BaseJsonVO searchReviewProductYouhua(ProductReviewSearchVO param) {
		long poId = param.getPoId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BaseJsonVO resJosnVo = new BaseJsonVO();
		BaseJsonListResultVO retVO = new BaseJsonListResultVO();

		List<PoProduct> plist = poProductService.getProductListByPo(poId);

		List<ProductReviewVO> produdctReviewList = new ArrayList<ProductReviewVO>();
		if (plist != null && plist.size() > 0) {
			for (PoProduct p : plist) {
				if (!StringUtils.isBlank(param.getGoodsNo()) && !param.getGoodsNo().equals(p.getGoodsNo()))
					continue;
				if (!StringUtils.isBlank(param.getProductName()) && !param.getProductName().equals(p.getProductName()))
					continue;
				if (param.getStatus() != -1 && param.getStatus() != p.getStatus().getIntValue())
					continue;
				ProductReviewVO vo = new ProductReviewVO();
				vo.setCategoryName(categoryService.getCategoryById(p.getLowCategoryId()).getName());
				vo.setColorName(p.getColorName());
				vo.setGoodsNo(p.getGoodsNo());
				vo.setId(String.valueOf(p.getId()));
				vo.setPoId(String.valueOf(p.getPoId()));
				vo.setProductName(p.getProductName());
				vo.setProductStatus(p.getStatus().getIntValue());
				vo.setReason(p.getRejectReason());
				vo.setThumb(p.getShowPicPath());
				vo.setSubmitTime(sdf.format(new Date(p.getSubmitTime())));
				produdctReviewList.add(vo);
			}
			int offset = param.getOffset();
			int limit = param.getLimit();
			int totalSize = produdctReviewList.size();
			boolean hasNext = false;
			if (totalSize + 1 > offset + limit)
				hasNext = true;
			List<ProductReviewVO> resultList = null;
			if (hasNext && produdctReviewList != null && produdctReviewList.size() > 0)
				resultList = produdctReviewList.subList(offset, offset + limit);
			else if (produdctReviewList != null && produdctReviewList.size() > 0)
				resultList = produdctReviewList.subList(offset, totalSize);
			else
				resultList = new ArrayList<ProductReviewVO>();
			retVO.setList(resultList);
			retVO.setTotal(totalSize);
			retVO.setHasNext(hasNext);
		} else {
			retVO.setTotal(0);
			retVO.setHasNext(false);
			retVO.setList(produdctReviewList);
		}
		resJosnVo.setResult(retVO);
		resJosnVo.setCode(ErrorCode.SUCCESS);
		return resJosnVo;
	}

	@Override
	public BaseJsonVO productReviewPass(ItemReviewPassVO param) {
		try {
			List<Long> poIds = param.getPoList();
			poProductService.updateProductStaus(StatusType.APPROVAL, null, null, param.getList());
			if (poIds.size() > 0) {
				for (Long poId : poIds) {
					int prodNum = poProductService.getProductNum(poId);
					int prodApproval = poProductService.getProductNumOfStatus(poId, StatusType.APPROVAL);
					int prodReject = poProductService.getProductNumOfStatus(poId, StatusType.REJECT);
					if (prodNum == prodApproval + prodReject) {
						if (prodNum == prodApproval)
							scheduleService.updatePrdzlStatus(poId, StatusType.APPROVAL.getIntValue());
						else
							scheduleService.updatePrdzlStatus(poId, StatusType.REJECT.getIntValue());
					}
					int skuNum = poProductService.getSkuNum(poId);
					int skuApproval = poProductService.getSkuNumOfStatus(poId, StatusType.APPROVAL);
					int skuReject = poProductService.getSkuNumOfStatus(poId, StatusType.REJECT);
					if ((prodNum == prodApproval + prodReject) && (skuNum == skuApproval + skuReject)) {
						List<Long> inPoIds = new ArrayList<Long>();
						inPoIds.add(poId);
						if ((prodNum == prodApproval) && (skuNum == skuApproval)) {
							scheduleService.batchUpdatePOPrdStatus(inPoIds, StatusType.APPROVAL.getIntValue());
						} else {
							scheduleService.batchUpdatePOPrdStatus(inPoIds, StatusType.REJECT.getIntValue());
						}
					}

				}
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
	public BaseJsonVO productReviewReject(ItemReviewRejectVO param) {
		try {
			String reason = param.getReason();
			String descp = param.getDescp();
			List<Long> list = param.getIds();
			List<Long> poIds = param.getPoList();
			poProductService.updateProductStaus(StatusType.REJECT, reason, descp, list);
			if (poIds.size() > 0) {
				for (Long poId : poIds) {
					int prodPend = poProductService.getProductNumOfStatus(poId, StatusType.PENDING);
					int prodReject = poProductService.getProductNumOfStatus(poId, StatusType.REJECT);
					if (prodPend == 0 && prodReject > 0) {
						scheduleService.updatePrdzlStatus(poId, StatusType.REJECT.getIntValue());
					}
					int skuPend = poProductService.getSkuNumOfStatus(poId, StatusType.PENDING);
					int skuReject = poProductService.getSkuNumOfStatus(poId, StatusType.REJECT);
					if ((prodPend == 0 && skuPend == 0) && (prodReject > 0 || skuReject > 0)) {
						List<Long> inPoIds = new ArrayList<Long>();
						inPoIds.add(poId);
						scheduleService.batchUpdatePOPrdStatus(inPoIds, StatusType.REJECT.getIntValue());
					}
				}
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
	public BaseJsonVO skuReviewPass(ItemReviewPassVO param) {
		try {
			List<Long> poIds = param.getPoList();
			poProductService.updateSkuStaus(StatusType.APPROVAL, null, param.getList());
			if (poIds.size() > 0) {
				for (Long poId : poIds) {
					int skuNum = poProductService.getSkuNum(poId);
					int skuApproval = poProductService.getSkuNumOfStatus(poId, StatusType.APPROVAL);
					int skuReject = poProductService.getSkuNumOfStatus(poId, StatusType.REJECT);
					if (skuNum == skuApproval + skuReject) {
						if (skuNum == skuApproval)
							scheduleService.updatePrdqdStatus(poId, StatusType.APPROVAL.getIntValue());
						else
							scheduleService.updatePrdqdStatus(poId, StatusType.REJECT.getIntValue());
					}
					int prodNum = poProductService.getProductNum(poId);
					int prodApproval = poProductService.getProductNumOfStatus(poId, StatusType.APPROVAL);
					int prodReject = poProductService.getProductNumOfStatus(poId, StatusType.REJECT);
					if ((prodNum == prodApproval + prodReject) && (skuNum == skuApproval + skuReject)) {
						List<Long> inPoIds = new ArrayList<Long>();
						inPoIds.add(poId);
						if (prodReject > 0 || skuReject > 0) {
							scheduleService.batchUpdatePOPrdStatus(inPoIds, StatusType.REJECT.getIntValue());
						} else {
							scheduleService.batchUpdatePOPrdStatus(inPoIds, StatusType.APPROVAL.getIntValue());
						}
					}
				}
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
	public BaseJsonVO skuReviewReject(ItemReviewRejectVO param) {
		try {
			String reason = param.getReason();
			List<Long> list = param.getIds();
			List<Long> poIds = param.getPoList();
			poProductService.updateSkuStaus(StatusType.REJECT, reason, list);
			if (poIds.size() > 0) {
				for (Long poId : poIds) {
					int skuNum = poProductService.getSkuNum(poId);
					int skuApproval = poProductService.getSkuNumOfStatus(poId, StatusType.APPROVAL);
					int skuReject = poProductService.getSkuNumOfStatus(poId, StatusType.REJECT);
					if (skuNum == skuApproval + skuReject) {
						if (skuReject > 0)
							scheduleService.updatePrdqdStatus(poId, StatusType.REJECT.getIntValue());
					}
					int prodNum = poProductService.getProductNum(poId);
					int prodApproval = poProductService.getProductNumOfStatus(poId, StatusType.APPROVAL);
					int prodReject = poProductService.getProductNumOfStatus(poId, StatusType.REJECT);
					if ((prodNum == prodApproval + prodReject) && (skuNum == skuReject + skuApproval)
							&& (prodReject > 0 || skuReject > 0)) {
						List<Long> inPoIds = new ArrayList<Long>();
						inPoIds.add(poId);
						scheduleService.batchUpdatePOPrdStatus(inPoIds, StatusType.REJECT.getIntValue());
					}
				}
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
	public BaseJsonVO searchReviewSKUYouhua(ProductReviewSearchVO param) {
		long oldTime = System.currentTimeMillis();
		BaseJsonVO resJosnVo = new BaseJsonVO();
		BaseJsonListResultVO retVO = new BaseJsonListResultVO();
		long poId = param.getPoId();

		List<POProductDTO> plist = poProductService.getProductDTOListByPo(poId);
		List<SkuReviewVO> skuReviewList = new ArrayList<SkuReviewVO>();
		if (plist != null && plist.size() > 0) {
			for (POProductDTO p : plist) {
				if (!StringUtils.isBlank(param.getGoodsNo()) && !param.getGoodsNo().equals(p.getGoodsNo()))
					continue;
				if (!StringUtils.isBlank(param.getProductName()) && !param.getProductName().equals(p.getProductName()))
					continue;

				List<? extends BaseSkuDTO> skuList = p.getSKUList();
				if (skuList != null && skuList.size() > 0) {
					for (BaseSkuDTO sku : skuList) {
						if (param.getStatus() != -1 && param.getStatus() != ((POSkuDTO) sku).getStatus().getIntValue())
							continue;
						if (!StringUtils.isBlank(param.getBarCode()) && !param.getBarCode().equals(sku.getBarCode()))
							continue;
						SkuReviewVO vo = new SkuReviewVO();
						vo.setBarCode(sku.getBarCode());
						vo.setBasePrice(p.getBasePrice());
						vo.setColorName(p.getColorName());
						vo.setGoodsNo(p.getGoodsNo());
						vo.setId(String.valueOf(p.getId()));
						vo.setMarketPrice(p.getMarketPrice());
						vo.setPoId(String.valueOf(poId));
						vo.setProductName(p.getProductName());
						vo.setReason(((POSkuDTO) sku).getRejectReason());
						vo.setSalePrice(p.getSalePrice());
						vo.setSize(((POSkuDTO) sku).getSize());
						vo.setSkuId(String.valueOf(sku.getId()));
						vo.setSkuNum(((POSkuDTO) sku).getSkuNum() + ((POSkuDTO) sku).getSupplierSkuNum());
						vo.setSkuStatus(((POSkuDTO) sku).getStatus().getIntValue());
						vo.setThumb(p.getShowPicPath());
						skuReviewList.add(vo);
					}
				}
			}
		}

		int offset = param.getOffset();
		int limit = param.getLimit();
		int totalSize = skuReviewList.size();
		boolean hasNext = false;
		if (totalSize + 1 > offset + limit)
			hasNext = true;
		List<SkuReviewVO> resultList = null;
		if (hasNext && skuReviewList != null && skuReviewList.size() > 0)
			resultList = skuReviewList.subList(offset, offset + limit);
		else if (skuReviewList != null && skuReviewList.size() > 0)
			resultList = skuReviewList.subList(offset, totalSize);
		else
			resultList = new ArrayList<SkuReviewVO>();
		retVO.setList(resultList);
		retVO.setTotal(totalSize);
		retVO.setHasNext(hasNext);
		resJosnVo.setResult(retVO);
		resJosnVo.setCode(ErrorCode.SUCCESS);
		long cost = System.currentTimeMillis() - oldTime;
		logger.info("=====all cost youhua:" + cost);
		return resJosnVo;
	}

	@Override
	public List<ExportMaterialVO> getExportMaterialVO(long poId) {
		List<ExportMaterialVO> retList = new ArrayList<ExportMaterialVO>();
		List<POProductDTO> plist = poProductService.getProductDTOListByPo(poId);
		if (plist != null && plist.size() > 0) {
			for (POProductDTO p : plist) {
				List<? extends BaseSkuDTO> skuList = p.getSKUList();
				if (skuList != null && skuList.size() > 0) {
					for (BaseSkuDTO sku : skuList) {
						ExportMaterialVO vo = new ExportMaterialVO();
						vo.setBarCode(sku.getBarCode());
						vo.setBasePrice(p.getBasePrice());
						vo.setColorName(p.getColorName());
						vo.setGoodsNo(p.getGoodsNo());
						vo.setMarketPrice(p.getMarketPrice());
						vo.setNum(((POSkuDTO) sku).getSkuNum() + ((POSkuDTO) sku).getSupplierSkuNum());
						vo.setPoId(poId);
						vo.setProductName(p.getProductName());
						vo.setSalePrice(p.getSalePrice());
						vo.setSize(((POSkuDTO) sku).getSize());
						retList.add(vo);
					}
				}
			}
		}
		return retList;
	}

}
