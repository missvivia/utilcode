/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.common.enums.OrderQueryType;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.param.OrderFacadeComposeOrderParam;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;
import com.xyl.mmall.mainsite.util.MainsiteVOConvertUtil;
import com.xyl.mmall.mainsite.vo.CartVO;
import com.xyl.mmall.mainsite.vo.order.OrderCartItemVO;
import com.xyl.mmall.mainsite.vo.order.OrderCoupon1VO;
import com.xyl.mmall.mainsite.vo.order.OrderExpInfoVO;
import com.xyl.mmall.mainsite.vo.order.OrderForm1VO;
import com.xyl.mmall.mainsite.vo.order.OrderForm2VO;
import com.xyl.mmall.mainsite.vo.order.OrderPackageVO;
import com.xyl.mmall.mainsite.vo.order.OrderSkuVO;
import com.xyl.mmall.member.dto.MobileInfoDTO;
import com.xyl.mmall.member.service.MobileInfoService;
import com.xyl.mmall.mobile.facade.MobileOrderFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileCartSkuList;
import com.xyl.mmall.mobile.facade.converter.MobileChecker;
import com.xyl.mmall.mobile.facade.converter.MobileConfig;
import com.xyl.mmall.mobile.facade.converter.MobileOrderStatus;
import com.xyl.mmall.mobile.facade.param.MobileOrderCommitAO;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;
import com.xyl.mmall.mobile.facade.param.MobilePayOrderParam;
import com.xyl.mmall.mobile.facade.vo.MobileCartDetailVO;
import com.xyl.mmall.mobile.facade.vo.MobileConsigneeAddressVO;
import com.xyl.mmall.mobile.facade.vo.MobileKeyPairVO;
import com.xyl.mmall.mobile.facade.vo.MobileOrderCompletVO;
import com.xyl.mmall.mobile.facade.vo.MobileOrderPriceVO;
import com.xyl.mmall.mobile.facade.vo.MobileOrderSummaryVO;
import com.xyl.mmall.mobile.facade.vo.MobileOrderVO;
import com.xyl.mmall.mobile.facade.vo.MobilePackageVO;
import com.xyl.mmall.mobile.facade.vo.MobileShareVO;
import com.xyl.mmall.mobile.facade.vo.MobileSkuVO;
import com.xyl.mmall.mobile.facade.vo.MobileUnReadMessageVO;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderFormPayMethodDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormSource;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.OrderCancelInfo;
import com.xyl.mmall.order.result.OrderCalServiceGenOrderResult;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.OrderUnreadService;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.promotion.enums.ShareChannel;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.PromotionService;
import com.xyl.mmall.promotion.service.RebateService;
import com.xyl.mmall.promotion.service.RedPacketShareRecordService;
import com.xyl.mmall.promotion.service.UserCouponService;

/**
 * @author hzjiangww
 * 
 */
@Facade
public class MobileOrderFacadeImpl implements MobileOrderFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private RedPacketShareRecordService redPacketShareRecordService;

	@Resource
	private OrderUnreadService orderUnreadService;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CartFacade cartFacade;

	@Autowired
	private ConsigneeAddressFacade consigneeAddressFacade;

	@Resource
	private CouponOrderService couponOrderService;

	@Resource
	private CouponService couponService;

	@Autowired
	private RebateService rebateService;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private PromotionService promotionService;

	@Resource
	private UserCouponService userCouponService;

	@Resource
	private MobileInfoService mobileInfoService;

	@Autowired
	private ReturnPackageFacade returnPackageFacade;

	@Override
	public BaseJsonVO unReadOrder(long userId) {
		logger.info("unReadOrder userId : <" + userId + ">");

		MobileUnReadMessageVO vo = new MobileUnReadMessageVO();
		try {
			// 显示全部
			int c1 = orderUnreadService.getUnReadOrderNumber(userId, 1l, OrderQueryType.genEnumByIntValueSt(1)
					.getOrderStateArray(),
					new long[] { Converter.getTime() - ConstValueOfOrder.MAX_PAY_TIME, Converter.getTime() });
			int c2 = orderUnreadService.getUnReadOrderNumber(userId, 1l, OrderQueryType.genEnumByIntValueSt(2)
					.getOrderStateArray(), null);
			int c3 = orderUnreadService.getUnReadOrderNumber(userId, 1l, OrderQueryType.genEnumByIntValueSt(3)
					.getOrderStateArray(), null);
			vo.setUnPayCount(c1);
			vo.setUnSendCount(c2);
			vo.setSendCount(c3);

			/*
			 * DDBParam param = DDBParam.genParamX(10); long[] time = { 0l,
			 * Converter.getTime() }; RetArg retArg =
			 * orderFacade.queryOrderList(userId, OrderQueryType.WAITING_PAY,
			 * null, time, param);
			 * 
			 * @SuppressWarnings("unchecked") List<OrderFormDTO> orderDTOList =
			 * RetArgUtil.get(retArg, ArrayList.class); if (orderDTOList != null
			 * && orderDTOList.size() > 0) { List<MobileCountDwonVO>
			 * countDownList = new ArrayList<MobileCountDwonVO>(); for
			 * (OrderFormDTO d : orderDTOList) { MobileCountDwonVO cd = new
			 * MobileCountDwonVO(); OrderForm2VO d2 =
			 * MainsiteVOConvertUtil.convertToOrderForm2VO(d);
			 * cd.setId(d2.getOrderId()); cd.setEndTime(Converter.getTime() +
			 * d2.getPayCloseCD()); cd.setCountDownTime(d2.getPayCloseCD());
			 * countDownList.add(cd); } vo.setCountDownList(countDownList); }
			 * long couponReadTime = orderUnreadService.getUnReadTime(userId,
			 * 10); int size =
			 * userCouponService.getUserCouponCountBycreateTime(userId,
			 * couponReadTime); vo.setUnReadNewCoupon(size);
			 */

			return Converter.converterBaseJsonVO(vo);
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR, e1.getMessage());
		}

	}

	@Override
	public BaseJsonVO deleteOrder(long userId, Long orderId) {
		logger.info("order cancel for orderId:<" + orderId + ">,userId : <" + userId + ">");

		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkZero("ORDER ID", orderId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		try {
			boolean isSucc = orderFacade.updateIsVisible(userId, orderId, false);
			if (isSucc) {
				return Converter.converterBaseJsonVO(null);
			} else {
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL);
			}
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR, e1.getMessage());
		}
	}

	@Override
	public BaseJsonVO cancelOrder(long userId, Long orderId, int type, String reason) {
		logger.info("order cancel for orderId:<" + orderId + ">,userId : <" + userId + ">");

		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkZero("ORDER ID", orderId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		if (type != 1 && type != 0)
			return Converter.errorBaseJsonVO(MobileErrorCode.PARAM_NO_MATCH, "rtype:" + type);

		try {
			OrderCancelInfo canceler = new OrderCancelInfo();
			canceler.setCtime(Converter.getTime());
			canceler.setCancelSource(OrderCancelSource.USER);
			canceler.setUserId(userId);
			canceler.setOrderId(orderId);
			canceler.setRtype(OrderCancelRType.genEnumByIntValueSt(type));
			canceler.setReason(reason);
			CancelOmsOrderTaskDTO cancelTaskDTO = new CancelOmsOrderTaskDTO(canceler);
			RetArg retArg = orderFacade.addCancelOmsOrderTask(cancelTaskDTO);
			Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
			if (isSucc == Boolean.TRUE) {
				return Converter.converterBaseJsonVO(null);
			} else {
				return Converter.errorBaseJsonVO(MobileErrorCode.ORDER_CANCEL_ERROR);
			}
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.ORDER_CANCEL_ERROR, e1.getMessage());
		}

	}

	private MobileSkuVO coverSkuVO(OrderSkuVO skuvo) {
		MobileSkuVO sku = new MobileSkuVO();
		sku.setBuyCount(skuvo.getTotalCount());
		sku.setOriginPrice(Converter.doubleFormat(skuvo.getOriRPrice()));
		sku.setPoPrice(Converter.doubleFormat(skuvo.getRprice()));
		sku.setPrdtId(skuvo.getProductId());
		// if (skuvo.getSkuSPDTO() != null) {
		// sku.setPrdtName(skuvo.getSkuSPDTO().getProductName());
		// sku.setBrandName(skuvo.getSkuSPDTO().getBrandName());
		// sku.setSkuImageUrl(skuvo.getSkuSPDTO().getPicUrl());
		// Map<String, String> desc = skuvo.getSkuSPDTO().getSkuSpecValueMap();
		// if (desc != null) {
		// sku.setSkuSizeDesc("");
		// for (String d : desc.keySet()) {
		// sku.setSkuSizeDesc(sku.getSkuSizeDesc() + d + ":" + desc.get(d) +
		// " ");
		// }
		// sku.setSkuSizeDesc(sku.getSkuSizeDesc().trim());
		// }
		//
		// }
		sku.setSkuId(skuvo.getSkuId());
		// 訂單的永遠正常
		sku.setValidStatus(1);
		return sku;
	}

	@Override
	public BaseJsonVO getOrderList(long userId, Integer type, MobilePageCommonAO ao) {
		if (type == null)
			type = 0;
		if (type < 0 || type > 3)
			return Converter.errorBaseJsonVO(MobileErrorCode.PARAM_NO_MATCH, "type:<" + type + "> not support");
		try {
			// orderUnreadService.updateReadTime(userId, type);
			OrderQueryType qtype = OrderQueryType.genEnumByIntValueSt(type);
			DDBParam param = DDBParam.genParamX(ao.getPageSize());
			param.setOrderColumn("orderTime");
			param.setAsc(false);
			if (ao.getTimestamp() == 0) {
				ao.setTimestamp(Converter.getTime());
			}
			long[] time = { 0l, ao.getTimestamp() };
			RetArg retArg = orderFacade.queryOrderList(userId, qtype, null, time, param);
			@SuppressWarnings("unchecked")
			List<OrderFormDTO> orderDTOList = RetArgUtil.get(retArg, ArrayList.class);
			param = RetArgUtil.get(retArg, DDBParam.class);
			List<MobileOrderSummaryVO> list = new ArrayList<MobileOrderSummaryVO>();
			if (orderDTOList == null || orderDTOList.size() == 0)
				return Converter.listBaseJsonVO(list, false);
			for (OrderFormDTO from : orderDTOList) {
				MobileOrderSummaryVO vo = coverOrderSummaryVo(from);
				list.add(vo);
				// 基本数据均OK
			}
			return Converter.listBaseJsonVO(list, param.isHasNext());
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR, e1.getMessage());
		}
	}

	private MobileOrderSummaryVO coverOrderSummaryVo(OrderFormDTO form) {
		OrderForm2VO dto = MainsiteVOConvertUtil.convertToOrderForm2VO(form, returnPackageFacade);
		MobileOrderSummaryVO vo = new MobileOrderSummaryVO();
		if (dto.getOrderFormPayMethod() != null)
			vo.setPayChannel(dto.getOrderFormPayMethod().getIntValue());
		MobileOrderStatus status = Converter.coverOrderState(dto);
		boolean canCancel = dto.isCanCancel();
		if (dto.getExtDTO() != null) {
			if (dto.getExtDTO().isCancelFail()) {
				canCancel = false;
				vo.setCancelFail(1);
			}
		}
		List<Integer> button = Converter
				.coverOrderButtonState(status.getIntValue(), canCancel, dto.getOrderFormState());
		vo.setButtonList(button);
		vo.setStatus(status.getIntValue());
		vo.setDeliverURL(Converter.genWebSiteLink(Converter.LOGISTIC_DETAIL_BY_ORDER, dto.getOrderId()));
		vo.setEndTime(dto.getPayCloseCD() + Converter.getTime());
		vo.setCountdownTime(dto.getPayCloseCD());
		vo.setCarriageFee(Converter.doubleFormat(dto.getExpUserPrice()));
		vo.setOrderId(dto.getOrderId());
		vo.setOrderTime(dto.getOrderTime());
		// if button content 被拒原因
		// vo.setCodReject("");
		vo.setPrdtCount(form.getSkuCount());
		vo.setTotalPrice(Converter.doubleFormat(dto.getCartRPrice()) + Converter.doubleFormat(dto.getExpUserPrice()));
		if (dto.getPackageList() != null && dto.getPackageList().size() > 0) {
			List<MobilePackageVO> pakages = new ArrayList<MobilePackageVO>();
			int i = 1;
			for (OrderPackageVO orderPackage : dto.getPackageList()) {
				MobilePackageVO m_pakage = new MobilePackageVO();
				m_pakage.setDeliverCode(orderPackage.getMailNO());
				m_pakage.setDeliverURL(Converter.genWebSiteLink(Converter.LOGISTIC_DETAIL, orderPackage.getMailNO(),
						orderPackage.getExpressCompanyReturn()));
				m_pakage.setPackageId(orderPackage.getPackageId());
				m_pakage.setPackageIndex(orderPackage.getPackageIndex() + i);
				m_pakage.setOrderTime(orderPackage.getExpSTime());
				m_pakage.setPackageStatus(Converter.coverPacketState(dto.getOrderFormState(),
						orderPackage.getOrderPackageState(), orderPackage.getReturnPackageState()).getIntValue());

				if (orderPackage.getCartList() != null && orderPackage.getCartList().size() > 0
						&& orderPackage.getCartList().get(0).getOrderSkuList() != null
						&& orderPackage.getCartList().get(0).getOrderSkuList().size() > 0) {
					int prdtCount = 0;
					for (OrderCartItemVO v1 : orderPackage.getCartList()) {
						for (OrderSkuVO v2 : v1.getOrderSkuList())
							prdtCount = prdtCount + v2.getTotalCount();
					}
					m_pakage.setPrdtCount(prdtCount);
					OrderSkuVO skuVo = orderPackage.getCartList().get(0).getOrderSkuList().get(0);
					MobileSkuVO m_skuVo = coverSkuVO(skuVo);
					List<MobileSkuVO> m_skuVos = new ArrayList<MobileSkuVO>();
					m_skuVos.add(m_skuVo);
					m_pakage.setSkuList(m_skuVos);
				}
				pakages.add(m_pakage);
			}
			vo.setPackages(pakages);
			if (pakages.size() > 0 && pakages.get(0).getSkuList() != null
					&& dto.getPackageList().get(0).getCartList().get(0).getOrderSkuList() != null
					&& pakages.get(0).getSkuList().size() > 0) {
				vo.setFirstSku(pakages.get(0).getSkuList().get(0));
			}
		}
		return vo;
	}

	private MobileOrderPriceVO getPrice(OrderForm2VO dto, long appversion) {
		MobileOrderPriceVO price = new MobileOrderPriceVO();
		price.setCarriageFee(Converter.doubleFormat(dto.getExpUserPrice()));
		price.setFinalprice(Converter.doubleFormat(dto.getCartRPrice().add(dto.getExpUserPrice())
				.subtract(dto.getRedPacketSPrice())));
		price.setOriginPrice(Converter.doubleFormat(dto.getCartOriRPrice()));
		price.setReducedPrice(Converter.doubleFormat(dto.getCartOriRPrice().subtract(dto.getCartRPrice())));
		if (appversion >= Converter.protocolVersion("1.2.0")) {
			price.setGiftPrice(Converter.doubleFormat(dto.getRedPacketSPrice()));
			price.setHdPrice(Converter.doubleFormat(dto.getHdSPrice()));
			price.setCouponPrice(Converter.doubleFormat(dto.getCouponSPrice()));
		}

		return price;
	}

	private MobileOrderVO coverOrderVo(OrderFormDTO form, long appversion, Coupon coupon) {
		OrderForm2VO dto = MainsiteVOConvertUtil.convertToOrderForm2VO(form, returnPackageFacade);
		MobileOrderVO vo = new MobileOrderVO();
		// since 1.2.0
		if (dto.getExtDTO() != null) {
			if (dto.getExtDTO().isCancelFail()) {
				vo.setCancelFail(1);
			}
		}
		vo.setCountDownSeconds(dto.getPayCloseCD());
		vo.setEndTime(dto.getPayCloseCD() + Converter.getTime());
		List<String> allInfo = new ArrayList<String>();
		vo.setStatus(Converter.coverOrderState(dto).getIntValue());
		if (Converter.hasLogist(dto.getOrderFormState()))
			vo.setDeliverURL(Converter.genWebSiteLink(Converter.LOGISTIC_DETAIL_BY_ORDER, dto.getOrderId()));
		// TODO 优惠券名字
		if (appversion >= Converter.protocolVersion("1.2.0")) {
			if (vo.getStatus() == MobileOrderStatus.FINISH.getIntValue()) {
				vo.setShareGift(shareGiftF(form.getOrderId()));
			}
			if (dto.getRedPacketSPrice().doubleValue() > 0)
				allInfo.add("已使用红包:" + Converter.doubleFormat(dto.getRedPacketSPrice()) + "元;");
			// vo.setGiftMoneyInvolved(giftMoneyInvolved);
		}
		// dto.getRedPacketSPrice()
		// 紅包
		// 紅包 vo.setGiftMoneyRemain(giftMoneyRemain);
		if (StringUtils.isNotBlank(dto.getInvoiceTitle()))
			vo.setInvoiceTitle(dto.getInvoiceTitle());

		if (form.getExpressCompany() != null) {
			vo.setExpressType(form.getExpressCompany().getIntValue());
			vo.setExpressName(form.getExpressCompany().getDesc());
		}
		vo.setOrderId(dto.getOrderId());
		vo.setOrderTime(dto.getOrderTime());
		// 支付方式
		if (appversion < Converter.protocolVersion("1.2.0") && dto.getOrderFormPayMethod() == OrderFormPayMethod.ALIPAY) {
			vo.setPayChannel(OrderFormPayMethod.EPAY.getIntValue());
		} else {
			vo.setPayChannel(dto.getOrderFormPayMethod().getIntValue());
		}
		if (dto.getOrderFormState() == OrderFormState.WAITING_PAY) {
			List<Integer> channel = new ArrayList<Integer>();
			for (OrderFormPayMethod a : OrderFormPayMethod.values()) {
				if (appversion < Converter.protocolVersion("1.2.0") && a == OrderFormPayMethod.ALIPAY) {
					continue;
				}
				if (!dto.isCanCOD() && !OrderFormPayMethod.isOnlinePayMethod(a)) {
					continue;
				}
				channel.add(a.getIntValue());
			}

			int[] c = new int[channel.size()];
			for (int i = 0; i < channel.size(); i++) {
				c[i] = channel.get(i);
			}
			vo.setPayChannelList(c);

		}

		MobileOrderPriceVO price = getPrice(dto, appversion);
		vo.setPriceInfo(price);
		OrderExpInfoVO orderExp = dto.getExpInfo();
		if (orderExp != null) {
			MobileConsigneeAddressVO address = new MobileConsigneeAddressVO();
			address.setAddressSuffix(orderExp.getAddress());
			address.setCityName(orderExp.getCity());
			address.setDistrictName(orderExp.getSection());
			address.setName(orderExp.getConsigneeName());
			address.setPhone(orderExp.getConsigneeMobile());
			address.setTel(orderExp.getConsigneeTel());
			address.setProvinceName(orderExp.getProvince());
			address.setStreetName(orderExp.getStreet());
			address.setZipcode(orderExp.getZipcode());
			vo.setShipAddress(address);
		}

		if (dto.getPackageList() != null && dto.getPackageList().size() > 0) {
			List<MobilePackageVO> pakages = new ArrayList<MobilePackageVO>();
			for (OrderPackageVO orderPackage : dto.getPackageList()) {
				MobilePackageVO m_pakage = new MobilePackageVO();
				if (StringUtils.isNotBlank(orderPackage.getMailNO())) {
					m_pakage.setDeliverCode(orderPackage.getMailNO());
					m_pakage.setDeliverURL(Converter.genWebSiteLink(Converter.LOGISTIC_DETAIL,
							orderPackage.getMailNO(), orderPackage.getExpressCompanyReturn()));
				}

				m_pakage.setPackageId(orderPackage.getPackageId());
				m_pakage.setPackageIndex(orderPackage.getPackageIndex() + 1);
				m_pakage.setPackageStatus(Converter.coverPacketState(dto.getOrderFormState(),
						orderPackage.getOrderPackageState(), orderPackage.getReturnPackageState()).getIntValue());

				if (m_pakage.getPackageStatus() == MobileOrderStatus.CANCEL.getIntValue()
						&& vo.getStatus() != MobileOrderStatus.CANCEL.getIntValue()) {
					m_pakage.setRejectInfo(orderPackage.getOrderPackageState().getName());
				}
				m_pakage.setOrderTime(orderPackage.getExpSTime());
				List<MobileSkuVO> skuList = new ArrayList<MobileSkuVO>();
				if (orderPackage.getCartList() != null) {
					for (OrderCartItemVO cartItem : orderPackage.getCartList()) {
						if (cartItem.getOrderSkuList() != null) {
							for (OrderSkuVO skuvo : cartItem.getOrderSkuList()) {
								skuList.add(coverSkuVO(skuvo));
							}
						}
					}
				}
				m_pakage.setSkuList(skuList);
				pakages.add(m_pakage);
			}
			vo.setPackageList(pakages);
		}
		String platInfo = "";
		/**
		 * 预留 Promotion p =
		 * promotionService.getPromotionById(form.getPromotionId());
		 * 
		 * if (p != null && StringUtils.isNotBlank(p.getDescription())) {
		 * platInfo = platInfo + p.getDescription() + ";";
		 * 
		 * }
		 **/
		if (coupon != null) {
			vo.setCouponCode(coupon.getCouponCode());
			vo.setCouponName(coupon.getName());
			allInfo.add("已使用优惠券：" + coupon.getName());
		}

		if (Converter.doubleFormat(dto.getCartRPrice()) >= 288) {
			platInfo = platInfo + MobileConfig.active_platform_info + ";";
			allInfo.add("订单已享受:" + MobileConfig.active_platform_info + ";");
		}

		vo.setAllInfo(allInfo);
		vo.setPlatformInfo(platInfo);
		return vo;
	}

	@Override
	public BaseJsonVO getOrderDetail(long userId, long orderId, long appversion) {
		logger.info("getOrderDetail for orderId:<" + orderId + ">,userId : <" + userId + ">");

		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkZero("ORDER ID", orderId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		try {
			OrderFormDTO dto = orderFacade.queryOrderForm(userId, orderId, true);
			if (dto == null)
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL);
			Coupon coupon = null;
			try {
				// 补下优惠券信息
				// 补充平台活动信息

				CouponOrder couponCode = couponOrderService.getCouponOrderByOrderIdOfUseType(userId, orderId);
				if (couponCode != null) {
					coupon = couponService.getCouponByCode(couponCode.getCouponCode(), false);
				}

			} catch (Exception e0) {
				logger.error(e0.getMessage());
				// 抛错不返回
			}
			MobileOrderVO vo = coverOrderVo(dto, appversion, coupon);
			return Converter.genrBaseJsonVO("orderDtail", vo);
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR, e1.getMessage());
		}
	}

	/**
	 * *********************************************** 以下备用
	 */
	private MobileErrorCode genCodeForComposeOrder(Integer retCodeOfOrderService) {
		// CASE0: 组单失败-其他原因
		if (retCodeOfOrderService == 0)
			return MobileErrorCode.ORDER_ERROR;
		// CASE1: 组单成功
		if (retCodeOfOrderService == 1)
			return MobileErrorCode.NULL;
		// CASE2: 组单失败-库存不足
		if (retCodeOfOrderService == 2)
			return MobileErrorCode.ORDER_SKU_EMPTY;
		if (retCodeOfOrderService == 3)
			return MobileErrorCode.ORDER_ADDRESS_ERROR;
		if (retCodeOfOrderService == 4)
			return MobileErrorCode.ORDER_COUPON_ERROR;
		if (retCodeOfOrderService == 5)
			return MobileErrorCode.PARAM_NO_MATCH;
		// CASE3： 组单失败-其他原因
		return MobileErrorCode.ORDER_ERROR;
	}

	@Override
	public BaseJsonVO genOrder(long userId, int areaId, int os, long appversion, int useGiftMoney) {
		logger.info("addInDetailPage -> userId:<" + userId + ">,areaCode:<" + areaId + ">");
		try {
			MobileChecker.checkZero("USER ID", userId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		try {
			MobileChecker.checkZero("AREA CODE", areaId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_MATCH, e1.getMessage());
		}
		try {
			CartVO vo = cartFacade.getCart(userId, areaId, null, PlatformType.MOBILE);
			if (vo == null)
				return Converter.errorBaseJsonVO(MobileErrorCode.CART_TIMEOUT, "empty cart");
			// 取一条地址
			MobileCartSkuList mcl = MobileCartFacadeImpl.getSkuIdList(vo);
			String cartIds = MobileCartFacadeImpl.getSkuIds(mcl);
			String hash = Converter.genCartHash(vo.getCartInfoVO().getUpdateTime(), cartIds);
			ConsigneeAddressDTO caDTO = consigneeAddressFacade.getDefaultConsigneeAddress(userId);
			if (caDTO == null || caDTO.getId() == 0)
				caDTO = consigneeAddressFacade.getOneAddress(userId);

			if (caDTO == null)
				return Converter.errorBaseJsonVO(MobileErrorCode.CART_ADDRESS_EMPTY);

			HashMap<String, Object> map = genOrderMap(
					new MobilePayOrderParam(userId, areaId, cartIds, os, caDTO.getId(), hash, useGiftMoney), appversion);
			// 替换掉订单倒计时
			cartFacade.resetTime(userId, areaId);
			vo = cartFacade.getCart(userId, areaId, null, PlatformType.MOBILE);
			long time = vo.getCartInfoVO().getLeftTime();
			hash = Converter.genCartHash(vo.getCartInfoVO().getUpdateTime(), cartIds);
			map.put("hash", hash);
			map.put("countDownTime", time);
			map.put("endTime", Converter.getTime() + time);

			MobileCartDetailVO cart = MobileCartFacadeImpl.coverToMobileCartDetailVO(vo, true);
			if (cart != null) {
				map.put("cartPOList", cart.getCartPOList());
			} else {
				Converter.errorBaseJsonVO(MobileErrorCode.CART_TIMEOUT);
			}
			if (map.get("error") != null)
				return Converter.errorBaseJsonVO((MobileErrorCode) map.get("error"));
			map.remove("orderCalDTO");
			map.remove("fcResultDTO");
			map.put("platformInfo", MobileConfig.active_platform_info);
			return Converter.converterBaseJsonVO(map);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}

	}

	@Override
	public BaseJsonVO genOrderChange(long userId, int areaId, int os, MobileOrderCommitAO ao, long appversion) {
		logger.info("addInDetailPage -> userId:<" + userId + ">,areaCode:<" + areaId + ">");
		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkZero("ADDRESS ID", ao.getAddressId());
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		try {
			MobileChecker.checkZero("AREA CODE", areaId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_MATCH, e1.getMessage());
		}
		try {
			CartVO vo = cartFacade.getCart(userId, areaId, null, PlatformType.MOBILE);
			if (vo == null)
				return Converter.errorBaseJsonVO(MobileErrorCode.CART_TIMEOUT, "empty cart");
			// 取一条地址
			MobileCartSkuList mcl = MobileCartFacadeImpl.getSkuIdList(vo);
			String cartIds = MobileCartFacadeImpl.getSkuIds(mcl);
			String hash = Converter.genCartHash(vo.getCartInfoVO().getUpdateTime(), cartIds);
			HashMap<String, Object> map = genOrderMap(new MobilePayOrderParam(userId, areaId, cartIds, os, hash, ao),
					appversion);
			if (map.get("error") != null)
				return Converter.errorBaseJsonVO((MobileErrorCode) map.get("error"));

			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("orderPrice", map.get("orderPrice"));
			result.put("payChannel", map.get("payChannel"));
			result.put("expressChannel", map.get("expressChannel"));

			return Converter.converterBaseJsonVO(result);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

	private HashMap<String, Object> genOrderMap(MobilePayOrderParam param, long appversion) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		// 拼参数
		// if()
		// .
		OrderFacadeComposeOrderParam ocop = new OrderFacadeComposeOrderParam();
		ocop.setCartIds(param.getCartIds());
		ocop.setCaId(param.getAddressId());
		ocop.setProvinceId(param.getAreaCode());
		ocop.setUserId(param.getUserId());
		ocop.setHbCash(BigDecimal.ZERO);
		if (appversion >= Converter.protocolVersion("1.2.0") && param.isUseGift()) {
			// 如果使用，就自动使用
			ocop.setHbCash(new BigDecimal("-1"));
		}
		if (param.getCouponId() != 0l) {
			ocop.setUserCouponId(param.getCouponId());
		}
		if (StringUtils.isNotBlank(param.getInvoiceTitle()))
			ocop.setInvoiceTitle(param.getInvoiceTitle());
		if (param.getPayMethodInt() != -1)
			ocop.setPayMethodInt(param.getPayMethodInt());

		// 都是ios先
		// MobileOrderBuildVO result_vo = new MobileOrderBuildVO();
		// 判断系统，这个要改emun配置
		if (param.getOs() == 1)
			ocop.setSource(OrderFormSource.MOBILE_IOS);
		else
			ocop.setSource(OrderFormSource.MOBILE_ANDROID);
		RetArg retArg = orderFacade.composeOrder(ocop, PlatformType.MOBILE);
		OrderCalServiceGenOrderResult genOrderResult = RetArgUtil.get(retArg, OrderCalServiceGenOrderResult.class);
		FavorCaculateResultDTO fcResultDTO = RetArgUtil.get(retArg, FavorCaculateResultDTO.class);
		if (fcResultDTO == null || genOrderResult == null) {
			result.put("error", MobileErrorCode.ORDER_ERROR);
			return result;
		}
		MobileErrorCode code = genCodeForComposeOrder(genOrderResult.getResultCode().getIntValue());
		if (code != MobileErrorCode.NULL) {
			result.put("error", code);
			return result;
		}

		// 2.生成VO对象
		OrderFormCalDTO orderCalDTO = genOrderResult.getOrderFormCalDTO();
		// 移动端去掉支付宝的露出

		OrderForm1VO order1VO = MainsiteVOConvertUtil.convertToOrderForm1VO(orderCalDTO, fcResultDTO);
		result.put("orderId", orderCalDTO.getOrderId());
		result.put("orderCalDTO", orderCalDTO);
		result.put("fcResultDTO", fcResultDTO);
		if (appversion >= Converter.protocolVersion("1.2.0")) {
			// TODO
			result.put("giftMoneyTotal", Converter.doubleFormat(order1VO.getCanUseRedPackets()));
			result.put("giftCanUse", Converter.doubleFormat(order1VO.getCanOrderRedPackets()));
		} else {
			filterAlipay(orderCalDTO);
		}
		// 下面开始转化
		OrderForm2VO dto = MainsiteVOConvertUtil.convertToOrderForm2VO(orderCalDTO, returnPackageFacade);
		result.put("hash", param.getHash());
		result.put("countDownTime", dto.getPayCloseCD());
		result.put("endTime", Converter.getTime() + dto.getPayCloseCD());
		MobileOrderPriceVO price = getPrice(dto, appversion);

		if (result.get("giftCanUse") != null) {
			price.setGiftCanUse(Converter.doubleFormat(order1VO.getCanOrderRedPackets()));
		}

		result.put("orderPrice", price);
		if (order1VO != null)
			result.put("balance", BigDecimal.ZERO);

		if (orderCalDTO.getPaymethodArray() != null) {
			List<Integer> a = new ArrayList<Integer>();
			for (int i = 0; i < orderCalDTO.getPaymethodArray().length; i++) {
				OrderFormPayMethodDTO paydto = orderCalDTO.getPaymethodArray()[i];
				if (paydto.isValid())
					a.add(paydto.getPayMethod().getIntValue());
			}
			result.put("payChannel", a);
		}
		OrderExpInfoVO orderExp = dto.getExpInfo();
		if (orderExp != null) {
			MobileConsigneeAddressVO address = new MobileConsigneeAddressVO();
			address.setAddressId(param.getAddressId());
			address.setAddressSuffix(orderExp.getAddress());
			address.setCityName(orderExp.getCity());
			address.setDistrictName(orderExp.getSection());
			// address.setStreetId(orderExp.gets);
			address.setName(orderExp.getConsigneeName());
			address.setPhone(orderExp.getConsigneeMobile());
			address.setTel(orderExp.getConsigneeTel());
			address.setProvinceName(orderExp.getProvince());
			address.setStreetName(orderExp.getStreet());
			address.setZipcode(orderExp.getZipcode());
			result.put("address", address);
		}
		if (orderCalDTO.getExpressArray() != null) {
			List<MobileKeyPairVO> a = new ArrayList<MobileKeyPairVO>();
			for (int i = 0; i < orderCalDTO.getExpressArray().length; i++) {
				MobileKeyPairVO pair = new MobileKeyPairVO();
				pair.setId(orderCalDTO.getExpressArray()[i].getIntValue());
				pair.setName(orderCalDTO.getExpressArray()[i].getDesc());
				a.add(pair);
			}
			result.put("expressChannel", a);
		}

		if (order1VO != null && order1VO.getCouponList() != null) {
			List<MobileKeyPairVO> list = new ArrayList<MobileKeyPairVO>();
			for (OrderCoupon1VO couponVo : order1VO.getCouponList()) {
				MobileKeyPairVO pair = new MobileKeyPairVO();
				pair.setId(couponVo.getUserCouponId());
				pair.setName(couponVo.getTitle());
				list.add(pair);
			}
			result.put("coupon", list);
		}
		return result;
	}

	/**
	 * 移动端去掉支付宝的露出
	 * 
	 * @param orderCalDTO
	 */
	private void filterAlipay(OrderFormCalDTO orderCalDTO) {
		if (orderCalDTO == null)
			return;
		// 过滤支付宝
		List<OrderFormPayMethodDTO> pmDTOList = new ArrayList<>();
		for (OrderFormPayMethodDTO payMethodDTO : orderCalDTO.getPaymethodArray()) {
			if (payMethodDTO.getPayMethod() == OrderFormPayMethod.ALIPAY)
				continue;
			pmDTOList.add(payMethodDTO);
		}
		orderCalDTO.setPaymethodArray(pmDTOList.toArray(new OrderFormPayMethodDTO[0]));
		// 如果选中了支付宝,则重新设置选中的支付方式
		if (orderCalDTO.getOrderFormPayMethod() == OrderFormPayMethod.ALIPAY) {
			orderCalDTO.setOrderFormPayMethod(OrderFormPayMethod.EPAY);
		}
	}

	@Override
	public BaseJsonVO postOrder(long userId, int areaId, int os, MobileOrderCommitAO ao, String token, long appversion) {
		logger.info("addInDetailPage -> userId:<" + userId + ">,areaCode:<" + areaId + ">");
		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkZero("ADDRESS ID", ao.getAddressId());
			MobileChecker.checkNull("HASH", ao.getHash());

			if (ao.getPayChannel() == -1)
				throw new ParamNullException("pay channel is null");
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		try {
			MobileChecker.checkZero("AREA CODE", areaId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_MATCH, e1.getMessage());
		}

		try {
			CartVO vo = cartFacade.getCart(userId, areaId, null, PlatformType.MOBILE);
			if (vo == null)
				return Converter.errorBaseJsonVO(MobileErrorCode.CART_TIMEOUT, "empty cart");
			MobileCartSkuList mcl = MobileCartFacadeImpl.getSkuIdList(vo);
			String cartIds = MobileCartFacadeImpl.getSkuIds(mcl);
			String hash = Converter.genCartHash(vo.getCartInfoVO().getUpdateTime(), cartIds);
			try {
				String hashInput = ao.getHash().trim();
				if (!hash.equals(hashInput)) {
					return Converter.errorBaseJsonVO(MobileErrorCode.ORDER_HASH_ERROR);
				}
			} catch (Exception es) {
				return Converter.errorBaseJsonVO(MobileErrorCode.ORDER_HASH_ERROR);
			}
			// 取一条地址

			HashMap<String, Object> map = genOrderMap(new MobilePayOrderParam(userId, areaId, cartIds, os, hash, ao),
					appversion);
			if (map.get("error") != null)
				return Converter.errorBaseJsonVO((MobileErrorCode) map.get("error"));

			MobileOrderPriceVO orderPrice = (MobileOrderPriceVO) map.get("orderPrice");
			OrderFormCalDTO orderCalDTO = (OrderFormCalDTO) map.get("orderCalDTO");
			FavorCaculateResultDTO fcResultDTO = (FavorCaculateResultDTO) map.get("fcResultDTO");
			if (orderCalDTO == null) {
				return Converter.errorBaseJsonVO(MobileErrorCode.ORDER_ERROR);
			}
			// 2.添加订单
			RetArg retArgOfMakeOrder = orderFacade.makeOrder(orderCalDTO, fcResultDTO, cartIds);

			boolean isSucc = RetArgUtil.get(retArgOfMakeOrder, Boolean.class);
			if (!isSucc) {
				return Converter.errorBaseJsonVO(MobileErrorCode.ORDER_ERROR);
			}
			// 强制变0

			MobileOrderCompletVO ordervo = pay(orderCalDTO, ao.getPayChannel(), userId, token, appversion);
			if (orderPrice.getFinalprice() == 0) {
				ordervo.setPaySuccess(1);
				ordervo.setPayLink("");
				ordervo.setPayCloseCD(0);
			}

			return Converter.genrBaseJsonVO("orderCommit", ordervo);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.ORDER_ERROR);
		}

	}

	private MobileOrderCompletVO pay(OrderFormDTO dto, int payType, long userId, String token, long appversion)
			throws ParamNullException {
		MobileOrderCompletVO ordervo = new MobileOrderCompletVO();
		ordervo.setPaySuccess(0);
		if (dto == null) {
			return ordervo;
		}
		long orderId = dto.getOrderId();
		ordervo.setOrderId(orderId);
		List<TradeItemDTO> tradeId = tradeService.getTradeItemDTOList(orderId, userId);
		TradeItemDTO epayTrade = null;
		for (TradeItemDTO trade : tradeId) {
			if (TradeItemPayMethod.COD == trade.getTradeItemPayMethod()) {
				// 已经是货到付款的，不允许更改其他支付方式
				ordervo.setPaySuccess(1);
				return ordervo;
			}
			if (TradeItemPayMethod.isOnlinePayMethod(trade.getTradeItemPayMethod())) {
				epayTrade = trade;
				break;
			}

		}
		// 不存在 EPAY的支付方式
		if (epayTrade == null)
			return ordervo;
		// 货到付款也不能切回去,除了epay未支付
		if (epayTrade.getPayState() != PayState.ONLINE_NOT_PAY) {
			// ordervo.setPaySuccess(1);
			return ordervo;
		}

		boolean isSuc = true;
		if (OrderFormPayMethod.isOnlinePayMethod(OrderFormPayMethod.genEnumByIntValueSt(payType))) {
			int pay = 0;
			if (payType == OrderFormPayMethod.ALIPAY.getIntValue()) {
				pay = TradeItemPayMethod.ALIPAY.getIntValue();
			} else if (payType == OrderFormPayMethod.EPAY.getIntValue()) {
				pay = TradeItemPayMethod.EPAY.getIntValue();
			}

			MobileInfoDTO mobileInfoDTO = mobileInfoService.findMobileInfoByInitIdAndToken(token);
			if (mobileInfoDTO == null) {
				ordervo.setPayLink(Converter.genPayLink(epayTrade.getTradeId(), epayTrade.getSpSource(), "", "", pay));
			} else {
				String initId = mobileInfoDTO.getInitId();
				String ursToken = mobileInfoDTO.getUrsToken();
				ordervo.setPayLink(Converter.genPayLink(epayTrade.getTradeId(), epayTrade.getSpSource(), initId,
						ursToken, pay));
			}
			long currTime = System.currentTimeMillis(), payCloseCD = 0;
			if (dto.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME > currTime) {
				payCloseCD = dto.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME - currTime;
				payCloseCD = payCloseCD <= 0 ? 0 : payCloseCD;
				ordervo.setPayCloseCD(payCloseCD);
			}
		} else if (payType == OrderFormPayMethod.COD.getIntValue()) {
			// 改为货到付款
			isSuc = orderService.changePaymethodToCOD(userId, orderId) > 0;
		} else {
			throw new ParamNullException("pay type not support");
		}
		ordervo.setPaySuccess(isSuc ? 1 : 0);
		/**
		 * 配置分享红包,
		 */
		// if (appversion >= Converter.protocolVersion("1.2.0"))
		// ordervo.setShareGift(shareGiftF(orderId));
		// 只有在线支付即时饭红包
		if (OrderFormPayMethod.isOnlinePayMethod(OrderFormPayMethod.genEnumByIntValueSt(payType))) {
			RetArg rebate = rebateService.rebateAndReturnObjcet(userId, orderId);
			boolean rebateSuccess = RetArgUtil.get(rebate, Boolean.class);
			if (rebateSuccess) {
				CouponOrder couponOrder = RetArgUtil.get(rebate, CouponOrder.class);
				RedPacket redPacket = RetArgUtil.get(rebate, RedPacket.class);
				if (couponOrder != null)
					ordervo.setCouponInfo("获得返券");
				if (redPacket != null && !redPacket.isShare() && appversion >= Converter.protocolVersion("1.2.0"))
					ordervo.setGiftInfo("获得" + Converter.doubleFormat(redPacket.getCash()) + "元红包");

			}
		}
		logger.warn("user : " + userId + ",orderid: " + orderId + ",money:"
				+ (dto.getCartRPrice().doubleValue() + dto.getExpUserPrice().doubleValue()));
		return ordervo;

	}

	@Override
	public BaseJsonVO changePay(long userId, int payChannel, long orderId, String token, long appversion) {
		logger.info("addInDetailPage -> userId:<" + userId + ">,orderId:<" + orderId + ">");
		try {
			MobileChecker.checkZero("USER ID", userId);
			MobileChecker.checkZero("ODEDER ID", orderId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}

		try {
			OrderFormDTO dto = orderFacade.queryOrderForm(userId, orderId, true);
			if (dto == null)
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL);
			OrderForm2VO formVo = MainsiteVOConvertUtil.convertToOrderForm2VO(dto, returnPackageFacade);
			if (formVo.getPayCloseCD() <= 0) {
				return Converter.errorBaseJsonVO(MobileErrorCode.ORDER_TIMEOUT);
			}
			MobileOrderCompletVO ordervo = new MobileOrderCompletVO();
			ordervo.setPaySuccess(0);
			if (dto.getOrderFormState() == OrderFormState.WAITING_PAY) {
				ordervo = pay(dto, payChannel, userId, token, appversion);
			} else {
				return Converter.errorBaseJsonVO(MobileErrorCode.PARAM_NO_MATCH, "订单状态不能更改支付方式");
			}

			return Converter.genrBaseJsonVO("orderCommit", ordervo);
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR, e1.getMessage());
		}

	}

	@Override
	public boolean paySuccess(long tradeId) {
		logger.info("paySuccess -> tradeId:<" + tradeId + ">");
		try {
			TradeItemDTO trade = tradeService.getTradeItemDTO(tradeId);
			if (trade.getPayState() != PayState.ONLINE_PAYED) {
				return false;
			}

			// OrderServiceSetStateToEPayedParam param = new
			// OrderServiceSetStateToEPayedParam();
			// param.setOrderId(trade.getOrderId());
			// param.setOrderSn(trade.getOrderSn());
			// param.setPayOrderSn(trade.getPayOrderSn());
			// param.setTradeId(trade.getTradeId());
			// param.setUserId(trade.getUserId());
			// int ret = orderService.setStateToEPayed(param);
			// if (ret < 1)
			// return false;
			return true;
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return false;
		}
	}

	/**
	 * TODO 未完成的接口
	 * 
	 * @return
	 */
	public MobileShareVO shareGiftF(long orderId) {
		// TODO 红包RedPacketShareRecordService.getRedPacketShareId
		String redId = null;
		try {
			redId = redPacketShareRecordService.getRedPacketShareId(ShareChannel.BY_ORDER, orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtils.isNotBlank(redId)) {
			MobileShareVO shareGift = Converter.shareGiftTemplate(Converter.genWebSiteLink(Converter.SHARE_GIFT, redId,
					null));
			return shareGift;
		}
		return null;
	}
}
