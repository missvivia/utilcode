package com.xyl.mmall.mobile.ios.facade.pageView.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.netease.print.common.util.DateFormatEnum;
import com.xyl.mmall.mobile.ios.facade.pageView.orderDetail.OrderDetailComposite;
import com.xyl.mmall.mobile.ios.facade.pageView.orderDetail.OrderExpInfoComposite;
import com.xyl.mmall.mobile.ios.facade.pageView.orderDetail.OrderItemCommitInfoVO;
import com.xyl.mmall.mobile.ios.facade.pageView.orderDetail.OrderSkuViewVO;
import com.xyl.mmall.mobile.web.util.MobileVOConvertUtil;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.OrderFormState;

public class ConverUtilForMobile {
	private static Logger logger = Logger.getLogger(ConverUtilForMobile.class);


	public static OrderDetailComposite convertToCompositeOrderDetail(List<OrderFormDTO> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		OrderDetailComposite orderDetailComposite = new OrderDetailComposite();
		OrderExpInfoComposite orderExpInfoComposite = new OrderExpInfoComposite();
		List<OrderItemCommitInfoVO> orderSkuInfos = new ArrayList<>();
		OrderFormDTO firstOrderFormDTO = list.get(0);
		if (firstOrderFormDTO != null) {

			// 设置地址
			if (orderExpInfoComposite.getUserId() == 0) {
				try {
					BeanUtils.copyProperties(orderExpInfoComposite, firstOrderFormDTO.getOrderExpInfoDTO());
				} catch (Exception e) {
					logger.error("orderExpInfoComposite attribute copy fail:  " + e.getMessage());
				}

				orderDetailComposite.setExpInfo(orderExpInfoComposite);
			}
			// 设置PayCloseCD
			long currTime = System.currentTimeMillis(), payCloseCD = 0;
			if (firstOrderFormDTO.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME > currTime
					&& firstOrderFormDTO.getOrderFormState() == OrderFormState.WAITING_PAY) {
				payCloseCD = firstOrderFormDTO.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME - currTime;
				payCloseCD = payCloseCD <= 0 ? 0 : payCloseCD;
			}
			orderDetailComposite.setPayCloseCD(payCloseCD);
			// 6.设置订单状态
			if (payCloseCD <= 0 && firstOrderFormDTO.getOrderFormState() == OrderFormState.WAITING_PAY) {
				firstOrderFormDTO.setOrderFormState(OrderFormState.CANCEL_ED);

			}

			orderDetailComposite.setOrderDate(DateFormatEnum.TYPE5.getFormatDate(firstOrderFormDTO.getOrderTime()));
			orderDetailComposite.setPayDate(firstOrderFormDTO.getPayTime() > 0
					? DateFormatEnum.TYPE5.getFormatDate(firstOrderFormDTO.getPayTime()) : "");
			orderDetailComposite.setInvoiceTitle(firstOrderFormDTO.getInvoiceInOrdDTO() != null
					? firstOrderFormDTO.getInvoiceInOrdDTO().getTitle() : null);
			boolean hasInvoice = firstOrderFormDTO.getInvoiceDTOs() != null;
			if (hasInvoice) {
				orderDetailComposite.setInvoiceInOrdVOs(
						MobileVOConvertUtil.convertToInvoiceInOrdVOs(firstOrderFormDTO.getInvoiceDTOs()));
			}

			orderDetailComposite.setParentId(firstOrderFormDTO.getParentId());
			orderDetailComposite.setComment(firstOrderFormDTO.getComment());
			orderDetailComposite.setCanCOD(firstOrderFormDTO.isCanCOD());
			orderDetailComposite.setCanCancel(firstOrderFormDTO.isCanCancel());
			orderDetailComposite.setUserId(firstOrderFormDTO.getUserId());
			orderDetailComposite.setPayState(firstOrderFormDTO.getPayState());
			orderDetailComposite.setOrderFormState(firstOrderFormDTO.getOrderFormState());
			orderDetailComposite.setOrderTime(firstOrderFormDTO.getOrderTime());
			orderDetailComposite.setSpSource(firstOrderFormDTO.getSpSource());
			orderDetailComposite.setCancelDate(firstOrderFormDTO.getCancelTime()>0?DateFormatEnum.TYPE5.getFormatDate(firstOrderFormDTO.getCancelTime()):"");
			orderDetailComposite.setCancelReason(firstOrderFormDTO.getCancelReason());
//			orderDetailComposite.setCancelSource(firstOrderFor);
		}
		for (OrderFormDTO orderDTO : list) {
			// 转化购物车
			OrderItemCommitInfoVO orderItemInfoVO = converToOrderItemInfoVO(orderDTO.getOrderCartItemDTOList());
			orderItemInfoVO.setOmsDate(orderDTO.getOmsTime()>0?DateFormatEnum.TYPE5.getFormatDate(orderDTO.getOmsTime()):"");
			orderItemInfoVO.setConfirmDate(orderDTO.getConfirmTime()>0?DateFormatEnum.TYPE5.getFormatDate(orderDTO.getConfirmTime()):"");
			orderSkuInfos.add(orderItemInfoVO);
			// 设置商品金额
			setPrice(orderDetailComposite, orderDTO);

		}
		orderDetailComposite.setOrderSkuInfos(orderSkuInfos);
		// 7.2 设置商品金额+邮费
		return orderDetailComposite;
	}

	private static void setPrice(OrderDetailComposite orderDetailComposite, OrderFormDTO orderDTO) {
		// TODO Auto-generated method stub

		BigDecimal cartOriRPrice = orderDTO.getCartOriRPrice();
		BigDecimal cartRPrice = orderDTO.getCartRPrice();
		BigDecimal hdSPrice = BigDecimal.ZERO;
		BigDecimal redPacketSPrice = orderDTO.getRedCash() != null ? orderDTO.getRedCash() : BigDecimal.ZERO;
		BigDecimal totalCash = cartRPrice.add(orderDTO.getExpUserPrice());
		BigDecimal realCash = totalCash.subtract(redPacketSPrice);

		orderDetailComposite.setCartOriRPrice(cartOriRPrice.add(orderDetailComposite.getCartOriRPrice()));
		orderDetailComposite.setCartRPrice(cartRPrice.add(orderDetailComposite.getCartRPrice()));
		orderDetailComposite.setHdSPrice(hdSPrice.add(orderDetailComposite.getHdSPrice()));
		orderDetailComposite.setCouponSPrice(orderDTO.getCouponDiscount().add(orderDetailComposite.getCouponSPrice()));
		orderDetailComposite.setExpOriPrice(orderDTO.getExpOriPrice().add(orderDetailComposite.getExpOriPrice()));
		orderDetailComposite
				.setExpSysPayPrice(orderDTO.getExpSysPayPrice().add(orderDetailComposite.getExpSysPayPrice()));
		orderDetailComposite.setExpUserPrice(orderDTO.getExpUserPrice().add(orderDetailComposite.getExpUserPrice()));
		orderDetailComposite.setTotalCash(totalCash.add(orderDetailComposite.getTotalCash()));
		orderDetailComposite.setRealCash(realCash.add(orderDetailComposite.getRealCash()));

	}

	private static OrderItemCommitInfoVO converToOrderItemInfoVO(List<? extends OrderCartItemDTO> orderCartItemDTOList) {
		// TODO Auto-generated method stub
		OrderItemCommitInfoVO orderItemInfoVO = new OrderItemCommitInfoVO();
		if (CollectionUtils.isNotEmpty(orderCartItemDTOList)) {
			orderItemInfoVO.setStoreId(orderCartItemDTOList.get(0).getStoreId());
			orderItemInfoVO.setStoreName(orderCartItemDTOList.get(0).getStoreName());
			orderItemInfoVO.setOrderId(orderCartItemDTOList.get(0).getOrderId());
			List<OrderSkuViewVO> list = new ArrayList<>();
			OrderSkuViewVO orderSkuViewVO = null;
			for (OrderCartItemDTO orderCartItemDTO : orderCartItemDTOList) {
				if (orderCartItemDTO == null) {
					continue;
				}
				List<? extends OrderSkuDTO> skuDTOs = orderCartItemDTO.getOrderSkuDTOList();
				if (CollectionUtils.isNotEmpty(skuDTOs)) {
					for (OrderSkuDTO skuDTO : skuDTOs) {
						if (skuDTO != null) {
							orderSkuViewVO = new OrderSkuViewVO(skuDTO);
							list.add(orderSkuViewVO);
						}
					}
				}

			}
			orderItemInfoVO.setOrderSkuList(list);
		}

		return orderItemInfoVO;
	}


}
