package com.xyl.mmall.mainsite.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.facade.ReturnPackageCommonFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;
import com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam;
import com.xyl.mmall.mainsite.facade.param._FrontReturnExpInfoParam;
import com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam.RetOrdSkuEntity;
import com.xyl.mmall.mainsite.vo.order.ReturnApplyVO;
import com.xyl.mmall.mainsite.vo.order.ReturnExpInfoVO;
import com.xyl.mmall.mainsite.vo.order.ReturnOrderSkuInfoList;
import com.xyl.mmall.mainsite.vo.order.ReturnOrderSkuInfoListVO;
import com.xyl.mmall.mainsite.vo.order.ReturnPriceVO;
import com.xyl.mmall.mainsite.vo.order.ReturnStatusVO;
import com.xyl.mmall.mainsite.vo.order.ReturnApplyVO.ReturnAddressVO;
import com.xyl.mmall.mainsite.vo.order.ReturnExpInfoVO.ExpInfoVO;
import com.xyl.mmall.mainsite.vo.order.ReturnOrderSkuInfoList.ReturnOrderSkuInfo;
import com.xyl.mmall.mainsite.vo.order.ReturnPriceVO.ReturnCoupon;
import com.xyl.mmall.mainsite.vo.order.ReturnStatusVO.StatusInfo;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.order.api.util.ReturnPriceCalculator;
import com.xyl.mmall.order.api.util.ReturnPriceCalculator._ReturnPackagePriceParam;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.ReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderSkuReturnJudgement;
import com.xyl.mmall.order.enums.PackageReturnJudgement;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.order.param.ReturnBankCardParam;
import com.xyl.mmall.order.param.ReturnOrderSkuParam;
import com.xyl.mmall.order.param.ReturnPackageApplyParam;
import com.xyl.mmall.order.param.ReturnPackageExpInfoParam;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.order.service.ReturnPackageUpdateService;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.saleschedule.service.ScheduleService;
import com.xyl.mmall.timer.facade.ReturnTimerFacade;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午2:46:23
 * 
 */
@Facade("returnPackageFacade")
public class ReturnPackageFacadeImpl implements ReturnPackageFacade {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private OrderService ordService;
	
	@Resource
	private OrderPackageSimpleService ordPkgSimpleService;

	@Resource
	private ReturnPackageQueryService retPkgQueryService;

	@Resource
	private ReturnPackageUpdateService retPkgUpdateService;

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private ReturnPackageCommonFacade retPkgCommonFacade;
	
	@Resource
	private ReturnTimerFacade retTimerFacade;

	@Resource
	private OrderFacade ordFacade;

	@Resource
	private PromotionFacade promotionFacade;

	@Resource
	private CouponOrderService couponOrderService;

	@Resource
	private CouponService couponService;
	
	@Resource
	private MobilePushManageFacade mobilePushManageFacade;
	
	@Resource
	private JITSupplyManagerFacade jitSupplyManagerFacade;
	
	@Resource
	private RedPacketOrderService redPacketOrderService;

	/**
	 * 参数转换
	 * 
	 * @param entityList
	 * @param orderSkuMap
	 * @return
	 */
	private List<ReturnOrderSkuParam> convertEntityListToParamList(List<RetOrdSkuEntity> entityList, Map<Long, OrderSkuDTO> orderSkuMap) {
		List<ReturnOrderSkuParam> retParamList = new ArrayList<ReturnOrderSkuParam>();
		if(CollectionUtil.isEmptyOfMap(orderSkuMap) || CollectionUtil.isEmptyOfList(entityList)) {
			return retParamList;
		}
		for(RetOrdSkuEntity entity : entityList) {
			if(null == entity) {
				continue;
			}
			long orderSkuId = entity.getProductid();
			OrderSkuDTO ordSkuDTO = orderSkuMap.get(orderSkuId);
			if(null == ordSkuDTO) {
				continue;
			}
			ReturnOrderSkuParam p = new ReturnOrderSkuParam();
			p.setOrderSkuId(orderSkuId);
			p.setSkuId(ordSkuDTO.getSkuId());
			p.setRetCount(entity.getQuantity());
			p.setReason(entity.getComment());
			retParamList.add(p);
		}
		return retParamList;
	}
	
	/**
	 * 计算退款金额
	 * 
	 * @param ordPkgDTO
	 * @param param
	 * @return
	 */
	private _ReturnPackagePriceParam calPrice(OrderPackageSimpleDTO ordPkgDTO, _FrontReturnApplyParam param) {
		_ReturnPackagePriceParam priceParam = new _ReturnPackagePriceParam();
		if (null == ordPkgDTO || null == param) {
			return priceParam;
		}
		List<ReturnOrderSkuParam> retParamList = convertEntityListToParamList(param.getList(), ordPkgDTO.getOrderSkuMap());
		return ReturnPriceCalculator.compute(ordPkgDTO, retParamList);
	}

	/**
	 * 参数转换：_ReturnPackageApplyParam <- (_OrderPackageSimpleDTO + _FrontReturnApplyParam)
	 * 
	 * @param param
	 * @param ordPkgDTO
	 * @return
	 */
	private ReturnPackageApplyParam convertApplyParam(OrderPackageSimpleDTO ordPkgDTO, _FrontReturnApplyParam param) {
		ReturnPackageApplyParam retParam = new ReturnPackageApplyParam();
		if (null == param || null == ordPkgDTO) {
			return retParam;
		}
		Map<Long, OrderSkuDTO> ordSkuMap = ordPkgDTO.getOrderSkuMap();
		List<RetOrdSkuEntity> entityList = param.getList();
		if(CollectionUtil.isEmptyOfMap(ordSkuMap) || CollectionUtil.isEmptyOfList(entityList)) {
			return retParam;
		}
		retParam.setRetOrderSkuParamList(convertEntityListToParamList(entityList, ordSkuMap));
		retParam.setRefundType(RefundType.ORIGINAL_PATH.genEnumByIntValue(param.getRefundType()));
		retParam.setRetBankCard(param.getBankCard());
		CouponOrder co = couponOrderService.getCouponOrderByOrderIdOfUseType(ordPkgDTO.getUserId(), ordPkgDTO.getOrderId());
		retParam.setCouponUsedInOrder(null != co);
		return retParam;
	}

	/**
	 * 获取退回的订单优惠券信息
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	private List<ReturnCoupon> getOrderReturnCoupon(long userId, long orderId) {
		List<ReturnCoupon> returnCoupon = new ArrayList<ReturnCoupon>();
		CouponOrder couponOrder = couponOrderService.getCouponOrderByOrderIdOfUseType(userId, orderId);
		if(null != couponOrder) {
			boolean recycled = retTimerFacade.couponRecycled(userId, orderId);
			Coupon coupon = null;
			coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
			if(null == coupon) {
				coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), true);
			}
			returnCoupon.add((new ReturnCoupon()).fillWithCouponOrder(couponOrder, coupon, recycled));
		}
		return returnCoupon;
	}
	
	/**
	 * 获取退回的订单优惠券信息
	 * 
	 * @param retPkgDTO
	 * @return
	 */
	private List<ReturnCoupon> getOrderReturnCoupon(ReturnPackageDTO retPkgDTO) {
		List<ReturnCoupon> returnCoupon = new ArrayList<ReturnCoupon>();
		if(null == retPkgDTO) {
			return returnCoupon;
		}
		ReturnPackageState state = retPkgDTO.getReturnState();
		long userId = retPkgDTO.getUserId();
		long orderId = retPkgDTO.getOrderId();
		OrderPackageSimpleDTO ordPkgDTO = retPkgDTO.getOrdPkgDTO();
		boolean applySituation = !CollectionUtil.isInArray(ReturnPackageState.stateArrayOfReturned(), state);
		if(applySituation) {
			if(retPkgCommonFacade.totalOrderApplyedReturn(ordPkgDTO)) {
				return getOrderReturnCoupon(userId, orderId);
			}
		} else {
			if(retPkgCommonFacade.totalOrderConfirmedReturn(ordPkgDTO)) {
				return getOrderReturnCoupon(userId, orderId);
			}
		}
		return returnCoupon;
	}
	
	/**
	 * 退款商品金额计算实际执行方法
	 * 
	 * @param param
	 * @return
	 */
	private ReturnPriceVO returnOrdSkuExec(_FrontReturnApplyParam param) {
		ReturnPriceVO rpVO = new ReturnPriceVO();
		if (null == param) {
			return rpVO;
		}
		long ordPkgId = param.getOrdPkgId();
		long userId = SecurityContextUtils.getUserId();
		OrderPackageSimpleDTO ordPkgDTO = ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
		if (null == ordPkgDTO) {
			return rpVO;
		}
		_ReturnPackagePriceParam priceParam = calPrice(ordPkgDTO, param);
		RefundType rt = RefundType.ORIGINAL_PATH.genEnumByIntValue(param.getRefundType());
		ReturnBankCardParam bankCard = param.getBankCard();
		String bankCardInfo = "";
		if(null != bankCard) {
			bankCardInfo = bankCard.getBankCardNO();
		}
		List<ReturnCoupon> returnCoupon = new ArrayList<ReturnCoupon>();
		if(retPkgCommonFacade.totalOrderApplyedReturn(ordPkgDTO, param)) {
			returnCoupon = getOrderReturnCoupon(userId, ordPkgDTO.getOrderId());
		}
		rpVO.fillWithPriceParam(priceParam, rt, bankCardInfo, returnCoupon);
		return rpVO;
	}

	/**
	 * 参数转换：_FrontReturnExpInfoParam -> ReturnPackageExpInfoParam
	 * 
	 * @param param
	 * @return
	 */
	private ReturnPackageExpInfoParam convertExpInfoParam(_FrontReturnExpInfoParam param) {
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
	 * @param ordPkgDTO
	 * @param prj
	 * @return
	 */
	private ReturnOrderSkuInfoList extractRetOrdSkuInfo(OrderPackageSimpleDTO ordPkgDTO, PackageReturnJudgement prj) {
		ReturnOrderSkuInfoList rosList = new ReturnOrderSkuInfoList();
		Map<Long, OrderSkuDTO> allSku = null;
		if (null == ordPkgDTO || CollectionUtil.isEmptyOfMap(allSku = ordPkgDTO.getOrderSkuMap())) {
			return rosList;
		}
		if (null == prj) {
			prj = PackageReturnJudgement.NULL;
		}
		long userId = ordPkgDTO.getUserId();
		long orderId = ordPkgDTO.getOrderId();
		// coupon: 订单关联的优惠券
		Coupon coupon = ordFacade.getCoupon(userId, orderId);
		OrderFormDTO ordFormDTO = ordService.queryOrderForm(userId, orderId, null);
		if(null == ordFormDTO) {
			return rosList;
		}
		PromotionDTO orderProm = promotionFacade.getPromotionById(ordFormDTO.getPromotionId());
		// poProms: po关联的促销 (策划说orderProm与poProms只能有一个存在)
		Map<Long, PromotionDTO> poProms = ordFacade.extractPromotion(ordFormDTO);
		for (Entry<Long, OrderSkuDTO> entry : allSku.entrySet()) {
			OrderSkuDTO ordSkuDTO = entry.getValue();
			if (null == ordSkuDTO) {
				continue;
			}
			OrderSkuReturnJudgement osrj = ordPkgSimpleService.canOrderSkuBeReturned(prj, ordSkuDTO);
			ReturnOrderSkuInfo rosVO = new ReturnOrderSkuInfo();
			PromotionDTO selectedProm = orderProm;
			// (策划说orderProm与poProms只能有一个存在)
			if (null == orderProm && null != poProms && poProms.containsKey(ordSkuDTO.getPoId())) {
				selectedProm = poProms.get(ordSkuDTO.getPoId());
			}
			rosVO.fillWithOrderSku(ordSkuDTO, prj, osrj, selectedProm, coupon);
			rosList.getList().add(rosVO);
		}
		rosList.setTotal(rosList.getList().size());
		return rosList;
	}

	/**
	 * 
	 * @param retPkgDTO
	 * @return
	 */
	private ReturnOrderSkuInfoList extractRetOrdSkuInfo(ReturnPackageDTO retPkgDTO) {
		ReturnOrderSkuInfoList rosList = new ReturnOrderSkuInfoList();
		if (null == retPkgDTO) {
			return rosList;
		}
		Map<Long, ReturnOrderSkuDTO> retOrdSkuMap = retPkgDTO.getRetOrdSkuMap();
		if (CollectionUtil.isEmptyOfMap(retOrdSkuMap)) {
			return rosList;
		}
		rosList.setTotal(retOrdSkuMap.size());
		long userId = retPkgDTO.getUserId();
		long orderId = retPkgDTO.getOrderId();
		// coupon: 订单关联的优惠券
		Coupon coupon = ordFacade.getCoupon(userId, orderId);
		// orderProm: 订单关联的促销 (策划说orderProm与poProms只能有一个存在)
		OrderFormDTO ordFormDTO = ordService.queryOrderForm(userId, orderId, null);
		if(null == ordFormDTO) {
			return rosList;
		}
		PromotionDTO orderProm = promotionFacade.getPromotionById(ordFormDTO.getPromotionId());
		// poProms: po关联的促销 (策划说orderProm与poProms只能有一个存在)
		Map<Long, PromotionDTO> poProms = ordFacade.extractPromotion(ordFormDTO);
		for (Entry<Long, ReturnOrderSkuDTO> entry : retOrdSkuMap.entrySet()) {
			ReturnOrderSkuDTO retOrdSku = entry.getValue();
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
			PackageReturnJudgement prj = PackageReturnJudgement.APPLY_RETURN;
			OrderSkuReturnJudgement osrj = OrderSkuReturnJudgement.APPLY_RETURN;
			rosVO.fillWithRetOrderSku(retOrdSku, prj, osrj, selectedProm, coupon);
			rosList.getList().add(rosVO);
		}
		return rosList;
	}

	/**
	 * 回填_ReturnExpInfoVO数据
	 * 
	 * @param vo
	 * @param retPkg
	 */
	private void fillReturnExpInfoVO(ReturnExpInfoVO vo, ReturnPackageDTO retPkg) {
		if (null == vo || null == retPkg) {
			return;
		}
		long orderId = retPkg.getOrderId();
		long orderPkgId = retPkg.getOrderPkgId();
		long retPkgId = retPkg.getRetPkgId();
		// 0. 回传数据：returnExpress(ExpInfoVO )
		vo.getReturnExpress().fillWithReturnPackage(retPkg);
		// 1. 回传数据：orderId + ordPkgId + returnId + returnState + pay
		vo.setOrderId(String.valueOf(orderId));
		vo.setOrdPkgId(String.valueOf(orderPkgId));
		vo.setRetPkgId(String.valueOf(retPkgId));
		vo.setReturnState(retPkg.getReturnState());
		if(null != retPkg.getOrdFormBriefDTO()) {
			vo.setPayMethod(retPkg.getOrdFormBriefDTO().getOrderFormPayMethod());
		}
		// 2. 回传数据：returns(ReturnOrderSkuVOList)
		ReturnOrderSkuInfoList retOrdSkuInfoList = extractRetOrdSkuInfo(retPkg);
		vo.setReturns(retOrdSkuInfoList);
		// 3. 回传数据：returnForm(_ReturnPriceVO)
		ReturnPriceVO retPrice = new ReturnPriceVO();
		ReturnCODBankCardInfoDTO bankCard = retPkgQueryService.queryReturnCODBankCardInfo(retPkg.getBankCardInfoId(), retPkg.getUserId());
		String bankCardNO = null == bankCard ? "" : bankCard.getBankCardNO();
		boolean applySituation = !CollectionUtil.isInArray(ReturnPackageState.stateArrayOfReturned(), retPkg.getReturnState());
		List<ReturnCoupon> retCoupon = getOrderReturnCoupon(retPkg);
		retPrice.fillWithReturnPackage(retPkg, applySituation, bankCardNO, retCoupon);
		vo.setReturnForm(retPrice);
	}

	private ReturnApplyVO getApplyExec(ReturnPackageDTO retPkgDTO) {
		ReturnApplyVO retVO = new ReturnApplyVO();
		if (null == retPkgDTO) {
			return retVO;
		}
		OrderPackageSimpleDTO ordPkgDTO = retPkgDTO.getOrdPkgDTO();
		if (null == ordPkgDTO) {
			return retVO;
		}
		long userId = retPkgDTO.getUserId();
		long orderId = retPkgDTO.getOrderId();
		long ordPkgId = retPkgDTO.getOrderPkgId();
		long retPkgId = retPkgDTO.getRetPkgId();
		// 0. 回传数据：returnInfo(ReturnAddressVO)，等待其他服务对接
		WarehouseForm warehouse = retPkgCommonFacade.getReturnWarehouseForm(ordPkgDTO);
		retVO.setReturnInfo((new ReturnAddressVO()).fillWithWarehouse(warehouse));
		// 1. 回传数据：回传数据：orderId + ordPkgId + returnId + returnState
		retVO.setOrderId(String.valueOf(orderId));
		retVO.setOrdPkgId(String.valueOf(ordPkgId));
		retVO.setRetPkgId(String.valueOf(retPkgId));
		retVO.setReturnState(retPkgDTO.getReturnState());
		// 2. 回传数据：returns(ReturnOrderSkuVOList)
		ReturnOrderSkuInfoList retOrdSkuInfoList = extractRetOrdSkuInfo(retPkgDTO);
		retVO.setReturns(retOrdSkuInfoList);
		// 3. 回传数据：returnForm(_ReturnPriceVO)
		ReturnPriceVO retPrice = new ReturnPriceVO();
		ReturnCODBankCardInfoDTO bankCardInfo = retPkgQueryService.queryReturnCODBankCardInfo(retPkgDTO.getBankCardInfoId(), userId);
		String bankCardNOInfo = null == bankCardInfo ? "" : bankCardInfo.getBankCardNO();
		boolean applySituation = !CollectionUtil.isInArray(ReturnPackageState.stateArrayOfReturned(), retPkgDTO.getReturnState());
		List<ReturnCoupon> retCoupon = getOrderReturnCoupon(retPkgDTO);
		retPrice.fillWithReturnPackage(retPkgDTO, applySituation, bankCardNOInfo, retCoupon);
		retVO.setReturnForm(retPrice);
		// return
		return retVO;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#queryReturnPackageByOrderPackageId(long)
	 */
	@Override
	public ReturnPackageDTO queryReturnPackageByOrderPackageId(long ordPkgId) {
		long userId = SecurityContextUtils.getUserId();
		return queryReturnPackageByOrderPackageId(ordPkgId, userId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#queryReturnPackageByOrderPackageId(long,
	 *      long)
	 */
	public ReturnPackageDTO queryReturnPackageByOrderPackageId(long ordPkgId, long userId) {
		List<ReturnPackageDTO> retPkgDTOList = retPkgQueryService.queryReturnPackageByOrderPackageId(userId, ordPkgId,
				false, null);
		if (CollectionUtil.isEmptyOfList(retPkgDTOList)) {
			return null;
		}
		if (1 != retPkgDTOList.size()) {
			logger.error("退货业务逻辑出现错误！包裹退货次数大于1！[userId:" + userId + ", ordPkgId:" + ordPkgId + "]");
		}
		return retPkgDTOList.get(0);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#getReturnOrderSkuVOList(long)
	 */
	@Override
	public ReturnOrderSkuInfoListVO getReturnOrderSkuVOList(long ordPkgId) {
		ReturnOrderSkuInfoListVO retVO = new ReturnOrderSkuInfoListVO();
		long userId = SecurityContextUtils.getUserId();
		OrderPackageSimpleDTO ordPkgDTO = ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
		if (null == ordPkgDTO) {
			return retVO;
		}
		long orderId = ordPkgDTO.getOrderId();
		OrderFormBriefDTO briefOrderForm = ordFacade.queryOrderFormBriefDTO(userId, orderId, null);
		if(null == briefOrderForm) {
			return retVO;
		}
		OrderFormPayMethod payMethod = briefOrderForm.getOrderFormPayMethod();
		// 1. 回传数据：_ReturnOrderSkuInfoListVO.returns
		PackageReturnJudgement prj = getPackageReturnJudgement(ordPkgDTO);
		ReturnOrderSkuInfoList retOrdSkuInfoList = extractRetOrdSkuInfo(ordPkgDTO, prj);
		retVO.setReturns(retOrdSkuInfoList);
		// 2. 回传数据：orderId + ordPkgId + payMethod + returnState
		retVO.setOrderId(String.valueOf(orderId));
		retVO.setOrdPkgId(String.valueOf(ordPkgId));
		retVO.setPayMethod(payMethod);
		retVO.setReturnState(ReturnPackageState.INIT);
		/** 如果已经提交申请，更新ReturnState */
		ReturnPackageDTO retPkgDTO = queryReturnPackageByOrderPackageId(ordPkgId);
		if(null != retPkgDTO) {
			retVO.setReturnState(retPkgDTO.getReturnState());
		}
		return retVO;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#getPackageReturnJudgement(com.xyl.mmall.order.dto.OrderPackageSimpleDTO)
	 */
	@Override
	public PackageReturnJudgement getPackageReturnJudgement(OrderPackageSimpleDTO ordPkgDTO) {
		long earliestPOEndTime = retPkgCommonFacade.getEarliestPOEndTime(ordPkgDTO);
		PackageReturnJudgement prj = ordPkgSimpleService.canUserReturnPackage(ordPkgDTO, earliestPOEndTime);
		return prj;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#returnAllOrdSku(com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam)
	 */
	@Override
	public ReturnPriceVO returnAllOrdSku(_FrontReturnApplyParam param) {
		return returnOrdSkuExec(param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#addRetOrdSkuDefault(com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam)
	 */
	@Override
	public ReturnPriceVO addRetOrdSkuDefault(_FrontReturnApplyParam param) {
		return returnOrdSkuExec(param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#increaseRetOrdSku(com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam)
	 */
	@Override
	public ReturnPriceVO increaseRetOrdSku(_FrontReturnApplyParam param) {
		return returnOrdSkuExec(param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#removeRetOrdSku(com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam)
	 */
	@Override
	public ReturnPriceVO removeRetOrdSku(_FrontReturnApplyParam param) {
		return returnOrdSkuExec(param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#decreaseRetOrdSku(com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam)
	 */
	@Override
	public ReturnPriceVO decreaseRetOrdSku(_FrontReturnApplyParam param) {
		return returnOrdSkuExec(param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#applyReturn(com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam)
	 */
	@Override
	public ReturnApplyVO applyReturn(_FrontReturnApplyParam param) {
		ReturnApplyVO retVO = new ReturnApplyVO();
		if (null == param) {
			return retVO;
		}
		long userId = SecurityContextUtils.getUserId();
		long ordPkgId = param.getOrdPkgId();
		OrderPackageSimpleDTO ordPkgDTO = ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
		if (null == ordPkgDTO) {
			return retVO;
		}
		// ! 数据入库：
		ReturnPackageApplyParam retApplyParam = convertApplyParam(ordPkgDTO, param);
		/**
		 * 前端会处理 ServiceException <- [retPkgUpdateService.applyReturn(...)]
		 */
		RetArg retArg = retPkgUpdateService.applyReturn(userId, ordPkgId, retApplyParam);
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		if(null == isSucc || Boolean.FALSE == isSucc) {
			return retVO;
		}
		ReturnPackageDTO retPkgDTO = RetArgUtil.get(retArg, ReturnPackageDTO.class);
		if (null == retPkgDTO) {
			return retVO;
		}
		return getApplyExec(retPkgDTO);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#getApply(long)
	 */
	@Override
	public ReturnApplyVO getApply(long ordPkgId) {
		ReturnApplyVO retVO = new ReturnApplyVO();
		ReturnPackageDTO retPkgDTO = this.queryReturnPackageByOrderPackageId(ordPkgId);
		if (null == retPkgDTO) {
			return retVO;
		}
		retVO = getApplyExec(retPkgDTO);
		return retVO;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#completeApplyWithExpInfo(com.xyl.mmall.mainsite.facade.param._FrontReturnExpInfoParam)
	 */
	@Override
	public ReturnExpInfoVO completeApplyWithExpInfo(_FrontReturnExpInfoParam param) {
		ReturnExpInfoVO ret = new ReturnExpInfoVO();
		if (null == param) {
			return ret;
		}
		ReturnPackageExpInfoParam expInfo = convertExpInfoParam(param);
		long userId = SecurityContextUtils.getUserId();
		/**
		 * 前端会处理 ServiceException <- [retPkgUpdateService.updateReturnExpInfo(...)]
		 */
		RetArg retArg = retPkgUpdateService.updateReturnExpInfo(userId, param.getRetPkgId(), expInfo);
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		if(null == isSucc || Boolean.TRUE != isSucc) {
			return ret;
		}
		ReturnPackageDTO retPkgDTO = RetArgUtil.get(retArg, ReturnPackageDTO.class);
		try {
			// to be continued: 尝试调用波爷的Facade，减少定时器任务数
			boolean jitSucc = jitSupplyManagerFacade.saveReturnOrderForm(retPkgDTO);
			if(!jitSucc) {
				logger.info("jitSupplyManagerFacade.saveReturnOrderForm(retPkgDTO) failed for userId:" + userId + ", retPkgId:" + param.getRetPkgId());
			} else {
				RetArg pushArg = retPkgUpdateService.updateJITPushToSuccessful(retPkgDTO);
				Boolean pushSucc = RetArgUtil.get(pushArg, Boolean.class);
				if(null == pushSucc || Boolean.TRUE == pushSucc) {
					logger.info("retPkgUpdateService.updateJITPushToSuccessful(retPkgDTO) failed for userId:" + userId + ", retPkgId:" + param.getRetPkgId());
				}
			}
		} catch (Exception e) {
			logger.warn("failed in trying jitSupplyManagerFacade.saveReturnOrderForm(retPkgDTO) or retPkgUpdateService.updateJITPushToSuccessful(retPkgDTO)", e);
		} 
		if (null == retPkgDTO) {
			return ret;
		}
		fillReturnExpInfoVO(ret, retPkgDTO);
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#getReturning(long)
	 */
	@Override
	public ReturnExpInfoVO getReturning(long ordPkgId) {
		ReturnExpInfoVO ret = new ReturnExpInfoVO();
		ReturnPackageDTO retPkgDTO = queryReturnPackageByOrderPackageId(ordPkgId);
		if (null == retPkgDTO) {
			logger.warn("null retPkgDTO in retPkgDTOList");
			return ret;
		}
		fillReturnExpInfoVO(ret, retPkgDTO);
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#returnStatus(long)
	 */
	@Override
	public ReturnStatusVO returnStatus(long ordPkgId) {
		ReturnStatusVO ret = new ReturnStatusVO();
		ReturnPackageDTO retPkgDTO = queryReturnPackageByOrderPackageId(ordPkgId);
		if(null == retPkgDTO) {
			return ret;
		}
		long orderId = retPkgDTO.getOrderId();
		long retPkgId = retPkgDTO.getRetPkgId();
		// 0. 回传数据：status(StatusInfo) + price(_ReturnPriceVOExtension)
		ReturnPackageState retState = retPkgDTO.getReturnState();
		if (CollectionUtil.isInArray(ReturnPackageState.stateArrayOfReturned(), retState)) {
			ret.setStatusInfo(new StatusInfo(StatusInfo.Status.SUCCESSFUL, retPkgDTO.getExtInfo()));
		} else if (CollectionUtil.isInArray(ReturnPackageState.stateArrayOfRefused(), retState)) {
			ret.setStatusInfo(new StatusInfo(StatusInfo.Status.FAILED, retPkgDTO.getExtInfo()));
		} else if (CollectionUtil.isInArray(ReturnPackageState.stateArrayOfCancelled(), retPkgDTO.getReturnState())){
			// http://jira6.hz.netease.com/browse/VSTORE-785
			ret.setStatusInfo(new StatusInfo(StatusInfo.Status.FAILED, "自下单日起" + ConstValueOfOrder.SEP_RP_APPLY_DAY
					+ "天内未收到退货包裹，自动取消退货申请"));
		} else {
			ret.setStatusInfo(new StatusInfo(StatusInfo.Status.RETURNING, retPkgDTO.getExtInfo()));
		}
		ReturnCODBankCardInfoDTO bankCard = retPkgQueryService.queryReturnCODBankCardInfo(retPkgDTO.getBankCardInfoId(), retPkgDTO.getUserId());
		String bankCardNO = null == bankCard ? "" : bankCard.getBankCardNO();
		List<ReturnCoupon> retCoupon = getOrderReturnCoupon(retPkgDTO);
		ret.getPrice().fillWithReturnPackage(retPkgDTO, false, bankCardNO, retCoupon, retPkgDTO.getReturnOperationTime(), retPkgDTO.getExtInfo());
		// 1. 回传数据：orderId + ordPkgId + retPkgId + returnState + payMethod
		ret.setOrderId(String.valueOf(orderId));
		ret.setOrdPkgId(String.valueOf(ordPkgId));
		ret.setRetPkgId(String.valueOf(retPkgId));
		ret.setReturnState(retPkgDTO.getReturnState());
		if(null != retPkgDTO.getOrdFormBriefDTO()) {
			ret.setPayMethod(retPkgDTO.getOrdFormBriefDTO().getOrderFormPayMethod());
		}
		// 2. 回传数据：returns(ReturnOrderSkuVOList)
		ReturnOrderSkuInfoList retOrdSkuInfoList = extractRetOrdSkuInfo(retPkgDTO);
		ret.setReturns(retOrdSkuInfoList);
		// 3. 回传数据 returnInfo(ReturnAddressVO)，等待其他服务对接
		ExpInfoVO expInfo = new ExpInfoVO();
		expInfo.fillWithReturnPackage(retPkgDTO);
		ret.setReturnExpress(expInfo);
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.mainsite.facade.ReturnPackageFacade#cancelReturn(long)
	 */
	@Override
	public boolean cancelReturn(long ordPkgId) {
		long userId = SecurityContextUtils.getUserId();
		/**
		 * 前端会处理 ServiceException <- [retPkgUpdateService.deprecateApply(...)]
		 */
		return retPkgUpdateService.deprecateApply(userId, ordPkgId);
	}

}
