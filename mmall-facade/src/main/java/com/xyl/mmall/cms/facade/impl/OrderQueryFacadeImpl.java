package com.xyl.mmall.cms.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusinessConditionDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.OrderQueryFacade;
import com.xyl.mmall.cms.facade.param.FrontOrderExpInfoUpdateParam;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.vo.OrderOperateLogVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.OrderCategory;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.TimeRangeCategory;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.UserCategory;
import com.xyl.mmall.cms.vo.order.InvoiceListVO;
import com.xyl.mmall.cms.vo.order.OrderBriefInfoListVO;
import com.xyl.mmall.cms.vo.order.OrderBriefInfoListVO.OrderBriefInfo;
import com.xyl.mmall.cms.vo.order.OrderDetailInfoVO;
import com.xyl.mmall.cms.vo.order.OrderPackageExpInfoVO;
import com.xyl.mmall.cms.vo.order.OrderPackageSkuInfoVO;
import com.xyl.mmall.cms.vo.order.TradeDetailInfoVO;
import com.xyl.mmall.cms.vo.order.TradeOrderInfo;
import com.xyl.mmall.cms.vo.order.TradeOrderInfoListVO;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.facade.ReturnPackageCommonFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.util.UrlBaseUtil;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.mainsite.util.MainsiteVOConvertUtil;
import com.xyl.mmall.mainsite.vo.order.OrderExpInfoVO;
import com.xyl.mmall.mainsite.vo.order.OrderFormVO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.service.OmsOrderFormService;
import com.xyl.mmall.oms.service.OmsOrderPackageService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderOperateLogDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.InvoiceInOrdState;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.order.service.ConsigneeAddressService;
import com.xyl.mmall.order.service.InvoiceService;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.order.service.ReturnPackageUpdateService;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午9:24:08
 *
 */
@Facade("OrderQueryFacade")
public class OrderQueryFacadeImpl implements OrderQueryFacade {

	private static final Logger logger = Logger.getLogger(OrderQueryFacadeImpl.class);

	@Resource
	private OrderService orderService;

	@Resource
	private OrderFacade orderFacade;

	@Resource
	private TradeService tradeService;

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private PromotionFacade promotionFacade;

	@Resource
	private CouponOrderService couponOrderService;

	@Resource
	private CouponService couponService;

	@Resource
	private InvoiceService invoiceService;

	@Resource
	private ConsigneeAddressService consigneeAddressService;

	@Resource
	private UserProfileService userProfileService;

	@Resource
	private BusinessService businessService;

	@Resource
	private WarehouseService warehouseService;

	@Resource
	private OrderBriefService orderBriefService;

	@Resource
	private OmsOrderPackageService omsOrderPackageService;

	@Resource
	private OrderPackageSimpleService ordPkgSimpleService;

	@Resource
	private ReturnPackageQueryService retPkgQueryService;

	@Resource
	private ReturnPackageUpdateService retPkgUpdateService;

	@Resource
	private ReturnPackageCommonFacade retPkgCommonFacade;

	@Autowired
	private OmsOrderFormService omsOrderFormService;

	@Resource
	private AgentService agentService;

	@Resource
	private DealerService dealerService;

	/**
	 * 根据UserType获取用户id
	 * 
	 * @param ut
	 * @return
	 */
	private List<Long> getUserIdListByUserInfo(UserCategory ut, String value) {
		Set<Long> userIdSet = new HashSet<Long>();
		if (UserCategory.CONSIGNEE_MOBILE == ut) {
			List<ConsigneeAddressDTO> addressList = consigneeAddressService.queryUserIdByConsigneeMobile(value,
					Integer.MAX_VALUE, 0);
			if (CollectionUtils.isNotEmpty(addressList)) {
				for (ConsigneeAddressDTO address : addressList) {
					userIdSet.add(address.getUserId());
				}
			}
		} else {
			Map<Integer, String> searchParams = new HashMap<>();
			// ugly code: Convert search type
			searchParams.put(ut.getIntValue() + 1, value);
			List<UserProfileDTO> userList = userProfileService.searchUserByParams(searchParams, Integer.MAX_VALUE, 0);
			if (CollectionUtils.isNotEmpty(userList)) {
				for (UserProfileDTO user : userList) {
					userIdSet.add(user.getUserId());
				}
			}
		}
		List<Long> userIdList = new ArrayList<Long>(userIdSet.size());
		for (Long userId : userIdSet) {
			userIdList.add(userId);
		}
		return userIdList;
	}

	/**
	 * poId -> brandName
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	private Map<Long, String> getInvoiceBrandNames(OrderFormDTO ordFormDTO) {
		Map<Long, String> ret = new HashMap<Long, String>();
		List<PODTO> poList = orderFacade.getPOList(ordFormDTO);
		for (PODTO po : poList) {
			ScheduleDTO schedDTO = po.getScheduleDTO();
			Schedule sched = null;
			if (null == schedDTO || null == (sched = schedDTO.getSchedule())) {
				continue;
			}
			long supplierId = sched.getSupplierId();
			String brandName = sched.getBrandName();
			ret.put(supplierId, brandName);
		}
		return ret;
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

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#getCmsOrderQueryTypeList()
	 */
	@Override
	public OrderQueryCategoryListVO getCmsOrderQueryTypeList() {
		return OrderQueryCategoryListVO.getInstance();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#queryTrade(long, long,
	 *      long, String)
	 */
	@Override
	public TradeDetailInfoVO queryTrade(long tradeId, long orderId, long userId, String userName) {
		TradeDetailInfoVO ret = new TradeDetailInfoVO();
		TradeItemDTO tradeItem = tradeService.getTradeItemDTO(tradeId, userId);
		List<TradeItemDTO> allTradesInSameOrder = tradeService.getTradeItemDTOList(orderId, userId);
		return ret.fillWihtInfos(tradeItem, allTradesInSameOrder, userName);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#queryByUserInfo(com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.UserCategory,
	 *      java.lang.String, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OrderBriefInfoListVO queryByUserInfo(UserCategory ut, String value, int limit, int offset) {
		OrderBriefInfoListVO ret = new OrderBriefInfoListVO();
		DDBParam ddbParam = new DDBParam("orderTime", false, limit, offset);
		List<Long> userIdList = this.getUserIdListByUserInfo(ut, value);
		if (null == userIdList || 0 == userIdList.size()) {
			return ret;
		}
		RetArg retArg = orderBriefService.queryOrderFormBriefByState(userIdList, null, OrderFormState.values(),
				ddbParam);
		List<OrderFormBriefDTO> orderList = RetArgUtil.get(retArg, ArrayList.class);
		if (null == orderList) {
			return ret;
		}
		List<OrderBriefInfo> voList = new ArrayList<OrderBriefInfo>(orderList.size());
		for (OrderFormBriefDTO ord : orderList) {
			voList.add(OrderBriefInfo.orderDTO2VO(ord));
		}
		ret.setList(voList);
		DDBParam ddbRemoteParam = RetArgUtil.get(retArg, DDBParam.class);
		if (null != ddbRemoteParam && null != ddbRemoteParam.getTotalCount()) {
			ret.setTotal(ddbRemoteParam.getTotalCount());
		}
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#queryByTimeRange(com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.TimeRangeCategory,
	 *      long, long, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OrderBriefInfoListVO queryByTimeRange(TimeRangeCategory tr, long start, long end, int limit, int offset) {
		OrderBriefInfoListVO ret = new OrderBriefInfoListVO();
		DDBParam ddbParam = new DDBParam("orderTime", false, limit, offset);
		RetArg retArg = null;
		if (TimeRangeCategory.NOT_LACK == tr) {
			retArg = orderBriefService.queryOrderFormBriefByStateWithTimeRange(start, end, false, null, ddbParam);
		}
		// else {
		// retArg =
		// orderBriefService.queryOrderFormBriefByStateWithTimeRange(start, end,
		// true, null, ddbParam);
		// }
		List<OrderFormBriefDTO> orderList = RetArgUtil.get(retArg, ArrayList.class);
		if (null == orderList) {
			return ret;
		}
		List<OrderBriefInfo> voList = new ArrayList<OrderBriefInfo>(orderList.size());
		for (OrderFormBriefDTO ord : orderList) {
			voList.add(OrderBriefInfo.orderDTO2VO(ord));
		}
		ret.setList(voList);
		DDBParam ddbRemoteParam = RetArgUtil.get(retArg, DDBParam.class);
		if (null != ddbRemoteParam && null != ddbRemoteParam.getTotalCount()) {
			ret.setTotal(ddbRemoteParam.getTotalCount());
		}
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#orderExists(com.xyl.mmall.cms.vo.order.OrderCategory,
	 *      java.lang.String)
	 */
	@Override
	public boolean orderExists(OrderCategory ott, String value) {
		if (null == ott || null == value) {
			return false;
		}
		return (null != getOrderIdByOrderTrade(ott, value));
	}

	/**
	 * 根据OrderTradeType获取订单id列表
	 * 
	 * @param ott
	 * @param value
	 * @return
	 */
	private OrderFormDTO getOrderIdByOrderTrade(OrderCategory ott, String value) {
		OrderFormDTO ordDTO = null;
		switch (ott) {
		case ORDER_ID:
			try {
				long orderId = Long.parseLong(value.trim());
				ordDTO = orderService.queryOrderFormByOrderId(orderId);
				return ordDTO;
			} catch (NumberFormatException e) {
				return null;
			}
		case MAIL_NO:// 逻辑废弃 bylhp
			// to be continued：这里在业务逻辑上是否有问题？
			List<OrderFormDTO> ordList = orderService.queryOrderFormListByMailNO(value, null, null);
			if (null == ordList || 0 == ordList.size()) {
				return null;
			}
			ordDTO = ordList.get(0);
			return ordDTO;
		case PACKAGE_ID: // 逻辑废弃 bylhp
			try {
				long orderPkgId = Long.parseLong(value.trim());
				OrderPackageSimpleDTO ordPkg = ordPkgSimpleService.queryOrderPackageSimple(orderPkgId);
				if (null == ordPkg) {
					return null;
				}
				return orderService.queryOrderForm(ordPkg.getUserId(), ordPkg.getOrderId(), null);
			} catch (NumberFormatException e) {
				return null;
			}
		default:
			return null;
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#queryOrderDetailInfo(com.xyl.mmall.cms.vo.order.OrderCategory,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public OrderDetailInfoVO queryOrderDetailInfo(OrderCategory ott, String value) {
		OrderDetailInfoVO ret = new OrderDetailInfoVO();
		OrderFormDTO ordDTO = getOrderIdByOrderTrade(ott, value);
		if (null == ordDTO) {
			return ret;
		}
		long orderId = ordDTO.getOrderId();
		long userId = ordDTO.getUserId();
		// 0. 回填数据：基本信息 + 配送信息
		// couponOrder: 订单关联的优惠券
		CouponOrder couponOrder = couponOrderService.getCouponOrderByOrderIdOfUseType(userId, orderId);
		// coupon：根据优惠券Code反查出优惠券
		Coupon coupon = null;
		if (null != couponOrder) {
			coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
			ret.getBasicInfo().setCouponSPrice(ordDTO.getCouponDiscount());
		}
		// orderProm: 订单关联的促销 (策划说orderProm与poProms只能有一个存在)
		PromotionDTO orderProm = promotionFacade.getPromotionById(ordDTO.getPromotionId());
		// poProms: po关联的促销 (策划说orderProm与poProms只能有一个存在)
		Map<Long, PromotionDTO> poProms = orderFacade.extractPromotion(ordDTO);
		String userName = "";
		UserProfileDTO up = userProfileService.findUserProfileById(userId);
		if (null != up) {
			userName = up.getUserName();
		}
		ret.getBasicInfo().fillUserInfo(userId, userName, up.getNickName());
		OrderCancelInfoDTO cancelInfo = orderService.getOrderCancelInfo(userId, orderId);
		List<Long> areaIdList = orderFacade.getSaleAreaIdList(ordDTO);
		List<AreaDTO> areaList = businessService.getAreadByIdList(areaIdList);
		List<String> warehouseList = new ArrayList<String>();
		List<OrderPackageDTO> ordPkgList = ordDTO.getOrderPackageDTOList();
		if (!CollectionUtil.isEmptyOfList(ordPkgList)) {
			for (OrderPackageDTO ordPkg : ordPkgList) {
				if (null == ordPkg) {
					continue;
				}
				String whForm = ordPkg.getWarehouseName();
				if (null != whForm) {
					warehouseList.add(whForm);
				}
			}
		}
		boolean canCancel = ordDTO.canCancel();
		ReturnPackageState[] stateArray = new ReturnPackageState[] { ReturnPackageState.FINALLY_COD_RETURNED_TO_USER,
				ReturnPackageState.FINALLY_RETURNED_TO_USER };
		List<ReturnPackageDTO> retPkgList = retPkgQueryService.queryReturnPackageByUserIdAndOrderIdWithState(userId,
				orderId, false, stateArray, null);
		// 1. 订单基本信息
		ret.getBasicInfo().fillOrderInfo(ordDTO, cancelInfo, areaList, warehouseList, retPkgList, canCancel);
		ret.getBasicInfo().fillCouponPromotionInfo(couponOrder, coupon, orderProm, poProms);
		ret.getDeliveryInfo().fillWithOrderExpInfo(ordDTO.getOrderExpInfoDTO(),
				orderService.canOrderExpInfoBeChanged(ordDTO));
		/* -------delete start ----------- */
		// TODO
		RetArg retArg = invoiceService.getInvoiceByOrderId(orderId, userId);
		List<InvoiceInOrdSupplierDTO> invoiceList = null;
		InvoiceInOrdDTO invoiceInOrd = null;
		if (null != retArg) {
			invoiceList = RetArgUtil.get(retArg, ArrayList.class);
			invoiceInOrd = RetArgUtil.get(retArg, InvoiceInOrdDTO.class);
		}
		ret.getInvoce().fillInvoice(null != invoiceInOrd, invoiceInOrd, invoiceList);
		/* -------delete end ----------- */
		// 2.回填发票
		ret.setInvoiceInOrdVOs(MainsiteVOConvertUtil.convertToInvoiceInOrdVOs(ordDTO.getInvoiceDTOs()));
		// 3.回填物流
		ret.setOrderLogisticsVOs(MainsiteVOConvertUtil.convertToOrderLogistics(ordDTO.getOrderLogisticsDTOs()));
		// 4.设置店铺信息
		BusinessDTO businessDTO = businessService.getBreifBusinessById(ordDTO.getBusinessId(), -1);
		buildStoreInfo(ordDTO.getOrderCartItemDTOList(), businessDTO);
		ret.getBasicInfo().fillBusinessInfo(businessDTO);
		// 5.转化购物车Vo
		ret.setCartList(MainsiteVOConvertUtil.convertToOrderCartItemVOList(ordDTO.getOrderCartItemDTOList(), true));
		// 6. 回填数据：交易信息
		List<TradeItemDTO> tradeItemDTOList = tradeService.getTradeItemDTOList(orderId, userId);
		TradeItemDTO hbFakeTradeItem = convertHBToFakeTradeItem(ordDTO);
		ret.fillTradeList(ordDTO, tradeItemDTOList, hbFakeTradeItem);
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#queryTradeOrderInfo(long,
	 *      long)
	 */
	@Override
	public TradeOrderInfoListVO queryTradeOrderInfo(long userId, long orderId) {
		TradeOrderInfoListVO vo = new TradeOrderInfoListVO();
		OrderFormDTO ordForm = orderService.queryOrderForm(userId, orderId, null);
		if (null == ordForm) {
			return vo;
		}
		TradeOrderInfo ret = new TradeOrderInfo();
		ret.fillOrder(ordForm);
		vo.getList().add(ret);
		vo.setTotal(1 + vo.getTotal());
		return vo;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#queryOrderPackageExpInfo(com.xyl.mmall.cms.vo.order.OrderCategory,
	 *      java.lang.String)
	 */
	@Override
	public OrderPackageExpInfoVO queryOrderPackageExpInfo(OrderCategory ott, String value) {
		OrderFormDTO ordDTO = getOrderIdByOrderTrade(ott, value);
		if (null == ordDTO) {
			return null;
		}
		OrderPackageExpInfoVO ret = new OrderPackageExpInfoVO();
		Map<Long, Boolean> canReopenReturnJudge = retPkgCommonFacade.canReopenReturnShowToKF(ordDTO);
		List<ReturnPackageDTO> retPkgList = retPkgQueryService.queryReturnPackageByUserIdAndOrderId(ordDTO.getUserId(),
				ordDTO.getOrderId(), false, null);
		Map<Long, ReturnPackageDTO> retPkgMap = new HashMap<Long, ReturnPackageDTO>();
		if (!CollectionUtil.isEmptyOfList(retPkgList)) {
			for (ReturnPackageDTO retPkg : retPkgList) {
				if (null == retPkg) {
					continue;
				}
				retPkgMap.put(retPkg.getOrderPkgId(), retPkg);
			}
		}
		Map<Long, OmsOrderPackage> omsPkgMap = new HashMap<Long, OmsOrderPackage>();
		List<OrderPackageDTO> ordPkgList = ordDTO.getOrderPackageDTOList();
		if (!CollectionUtil.isEmptyOfList(ordPkgList)) {
			for (OrderPackageDTO ordPkg : ordPkgList) {
				if (null == ordPkg) {
					continue;
				}
				OmsOrderPackage omsPkg = omsOrderPackageService.getOmsOrderPackageByExpress(ordPkg.getMailNO(),
						ordPkg.getExpressCompanyReturn());
				if (null != omsPkg) {
					omsPkgMap.put(ordPkg.getPackageId(), omsPkg);
				}
			}
		}
		ret.fillWithOrderFormDTO(ordDTO, canReopenReturnJudge, retPkgMap, omsPkgMap);
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#queryOrderPackageSkuInfo(com.xyl.mmall.cms.vo.order.OrderCategory,
	 *      java.lang.String)
	 */
	@Override
	public OrderPackageSkuInfoVO queryOrderPackageSkuInfo(OrderCategory ott, String value) {
		OrderFormDTO ordDTO = getOrderIdByOrderTrade(ott, value);
		if (null == ordDTO) {
			return null;
		}
		OrderPackageSkuInfoVO ret = new OrderPackageSkuInfoVO();
		ret.fillWithOrderFormDTO(ordDTO, orderFacade.getProducts(ordDTO));
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#updateOrderExpInfo(com.xyl.mmall.cms.facade.param.FrontOrderExpInfoUpdateParam)
	 */
	@Override
	public int updateOrderExpInfo(FrontOrderExpInfoUpdateParam param) {
		if (null == param || null == param.getChgParam()) {
			return -1;
		}
		return orderService.updateOrderExpInfo(param.getOrderId(), param.getUserId(), param.getChgParam());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#cancelOrder(long, long,
	 *      OrderCancelRType)
	 */
	@Override
	public boolean cancelOrder(long orderId, long userId, OrderCancelRType rtype) {
		if (null == rtype) {
			return false;
		}
		OrderCancelInfoDTO cancelDTO = new OrderCancelInfoDTO();
		cancelDTO.setOrderId(orderId);
		cancelDTO.setUserId(userId);
		cancelDTO.setCtime(System.currentTimeMillis());
		// to be continued: 客服取消 (交互稿里面没有)
		cancelDTO.setReason("");
		cancelDTO.setRtype(rtype);
		cancelDTO.setCancelSource(OrderCancelSource.KF);
		CancelOmsOrderTaskDTO cancelTaskDTO = new CancelOmsOrderTaskDTO(cancelDTO);
		// 1.调用取消订单的服务
		RetArg retArg = orderFacade.addCancelOmsOrderTask(cancelTaskDTO);
		if (null == retArg) {
			return false;
		}
		Boolean result = RetArgUtil.get(retArg, Boolean.class);
		if (null == result) {
			return false;
		}
		return result.booleanValue();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#addInvoice(long, long,
	 *      java.lang.String, boolean)
	 */
	@Override
	public boolean addInvoice(long orderId, long userId, String title, boolean associated) {
		OrderFormDTO ordFormDTO = orderService.queryOrderForm(userId, orderId, null);
		if (null == ordFormDTO) {
			return false;
		}
		InvoiceInOrdDTO invoiceInOrd = new InvoiceInOrdDTO();
		invoiceInOrd.setOrderId(orderId);
		invoiceInOrd.setUserId(userId);
		invoiceInOrd.setTitle(title);
		invoiceInOrd.setOmsTime(ordFormDTO.getOmsTime());
		// to be continued：producedTime 全部订单生产结束时间
		invoiceInOrd.setProducedTime(0);
		invoiceInOrd.setState(InvoiceInOrdState.INIT);
		return invoiceService.addInvoiceInOrd(invoiceInOrd);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#updateInvoice(long, long,
	 *      java.lang.String, boolean)
	 */
	@Override
	public boolean updateInvoice(long orderId, long userId, String title, boolean associated) {
		InvoiceInOrdDTO invoice = invoiceService.getInvoiceInOrdByOrderId(orderId, userId);
		if (null == invoice) {
			return false;
		}
		invoice.setTitle(title);
		return invoiceService.updateInvoiceInOrd(invoice);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#getInvoiceList(long, long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public InvoiceListVO getInvoiceList(long orderId, long userId) {
		InvoiceListVO vo = new InvoiceListVO();
		RetArg retArg = invoiceService.getInvoiceByOrderId(orderId, userId);
		List<InvoiceInOrdSupplierDTO> invoiceList = null;
		if (null != retArg) {
			invoiceList = RetArgUtil.get(retArg, ArrayList.class);
		}
		OrderFormDTO ordDTO = orderService.queryOrderForm(userId, orderId, null);
		Map<Long, String> brandNames = getInvoiceBrandNames(ordDTO);
		vo.fillInvoiceList(invoiceList, brandNames);
		return vo;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#kfReopenReturn(long, long)
	 */
	@Override
	public RetArg kfReopenReturn(long ordPkgId, long userId) {
		RetArg retArg = new RetArg();
		OrderPackageSimpleDTO ordPkg = ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
		if (null == ordPkg) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no OrderPackage for [ordPkgId:" + ordPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		long earliestPOEndTime = retPkgCommonFacade.getEarliestPOEndTime(ordPkg);
		boolean isSucc = ordPkgSimpleService.reOpenReturn(userId, ordPkgId, earliestPOEndTime);
		if (!isSucc) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "failed");
		} else {
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful");
		}
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#setPackageLost(long, long)
	 */
	@Override
	public RetArg setPackageLost(long ordPkgId, long userId) {
		RetArg retArg = new RetArg();
		OrderPackageSimpleDTO ordPkg = ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
		if (null == ordPkg) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no OrderPackage for [ordPkgId:" + ordPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		String expressCompany = ordPkg.getExpressCompanyReturn();
		String mailNO = ordPkg.getMailNO();
		boolean isSucc = false;
		try {
			isSucc = omsOrderPackageService.setLostPackage(mailNO, expressCompany);
		} catch (Exception e) {
			logger.warn("omsOrderPackageService.setLostPackage(...) throws Exception for [ordPkgId:" + ordPkgId
					+ ", userId:" + userId + "]", e);
			isSucc = false;
		}
		if (!isSucc) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "omsOrderPackageService.setLostPackage(...) failed for [ordPkgId:" + ordPkgId
					+ ", userId:" + userId + "]");
			return retArg;
		}
		isSucc = ordPkgSimpleService.setPackageToCancelLost(ordPkgId, userId);
		if (!isSucc) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "failed");
		} else {
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful");
		}
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#setPackageRefused(long,
	 *      long)
	 */
	@Override
	public RetArg setPackageRefused(long ordPkgId, long userId) {
		RetArg retArg = new RetArg();
		OrderPackageSimpleDTO ordPkg = ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
		if (null == ordPkg) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no OrderPackage for [ordPkgId:" + ordPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		String expressCompany = ordPkg.getExpressCompanyReturn();
		String mailNO = ordPkg.getMailNO();
		boolean isSucc = false;
		try {
			isSucc = omsOrderPackageService.setRejectPackage(mailNO, expressCompany);
		} catch (Exception e) {
			logger.warn("rejectPackageService.agreeRejectPackage(...) thorws Exception for [ordPkgId:" + ordPkgId
					+ ", userId:" + userId + "]", e);
			isSucc = false;
		}
		if (!isSucc) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "rejectPackageService.agreeRejectPackage(...) failed for [ordPkgId:" + ordPkgId
					+ ", userId:" + userId + "]");
			return retArg;
		}
		isSucc = ordPkgSimpleService.setPackageToCancelSR(ordPkgId, userId);
		if (!isSucc) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "ordPkgSimpleService.setPackageToCancelSR(...) failed for [ordPkgId:" + ordPkgId
					+ ", userId:" + userId + "]");
		} else {
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful");
		}
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#cancelPackage(long, long)
	 */
	@Override
	public RetArg cancelPackage(long ordPkgId, long userId) {
		RetArg retArg = new RetArg();
		boolean isSucc = ordPkgSimpleService.setPackageToCancelOT(ordPkgId, userId);
		if (!isSucc) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "failed");
		} else {
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful");
		}
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OrderQueryFacade#consignPackage(long, long)
	 */
	@Override
	public RetArg consignPackage(long ordPkgId, long userId) {
		RetArg retArg = new RetArg();
		OrderPackageSimpleDTO ordPkg = ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
		if (null == ordPkg) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no OrderPakcage for [userId:" + userId + ", ordPkgId:" + ordPkgId + "]");
			return retArg;
		}
		boolean isSucc = true;
		try {
			isSucc = ordPkgSimpleService.setPackageToSignIn(ordPkg.getMailNO(), ordPkg.getExpressCompanyReturn());
		} catch (Exception e) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, e.getMessage());
			return retArg;
		}
		if (!isSucc) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "failed");
		} else {
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful");
		}
		return retArg;
	}

	@Override
	public BasePageParamVO<OrderFormVO> searchOrderForm(OrderSearchParam searchParam) {
		RetArg retArg = null;
		BasePageParamVO<OrderFormVO> resultVO = new BasePageParamVO<OrderFormVO>();
		if (searchParam.getQueryType() == 6) {// 按用户名查询
			List<Long> userIdList = this.getUserIdListByUserInfo(UserCategory.USER_NAME, searchParam.getUserName());
			if (null == userIdList || 0 == userIdList.size()) {
				return resultVO;
			}
			searchParam.setUserIdList(userIdList);
		}
		retArg = orderBriefService.queryOrderFormBriefByOrderSearchParam(searchParam);
		@SuppressWarnings("unchecked")
		List<OrderFormBriefDTO> orderList = RetArgUtil.get(retArg, ArrayList.class);
		if (null == orderList) {
			return resultVO;
		}
		List<OrderFormVO> orderFormVOs = new ArrayList<OrderFormVO>(orderList.size());
		for (OrderFormBriefDTO ord : orderList) {
			BusinessDTO businessDTO = businessService.getBreifBusinessById(ord.getBusinessId(), -1);
			ord.setBusinessAccount(businessDTO == null ? "" : businessDTO.getBusinessAccount());
			UserProfileDTO userProfileDTO = userProfileService.findUserProfileById(ord.getUserId());
			ord.setUserName(userProfileDTO == null ? "" : userProfileDTO.getUserName());
			OrderFormVO orderFormVO = new OrderFormVO(ord);
			orderFormVOs.add(orderFormVO);
		}
		resultVO.setList(orderFormVOs);
		OrderSearchParam searchParamResult = RetArgUtil.get(retArg, OrderSearchParam.class);
		resultVO.setTotal(searchParamResult.getTotalCount());
		resultVO.setHasNextPage(searchParamResult.isHasNext());
		return resultVO;
	}

	@Override
	public RetArg queryOrderFormByOrderId(long orderId) {
		RetArg retArg = new RetArg();
		OrderFormDTO orderFormDTO = orderService.queryOrderFormByOrderId(orderId);
		if (orderFormDTO == null) {
			return retArg;
		}
		BusinessDTO businessDTO = businessService.getBreifBusinessById(orderFormDTO.getBusinessId(), -1);
		buildStoreInfo(orderFormDTO.getOrderCartItemDTOList(), businessDTO);
		if (orderFormDTO.getOrderFormState().equals(OrderFormState.CANCEL_ED)) {
			OrderCancelInfoDTO orderCancelInfoDTO = orderService.getOrderCancelInfo(orderFormDTO.getUserId(), orderId);
			if (orderCancelInfoDTO != null) {
				orderFormDTO.setCancelReason(orderCancelInfoDTO.getReason());
				orderFormDTO.setCancelTime(orderCancelInfoDTO.getCtime());
			}
		}
		List<TradeItemDTO> tradeItemDTOList = tradeService.getTradeItemDTOList(orderId, orderFormDTO.getUserId());
		orderFormDTO.setPayOrderSn(tradeItemDTOList.get(0).getPayOrderSn());
		RetArgUtil.put(retArg, orderFormDTO);
		CouponOrder couponOrder = couponOrderService
				.getCouponOrderByOrderIdOfUseType(orderFormDTO.getUserId(), orderId);
		// coupon：根据优惠券Code反查出优惠券
		Coupon coupon = null;
		if (null != couponOrder) {
			coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
		}
		RetArgUtil.put(retArg, new CouponDTO(coupon));
		return retArg;
	}

	@Override
	public boolean addInvoiceInBusiness(long orderId, long userId, long businesssId, String invoiceNo, long operator) {
		InvoiceInOrdDTO invoice = invoiceService.getInvoiceInOrdByOrderId(orderId, userId);
		if (null == invoice) {
			return false;
		}
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		invoiceDTO.setInvoiceNo(invoiceNo);
		invoiceDTO.setCreateBy(operator);
		invoiceDTO.setUpdateBy(operator);
		invoiceDTO.setBusinessId(businesssId);
		invoiceDTO.setState(InvoiceInOrdState.KP_ING);
		invoiceDTO.setTitle(invoice.getTitle());
		invoiceDTO.setUserId(userId);
		invoiceDTO.setOrderId(orderId);
		return invoiceService.addInvoice(invoiceDTO);
	}

	@Override
	public boolean updateInvoiceStateInBusiness(InvoiceDTO invoiceDTO) {
		return invoiceService.updateState(invoiceDTO);
	}

	@Override
	public List<OrderOperateLogVO> queryOperateLog(OrderOperateLogDTO operateLogDTO, String startTime, String endTime) {
		List<OrderOperateLogDTO> operateLogDTOList = orderService.queryOperateLog(operateLogDTO, startTime, endTime);
		return convertToVOList(operateLogDTOList);
	}

	private List<OrderOperateLogVO> convertToVOList(List<OrderOperateLogDTO> operateLogDTOList) {
		if (CollectionUtils.isEmpty(operateLogDTOList)) {
			return null;
		}
		List<OrderOperateLogVO> retList = new ArrayList<OrderOperateLogVO>(operateLogDTOList.size());
		for (OrderOperateLogDTO operateLogDTO : operateLogDTOList) {
			OrderOperateLogVO logVO = new OrderOperateLogVO(operateLogDTO);
			int operatorType = operateLogDTO.getOperatorType();
			if (operatorType == 1) {
				AgentDTO agent = agentService.findAgentById(operateLogDTO.getOperatorId());
				logVO.setOperator(agent == null ? "" : agent.getRealName());
			} else if (operatorType == 2) {
				DealerDTO dealer = dealerService.findDealerById(operateLogDTO.getOperatorId());
				logVO.setOperator(dealer == null ? "" : dealer.getName());
			} else if (operatorType == 3) {
				UserProfileDTO userProfile = userProfileService.findUserProfileById(operateLogDTO.getOperatorId());
				logVO.setOperator(userProfile == null ? "" : userProfile.getNickName());
			} else {
				logVO.setOperator("");
			}
			retList.add(logVO);
		}
		return retList;
	}

	@Override
	public OrderBriefInfoListVO queryOrderByOrderSearchParam(OrderSearchParam searchParam) {
		OrderBriefInfoListVO ret = new OrderBriefInfoListVO();
		RetArg retArg = null;
		if (searchParam.getQueryType() == 6) {// 按用户名查询
			List<Long> userIdList = this.getUserIdListByUserInfo(UserCategory.USER_NAME, searchParam.getUserName());
			if (null == userIdList || 0 == userIdList.size()) {
				return ret;
			}
			searchParam.setUserIdList(userIdList);
		}
		if (searchParam.getQueryType() == 7) {// 按商家账号查询
			BusinessConditionDTO businessConditionDTO = new BusinessConditionDTO();
			businessConditionDTO.setAccount(searchParam.getBusinessAccount());
			businessConditionDTO.setLimit(1000);// 1000足够取出所有商家
			BasePageParamVO<BusinessDTO> buBasePageParamVO = businessService
					.getBusinessListByBusinessCondition(businessConditionDTO);
			if (null == buBasePageParamVO || CollectionUtil.isEmptyOfList(buBasePageParamVO.getList())) {
				return ret;
			}
			List<Long> businessIdList = new ArrayList<Long>();
			for (BusinessDTO businessDTO : buBasePageParamVO.getList()) {
				businessIdList.add(businessDTO.getId());
			}
			searchParam.setBusinessIdList(businessIdList);
		}
		retArg = orderBriefService.queryOrderFormBriefByOrderSearchParam(searchParam);
		@SuppressWarnings("unchecked")
		List<OrderFormBriefDTO> orderList = RetArgUtil.get(retArg, ArrayList.class);
		if (null == orderList) {
			return ret;
		}
		List<OrderBriefInfo> voList = new ArrayList<OrderBriefInfo>(orderList.size());
		for (OrderFormBriefDTO ord : orderList) {
			BusinessDTO businessDTO = businessService.getBreifBusinessById(ord.getBusinessId(), -1);
			ord.setBusinessAccount(businessDTO == null ? "" : businessDTO.getBusinessAccount());
			UserProfileDTO userProfileDTO = userProfileService.findUserProfileById(ord.getUserId());
			ord.setUserName(userProfileDTO == null ? "" : userProfileDTO.getUserName());
			voList.add(OrderBriefInfo.orderDTO2VO(ord));
		}
		ret.setList(voList);
		OrderSearchParam ddbRemoteParam = RetArgUtil.get(retArg, OrderSearchParam.class);
		if (null != ddbRemoteParam && null != ddbRemoteParam.getTotalCount()) {
			ret.setTotal(ddbRemoteParam.getTotalCount());
		}
		return ret;
	}

	@Override
	public OrderDetailInfoVO queryOrderDetailInfoByOrderId(long orderId) {
		OrderFormDTO ordDTO = orderService.queryOrderFormByOrderId(orderId);
		if (null == ordDTO) {
			return null;
		}
		OrderDetailInfoVO ret = new OrderDetailInfoVO();
		long userId = ordDTO.getUserId();
		// 0. 回填数据：基本信息 + 配送信息
		// // orderProm: 订单关联的促销 (策划说orderProm与poProms只能有一个存在)
		// PromotionDTO orderProm =
		// promotionFacade.getPromotionById(ordDTO.getPromotionId());
		// // poProms: po关联的促销 (策划说orderProm与poProms只能有一个存在)
		// Map<Long, PromotionDTO> poProms =
		// orderFacade.extractPromotion(ordDTO);
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
		ret.setCartList(MainsiteVOConvertUtil.convertToOrderCartItemVOList(ordDTO.getOrderCartItemDTOList(), true));

		// businessService.getBusinessDTOListByIdList(ids);

		// 9.备注
		ret.setComment(ordDTO.getComment());
		// 10. 设置代客下单账号
		if (ordDTO.getAgentId() > 0) {
			AgentDTO agentDTO = agentService.findAgentById(ordDTO.getAgentId());
			ret.getBasicInfo().setProxyAccount(agentDTO != null ? agentDTO.getName() : "");
		}
		// TradeItemDTO hbFakeTradeItem = convertHBToFakeTradeItem(ordDTO);
		// ret.fillTradeList(ordDTO, tradeItemDTOList, hbFakeTradeItem);
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

	@Override
	public RetArg querySKULimitOrderList(OrderSearchParam searchParam, long skuId) {
		return orderBriefService.queryLimitOrderBrief(searchParam);
	}

	@Override
	public OrderSkuDTO getOrderSKU(long skuId, long orderId, long userId) {
		return orderService.getOrderSku(userId, orderId, skuId);
	}

}
