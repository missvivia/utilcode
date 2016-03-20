package com.xyl.mmall.mobile.ios.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.constant.CalendarConst;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.enums.OrderQueryType;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.util.UrlBaseUtil;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.mobile.ios.facade.IosOrderFacade;
import com.xyl.mmall.mobile.ios.facade.pageView.orderList.IosOrderForm;
import com.xyl.mmall.mobile.ios.facade.pageView.orderList.IosOrderSku;
import com.xyl.mmall.mobile.ios.facade.pageView.orderList.KeyOfOrder;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;

@Facade("iosOrderFacade")
public class IosOrderFacadeImpl implements IosOrderFacade {

	@Autowired
	private OrderService orderService;
	@Autowired
	private BusinessService businessService;
	@Autowired
	private TradeService tradeService;
	@Autowired
	private CouponOrderService couponOrderService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private ProductService productService;

	@SuppressWarnings("unchecked")
	public RetArg queryNewOrderList(OrderSearchParam orderSearchParam) {
//		int limit = orderSearchParam.getLimit();
//		int offset = orderSearchParam.getOffset();

		RetArg retArg = orderService.queryOrderFormListByOrderSearchParam(orderSearchParam);

		orderSearchParam = RetArgUtil.get(retArg, OrderSearchParam.class);
//		orderSearchParam.setLimit(limit);
//		orderSearchParam.setOffset(offset);

		List<OrderFormDTO> orderFormDTOs = RetArgUtil.get(retArg, ArrayList.class);
		if (orderFormDTOs == null) {
			orderSearchParam.setTotalCount(0);
			return retArg;
		}

//		Map<Integer, OrderQueryType> hashMap = new HashMap<>();
//		hashMap.put(0, OrderQueryType.ALL);
//		hashMap.put(1, OrderQueryType.WAITING_PAY);
//		hashMap.put(2, OrderQueryType.WAITING_DELIVE);
//		hashMap.put(3, OrderQueryType.ALREADY_DELIVE);
//		long userId = orderSearchParam.getUserId();
//		int count = 0;
//		switch (orderSearchParam.getQueryType()) {
//		case 0:
//			count = orderService.queryAllOrderFormListCount(userId, true, null);
//			break;
//		default:
//			count = orderService.queryOrderFormListCountByState2(userId, true,
//					hashMap.get(orderSearchParam.getQueryType()).getOrderStateArray(),
//					genOrderTimeRangeByOrderQueryType(hashMap.get(orderSearchParam.getQueryType()), null), null);
//			break;
//		}

		Map<KeyOfOrder, List<IosOrderForm>> map = convertToOrderFormListVO(orderFormDTOs);
		List<KeyOfOrder> keyOfOrders = new ArrayList<>();
		for (Entry<KeyOfOrder, List<IosOrderForm>> e : map.entrySet()) {
			KeyOfOrder keyOfOrder = e.getKey();
			keyOfOrder.setSubOrder(e.getValue());
			//在订单列表中添加起批数量
			for(IosOrderForm iosOrderForm:e.getValue()){
				for(IosOrderSku iosOrderSku:iosOrderForm.getOrderSkuList()){
					List<ProductPriceDTO>  productPriceDTOs = productService.getProductPriceDTOByProductId(iosOrderSku.getSkuId());
					if(!CollectionUtils.isEmpty(productPriceDTOs)){
						iosOrderSku.setPriceList(productPriceDTOs);
					}
				}
			}
			
			
			keyOfOrders.add(keyOfOrder);
		}

		RetArgUtil.put(retArg, keyOfOrders);
//		orderSearchParam.setTotalCount(count);
		RetArgUtil.put(retArg, orderSearchParam);
		return retArg;
	}
	
	
	public int queryNewOrderCount(OrderSearchParam orderSearchParam) {

		Map<Integer, OrderQueryType> hashMap = new HashMap<>();
		hashMap.put(0, OrderQueryType.ALL);
		hashMap.put(1, OrderQueryType.WAITING_PAY);
		hashMap.put(2, OrderQueryType.WAITING_DELIVE);
		hashMap.put(3, OrderQueryType.ALREADY_DELIVE);
		long userId = orderSearchParam.getUserId();
		int count = 0;
		switch (orderSearchParam.getQueryType()) {
		case 0:
			count = orderService.queryAllOrderFormListCount(userId, true, null);
			break;
		default:
			count = orderService.queryOrderFormListCountByState2(userId, true,
					hashMap.get(orderSearchParam.getQueryType()).getOrderStateArray(),
					genOrderTimeRangeByOrderQueryType(hashMap.get(orderSearchParam.getQueryType()), null), null);
			break;
		}

		return count;
	}

	private Map<KeyOfOrder, List<IosOrderForm>> convertToOrderFormListVO(List<OrderFormDTO> orderFormDTOs) {
		if (orderFormDTOs == null) {
			return null;
		}
		Map<KeyOfOrder, List<IosOrderForm>> map = new LinkedHashMap<>();
		long businessId = 0l;
		for (OrderFormDTO orderFormDTO : orderFormDTOs) {
			IosOrderForm orderForm = new IosOrderForm(orderFormDTO);
			businessId = orderFormDTO.getOrderSkuDTOListOfOrdGift().get(0).getSupplierId();
			orderForm.setStoreName(businessService.getBusinessNameById(businessId, 0));
			KeyOfOrder keyOfOrder = new KeyOfOrder();
			keyOfOrder.setOrderFormState(orderFormDTO.getOrderFormState());
			keyOfOrder.setPayCloseCD(orderFormDTO.getOrderTime());
			keyOfOrder.setOnpay(orderFormDTO.getOrderFormPayMethod());
			keyOfOrder.setSpSource(orderFormDTO.getSpSource());
			if (orderFormDTO.getOrderFormState() != null
					&& orderFormDTO.getOrderFormState().getIntValue() == OrderFormState.WAITING_PAY.getIntValue()) {
				keyOfOrder.setOrderId(0);
				keyOfOrder.setCombined(true);
			} else {
				keyOfOrder.setOrderId(orderFormDTO.getOrderId());
				keyOfOrder.setCombined(false);
			}
			keyOfOrder.setParentId(orderFormDTO.getParentId());
			
			if (map.get(keyOfOrder) != null) {
				map.get(keyOfOrder).add(orderForm);
			} else {
				List<IosOrderForm> list = new ArrayList<>();
				map.put(keyOfOrder, list);
				list.add(orderForm);
			}
		}

		return map;
	}

	@Override
	public RetArg queryOrderFormByOrderIdAndUserId(long userId, long orderId, Boolean isVisible) {
		RetArg retArg = new RetArg();
		OrderFormDTO orderFormDTO = orderService.queryOrderForm(userId, orderId, isVisible);
		RetArgUtil.put(retArg, orderFormDTO);
		if (orderFormDTO == null) {
			return retArg;
		}
		List<TradeItemDTO> tradeItemDTOs = tradeService.getTradeItemDTOList(orderId, userId);
		if (CollectionUtil.isNotEmptyOfList(tradeItemDTOs)) {
			orderFormDTO.setPayOrderSn(tradeItemDTOs.get(0).getPayOrderSn());
		}
		buildStoreInfo(orderFormDTO.getOrderCartItemDTOList());
		CouponOrder couponOrder = couponOrderService.getCouponOrderByOrderIdOfUseType(orderFormDTO.getUserId(),
				orderId);
		Coupon coupon = null;
		if (null != couponOrder) {
			coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
		}
		RetArgUtil.put(retArg, new CouponDTO(coupon));
		return retArg;
	}
	
	@Override
	public RetArg queryOrderFormByParentOrderIdAndUserId(long userId, long parentOrderId, Boolean isVisible) {
		RetArg retArg = new RetArg();
		
		List<OrderFormDTO> orderFormDTOs = orderService.queryOrderFormListByParentId(parentOrderId);
		List<OrderFormDTO> validOrderFormDTOs = new ArrayList<>();
		CouponOrder couponOrder  = null;
		if (!CollectionUtils.isEmpty(orderFormDTOs)) {
			for (OrderFormDTO formDTO : orderFormDTOs) {
				if (!formDTO.isVisible() || formDTO.getUserId() != userId) {
					continue;
				}
				List<TradeItemDTO> tradeItemDTOs = tradeService.getTradeItemDTOList(formDTO.getOrderId(), userId);
				if (CollectionUtil.isNotEmptyOfList(tradeItemDTOs)) {
					formDTO.setPayOrderSn(tradeItemDTOs.get(0).getPayOrderSn());
				}
				
				if(couponOrder == null){
					couponOrder = couponOrderService.getCouponOrderByOrderIdOfUseType(userId,formDTO.getOrderId());
					Coupon coupon = null;
					if (null != couponOrder) {
						coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
					}
					RetArgUtil.put(retArg, new CouponDTO(coupon));
				}
				buildStoreInfo(formDTO.getOrderCartItemDTOList());
				
				validOrderFormDTOs.add(formDTO);
			}
		}

		if(!CollectionUtils.isEmpty(validOrderFormDTOs)){
			RetArgUtil.put(retArg, validOrderFormDTOs);
		}
		return retArg;
	}

	// 设置店铺信息
	private void buildStoreInfo(List<? extends OrderCartItemDTO> orderCartItemDTOS) {
		if (orderCartItemDTOS == null) {
			return;
		}
		long businessId = 0l;
		for (OrderCartItemDTO orderCartItemDTO : orderCartItemDTOS) {
			if (CollectionUtil.isNotEmptyOfList(orderCartItemDTO.getOrderSkuDTOList())) {
				businessId = orderCartItemDTO.getOrderSkuDTOList().get(0).getSupplierId();
				orderCartItemDTO.setStoreName(businessService.getBusinessNameById(businessId, 0));
				orderCartItemDTO.setStoreUrl(UrlBaseUtil.buildStoreUrl(businessId));
				orderCartItemDTO.setStoreId(businessId);
			}
		}

	}

	public Map<OrderQueryType, Integer> queryOrderListCount(long userId) {
		Map<OrderQueryType, Integer> queryResultMap = new HashMap<>();
		boolean isVisible = true;
		long[] orderTimeRange = null;
		Integer countOfAll = null;

		OrderQueryType[] queryTypeArray = new OrderQueryType[] { OrderQueryType.ALL, OrderQueryType.WAITING_PAY,
				OrderQueryType.WAITING_DELIVE, OrderQueryType.ALREADY_DELIVE };
		for (OrderQueryType queryType : queryTypeArray) {
			orderTimeRange = genOrderTimeRangeByOrderQueryType(queryType, null);
			int count = 0;
			if (countOfAll != null && countOfAll <= 0) {
				// 特殊优化: 全部订单为0的时候,其他订单数目必定为0
				count = 0;
			} else if (queryType == OrderQueryType.WAITING_PAY || queryType == OrderQueryType.ALREADY_DELIVE
					|| queryType == OrderQueryType.WAITING_DELIVE) {
				// CASE1: WAITING_PAY || WAITING_DELIVE || ALREADY_DELIVE
				count = orderService.queryOrderFormListCountByState2(userId, isVisible, queryType.getOrderStateArray(),
						orderTimeRange, null);
			} else if (queryType == OrderQueryType.ALL) {
				// CASE2: ALL
				count = orderService.queryAllOrderFormListCount(userId, isVisible, null);
				countOfAll = count;
			}
			queryResultMap.put(queryType, count);
		}

		// 返回结果
		return queryResultMap;
	}

	private long[] genOrderTimeRangeByOrderQueryType(OrderQueryType queryType, long[] orderTimeRange) {
		long currTime = System.currentTimeMillis();
		if (queryType == OrderQueryType.WAITING_PAY)
			orderTimeRange = new long[] { currTime - ConstValueOfOrder.MAX_PAY_TIME,
					currTime + CalendarConst.MINUTE_TIME * 5 };
		return orderTimeRange;
	}
}
