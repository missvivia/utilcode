/*
 * @(#) 2014-12-2
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.cms.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.facade.promotion.CouponFacade;
import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.backend.vo.PoOrderStatisticVo;
import com.xyl.mmall.backend.vo.PoReturnOrderVO;
import com.xyl.mmall.backend.vo.PoReturnSkuVO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.SupplierFinanceFacade;
import com.xyl.mmall.cms.vo.finance.FinanceFirstPayConfirmVO;
import com.xyl.mmall.cms.vo.finance.FinanceFirstPayOrderVO;
import com.xyl.mmall.cms.vo.finance.FinanceFullPayMetaVO;
import com.xyl.mmall.cms.vo.finance.FinanceInDetailMetaVO;
import com.xyl.mmall.cms.vo.finance.FinanceRefundDetailMetaVO;
import com.xyl.mmall.common.facade.POProductQueryFacade;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.service.PoReturnService;
import com.xyl.mmall.order.dto.OrderCartItemBriefDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPOInfoBriefDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.order.param.OrderSkuQueryParam1;
import com.xyl.mmall.order.result.StCancelOrderSkuResult;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderMiscService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.utils.DateUtils;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.enums.SupplyMode;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * SupplierFinanceFacadeImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-12-2
 * @since 1.0
 */
@Service("supplierFinanceFacade")
public class SupplierFinanceFacadeImpl implements SupplierFinanceFacade {

	@Autowired
	private OrderBriefService orderBriefService;

	@Autowired
	private OrderMiscService orderMiscService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ReturnPackageQueryService returnPackageQueryService;

	@Autowired
	private PoReturnService poReturnService;

	@Autowired
	private JITSupplyManagerFacade jitSupplyManagerFacade;

	@Autowired
	private POProductService poProductService;

	@Resource(name = "poProductQueryFacadeXHCacheImpl")
	private POProductQueryFacade poProductQueryFacadeXHCacheImpl;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private PromotionFacade promotionFacade;

	@Autowired
	private CouponOrderService couponOrderService;

	@Autowired
	private CouponFacade couponFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.SupplierFinanceFacade#getFirstPayConfirm(long)
	 */
	@Override
	public FinanceFirstPayConfirmVO getFirstPayConfirm(long poId) {
		// 1.获取po档期信息
		PODTO poDTO = scheduleService.getScheduleById(poId);
		if (poDTO == null)
			return null;
		ScheduleDTO scheduleDTO = poDTO.getScheduleDTO();
		if (scheduleDTO == null)
			return null;
		// 2.Schedule->FinanceFirstPayConfirmVO
		FinanceFirstPayConfirmVO vo = new FinanceFirstPayConfirmVO(scheduleDTO);
		setFirstPayPriceInfo(poId, vo);
		return vo;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.SupplierFinanceFacade#queryFinanceFirstPayOrderVOList(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FinanceFirstPayOrderVO> queryFinanceFirstPayOrderVOList(long poId) {
		FinanceFirstPayConfirmVO financeFirstPayConfirmVO = getFirstPayConfirm(poId);
		if (financeFirstPayConfirmVO == null)
			return Collections.emptyList();

		DDBParam param = new DDBParam();
		param.setLimit(100);
		param.setAsc(true);
		param.setOrderColumn("id");
		long minOrderPOInfoId = 0L;
		RetArg retArg = orderBriefService.queryOrderPOInfoBriefDTOList(minOrderPOInfoId, poId, param);
		if (retArg == null)
			return Collections.emptyList();

		List<OrderPOInfoBriefDTO> orderPOInfoBriefDTOList = RetArgUtil.get(retArg, ArrayList.class);
		List<FinanceFirstPayOrderVO> financeFirstPayOrderVOList = new ArrayList<FinanceFirstPayOrderVO>();
		while (CollectionUtil.isNotEmptyOfList(orderPOInfoBriefDTOList)) {
			// 1.根据poId获取有效订单销售数据
			Map<Long, List<OrderSkuDTO>> orderSkuDTOMap = getOrderSkuDTOMapInPo(poId, orderPOInfoBriefDTOList);
			for (Entry<Long, List<OrderSkuDTO>> entry : orderSkuDTOMap.entrySet()) {
				List<OrderSkuDTO> orderSkuDTOList = entry.getValue();
				for (OrderSkuDTO orderSkuDTO : orderSkuDTOList) {
					// 2.去除已经取消或者退款的orderSku
					Map<Long, StCancelOrderSkuResult> stCancelOrderSkuMap = getStCancelOrderSkuMap(orderSkuDTO, poId);
					if (stCancelOrderSkuMap.containsKey(orderSkuDTO.getSkuId())) {
						StCancelOrderSkuResult stCancelOrderSkuResult = stCancelOrderSkuMap.get(orderSkuDTO.getSkuId());
						int totalCount = orderSkuDTO.getTotalCount() - stCancelOrderSkuResult.getCount();
						orderSkuDTO.setTotalCount(totalCount);
						if (totalCount == 0)
							continue;
					}
					// 3.条形码+货号
					POSkuDTO poSkuDTO = poProductService.getPOSkuDTO(orderSkuDTO.getSkuId());
					// 4.获取首付款销售明细
					FinanceFirstPayOrderVO financeFirstPayOrderVO = new FinanceFirstPayOrderVO(orderSkuDTO,
							financeFirstPayConfirmVO, poSkuDTO);
					financeFirstPayOrderVOList.add(financeFirstPayOrderVO);
				}
			}

			// 3.获取下一批数据再加总
			minOrderPOInfoId = orderPOInfoBriefDTOList.get(orderPOInfoBriefDTOList.size() - 1).getId();
			retArg = orderBriefService.queryOrderPOInfoBriefDTOList(minOrderPOInfoId, poId, param);
			if (retArg == null)
				break;
			orderPOInfoBriefDTOList = RetArgUtil.get(retArg, ArrayList.class);
		}

		return financeFirstPayOrderVOList;
	}

	/**
	 * 设置商品销售总额和首付款.
	 * 
	 * @param poId
	 * @param vo
	 */
	@SuppressWarnings("unchecked")
	private void setFirstPayPriceInfo(long poId, FinanceFirstPayConfirmVO vo) {
		DDBParam param = new DDBParam();
		param.setLimit(100);
		param.setAsc(true);
		param.setOrderColumn("id");
		long minOrderPOInfoId = 0L;
		RetArg retArg = orderBriefService.queryOrderPOInfoBriefDTOList(minOrderPOInfoId, poId, param);
		if (retArg == null)
			return;

		BigDecimal totalOriRPrice = BigDecimal.ZERO;
		List<OrderPOInfoBriefDTO> orderPOInfoBriefDTOList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfList(orderPOInfoBriefDTOList)) {
			// 1.根据poId获取有效订单销售数据
			Map<Long, List<OrderSkuDTO>> orderSkuDTOMap = getOrderSkuDTOMapInPo(poId, orderPOInfoBriefDTOList);
			// 2.计算销售总额
			for (Entry<Long, List<OrderSkuDTO>> entry : orderSkuDTOMap.entrySet()) {
				List<OrderSkuDTO> orderSkuDTOList = entry.getValue();
				for (OrderSkuDTO orderSkuDTO : orderSkuDTOList) {
					// 2.1去除已经取消或者退款的orderSku
					Map<Long, StCancelOrderSkuResult> stCancelOrderSkuMap = getStCancelOrderSkuMap(orderSkuDTO, poId);
					if (stCancelOrderSkuMap.containsKey(orderSkuDTO.getSkuId())) {
						StCancelOrderSkuResult stCancelOrderSkuResult = stCancelOrderSkuMap.get(orderSkuDTO.getSkuId());
						int totalCount = orderSkuDTO.getTotalCount() - stCancelOrderSkuResult.getCount();
						orderSkuDTO.setTotalCount(totalCount);
						if (totalCount == 0)
							continue;
					}
					// 2.2累加销售总额
					BigDecimal totalOriRPriceOfOrderSku = orderSkuDTO.getOriRPrice().multiply(
							BigDecimal.valueOf(orderSkuDTO.getTotalCount()));
					totalOriRPrice = totalOriRPrice.add(totalOriRPriceOfOrderSku);
				}
			}
			// 3.获取下一批数据再加总
			minOrderPOInfoId = orderPOInfoBriefDTOList.get(orderPOInfoBriefDTOList.size() - 1).getId();
			retArg = orderBriefService.queryOrderPOInfoBriefDTOList(minOrderPOInfoId, poId, param);
			if (retArg == null)
				break;
			orderPOInfoBriefDTOList = RetArgUtil.get(retArg, ArrayList.class);
		}
		BigDecimal platformSrvFeeRate = vo.getPlatformSrvFeeRate();
		// 首付款=sum(商品销量*商品零售价)*0.5*(1-平台技术服务费)
		BigDecimal firstPayPrice = totalOriRPrice.multiply(BigDecimal.valueOf(0.5)).multiply(
				BigDecimal.valueOf(1).subtract(platformSrvFeeRate.divide(BigDecimal.valueOf(100))));
		vo.setFirstPayPrice(firstPayPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
		vo.setTotalOriRPrice(totalOriRPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
	}

	/**
	 * 获取订单里取消的OrderSku.
	 * 
	 * @param orderSkuDTO
	 * @param poId
	 * @return key=skuId,value=StCancelOrderSkuResult
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, StCancelOrderSkuResult> getStCancelOrderSkuMap(OrderSkuDTO orderSkuDTO, long poId) {
		RetArg retArg = orderMiscService.stCancelOrderSku(orderSkuDTO.getOrderId(), orderSkuDTO.getUserId(), poId);
		if (retArg == null)
			return Collections.emptyMap();
		List<StCancelOrderSkuResult> resultList = RetArgUtil.get(retArg, ArrayList.class);
		if (CollectionUtil.isEmptyOfList(resultList))
			return Collections.emptyMap();
		Map<Long, StCancelOrderSkuResult> resultMap = new HashMap<Long, StCancelOrderSkuResult>();
		for (StCancelOrderSkuResult result : resultList) {
			resultMap.put(result.getSkuId(), result);
		}
		return resultMap;
	}

	/**
	 * 根据poId获取有效订单销售数据.
	 * 
	 * @param poId
	 * @param orderPOInfoBriefDTOList
	 * @return
	 */
	private Map<Long, List<OrderSkuDTO>> getOrderSkuDTOMapInPo(long poId,
			List<OrderPOInfoBriefDTO> orderPOInfoBriefDTOList) {
		List<OrderSkuQueryParam1> orderSkuQueryParam1List = new ArrayList<>();
		for (OrderPOInfoBriefDTO orderPOInfoBriefDTO : orderPOInfoBriefDTOList) {
			// 1.查询订单详情
			long userId = orderPOInfoBriefDTO.getUserId();
			long orderId = orderPOInfoBriefDTO.getOrderId();
			Boolean isVisible = null;
			OrderFormDTO orderFormDTO = orderService.queryOrderForm(userId, orderId, isVisible);
			// 2.获取有效订单
			OrderFormState[] orderFormStateArray = OrderFormState.getMaybePayedArray();
			PayState[] payStateArray = new PayState[] { PayState.ONLINE_PAYED, PayState.COD_PAYED,
					PayState.COD_NOT_PAY, PayState.ZERO_PAYED };
			boolean isSuccessOrder = CollectionUtil.isInArray(orderFormStateArray, orderFormDTO.getOrderFormState())
					&& CollectionUtil.isInArray(payStateArray, orderFormDTO.getPayState());
			if (!isSuccessOrder)
				continue;
			OrderSkuQueryParam1 orderSkuQueryParam1 = new OrderSkuQueryParam1();
			orderSkuQueryParam1.setOrderId(orderPOInfoBriefDTO.getOrderId());
			orderSkuQueryParam1.setUserId(orderPOInfoBriefDTO.getUserId());
			orderSkuQueryParam1List.add(orderSkuQueryParam1);
		}
		// 3.过滤非poId的记录
		return orderMiscService.getOrderSkuDTOListMap(poId, orderSkuQueryParam1List);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RetArg getFullPayMetaVOList(long poId) {
		RetArg returnArg = new RetArg();
		// 来货数
		RetArg inArg = getInDetails(poId);

		// 销售明细
		RetArg discountArg = getFullPayDetail(poId);
		// 没有来货明细，不计算优惠
		List<FinanceInDetailMetaVO> vos = RetArgUtil.get(inArg, ArrayList.class);
		if (CollectionUtils.isEmpty(vos)) {
			return null;
		}
		List<OrderSkuDTO> dtos = RetArgUtil.get(discountArg, ArrayList.class);
		PODTO podto = RetArgUtil.get(inArg, PODTO.class);

		FinanceFullPayMetaVO metaVO = new FinanceFullPayMetaVO();
		for (FinanceInDetailMetaVO vo : vos) {
			if (metaVO.getPoId() <= 0) {
				metaVO.setPoId(vo.getPoId());
			}

			if (StringUtils.isBlank(metaVO.getOffLineTime())) {
				metaVO.setOffLineTime(vo.getOfflineDate());
			}

			if (StringUtils.isBlank(metaVO.getBrandName())) {
				metaVO.setBrandName(vo.getBrandName());
			}

			if (StringUtils.isBlank(metaVO.getWarehouseName())) {
				metaVO.setWarehouseName(vo.getWarehouse());
			}

			// 设置采购金额
			metaVO.setPurchaseAmount(metaVO
					.getPurchaseAmount()
					.add(vo.getRetailPrice().multiply(
							new BigDecimal(vo.getArrivedCount()).subtract(new BigDecimal(vo.getRefundCount()
									+ vo.getNotNormalCount())))).setScale(2, BigDecimal.ROUND_HALF_UP));

			metaVO.setInCount(metaVO.getInCount() + vo.getArrivedCount());
			metaVO.setRefundCount(metaVO.getRefundCount() + vo.getRefundCount() + vo.getNotNormalCount());
			metaVO.setActualCount(metaVO.getInCount() - metaVO.getRefundCount());
		}

		BigDecimal platSrvFreeRate = podto.getScheduleDTO().getScheduleVice().getPlatformSrvFeeRate() == null ? BigDecimal.ZERO
				: podto.getScheduleDTO().getScheduleVice().getPlatformSrvFeeRate();
		platSrvFreeRate = new BigDecimal(100).subtract(platSrvFreeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
		if (metaVO.getShareRatio() == null || metaVO.getShareRatio().compareTo(BigDecimal.ZERO) == 0) {
			metaVO.setShareRatio(platSrvFreeRate);
		}

		if (!CollectionUtils.isEmpty(dtos)) {
			for (OrderSkuDTO dto : dtos) {
				metaVO.setPromotionDiscountAmount(metaVO
						.getPromotionDiscountAmount()
						.add(dto.getHdSPrice().multiply(new BigDecimal(dto.getTotalCount())).multiply(platSrvFreeRate)
								.divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP));
//				metaVO.setCouponDiscountAmount(metaVO.getCouponDiscountAmount()
//						.add(dto.getCouponSPrice().multiply(new BigDecimal(dto.getTotalCount())))
//						.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
		}

		metaVO.setRealPayAmount(metaVO.getPurchaseAmount().multiply(platSrvFreeRate.divide(new BigDecimal(100)))
				.subtract(metaVO.getPromotionDiscountAmount()).subtract(metaVO.getCouponDiscountAmount())
				.setScale(2, BigDecimal.ROUND_HALF_UP));
		RetArgUtil.put(returnArg, RetArgUtil.get(inArg, PODTO.class));
		RetArgUtil.put(returnArg, metaVO);
		return returnArg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RetArg getFullPayDetail(long poId) {
		RetArg ret = new RetArg();
		putScheduleToRetArg(poId, ret);

		long minId = 0;
		DDBParam param = DDBParam.genParam500();
		param.setAsc(true);
		param.setOrderColumn("Id");

		RetArg retArg = orderBriefService.queryOrderPOInfoBriefDTOList(minId, poId, param);
		List<OrderPOInfoBriefDTO> briefDTOs = RetArgUtil.get(retArg, ArrayList.class);
		if (CollectionUtils.isEmpty(briefDTOs)) {
			RetArgUtil.put(ret, Collections.EMPTY_LIST);
			return ret;
		}

		List<OrderSkuDTO> skuList = new ArrayList<>();
		while (!CollectionUtils.isEmpty(briefDTOs)) {
			// orderid列表
			Map<Long, OrderPOInfoBriefDTO> tmpMap = new HashMap<Long, OrderPOInfoBriefDTO>();
			Iterator<OrderPOInfoBriefDTO> iterator = briefDTOs.iterator();
			while (iterator.hasNext()) {
				minId = briefDTOs.get(briefDTOs.size() - 1).getId();
				OrderPOInfoBriefDTO dto = iterator.next();
				// 包含了orderid的退出 不重复处理
				if (tmpMap.containsKey(dto.getOrderId())) {
					continue;
				}

				tmpMap.put(dto.getOrderId(), dto);

				OrderFormState state = dto.getOrderBDTO().getOrderFormState();
				if (state == OrderFormState.WAITING_PAY || state == OrderFormState.WAITING_COD_AUDIT
						|| state == OrderFormState.CANCEL_ING || state == OrderFormState.CANCEL_ED
						|| state == OrderFormState.COD_AUDIT_REFUSE) {
					iterator.remove();
					continue;
				}
				OrderFormDTO formDTO = orderService.queryOrderFormByOrderId(dto.getOrderId());
				List<OrderPackageDTO> packageDTOs = formDTO.getOrderPackageDTOList();
				if (CollectionUtils.isEmpty(packageDTOs)) {
					continue;
				}

				Map<Long, List<OrderSkuDTO>> orderSkuDTOMap = CollectionUtil.convertCollToListMap(
						formDTO.getAllOrderSkuDTOList(), "packageId");

				Iterator<OrderPackageDTO> iterator2 = packageDTOs.iterator();
				while (iterator2.hasNext()) {
					OrderPackageDTO packageDTO = iterator2.next();
					OrderPackageState orderPackageState = packageDTO.getOrderPackageState();
					if (OrderPackageState.isCancel(orderPackageState)) {
						orderSkuDTOMap.remove(packageDTO.getPackageId());
						continue;
					}
				}

				// 所有包裹都删除
				if (CollectionUtils.isEmpty(packageDTOs)) {
					iterator.remove();
					continue;
				}

				if (CollectionUtils.isEmpty(orderSkuDTOMap)) {
					continue;
				}

				List<OrderSkuDTO> packageSkuList = new ArrayList<>();

				// 添加到包裹列表中
				for (Entry<Long, List<OrderSkuDTO>> entry : orderSkuDTOMap.entrySet()) {
					packageSkuList.addAll(entry.getValue());
				}

				if (CollectionUtils.isEmpty(packageSkuList)) {
					continue;
				}

				List<ReturnPackageDTO> dtos = returnPackageQueryService.queryReturnPackageByUserIdAndOrderIdWithState(
						formDTO.getUserId(), formDTO.getOrderId(), false,
						new ReturnPackageState[] { ReturnPackageState.FINISH_RETURN,
								ReturnPackageState.FINALLY_RETURNED_TO_USER, ReturnPackageState.CW_WAITING_RETURN,
								ReturnPackageState.FINALLY_COD_RETURNED_TO_USER }, null);

				// 没有退款的 直接退出
				if (CollectionUtils.isEmpty(dtos)) {
					skuList.addAll(packageSkuList);
					continue;
				}

				// 将退款的部分剔除掉
				for (ReturnPackageDTO returnPackageDTO : dtos) {
					Map<Long, ReturnOrderSkuDTO> map = returnPackageDTO.getRetOrdSkuMap();
					Iterator<OrderSkuDTO> iterator3 = packageSkuList.iterator();
					while (iterator3.hasNext()) {
						OrderSkuDTO skuDTO = iterator3.next();
						if (skuDTO.getPoId() != poId) {
							iterator3.remove();
							continue;
						}
						if (map.containsKey(skuDTO.getId())) {
							ReturnOrderSkuDTO returnOrderSkuDTO = map.get(skuDTO.getId());
							int count = skuDTO.getTotalCount() - returnOrderSkuDTO.getConfirmCount();
							if (count <= 0) {
								iterator3.remove();
								continue;
							}
							skuDTO.setTotalCount(count);
						}
					}
				}

				if (CollectionUtils.isEmpty(packageSkuList)) {
					continue;
				}

				skuList.addAll(packageSkuList);
			}
			retArg = orderBriefService.queryOrderPOInfoBriefDTOList(minId, poId, param);
			briefDTOs = RetArgUtil.get(retArg, ArrayList.class);
			if (CollectionUtils.isEmpty(briefDTOs)) {
				RetArgUtil.put(ret, Collections.EMPTY_LIST);
				break;
			}
			iterator = briefDTOs.iterator();
		}

		if (!CollectionUtils.isEmpty(skuList)) {
			Iterator<OrderSkuDTO> it = skuList.iterator();
			while (it.hasNext()) {
				OrderSkuDTO dto = it.next();
				if (dto.getPoId() != poId) {
					it.remove();
				}
			}
		}

		Map<String, String> descriptionMap = buildDiscriptionMap(skuList);

		RetArgUtil.put(ret, descriptionMap);
		RetArgUtil.put(ret, skuList);
		return ret;
	}

	private Map<String, String> buildDiscriptionMap(List<OrderSkuDTO> skuList) {
		if (CollectionUtils.isEmpty(skuList)) {
			return null;
		}

		Map<Long, String> couponDes = buildCouponDesMap(skuList);

		Map<String, String> descriptionMap = new HashMap<>();

		// 根据orderid分组
		Map<Long, List<OrderSkuDTO>> orderSkuDTOMap = CollectionUtil.convertCollToListMap(skuList, "orderId");

		for (Entry<Long, List<OrderSkuDTO>> entry : orderSkuDTOMap.entrySet()) {
			List<OrderSkuDTO> values = entry.getValue();
			if (CollectionUtils.isEmpty(values)) {
				continue;
			}
			List<OrderCartItemBriefDTO> briefDTOs = orderBriefService.queryOrderCartItemBriefDTOList(values.get(0)
					.getUserId(), entry.getKey());
			for (OrderSkuDTO dto : values) {
				for (OrderCartItemBriefDTO briefDTO : briefDTOs) {
					String key = dto.getOrderId() + "-" + dto.getPackageId();
					if (descriptionMap.containsKey(key)) {
						continue;
					}
					if (briefDTO.getPromotionId() == null || briefDTO.getPromotionId() <= -1) {
						continue;
					}
					PromotionDTO promotionDTO = promotionFacade.getPromotionById(briefDTO.getPromotionId());
					if (promotionDTO == null) {
						continue;
					}

					String description = "活动：" + promotionDTO.getDescription();
					if (couponDes.containsKey(dto.getOrderId())) {
						description = description.concat("\r\n").concat(couponDes.get(dto.getOrderId()));
					}
					descriptionMap.put(key, description);
				}
			}
		}
		return descriptionMap;
	}

	private Map<Long, String> buildCouponDesMap(List<OrderSkuDTO> skuList) {
		Map<Long, String> couponDes = new HashMap<>();
		if (CollectionUtils.isEmpty(skuList)) {
			return couponDes;
		}

		for (OrderSkuDTO dto : skuList) {
			if (couponDes.containsKey(dto.getOrderId())) {
				continue;
			}
			CouponOrder couponOrder = couponOrderService.getCouponOrderByOrderIdOfUseType(dto.getUserId(),
					dto.getOrderId());
			if (couponOrder == null) {
				continue;
			}
			CouponDTO couponDTO = couponFacade.getCouponByCode(couponOrder.getCouponCode(), false);
			if (couponDTO == null) {
				continue;
			}

			String desc = "优惠券：" + couponDTO.getDescription();
			couponDes.put(dto.getOrderId(), desc);
		}
		return couponDes;
	}

	@Override
	public RetArg getInDetails(long poId) {
		RetArg retArg = new RetArg();

		putScheduleToRetArg(poId, retArg);

		List<PoOrderStatisticVo> list = jitSupplyManagerFacade.getPoOrderStatistic(poId);

		if (CollectionUtils.isEmpty(list)) {
			RetArgUtil.put(retArg, Collections.EMPTY_LIST);
			return retArg;
		}

		Schedule schedule = RetArgUtil.get(retArg, Schedule.class);
		PODTO podto = RetArgUtil.get(retArg, PODTO.class);

		List<FinanceInDetailMetaVO> vos = new ArrayList<>();

		for (PoOrderStatisticVo vo : list) {
			FinanceInDetailMetaVO detailMetaVO = new FinanceInDetailMetaVO();

			BusinessDTO businessDTO = businessFacade.getBusinessById(vo.getSupplierId());

			detailMetaVO.setPoId(poId);
			detailMetaVO.setOnlineDate(DateUtils.parseLongToString(DateUtils.DATE_FORMAT, schedule.getStartTime()));
			detailMetaVO.setOfflineDate(DateUtils.parseLongToString(DateUtils.DATE_FORMAT, schedule.getEndTime()));
			detailMetaVO.setSupplierId(vo.getSupplierId());
			detailMetaVO.setCompanyName(businessDTO.getCompanyName());
			detailMetaVO.setBrandName(schedule.getBrandName());
			if (vo.getWarehouseForm() != null) {
				detailMetaVO.setWarehouse(vo.getWarehouseForm().getWarehouseName());
			} else {
				detailMetaVO.setWarehouse("");
			}
			POSkuDTO poSkuDTO = poProductService.getPOSkuDTO(vo.getSkuId());
			if (poSkuDTO == null) {
				continue;
			}
			detailMetaVO.setMarketPrice(poSkuDTO.getMarketPrice());
			detailMetaVO.setRetailPrice(poSkuDTO.getSalePrice());
			detailMetaVO.setBarcode(poSkuDTO.getBarCode());
			detailMetaVO.setGoodsNo(poSkuDTO.getGoodsNo());
			detailMetaVO.setGoodsName(poSkuDTO.getProductName());
			detailMetaVO.setPoFollowerUserName(podto.getScheduleDTO().getScheduleVice().getPoFollowerUserName());
			if (podto.getScheduleDTO().getScheduleVice().getSupplyMode() == SupplyMode.SELF) {
				detailMetaVO.setReceivableCount(poSkuDTO.getSkuNum());
			} else {
				if (vo.getSupplierId() == schedule.getSupplierId()) {
					// self
					detailMetaVO.setReceivableCount(poSkuDTO.getSkuNum());
				} else {
					// together
					detailMetaVO.setReceivableCount(poSkuDTO.getSupplierSkuNum());
				}
			}

			detailMetaVO.setArrivedCount(vo.getArrivedQuantity());
			// 残次品
			detailMetaVO.setNotNormalCount(vo.getArrivedDefectiveCount());
			detailMetaVO.setRefundCount(vo.getArrivedRefundCount());
			detailMetaVO.setRealReceiveCount(detailMetaVO.getArrivedCount() - detailMetaVO.getNotNormalCount());

			vos.add(detailMetaVO);
		}

		RetArgUtil.put(retArg, vos);
		return retArg;
	}

	@Override
	public RetArg getRefundDetails(long poId) {
		RetArg retArg = new RetArg();

		putScheduleToRetArg(poId, retArg);

		Schedule schedule = RetArgUtil.get(retArg, Schedule.class);
		PODTO podto = RetArgUtil.get(retArg, PODTO.class);
		String companyName = RetArgUtil.get(retArg, String.class);

		List<PoReturnOrderVO> orderVOs = jitSupplyManagerFacade.getReturnVoByPoOrderId(String.valueOf(poId));
		if (CollectionUtils.isEmpty(orderVOs)) {
			RetArgUtil.put(retArg, Collections.EMPTY_LIST);
			return retArg;
		}

		List<FinanceRefundDetailMetaVO> list = new ArrayList<>();

		Iterator<PoReturnOrderVO> iterator = orderVOs.iterator();
		while (iterator.hasNext()) {
			PoReturnOrderVO dto = iterator.next();
			if (PoReturnOrderState.RECEIPTED != dto.getState()) {
				iterator.remove();
				continue;
			}
			List<PoReturnSkuVO> formSkuDTOs = dto.getSkuDetails();
			if (CollectionUtils.isEmpty(formSkuDTOs)) {
				continue;
			}

			for (PoReturnSkuVO skuVO : formSkuDTOs) {
				FinanceRefundDetailMetaVO vo = new FinanceRefundDetailMetaVO();
				vo.setPoId(poId);
				vo.setOnlineDate(DateUtils.parseLongToString(DateUtils.DATE_FORMAT, schedule.getStartTime()));
				vo.setSupplierNo(skuVO.getSupplierId());
				vo.setCompanyName(companyName);
				vo.setBrandName(schedule.getBrandName());
				vo.setWarehouse(dto.getWarehouseName());
				vo.setRefundType(skuVO.getType().getDesc());
				vo.setRefundCount(skuVO.getCount());
				POSkuDTO poSkuDTO = poProductService.getPOSkuDTO(skuVO.getSkuId());
				vo.setMarketPrice(poSkuDTO.getMarketPrice());
				vo.setRetailPrice(poSkuDTO.getSalePrice());
				vo.setBarcode(poSkuDTO.getBarCode());
				vo.setGoodsNo(poSkuDTO.getGoodsNo());
				vo.setGoodsName(poSkuDTO.getProductName());
				vo.setAssistant(podto.getScheduleDTO().getScheduleVice().getPoFollowerUserName());
				vo.setSignRefundDate(DateUtils.parseLongToString(DateUtils.DATE_TIME_FORMAT, skuVO.getShipTime()));
				list.add(vo);
			}
		}

		RetArgUtil.put(retArg, list);

		return retArg;
	}

	private void putScheduleToRetArg(long poId, RetArg ret) {
		PODTO podto = scheduleService.getScheduleById(poId);
		if (podto == null) {
			return;
		}

		// 获取档期
		Schedule schedule = podto.getScheduleDTO().getSchedule();
		BusinessDTO businessDTO = businessFacade.getBusinessById(schedule.getSupplierId());
		RetArgUtil.put(ret, schedule);
		RetArgUtil.put(ret, podto);
		RetArgUtil.put(ret, businessDTO.getCompanyName());
	}
}
