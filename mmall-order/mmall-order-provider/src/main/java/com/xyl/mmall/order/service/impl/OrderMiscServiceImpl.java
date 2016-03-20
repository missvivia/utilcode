package com.xyl.mmall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.order.dao.CancelOmsOrderTaskDao;
import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.dao.OrderPackageDao;
import com.xyl.mmall.order.dao.OrderRefundExpDao;
import com.xyl.mmall.order.dao.OrderSkuDao;
import com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.CancelOmsOrderTaskState;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.meta.CancelOmsOrderTask;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.OrderPackage;
import com.xyl.mmall.order.meta.OrderRefundExp;
import com.xyl.mmall.order.meta.OrderSku;
import com.xyl.mmall.order.param.OrderSkuQueryParam1;
import com.xyl.mmall.order.result.StCancelOrderSkuResult;
import com.xyl.mmall.order.service.OrderMiscService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.order.util.OrderInstantiationUtil;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * @author dingmingliang
 * 
 */
@Service("orderMiscService")
public class OrderMiscServiceImpl implements OrderMiscService {

	@Autowired
	private OrderFormDao orderFormDao;

	@Autowired
	private OrderPackageDao orderPackageDao;

	@Autowired
	private OrderSkuDao orderSkuDao;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRefundExpDao orderRefundExpDao;

	@Autowired
	private CancelOmsOrderTaskDao cancelOmsOrderTaskDao;

	@Autowired
	private ReturnPackageQueryService returnPackageQueryService;

	@Autowired
	private OrderInstantiationUtil orderInstantiationUtil;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderMiscService#getOrderSkuDTOListMap(long,
	 *      java.util.Collection)
	 */
	public Map<Long, List<OrderSkuDTO>> getOrderSkuDTOListMap(long poId, Collection<OrderSkuQueryParam1> paramColl) {
		Map<Long, List<OrderSkuDTO>> resultMap = new TreeMap<Long, List<OrderSkuDTO>>();
		for (OrderSkuQueryParam1 param : paramColl) {
			// 1.读取订单名下的OrderSku记录
			long orderId = param.getOrderId(), userId = param.getUserId();
			List<OrderSku> orderSkuList = orderSkuDao.getListByOrderId(orderId, userId, null);
			// 2.过滤非poId的记录
			Map<Long, List<OrderSku>> orderSkuMap = CollectionUtil.convertCollToListMap(orderSkuList, "poId");
			orderSkuList = CollectionUtil.getValueOfMap(orderSkuMap, poId);
			if (CollectionUtil.isEmptyOfCollection(orderSkuList))
				continue;
			List<OrderSkuDTO> orderSkuDTOList = ReflectUtil.convertList(OrderSkuDTO.class, orderSkuList, false);
			// 3.设置结果
			resultMap.put(orderId, orderSkuDTOList);
		}

		// 返回结果
		return resultMap;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderMiscService#stCancelOrderSku(long,
	 *      long, java.lang.Long)
	 */
	@SuppressWarnings({ "unchecked" })
	public RetArg stCancelOrderSku(long orderId, long userId, Long poId) {
		RetArg retArg = new RetArg();
		// 0.判断订单是否存在
		OrderForm order = orderFormDao.getObjectByIdAndUserId(orderId, userId);
		if (order == null) {
			RetArgUtil.put(retArg, 0);
			return retArg;
		}

		// 1.生成订单名下全部的取消信息
		// 1.1 读取所有包裹取消的记录
		RetArg retArgOfPackageCancel = queryOrderSkuListByPackageCancel(order);
		List<OrderSku> orderSkuListOfAll = (List<OrderSku>) RetArgUtil.get(retArgOfPackageCancel, ArrayList.class, 0);
		if (CollectionUtil.isEmptyOfCollection(orderSkuListOfAll)) {
			RetArgUtil.put(retArg, 0);
			return retArg;
		}
		Map<OrderPackage, List<OrderSku>> orderPackageMapOfCancel = RetArgUtil
				.get(retArgOfPackageCancel, HashMap.class);
		// Key:orderSkuId, Value:OrderSkuDTO
		Map<Long, OrderSkuDTO> orderSkuDTOMap = new TreeMap<>();
		for (OrderSku orderSku : orderSkuListOfAll) {
			orderSkuDTOMap.put(orderSku.getId(), new OrderSkuDTO(orderSku));
		}
		// 1.2 读取所有退货记录
		List<ReturnPackageDTO> returnPackageDTOList = returnPackageQueryService
				.querySuccessfullyReturnedPackageByUserIdAndOrderId(userId, orderId, null);
		// 1.3 合并结果为StCancelOrderSkuResult
		List<StCancelOrderSkuResult> resultListOfAll = convertToStCancelOrderSkuResultList(order,
				orderPackageMapOfCancel, returnPackageDTOList, orderSkuDTOMap);
		if (CollectionUtil.isEmptyOfCollection(resultListOfAll)) {
			RetArgUtil.put(retArg, 1);
			return retArg;
		}

		// 2.根据poId过滤结果
		List<StCancelOrderSkuResult> resultList = null;
		if (poId == null)
			resultList = resultListOfAll;
		else {
			Map<Long, List<StCancelOrderSkuResult>> resultMapOfAll = CollectionUtil.convertCollToListMap(
					resultListOfAll, "poId");
			resultList = CollectionUtil.getValueOfMap(resultMapOfAll, poId);
		}
		// 3.返回
		RetArgUtil.put(retArg, 1);
		RetArgUtil.put(retArg, resultList);
		return retArg;
	}

	/**
	 * 合并结果为StCancelOrderSkuResult
	 * 
	 * @param order
	 * @param orderPackageMapOfCancel
	 * @param returnPackageDTOList
	 * @param orderSkuDTOMap
	 * @return
	 */
	private List<StCancelOrderSkuResult> convertToStCancelOrderSkuResultList(OrderForm order,
			Map<OrderPackage, List<OrderSku>> orderPackageMapOfCancel, List<ReturnPackageDTO> returnPackageDTOList,
			Map<Long, OrderSkuDTO> orderSkuDTOMap) {
		// 0.读取OrderRefundExp和OrderCancelInfoDTO对象
		OrderSkuDTO orderSkuDTOOfFirst = CollectionUtil.getFirstValueOfMap(orderSkuDTOMap);
		long orderId = orderSkuDTOOfFirst.getOrderId(), userId = orderSkuDTOOfFirst.getUserId();
		OrderRefundExp orderRefundExp = orderRefundExpDao.getObjectByIdAndUserId(orderId, userId);
		OrderCancelInfoDTO cancelDTO = orderService.getOrderCancelInfo(userId, orderId);

		List<StCancelOrderSkuResult> resultListOfAll = new ArrayList<>();
		BigDecimal refundExp = orderRefundExp != null && orderRefundExp.getExpPrice() != null ? orderRefundExp
				.getExpPrice() : BigDecimal.ZERO;
		// 1.处理orderSkuList
		if (CollectionUtil.isNotEmptyOfMap(orderPackageMapOfCancel)) {
			boolean isOrderCancel = OrderFormState.isCancel(order.getOrderFormState());
			for (Entry<OrderPackage, List<OrderSku>> entry : orderPackageMapOfCancel.entrySet()) {
				OrderPackage op = entry.getKey();
				for (OrderSku orderSku : entry.getValue()) {
					OrderSkuDTO orderSkuDTO = orderSkuDTOMap.get(orderSku.getId());
					StCancelOrderSkuResult resultItem = StCancelOrderSkuResult.genInstance(orderSkuDTO);
					resultItem.setCancelTime(op.getCancelTime());
					if (resultItem.getCancelTime() <= 0 && cancelDTO != null)
						resultItem.setCancelTime(cancelDTO.getCtime());
					resultItem.setRefundExp(refundExp);
					resultItem.setRefundReason(isOrderCancel && cancelDTO != null ? cancelDTO.getReason() : op
							.getOrderPackageState().getName());
					String refundType = cancelDTO == null ? "原路退回" : cancelDTO.getRtype().getDesc();
					resultItem.setRefundType(refundType);
					resultListOfAll.add(resultItem);
				}
			}
		}
		// 2.处理returnPackageDTOList
		if (CollectionUtil.isNotEmptyOfCollection(returnPackageDTOList)) {
			for (ReturnPackageDTO returnPackageDTO : returnPackageDTOList) {
				if (CollectionUtil.isEmptyOfMap(returnPackageDTO.getRetOrdSkuMap())) {
					logger.error("returnPackageDTO.getRetOrdSkuMap()==null");
					continue;
				}
				for (ReturnOrderSkuDTO returnOrderSkuDTO : returnPackageDTO.getRetOrdSkuMap().values()) {
					OrderSkuDTO orderSkuDTO = orderSkuDTOMap.get(returnOrderSkuDTO.getOrderSkuId());
					StCancelOrderSkuResult resultItem = StCancelOrderSkuResult.genInstance(orderSkuDTO,
							returnOrderSkuDTO);
					resultItem.setCancelTime(returnPackageDTO.getReturnOperationTime());
					resultItem.setRefundExp(refundExp);
					resultItem.setRefundReason(returnOrderSkuDTO.getReason());
					resultItem.setRefundType(returnPackageDTO.getRefundType().getDesc());
					resultListOfAll.add(resultItem);
				}
			}
		}
		return resultListOfAll;
	}

	/**
	 * 读取所有包裹取消的记录
	 * 
	 * @param order
	 * @return RetArg.ArrayList[0]: List(OrderSku) 订单对应的全部OrderSku<br>
	 *         RetArg.ArrayList[1]: List(OrderSku) 包裹取消对应的OrderSku<br>
	 *         RetArg.HashMap: Map(Key:OrderPackage,Value:List(OrderSku))
	 *         取消的包裹明细
	 */
	private RetArg queryOrderSkuListByPackageCancel(OrderForm order) {
		// 1.读取订单记录和包裹记录
		long orderId = order.getOrderId(), userId = order.getUserId();
		boolean isOrderCancel = OrderFormState.isCancel(order.getOrderFormState());
//		List<OrderPackage> opList = orderPackageDao.getListByOrderId(userId, orderId);
//		boolean isEmptyPackage = CollectionUtil.isEmptyOfCollection(opList);
		List<OrderSku> orderSkuListOfAll = new ArrayList<>();
		List<OrderSku> orderSkuList = new ArrayList<>();
		Map<OrderPackage, List<OrderSku>> orderPackageMap = new HashMap<OrderPackage, List<OrderSku>>();
		// 整个包裹取消的特殊处理
		if (isOrderCancel) {
			OrderFormDTO orderDTO = orderService.queryOrderForm(userId, orderId, null);
			List<OrderSkuDTO> orderSkuDTOListOfAll = orderDTO.getAllOrderSkuDTOList();
			// 读取订单对应的OrderSku记录
			orderSkuListOfAll = CollectionUtil.convertList(orderSkuDTOListOfAll, OrderSku.class);
			orderSkuList = CollectionUtil.addAllOfList(null, orderSkuListOfAll);
			//orderPackageMap.put(orderDTO.getOrderPackageDTOList().get(0), orderSkuList);
		} else {
			// 3.读取订单对应的OrderSku记录
			orderSkuListOfAll = orderSkuDao.getListByOrderId(orderId, userId, null);
			if (CollectionUtil.isEmptyOfCollection(orderSkuListOfAll))
				return null;
			// 4.生成有包裹取消标记的OrderSku记录
//			Map<Long, List<OrderSku>> orderSkuMap = CollectionUtil.convertCollToListMap(orderSkuListOfAll, "packageId");
//			for (OrderPackage op : opList) {
//				if (!OrderPackageState.isCancel(op.getOrderPackageState()))
//					continue;
//				List<OrderSku> orderSkuListOfTmp = orderSkuMap.get(op.getPackageId());
//				orderPackageMap.put(op, orderSkuListOfTmp);
//				CollectionUtil.addAllOfList(orderSkuList, orderSkuListOfTmp);
//			}
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, orderSkuListOfAll, 0);
		RetArgUtil.put(retArg, orderSkuList, 1);
	//	RetArgUtil.put(retArg, orderPackageMap);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#addCancelOmsOrderTask(com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO)
	 */
	@Transaction
	public CancelOmsOrderTaskDTO addCancelOmsOrderTask(CancelOmsOrderTaskDTO cancelTaskDTO) {
		long orderId = cancelTaskDTO.getOrderId(), userId = cancelTaskDTO.getUserId();
		// 0.获得订单锁
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + orderId + " ,userId="
					+ userId);
			return null;
		}
		// 1.查询订单信息		
		OrderForm order = RetArgUtil.get(retArgOfTmp, OrderForm.class);
		OrderFormState oldOFState = order != null ? order.getOrderFormState() : null;
		// 判断是否允许更新
		if (!OrderFormState.canCancel(oldOFState)) {
			logger.error("!OrderFormState.canCancel(oldOFState), orderId=" + orderId + " ,userId=" + userId);
			return null;
		}
		OrderFormDTO orderDTO = orderService.queryOrderForm(userId, orderId, null);
		if(!OrderUtil.canCancel(orderDTO)){
			logger.error("!OrderUtil.canCancel(orderDTO), orderId=" + orderId + " ,userId=" + userId);
			return null;
		}

		boolean isSucc = true;
		// 2.更新订单状态为 WAITING_CANCEL_OMSORDER
		order.setOrderFormState(OrderFormState.WAITING_CANCEL_OMSORDER);
		isSucc = isSucc && orderFormDao.updateOrdState(order, new OrderFormState[] { oldOFState });
		// 3.添加取消记录
		cancelTaskDTO.setOldOrderFormState(oldOFState);
		isSucc = isSucc && cancelOmsOrderTaskDao.addObject(cancelTaskDTO) != null;

		if (!isSucc) {
			throw new ServiceNoThrowException("orderId=" + orderId);
		}
		return isSucc ? cancelTaskDTO : null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderMiscService#queryCancelOmsOrderTaskDTOList(long,
	 *      com.xyl.mmall.order.enums.CancelOmsOrderTaskState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<CancelOmsOrderTaskDTO> queryCancelOmsOrderTaskDTOList(long minOrderId,
			CancelOmsOrderTaskState[] taskStateArray, DDBParam param) {
		// 1.查询原始数据
		param = OrderUtil.initDDBParamWithMinOrderId(param);
		List<CancelOmsOrderTask> metaList = cancelOmsOrderTaskDao.queryCancelOmsOrderTaskListWithMinOrderId(minOrderId,
				taskStateArray, param);
		// 2.转换meta对象为DTO对象
		List<CancelOmsOrderTaskDTO> dtoList = convertToCancelOmsOrderTaskDTOList(metaList);
		return dtoList;
	}

	/**
	 * @param metaList
	 * @return
	 */
	private List<CancelOmsOrderTaskDTO> convertToCancelOmsOrderTaskDTOList(List<CancelOmsOrderTask> metaList) {
		if (CollectionUtil.isEmptyOfCollection(metaList))
			return null;
		List<CancelOmsOrderTaskDTO> dtoList = new ArrayList<>();
		for (CancelOmsOrderTask meta : metaList) {
			CancelOmsOrderTaskDTO dto = new CancelOmsOrderTaskDTO(meta);
			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderMiscService#updateCancelOmsOrderTaskState(com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO,
	 *      com.xyl.mmall.order.enums.CancelOmsOrderTaskState)
	 */
	public boolean updateCancelOmsOrderTaskState(CancelOmsOrderTaskDTO cancelTaskDTO, CancelOmsOrderTaskState oldState) {
		return cancelOmsOrderTaskDao.updateCancelOmsOrderTaskState(cancelTaskDTO, oldState);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderMiscService#queryCancelOmsOrderTaskDTO(long,
	 *      long)
	 */
	public CancelOmsOrderTaskDTO queryCancelOmsOrderTaskDTO(long orderId, long userId) {
		CancelOmsOrderTask meta = cancelOmsOrderTaskDao.queryCancelOmsOrderTask(orderId, userId);
		CancelOmsOrderTaskDTO dto = meta != null ? new CancelOmsOrderTaskDTO(meta) : null;
		return dto;
	}
}
