package com.xyl.mmall.timer.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.constant.CalendarConst;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.LoggerUtil;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.common.facade.ReturnPackageCommonFacade;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.InvoiceInOrdDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO;
import com.xyl.mmall.order.dto.InvoiceSkuSPDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;
import com.xyl.mmall.order.enums.InvoiceInOrdState;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.result.StCancelOrderSkuResult;
import com.xyl.mmall.order.service.InvoiceService;
import com.xyl.mmall.order.service.OrderMiscService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.timer.facade.InvoiceTimerFacade;

/**
 * 发票相关的定时器
 * 
 * @author dingmingliang
 * 
 */
@Service("invoiceTimerFacade")
public class InvoiceTimerFacadeImpl implements InvoiceTimerFacade {

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderMiscService orderMiscService;

	@Autowired
	private ReturnPackageCommonFacade returnPackageCommonFacade;

	@Autowired
	private ReturnPackageQueryService returnPackageQueryService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private long sepInvoiceTime = ConstValueOfOrder.SEP_INVOICE_TIME;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.common.service.ReloadServiceIpSingletonImpl#dealReload()
	 */
	public void convertOrdInvoToSuppInvo() {
		// 将订单发票转成商家的订单发票
		String className = "InvoiceTimer", methodName = "saveInvoiceInOrdSupplier";
		try {
			LoggerUtil.timerStart(className, methodName);
			saveInvoiceInOrdSupplier();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			LoggerUtil.timerEnd(className, methodName);
		}
	}

	/**
	 * 将订单发票转成商家的订单发票
	 */
	private void saveInvoiceInOrdSupplier() {
		// 1.读取时间段内,未处理的发票信息
		int limit = 500;
		long currTime = System.currentTimeMillis(), minOrderId = 0, endTime = currTime - sepInvoiceTime;
		long[] producedTimeRange = new long[] { endTime - CalendarConst.MONTH_EST_TIME * 2, endTime };
		List<InvoiceInOrdDTO> invoiceInOrdDTOList = invoiceService.getInvoiceInOrdByOrderTimeRange(minOrderId,
				InvoiceInOrdState.INIT, producedTimeRange, limit);

		while (CollectionUtil.isNotEmptyOfCollection(invoiceInOrdDTOList)) {
			// 2.循环处理发票信息
			for (InvoiceInOrdDTO invoiceInOrdDTO : invoiceInOrdDTOList) {
				long orderId = invoiceInOrdDTO.getOrderId();
				try {
					saveInvoiceInOrdSupplier(currTime, invoiceInOrdDTO);
				} catch (Exception ex) {
					logger.error("orderId=" + orderId + ". " + ex.getMessage());
				}
			}
			minOrderId = invoiceInOrdDTOList.get(invoiceInOrdDTOList.size() - 1).getOrderId();
			invoiceInOrdDTOList = invoiceService.getInvoiceInOrdByOrderTimeRange(minOrderId, InvoiceInOrdState.INIT,
					producedTimeRange, limit);
		}
	}

	/**
	 * 根据订单发票,生成并保存商家的订单发票
	 * 
	 * @param currTime
	 * @param invoiceInOrdDTO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean saveInvoiceInOrdSupplier(long currTime, InvoiceInOrdDTO invoiceInOrdDTO) {
		// 1 读取订单信息
		long userId = invoiceInOrdDTO.getUserId(), orderId = invoiceInOrdDTO.getOrderId();
		Boolean isVisible = null;
		OrderFormDTO orderDTO = orderService.queryOrderForm(userId, orderId, isVisible);
		if (orderDTO == null) {
			logger.error("orderDTO==null, " + invoiceInOrdDTO.toString());
			return false;
		}
		// 2.判断订单状态,从而决定是否可以开发票
		if (orderDTO.getOrderFormState() == OrderFormState.CANCEL_ING) {
			logger.error("OrderFormState.CANCEL_ING, " + invoiceInOrdDTO.toString());
			return false;
		}
		// CASE1: 订单取消
		boolean isCancelOfOrder = orderDTO.getOrderFormState() == OrderFormState.CANCEL_ED;
		// CASE2: 包裹都取消了
//		boolean isCancelOfAllPackage = true, isDeliveingOfPackage = false;
//		for (OrderPackageDTO opDTO : orderDTO.getOrderPackageDTOList()) {
//			OrderPackageState opState = opDTO.getOrderPackageState();
//			isCancelOfAllPackage = isCancelOfAllPackage && OrderPackageState.isCancel(opState);
//			isDeliveingOfPackage = isDeliveingOfPackage
//					|| (opState == OrderPackageState.INIT || opState == OrderPackageState.WAITING_SIGN_IN);
//		}
		boolean isUnkp = isCancelOfOrder;// || isCancelOfAllPackage;
		if (isUnkp) {
			invoiceInOrdDTO.setState(InvoiceInOrdState.UN_KP);
			return invoiceService.saveInvoice(invoiceInOrdDTO, null);
		}
//		if (isDeliveingOfPackage) {
//			logger.info("isDeliveingOfPackage==true, " + invoiceInOrdDTO.toString());
//			return true;
//		}

		// 3.根据退货状态,判断是否可以生成商家发票
		// 3.1 根据退货处理中的数量判断
//		int countOfDealingReturn = returnPackageQueryService.queryWaitingReturningPackageByUserIdAndOrderId(userId,
//				orderId);
//		if (countOfDealingReturn > 0) {
//			logger.info("countOfDealingReturn=" + countOfDealingReturn + " ," + invoiceInOrdDTO.toString());
//			return true;
//		}
		// 3.2 根据退货申请的最迟时间点判断
//		long deadline = returnPackageCommonFacade.getDeadlineOfApplyReturn(orderDTO);
//		if (deadline > currTime || deadline <= 0) {
//			logger.info("deadline > currTime || deadline <= 0," + invoiceInOrdDTO.toString());
//			return true;
//		}

		// 4.获得已经取消的OrderSku明细(包含包裹取消和退货)
		RetArg retArg1 = orderMiscService.stCancelOrderSku(orderId, userId, null);
		List<StCancelOrderSkuResult> stCancelOrderSkuResultList = (List<StCancelOrderSkuResult>) RetArgUtil.get(
				retArg1, ArrayList.class);
		if (RetArgUtil.get(retArg1, Integer.class) != Integer.valueOf(1)) {
			logger.error("orderMiscService.stCancelOrderSku(orderId, userId, null) error! "
					+ invoiceInOrdDTO.toString());
			return false;
		}

		// 5.按照商家维度,划分OrderSku信息(去掉退货商品)
		Map<Long, List<OrderSkuDTO>> supplierAndOrderSkuMap = genSupplierAndOrderSkuMap(orderDTO,
				stCancelOrderSkuResultList);

		// 6.按商家生成发票信息
		List<InvoiceInOrdSupplierDTO> invoiceInOrdSupplierDTOList = new ArrayList<>();
		if (CollectionUtil.isNotEmptyOfMap(supplierAndOrderSkuMap)) {
			for (Long supplierId : supplierAndOrderSkuMap.keySet()) {
				// 6.1 计算发票金额
				List<OrderSkuDTO> orderSkuDTOList = supplierAndOrderSkuMap.get(supplierId);
				BigDecimal realCash = BigDecimal.ZERO;
				for (OrderSkuDTO orderSkuDTO : orderSkuDTOList) {
					realCash = realCash.add(orderSkuDTO.getRprice().subtract(orderSkuDTO.getRedSPrice()).multiply(new BigDecimal(orderSkuDTO.getTotalCount())));
				}
				if (realCash.compareTo(BigDecimal.ZERO) <= 0)
					continue;
				String skuSnapshots = genSkuSnapshots(orderSkuDTOList);

				// 6.2 生成商家的订单发票
				InvoiceInOrdSupplierDTO invoiceInOrdSupplierDTO = new InvoiceInOrdSupplierDTO();
				invoiceInOrdSupplierDTO.setCash(realCash);
				invoiceInOrdSupplierDTO.setCtime(currTime);
				invoiceInOrdSupplierDTO.setOrderId(orderId);
				invoiceInOrdSupplierDTO.setOrderTime(orderDTO.getOrderTime());
				invoiceInOrdSupplierDTO.setState(InvoiceInOrdSupplierState.INIT);
				invoiceInOrdSupplierDTO.setSupplierId(supplierId);
				invoiceInOrdSupplierDTO.setUserId(userId);
				invoiceInOrdSupplierDTO.setSkuSnapshots(skuSnapshots);
				invoiceInOrdSupplierDTO.setTitle(invoiceInOrdDTO.getTitle());
				invoiceInOrdSupplierDTOList.add(invoiceInOrdSupplierDTO);
			}
		}
		// 7.根据商家的订单发票,设置发票状态
		if (CollectionUtil.isEmptyOfCollection(invoiceInOrdSupplierDTOList)) {
			invoiceInOrdDTO.setState(InvoiceInOrdState.UN_KP_AMOUNT0);
		} else
			invoiceInOrdDTO.setState(InvoiceInOrdState.KP_ING);
		// 8.保存发票结果
		return invoiceService.saveInvoice(invoiceInOrdDTO, invoiceInOrdSupplierDTOList);
	}

	/**
	 * @param orderSkuDTOList
	 * @return
	 */
	private String genSkuSnapshots(List<OrderSkuDTO> orderSkuDTOList) {
		List<InvoiceSkuSPDTO> invoiceSkuSPDTOList = new ArrayList<>();
		if (CollectionUtil.isNotEmptyOfCollection(orderSkuDTOList)) {
			for (OrderSkuDTO orderSkuDTO : orderSkuDTOList) {
				SkuSPDTO skuSPDTO = orderSkuDTO.getSkuSPDTO();
				InvoiceSkuSPDTO invoiceSkuSPDTO = new InvoiceSkuSPDTO();
				invoiceSkuSPDTO.setBrandName(skuSPDTO.getBrandName());
				invoiceSkuSPDTO.setColorName(skuSPDTO.getColorName());
				invoiceSkuSPDTO.setCount(orderSkuDTO.getTotalCount());
				invoiceSkuSPDTO.setProductName(skuSPDTO.getProductName());
				invoiceSkuSPDTO.setRprice(orderSkuDTO.getRprice().subtract(orderSkuDTO.getRedSPrice()));
				invoiceSkuSPDTO.setSkuId(orderSkuDTO.getSkuId());
				invoiceSkuSPDTO.setSkuSpecValueMap(skuSPDTO.getSkuSpecValueMap());
				invoiceSkuSPDTO.setOrderSkuId(orderSkuDTO.getId());
				invoiceSkuSPDTOList.add(invoiceSkuSPDTO);
			}
		}
		return JsonUtils.toJson(invoiceSkuSPDTOList);
	}

	/**
	 * 按照商家维度,划分OrderSku信息(去掉取消商品)
	 * 
	 * @param orderDTO
	 * @param stCancelOrderSkuResultList
	 * @return
	 */
	private Map<Long, List<OrderSkuDTO>> genSupplierAndOrderSkuMap(OrderFormDTO orderDTO,
			List<StCancelOrderSkuResult> stCancelOrderSkuResultList) {
		// 1.针对取消数据为空的特殊处理
		if (CollectionUtil.isEmptyOfCollection(stCancelOrderSkuResultList)) {
			Map<Long, List<OrderSkuDTO>> supplierAndOrderSkusMap = CollectionUtil.convertCollToListMap(
					orderDTO.getAllOrderSkuDTOList(), "supplierId");
			return supplierAndOrderSkusMap;
		}

		// 2.根据取消记录,修改OrderSkuDTO.Count字段
		Map<Long, OrderSkuDTO> orderSkuIdAndOrderSkuMap = CollectionUtil.convertCollToMap(
				orderDTO.getAllOrderSkuDTOList(), "id");
		for (StCancelOrderSkuResult stCancelOrderSkuResult : stCancelOrderSkuResultList) {
			// 2.2.1 读取退货明细对应的OrderSkuDTO
			long orderSkuId = stCancelOrderSkuResult.getOrderSkuId();
			OrderSkuDTO orderSkuDTO = orderSkuIdAndOrderSkuMap.get(orderSkuId);
			if (orderSkuDTO == null)
				continue;

			// 2.2.2 更新OrderSkuDTO的售出数量
			int returnCount = stCancelOrderSkuResult.getCount();
			returnCount = returnCount >= 0 ? returnCount : 0;
			int count = orderSkuDTO.getTotalCount() - returnCount;
			orderSkuDTO.setTotalCount(count);
			// 2.2.3 移除售出数量为0的OrderSkuDTO
			if (count <= 0)
				orderSkuIdAndOrderSkuMap.remove(orderSkuId);
		}
		// 3.重新生成supplierId维度的OrderSkuDTO集合
		Map<Long, List<OrderSkuDTO>> supplierAndOrderSkusMap = CollectionUtil.convertCollToListMap(
				orderSkuIdAndOrderSkuMap.values(), "supplierId");
		return supplierAndOrderSkusMap;
	}
}
