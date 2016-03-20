/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.erp.facade.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.erp.facade.ERPOrderFacade;
import com.xyl.mmall.erp.util.ERPVOConvertUtil;
import com.xyl.mmall.erp.vo.OrderDetailInfoErpVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.util.UrlBaseUtil;
import com.xyl.mmall.mainsite.util.MainsiteVOConvertUtil;
import com.xyl.mmall.mainsite.vo.order.OrderExpInfoVO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;

/**
 * ERPOrderFacadeImpl.java created by yydx811 at 2015年8月3日 上午10:02:52
 * erp订单facade接口实现
 *
 * @author yydx811
 */
@Facade
public class ERPOrderFacadeImpl implements ERPOrderFacade {

	private static Logger logger = Logger.getLogger(ERPOrderFacadeImpl.class);
	
	@Resource
	private OrderService orderService;

	@Resource
	private OrderBriefService orderBriefService;

	@Resource
	private UserProfileService userProfileService;

	@Resource
	private TradeService tradeService;

	@Resource
	private CouponOrderService couponOrderService;

	@Resource
	private CouponService couponService;

	@Resource
	private BusinessService businessService;

	@Resource
	private AgentService agentService;
	
	@Override
	public int modifyOrderState(OrderOperateParam param) {
		return orderService.modifyOrderFormState(param);
	}

	@Override
	public List<OrderForm> queryOrderFacade(String businessIds, long startTime, long endTime, int state) {
		return orderBriefService.queryOrderFormList(businessIds, startTime, endTime, state);
	}

	@Override
	public OrderFormDTO getOrderFormByOrderId(long orderId, long userId) {
		return orderService.queryOrderForm(userId, orderId, null);
	}

	@Override
	public OrderDetailInfoErpVO queryOrderDetailInfoByOrderId(long orderId) {
		OrderFormDTO ordDTO = orderService.queryOrderFormByOrderId(orderId);
		if (null == ordDTO) {
			return null;
		}
		OrderDetailInfoErpVO ret = new OrderDetailInfoErpVO();
		long userId = ordDTO.getUserId();
		// 0. 回填数据：基本信息 + 配送信息
		String userName = "";
		UserProfileDTO up = userProfileService.findUserProfileById(userId);
		if (null != up) {
			userName = up.getUserName();
		}
		ret.getBasicInfo().fillUserInfo(userId, userName, up.getNickName());
		OrderCancelInfoDTO cancelInfo = orderService.getOrderCancelInfo(userId, orderId);
		// 0. 回填数据：交易信息
		List<TradeItemDTO> tradeItemDTOList = tradeService.getTradeItemDTOList(orderId, userId);
		ordDTO.setPayOrderSn(tradeItemDTOList.get(0).getPayOrderSn());
		// 1. 订单基本信息
		ret.getBasicInfo().fillOrderInfo(ordDTO, cancelInfo);
		// 2.设置优惠券
		// couponOrder: 订单关联的优惠券
		CouponOrder couponOrder = couponOrderService.getCouponOrderByOrderIdOfUseType(userId, orderId);
		// coupon：根据优惠券Code反查出优惠券
		Coupon coupon = null;
		if (null != couponOrder) {
			coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
			ret.getBasicInfo().setCouponSPrice(ordDTO.getCouponDiscount());
			ret.getBasicInfo().fillCouponPromotionInfo(couponOrder, coupon, null, null);
		}
		// 3.收货地址
		ret.setOrderExpInfoVO(ReflectUtil.convertObj(OrderExpInfoVO.class, ordDTO.getOrderExpInfoDTO(), false));
		// 4.回填发票
		ret.setInvoiceInOrdVOs(MainsiteVOConvertUtil.convertToInvoiceInOrdVOs(ordDTO.getInvoiceDTOs()));
		// 5.回填物流
		ret.setOrderLogisticsVOs(MainsiteVOConvertUtil.convertToOrderLogistics(ordDTO.getOrderLogisticsDTOs()));
		// 6.设置店铺信息
		BusinessDTO businessDTO = businessService.getBreifBusinessById(ordDTO.getBusinessId(), -1);
		buildStoreInfo(ordDTO.getOrderCartItemDTOList(), businessDTO);
		ret.getBasicInfo().fillBusinessInfo(businessDTO);
		// 7.转化购物车Vo
		ret.setCartList(ERPVOConvertUtil.convertToOrderCartItemVOList(ordDTO.getOrderCartItemDTOList(), true));

		// 9.备注
		ret.setComment(ordDTO.getComment());
		TradeItemDTO hbFakeTradeItem = convertHBToFakeTradeItem(ordDTO);
		ret.fillTradeList(ordDTO, tradeItemDTOList, hbFakeTradeItem);
		// 10. 设置代客下单账号
		if (ordDTO.getAgentId() > 0) {
			AgentDTO agentDTO = agentService.findAgentById(ordDTO.getAgentId());
			ret.getBasicInfo().setProxyAccount(agentDTO != null ? agentDTO.getName() : "");
		}
		return ret;
	}

	private void buildStoreInfo(List<? extends OrderCartItemDTO> orderCartItemDTOS, BusinessDTO businessDTO) {
		if (orderCartItemDTOS == null) {
			return;
		}
		for (OrderCartItemDTO orderCartItemDTO : orderCartItemDTOS) {
			if (CollectionUtil.isNotEmptyOfList(orderCartItemDTO.getOrderSkuDTOList())) {
				orderCartItemDTO.setStoreName(businessDTO.getStoreName());
				orderCartItemDTO.setStoreUrl(UrlBaseUtil.buildStoreUrl(businessDTO.getId()));
			}
		}
	}

	private TradeItemDTO convertHBToFakeTradeItem(OrderFormDTO ordForm) {
		if (null == ordForm || BigDecimal.ZERO.compareTo(ordForm.getRedCash()) >= 0) {
			return null;
		}
		TradeItemDTO trade = new TradeItemDTO();
		trade.setTradeId(-1);
		trade.setOrderId(ordForm.getOrderId());
		trade.setUserId(ordForm.getUserId());
		trade.setCash(ordForm.getRedCash());
		trade.setTradeItemPayMethod(null);
		return trade;
	}
}
