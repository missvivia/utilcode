package com.xyl.mmall.timer.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.netease.backend.tcc.TccActivity;
import com.netease.backend.tcc.TccManager;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.HBRecycleLogDTO;
import com.xyl.mmall.order.dto.ReturnFormDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.service.HBRecycleLogService;
import com.xyl.mmall.order.service.ReturnFormService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.order.service.ReturnPackageUpdateService;
import com.xyl.mmall.promotion.dto.CouponOrderDTO;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.UserCouponService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.promotion.service.tcc.RecycleTCCService;
import com.xyl.mmall.timer.facade.ReturnTimerFacade;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月29日 下午12:38:56
 * 
 */
@Facade("returnTimerFacade")
public class ReturnTimerFacadeImpl implements ReturnTimerFacade {

	private Logger logger = LoggerFactory.getLogger(getClass());

	// 订单发货超过30天，仓库没有收到退货，系统自动取消
	private static final long TIME_DETAIN = ConstValueOfOrder.SEP_RP_APPLY_DAY * 24 * 60 * 60 * 1000L;

	@Value("${return.coupon.code}")
	private String returnCouponCode;

	@Value("${return.exp.compensation}")
	private double returnExpCompensation;

	@Autowired
	private ReturnFormService retFormService;

	@Autowired
	private ReturnPackageQueryService retPkgQueryService;

	@Autowired
	private ReturnPackageUpdateService retPkgUpdateService;

	@Autowired
	private CouponOrderService couponOrderService;

	@Autowired
	private JITSupplyManagerFacade jitSupplyManagerFacade;

	@Autowired
	private RecycleTCCService recycleTCCService;

	@Autowired
	private TccManager tccManager;

	@Resource(name = "recycleCouponHbTCCActivity")
	private TccActivity recycleCouponHbTCCActivity;

	@Autowired
	private UserCouponService userCouponService;

	// @Autowired
	// protected TradeInternalProxyService tradeInternalProxyService;

	@Autowired
	protected UserRedPacketService userRedPacketService;

	@Autowired
	protected HBRecycleLogService hbRecycleLogService;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.ReturnTimerFacade#setReturnStateToCanceled()
	 */
	@Override
	public RetArg setReturnStateToCanceled() {
		RetArg retArg = new RetArg();
		long totalCount = 0, successfulCount = 0;
		Map<Long, List<Long>> retPkgs = retPkgQueryService.getReturnPackageShouldBeCanceled(TIME_DETAIN);
		if (null == retPkgs) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null returned from retPkgQueryService.getReturnPackageShouldBeCanceled()");
			return retArg;
		}
		for (Entry<Long, List<Long>> entry : retPkgs.entrySet()) {
			Long userId = entry.getKey();
			List<Long> retIdList = entry.getValue();
			if (null == userId || null == retIdList) {
				continue;
			}
			for (Long retPkgId : retIdList) {
				if (null == retPkgId) {
					continue;
				}
				totalCount++;
				try {
					RetArg updateArg = retPkgUpdateService.setReturnStateToCanceled(retPkgId, userId);
					Boolean isSucc = RetArgUtil.get(updateArg, Boolean.class);
					if (null == isSucc || isSucc != Boolean.TRUE) {
						String info = RetArgUtil.get(updateArg, String.class);
						if (null != info) {
							logger.error("设置为取消状态失败：userId=" + userId + ", retPkgId=" + retPkgId + ", info -> " + info);
						} else {
							logger.error("设置为取消状态失败：userId=" + userId + ", retPkgId=" + retPkgId);
						}
					} else {
						successfulCount++;
					}
				} catch (Exception e) {
					logger.error("设置为取消状态失败：userId=" + userId + "retPkgId=" + retPkgId, e);
				}
			}
		}
		Boolean match = (totalCount == successfulCount) ? Boolean.TRUE : Boolean.FALSE;
		RetArgUtil.put(retArg, match);
		RetArgUtil.put(retArg, "[setReturnStateToCanceled()] total:" + totalCount + ", successful:" + successfulCount);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.ReturnTimerFacade#pushReturnPackageToJIT()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg pushReturnPackageToJIT() {
		RetArg pushRetArg = new RetArg();
		long totalCount = 0, jitSuccCount = 0, pkgStateSuccCount = 0;
		long minRetPkgId = 0;
		DDBParam param = DDBParam.genParamX(100);
		param.setAsc(true);
		param.setOrderColumn("retPkgId");
		RetArg retArg = retPkgQueryService.queryJITFailedReturnPackageByMinRetPkgId(minRetPkgId, param);
		if (null == retArg) {
			RetArgUtil.put(pushRetArg, Boolean.FALSE);
			RetArgUtil
					.put(pushRetArg,
							"null RetArg returned from _ReturnPackageQueryService.queryJITFailedReturnPackageByMinRetPkgId(...)");
			return pushRetArg;
		}
		List<ReturnPackageDTO> retPkgList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfCollection(retPkgList)) {
			for (ReturnPackageDTO retPkg : retPkgList) {
				if (null == retPkg) {
					logger.error("null retPkg in retPkgList");
					continue;
				}
				totalCount++;
				long retPkgId = retPkg.getRetPkgId();
				long userId = retPkg.getUserId();
				try {
					// 1. 向JIT发送退货数据（to be continued: 和波爷对接）
					boolean jitSucc = jitSupplyManagerFacade.saveReturnOrderForm(retPkg);
					if (jitSucc) {
						jitSuccCount++;
						// 2. 更新ReturnForm的jitSucc字段
						RetArg pkgStateArg = retPkgUpdateService.updateJITPushToSuccessful(retPkg);
						Boolean pushSucc = RetArgUtil.get(pkgStateArg, Boolean.class);
						if (null != pushSucc && Boolean.TRUE == pushSucc) {
							pkgStateSuccCount++;
						} else {
							StringBuffer logBuf = new StringBuffer();
							logBuf.append("retPkgUpdateService.updateJITPushToSuccessful(...) failed. [userId:")
									.append(userId).append(", retPkgId:").append(retPkgId).append("].")
									.append(" extInfo -> ").append(RetArgUtil.get(pkgStateArg, String.class));
							logger.error(logBuf.toString());
						}
					} else {
						logger.error("push to jit failed. [userId:" + userId + ", retPkgId:" + retPkgId + "]");
					}
				} catch (Exception e) {
					logger.error("push to jit failed. [userId:" + userId + ", retPkgId:" + retPkgId + "]", e);
				}
			}
			// 3. 再次读取未推送的数据
			ReturnPackageDTO last = retPkgList.get(retPkgList.size() - 1);
			if (null == last) {
				logger.error("null retPkg in retPkgList");
				break;
			}
			minRetPkgId = last.getRetPkgId();
			param = RetArgUtil.get(retArg, DDBParam.class);
			if (null == param) {
				logger.error("null DDBParam in RetArg");
				break;
			}
			if (param.isHasNext()) {
				retArg = retPkgQueryService.queryJITFailedReturnPackageByMinRetPkgId(minRetPkgId, param);
				retPkgList = RetArgUtil.get(retArg, ArrayList.class);
			} else {
				break;
			}
		}
		Boolean match = (totalCount == jitSuccCount && totalCount == pkgStateSuccCount) ? Boolean.TRUE : Boolean.FALSE;
		RetArgUtil.put(pushRetArg, match);
		RetArgUtil.put(pushRetArg, "[pushReturnPackageToJIT()] total:" + totalCount + ", jitSucc:" + jitSuccCount
				+ ", pkgStateSucc:" + pkgStateSuccCount);
		return pushRetArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.ReturnTimerFacade#recycleCoupon()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg recycleCoupon() {
		RetArg recycleRetArg = new RetArg();
		long totalCount = 0, recySuccCount = 0, formStateSuccCount = 0;
		// 1. 读取_ReturnCouponHbRecycleState.WAITING_RECYCEL的退货单列表
		long minOrderId = 0;
		DDBParam param = DDBParam.genParamX(100);
		param.setAsc(true);
		param.setOrderColumn("orderId");
		RetArg retArg = retFormService.queryReturnFormShouldRecycleCouponByMinOrderId(minOrderId, param);
		if (null == retArg) {
			RetArgUtil.put(recycleRetArg, Boolean.FALSE);
			RetArgUtil.put(recycleRetArg,
					"null RetArg returned from _ReturnFormService.queryReturnFormShouldRecycleCouponByMinOrderId(...)");
			return recycleRetArg;
		}
		List<ReturnFormDTO> retFormList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfCollection(retFormList)) {
			for (ReturnFormDTO retForm : retFormList) {
				if (null == retForm) {
					logger.error("null retForm in retFormList");
					continue;
				}
				totalCount++;
				/**
				 * // 2.1 处理已回收的情形 boolean couponRecycled =
				 * couponRecycled(retForm.getUserId(), retForm.getOrderId()); if
				 * (couponRecycled) { recySuccCount++; if
				 * (recycleCoupon_Step_2_1(retForm)) { formStateSuccCount++; }
				 * continue; } // 2.2 处理未回收的情况 int result =
				 * recycleCoupon_Step_2_2(retForm); if (1 == result) {
				 * recySuccCount++; } else if (2 == result) { recySuccCount++;
				 * formStateSuccCount++; } else {
				 * 
				 * }
				 */
				long userId = retForm.getUserId();
				long orderId = retForm.getOrderId();
				List<CouponOrderDTO> couponOrderList = couponOrderService.getListByOrderId(userId, orderId);
				if (CollectionUtil.isEmptyOfList(couponOrderList)) {
					recySuccCount++;
					formStateSuccCount++;
					continue;
				}
				boolean isSucc = couponOrderService.recycleCouponOrderList(couponOrderList);
				if (!isSucc) {
					logger.warn("couponOrderService.recycleCouponOrderList(couponOrderList) failed for userId:"
							+ userId + ", orderId:" + orderId);
					continue;
				}
				recySuccCount++;
				RetArg retFormServiceRetArg = retFormService.recycleCoupon(retForm);
				Boolean recySucc = RetArgUtil.get(retFormServiceRetArg, Boolean.class);
				if (null != recySucc && recySucc == Boolean.TRUE) {
					formStateSuccCount++;
				} else {
					StringBuffer logBuf = new StringBuffer();
					logBuf.append("更新退货单优惠券回收状态失败 [userId:").append(retForm.getUserId()).append(", orderId:")
							.append(retForm.getOrderId()).append("]").append(" extInfo -> ")
							.append(RetArgUtil.get(retArg, String.class));
					logger.error(logBuf.toString());
				}
			}
			// 3. 再次读取未推送的数据
			ReturnFormDTO last = retFormList.get(retFormList.size() - 1);
			if (null == last) {
				logger.error("null retForm in retFormList");
				break;
			}
			minOrderId = last.getOrderId();
			param = RetArgUtil.get(retArg, DDBParam.class);
			if (null == param) {
				logger.error("null DDBParam in RetArg");
				break;
			}
			if (param.isHasNext()) {
				retArg = retFormService.queryReturnFormShouldRecycleCouponByMinOrderId(minOrderId, param);
				retFormList = RetArgUtil.get(retArg, ArrayList.class);
			} else {
				break;
			}
		}
		Boolean match = (totalCount == recySuccCount && totalCount == formStateSuccCount) ? Boolean.TRUE
				: Boolean.FALSE;
		RetArgUtil.put(recycleRetArg, match);
		RetArgUtil.put(recycleRetArg, "[recycleCoupon()] total:" + totalCount + ", recySucc:" + recySuccCount
				+ ", formStateSucc:" + formStateSuccCount);
		return recycleRetArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.ReturnTimerFacade#couponRecycled(long,
	 *      long)
	 */
	@Override
	public boolean couponRecycled(long userId, long orderId) {
		boolean couponRecycled = false;
		CouponOrder couponOrder = couponOrderService.getCouponOrderByOrderIdOfUseType(userId, orderId);
		if (null == couponOrder) {
			logger.error("no couponOrder for userId:" + userId + ", orderId:" + orderId);
		} else {
			ActivationHandlerType handlerType = couponOrder.getCouponHandlerType();
			if (null != handlerType && handlerType == ActivationHandlerType.CANCEL_RESET) {
				couponRecycled = true;
			}
		}
		return couponRecycled;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.ReturnTimerFacade#distributeReturnExpHb()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg distributeReturnExpHb() {
		RetArg distributeRetArg = new RetArg();
		long totalCount = 0, distributeSuccCount = 0, pkgStateSuccCount = 0;
		// 1. 找出退款成功、10元的优惠券补贴未发的退货记录列表
		long minRetPkgId = 0;
		DDBParam param = DDBParam.genParamX(100);
		param.setAsc(true);
		param.setOrderColumn("retPkgId");
		RetArg retArg = retPkgQueryService.getReturnedButNotDistributedReturnPackage(minRetPkgId, param);
		if (null == retArg) {
			RetArgUtil.put(distributeRetArg, Boolean.FALSE);
			RetArgUtil
					.put(distributeRetArg,
							"null RetArg returned from _ReturnPackageQueryService.getReturnedButNotDistributedReturnPackage(...)");
			return distributeRetArg;
		}
		List<ReturnPackageDTO> retPkgList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfCollection(retPkgList)) {
			for (ReturnPackageDTO retPkg : retPkgList) {
				if (null == retPkg) {
					logger.error("null retPkg in retPkgList");
					continue;
				}
				totalCount++;
				// // 2.1 处理distributed的情形
				// boolean alreadyDistributed =
				// couponAlreadyDistributed(retPkg.getUserId(),
				// retPkg.getOrderId());
				// if (alreadyDistributed) {
				// distributeSuccCount++;
				// if (distributeReturnCoupon_Step_2_1(retPkg)) {
				// pkgStateSuccCount++;
				// }
				// continue;
				// }
				// // 2.2 处理未distribute情况
				// int result = distributeReturnCoupon_Step_2_2(retPkg);
				// if (1 == result) {
				// distributeSuccCount++;
				// } else if (2 == result) {
				// distributeSuccCount++;
				// pkgStateSuccCount++;
				// } else {
				//
				// }
				long userId = retPkg.getUserId();
				long orderId = retPkg.getOrderId();
				long packageId = retPkg.getOrderPkgId();
				BigDecimal cash = new BigDecimal(returnExpCompensation);
				boolean isSucc = userRedPacketService.refundCompensateUserRP(userId, orderId, packageId, cash);
				if (!isSucc) {
					logger.warn("userRedPacketService.refundCompensateUserRP(userId, orderId, packageId, cash) failed for retPkg "
							+ retPkg.getRetPkgId());
					continue;
				}
				distributeSuccCount++;
				RetArg retPkgStateArg = retPkgUpdateService.distributeReturnExpHb(retPkg);
				Boolean retPkgStateUpdateSucc = RetArgUtil.get(retPkgStateArg, Boolean.class);
				if (Boolean.TRUE != retPkgStateUpdateSucc) {
					String msg = RetArgUtil.get(retPkgStateArg, String.class);
					if (null == msg) {
						msg = "";
					}
					logger.warn("retPkgUpdateService.distributeReturnExpHb(retPkg) for retPkg " + retPkg.getRetPkgId()
							+ " failed due to " + msg);
				} else {
					pkgStateSuccCount++;
				}
			}
			// 3. 再次读取未推送的数据
			ReturnPackageDTO last = retPkgList.get(retPkgList.size() - 1);
			if (null == last) {
				logger.error("null retPkg in retPkgList");
				break;
			}
			minRetPkgId = last.getRetPkgId();
			param = RetArgUtil.get(retArg, DDBParam.class);
			if (null == param) {
				logger.error("null DDBParam in RetArg");
				break;
			}
			if (param.isHasNext()) {
				retArg = retPkgQueryService.getReturnedButNotDistributedReturnPackage(minRetPkgId, param);
				retPkgList = RetArgUtil.get(retArg, ArrayList.class);
			} else {
				break;
			}
		}

		Boolean match = (totalCount == distributeSuccCount && totalCount == pkgStateSuccCount) ? Boolean.TRUE
				: Boolean.FALSE;
		RetArgUtil.put(distributeRetArg, match);
		RetArgUtil.put(distributeRetArg, "[distributeReturnExpHb()] total:" + totalCount + ", distributeSucc:"
				+ distributeSuccCount + ", pkgStateSucc:" + pkgStateSuccCount);
		return distributeRetArg;
	}

	// private boolean couponAlreadyDistributed(long userId, long orderId) {
	// Map<CouponOrderType, List<CouponOrder>> couponOrderMap =
	// couponOrderService.getMapByOrderId(userId, orderId);
	// if (null == couponOrderMap ||
	// !couponOrderMap.containsKey(CouponOrderType.RETURN_COUPON)) {
	// return false;
	// }
	// List<CouponOrder> couponOrderList =
	// couponOrderMap.get(CouponOrderType.RETURN_COUPON);
	// if (null == couponOrderList) {
	// return false;
	// }
	// for (CouponOrder couponOrder : couponOrderList) {
	// if (null == couponOrder) {
	// continue;
	// }
	// if (null == returnCouponCode ||
	// !returnCouponCode.equals(couponOrder.getCouponCode())) {
	// continue;
	// }
	// ActivationHandlerType aht = couponOrder.getCouponHandlerType();
	// if (null == aht || ActivationHandlerType.GRANT != aht) {
	// continue;
	// }
	// return true;
	// }
	// return false;
	// }
	//
	// private boolean distributeReturnCoupon_Step_2_1(ReturnPackageDTO retPkg)
	// {
	// if (null == retPkg) {
	// return false;
	// }
	// RetArg retArg = retPkgUpdateService.distributeReturnExpHb(retPkg);
	// Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
	// if (null == isSucc || isSucc == Boolean.FALSE) {
	// StringBuffer logBuf = new StringBuffer();
	// logBuf.append("更新退货单退货红包补贴状态[userId:").append(retPkg.getUserId()).append(", retPkgId:")
	// .append(retPkg.getRetPkgId()).append("]").append(" extInfo -> ")
	// .append(RetArgUtil.get(retArg, String.class));
	// logger.error(logBuf.toString());
	// return false;
	// }
	// return true;
	// }
	//
	// /**
	// *
	// * @param retPkg
	// * @return 0: both failed, 1: tcc succ, 2: both succ
	// */
	// private int distributeReturnCoupon_Step_2_2(ReturnPackageDTO retPkg) {
	// if (null == retPkg) {
	// return 0;
	// }
	// long orderId = retPkg.getOrderId();
	// long retPkgId = retPkg.getRetPkgId();
	// long userId = retPkg.getUserId();
	// boolean isSucc = false;
	// isSucc = userCouponService.refundCompensateUserCoupon(userId, orderId,
	// returnCouponCode);
	// if (!isSucc) {
	// logger.error("UserCouponService 返券失败 [userId:" + userId + ", orderId:" +
	// orderId + ", retPkgId:" + retPkgId
	// + "]");
	// return 0;
	// }
	// RetArg retArg = retPkgUpdateService.distributeReturnExpHb(retPkg);
	// Boolean distributeSucc = RetArgUtil.get(retArg, Boolean.class);
	// if (null == distributeSucc || distributeSucc == Boolean.FALSE) {
	// StringBuffer logBuf = new StringBuffer();
	// logBuf.append("更新退货单退货红包补贴状态[userId:").append(retPkg.getUserId()).append(", retPkgId:")
	// .append(retPkg.getRetPkgId()).append("]").append(" extInfo -> ")
	// .append(RetArgUtil.get(retArg, String.class));
	// logger.error(logBuf.toString());
	// return 1;
	// }
	// return 2;
	// }

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.ReturnTimerFacade#recycleHb()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg recycleHb() {
		RetArg recycleRetArg = new RetArg();
		long totalCount = 0, recycleSuccCount = 0, pkgStateSuccCount = 0;
		// 1. 找出退款成功、红包未回收的退货记录列表
		long minRetPkgId = 0;
		DDBParam param = DDBParam.genParamX(100);
		param.setAsc(true);
		param.setOrderColumn("retPkgId");
		RetArg retArg = hbRecycleLogService.getReturnedButNotRecycledObjects(minRetPkgId, param);
		if (null == retArg) {
			RetArgUtil.put(recycleRetArg, Boolean.FALSE);
			RetArgUtil.put(recycleRetArg,
					"null RetArg returned from HBRecycleLogService.getReturnedButNotRecycledObjects(...)");
			return recycleRetArg;
		}
		List<HBRecycleLogDTO> hbRecycleLogList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfCollection(hbRecycleLogList)) {
			for (HBRecycleLogDTO hbRecycleLog : hbRecycleLogList) {
				if (null == hbRecycleLog) {
					logger.error("null hbRecycleLog in hbRecycleLogList");
					continue;
				}
				totalCount++;
				BigDecimal cash = hbRecycleLog.getPayedHbPriceToUser();
				long userId = hbRecycleLog.getUserId();
				long orderId = hbRecycleLog.getOrderId();
				long packageId = hbRecycleLog.getOrderPkgId();
				boolean isSucc = true;
				if (null != cash && cash.compareTo(BigDecimal.ZERO) > 0) {
					isSucc = userRedPacketService.refundUserRedpackets(cash, userId, orderId, packageId);
				}
				if (!isSucc) {
					logger.warn("userRedPacketService.refundUserRedpackets(cash, userId, orderId, packageId) failed for retPkgId "
							+ hbRecycleLog.getRetPkgId());
					continue;
				}
				recycleSuccCount++;
				if (hbRecycleLogService.recycleHb(hbRecycleLog)) {
					pkgStateSuccCount++;
				} else {
					logger.warn("hbRecycleLogService.recycleHb(hbRecycleLog) failed for hbRecycleLog: "
							+ hbRecycleLog.getRetPkgId());
				}
			}
			// 3. 再次读取未推送的数据
			HBRecycleLogDTO last = hbRecycleLogList.get(hbRecycleLogList.size() - 1);
			if (null == last) {
				logger.error("null hbRecycleLog in hbRecycleLogList");
				break;
			}
			minRetPkgId = last.getRetPkgId();
			param = RetArgUtil.get(retArg, DDBParam.class);
			if (null == param) {
				logger.error("null DDBParam in RetArg");
				break;
			}
			if (param.isHasNext()) {
				retArg = hbRecycleLogService.getReturnedButNotRecycledObjects(minRetPkgId, param);
				hbRecycleLogList = RetArgUtil.get(retArg, ArrayList.class);
			} else {
				break;
			}
		}

		Boolean match = (totalCount == recycleSuccCount && totalCount == pkgStateSuccCount) ? Boolean.TRUE
				: Boolean.FALSE;
		RetArgUtil.put(recycleRetArg, match);
		RetArgUtil.put(recycleRetArg, "[recycleHb()] total:" + totalCount + ", recycleSucc:" + recycleSuccCount
				+ ", pkgStateSucc:" + pkgStateSuccCount);
		return recycleRetArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.timer.facade.ReturnTimerFacade#returnCash()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg returnCash() {
		RetArg cashRetArg = new RetArg();
		long totalCount = 0, wybReturnSuccCount = 0, pkgStateSuccCount = 0;
		// 1. 找出退款成功、10元的优惠券补贴未发的退货记录列表
		long minRetPkgId = 0;
		DDBParam param = DDBParam.genParamX(100);
		param.setAsc(true);
		param.setOrderColumn("retPkgId");
		RetArg retArg = retPkgQueryService.queryReturnPackageShouldReturnCashByMinRetPkgId(minRetPkgId, param);
		if (null == retArg) {
			RetArgUtil.put(cashRetArg, Boolean.FALSE);
			RetArgUtil
					.put(cashRetArg,
							"null RetArg returned from _ReturnPackageQueryService.queryReturnPackageShouldReturnCashByMinRetPkgId(...)");
			return cashRetArg;
		}
		List<ReturnPackageDTO> retPkgList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfCollection(retPkgList)) {
			for (ReturnPackageDTO retPkg : retPkgList) {
				if (null == retPkg) {
					logger.error("null retPkg in retPkgList");
					continue;
				}
				totalCount++;
				// 2.1 调用网易宝退款接口
				long packageId = retPkg.getOrderPkgId();
				long orderId = retPkg.getOrderId();
				long userId = retPkg.getUserId();
				try {
					/**
					 * BigDecimal cash = retPkg.getPayedCashPriceToUser();
					 * OrderCancelRType rtype = OrderCancelRType.UN_ORI; if
					 * (RefundType.ORIGINAL_PATH == retPkg.getRefundType()) {
					 * rtype = OrderCancelRType.ORI; } boolean isSucc =
					 * tradeInternalProxyService
					 * .setOnlineTradeToRefundWithTransaction(packageId,
					 * orderId, userId, cash, rtype); if (isSucc) {
					 * wybReturnSuccCount++; } else { logger.error(
					 * "tradeInternalProxyService.setOnlineTradeToRefundWithTransaction(...) failed for [userId:"
					 * + userId + ", packageId:" + packageId + ", orderId:" +
					 * orderId + "]"); }
					 */
					if (retPkgUpdateService.finishReturnForNotCOD(retPkg, null)) {
						pkgStateSuccCount++;
					}
				} catch (Exception e) {
					logger.error("retPkgUpdateService.finishReturnForNotCOD(...) failed for [userId:" + userId
							+ ", packageId:" + packageId + ", orderId:" + orderId + "]", e);
					continue;
				}
				/**
				 * // 2.2 更新退货包裹状态 if
				 * (retPkgUpdateService.finishReturnExec(retPkg, false, null)) {
				 * pkgStateSuccCount++; } else { logger.error(
				 * "retPkgUpdateService.finishReturnExec(...) failed for [userId:"
				 * + retPkg.getUserId() + ", retPkgId:" + retPkg.getRetPkgId() +
				 * "]"); }
				 */
			}
			// 3. 再次读取未推送的数据
			ReturnPackageDTO last = retPkgList.get(retPkgList.size() - 1);
			if (null == last) {
				logger.error("null retPkg in retPkgList");
				break;
			}
			minRetPkgId = last.getRetPkgId();
			param = RetArgUtil.get(retArg, DDBParam.class);
			if (null == param) {
				logger.error("null DDBParam in RetArg");
				break;
			}
			if (param.isHasNext()) {
				retArg = retPkgQueryService.queryReturnPackageShouldReturnCashByMinRetPkgId(minRetPkgId, param);
				retPkgList = RetArgUtil.get(retArg, ArrayList.class);
			} else {
				break;
			}
		}

		Boolean match = (totalCount == wybReturnSuccCount && totalCount == pkgStateSuccCount) ? Boolean.TRUE
				: Boolean.FALSE;
		RetArgUtil.put(cashRetArg, match);
		RetArgUtil.put(cashRetArg, "[returnCash()] total:" + totalCount + ", wybReturnSucc:" + wybReturnSuccCount
				+ ", pkgStateSucc:" + pkgStateSuccCount);
		return cashRetArg;
	}

}
