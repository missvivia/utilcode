package com.xyl.mmall.mainsite.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.facade.ReturnCommonFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.mainsite.facade.CalCenterFacade;
import com.xyl.mmall.mainsite.facade.ReturnFacade;
import com.xyl.mmall.mainsite.facade.param.FrontReturnApplyParam;
import com.xyl.mmall.mainsite.facade.param.FrontReturnExpInfoParam;
import com.xyl.mmall.mainsite.facade.param.FrontReturnApplyParam.RetOrdSkuEntity;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnApplyVO;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnExpInfoVO;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnOrderSkuInfoList;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnOrderSkuInfoListVO;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnPriceVO;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnStatusVO;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnApplyVO.ReturnAddressVO;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnOrderSkuInfoList.ReturnOrderSkuInfo;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnStatusVO.StatusInfo;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.order.dto.DeprecatedReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.DeprecatedReturnFormDTO;
import com.xyl.mmall.order.dto.DeprecatedReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.DeprecatedOrderReturnJudgement;
import com.xyl.mmall.order.enums.DeprecatedOrderSkuReturnJudgement;
import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.order.param.DeprecatedReturnFormApplyParam;
import com.xyl.mmall.order.param.DeprecatedReturnOrderSkuParam;
import com.xyl.mmall.order.param.RetOrdSkuPriceCalParam;
import com.xyl.mmall.order.param.ReturnBankCardParam;
import com.xyl.mmall.order.param.ReturnPackageExpInfoParam;
import com.xyl.mmall.order.param.DeprecatedReturnFormApplyParam.PriceParam;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.ReturnQueryService;
import com.xyl.mmall.order.service.ReturnUpdateService;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.dto.RefundDTO;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午2:46:23
 * 
 */
@Deprecated
@Facade("returnFacade")
public class ReturnFacadeImpl implements ReturnFacade {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private OrderService orderService;

	@Resource
	private ReturnQueryService retQueryService;

	@Resource
	private ReturnUpdateService retUpdateService;

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private ReturnCommonFacade retCommonFacade;

	@Resource
	private OrderFacade ordFacade;

	@Resource
	private CalCenterFacade calCenterFacade;

	@Resource
	private PromotionFacade promotionFacade;

	@Resource
	private CouponOrderService couponOrderService;

	@Resource
	private CouponService couponService;
	
	@Resource
	private MobilePushManageFacade mobilePushManageFacade;

	/**
	 * 
	 * @param entityList
	 * @return
	 */
	private List<RetOrdSkuPriceCalParam> convertCalParam(List<RetOrdSkuEntity> entityList) {
		List<RetOrdSkuPriceCalParam> calParamList = new ArrayList<RetOrdSkuPriceCalParam>();
		if (null == entityList) {
			return calParamList;
		}
		for (RetOrdSkuEntity entity : entityList) {
			calParamList.add(entity);
		}
		return calParamList;
	}

	/**
	 * 计算退款金额
	 * 
	 * @param param
	 * @param ordFormDTO
	 * @return
	 */
	private PriceParam calPrice(FrontReturnApplyParam param, OrderFormDTO ordFormDTO) {
		if (null == param || null == ordFormDTO) {
			return new PriceParam();
		}
		List<RetOrdSkuPriceCalParam> calParamList = convertCalParam(param.getList());
		Map<Long, List<PromotionSkuItemDTO>> refundItems = retCommonFacade.extractRefindItems(calParamList, ordFormDTO);
		Map<Long, List<PromotionSkuItemDTO>> skuItems = retCommonFacade.filterRefindItems(calParamList, ordFormDTO);
		FavorCaculateParamDTO paramDTO = retCommonFacade.extractFavorCaculateParam(ordFormDTO);
		RefundDTO refundDTO = calCenterFacade.refundCaculation(refundItems, skuItems, paramDTO);
		RefundType rt = RefundType.ORIGINAL_PATH.genEnumByIntValue(param.getRefundType());
		return calPriceExec(refundDTO, rt, param.getBankCard(), ordFormDTO);
	}

	/**
	 * 计算退款金额
	 * 
	 * @param refundDTO
	 * @param rt
	 * @param retBankCard
	 * @param ordForm
	 * @return
	 */
	private PriceParam calPriceExec(RefundDTO refundDTO, RefundType rt, ReturnBankCardParam retBankCard,
			OrderFormDTO ordForm) {
		PriceParam priceParam = new PriceParam();
		priceParam.setRefundType(rt);
		priceParam.setRetBankCard(retBankCard);
		if (null == refundDTO) {
			return priceParam;
		}
		BigDecimal expPrice = BigDecimal.ZERO;

		// 金额数据：退款金额 = 商品总金额 - 扣除运费 - 活动优惠金额 - 优惠券抵用金额
		// 1.1 商品总金额
		priceParam.setGoodsTotalRPrice(refundDTO.getTotalOriCash());
		if (!refundDTO.isExpFree() && null != ordForm) {
			expPrice = ordForm.getExpSysPayPrice();
			priceParam.setExpPrice(expPrice);
		}
		// 1.2&1.3 活动优惠金额&优惠券抵用金额
		priceParam.setHdYPrice(refundDTO.getPromotionCash());
		priceParam.setCouponYPrice(refundDTO.getCouponCash());
		// 1.4&1.5 退款金额&扣除运费
		double payedCash = refundDTO.getRefundTotalCash().doubleValue();
		double expCash = expPrice.doubleValue();
		payedCash = payedCash - expCash;
		if (payedCash < 0) {
			payedCash = 0;
		}
		priceParam.setPayedCashPrice(new BigDecimal(payedCash));

		// priceParam.setHbPrice(BigDecimal.ZERO);
		// priceParam.setReturnCashPrice(BigDecimal.ZERO);
		priceParam.setExpSubsidyPrice(BigDecimal.TEN);

		priceParam.setRecycle(refundDTO.isReturn());

		return priceParam;
	}

	/**
	 * 按照Sku的Id映射：List<PromotionSkuItemDTO> -> Map<Long, PromotionSkuItemDTO>
	 * 
	 * @param calRefundItems
	 * @return
	 */
	private Map<Long, Map<Long, PromotionSkuItemDTO>> mapCalRefundItems(
			Map<Long, List<PromotionSkuItemDTO>> calRefundItems) {
		Map<Long, Map<Long, PromotionSkuItemDTO>> ret = new HashMap<Long, Map<Long, PromotionSkuItemDTO>>();
		if (null == calRefundItems) {
			return ret;
		}
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : calRefundItems.entrySet()) {
			long poId = entry.getKey();
			List<PromotionSkuItemDTO> itemList = entry.getValue();
			if (null == itemList) {
				continue;
			}
			ret.put(poId, new HashMap<Long, PromotionSkuItemDTO>(itemList.size()));
			for (PromotionSkuItemDTO item : itemList) {
				ret.get(poId).put(item.getSkuId(), item);
			}
		}
		return ret;
	}

	/**
	 * 参数转换：ReturnFormApplyParam <- (FrontReturnApplyParam + OrderFormDTO)
	 * 
	 * @param param
	 * @param ordFormDTO
	 * @return
	 */
	private DeprecatedReturnFormApplyParam convertApplyParam(FrontReturnApplyParam param, OrderFormDTO ordFormDTO) {
		DeprecatedReturnFormApplyParam retParam = new DeprecatedReturnFormApplyParam();
		if (null == param || null == ordFormDTO) {
			return retParam;
		}
		List<RetOrdSkuEntity> entityList = param.getList();
		// 1. 金额数据
		Map<Long, OrderSkuDTO> allSku = ordFormDTO.mapOrderSkusByTheirId();
		List<RetOrdSkuPriceCalParam> calParamList = convertCalParam(param.getList());
		Map<Long, List<PromotionSkuItemDTO>> refundItems = retCommonFacade.extractRefindItems(calParamList, ordFormDTO);
		Map<Long, List<PromotionSkuItemDTO>> skuItems = retCommonFacade.filterRefindItems(calParamList, ordFormDTO);
		FavorCaculateParamDTO paramDTO = retCommonFacade.extractFavorCaculateParam(ordFormDTO);
		RefundDTO refundDTO = calCenterFacade.refundCaculation(refundItems, skuItems, paramDTO);
		RefundType rt = RefundType.ORIGINAL_PATH.genEnumByIntValue(param.getRefundType());
		PriceParam pp = calPriceExec(refundDTO, rt, param.getBankCard(), ordFormDTO);
		retParam.setPriceParam(pp);
		// 2. 退货的OrderSku列表
		Map<Long, List<PromotionSkuItemDTO>> calRefundItems = refundDTO.getRefundMap();
		Map<Long, Map<Long, PromotionSkuItemDTO>> mapedCalRefundItems = mapCalRefundItems(calRefundItems);
		for (RetOrdSkuEntity entity : entityList) {
			if (null == entity) {
				continue;
			}
			long ordSkuId = entity.getProductid();
			OrderSkuDTO ordSku = allSku.get(ordSkuId);
			if (null == ordSku || !mapedCalRefundItems.containsKey(ordSku.getPoId())) {
				continue;
			}
			long skuId = ordSku.getSkuId();
			Map<Long, PromotionSkuItemDTO> proms = mapedCalRefundItems.get(ordSku.getPoId());
			if (null == proms || !proms.containsKey(skuId)) {
				continue;
			}
			PromotionSkuItemDTO prom = proms.get(skuId);
			if (null == prom) {
				continue;
			}
			DeprecatedReturnOrderSkuParam rosp = new DeprecatedReturnOrderSkuParam();
			rosp.setOrderSkuId(ordSkuId);
			rosp.setSkuId(skuId);
			rosp.setCount(entity.getQuantity());
			rosp.setReason(entity.getComment());
			// skuRetPrice：显示的是订单中的零售价，还是退款服务计算后的退款价？
			BigDecimal skuRetPrice = prom.getOriRetailPrice();
			BigDecimal skuRetCount = new BigDecimal(prom.getCount());
			rosp.setTotalReturnPrice(skuRetPrice.multiply(skuRetCount));
			retParam.getRetOrderSkuParamList().add(rosp);
		}
		return retParam;
	}

	/**
	 * 退款商品金额计算实际执行方法
	 * 
	 * @param param
	 * @return
	 */
	private DeprecatedReturnPriceVO returnOrdSkuExec(FrontReturnApplyParam param) {
		DeprecatedReturnPriceVO rpVO = new DeprecatedReturnPriceVO();
		if (null == param) {
			return rpVO;
		}
		long orderId = param.getOrderId();
		long userId = SecurityContextUtils.getUserId();
		OrderFormDTO ordFormDTO = orderService.queryOrderForm(userId, orderId, null);
		if (null == ordFormDTO) {
			return rpVO;
		}
		PriceParam priceParam = calPrice(param, ordFormDTO);
		rpVO.fillWithPriceParam(priceParam);
		return rpVO;
	}

	/**
	 * 参数转换：FrontReturnExpInfoParam -> ReturnFormExpInfoParam
	 * 
	 * @param param
	 * @return
	 */
	private ReturnPackageExpInfoParam convertExpInfoParam(FrontReturnExpInfoParam param) {
		ReturnPackageExpInfoParam ret = new ReturnPackageExpInfoParam();
		if (null == param) {
			return ret;
		}
		ret.setReturnExpInfoId(param.getReturnExpInfoId());
		ret.setExpressCompany(ExpressCompany.NULL.genEnumByIntValue(param.getExpValue()));
		ret.setMailNO(param.getExpNO());
		return ret;
	}

	/**
	 * 
	 * @param param
	 * @param ordFormDTO
	 * @param orj
	 * @return
	 */
	private DeprecatedReturnOrderSkuInfoList extractRetOrdSkuInfo(OrderFormDTO ordFormDTO, DeprecatedOrderReturnJudgement orj) {
		DeprecatedReturnOrderSkuInfoList rosList = new DeprecatedReturnOrderSkuInfoList();
		if (null == ordFormDTO) {
			return rosList;
		}
		if (null == orj) {
			orj = DeprecatedOrderReturnJudgement.NULL;
		}
		long userId = ordFormDTO.getUserId();
		long orderId = ordFormDTO.getOrderId();
		// coupon: 订单关联的优惠券
		Coupon coupon = ordFacade.getCoupon(userId, orderId);
		// orderProm: 订单关联的促销 (策划说orderProm与poProms只能有一个存在)
		PromotionDTO orderProm = promotionFacade.getPromotionById(ordFormDTO.getPromotionId());
		// poProms: po关联的促销 (策划说orderProm与poProms只能有一个存在)
		// List<Long> poIdList = ordFormDTO.extractPOIdList();
		// Map<Long, PromotionDTO> poProms = promotionFacade.getPromotionListByPos(poIdList, false);
		Map<Long, PromotionDTO> poProms = retCommonFacade.extractPromotion(ordFormDTO);
		Map<Long, OrderSkuDTO> allSku = ordFormDTO.mapOrderSkusByTheirId();
		rosList.setTotal(allSku.size());
		for (Entry<Long, OrderSkuDTO> entry : allSku.entrySet()) {
			OrderSkuDTO ordSkuDTO = entry.getValue();
			if (null == ordSkuDTO) {
				continue;
			}
			// DeprecatedOrderSkuReturnJudgement osrj = orderService.canOrderSkuBeReturned(orj, ordSkuDTO);
			DeprecatedOrderSkuReturnJudgement osrj = DeprecatedOrderSkuReturnJudgement.NULL;
			ReturnOrderSkuInfo rosVO = new ReturnOrderSkuInfo();
			PromotionDTO selectedProm = orderProm;
			// (策划说orderProm与poProms只能有一个存在)
			if (null == orderProm && null != poProms && poProms.containsKey(ordSkuDTO.getPoId())) {
				selectedProm = poProms.get(ordSkuDTO.getPoId());
			}
			rosVO.fillWithOrderSku(ordSkuDTO, orj, osrj, selectedProm, coupon);
			rosList.getList().add(rosVO);
		}
		return rosList;
	}

	/**
	 * 
	 * @param retFormDTO
	 * @return
	 */
	private DeprecatedReturnOrderSkuInfoList extractRetOrdSkuInfo(DeprecatedReturnFormDTO retFormDTO) {
		DeprecatedReturnOrderSkuInfoList rosList = new DeprecatedReturnOrderSkuInfoList();
		if (null == retFormDTO) {
			return rosList;
		}
		OrderFormDTO ordFormDTO = retFormDTO.getOrderFormDTO();
		if (null == ordFormDTO) {
			return rosList;
		}
		List<DeprecatedReturnOrderSkuDTO> retOrdSkuList = retFormDTO.getRetOrderSkuList();
		if (null == retOrdSkuList) {
			return rosList;
		}
		rosList.setTotal(retOrdSkuList.size());
		long userId = ordFormDTO.getUserId();
		long orderId = ordFormDTO.getOrderId();
		// coupon: 订单关联的优惠券
		Coupon coupon = ordFacade.getCoupon(userId, orderId);
		// orderProm: 订单关联的促销 (策划说orderProm与poProms只能有一个存在)
		PromotionDTO orderProm = promotionFacade.getPromotionById(ordFormDTO.getPromotionId());
		// poProms: po关联的促销 (策划说orderProm与poProms只能有一个存在)
		// List<Long> poIdList = ordFormDTO.extractPOIdList();
		// Map<Long, PromotionDTO> poProms = promotionFacade.getPromotionListByPos(poIdList, false);
		Map<Long, PromotionDTO> poProms = retCommonFacade.extractPromotion(ordFormDTO);
		for (DeprecatedReturnOrderSkuDTO retOrdSku : retOrdSkuList) {
			if (null == retOrdSku) {
				continue;
			}
			OrderSkuDTO ordSkuDTO = retOrdSku.getOrdSkuDTO();
			if (null == ordSkuDTO) {
				continue;
			}
			PromotionDTO selectedProm = orderProm;
			// (策划说orderProm与poProms只能有一个存在)
			if (null == orderProm && null != poProms && poProms.containsKey(ordSkuDTO.getPoId())) {
				selectedProm = poProms.get(ordSkuDTO.getPoId());
			}
			ReturnOrderSkuInfo rosVO = new ReturnOrderSkuInfo();
			DeprecatedOrderReturnJudgement orj = DeprecatedOrderReturnJudgement.APPLY_RETURN;
			DeprecatedOrderSkuReturnJudgement osrj = DeprecatedOrderSkuReturnJudgement.APPLY_RETURN;
			rosVO.fillWithRetOrderSku(retOrdSku, orj, osrj, selectedProm, coupon);
			rosList.getList().add(rosVO);
		}
		return rosList;
	}

	/**
	 * 回填ReturnExpInfoVO数据
	 * 
	 * @param vo
	 * @param retForm
	 */
	private void fillReturnExpInfoVO(DeprecatedReturnExpInfoVO vo, DeprecatedReturnFormDTO retForm) {
		if (null == vo || null == retForm) {
			return;
		}
		// 0. 回传数据：returnExpress(ExpInfoVO )
		vo.getReturnExpress().fillWithReturnForm(retForm);
		// 1. 回传数据：orderId + returnId + returnState
		vo.setOrderId(String.valueOf(retForm.getOrderId()));
		vo.setReturnId(String.valueOf(retForm.getId()));
		vo.setReturnState(retForm.getReturnState());
		// 2. 回传数据：returns(ReturnOrderSkuVOList)
		DeprecatedReturnOrderSkuInfoList retOrdSkuInfoList = extractRetOrdSkuInfo(retForm);
		vo.setReturns(retOrdSkuInfoList);
		// 3. 回传数据：returnForm(ReturnPriceVO)
		DeprecatedReturnPriceVO retPrice = new DeprecatedReturnPriceVO();
		DeprecatedReturnCODBankCardInfoDTO bankCard = retQueryService.queryReturnCODBankCardInfo(retForm.getId(),
				retForm.getUserId());
		retPrice.fillWithReturnForm(retForm, bankCard);
		vo.setReturnForm(retPrice);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#queryReturnFormByUserIdAndOrderId(long,
	 *      long)
	 */
	public List<DeprecatedReturnFormDTO> queryReturnFormByUserIdAndOrderId(long userId, long orderId) {
		List<DeprecatedReturnFormDTO> retFormDTOList = retQueryService.queryReturnFormByUserIdAndOrderId(userId, orderId, null);
		return retFormDTOList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#getReturnOrderSkuVOList(long)
	 */
	@Override
	public DeprecatedReturnOrderSkuInfoListVO getReturnOrderSkuVOList(long orderId, long userId) {
		DeprecatedReturnOrderSkuInfoListVO rosListWrap = new DeprecatedReturnOrderSkuInfoListVO();
		OrderFormDTO ordFormDTO = orderService.queryOrderForm(userId, orderId, null);
		if (null == ordFormDTO) {
			return rosListWrap;
		}
		DeprecatedOrderReturnJudgement orj = getOrderReturnJudgement(ordFormDTO);

		// 1. 回传数据：ReturnOrderSkuInfoListVO.returns.total +
		// ReturnOrderSkuInfoListVO.returns.list
		DeprecatedReturnOrderSkuInfoList retOrdSkuInfoList = extractRetOrdSkuInfo(ordFormDTO, orj);
		rosListWrap.setReturns(retOrdSkuInfoList);

		// 2. 回传数据：orderId + payMethod + returnState
		rosListWrap.setOrderId(String.valueOf(orderId));
		rosListWrap.setPayMethod(ordFormDTO.getOrderFormPayMethod());
		rosListWrap.setReturnState(DeprecatedReturnState.INIT);
		/** 如果已经提交申请，更新ReturnState */
		List<DeprecatedReturnFormDTO> retFormDTOList = retQueryService.queryReturnFormByUserIdAndOrderId(userId, orderId, null);
		for (DeprecatedReturnFormDTO retFormDTO : retFormDTOList) {
			if (orderId == retFormDTO.getOrderId()) {
				rosListWrap.setReturnState(retFormDTO.getReturnState());
				break;
			}
		}

		return rosListWrap;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#getOrderReturnJudgement(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	public DeprecatedOrderReturnJudgement getOrderReturnJudgement(OrderFormDTO orderFormDTO) {
		long earliestPOEndTime = retCommonFacade.getEarliestPOEndTime(orderFormDTO);
		// DeprecatedOrderReturnJudgement orj = orderService.canOrderBeReturned(orderFormDTO, earliestPOEndTime);
		DeprecatedOrderReturnJudgement orj = DeprecatedOrderReturnJudgement.NULL;
		return orj;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#returnAllOrdSku(com.xyl.mmall.mainsite.facade.param.FrontReturnApplyParam)
	 */
	@Override
	public DeprecatedReturnPriceVO returnAllOrdSku(FrontReturnApplyParam param) {
		return returnOrdSkuExec(param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#addRetOrdSkuDefault(com.xyl.mmall.mainsite.facade.param.FrontReturnApplyParam)
	 */
	@Override
	public DeprecatedReturnPriceVO addRetOrdSkuDefault(FrontReturnApplyParam param) {
		return returnOrdSkuExec(param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#increaseRetOrdSku(com.xyl.mmall.mainsite.facade.param.FrontReturnApplyParam)
	 */
	@Override
	public DeprecatedReturnPriceVO increaseRetOrdSku(FrontReturnApplyParam param) {
		return returnOrdSkuExec(param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#removeRetOrdSku(com.xyl.mmall.mainsite.facade.param.FrontReturnApplyParam)
	 */
	@Override
	public DeprecatedReturnPriceVO removeRetOrdSku(FrontReturnApplyParam param) {
		return returnOrdSkuExec(param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#decreaseRetOrdSku(com.xyl.mmall.mainsite.facade.param.FrontReturnApplyParam)
	 */
	@Override
	public DeprecatedReturnPriceVO decreaseRetOrdSku(FrontReturnApplyParam param) {
		return returnOrdSkuExec(param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#applyReturn(com.xyl.mmall.mainsite.facade.param.FrontReturnApplyParam)
	 */
	@Override
	public DeprecatedReturnApplyVO applyReturn(FrontReturnApplyParam param) {
		DeprecatedReturnApplyVO rosListWrap = new DeprecatedReturnApplyVO();
		if (null == param) {
			return rosListWrap;
		}
		long orderId = param.getOrderId();
		long userId = SecurityContextUtils.getUserId();
		OrderFormDTO ordFormDTO = orderService.queryOrderForm(userId, orderId, null);
		if (null == ordFormDTO) {
			return rosListWrap;
		}
		Map<Long, OrderSkuDTO> allSku = ordFormDTO.mapOrderSkusByTheirId();
		if (null == allSku) {
			return rosListWrap;
		}
		// ! 数据入库：
		DeprecatedReturnFormApplyParam retApplyParam = convertApplyParam(param, ordFormDTO);
		DeprecatedReturnFormDTO retFormDTO = retUpdateService.applyReturn(userId, orderId, null, retApplyParam);
		if (null == retFormDTO) {
			return rosListWrap;
		}
		// 0. 回传数据：returnInfo(ReturnAddressVO)，等待其他服务对接
		rosListWrap.setReturnInfo((new ReturnAddressVO()).fillWithWarehouse(retCommonFacade
				.getReturnWarehouseForm(ordFormDTO)));
		// 1. 回传数据：orderId + returnId + returnState
		rosListWrap.setOrderId(String.valueOf(orderId));
		rosListWrap.setReturnId(String.valueOf(retFormDTO.getId()));
		rosListWrap.setReturnState(retFormDTO.getReturnState());
		// 2. 回传数据：returns(ReturnOrderSkuVOList)
		DeprecatedReturnOrderSkuInfoList retOrdSkuInfoList = extractRetOrdSkuInfo(retFormDTO);
		rosListWrap.setReturns(retOrdSkuInfoList);
		// 3. 回传数据：returnForm(ReturnPriceVO)
		DeprecatedReturnPriceVO retPrice = new DeprecatedReturnPriceVO();
		retPrice.fillWithPriceParam(calPrice(param, ordFormDTO));
		rosListWrap.setReturnForm(retPrice);

		// return
		return rosListWrap;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#getApply(long)
	 */
	@Override
	public DeprecatedReturnApplyVO getApply(long orderId) {
		DeprecatedReturnApplyVO rosListWrap = new DeprecatedReturnApplyVO();
		long userId = SecurityContextUtils.getUserId();
		List<DeprecatedReturnFormDTO> retFormDTOList = retQueryService.queryReturnFormByUserIdAndOrderId(userId, orderId, null);
		if (null == retFormDTOList || 0 == retFormDTOList.size()) {
			return rosListWrap;
		}
		// to be reviewed：ugly code, due to design of ReturnForm
		DeprecatedReturnFormDTO retFormDTO = retFormDTOList.get(0);
		if (null == retFormDTO) {
			return rosListWrap;
		}
		OrderFormDTO ordFormDTO = retFormDTO.getOrderFormDTO();
		if (null == ordFormDTO) {
			return rosListWrap;
		}
		Map<Long, OrderSkuDTO> allSku = ordFormDTO.mapOrderSkusByTheirId();
		if (null == allSku) {
			return rosListWrap;
		}

		// 0. 回传数据：returnInfo(ReturnAddressVO)，等待其他服务对接
		rosListWrap.setReturnInfo((new ReturnAddressVO()).fillWithWarehouse(retCommonFacade
				.getReturnWarehouseForm(ordFormDTO)));
		// 1. 回传数据：orderId
		rosListWrap.setOrderId(String.valueOf(orderId));
		// 2. 回传数据：returns(ReturnOrderSkuVOList)
		DeprecatedReturnOrderSkuInfoList retOrdSkuInfoList = extractRetOrdSkuInfo(retFormDTO);
		rosListWrap.setReturns(retOrdSkuInfoList);
		// 3. 回传数据：returnForm(ReturnPriceVO)
		DeprecatedReturnPriceVO retPrice = new DeprecatedReturnPriceVO();
		DeprecatedReturnCODBankCardInfoDTO bankCard = retQueryService.queryReturnCODBankCardInfo(retFormDTO.getId(), userId);
		retPrice.fillWithReturnForm(retFormDTO, bankCard);
		rosListWrap.setReturnForm(retPrice);
		// 4. 回传数据：returnId + returnState
		if (null != retFormDTO) {
			rosListWrap.setReturnId(String.valueOf(retFormDTO.getId()));
			rosListWrap.setReturnState(retFormDTO.getReturnState());
		}
		// return
		return rosListWrap;

	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#completeApplyWithExpInfo(com.xyl.mmall.mainsite.facade.param.FrontReturnExpInfoParam)
	 */
	@Override
	public DeprecatedReturnExpInfoVO completeApplyWithExpInfo(FrontReturnExpInfoParam param) {
		DeprecatedReturnExpInfoVO ret = new DeprecatedReturnExpInfoVO();
		if (null == param) {
			return ret;
		}
		ReturnPackageExpInfoParam expInfo = convertExpInfoParam(param);
		DeprecatedReturnFormDTO retFormDTO = retUpdateService.updateReturnExpInfo(param.getReturnId(), expInfo);
		if (null == retFormDTO) {
			throw new ServiceException("提交申请失败");
		}
		fillReturnExpInfoVO(ret, retFormDTO);
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#getReturning(long)
	 */
	@Override
	public DeprecatedReturnExpInfoVO getReturning(long orderId) {
		DeprecatedReturnExpInfoVO ret = new DeprecatedReturnExpInfoVO();
		long userId = SecurityContextUtils.getUserId();
		List<DeprecatedReturnFormDTO> retFormDTOList = retQueryService.queryReturnFormByUserIdAndOrderId(userId, orderId, null);
		if (null == retFormDTOList || 0 == retFormDTOList.size()) {
			logger.warn("no ReturnForm for [userId:" + userId + ", orderId:" + orderId + "]");
			return ret;
		}
		DeprecatedReturnFormDTO retFormDTO = retFormDTOList.get(0);
		if (null == retFormDTO) {
			logger.warn("null retFormDTO in retFormDTOList");
			return ret;
		}
		fillReturnExpInfoVO(ret, retFormDTO);
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#returnStatus(long)
	 */
	@Override
	public DeprecatedReturnStatusVO returnStatus(long orderId) {
		DeprecatedReturnStatusVO ret = new DeprecatedReturnStatusVO();
		long userId = SecurityContextUtils.getUserId();
		List<DeprecatedReturnFormDTO> retFormDTOList = retQueryService.queryReturnFormByUserIdAndOrderId(userId, orderId, null);
		if (null == retFormDTOList || 0 == retFormDTOList.size()) {
			return ret;
		}
		DeprecatedReturnFormDTO retFormDTO = retFormDTOList.get(0);
		// 0. 回传数据：status(StatusInfo) + price(ReturnPriceVOExtension)
		DeprecatedReturnState retState = retFormDTO.getReturnState();
		if (DeprecatedReturnState.FINISH_RETURN == retState) {
			ret.setStatusInfo(new StatusInfo(StatusInfo.Status.SUCCESSFUL, retFormDTO.getExtInfo()));
		} else if (DeprecatedReturnState.REFUSED == retState || DeprecatedReturnState.CANCELED == retState) {
			ret.setStatusInfo(new StatusInfo(StatusInfo.Status.FAILED, retFormDTO.getExtInfo()));
		} else {
			ret.setStatusInfo(new StatusInfo(StatusInfo.Status.RETURNING, retFormDTO.getExtInfo()));
		}
		DeprecatedReturnCODBankCardInfoDTO bankCard = retQueryService.queryReturnCODBankCardInfo(retFormDTO.getId(),
				retFormDTO.getUserId());
		ret.getPrice().fillWithParams(retFormDTO, bankCard, retFormDTO.getReturnTime(), retFormDTO.getExtInfo());
		// 1. 回传数据：orderId + returnId + returnState
		ret.setOrderId(String.valueOf(retFormDTO.getOrderId()));
		ret.setReturnId(String.valueOf(retFormDTO.getId()));
		ret.setReturnState(retFormDTO.getReturnState());
		// 2. 回传数据：returns(ReturnOrderSkuVOList)
		DeprecatedReturnOrderSkuInfoList retOrdSkuInfoList = extractRetOrdSkuInfo(retFormDTO);
		ret.setReturns(retOrdSkuInfoList);
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnFacade#cancelReturn(long)
	 */
	@Override
	public boolean cancelReturn(long orderId) {
		long userId = SecurityContextUtils.getUserId();
		return retUpdateService.deprecateApply(userId, orderId);
	}

}
