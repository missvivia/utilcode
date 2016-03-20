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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.DateFormatUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.facade.OrderFinanceFacade;
import com.xyl.mmall.cms.vo.finance.FinanceOrderPackageVO;
import com.xyl.mmall.cms.vo.finance.FinanceOrderRefundPackageVO;
import com.xyl.mmall.cms.vo.finance.FinanceOrderRefundSkuVO;
import com.xyl.mmall.cms.vo.finance.FinanceOrderRefundVO;
import com.xyl.mmall.cms.vo.finance.FinanceOrderSkuVO;
import com.xyl.mmall.cms.vo.finance.FinanceOrderVO;
import com.xyl.mmall.cms.vo.finance.FinanceTradeVO;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.itemcenter.dto.ProductDTO;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageBriefDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.result.StCancelOrderSkuResult;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderMiscService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.meta.Promotion;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.meta.RedPacketOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.PromotionService;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.promotion.service.RedPacketService;
import com.xyl.mmall.promotion.utils.DateUtils;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * OrderFinanceFacadeImpl.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-12-2
 * @since 1.0
 */
@Service("orderFinanceFacade")
public class OrderFinanceFacadeImpl implements OrderFinanceFacade {

	@Autowired
	private OrderService orderService;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private CouponOrderService couponOrderService;

	@Autowired
	private CouponService couponservice;

	@Autowired
	private RedPacketOrderService redPacketOrderService;

	@Autowired
	private RedPacketService redPacketService;

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private OrderBriefService orderBriefService;

	@Autowired
	private OrderMiscService orderMiscService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private POProductService poProductService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderFinanceFacade#queryOrderList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FinanceOrderVO> queryOrderList(long[] timeRange) {
		// 1.判断是否有效时间戳
		if (timeRange == null || timeRange.length != 2)
			return Collections.emptyList();

		// 2.根据时间戳分页获取有效订单信息
		DDBParam param = new DDBParam();
		param.setLimit(100);
		param.setAsc(true);
		param.setOrderColumn("OrderId");
		long minOrderId = 0L;
		RetArg retArg = orderService.queryOrderFormListByType1WithMinOrderId(minOrderId, timeRange, param);
		if (retArg == null)
			return Collections.emptyList();

		// 3.OrderFormDTO -> FinanceOrderVO
		List<OrderFormDTO> orderFormList = RetArgUtil.get(retArg, ArrayList.class);
		List<FinanceOrderVO> finaceOrderList = new ArrayList<>();
		Map<Long, String> provinceCodeNameMap = locationService.getProvinceCodeNameMap();
		while (CollectionUtil.isNotEmptyOfList(orderFormList)) {
			for (OrderFormDTO dto : orderFormList) {
				// 3.1订单优惠信息
				String counponInfo = getYhInfo(dto);
				// 3.2订单关联的交易信息
				List<TradeItemDTO> treadeItemList = tradeService.getTradeItemDTOList(dto.getOrderId(), dto.getUserId());
				// 3.3dto->vo
				FinanceOrderVO financeOrderVO = new FinanceOrderVO(dto, provinceCodeNameMap, treadeItemList,
						counponInfo);
				// 3.4设置产品类目
				setCategoryInfo(financeOrderVO);
				// 3.5设置产品PO信息
				setPOInfo(financeOrderVO);
				finaceOrderList.add(financeOrderVO);
			}

			minOrderId = orderFormList.get(orderFormList.size() - 1).getOrderId();
			retArg = orderService.queryOrderFormListByType1WithMinOrderId(minOrderId, timeRange, param);
			if (retArg == null)
				break;
			orderFormList = RetArgUtil.get(retArg, ArrayList.class);
		}
		return finaceOrderList;
	}

	/**
	 * 获取订单优惠信息.
	 * 
	 * @param dto
	 * @return
	 */
	private String getYhInfo(OrderFormDTO dto) {
		StringBuilder yhInfo = new StringBuilder(256);
		// 1.优惠券信息
		CouponOrder couponOrder = couponOrderService
				.getCouponOrderByOrderIdOfUseType(dto.getUserId(), dto.getOrderId());
		if (couponOrder != null) {
			boolean isValidOnly = false;
			Coupon coupon = couponservice.getCouponByCode(couponOrder.getCouponCode(), isValidOnly);
			if (coupon != null)
				yhInfo.append("优惠券:" + coupon.getDescription() + ";");
		}
		// 2.活动信息
		Long promotionId = dto.getPromotionId();
		if (promotionId != null && promotionId != 0) {
			Promotion promotion = promotionService.getPromotionById(promotionId);
			if (promotion != null) {
				yhInfo.append("活动:" + promotion.getDescription() + ";");
			}
		}
		// 3.红包信息
		List<RedPacketOrder> redPacketOrderList = redPacketOrderService.getRedPacketOrderList(dto.getUserId(),
				dto.getOrderId());
		if (CollectionUtil.isNotEmptyOfList(redPacketOrderList)) {
			yhInfo.append("红包:");
			for (RedPacketOrder redPacketOrder : redPacketOrderList) {
				if (redPacketOrder.getRedPacketOrderType() != RedPacketOrderType.USE_RED_PACKET
						&& redPacketOrder.getRedPacketOrderType() != RedPacketOrderType.USE_RED_PACKET_FOR_EXPRESS)
					continue;
				if (redPacketOrder.getRedPacketHandlerType() != ActivationHandlerType.DEFAULT)
					continue;
				RedPacket redPacket = redPacketService.getRedPacketById(redPacketOrder.getRedPacketId());
				if (redPacket != null)
					yhInfo.append(redPacket.getDescription() + " ");
			}
			yhInfo.append(";");
		}
		return yhInfo.toString();
	}

	/**
	 * 设置订单下的产品类目.
	 * 
	 * @param financeOrderVO
	 */
	private void setCategoryInfo(FinanceOrderVO financeOrderVO) {
		Set<Long> productIdSet = financeOrderVO.getProductIdSet();
		Map<Long, String> productIdAndCategoryNameMap = new HashMap<Long, String>();
		for (Long productId : productIdSet) {
			// 1.根据产品id获取产品信息
			ProductDTO productDTO = poProductService.getProductDTO(productId);
			if (productDTO == null)
				continue;
			// 2.根据类目id获取类目信息
			long categoryId = productDTO.getLowCategoryId();
			Category category = categoryService.getCategoryById(categoryId);
			if (category == null)
				continue;
			// 3.设置产品id-类目名称Map
			String categoryName = category.getName();
			productIdAndCategoryNameMap.put(productId, categoryName);
		}

		List<FinanceOrderPackageVO> orderPackageVOList = financeOrderVO.getFinanceOrderPackageList();
		for (FinanceOrderPackageVO financeOrderPackageVO : orderPackageVOList) {
			List<FinanceOrderSkuVO> financeOrderSkuVOList = financeOrderPackageVO.getFinanceOrderSkuList();
			for (FinanceOrderSkuVO financeOrderSkuVO : financeOrderSkuVOList) {
				long productId = financeOrderSkuVO.getProductId();
				if (productIdAndCategoryNameMap.containsKey(productId))
					financeOrderSkuVO.setCategoryName(productIdAndCategoryNameMap.get(productId));
			}
		}
	}

	/**
	 * 设置订单下产品po信息.
	 * 
	 * @param financeOrderVO
	 */
	private void setPOInfo(FinanceOrderVO financeOrderVO) {
		Set<Long> poIdSet = financeOrderVO.getPoIdSet();
		Map<Long, Schedule> poIdAndScheduleMap = new HashMap<Long, Schedule>();
		for (Long poId : poIdSet) {
			PODTO poDTO = scheduleService.getScheduleById(poId);
			if (poDTO == null)
				continue;
			ScheduleDTO scheduleDTO = poDTO.getScheduleDTO();
			if (scheduleDTO == null)
				continue;
			Schedule schedule = scheduleDTO.getSchedule();
			if (schedule != null)
				poIdAndScheduleMap.put(poId, schedule);
		}

		List<FinanceOrderPackageVO> orderPackageVOList = financeOrderVO.getFinanceOrderPackageList();
		for (FinanceOrderPackageVO financeOrderPackageVO : orderPackageVOList) {
			List<FinanceOrderSkuVO> financeOrderSkuVOList = financeOrderPackageVO.getFinanceOrderSkuList();
			for (FinanceOrderSkuVO financeOrderSkuVO : financeOrderSkuVOList) {
				long poId = financeOrderSkuVO.getPoId();
				if (poIdAndScheduleMap.containsKey(poId)) {
					Schedule schedule = poIdAndScheduleMap.get(poId);
					String poStartDate = schedule.getStartTime() == 0 ? "" : DateFormatUtil.getFormatDateType5(schedule
							.getStartTime());
					String poEndDate = schedule.getEndTime() == 0 ? "" : DateFormatUtil.getFormatDateType5(schedule
							.getEndTime());
					financeOrderSkuVO.setPoStartDate(poStartDate);
					financeOrderSkuVO.setPoEndDate(poEndDate);
				}
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderFinanceFacade#queryTradeList(long[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FinanceTradeVO> queryTradeList(long[] timeRange) {
		// 1.判断是否有效时间戳
		if (timeRange == null || timeRange.length != 2)
			return Collections.emptyList();

		DDBParam param = new DDBParam();
		param.setLimit(100);
		param.setAsc(true);
		param.setOrderColumn("TradeId");
		long minTradeId = 0L;
		PayState[] payStateArray = { PayState.ONLINE_PAYED, PayState.COD_PAYED, PayState.ZERO_PAYED };
		RetArg retArg = tradeService.getTradeItemDTOListWithMinTradeId(minTradeId, null, payStateArray, timeRange,
				param);
		if (retArg == null)
			return Collections.emptyList();

		List<TradeItemDTO> tradeItemList = RetArgUtil.get(retArg, ArrayList.class);
		List<FinanceTradeVO> finaceTradeList = new ArrayList<>();
		while (CollectionUtil.isNotEmptyOfList(tradeItemList)) {
			for (TradeItemDTO tradeItem : tradeItemList) {
				FinanceTradeVO entity = new FinanceTradeVO(tradeItem);
				finaceTradeList.add(entity);
			}
			// 重新设置tradeId,获取交易数据
			minTradeId = tradeItemList.get(tradeItemList.size() - 1).getTradeId();
			retArg = tradeService.getTradeItemDTOListWithMinTradeId(minTradeId, null, payStateArray, timeRange, param);
			if (retArg == null)
				break;
			tradeItemList = RetArgUtil.get(retArg, ArrayList.class);
		}
		return finaceTradeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FinanceOrderRefundVO> queryOrderRefundList(long[] timeRange) {
		if (timeRange == null || timeRange.length != 2) {
			return Collections.emptyList();
		}

		DDBParam param = DDBParam.genParam500();
		param.setAsc(true);
		long minPackageId = 0L;

		RetArg retArg = orderBriefService.queryOrderPackageBriefDTOListByType1WithMinPackageId(minPackageId, timeRange,
				param);
		if (retArg == null) {
			return Collections.emptyList();
		}

		List<OrderPackageBriefDTO> list = RetArgUtil.get(retArg, ArrayList.class);
		List<FinanceOrderRefundVO> vos = new ArrayList<>();
		Map<Long, FinanceOrderRefundVO> map = new HashMap<>();
		while (!CollectionUtils.isEmpty(list)) {
			for (OrderPackageBriefDTO dto : list) {
				FinanceOrderRefundVO vo = null;
				if (map.containsKey(dto.getOrderId())) {
					continue;
				} else {
					vo = new FinanceOrderRefundVO();
					vo.setOrderId(dto.getOrderId());
					map.put(dto.getOrderId(), vo);
				}

				// vo.setRetailPrice(dto.ge)
				RetArg arg = orderMiscService.stCancelOrderSku(dto.getOrderId(), dto.getUserId(), null);
				List<StCancelOrderSkuResult> results = RetArgUtil.get(arg, ArrayList.class);
				if (CollectionUtils.isEmpty(results)) {
					continue;
				}

				// 按照packageid分组
				Map<Long, List<StCancelOrderSkuResult>> orderSkuMap = CollectionUtil.convertCollToListMap(results,
						"packageId");

				for (Entry<Long, List<StCancelOrderSkuResult>> entry : orderSkuMap.entrySet()) {
					List<StCancelOrderSkuResult> packageResults = entry.getValue();

					FinanceOrderRefundPackageVO packageVO = new FinanceOrderRefundPackageVO();
					packageVO.setPackageId(entry.getKey());

					if (CollectionUtils.isEmpty(packageResults)) {
						continue;
					}

					for (StCancelOrderSkuResult result : packageResults) {
						// 设置运费
						if (vo.getExpressRefundCash().compareTo(BigDecimal.ZERO) == 0) {
							vo.setExpressRefundCash(result.getRefundExp());
						}
						// 设置退款原因
						if (StringUtils.isBlank(packageVO.getRefundReason())) {
							packageVO.setRefundReason(result.getRefundReason());
						}

						// 设置退款渠道
						if (StringUtils.isBlank(packageVO.getRefundChannel())) {
							packageVO.setRefundChannel(result.getRefundType());
						}

						// 设置退款时间
						if (StringUtils.isBlank(packageVO.getRefundDate())) {
							packageVO.setRefundDate(DateUtils.parseLongToString(DateUtils.DATE_TIME_FORMAT,
									result.getCancelTime()));
						}

						FinanceOrderRefundSkuVO refundSkuVO = new FinanceOrderRefundSkuVO();
						Category category = poProductService.getCategoryBySkuId(result.getSkuId());
						refundSkuVO.setPoId(result.getPoId());
						if (category != null) {
							refundSkuVO.setCategoryName(category.getName());
						} else {
							refundSkuVO.setCategoryName("");
						}
						refundSkuVO.setProductName(result.getOrderSkuDTO().getSkuSPDTO().getProductName());
						refundSkuVO.setRefundCash(result.getOrderSkuDTO().getRprice()
								.multiply(new BigDecimal(result.getCount())));
						refundSkuVO.setRefundCount(result.getCount());
						refundSkuVO.setRetailPrice(result.getOrderSkuDTO().getOriRPrice());
						refundSkuVO.setSalePrice(result.getOrderSkuDTO().getRprice());

						if (StringUtils.isBlank(packageVO.getTradeSerial())) {
							TradeItemDTO itemDTO = tradeService.getOnlinePayTradeItemDTO(result.getOrderId(),
									result.getUserId());
							if (itemDTO != null && StringUtils.isNotBlank(itemDTO.getPayOrderSn())) {
								packageVO.setTradeSerial(String.valueOf(itemDTO.getTradeId()));
							} else {
								packageVO.setTradeSerial("");
							}
						}

						packageVO.getSkuVOs().add(refundSkuVO);
					}

					vo.getPackageVOs().add(packageVO);
				}

			}

			minPackageId = list.get(list.size() - 1).getPackageId();
			retArg = orderBriefService.queryOrderPackageBriefDTOListByType1WithMinPackageId(minPackageId, timeRange,
					param);
			list = RetArgUtil.get(retArg, ArrayList.class);
		}

		vos.addAll(map.values());

		return vos;
	}
}
