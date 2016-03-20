package com.xyl.mmall.timer.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.constant.CalendarConst;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.ExtInfoFieldUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.facade.promotion.RedPacketFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.mainsite.facade.MessagePushFacade;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.oms.service.OmsOrderFormService;
import com.xyl.mmall.order.api.util.OrderApiUtil;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderPackageRefundTaskDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.enums.CancelOmsOrderTaskState;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderCancelState;
import com.xyl.mmall.order.enums.OrderFormSource;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageRefundTaskState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.meta.OrderPackageRefundTask;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderMiscService;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.TradeInternalProxyService;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.promotion.dto.CouponOrderDTO;
import com.xyl.mmall.promotion.dto.RedPacketDTO;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;
import com.xyl.mmall.promotion.meta.RedPacketOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.task.enums.PushMessageType;
import com.xyl.mmall.timer.facade.OrderTimerFacade;

/**
 * @author dingmingliang
 * 
 */
@Facade
public class OrderTimerFacadeImpl implements OrderTimerFacade {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderMiscService orderMiscService;

	@Autowired
	private OrderBriefService orderBriefService;

	@Autowired
	private OrderPackageSimpleService orderPackageSimpleService;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private OmsOrderFormService omsOrderFormService;

	@Autowired
	private JITSupplyManagerFacade jitSupplyManagerFacade;

	@Autowired
	private MobilePushManageFacade mobilePushManageFacade;

	@Autowired
	private RedPacketOrderService redPacketOrderService;

	@Autowired
	private RedPacketFacade redPacketFacade;

	@Autowired
	private MessagePushFacade messagePushFacade;

	@Autowired
	private CouponOrderService couponOrderService;

	@Autowired
	private UserRedPacketService userRedPacketService;

	@Autowired
	private TradeInternalProxyService tradeInternalProxyService;
	
	@Autowired
	private ItemProductService itemProductService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.OrderTimerFacade#pushOrderToOms()
	 */
	@SuppressWarnings("unchecked")
	public void pushOrderToOms() {
		// 1.读取OrderFormState.WAITING_SEND_ORDER的订单列表
		long minOrderId = 0;
		OrderFormState[] stateArray = new OrderFormState[] { OrderFormState.WAITING_SEND_ORDER };
		DDBParam param = DDBParam.genParamX(100);
		RetArg retArg = orderService.queryOrderFormListByStateWithMinOrderId(minOrderId, stateArray, param);
		List<OrderFormDTO> orderDTOList = RetArgUtil.get(retArg, ArrayList.class);

		while (CollectionUtil.isNotEmptyOfCollection(orderDTOList)) {
			for (OrderFormDTO orderDTO : orderDTOList) {
				long orderId = orderDTO.getOrderId(), userId = orderDTO.getUserId();
				try {
					// 2.向OMS发送订单数据
					if (!jitSupplyManagerFacade.savePoOrderForm(orderDTO)) {
						logger.error("jitSupplyManagerFacade.savePoOrderForm fail, orderId=" + orderId);
						continue;
					}
					// 3.更新订单的状态为WAITING_DELIVE
					if (orderService.setOrderFormStateToWaitingDelive(userId, orderId) <= 0) {
						logger.error("orderService.setOrderFormStateToWaitingDelive fail, orderId=" + orderId);
						continue;
					}
				} catch (Exception ex) {
					logger.error("orderId=" + orderId, ex);
				}
			}

			// 4.再次读取未处理的数据
			minOrderId = orderDTOList.get(orderDTOList.size() - 1).getOrderId();
			param = RetArgUtil.get(retArg, DDBParam.class);
			if (param.isHasNext()) {
				retArg = orderService.queryOrderFormListByStateWithMinOrderId(minOrderId, stateArray, param);
				orderDTOList = RetArgUtil.get(retArg, ArrayList.class);
			} else
				orderDTOList = null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.OrderTimerFacade#cancelUnpayOrderByTimout()
	 */
	@SuppressWarnings("unchecked")
	public void cancelUnpayOrderByTimout() {
		// 1.读取OrderFormState.WAITING_SEND_ORDER的订单列表
		long minOrderId = 0, currTime = System.currentTimeMillis();
		OrderFormState[] stateArray = new OrderFormState[] { OrderFormState.WAITING_PAY };
		long[] orderTimeRange = new long[] { 0, currTime - ConstValueOfOrder.MAX_PAY_TIME };
		Set<Long>orderIdSet = new HashSet<Long>();
		DDBParam param = DDBParam.genParamX(100);
		RetArg retArg = orderBriefService.queryOrderFormBriefDTOListByStateWithMinOrderId(minOrderId, stateArray,
				orderTimeRange, param);
		List<OrderFormBriefDTO> orderBDTOList = RetArgUtil.get(retArg, ArrayList.class);

		while (CollectionUtil.isNotEmptyOfCollection(orderBDTOList)) {
			for (OrderFormBriefDTO orderBDTO : orderBDTOList) {
				if(orderIdSet.contains(orderBDTO.getOrderId())){
					continue;
				}
				try {
					// 2.取消订单
					long orderId = orderBDTO.getOrderId(), userId = orderBDTO.getUserId();
					List<Long>orderIds = orderFacade.getSubOrderIds(orderId);
					if(CollectionUtil.isEmptyOfList(orderIds)){
						orderIds = new ArrayList<Long>();
						orderIds.add(orderId);
					}
					orderIdSet.addAll(orderIds);
					List<OrderCancelInfoDTO>list = new ArrayList<OrderCancelInfoDTO>();
					for (Long orderId1 : orderIds) {
						OrderCancelInfoDTO cancelDTO = new OrderCancelInfoDTO();
						cancelDTO.setOrderId(orderId1);
						cancelDTO.setUserId(userId);
						cancelDTO.setCancelSource(OrderCancelSource.OT_SYS);
						cancelDTO.setReason(OrderCancelSource.OT_SYS.getDesc());
						cancelDTO.setRtype(OrderCancelRType.ORI);
						cancelDTO.setOperateUserType(OperateUserType.SYSTEMER);
						list.add(cancelDTO);
						
					}
					RetArg ret = orderFacade.cancelOrders(list);
//					OrderCancelInfoDTO cancelDTO = new OrderCancelInfoDTO();
//					cancelDTO.setOrderId(orderId);
//					cancelDTO.setUserId(userId);
//					cancelDTO.setCancelSource(OrderCancelSource.OT_SYS);
//					cancelDTO.setReason(OrderCancelSource.OT_SYS.getDesc());
//					cancelDTO.setRtype(OrderCancelRType.ORI);
//					cancelDTO.setOperateUserType(OperateUserType.SYSTEMER);
		//			RetArg retArgOfCancel = orderFacade.cancelOrder(cancelDTO);
					Boolean retOfCancel = RetArgUtil.get(ret, Boolean.class);
					if (retOfCancel != Boolean.TRUE) {
						logger.error("orderService.cancelOrder fail, orderId=" + orderIds.toString());
						continue;
					}
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
				}
			}

			// 3.再次读取未处理的数据
			minOrderId = orderBDTOList.get(orderBDTOList.size() - 1).getOrderId();
			param = RetArgUtil.get(retArg, DDBParam.class);
			if (param.isHasNext()) {
				retArg = orderBriefService.queryOrderFormBriefDTOListByStateWithMinOrderId(minOrderId, stateArray,
						orderTimeRange, param);
				orderBDTOList = RetArgUtil.get(retArg, ArrayList.class);
			} else
				orderBDTOList = null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.OrderTimerFacade#setOrderToFinishDelive()
	 */
	@SuppressWarnings("unchecked")
	public void setOrderToFinishDelive() {
		// 1.读取符合条件的订单数据(x天内,订单状态为)
		long minOrderId = 0, currTime = System.currentTimeMillis();
		OrderFormState[] stateArray = new OrderFormState[] { OrderFormState.WAITING_DELIVE, OrderFormState.PART_DELIVE,
				OrderFormState.ALL_DELIVE };
		long[] orderTimeRange = new long[] { currTime - CalendarConst.DAY_TIME * 30, currTime };
		DDBParam param = DDBParam.genParamX(100);
		RetArg retArg = orderBriefService.queryOrderFormBriefDTOListByStateWithMinOrderId(minOrderId, stateArray,
				orderTimeRange, param);

		List<OrderFormBriefDTO> orderBDTOList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfCollection(orderBDTOList)) {
			for (OrderFormBriefDTO orderBDTO : orderBDTOList) {
				try {
					minOrderId = orderBDTO.getOrderId();
					long orderId = orderBDTO.getOrderId(), userId = orderBDTO.getUserId();
					// 2.读取订单名下的包裹信息
					OrderFormDTO orderDTO = orderService.queryOrderForm(userId, orderId, null);

					List<OrderPackageDTO> opDTOList = orderDTO.getOrderPackageDTOList();
					if (CollectionUtil.isEmptyOfCollection(opDTOList))
						continue;
					// 3.判断包裹状态
					boolean hasFinishDelivePackage = false, isAllInFinishOrCancel = true, isAllInCancel = true;
					/*for (OrderPackageDTO opDTO : opDTOList) {
						OrderPackageState opState = opDTO.getOrderPackageState();
						hasFinishDelivePackage = hasFinishDelivePackage || OrderPackageState.isFinishDelive(opState);
						isAllInCancel = isAllInCancel && OrderPackageState.isCancel(opState);
						isAllInFinishOrCancel = isAllInFinishOrCancel
								&& (OrderPackageState.isFinishDelive(opState) || OrderPackageState.isCancel(opState));
					}*/
					boolean isFinishDeliveOrder = hasFinishDelivePackage && isAllInFinishOrCancel;
					// 4.更新订单状态
					if (isFinishDeliveOrder) {
						int retCode = orderService.setOrderFormStateToFinishDelive(userId, orderId);
						if (retCode <= 0)
							logger.error("orderService.setOrderFormStateToFinishDelive fail, retCode=" + retCode
									+ " ,orderId=" + orderId + " ,userId=" + userId);
						// 推送客户端订单可分享红包
					/*	List<RedPacketOrder> packetOrders = redPacketOrderService.getRedPacketOrderList(userId,
								orderDTO.getOrderId());

						if (hasSharedRedPacketOrder(packetOrders)) {
							mobilePushManageFacade.push(userId, PushMessageType.share_gift, "人品大爆发",
									"送您mmall红包啦，和小伙伴们一起抢吧！", orderDTO.getOrderId());
						}*/
					} else if (isAllInCancel) {
						int retCode = orderService.setOrderFormStateToCancel(userId, orderId);
						if (retCode <= 0)
							logger.error("orderService.setOrderFormStateToCancel fail, retCode=" + retCode
									+ " ,orderId=" + orderId + " ,userId=" + userId);
					}
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
//			retArg = orderBriefService.queryOrderFormBriefDTOListByStateWithMinOrderId(minOrderId, stateArray,
//					orderTimeRange, param);
//			orderBDTOList = RetArgUtil.get(retArg, ArrayList.class);
		}
	}

	/**
	 * 判断是否有分享红包的订单
	 * 
	 * @param packetOrders
	 * @return
	 */
	private boolean hasSharedRedPacketOrder(List<RedPacketOrder> packetOrders) {
		if (CollectionUtils.isEmpty(packetOrders)) {
			return false;
		}

		for (RedPacketOrder packetOrder : packetOrders) {
			if (packetOrder.getRedPacketOrderType() == RedPacketOrderType.RETURN_RED_PACKET) {
				RedPacketDTO dto = redPacketFacade.getRedPacketById(packetOrder.getRedPacketId());
				if (dto.isShare()) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.OrderTimerFacade#cancelTrade()
	 */
	public void cancelTrade() {
		// 读取要取消交易的订单取消记录
		DDBParam param = DDBParam.genParam500();
		OrderCancelState cancelState = OrderCancelState.CREATE;

		long minOrderId = 0;
		List<OrderCancelInfoDTO> cancelDTOList = orderService.getOrderCancelInfoDTOList(minOrderId, cancelState, param);
		while (CollectionUtil.isNotEmptyOfCollection(cancelDTOList)) {
			for (OrderCancelInfoDTO cancelDTO : cancelDTOList) {
				orderFacade.cancelTrade(cancelDTO);
			}
			minOrderId = cancelDTOList.get(cancelDTOList.size() - 1).getOrderId();
			cancelDTOList = orderService.getOrderCancelInfoDTOList(minOrderId, cancelState, param);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.OrderTimerFacade#orderPackageRefundTask()
	 */
	public void orderPackageRefundTask() {
		DDBParam param = DDBParam.genParam500();
		long minId = 0;
		OrderPackageRefundTaskState state = OrderPackageRefundTaskState.CREATE;
		// 1.读取未处理完的OrderPackageRefundTask记录
		List<OrderPackageRefundTaskDTO> taskDTOList = orderPackageSimpleService
				.getOrderPackageRefundTaskListByStateWithMinId(minId, state, param);
		// 2.循环执行退款操作
		while (CollectionUtil.isNotEmptyOfCollection(taskDTOList)) {
			for (OrderPackageRefundTaskDTO taskDTO : taskDTOList) {
				minId = taskDTO.getId();
				try {
					dealOrderPackageRefundTask(taskDTO);
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
			// 再次读取记录
			taskDTOList = orderPackageSimpleService.getOrderPackageRefundTaskListByStateWithMinId(minId, state, param);
		}
	}

	/**
	 * 处理一条包裹退款任务
	 * 
	 * @param taskDTO
	 */
	private void dealOrderPackageRefundTask(OrderPackageRefundTaskDTO taskDTO) {
		long retryFlagOfOld = taskDTO.getRetryFlag(), packageId = taskDTO.getPackageId(), orderId = taskDTO
				.getOrderId(), userId = taskDTO.getUserId();
		// 2.1 尝试执行网易宝退款
		boolean isSuccOfRefundWYBCash = false;
		try {
			if (OrderApiUtil.needRefundWYBCash(taskDTO)) {
				isSuccOfRefundWYBCash = tradeInternalProxyService.setOnlineTradeToRefundWithTransaction(packageId,
						orderId, userId, taskDTO.getWybCash(), taskDTO.getRtype());
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

		// 2.2 尝试执行红包退款
		boolean isSuccOfRefundRedCash = false;
		try {
			if (OrderApiUtil.needRefundRedCash(taskDTO)) {
				isSuccOfRefundRedCash = userRedPacketService.refundUserRedpackets(taskDTO.getRedCash(),
						taskDTO.getUserId(), taskDTO.getOrderId(), taskDTO.getPackageId());
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

		// 2.3 更新任务记录
		long retryFlagOfNew = retryFlagOfOld;
		if (isSuccOfRefundWYBCash)
			retryFlagOfNew = ExtInfoFieldUtil.setValueOfType1(retryFlagOfOld, OrderPackageRefundTask.IDX_REFUND_WYB,
					false);
		if (isSuccOfRefundRedCash)
			retryFlagOfNew = ExtInfoFieldUtil.setValueOfType1(retryFlagOfOld, OrderPackageRefundTask.IDX_REFUND_HB,
					false);
		taskDTO.setRetryFlag(retryFlagOfNew);
		if (retryFlagOfNew == 0)
			taskDTO.setState(OrderPackageRefundTaskState.DONE);
		orderPackageSimpleService.updateRetryFlag(taskDTO, retryFlagOfOld);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.OrderTimerFacade#cancelOmsOrder()
	 */
	public void cancelOmsOrder() {
		// 读取取消记录
		DDBParam param = DDBParam.genParam500();
		OrderCancelState cancelState = OrderCancelState.CREATE;

		long minOrderId = 0;
		List<OrderCancelInfoDTO> cancelDTOList = orderService.getOrderCancelInfoDTOList(minOrderId, cancelState, param);
		while (CollectionUtil.isNotEmptyOfCollection(cancelDTOList)) {
			for (OrderCancelInfoDTO cancelDTO : cancelDTOList) {
				orderFacade.cancelOmsOrder(cancelDTO);
			}
			minOrderId = cancelDTOList.get(cancelDTOList.size() - 1).getOrderId();
			cancelDTOList = orderService.getOrderCancelInfoDTOList(minOrderId, cancelState, param);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.OrderTimerFacade#setOrderCancelToDone()
	 */
	public void setOrderCancelToDone() {
		orderService.setOrderCancelToDone();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.OrderTimerFacade#dealCancelOmsOrderTask()
	 */
	public void dealCancelOmsOrderTask() {
		// TODO.DML waiting test
		// 1.读取符合条件的Task记录
		long minOrderId = 0L;
		DDBParam param = DDBParam.genParamX(200);
		CancelOmsOrderTaskState[] taskStateArray = CancelOmsOrderTaskState.getUnTheEndStateArray();
		List<CancelOmsOrderTaskDTO> cancelTaskDTOList = orderMiscService.queryCancelOmsOrderTaskDTOList(minOrderId,
				taskStateArray, param);

		// 处理Task记录
		while (CollectionUtil.isNotEmptyOfCollection(cancelTaskDTOList)) {
			for (CancelOmsOrderTaskDTO taskDTOItem : cancelTaskDTOList) {
				minOrderId = taskDTOItem.getOrderId();
				long orderId = taskDTOItem.getOrderId(), userId = taskDTOItem.getUserId();
				CancelOmsOrderTaskDTO taskDTO = null;
				CancelOmsOrderTaskState taskState = null;
				try {
					// 2.通知oms取消订单
					taskDTO = orderMiscService.queryCancelOmsOrderTaskDTO(orderId, userId);
					taskState = taskDTO.getTaskState();
					if (taskState == CancelOmsOrderTaskState.CREATE) {
						orderFacade.cancelOmsOrder(taskDTO);
					}

					taskDTO = orderMiscService.queryCancelOmsOrderTaskDTO(orderId, userId);
					taskState = taskDTO.getTaskState();
					// 3.1 取消订单
					if (taskState == CancelOmsOrderTaskState.CANCEL_OMS_SUCC
							|| taskState == CancelOmsOrderTaskState.CANCEL_OMS_UNNEED) {
						RetArg retArg = orderFacade.cancelOrder(taskDTO);
						sendSmsForOrderCancelSucc(retArg, taskDTO);
					}
					// 3.2 order取消失败的情况还需要回滚数据
					else if (taskState == CancelOmsOrderTaskState.CANCEL_OMS_FAIL) {
						orderFacade.setOrderFormToCancelRevert(taskDTO);
					}
				} catch (Exception ex) {
					logger.error("dealCancelOmsOrderTask error, orderId=" + orderId, ex);
				}
			}
			// 4.再次读取符合条件的记录
			cancelTaskDTOList = orderMiscService.queryCancelOmsOrderTaskDTOList(minOrderId, taskStateArray, param);
		}
	}

	/**
	 * 订单取消成功的时候,发送通知短信
	 * 
	 * @param retArg
	 * @param taskDTO
	 * @return
	 */
	private void sendSmsForOrderCancelSucc(RetArg retArg, CancelOmsOrderTaskDTO taskDTO) {
		try {
			OrderFormDTO orderDTO = RetArgUtil.get(retArg, OrderFormDTO.class);
			BigDecimal money = RetArgUtil.get(retArg, BigDecimal.class);
			Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
			// 1.判断是否成功取消
			if (isSucc != Boolean.TRUE) {
				return;
			}
			// 2.判断订单来源是否是PC
			if (orderDTO.getOrderFormSource() != OrderFormSource.PC)
				return;
			// 3.判断金额是否>0
			if (money == null || money.compareTo(BigDecimal.ZERO) <= 0)
				return;
			// 4.尝试发送取消成功的短信
			long userId = orderDTO.getUserId(), orderId = orderDTO.getOrderId();
			Map<String, Object> otherParamMap = new HashMap<String, Object>();
			otherParamMap.put("refundType", taskDTO.getRtype().getIntValue());
			otherParamMap.put("money", money);
			messagePushFacade.pushForAll(userId, PushMessageType.order_cancel_after_pay, orderId, otherParamMap);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.OrderTimerFacade#recycleCouponForOrderOfAllPackageCancelled()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg recycleCouponForOrderOfAllPackageCancelled() {
		RetArg recycleRetArg = new RetArg();
		long totalCount = 0, recycleSuccCount = 0, stateSuccCount = 0;
		OrderCancelRType[] oldStates = new OrderCancelRType[] { OrderCancelRType.ALL_PACKAGE_CANCELLED };

		// 1.读取OrderCancelRType.ALL_PACKAGE_CANCELLED的取消原因列表
		long minOrderId = 0;
		DDBParam param = DDBParam.genParamX(100);
		param.setAsc(true);
		param.setOrderColumn("orderId");
		RetArg retArg = orderService.getOrderCancelInfoDTOListByRType(minOrderId,
				OrderCancelRType.ALL_PACKAGE_CANCELLED, param);
		List<OrderCancelInfoDTO> orderCancelInfoDTOList = RetArgUtil.get(retArg, ArrayList.class);

		while (CollectionUtil.isNotEmptyOfCollection(orderCancelInfoDTOList)) {
			for (OrderCancelInfoDTO orderCancelInfoDTO : orderCancelInfoDTOList) {
				if (null == orderCancelInfoDTO) {
					continue;
				}
				totalCount++;
				long orderId = orderCancelInfoDTO.getOrderId(), userId = orderCancelInfoDTO.getUserId();
				boolean isSucc = false, noCouponUsed = false, containsIllegalPkg = false;
				// 2.回收优惠券
				List<CouponOrderDTO> couponOrderList = couponOrderService.getListByOrderId(userId, orderId);
				if (CollectionUtil.isEmptyOfList(couponOrderList)) {
					noCouponUsed = true;
					isSucc = true;
				} else {
					OrderPackageState[] illegalStates = new OrderPackageState[] { OrderPackageState.SIGN_IN,
							OrderPackageState.CANCEL_SR };
					List<OrderPackageSimpleDTO> ordPkgList = orderPackageSimpleService
							.queryOrderPackageSimpleByOrderId(userId, orderId, illegalStates);
					if (!CollectionUtil.isEmptyOfList(ordPkgList)) {
						containsIllegalPkg = true;
						isSucc = true;
					} else {
						isSucc = couponOrderService.recycleCouponOrderList(couponOrderList);
					}
				}
				if (!isSucc) {
					logger.warn("couponOrderService.recycleCouponOrderList(couponOrderList) failed for userId:"
							+ userId + ", orderId:" + orderId);
					continue;
				}
				recycleSuccCount++;
				OrderCancelRType recycledRType = noCouponUsed ? OrderCancelRType.ALL_PACKAGE_CANCELLED_NO_COUPON
						: (containsIllegalPkg ? OrderCancelRType.ALL_PACKAGE_CANCELLED_NOT_RECYCLE_FOR_R_SR
								: OrderCancelRType.ALL_PACKAGE_CANCELLED_COUPON_RECYCLED);
				// 3.更新状态
				isSucc = orderService.setOrderCancelRType(orderCancelInfoDTO, oldStates, recycledRType);
				if (isSucc) {
					stateSuccCount++;
				} else {
					logger.warn("orderService.setOrderCancelRType(...) failed for userId:" + userId + ", orderId:"
							+ orderId);
				}
			}

			// 4.再次读取未处理的数据
			minOrderId = orderCancelInfoDTOList.get(orderCancelInfoDTOList.size() - 1).getOrderId();
			param = RetArgUtil.get(retArg, DDBParam.class);
			if (param.isHasNext()) {
				retArg = orderService.getOrderCancelInfoDTOListByRType(minOrderId,
						OrderCancelRType.ALL_PACKAGE_CANCELLED, param);
				orderCancelInfoDTOList = RetArgUtil.get(retArg, ArrayList.class);
			} else {
				orderCancelInfoDTOList = null;
			}
		}

		Boolean result = (totalCount == recycleSuccCount && totalCount == stateSuccCount) ? Boolean.TRUE
				: Boolean.FALSE;
		RetArgUtil.put(recycleRetArg, result);
		RetArgUtil.put(recycleRetArg, "[recycleCouponForOrderOfAllPackageCancelled()] total:" + totalCount
				+ ", recycled:" + recycleSuccCount + ", updated:" + stateSuccCount);
		return recycleRetArg;
	}
	
	
	@Override
	public void syncProductSaleNum(int dealNum) {
		 Map<Long, Integer> allPodctSaleNumMap = new HashMap<Long, Integer>(500);
		 Map<Long, Integer> partPodctSaleNumMap = null;
		int count = 0;
		List<Long>orderIdList = new ArrayList<Long>();
		OrderSearchParam orderSearchParam = new OrderSearchParam();
		orderSearchParam.setOrderStatus(OrderFormState.FINISH_TRADE.getIntValue());
		orderSearchParam.setLimit(dealNum>0?dealNum:100);
		orderSearchParam.setOffset(0);
		orderSearchParam.setOrderColumn("businessId");
		RetArg retArg = orderBriefService.queryOrderFormBriefByOrderSearchParam(orderSearchParam);
		List<OrderFormBriefDTO> orderBDTOList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfList(orderBDTOList)) {
			orderIdList.clear();
			for (OrderFormBriefDTO orderBDTO : orderBDTOList) {
				orderIdList.add(orderBDTO.getOrderId());
			}
			partPodctSaleNumMap = orderBriefService.getProductSaleNumMapByOrderIds(orderIdList);
			if(CollectionUtil.isNotEmptyOfMap(partPodctSaleNumMap)){
				for (Map.Entry<Long, Integer> entry : partPodctSaleNumMap.entrySet()) {
					if(allPodctSaleNumMap.containsKey(entry.getKey())){
						allPodctSaleNumMap.put(entry.getKey(), entry.getValue()+allPodctSaleNumMap.get(entry.getKey()));
					}else{
						allPodctSaleNumMap.put(entry.getKey(), entry.getValue());
					}
				}
			}
			// 再次读取未处理的数据
			orderSearchParam = RetArgUtil.get(retArg, OrderSearchParam.class);
			++count;
			if (orderSearchParam.isHasNext()) {
				orderSearchParam.setOffset(count*orderSearchParam.getLimit());
				retArg = orderBriefService.queryOrderFormBriefByOrderSearchParam(orderSearchParam);
				orderBDTOList = RetArgUtil.get(retArg, ArrayList.class);
			} else{
				orderBDTOList = null;
			}
		}
		//分批更新
		if(allPodctSaleNumMap.size()>dealNum){
			int i = 0;
			partPodctSaleNumMap = new HashMap<Long, Integer>();
			for (Map.Entry<Long, Integer> entry : allPodctSaleNumMap.entrySet()) {
				++i;
				partPodctSaleNumMap.put(entry.getKey(), entry.getValue());
				if(i%dealNum==0){
					itemProductService.updateProductsSaleNum(partPodctSaleNumMap);
					partPodctSaleNumMap.clear();
				}
			}
			if(CollectionUtil.isNotEmptyOfMap(partPodctSaleNumMap)){
				itemProductService.updateProductsSaleNum(partPodctSaleNumMap);
			}
		}else{
			itemProductService.updateProductsSaleNum(allPodctSaleNumMap);
		}
	}

}