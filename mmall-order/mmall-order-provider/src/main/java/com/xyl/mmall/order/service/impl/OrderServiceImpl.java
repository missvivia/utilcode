package com.xyl.mmall.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.ExtInfoFieldUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.aop.OperateLog;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.order.api.util.OrderApiUtil;
import com.xyl.mmall.order.api.util.TradeApiUtil;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dao.CODAuditLogDao;
import com.xyl.mmall.order.dao.InvoiceDao;
import com.xyl.mmall.order.dao.OrderCancelInfoDao;
import com.xyl.mmall.order.dao.OrderCartItemDao;
import com.xyl.mmall.order.dao.OrderExpInfoDao;
import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.dao.OrderLogisticsDao;
import com.xyl.mmall.order.dao.OrderOperateLogDao;
import com.xyl.mmall.order.dao.OrderPackageDao;
import com.xyl.mmall.order.dao.OrderPackageRefundDao;
import com.xyl.mmall.order.dao.OrderSkuCartItemDao;
import com.xyl.mmall.order.dao.OrderSkuDao;
import com.xyl.mmall.order.dao.TradeItemDao;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderFormExtInfoDTO;
import com.xyl.mmall.order.dto.OrderFormPayMethodDTO;
import com.xyl.mmall.order.dto.OrderLogisticsDTO;
import com.xyl.mmall.order.dto.OrderOperateLogDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderPackageRefundDTO;
import com.xyl.mmall.order.dto.OrderSkuCartItemDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.CODAuditState;
import com.xyl.mmall.order.enums.OperateLogType;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderCancelState;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.CODAuditLog;
import com.xyl.mmall.order.meta.Invoice;
import com.xyl.mmall.order.meta.OrderCancelInfo;
import com.xyl.mmall.order.meta.OrderCartItem;
import com.xyl.mmall.order.meta.OrderExpInfo;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.OrderLogistics;
import com.xyl.mmall.order.meta.OrderOperateLog;
import com.xyl.mmall.order.meta.OrderPackage;
import com.xyl.mmall.order.meta.OrderPackageRefund;
import com.xyl.mmall.order.meta.OrderSku;
import com.xyl.mmall.order.meta.OrderSkuCartItem;
import com.xyl.mmall.order.meta.TradeItem;
import com.xyl.mmall.order.param.OrderExpInfoChangeParam;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.order.param.OrderServiceSetStateToEPayedParam;
import com.xyl.mmall.order.service.InvoiceService;
import com.xyl.mmall.order.service.OrderCalService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.TradeInternalService;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.order.util.OrderInstantiationUtil;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * @author dingmingliang
 * 
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected OrderFormDao orderFormDao;

	@Autowired
	protected OrderExpInfoDao orderExpInfoDao;

	@Autowired
	protected OrderPackageDao orderPackageDao;

	@Autowired
	protected OrderCartItemDao orderCartItemDao;

	@Autowired
	protected OrderSkuCartItemDao orderSkuCartItemDao;

	@Autowired
	protected OrderPackageRefundDao orderPackageRefundDao;

	@Autowired
	protected OrderSkuDao orderSkuDao;

	@Autowired
	protected TradeItemDao tradeItemDao;

	@Autowired
	protected OrderCancelInfoDao orderCancelInfoDao;

	@Autowired
	protected CODAuditLogDao codAuditLogDao;

	@Autowired
	protected InvoiceService invoiceService;

	@Autowired
	protected OrderCalService orderCalService;

	@Autowired
	protected OrderInternalServiceImpl orderInternalServiceImpl;

	@Autowired
	protected TradeService tradeService;

	@Autowired
	protected TradeInternalService tradeInternalService;

	@Autowired
	protected OrderInstantiationUtil orderInstantiationUtil;

	@Autowired
	protected OrderLogisticsDao orderLogisticsDao;

	@Autowired
	private OrderOperateLogDao orderOperateLogDao;

	@Autowired
	private InvoiceDao invoiceDao;

	/**
	 * appendExtInfo的参数
	 * 
	 * @author dingmingliang
	 * 
	 */
	private static class AppendExtInfoParam {

		OrderExpInfo orderExpInfo;

		InvoiceInOrdDTO invoiceInOrdDTO;

		List<OrderPackage> orderPackageList;

		List<OrderCartItem> orderCartItemList;

		List<OrderSku> orderSkuList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#allocateRecordId()
	 */
	public long allocateRecordId() {
		return orderFormDao.allocateRecordId();
	}

	/**
	 * 获取与订单关联的配送地址
	 * 
	 * @param orderId
	 * @param userId
	 * @param param
	 * @return
	 */
	private OrderExpInfoDTO getOrderExpInfoByOrderId(long orderId, long userId, AppendExtInfoParam param) {
		// 优先读取Param里的参数,减少数据库读取操作
		OrderExpInfo orderExpInfo = param != null ? param.orderExpInfo : null;
		orderExpInfo = orderExpInfo != null && orderExpInfo.getUserId() == userId
				&& orderExpInfo.getOrderId() == orderId ? orderExpInfo : null;
		// Param里的参数不正确or为空,从数据库里读取
		if (orderExpInfo == null) {
			orderExpInfo = new OrderExpInfo();
			orderExpInfo.setOrderId(orderId);
			orderExpInfo.setUserId(userId);
			orderExpInfo = orderExpInfoDao.getObjectByPrimaryKeyAndPolicyKey(orderExpInfo);
			if (null == orderExpInfo) {
				return null;
			}
		}

		OrderExpInfoDTO orderExpInfoDTO = new OrderExpInfoDTO(orderExpInfo);
		return orderExpInfoDTO;
	}

	/**
	 * 读取OrderCartItemDTO集合
	 * 
	 * @param userId
	 * @param orderId
	 * @param orderSkuDTOList
	 * @param param
	 * @return
	 */
	private List<OrderCartItemDTO> getOrderCartItemDTOListByOrderId(long userId, long orderId,
			List<OrderSkuDTO> orderSkuDTOList, AppendExtInfoParam param) {
		// 1.读取全部的OrderCartItem记录
		List<OrderCartItem> orderCartItemList = param != null ? param.orderCartItemList : orderCartItemDao
				.getListByOrderId(userId, orderId);
		if (CollectionUtil.isEmptyOfCollection(orderCartItemList)) {
			return null;
		}
		Map<Long, OrderCartItem> orderCartItemMap = CollectionUtil.convertCollToMap(orderCartItemList, "id");
		// 2.以OrderCartItemId为Key,转换OrderSkuDTO
		Map<Long, List<OrderSkuDTO>> orderSkuDTOMap = CollectionUtil.convertCollToListMap(orderSkuDTOList,
				"orderCartItemId");

		// 3.读取全部的OrderSkuCartItem记录(由于表结构里没有orderId,暂时不从param里读取)
		List<OrderSkuCartItem> orderSkuCartItemList = orderSkuCartItemDao.getListByOrderCartItemIdsAndUserId(userId,
				orderCartItemMap.keySet());
		Map<Long, OrderSkuCartItem> orderSkuCartItemMap = CollectionUtil.convertCollToMap(orderSkuCartItemList,
				"orderCartItemId");

		// 4.填充OrderSku信息到OrderCartItem上
		List<OrderCartItemDTO> orderCartItemDTOList = new ArrayList<OrderCartItemDTO>(orderCartItemList.size());
		for (OrderCartItem oci : orderCartItemList) {
			long orderCartItemId = oci.getId();
			// 4.1 购物车的详细数据源
			List<OrderSkuDTO> orderSkuDTOListOfTmp = orderSkuDTOMap.get(orderCartItemId);
			// 4.2 订单明细-普通商品Sku
			OrderSkuCartItem oscItem = CollectionUtil.getValueOfMap(orderSkuCartItemMap, orderCartItemId);
			OrderSkuCartItemDTO oscItemDTO = OrderSkuCartItemDTO.genInstance(oscItem);
			// 4.3 创建OrderCartItemDTO
			OrderCartItemDTO ociDTO = new OrderCartItemDTO(oci);
			ociDTO.setOrderSkuDTOList(orderSkuDTOListOfTmp);
			ociDTO.setOrderSkuCartItemDTO(oscItemDTO);
			// 4.4 添加到返回结果中
			orderCartItemDTOList.add(ociDTO);
		}
		return orderCartItemDTOList;
	}

	/**
	 * 读取物流信息
	 * 
	 * @param orderId
	 * @return
	 */
	private List<OrderLogisticsDTO> getOrderLogisticsDTOListByOrderId(long orderId) {
		List<OrderLogistics> orderLogisticList = orderLogisticsDao.getOrderLogisticsByOrderIds(orderId);
		List<OrderLogisticsDTO> orderLogisticsDTOs = new ArrayList<OrderLogisticsDTO>();
		if (CollectionUtil.isNotEmptyOfList(orderLogisticList)) {
			for (OrderLogistics orderLogistics : orderLogisticList) {
				orderLogisticsDTOs.add(new OrderLogisticsDTO(orderLogistics));
			}
		}
		return orderLogisticsDTOs;
	}

	/**
	 * 读取包裹信息
	 * 
	 * @param userId
	 * @param orderId
	 * @param cartList
	 * @param orderFormDTO
	 * @param param
	 * @return
	 */
	private List<OrderPackageDTO> getOrderPackageDTOListByOrderId(long userId, long orderId,
			List<OrderCartItemDTO> cartList, OrderFormDTO orderFormDTO, AppendExtInfoParam param) {
		// // 1.读取全部的包裹信息
		List<OrderPackage> orderPackageList = param != null ? param.orderPackageList : orderPackageDao
				.getListByOrderId(userId, orderId);

		Map<Long, OrderPackage> orderPackageMap = CollectionUtil.convertCollToMap(orderPackageList, "packageId");
		// // 2.读取全部包裹的退款记录
		Map<Long, OrderPackageRefund> opRefundMap = genOrderPackageRefundMap(userId, orderId, orderPackageList);

		// 3.添加包裹上的额外信息
		Map<Long, List<OrderCartItemDTO>> cartMap = CollectionUtil.convertCollToListMap(cartList, "packageId");
		List<OrderPackageDTO> orderPackageDTOList = new ArrayList<OrderPackageDTO>();
		for (Long packageId : cartMap.keySet()) {
			OrderPackage op = CollectionUtil.getValueOfMap(orderPackageMap, packageId);
			if (op == null) {
				op = new OrderPackage();
				op.setExpressCompany(orderFormDTO.getExpressCompany());
				op.setOrderId(orderId);
				op.setOrderPackageState(OrderPackageState.INIT);
				op.setPackageId(packageId);
				op.setUserId(userId);
			}
			OrderPackageDTO opDTO = new OrderPackageDTO(op);
			opDTO.setOrderCartItemDTOList(cartMap.get(opDTO.getPackageId()));
			opDTO.setOrderExpInfoDTO(orderFormDTO.getOrderExpInfoDTO());
			opDTO.setOrderPackageRefundDTO(OrderPackageRefundDTO.genInstance(CollectionUtil.getValueOfMap(opRefundMap,
					packageId)));
			orderPackageDTOList.add(opDTO);
		}
		// 包裹排序
		Map<Long, OrderPackageDTO> orderPackageDTOMap = CollectionUtil.convertCollToMap(orderPackageDTOList,
				"packageId");
		orderPackageDTOList = CollectionUtil.addAllOfList(null, orderPackageDTOMap.values());
		if (orderPackageDTOList.size() > 1
				&& CollectionUtil.getFirstObjectOfCollection(orderPackageDTOList).getPackageId() == 0) {
			CollectionUtil.addOfList(orderPackageDTOList,
					CollectionUtil.getFirstObjectOfCollection(orderPackageDTOList));
			orderPackageDTOList.remove(0);
		}
		return orderPackageDTOList;
	}

	/**
	 * 读取全部包裹的退款记录
	 * 
	 * @param userId
	 * @param orderId
	 * @param orderPackageList
	 * @return
	 */
	private Map<Long, OrderPackageRefund> genOrderPackageRefundMap(long userId, long orderId,
			List<OrderPackage> orderPackageList) {
		Map<Long, OrderPackageRefund> opRefundMap = null;
		// 1.判断是否需要读取包裹退款记录(通过包裹状态判断)
		boolean needQueryOrderPackageRefund = false;
		if (CollectionUtil.isNotEmptyOfCollection(orderPackageList)) {
			for (OrderPackage op : orderPackageList) {
				OrderPackageState opState = op.getOrderPackageState();
				if (OrderPackageState.isCancel(opState) && !OrderPackageState.isCancelByOrder(opState)) {
					needQueryOrderPackageRefund = true;
					break;
				}
			}
		}
		// 2.读取包裹退款记录
		if (needQueryOrderPackageRefund) {
			List<OrderPackageRefund> opRefundList = orderPackageRefundDao.getListByOrderIdAndUserId(orderId, userId);
			opRefundMap = CollectionUtil.convertCollToMap(opRefundList, "packageId");
		}
		return opRefundMap;
	}

	/**
	 * 添加订单上的附属信息
	 * 
	 * @param ord
	 * @return
	 */
	private boolean appendExtInfo(OrderFormDTO ord) {
		return appendExtInfo(ord, null);
	}

	private boolean appendExtInfoList(List<OrderFormDTO> ordList) {
		return appendExtInfoList(ordList, null);
	}

	/**
	 * 添加订单上的附属信息
	 * 
	 * @param ord
	 * @param param
	 * @return
	 */
	private boolean appendExtInfo(OrderFormDTO ord, AppendExtInfoParam param) {
		// TODO.DML 优化
		if (ord == null)
			return false;
		/**
		 * [OrderFormDTO中的Ext字段] <br>
		 * 1. 订单快递地址信息：OrderExpInfoDTO orderExpInfoDTO; <br>
		 * 2. 订单下所有的购物车对象：List<OrderCartItemDTO> orderCartItemDTOList; <br>
		 * 3. 订单上的赠品对象列表：List<OrderSkuDTO> orderSkuDTOListOfOrdGift; <br>
		 * 4. 订单下的所有包裹信息：List<OrderPackageDTO> orderPackageDTOList;<br>
		 * 5. 是否可以取消、删除、退货（'can...'集合）<br>
		 */
		boolean isFilterEnum = false;
		long orderId = ord.getOrderId(), userId = ord.getUserId();
		try {
			// 1.读取订单的快递地址信息
			OrderExpInfoDTO orderExpInfoDTO = getOrderExpInfoByOrderId(orderId, userId, param);
			ord.setOrderExpInfoDTO(orderExpInfoDTO);

			// 2.读取全部的OrderSku
			List<OrderSku> orderSkuList = param != null ? param.orderSkuList : orderSkuDao.getListByOrderId(orderId,
					userId, false);
			List<OrderSkuDTO> orderSkuDTOList = ReflectUtil.convertList(OrderSkuDTO.class, orderSkuList, isFilterEnum);
			int totalSkuCount = 0;
			for (OrderSku ordSku : orderSkuList) {
				totalSkuCount += ordSku.getTotalCount();
			}
			ord.setSkuCount(totalSkuCount);

			// 3.读取全部的OrderCartItem
			List<OrderCartItemDTO> orderCartItemDTOList = getOrderCartItemDTOListByOrderId(userId, orderId,
					orderSkuDTOList, param);
			ord.setOrderCartItemDTOList(orderCartItemDTOList);

			// 4.订单下的所有包裹信息,并设置到包裹上
			// List<OrderPackageDTO> orderPackageDTOList =
			// getOrderPackageDTOListByOrderId(userId, orderId,
			// orderCartItemDTOList, ord, param);
			// ord.setOrderPackageDTOList(orderPackageDTOList);
			// 4.订单下的物流信息
			ord.setOrderLogisticsDTOs(getOrderLogisticsDTOListByOrderId(orderId));

			// 5.设置发票信息
			InvoiceInOrdDTO invoiceInOrdDTO = param != null ? param.invoiceInOrdDTO : invoiceService
					.getInvoiceInOrdByOrderId(orderId, userId);
			ord.setInvoiceInOrdDTO(invoiceInOrdDTO);// 买家申请的发票
			ord.setInvoiceDTOs(invoiceService.getInvoiceByOrderId(orderId));// 商家或者运营新增发票
			// 设置订单取消值
			OrderCancelInfoDTO cancelInfo = getOrderCancelInfo(userId, orderId);
			if (cancelInfo != null) {
				ord.setCancelTime(cancelInfo.getCtime());
				ord.setCancelReason(cancelInfo.getReason());
			}

			// 6.尝试设置是否可以修改为货到付款的标记
			// RetArg retArgOfCanChangeCOD = canChangePaymethodToCOD(ord,
			// orderExpInfoDTO);
			// Boolean canCOD = RetArgUtil.get(retArgOfCanChangeCOD,
			// Boolean.class);
			// if (canCOD == Boolean.TRUE)
			// ord.setCanCOD(canCOD);

			// 7.设置是否可以取消的标记位
			// boolean canCancel = OrderUtil.canCancel(ord);
			// ord.setCanCancel(canCancel);
		} catch (Exception ex) {
			logger.error("orderId=" + orderId, ex);
			return false;
		}
		return true;
	}

	private boolean appendExtInfoList(List<OrderFormDTO> ordList, AppendExtInfoParam param) {
		// TODO.DML 优化
		if (ordList == null)
			return false;
		boolean result = true;
		for (OrderFormDTO ord : ordList) {
			result = appendExtInfo(ord, param);
			if (!result) {
				return false;
			}
		}
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderForm(long, long,
	 *      java.lang.Boolean)
	 */
	@Override
	public OrderFormDTO queryOrderForm(long userId, long orderId, Boolean isVisible) {
		OrderForm ord = orderFormDao.getObjectByIdAndUserId(orderId, userId);
		if (ord == null || (isVisible != null && ord.isVisible() != isVisible)) {
			return null;
		}
		OrderFormDTO ordDTO = new OrderFormDTO(ord);
		boolean isSucc = appendExtInfo(ordDTO);
		return isSucc ? ordDTO : null;
	}

	@Override
	public List<OrderFormDTO> queryOrderFormList(long userId, long parentId, Boolean isVisible) {
		List<OrderForm> ordList = orderFormDao.queryOrderFormListByUserIdAndParentId(userId, parentId);
		if (ordList == null) {
			return null;
		}
		List<OrderFormDTO> result = new ArrayList<OrderFormDTO>();
		if (isVisible != null) {
			for (OrderForm ord : ordList) {
				if (ord.isVisible() != isVisible) {
					return null;
				}
				OrderFormDTO orderDTO = new OrderFormDTO(ord);
				result.add(orderDTO);
			}
		} else {
			for (OrderForm ord : ordList) {
				OrderFormDTO orderDTO = new OrderFormDTO(ord);
				result.add(orderDTO);
			}
		}
		boolean isSucc = appendExtInfoList(result);
		return isSucc ? result : null;
	}

	public List<OrderFormDTO> getOrderFormByParentId(long parentId, boolean isVisible) {
		List<OrderForm> ordList = orderFormDao.queryOrderFormListByParentId(parentId, isVisible);
		List<OrderFormDTO> result = new ArrayList<OrderFormDTO>(0);
		if (ordList == null) {
			return result;
		}
		for (OrderForm ord : ordList) {
			OrderFormDTO orderDTO = new OrderFormDTO(ord);
			result.add(orderDTO);
		}
		boolean isSucc = appendExtInfoList(result);
		if (!isSucc) {
			result.clear();
			logger.warn("appendExtInfoList fail.");
		}
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderFormListByState(long,
	 *      java.lang.Boolean, com.xyl.mmall.order.enums.OrderFormState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OrderFormDTO> queryOrderFormListByState(long userId, Boolean isVisible, OrderFormState[] stateArray,
			DDBParam param) {
		long[] orderTimeRange = null;
		return queryOrderFormListByState(userId, isVisible, stateArray, orderTimeRange, param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderFormListCountByState2(long,
	 *      java.lang.Boolean, com.xyl.mmall.order.enums.OrderFormState[],
	 *      long[])
	 */
	public int queryOrderFormListCountByState2(long userId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange, Boolean isOnPay) {
		return queryOrderFormListCountByState(userId, isVisible, stateArray, orderTimeRange, isOnPay);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryAllOrderFormListCount(long,
	 *      java.lang.Boolean, long[])
	 */
	public int queryAllOrderFormListCount(long userId, Boolean isVisible, long[] orderTimeRange) {
		return queryOrderFormListCountByState2(userId, isVisible, null, orderTimeRange, null);
	}

	/**
	 * @param userId
	 * @param isVisible
	 * @param stateArray
	 * @param orderTimeRange
	 * @return
	 */
	private int queryOrderFormListCountByState(long userId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange, Boolean isOnPay) {
		int count = orderFormDao.queryOrderFormListCount(userId, isVisible, stateArray, orderTimeRange, isOnPay);
		return count;
	}

	/**
	 * @param userId
	 * @param isVisible
	 * @param stateArray
	 * @param orderTimeRange
	 * @param param
	 * @return
	 */
	private List<OrderFormDTO> queryOrderFormListByState(long userId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param) {
		List<OrderForm> ordList = orderFormDao.queryOrderFormList(userId, isVisible, stateArray, orderTimeRange, param);
		return convertToOrderFormDTOList(ordList);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderFormListByLikeOrderId(long,
	 *      java.lang.Boolean, java.lang.Long, long[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg queryOrderFormListByLikeOrderId(long userId, Boolean isVisible, Long orderIdOfPart,
			long[] orderTimeRange, DDBParam param) {
		List<OrderForm> ordList = orderFormDao.queryOrderFormList(userId, isVisible, orderIdOfPart, orderTimeRange,
				param);
		List<OrderFormDTO> ordDTOList = convertToOrderFormDTOList(ordList);
		// 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, ordDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 将List(OrderForm)转换成List(OrderFormDTO)
	 * 
	 * @param ordList
	 * @return
	 */
	private List<OrderFormDTO> convertToOrderFormDTOList(List<OrderForm> ordList) {
		// 参数有效性判断
		if (CollectionUtil.isEmptyOfCollection(ordList)) {
			return null;
		}
		Map<Long, AppendExtInfoParam> paramMap = genAppendExtInfoParam(ordList);

		List<OrderFormDTO> ordDTOList = new ArrayList<OrderFormDTO>(ordList.size());
		Iterator<OrderForm> iter = ordList.iterator();
		while (iter.hasNext()) {
			// 将OrderForm转换成OrderFormDTO
			OrderFormDTO ordDTO = OrderFormDTO.genInstance(iter.next());
			if (ordDTO == null)
				continue;
			long orderId = ordDTO.getOrderId();
			AppendExtInfoParam param = CollectionUtil.getValueOfMap(paramMap, orderId);
			// 添加订单上的附属信息
			boolean isSucc = appendExtInfo(ordDTO, param);
			if (isSucc) {
				ordDTOList.add(ordDTO);
			}
		}
		return ordDTOList;
	}

	/**
	 * 生成AppendExtInfoParam
	 * 
	 * @param ordList
	 * @return
	 */
	private Map<Long, AppendExtInfoParam> genAppendExtInfoParam(List<OrderForm> ordList) {
		if (CollectionUtil.isEmptyOfCollection(ordList))
			return null;

		// 1.判断是否都是一个用户的订单(优化暂时只针对同一个用户名下的订单)
		long userId = CollectionUtil.getFirstObjectOfCollection(ordList).getUserId();
		boolean isSameUserId = true;
		for (OrderForm ord : ordList) {
			long userIdOfTmp = ord.getUserId();
			if (userIdOfTmp != userId) {
				isSameUserId = false;
				break;
			}
		}
		if (!isSameUserId)
			return null;

		// 2.从DB中批量数据
		Map<Long, OrderForm> ordMap = CollectionUtil.convertCollToMap(ordList, "orderId");
		Collection<Long> orderIdColl = ordMap.keySet();
		// 读取OrderExpInfo
		List<OrderExpInfo> orderExpInfoList = orderExpInfoDao.getListByOrderIdsAndUserId(userId, orderIdColl);
		Map<Long, OrderExpInfo> orderExpInfoMap = CollectionUtil.convertCollToMap(orderExpInfoList, "orderId");
		// 读取OrderPackage
		List<OrderPackage> orderPackageList = orderPackageDao.getListByOrderIdsAndUserId(userId, orderIdColl);
		Map<Long, List<OrderPackage>> orderPackageMap = CollectionUtil
				.convertCollToListMap(orderPackageList, "orderId");
		// 读取OrderCartItem
		List<OrderCartItem> orderCartItemList = orderCartItemDao.getListByOrderIdsAndUserId(userId, orderIdColl);
		Map<Long, List<OrderCartItem>> orderCartItemMap = CollectionUtil.convertCollToListMap(orderCartItemList,
				"orderId");
		// 读取OrderSku
		List<OrderSku> orderSkuList = orderSkuDao.getListByOrderIdsAndUserId(userId, orderIdColl);
		Map<Long, List<OrderSku>> orderSkuMap = CollectionUtil.convertCollToListMap(orderSkuList, "orderId");
		// 读取InvoiceInOrdDTO
		List<InvoiceInOrdDTO> invoiceInOrdDTOList = invoiceService.getInvoiceInOrdByOrderIdColl(orderIdColl, userId);
		Map<Long, InvoiceInOrdDTO> invoiceInOrdDTOMap = CollectionUtil.convertCollToMap(invoiceInOrdDTOList, "orderId");

		// 3.生成paramMap
		Map<Long, AppendExtInfoParam> paramMap = new TreeMap<Long, OrderServiceImpl.AppendExtInfoParam>();
		for (Long orderId : orderIdColl) {
			AppendExtInfoParam param = new AppendExtInfoParam();
			param.invoiceInOrdDTO = CollectionUtil.getValueOfMap(invoiceInOrdDTOMap, orderId);
			param.orderExpInfo = CollectionUtil.getValueOfMap(orderExpInfoMap, orderId);
			param.orderPackageList = CollectionUtil.getValueOfMap(orderPackageMap, orderId);
			param.orderCartItemList = CollectionUtil.getValueOfMap(orderCartItemMap, orderId);
			param.orderSkuList = CollectionUtil.getValueOfMap(orderSkuMap, orderId);
			paramMap.put(orderId, param);
		}
		return paramMap;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderFormListByState2(long,
	 *      java.lang.Boolean, com.xyl.mmall.order.enums.OrderFormState[],
	 *      long[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg queryOrderFormListByState2(long userId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param) {
		List<OrderFormDTO> ordDTOList = queryOrderFormListByState(userId, isVisible, stateArray, orderTimeRange, param);

		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, ordDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderFormListByMailNO(java.lang.String,
	 *      java.lang.Boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OrderFormDTO> queryOrderFormListByMailNO(String mailNO, Boolean isVisible, DDBParam param) {
		List<OrderPackage> pkgList = orderPackageDao.getListByMailNO(mailNO);
		if (null == pkgList) {
			return null;
		}
		Set<Long> orderIdSet = new HashSet<Long>();
		for (OrderPackage pkg : pkgList) {
			long orderId = pkg.getOrderId();
			if (!orderIdSet.contains(orderId)) {
				orderIdSet.add(orderId);
			}
		}
		List<OrderFormDTO> ret = new ArrayList<OrderFormDTO>(orderIdSet.size());
		for (Long orderId : orderIdSet) {
			OrderFormDTO ordFormDTO = queryOrderFormByOrderId(orderId);
			if (null != ordFormDTO) {
				ret.add(ordFormDTO);
			}
		}
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryAllOrderFormList(long,
	 *      java.lang.Boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryAllOrderFormList(long userId, Boolean isVisible, long[] orderTimeRange, DDBParam param) {
		return queryOrderFormListByState2(userId, isVisible, null, orderTimeRange, param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderFormByOrderId(long)
	 */
	@Override
	public OrderFormDTO queryOrderFormByOrderId(long orderId) {
		OrderForm orderForm = orderFormDao.getObjectById(orderId);
		if (null == orderForm) {
			return null;
		}
		OrderFormDTO ordFormDTO = new OrderFormDTO(orderForm);
		boolean isSucc = appendExtInfo(ordFormDTO);
		return isSucc ? ordFormDTO : null;
	}

	public List<OrderFormDTO> queryOrderFormListByParentId(long parentId) {
		List<OrderForm> list = orderFormDao.queryOrderFormListByParentId(parentId);
		if (null == list) {
			return null;
		}

		List<OrderFormDTO> result = new ArrayList<OrderFormDTO>();
		for (OrderForm orderForm : list) {
			OrderFormDTO orderFormDTO = new OrderFormDTO(orderForm);
			result.add(orderFormDTO);
		}

		boolean isSucc = appendExtInfoList(result);
		return isSucc ? result : null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#changePaymethodToCOD(long,
	 *      long)
	 */
	@Transaction
	public int changePaymethodToCOD(long userId, long orderId) {
		long currTime = System.currentTimeMillis();
		// 1.判断普通服务(非TCC服务),是否可以继续执行
		RetArg retArgOfIsContinue = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfIsContinue, Boolean.class);
		if (isContinue != Boolean.TRUE)
			return -1;
		// 读取订单数据,并做简单的判断
		OrderForm orderForm = RetArgUtil.get(retArgOfIsContinue, OrderForm.class);
		if (orderForm.getOrderFormPayMethod() == OrderFormPayMethod.COD)
			return 2;
		OrderExpInfo orderExpInfo = orderExpInfoDao.getObjectByIdAndUserId(orderId, userId);
		if (orderExpInfo == null) {
			logger.info("orderExpInfo == null ,orderId=" + orderId);
			return -2;
		}
		int provinceId = orderForm.getProvinceId();

		// 2.判断订单是否允许货到付款
		RetArg retArgOfChangePM = canChangePaymethodToCOD(orderForm, orderExpInfo);
		if (RetArgUtil.get(retArgOfChangePM, Boolean.class) != Boolean.TRUE) {
			OrderFormState orderState = orderForm.getOrderFormState();
			OrderFormState[] orderStateArrayOf3 = new OrderFormState[] { OrderFormState.CANCEL_ED,
					OrderFormState.CANCEL_ING };
			OrderFormState[] orderStateArrayOfUn5 = new OrderFormState[] { OrderFormState.WAITING_PAY,
					OrderFormState.WAITING_COD_AUDIT, OrderFormState.COD_AUDIT_REFUSE, OrderFormState.CANCEL_ED,
					OrderFormState.CANCEL_ING };
			if (CollectionUtil.isInArray(orderStateArrayOf3, orderState))
				return -3;
			else if (!CollectionUtil.isInArray(orderStateArrayOfUn5, orderState))
				return -5;
			else
				return -4;
		}

		boolean isSucc = true;
		// 3.尝试修改订单的支付方式
		TradeItem tradeItemOfEPay = RetArgUtil.get(retArgOfChangePM, TradeItem.class);
		// 计算交易上,取整后的现金金额
		BigDecimal cashOfTrade = tradeItemOfEPay.getCash().setScale(0, RoundingMode.DOWN);
		// 3.1 修改 订单状态+支付方式+支付状态
		if (isSucc) {
			OrderForm orderFormOfOri = orderForm, orderFormOfNew = ReflectUtil.cloneObj(orderForm);
			orderFormOfNew.setOrderFormState(OrderFormState.WAITING_COD_AUDIT);
			orderFormOfNew.setOrderFormPayMethod(OrderFormPayMethod.COD);
			orderFormOfNew.setPayState(PayState.COD_NOT_PAY);
			isSucc = isSucc && orderFormDao.updateOrdByType1(orderFormOfOri, orderFormOfNew);
		}
		// 3.2 创建新的货到付款交易
		if (isSucc) {
			TradeItem tradeItemOfCOD = ReflectUtil.cloneObj(tradeItemOfEPay);
			tradeItemOfCOD.setTradeItemPayMethod(TradeItemPayMethod.COD);
			tradeItemOfCOD.setTradeId(0);
			tradeItemOfCOD.setPayState(PayState.COD_NOT_PAY);
			tradeItemOfCOD.setCash(cashOfTrade);
			isSucc = isSucc && tradeItemDao.addObject(tradeItemOfCOD) != null;
		}
		// 3.3 设置原始的交易状态为关闭
		if (isSucc) {
			PayState[] oldPayStateArray = new PayState[] { tradeItemOfEPay.getPayState() };
			tradeItemOfEPay.setPayState(PayState.ONLINE_CHANGE);
			isSucc = isSucc && tradeItemDao.updatePayState(tradeItemOfEPay, oldPayStateArray);
		}
		// 3.4 创建货到付款审核记录
		if (isSucc) {
			CODAuditLog codAuditLog = new CODAuditLog();
			codAuditLog.setCtime(currTime);
			codAuditLog.setOrderId(orderId);
			codAuditLog.setUserId(userId);
			codAuditLog.setProvinceId(provinceId);
			codAuditLog.setAuditState(CODAuditState.WAITING);
			codAuditLog.setExtInfo("null");
			isSucc = isSucc && codAuditLogDao.addObject(codAuditLog) != null;
		}

		if (!isSucc) {
			throw new ServiceNoThrowException("orderId=" + orderId);
		}
		return isSucc ? 1 : 0;
	}

	/**
	 * 判断订单的支付方式是否允许修改为货到付款<br>
	 * (判断依据: 订单状态,订单支付方式,交易状态,可选支付方式)
	 * 
	 * @param orderForm
	 * @param orderExpInfo
	 * @return
	 */
	private RetArg canChangePaymethodToCOD(OrderForm orderForm, OrderExpInfo orderExpInfo) {
		RetArg retArg = new RetArg();
		long userId = orderForm.getUserId(), orderId = orderForm.getOrderId();

		// 0.判断 是否允许选择货到付款(根据产品的配送方式+快递+地址判断)
		if (!orderForm.isCanCODBySku()) {
			logger.debug("!orderForm.isCanCODByAddress() ,orderId=" + orderId);
			RetArgUtil.put(retArg, false);
			return retArg;
		}

		// 1.判断订单状态是否属于等待支付
		boolean isValidOfOrderState = orderForm.getOrderFormState() == OrderFormState.WAITING_PAY;
		if (!isValidOfOrderState) {
			logger.debug("!isValidOfOrderState ,orderId=" + orderId);
			RetArgUtil.put(retArg, false);
			return retArg;
		}

		// 2.判断订单原始的支付方式是否允许修改
		boolean isValidOfPayMethod = OrderFormPayMethod.isOnlinePayMethod(orderForm.getOrderFormPayMethod());
		if (!isValidOfPayMethod) {
			logger.debug("!isValidOfPayMethod ,orderId=" + orderId);
			RetArgUtil.put(retArg, false);
			return retArg;
		}

		// 3.判断交易的状态是否处于未支付
		List<TradeItem> tradeItemList = tradeItemDao.getListByOrderId(orderId, userId);
		TradeItem tradeOfOnline = TradeApiUtil.getTradeOfOnlineAndUnpay(tradeItemList);
		boolean isValidOfTradeItem = tradeOfOnline != null;
		if (!isValidOfTradeItem) {
			logger.debug("!isValidOfTradeItem ,orderId=" + orderId);
			RetArgUtil.put(retArg, false);
			return retArg;
		}

		// 4.判断可选的支付方式里,是否有货到付款
		BigDecimal totalRPrice = OrderApiUtil.calPriceForCanCOD(orderForm);
		OrderFormPayMethodDTO[] payMethodDTOArray = orderCalService.genOrderFormPayMethodDTOArray(userId,
				new OrderExpInfoDTO(orderExpInfo), totalRPrice);
		boolean isValidOfPayMethodArray = false;
		for (OrderFormPayMethodDTO payMethodDTO : payMethodDTOArray)
			isValidOfPayMethodArray = isValidOfPayMethodArray
					|| (payMethodDTO.getPayMethod() == OrderFormPayMethod.COD && payMethodDTO.isValid());
		if (!isValidOfPayMethodArray) {
			logger.debug("!isValidOfPayMethodArray ,orderId=" + orderId);
			RetArgUtil.put(retArg, false);
			return retArg;
		}

		// 返回
		RetArgUtil.put(retArg, true);
		RetArgUtil.put(retArg, tradeItemList);
		RetArgUtil.put(retArg, tradeOfOnline);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#setStateToEPayed(com.xyl.mmall.order.param.OrderServiceSetStateToEPayedParam)
	 */
	public int setStateToEPayed(OrderServiceSetStateToEPayedParam param) {
		RetArg retArg = new RetArg();
		orderInternalServiceImpl.setStateToEPayed(param, retArg);
		return RetArgUtil.get(retArg, Integer.class);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#updateIsVisible(long, long,
	 *      boolean)
	 */
	@Override
	@Transaction
	public boolean updateIsVisible(long userId, long orderId, boolean isVisible) {
		OrderForm ord = new OrderForm();
		ord.setUserId(userId);
		ord.setOrderId(orderId);
		// 获得记录锁
		ord = orderFormDao.getLockByKey(ord);
		if (ord == null)
			return false;
		if (ord.isVisible() == isVisible)
			return true;

		ord.setVisible(isVisible);
		OrderFormState[] oldOrderFormState = null;
		return orderFormDao.updateIsVisible(ord, oldOrderFormState);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#updateOrderExpInfo(long,
	 *      com.xyl.mmall.order.param.OrderExpInfoChangeParam)
	 */
	@Override
	@Transaction
	@OperateLog(clientType = "web")
	public int updateOrderExpInfo(long orderId, long userId, OrderExpInfoChangeParam param) {
		boolean result = true;
		if (null == param) {
			return -1;
		}

		// 1.读取订单信息,判断订单状态
		OrderForm order = new OrderForm();
		order.setUserId(userId);
		order.setOrderId(orderId);
		order = orderFormDao.getObjectByPrimaryKeyAndPolicyKey(order);
		OrderFormState[] validStateArray = new OrderFormState[] { OrderFormState.WAITING_PAY,
				OrderFormState.WAITING_DELIVE };
		if (order == null || !CollectionUtil.isInArray(validStateArray, order.getOrderFormState())) {
			logger.error("order==null || orderFormState invalid ,orderId=" + orderId);
			return -2;
		}
		// 2.读取快递信息
		OrderExpInfo ordExpInfo = orderExpInfoDao.getObjectByIdAndUserId(orderId, userId);
		if (null == ordExpInfo) {
			return -3;
		}
		// 3.更新快递信息(获得快递记录锁)
		ordExpInfo = orderExpInfoDao.getLockByKey(ordExpInfo);
		ordExpInfo.setAddress(param.getAddress());
		ordExpInfo.setConsigneeName(param.getConsigneeName());
		ordExpInfo.setConsigneeMobile(param.getConsigneeMobile());
		ordExpInfo.setConsigneeTel(param.getConsigneeTel());
		ordExpInfo.setAreaCode(param.getAreaCode());
		ordExpInfo.setZipcode(param.getZipcode());
		result = orderExpInfoDao.updateObjectByKey(ordExpInfo);
		return 1;
	}

	private String fullAddressInfo(OrderExpInfo ordExpInfo) {
		StringBuffer strBuf = new StringBuffer(1024);
		strBuf.append(ordExpInfo.getConsigneeName() + "," + ordExpInfo.getConsigneeMobile());
		if (StringUtils.isNotEmpty(ordExpInfo.getAreaCode()) || StringUtils.isNotEmpty(ordExpInfo.getConsigneeTel())) {
			strBuf.append("," + ordExpInfo.getAreaCode() + "-" + ordExpInfo.getConsigneeTel());
		}
		strBuf.append("<br/>").append(ordExpInfo.getProvince()).append(ordExpInfo.getCity())
				.append(ordExpInfo.getSection()).append(ordExpInfo.getAddress());
		if (StringUtils.isNotEmpty(ordExpInfo.getZipcode())) {
			strBuf.append("," + ordExpInfo.getZipcode());
		}
		return strBuf.toString();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#getOrderCancelInfo(long,
	 *      long)
	 */
	public OrderCancelInfoDTO getOrderCancelInfo(long userId, long orderId) {
		OrderCancelInfo info = orderCancelInfoDao.getObjectByIdAndUserId(orderId, userId);
		return null == info ? null : new OrderCancelInfoDTO(info);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#getOrderCancelInfoDTOList(long,
	 *      com.xyl.mmall.order.enums.OrderCancelState,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<OrderCancelInfoDTO> getOrderCancelInfoDTOList(long minOrderId, OrderCancelState cancelState,
			DDBParam param) {
		// 初始化Param
		param = OrderUtil.initDDBParamWithMinOrderId(param);
		List<OrderCancelInfo> cancelList = orderCancelInfoDao.getListByStateWithMinOrderId(minOrderId, cancelState,
				param);
		List<OrderCancelInfoDTO> cancelDTOList = new ArrayList<>();
		if (CollectionUtil.isNotEmptyOfCollection(cancelList)) {
			for (OrderCancelInfo cancel : cancelList)
				cancelDTOList.add(new OrderCancelInfoDTO(cancel));
		}
		return cancelDTOList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#getOrderCancelInfoDTOListByRType(long,
	 *      com.xyl.mmall.order.enums.OrderCancelRType,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg getOrderCancelInfoDTOListByRType(long minOrderId, OrderCancelRType cancelRType, DDBParam param) {
		// 初始化Param
		param = OrderUtil.initDDBParamWithMinOrderId(param);
		List<OrderCancelInfo> cancelList = orderCancelInfoDao.getListByRTypeWithMinOrderId(minOrderId, cancelRType,
				param);
		List<OrderCancelInfoDTO> cancelDTOList = new ArrayList<>();
		if (CollectionUtil.isNotEmptyOfCollection(cancelList)) {
			for (OrderCancelInfo cancel : cancelList)
				cancelDTOList.add(new OrderCancelInfoDTO(cancel));
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, cancelDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#setOrderCancelToDone()
	 */
	public boolean setOrderCancelToDone() {
		return orderCancelInfoDao.setOrderCancelToDone();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#setOrderCancelRType(com.xyl.mmall.order.dto.OrderCancelInfoDTO,
	 *      com.xyl.mmall.order.enums.OrderCancelRType[],
	 *      com.xyl.mmall.order.enums.OrderCancelRType)
	 */
	@Override
	public boolean setOrderCancelRType(OrderCancelInfoDTO cancelDTO, OrderCancelRType[] oldRTypes,
			OrderCancelRType newRType) {
		return orderCancelInfoDao.setCancelRType(cancelDTO, oldRTypes, newRType);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#cancelTradeByOrderCancelInfo(com.xyl.mmall.order.dto.OrderCancelInfoDTO)
	 */
	public boolean cancelTradeByOrderCancelInfo(OrderCancelInfoDTO cancelDTO) {
		// 0.判断是否需要取消交易
		long orderId = cancelDTO.getOrderId(), userId = cancelDTO.getUserId();
		boolean needCancel = OrderApiUtil.needCancelTrade(cancelDTO);
		if (!needCancel)
			return true;

		// 0.判断普通服务(非TCC服务),是否可以继续执行
		RetArg retArg = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
		Boolean isContinue = RetArgUtil.get(retArg, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			return false;
		}

		boolean isSucc = true;
		try {
			// 1.取消交易
			if (isSucc)
				isSucc = isSucc && tradeInternalService.cancelTrade(orderId, userId, cancelDTO.getRtype());

			// 2.生成新的retryFlag
			Long retryFlagOfOld = cancelDTO.getRetryFlag();
			if (isSucc) {
				long retryFlagOfNew = ExtInfoFieldUtil.setValueOfType1(retryFlagOfOld,
						OrderCancelInfo.IDX_CANCEL_TRADE, false);
				cancelDTO.setRetryFlag(retryFlagOfNew);
			}
			// 3.更新OrderCancelInfo的Retry信息
			boolean isUpdateRetryFlag = isSucc;
			if (updateOrderCancelInfoOfRetryInfo(isUpdateRetryFlag, cancelDTO, retryFlagOfOld) != 1)
				logger.info("updateOrderCancelInfoOfRetryInfo fail");
		} catch (Exception ex) {
			isSucc = false;
		}
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#updateOrderCancelInfoOfRetryInfo(boolean,
	 *      com.xyl.mmall.order.dto.OrderCancelInfoDTO, java.lang.Long)
	 */
	public int updateOrderCancelInfoOfRetryInfo(boolean isUpdateRetryFlag, OrderCancelInfoDTO cancelDTO,
			Long retryFlagOfOld) {
		boolean isSucc = true;
		if (isUpdateRetryFlag) {
			if (retryFlagOfOld == null)
				return -1;
			// CASE1: 取消交易成功(更新标记位)
			isSucc = isSucc && orderCancelInfoDao.updateRetryFlag(cancelDTO, retryFlagOfOld);
		} else {
			// CASE2: 取消交易失败(更新重试次数)
			isSucc = isSucc && orderCancelInfoDao.incrRetryCount(cancelDTO);
		}
		return isSucc ? 1 : 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderFormListByStateWithMinOrderId(long,
	 *      com.xyl.mmall.order.enums.OrderFormState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg queryOrderFormListByStateWithMinOrderId(long minOrderId, OrderFormState[] stateArray, DDBParam param) {
		// 初始化Param
		param = OrderUtil.initDDBParamWithMinOrderId(param);
		List<OrderForm> orderList = orderFormDao.queryOrderFormListByStateWithMinOrderId(minOrderId, stateArray, param);
		List<OrderFormDTO> orderDTOList = convertToOrderFormDTOList(orderList);
		// 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, orderDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderFormListByType1WithMinOrderId(long,
	 *      long[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg queryOrderFormListByType1WithMinOrderId(long minOrderId, long[] orderTimeRange, DDBParam param) {
		OrderFormState[] orderFormStateArray = OrderFormState.getMaybePayedArray();
		PayState[] payStateArray = new PayState[] { PayState.ONLINE_PAYED, PayState.COD_PAYED, PayState.COD_NOT_PAY,
				PayState.ZERO_PAYED };
		// param初始化
		param = OrderUtil.initDDBParamWithMinOrderId(param);
		List<OrderForm> orderList = orderFormDao.queryOrderFormDTOListWithMinOrderId(minOrderId, orderFormStateArray,
				orderTimeRange, payStateArray, param);
		List<OrderFormDTO> orderDTOList = convertToOrderFormDTOList(orderList);
		// 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, orderDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#setOrderFormStateToWaitingDelive(long,
	 *      long)
	 */
	@Transaction
	public int setOrderFormStateToWaitingDelive(long userId, long orderId) {
		// 设置订单状态为 WAITING_DELIVE(更新前状态为 WAITING_SEND_ORDER)
		OrderFormState newState = OrderFormState.WAITING_DELIVE;
		OrderFormState[] oldStateArray = new OrderFormState[] { OrderFormState.WAITING_SEND_ORDER };
		return setOrderFormState(userId, orderId, newState, oldStateArray);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#setOrderFormStateToFinishDelive(long,
	 *      long)
	 */
	@Transaction
	public int setOrderFormStateToFinishDelive(long userId, long orderId) {
		// 设置订单状态为 FINISH_DELIVE(更新前状态为 PART_DELIVE|ALL_DELIVE)
		OrderFormState newState = OrderFormState.FINISH_DELIVE;
		OrderFormState[] oldStateArray = new OrderFormState[] { OrderFormState.PART_DELIVE, OrderFormState.ALL_DELIVE };
		return setOrderFormState(userId, orderId, newState, oldStateArray);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#setOrderFormStateToCancel(long,
	 *      long)
	 */
	@Transaction
	public int setOrderFormStateToCancel(long userId, long orderId) {
		// 1.设置订单状态为 CANCEL_ED(更新前状态为 WAITING_DELIVE|PART_DELIVE|ALL_DELIVE)
		OrderFormState newState = OrderFormState.CANCEL_ED_ALLOP;
		OrderFormState[] oldStateArray = new OrderFormState[] { OrderFormState.WAITING_DELIVE,
				OrderFormState.PART_DELIVE, OrderFormState.ALL_DELIVE };
		int retCode = setOrderFormState(userId, orderId, newState, oldStateArray);

		// 2.添加取消原因OrderCancelInfo
		OrderCancelInfo cancelInfo = new OrderCancelInfo();
		cancelInfo.setCancelSource(OrderCancelSource.ALL_PACKAGE_CANCEL);
		cancelInfo.setCancelState(OrderCancelState.DONE);
		cancelInfo.setCtime(System.currentTimeMillis());
		cancelInfo.setOrderId(orderId);
		cancelInfo.setReason(OrderCancelSource.ALL_PACKAGE_CANCEL.getDesc());
		cancelInfo.setRtype(OrderCancelRType.ALL_PACKAGE_CANCELLED);
		cancelInfo.setUserId(userId);
		if (retCode == 1) {
			boolean isSucc = orderCancelInfoDao.addObject(cancelInfo) != null;
			if (!isSucc) {
				logger.error("orderCancelInfoDao.addObject(cancelInfo) fail.");
				retCode = 0;
			}
		}

		return retCode;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#setOrderFormStateToCancelRevert(long,
	 *      long, com.xyl.mmall.order.enums.OrderFormState)
	 */
	@Transaction
	public int setOrderFormStateToCancelRevert(long userId, long orderId, OrderFormState newState) {
		// 0.判断输入参数是否合法
		OrderFormState[] validNewStateArray = OrderFormState.getRevertOmsCancelNewStateArray();
		if (!CollectionUtil.isInArray(validNewStateArray, newState))
			return 0;

		// 1.生成newExtInfo
		OrderForm order = new OrderForm();
		order.setUserId(userId);
		order.setOrderId(orderId);
		order = orderFormDao.getLockByKey(order);
		if (order == null) {
			logger.error("order==null ,OrderId=" + orderId + " ,UserId=" + userId);
			return 0;
		}
		String newExtInfo = null, oldExtInfo = order.getExtInfo();
		OrderFormExtInfoDTO extDTO = OrderFormExtInfoDTO.genOrderFormExtInfoDTOByExtInfo(oldExtInfo);
		extDTO.setCancelFail(true);
		newExtInfo = JsonUtils.toJson(extDTO);

		OrderFormState[] oldStateArray = new OrderFormState[] { OrderFormState.WAITING_CANCEL_OMSORDER };
		// 2.更新订单状态和extInfo字段
		int retCode = setOrderFormStateAndExtInfo(userId, orderId, newState, oldStateArray, newExtInfo, oldExtInfo);
		return retCode;
	}

	/**
	 * 更新订单状态
	 * 
	 * @param userId
	 * @param orderId
	 * @param newState
	 * @param oldStateArray
	 * @return -1:获取订单锁失败<br>
	 *         0:更新失败 <br>
	 *         1:更新成功 <br>
	 *         2:重复更新
	 */
	@Deprecated
	private int setOrderFormState(long userId, long orderId, OrderFormState newState, OrderFormState[] oldStateArray) {
		String newExtInfo = null, oldExtInfo = null;
		return setOrderFormStateAndExtInfo(userId, orderId, newState, oldStateArray, newExtInfo, oldExtInfo);
	}

	/**
	 * 更新订单状态
	 * 
	 * @param userId
	 * @param orderId
	 * @param newState
	 * @param oldStateArray
	 * @param newExtInfo
	 * @param oldExtInfo
	 * @return -1:获取订单锁失败<br>
	 *         0:更新失败 <br>
	 *         1:更新成功 <br>
	 *         2:重复更新
	 */
	@Deprecated
	private int setOrderFormStateAndExtInfo(long userId, long orderId, OrderFormState newState,
			OrderFormState[] oldStateArray, String newExtInfo, String oldExtInfo) {
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + orderId + " ,userId="
					+ userId);
			return -1;
		}
		// 1.更新订单状态
		OrderForm order = new OrderForm();
		order.setUserId(userId);
		order.setOrderId(orderId);
		order.setOrderFormState(newState);
		boolean isSucc = true;
		if (newState == OrderFormState.FINISH_TRADE)
			isSucc = isSucc && orderFormDao.updateOrdStateToFinishDelive(order);
		else if (StringUtils.isNotBlank(newExtInfo)) {
			order.setExtInfo(newExtInfo);
			isSucc = isSucc && orderFormDao.updateOrdStateAndExtInfo(order, oldStateArray, oldExtInfo);
		} else
			isSucc = isSucc && orderFormDao.updateOrdState(order, oldStateArray);
		if (isSucc) {
			return 1;
		}
		// 2.如果更新失败,则判断是否属于重复更新的情况(即状态已经流转到下一步)
		order = orderFormDao.getObjectByPrimaryKeyAndPolicyKey(order);
		isSucc = order != null && order.getOrderFormState() == newState;
		if (isSucc) {
			return 2;
		}

		return 0;
	}

	public boolean updateOrderFormCommnet(long userId, long orderId, long businessId, String comment) {
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + orderId + " ,userId="
					+ userId);
			return false;
		}
		// 1.更新订单备注
		OrderForm orderForm = RetArgUtil.get(retArgOfTmp, OrderForm.class);
		if (orderForm.getBusinessId() != businessId) {
			return false;
		}
		String newComment = DateUtil.dateToString(new Date(), DateUtil.LONG_PATTERN) + "  " + comment;
		if (StringUtils.isNotEmpty(orderForm.getComment())) {
			newComment = newComment + "|" + orderForm.getComment();
		}
		orderForm.setComment(newComment);
		return orderFormDao.updateComment(orderForm);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#canOrderExpInfoBeChanged(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public boolean canOrderExpInfoBeChanged(OrderFormDTO ordFormDTO) {
		OrderFormState ordFormState = null;
		if (null == ordFormDTO || null == (ordFormState = ordFormDTO.getOrderFormState())) {
			return false;
		}
		if (OrderFormState.WAITING_DELIVE == ordFormState || OrderFormState.PART_DELIVE == ordFormState
				|| OrderFormState.ALL_DELIVE == ordFormState || OrderFormState.FINISH_DELIVE == ordFormState
				|| OrderFormState.CANCEL_ING == ordFormState || OrderFormState.CANCEL_ED == ordFormState) {
			return false;
		}
		return true;
	}

	@Override
	@Transaction
	public int setOrderFormStateToDelive(long userId, long orderId) {
		// 设置订单状态为 ALL_DELIVE(更新前状态为 WAITING_DELIVE)
		OrderFormState newState = OrderFormState.ALL_DELIVE;
		OrderFormState[] oldStateArray = new OrderFormState[] { OrderFormState.WAITING_DELIVE };
		return setOrderFormState(userId, orderId, newState, oldStateArray);
	}

	// TODO
	@Override
	@Transaction
	public boolean cancelOrder(OrderCancelInfoDTO orderCancelInfoDTO) {
		OrderFormState newState = null;
		OrderFormState[] oldStateArray = null;
		switch (orderCancelInfoDTO.getOperateUserType()) {
		case USER:
			// 1.设置订单状态为 CANCEL_ING(更新前状态为 WAITING_DELIVE|ALL_DELIVE)
			newState = OrderFormState.CANCEL_ING;
			oldStateArray = new OrderFormState[] { OrderFormState.WAITING_DELIVE, OrderFormState.ALL_DELIVE };
			break;
		case BACKERNDER:
			// 2.设置订单状态为 CANCEL_ED(更新前状态为 WAITING_DELIVE)
			newState = OrderFormState.CANCEL_ED;
			oldStateArray = new OrderFormState[] { OrderFormState.WAITING_DELIVE };
			break;
		default:
			break;
		}

		int retCode = setOrderFormState(orderCancelInfoDTO.getUserId(), orderCancelInfoDTO.getOrderId(), newState,
				oldStateArray);
		// 2.添加取消原因OrderCancelInfo
		// OrderCancelInfo cancelInfo = new OrderCancelInfo();
		// cancelInfo.setCancelSource(OrderCancelSource.ALL_PACKAGE_CANCEL);
		// cancelInfo.setCancelState(OrderCancelState.DONE);
		// cancelInfo.setCtime(System.currentTimeMillis());
		// cancelInfo.setOrderId(orderId);
		// cancelInfo.setReason(OrderCancelSource.ALL_PACKAGE_CANCEL.getDesc());
		// cancelInfo.setRtype(OrderCancelRType.ALL_PACKAGE_CANCELLED);
		// cancelInfo.setUserId(userId);
		if (retCode == 1) {
			boolean isSucc = orderCancelInfoDao.addObject(orderCancelInfoDTO) != null;
			if (!isSucc) {
				logger.error("orderCancelInfoDao.addObject(cancelInfo) fail.");
				return false;
			}
			return true;
		}

		return false;
	}

	@Override
	@Transaction
	public int setOrderFormStateToFinish(long orderId, long userId) {
		OrderFormState newState = OrderFormState.FINISH_TRADE;
		OrderFormState[] oldStateArray = new OrderFormState[] { OrderFormState.ALL_DELIVE, OrderFormState.PART_DELIVE };
		return setOrderFormState(userId, orderId, newState, oldStateArray);
	}

	@Override
	@Transaction
	@OperateLog(clientType = "web")
	public int deliverGoods(OrderLogisticsDTO orderLogisticsDTO) {
		// orderLogisticsDao.addObject((OrderLogisticsDTO)orderLogisticsDTO);物流业务暂时不涉及啦
		// 设置订单状态为 ALL_DELIVE(更新前状态为 WAITING_DELIVE)
		OrderFormState newState = OrderFormState.ALL_DELIVE;
		OrderFormState[] oldStateArray = new OrderFormState[] { OrderFormState.WAITING_DELIVE };
		OrderOperateParam param = new OrderOperateParam();
		param.setUserId(orderLogisticsDTO.getUserId());
		param.setOrderId(orderLogisticsDTO.getOrderId());
		param.setBusinessId(orderLogisticsDTO.getBusinessId());
		param.setOperateUserType(orderLogisticsDTO.getOperateUserType());
		param.setNewState(newState);
		param.setOldStateArray(oldStateArray);
		setOrderFormState(param);
		return 1;
	}

	@Override
	public int queryBusiOrderFormListCountByState(long businessId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange) {
		return queryOrderFormListCountByState(businessId, isVisible, stateArray, orderTimeRange, null);
	}

	@Override
	public List<OrderOperateLogDTO> queryOperateLog(OrderOperateLogDTO operateLogDTO, String startTime, String endTime) {
		OrderOperateLog operateLog = new OrderOperateLog(operateLogDTO);
		List<OrderOperateLog> operateLogList = orderOperateLogDao.queryOperateLog(operateLog, startTime, endTime);
		return convertToDTO(operateLogList);
	}

	private List<OrderOperateLogDTO> convertToDTO(List<OrderOperateLog> operateLogList) {
		if (CollectionUtils.isEmpty(operateLogList)) {
			return null;
		}
		List<OrderOperateLogDTO> retList = new ArrayList<OrderOperateLogDTO>(operateLogList.size());
		for (OrderOperateLog log : operateLogList) {
			retList.add(new OrderOperateLogDTO(log));
		}
		return retList;
	}

	@Override
	public boolean updateOrderForm(OrderFormDTO orderFormDTO) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transaction
	@OperateLog(clientType = "web")
	public int updateInvoice(InvoiceDTO invoiceDTO) {
		Invoice invoice = invoiceDao.getObjectById(invoiceDTO.getId());
		if (invoice == null) {
			return -1;
		}
		if (invoiceDTO.getBusinessId() > 0 && invoice.getBusinessId() != invoiceDTO.getBusinessId()) {
			return -2;// 商家修改发票时判断是否商家自己修改
		}
		invoice.setUpdateBy(invoiceDTO.getUpdateBy());
		boolean result = invoiceDao.updateObjectByKey(invoice);
		return result ? 1 : -1;

	}

	@Override
	@Transaction
	@OperateLog(clientType = "web")
	public int addInvoice(InvoiceDTO invoiceDTO) {
		Invoice invoice = invoiceDao.addObject(invoiceDTO);
		return invoice != null ? 1 : -1;
	}

	@Override
	@Transaction
	@OperateLog(clientType = "web")
	public int addOrUpdateOrderLogistics(OrderLogisticsDTO orderLogisticsDTO) {
		OrderLogistics orderLogistics = null;
		if (orderLogisticsDTO.getId() > 0) {
			// 更新
			orderLogistics = orderLogisticsDao.getObjectById(orderLogisticsDTO.getId());
			if (orderLogistics == null) {
				return -1;
			}
			orderLogistics.setMailNO(orderLogisticsDTO.getMailNO());
			orderLogistics.setExpressCompany(orderLogisticsDTO.getExpressCompany());
			orderLogistics.setUpdateBy(orderLogisticsDTO.getUpdateBy());
			orderLogisticsDao.updateObjectByKey(orderLogistics);
		} else {
			// 新增
			orderLogistics = orderLogisticsDao.addObject(orderLogisticsDTO);
		}
		return orderLogistics != null ? 1 : -1;
	}

	@Override
	@Transaction
	@OperateLog(clientType = "web")
	public int modifyOrderFormState(OrderOperateParam param) {
		int result = 0;
		if (param.getParentId() > 0) {// 运营修改订单状态未付款到待发货时，所有关联订单一起修改
			List<Long> orderIds = getSubOrderIdsByParentId(param.getParentId());
			for (Long orderId : orderIds) {
				param.setOrderId(orderId);
				result = setOrderFormState(param);
			}
		} else {
			result = setOrderFormState(param);
		}
		return result;
	}

	private int setOrderFormState(OrderOperateParam param) {
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(param.getOrderId(), param.getUserId());
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + param.getOrderId()
					+ " ,userId=" + param.getUserId());
			return -1;
		}
		OrderForm ordForm = RetArgUtil.get(retArgOfTmp, OrderForm.class);
		if (null == ordForm) {
			throw new ServiceException("没有找到相关订单 [orderId:" + param.getOrderId() + ", userId:" + param.getUserId()
					+ "]");
		}
		// 商家修改订单时判断是否是自己的订单
		if (param.getOperateUserType().equals(OperateUserType.BACKERNDER)
				&& ordForm.getBusinessId() != param.getBusinessId()) {
			return -2;
		}
		// 1.更新订单状态
		OrderForm order = new OrderForm();
		order.setUserId(param.getUserId());
		order.setOrderId(param.getOrderId());
		order.setOrderFormState(param.getNewState());
		order.setOrderFormPayMethod(ordForm.getOrderFormPayMethod());
		boolean isSucc = true;
		if (!param.getNewState().equals(OrderFormState.getNextState(ordForm.getOrderFormState()))) {
			return -3;
		}
		isSucc = orderFormDao.updateOrdState(order, new OrderFormState[] { ordForm.getOrderFormState() });
		param.setOperateLogType(OperateLogType.ORDER_STATE);// OperateLogAspect记录日志区分
		if (isSucc) {
			return 1;
		}
		// 2.如果更新失败,则判断是否属于重复更新的情况(即状态已经流转到下一步)
		order = orderFormDao.getObjectByPrimaryKeyAndPolicyKey(order);
		isSucc = order != null && order.getOrderFormState() == param.getNewState();
		return isSucc ? -4 : 0;
	}

	@Override
	@Transaction
	@OperateLog(clientType = "web")
	public int addOrUpdateOrderCommnet(OrderOperateParam param) {
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(param.getOrderId(), param.getUserId());
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + param.getOrderId()
					+ " ,userId=" + param.getUserId());
			return -1;
		}
		// 1.更新订单备注
		OrderForm orderForm = RetArgUtil.get(retArgOfTmp, OrderForm.class);
		if (param.getOperateUserType().equals(OperateUserType.BACKERNDER)
				&& orderForm.getBusinessId() != param.getBusinessId()) {
			return -2;
		}
		orderForm.setComment(param.getComment());
		orderFormDao.updateComment(orderForm);
		param.setOperateLogType(OperateLogType.ORDER_COMMENT);// OperateLogAspect记录日志区分
		return 1;
	}

	@Override
	@Transaction
	public void addOrderOperateLog(OrderOperateLogDTO dto) throws ServiceException {
		try {
			orderOperateLogDao.addObject(dto);
		} catch (Exception e) {
			logger.error("add order operate error ", e);
			logger.error("operate order detail", dto.toString());
			throw new ServiceException("add order operate error");
		}
	}

	@Transaction
	public void addOrderOperateLogs(List<OrderOperateLogDTO> list) throws ServiceException {
		try {
			orderOperateLogDao.addObjects(list);
		} catch (Exception e) {
			logger.error("add order operates error ", e);
			logger.error("operate order detail", list.toString());
			throw new ServiceException("add order operate error");
		}
	}

	@Override
	public List<Long> getSubOrderIdsByParentId(long parentId) {
		return orderFormDao.getSubOrderIdsByParentId(parentId);
	}

	public List<Long> getParentIds(long parentId, int count) {
		return orderFormDao.getParentIds(parentId, count);
	}

	@Override
	public RetArg queryOrderFormListByOrderSearchParam(OrderSearchParam orderSearchParam) {
		orderSearchParam.setOrderColumn("parentId");
		orderSearchParam.setAsc(false);
		RetArg retArg = new RetArg();
		List<OrderForm> ordList = new ArrayList<OrderForm>();
		OrderFormState[] stateArray = null;
		switch (orderSearchParam.getQueryType()) {
		case 0:// 全部订单
		case 1:// 待付款
			ordList = getSpecialOrderFormDTOList(orderSearchParam);
			break;
		case 2:// 待发货
			stateArray = new OrderFormState[] { OrderFormState.WAITING_DELIVE };
			ordList = orderFormDao.queryOrderFormList(orderSearchParam.getUserId(), orderSearchParam.isVisible(),
					stateArray, null, orderSearchParam);
			break;
		case 3:// 已发货
			stateArray = new OrderFormState[] { OrderFormState.ALL_DELIVE };
			ordList = orderFormDao.queryOrderFormList(orderSearchParam.getUserId(), orderSearchParam.isVisible(),
					stateArray, null, orderSearchParam);
			break;
		case 5:// 订单Id
			ordList = getOrderFormDTOListById(orderSearchParam);
			break;
		default:
			break;
		}
		RetArgUtil.put(retArg, convertToSimpleOrderFormDTOList(ordList));
		RetArgUtil.put(retArg, orderSearchParam);
		return retArg;
	}

	// 按订单Id或者订单parentId获取
	private List<OrderForm> getOrderFormDTOListById(OrderSearchParam orderSearchParam) {
		List<OrderForm> ordList = new ArrayList<OrderForm>();
		// 按订单Id或者parentId查询
		List<Long> orderIds = orderFormDao.getSubOrderIdsByParentId(orderSearchParam.getOrderId());
		if (CollectionUtil.isNotEmptyOfList(orderIds)) {
			ordList = orderFormDao.queryOrderFormListByParentId(orderSearchParam.getOrderId());
			if (CollectionUtil.isNotEmptyOfList(ordList)) {
				List<OrderForm> filterList = new ArrayList<OrderForm>();
				// 订单只包含一个商家时，显示的都是orderId
				if (ordList.size() != 1) {
					for (OrderForm orderform : ordList) {
						if (!orderform.isVisible()) {
							continue;
						}
						if (orderform.getOrderFormState().equals(OrderFormState.WAITING_PAY)
								&& OrderFormPayMethod.isOnlinePayMethod(orderform.getOrderFormPayMethod())
								|| (orderform.getOrderFormState().equals(OrderFormState.WAITING_DELIVE) && !OrderFormPayMethod
										.isOnlinePayMethod(orderform.getOrderFormPayMethod()))) {
							filterList.add(orderform);
						}
					}
				}
				ordList = filterList;
			}
		} else {
			OrderForm orderForm = orderFormDao.getObjectById(orderSearchParam.getOrderId());
			if (orderForm != null && orderForm.isVisible()) {
				ordList.add(orderForm);
			}
		}
		return ordList;
	}

	// 待付款订单或者全部订单时调用
	private List<OrderForm> getSpecialOrderFormDTOList(OrderSearchParam orderSearchParam) {
		int limit = orderSearchParam.getLimit(), offset = orderSearchParam.getOffset();
		List<OrderForm> resultList = new ArrayList<OrderForm>();
		// 订单状态
		OrderFormState[] stateArray = null;
		stateArray = orderSearchParam.getQueryType() == 1 ? new OrderFormState[] { OrderFormState.WAITING_PAY } : null;
		long[] orderTimeRange = new long[] { System.currentTimeMillis() - ConstValueOfOrder.MAX_PAY_TIME,
				System.currentTimeMillis() };
		// 先取出前2个小时的所有订单，待付款状态只停留2个小时
		orderSearchParam.setLimit(1000);// 最多取1000条
		orderSearchParam.setOffset(0);
		List<OrderForm> ordList = orderFormDao.queryOrderFormList(orderSearchParam.getUserId(),
				orderSearchParam.isVisible(), stateArray, orderTimeRange, orderSearchParam);
		int count = 0, totalCount = 0;
		if (CollectionUtil.isNotEmptyOfList(ordList)) {
			long preParentId = 0l;
			for (OrderForm orderForm : ordList) {
				if (!orderForm.getOrderFormState().equals(OrderFormState.WAITING_PAY)) {
					++totalCount;
					if (totalCount > offset) {
						++count;// 计算有效订单数
					}
				} else if (orderForm.getParentId() != preParentId) {
					++totalCount;// 在线支付待付款时合并一条订单
					if (totalCount > offset) {
						++count;
					}
				}
				preParentId = orderForm.getParentId();
				// 从offset起取得订单
				if (totalCount > offset && count <= limit) {
					resultList.add(orderForm);
				}
			}
		}
		// 取出剩余的订单
		orderTimeRange = new long[] { 0, System.currentTimeMillis() - ConstValueOfOrder.MAX_PAY_TIME };
		int leftOffset = offset - totalCount;
		orderSearchParam.setOffset(leftOffset <= 0 ? 0 : leftOffset);
		int leftLimit = limit - count;
		orderSearchParam.setLimit(leftLimit <= 0 ? 1 : leftLimit);
		ordList = orderFormDao.queryOrderFormList(orderSearchParam.getUserId(), orderSearchParam.isVisible(),
				stateArray, orderTimeRange, orderSearchParam);
		orderSearchParam.setTotalCount(orderSearchParam.getTotalCount() + totalCount);
		orderSearchParam.setLimit(limit);// 设回默认值
		orderSearchParam.setOffset(offset);
		if (leftLimit > 0) {
			resultList.addAll(ordList);
		}
		return resultList;
	}

	// 除了取订单表和OrderSku表信息，其他订单信息没取，适用场景：买家端订单列表
	private List<OrderFormDTO> convertToSimpleOrderFormDTOList(List<OrderForm> orderForms) {
		if (CollectionUtil.isEmptyOfList(orderForms)) {
			return null;
		}
		List<OrderFormDTO> resultList = new ArrayList<OrderFormDTO>();
		for (OrderForm orderForm : orderForms) {
			OrderFormDTO ordDTO = OrderFormDTO.genInstance(orderForm);
			// 读取全部的OrderSku
			List<OrderSku> orderSkuList = orderSkuDao.getListByOrderId(orderForm.getOrderId(), orderForm.getUserId(),
					false);
			List<OrderSkuDTO> orderSkuDTOList = ReflectUtil.convertList(OrderSkuDTO.class, orderSkuList, false);
			ordDTO.setOrderSkuDTOListOfOrdGift(orderSkuDTOList);
			resultList.add(ordDTO);
		}
		return resultList;
	}

	@Override
	public int countRelateOrderByorderId(long orderId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OrderSkuDTO getOrderSku(long userId, long orderId, long skuId) {
		OrderSku orderSku = orderSkuDao.getOrderSkuByUserIdAndOrderIdAndSkuId(userId, orderId, skuId);
		if (orderSku != null) {
			return new OrderSkuDTO(orderSku);
		}
		return null;
	}

	@Override
	public OrderForm getOrderForm(long orderId) {
		return orderFormDao.getObjectById(orderId);
	}

	@Override
	public List<OrderSku> getOrderSKUListByOrderIdAndUserId(long orderId, long userId) {
		return orderSkuDao.getListByOrderId(orderId, userId, false);
	}
}
