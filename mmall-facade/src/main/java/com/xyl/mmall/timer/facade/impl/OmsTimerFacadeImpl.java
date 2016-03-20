package com.xyl.mmall.timer.facade.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.codeinfo.MessageCodeInfo;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;
import com.xyl.mmall.oms.enums.ShipStateType;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.meta.OmsOrderPackageSku;
import com.xyl.mmall.oms.meta.OmsReturnOrderForm;
import com.xyl.mmall.oms.meta.OmsReturnOrderFormSku;
import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.service.FreightService;
import com.xyl.mmall.oms.service.OmsOrderFormService;
import com.xyl.mmall.oms.service.OmsOrderPackageService;
import com.xyl.mmall.oms.service.OmsReturnOrderFormService;
import com.xyl.mmall.oms.service.PickOrderService;
import com.xyl.mmall.oms.service.PickSkuService;
import com.xyl.mmall.oms.service.ShipOrderService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.util.OmsUtil;
import com.xyl.mmall.order.param.OrderServiceUpdatePackageIdParam;
import com.xyl.mmall.order.param.ReturnConfirmParam;
import com.xyl.mmall.order.param.SetPackageToOutTimeParam;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.ReturnPackageUpdateService;
import com.xyl.mmall.task.enums.PushMessageType;
import com.xyl.mmall.timer.facade.OmsTimerFacade;

/**
 * @author zb
 *
 */
@Facade
public class OmsTimerFacadeImpl implements OmsTimerFacade {
	private Logger logger = Logger.getLogger(OmsTimerFacadeImpl.class);

	@Autowired
	private OmsOrderFormService omsOrderFormService;

	@Autowired
	private MobilePushManageFacade mobilePushManageFacade;

	@Autowired
	private OmsReturnOrderFormService omsReturnOrderFormService;

	@Autowired
	private ReturnPackageUpdateService returnUpdateService;

	@Autowired
	private OmsOrderPackageService omsOrderPackageService;

	@Autowired
	private PickOrderService pickOrderService;

	@Autowired
	private PickSkuService pickSkuService;

	@Autowired
	private ShipOrderService shipOrderService;

	@Autowired
	private OrderPackageSimpleService orderPackageSimpleService;

	@Autowired
	private WarehouseService warehouseService;
	
	@Autowired
	private FreightService freightService;

	private int limit = 20;

	@Override
	public void pushOmsReturnOrderFormStateToApp() {
		// 1.批量获取仓库已经确认收货的退货订单
		long startConfirmTime = System.currentTimeMillis();
		// 暂时只推送正常的情况
		OmsReturnOrderFormState state = OmsReturnOrderFormState.CONFIRMED;
		List<OmsReturnOrderForm> list = omsReturnOrderFormService.getListByConfirmTimeAndState(startConfirmTime, state,
				limit);
		while (!list.isEmpty()) {
			for (OmsReturnOrderForm omsReturnOrderForm : list) {
				long retId = omsReturnOrderForm.getId();
				long userId = omsReturnOrderForm.getUserId();
				Map<Long, ReturnConfirmParam> receivedRetOrdSku = new LinkedHashMap<>();
				List<OmsReturnOrderFormSku> skuList = omsReturnOrderFormService.getOmsReturnOrderFormSkuListByReturnId(
						retId, userId);
				for (OmsReturnOrderFormSku sku : skuList) {
					ReturnConfirmParam param = new ReturnConfirmParam();
					param.setConfirmCount(sku.getConfirmCount());
					param.setExtInfo(omsReturnOrderForm.getExtInfo());
					receivedRetOrdSku.put(sku.getOrderSkuId(), param);
				}
				RetArg retArg = returnUpdateService.confirmReturnedOrderSku(retId, userId, receivedRetOrdSku);
				boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
				if (isSucc) {
					omsReturnOrderFormService.updateOmsReturnOrderFormState(retId,
							OmsReturnOrderFormState.NOTICE_CONFIRMED, omsReturnOrderForm.getExtInfo());
				}
			}
			startConfirmTime = list.get(list.size() - 1).getConfirmTime();
			list = omsReturnOrderFormService.getListByConfirmTimeAndState(startConfirmTime, state, limit);
		}

	}

	@Override
	public void pushOmsOrderFormToWarehose() {
		long minOrderId = 0L;
		OmsOrderFormState state = OmsOrderFormState.TOSEND;
		List<OmsOrderForm> list = omsOrderFormService
				.getOmsOrderFormListByStateWithMinOrderId(minOrderId, state, limit);
		while (!list.isEmpty()) {
			for (OmsOrderForm omsOrderForm : list) {
				try {
					omsOrderFormService.send(omsOrderForm);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			minOrderId = list.get(list.size() - 1).getOmsOrderFormId();
			list = omsOrderFormService.getOmsOrderFormListByStateWithMinOrderId(minOrderId, state, limit);
		}
	}

	@Override
	public void pushOmsShipOrderToWarehose() {
		long startCreateTime = System.currentTimeMillis();
		ShipStateType state = ShipStateType.UNSEDN;
		List<ShipOrderForm> list = shipOrderService.getListByCreateTime(startCreateTime, state, limit);
		while (!list.isEmpty()) {
			for (ShipOrderForm shipOrderForm : list) {
				try {
					shipOrderService.pushShipOrderToWarehose(shipOrderForm.getShipOrderId(),
							shipOrderForm.getSupplierId());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			startCreateTime = list.get(list.size() - 1).getCreateTime();
			list = shipOrderService.getListByCreateTime(startCreateTime, state, limit);
		}
	}

	@Override
	public void pushOmsOrderFormStateToApp() {
		// 1。批量获取已发货的订单
		long minOrderId = 0L;
		OmsOrderFormState state = OmsOrderFormState.SHIP;
		List<OmsOrderForm> list = omsOrderFormService
				.getOmsOrderFormListByStateWithMinOrderId(minOrderId, state, limit);
		Set<Long> userOrderFormIdSet = new TreeSet<Long>();
		while (!list.isEmpty()) {
			for (OmsOrderForm omsOrderForm : list) {
				try {
					long userId = omsOrderForm.getUserId();
					long omsOrderFormId = omsOrderForm.getOmsOrderFormId(), userOrderFormId = omsOrderForm
							.getUserOrderFormId();
					
					List<OrderServiceUpdatePackageIdParam> paramList = new ArrayList<OrderServiceUpdatePackageIdParam>();
					// 2.获取包裹信息
					List<OmsOrderPackage> packageList = omsOrderPackageService.getOmsOrderPackageListByOmsOrderFormId(
							omsOrderFormId, userId);

					for (OmsOrderPackage omsOrderPackage : packageList) {
						List<OmsOrderPackageSku> omsOrderPackageSkuList = omsOrderPackageService
								.getOmsOrderPackageSkuListByPackageId(omsOrderPackage.getPackageId(), userId);
						for (OmsOrderPackageSku omsOrderPackageSku : omsOrderPackageSkuList) {
							OrderServiceUpdatePackageIdParam param = new OrderServiceUpdatePackageIdParam();
							param.setSkuId(omsOrderPackageSku.getSkuId());
							param.setExpressCompany(omsOrderPackage.getExpressCompany());
							param.setMailNO(omsOrderPackage.getMailNO());
							param.setOrderId(omsOrderForm.getUserOrderFormId());
							param.setUserId(userId);
							param.setWarehouseId(omsOrderForm.getStoreAreaId());
							// 仓库
							param.setWarehouseName(warehouseService.getWarehouseById(omsOrderForm.getStoreAreaId())
									.getWarehouseName());
							paramList.add(param);
						}
					}
					boolean isSucc = true;
					// 3.推送给订单模块
					isSucc = isSucc && orderPackageSimpleService.updatePackageId(paramList);
					// 4.更新oms订单的状态
					isSucc = isSucc
							&& this.omsOrderFormService.updateOmsOrderFormState(omsOrderFormId,
									OmsOrderFormState.NOTICE_SHIP);

					// 5.推送发货信息给Mobile
					if (isSucc && !userOrderFormIdSet.contains(omsOrderFormId)) {
						logger.info("====send msg from pushOmsOrderFormStateToApp:"+omsOrderFormId);
						// use biz push interface to send message
						Map<String, Object> otherParamMap = new HashMap<String, Object>();
						otherParamMap.put(MessageCodeInfo.USER_FORM_ID, userOrderFormId);
						mobilePushManageFacade.pushForAll(userId, PushMessageType.send_order, omsOrderFormId,
								otherParamMap);
						
						//push消息
						mobilePushManageFacade.push(userId, PushMessageType.send_order, null, null, userOrderFormId);
						
						userOrderFormIdSet.add(omsOrderFormId);
					}
					
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
			minOrderId = list.get(list.size() - 1).getOmsOrderFormId();
			list = omsOrderFormService.getOmsOrderFormListByStateWithMinOrderId(minOrderId, state, limit);
		}

	}

	@Override
	public void pushOmsOrderPackageToApp() {
		// 批量获取待通知的包裹信息
		long startUpdateTime = System.currentTimeMillis();
		List<OmsOrderPackage> list = this.omsOrderPackageService.getUnFeedListByUpdateTime(startUpdateTime, limit);
		while (!list.isEmpty()) {
			for (OmsOrderPackage omsOrderPackage : list) {
				try {
					boolean isSucc = false;
					if (omsOrderPackage.getOmsOrderPackageState() == OmsOrderPackageState.DONE)
						isSucc = this.orderPackageSimpleService.setPackageToSignIn(omsOrderPackage.getMailNO(),
								omsOrderPackage.getExpressCompany());
					if (isSucc) {
						omsOrderPackageService.setPackageStateFeedBackToApp(omsOrderPackage.getPackageId());
					}

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			startUpdateTime = list.get(list.size() - 1).getPackageStateUpdateTime();
			list = this.omsOrderPackageService.getUnFeedListByUpdateTime(startUpdateTime, limit);
		}
	}

	@Override
	public void generatePickOrder() {
		Set<Long> processedSuppilerIdSet = new LinkedHashSet<Long>();
		long startCreateTime = OmsUtil.getCurrentBoci().getBociStartTime();
		long currentBociDeadLine = startCreateTime;
		List<PickSkuItemForm> list = this.pickSkuService.getUnPickListByCreateTime(startCreateTime, limit);
		while (!list.isEmpty()) {
			for (PickSkuItemForm pickSkuItemForm : list) {
				try {
					long supplierId = pickSkuItemForm.getSupplierId();
					if (processedSuppilerIdSet.contains(supplierId))
						continue;
					else
						processedSuppilerIdSet.add(supplierId);
					pickOrderService.createPickOrderFormAndShipOrder(supplierId, currentBociDeadLine);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			startCreateTime = list.get(list.size() - 1).getCreateTime();
			list = this.pickSkuService.getUnPickListByCreateTime(startCreateTime, limit);
		}

	}

	@Override
	public void cancelTimeOutOrderForm() {
		// 每天0点启动，获取三天之千最后一个拨次的时间
		long endTime = OmsUtil.getTimeOutTime();
		long[] timeRange = new long[] { 1415635200000L, endTime };
		long minOrderId = 0L;
		OmsOrderFormState[] omsOrderFormStateArray = new OmsOrderFormState[] { OmsOrderFormState.OMSRECVFAILED,
				OmsOrderFormState.SENT, OmsOrderFormState.RECVSUCCESS, OmsOrderFormState.PRINT, OmsOrderFormState.PICK };
		// 根据状态获取
		List<OmsOrderForm> list = this.omsOrderFormService.getOmsOrderFormListByStateWithMinOrderId(minOrderId,
				omsOrderFormStateArray, timeRange, limit);
		while (CollectionUtil.isNotEmptyOfList(list)) {
			for (OmsOrderForm omsOrderForm : list) {
				try {
					// 1.向仓库发出取消指令,OMSRECVFAILED状态的不需要向仓库推送指令
					boolean isSucc = true;
					OmsOrderFormState cancelState = OmsOrderFormState.TIMEOUT_CANCEL;
					if (omsOrderForm.getOmsOrderFormState() != OmsOrderFormState.OMSRECVFAILED) {
						isSucc = isSucc
								&& this.omsOrderFormService.sendCancelCommandToWarehouse(omsOrderForm);
					} else {
						cancelState = OmsOrderFormState.OMSRECVFAILED_CANCEL;
					}
					// 2.通知订单模块
					SetPackageToOutTimeParam param = new SetPackageToOutTimeParam();
					Collection<Long> skuIdColl = new ArrayList<Long>();
					for (OmsOrderFormSku sku : omsOrderForm.getOmsOrdeFormSkuList()) {
						skuIdColl.add(sku.getSkuId());
					}
					param.setUserId(omsOrderForm.getUserId());
					param.setOrderId(omsOrderForm.getUserOrderFormId());
					param.setSkuIdColl(skuIdColl);
					isSucc = isSucc && orderPackageSimpleService.setPackageToOutTime(param);
					// 3.变更订单状态
					isSucc = isSucc
							&& this.omsOrderFormService.updateOmsOrderFormState(omsOrderForm.getOmsOrderFormId(),
									cancelState);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			minOrderId = list.get(list.size() - 1).getOmsOrderFormId();
			list = 	this.omsOrderFormService.getOmsOrderFormListByStateWithMinOrderId(minOrderId, omsOrderFormStateArray,
					timeRange, limit);
		}
	}

	@Override
	public void cancelTimeOutShipOrder() {
		this.shipOrderService.cancelTimeOutShipOrder();
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.timer.facade.OmsTimerFacade#calcuateFreight()
	 */
	@Override
	public void calcuateFreight() {
		this.freightService.doCalcuateCache();
	}

}
