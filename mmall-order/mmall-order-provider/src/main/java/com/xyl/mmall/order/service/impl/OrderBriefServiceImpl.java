package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dao.OrderCartItemDao;
import com.xyl.mmall.order.dao.OrderExpInfoDao;
import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.dao.OrderPOInfoDao;
import com.xyl.mmall.order.dao.OrderPackageDao;
import com.xyl.mmall.order.dao.OrderSkuDao;
import com.xyl.mmall.order.dto.OrderCartItemBriefDTO;
import com.xyl.mmall.order.dto.OrderFormBrief2DTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderPOInfoBriefDTO;
import com.xyl.mmall.order.dto.OrderPackageBriefDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.meta.OrderCartItem;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.OrderPOInfo;
import com.xyl.mmall.order.meta.OrderPackage;
import com.xyl.mmall.order.meta.OrderSku;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * 读取一些BriefDTO对象
 * 
 * @author dingmingliang
 * 
 */
@Service("orderBriefService")
public class OrderBriefServiceImpl implements OrderBriefService {

	@Autowired
	private OrderFormDao orderFormDao;

	@Autowired
	private OrderPOInfoDao orderPOInfoDao;

	@Autowired
	private OrderCartItemDao orderCartItemDao;

	@Autowired
	private OrderExpInfoDao orderExpInfoDao;

	@Autowired
	private OrderPackageDao orderPackageDao;

	@Autowired
	private OrderSkuDao orderSkuDao;
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderBriefService#queryOrderPackageBriefDTO(long,
	 *      long)
	 */
	public OrderPackageBriefDTO queryOrderPackageBriefDTO(long userId, long packageId) {
		OrderPackage obj = new OrderPackage();
		obj.setUserId(userId);
		obj.setPackageId(packageId);
		obj = orderPackageDao.getObjectByPrimaryKeyAndPolicyKey(obj);
		return obj == null ? null : new OrderPackageBriefDTO(obj);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderBriefService#queryOrderPackageBriefDTOList(long,
	 *      long)
	 */
	public List<OrderPackageBriefDTO> queryOrderPackageBriefDTOList(long userId, long orderId) {
		List<OrderPackage> opList = orderPackageDao.getListByOrderId(userId, orderId);
		List<OrderPackageBriefDTO> opBDTOList = convertToOrderPackageBriefDTOList(opList);
		return opBDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderBriefService#queryOrderPackageBriefDTOListWithMinPackageId(long,
	 *      long[], com.xyl.mmall.order.enums.OrderPackageState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg queryOrderPackageBriefDTOListWithMinPackageId(long minPackageId, long[] orderTimeRange,
			OrderPackageState[] opStateArray, DDBParam param) {
		param = OrderUtil.initDDBParamWithMinId(param, "packageId");
		RetArg retArg = new RetArg();
		List<OrderPackage> opList = orderPackageDao.getListWithMinPackageId(minPackageId, orderTimeRange, opStateArray,
				param);
		List<OrderPackageBriefDTO> opBDTOList = convertToOrderPackageBriefDTOList(opList);
		RetArgUtil.put(retArg, opBDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderBriefService#queryOrderPackageBriefDTOListByType1WithMinPackageId(long,
	 *      long[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg queryOrderPackageBriefDTOListByType1WithMinPackageId(long minPackageId, long[] orderTimeRange,
			DDBParam param) {
		OrderPackageState[] opStateArray = OrderPackageState.getType1Array(OrderPackageState.TYPE2_ARRAY);
		return queryOrderPackageBriefDTOListWithMinPackageId(minPackageId, orderTimeRange, opStateArray, param);
	}

	/**
	 * @param opList
	 * @return
	 */
	private List<OrderPackageBriefDTO> convertToOrderPackageBriefDTOList(List<OrderPackage> opList) {
		List<OrderPackageBriefDTO> opBDTOList = new ArrayList<>();
		if (CollectionUtil.isNotEmptyOfCollection(opList)) {
			for (OrderPackage op : opList) {
				opBDTOList.add(new OrderPackageBriefDTO(op));
			}
		}
		return opBDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderFormBrief(long,
	 *      long, java.lang.Boolean)
	 */
	public OrderFormBriefDTO queryOrderFormBrief(long userId, long orderId, Boolean isVisible) {
		OrderForm ord = orderFormDao.getObjectByIdAndUserId(orderId, userId);
		if (ord == null || (isVisible != null && ord.isVisible() != isVisible)) {
			return null;
		}
		OrderFormBriefDTO orderBDTO = new OrderFormBriefDTO(ord);
		// 设置是否可以取消的标记位
//		boolean canCancel = canOrderBeCancelled(ord);
//		orderBDTO.setCanCancel(canCancel);
		return orderBDTO;
	}
	
    /**
     * (non-Javadoc)
     * 
     * @see com.xyl.mmall.order.service.OrderService#queryOrderFormBrief(long, long,
     *      java.lang.Boolean)
     */
    public List<OrderFormBriefDTO> queryOrderFormBriefList(long userId, long parentId,
            Boolean isVisible)
    {
        List<OrderFormBriefDTO> result = new ArrayList<OrderFormBriefDTO>();
        
        List<OrderForm> orderFormList = orderFormDao.queryOrderFormListByUserIdAndParentId(userId, parentId);
        if (orderFormList == null)
        {
            return null;
        }
        if(isVisible != null)
        {
            for(OrderForm orderForm : orderFormList)
            {
                if(orderForm.isVisible() != isVisible)
                {
                    return null;
                }
                else
                {
                    OrderFormBriefDTO orderBDTO = new OrderFormBriefDTO(orderForm);
                    // 设置是否可以取消的标记位
                    boolean canCancel = canOrderBeCancelled(orderForm);
                    orderBDTO.setCanCancel(canCancel);
                    result.add(orderBDTO);
                }
            }
        }
        
        if(result.size() == 0)
        {
            result = null;
        }
        return result;
    }

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderBriefService#queryOrderPOInfoBriefDTOList(long,
	 *      long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg queryOrderPOInfoBriefDTOList(long minId, long poId, DDBParam param) {
		param = OrderUtil.initDDBParamWithMinId(param, "id");
		// 1.读取相关的OrderPOInfo记录
		List<OrderPOInfo> orderPOInfoList = orderPOInfoDao.getListByPoId(minId, poId, param);
		if (CollectionUtil.isEmptyOfCollection(orderPOInfoList))
			return null;
		// 2.填充OrderPOInfoBriefDTO
		RetArg retArg = new RetArg();
		List<OrderPOInfoBriefDTO> orderPOInfoBriefDTOList = new ArrayList<>();
		for (OrderPOInfo orderPOInfo : orderPOInfoList) {
			long userId = orderPOInfo.getUserId(), orderId = orderPOInfo.getOrderId();
			OrderPOInfoBriefDTO orderPOInfoBriefDTO = new OrderPOInfoBriefDTO(orderPOInfo);
			OrderFormBriefDTO orderBDTO = queryOrderFormBrief(userId, orderId, null);
			orderPOInfoBriefDTO.setOrderBDTO(orderBDTO);
			orderPOInfoBriefDTOList.add(orderPOInfoBriefDTO);
		}
		// 返回
		RetArgUtil.put(retArg, orderPOInfoBriefDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderBriefService#queryOrderCartItemBriefDTOList(long,
	 *      long)
	 */
	public List<OrderCartItemBriefDTO> queryOrderCartItemBriefDTOList(long userId, long orderId) {
		List<OrderCartItem> cartList = orderCartItemDao.getListByOrderId(userId, orderId);
		List<OrderCartItemBriefDTO> cartBriefList = new ArrayList<>();
		if (CollectionUtil.isNotEmptyOfCollection(cartList)) {
			for (OrderCartItem cart : cartList) {
				cartBriefList.add(new OrderCartItemBriefDTO(cart));
			}
		}
		return cartBriefList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderService#queryOrderFormBriefDTOListByStateWithMinOrderId(long,
	 *      com.xyl.mmall.order.enums.OrderFormState[], long[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg queryOrderFormBriefDTOListByStateWithMinOrderId(long minOrderId, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param) {
		// 初始化Param
		param = OrderUtil.initDDBParamWithMinOrderId(param);
		List<OrderForm> orderList = orderFormDao.queryOrderFormListWithMinOrderId(minOrderId, stateArray,
				orderTimeRange, param);
		List<OrderFormBriefDTO> orderBDTOList = convertToOrderFormBriefDTOList(orderList);

		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, orderBDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.OrderBriefService#queryOrderFormBriefDTOListByStateWithUserId(long, com.xyl.mmall.order.enums.OrderFormState[], long[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryOrderFormBriefDTOListByStateWithUserId(long userId, OrderFormState[] stateArray, 
			long[] orderTimeRange, DDBParam param) {
		List<OrderForm> orderList = orderFormDao.queryOrderFormList(userId, null, stateArray, orderTimeRange, param);
		List<OrderFormBriefDTO> orderBDTOList = convertToOrderFormBriefDTOList(orderList);

		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, orderBDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.OrderBriefService#queryOrderFormBrief2DTOListByStateWithUserId(long, com.xyl.mmall.order.enums.OrderFormState[], long[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryOrderFormBrief2DTOListByStateWithUserId(long userId, OrderFormState[] stateArray, 
			long[] orderTimeRange, DDBParam param) {
		List<OrderForm> orderList = orderFormDao.queryOrderFormList(userId, null, stateArray, orderTimeRange, param);
		List<OrderFormBrief2DTO> orderBDTOList = convertToOrderFormBrief2DTOList(userId, orderList);

		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, orderBDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 将List(OrderForm)转换成List(OrderFormBriefDTO)
	 * 
	 * @param ordList
	 * @return
	 */
	private List<OrderFormBriefDTO> convertToOrderFormBriefDTOList(List<OrderForm> orderList) {
		// 参数有效性判断
		if (CollectionUtil.isEmptyOfCollection(orderList)) {
			return null;
		}

		List<OrderFormBriefDTO> orderBDTOList = new ArrayList<>();
		for (OrderForm order : orderList) {
			OrderFormBriefDTO orderBDTO = new OrderFormBriefDTO(order);
			orderBDTOList.add(orderBDTO);
		}
		return orderBDTOList;
	}
	
	/**
	 * 将List(OrderForm)转换成List(OrderFormBrief2DTO)
	 * 
	 * @param ordList
	 * @return
	 */
	private List<OrderFormBrief2DTO> convertToOrderFormBrief2DTOList(long userId, List<OrderForm> orderList) {
		// 参数有效性判断
		if (CollectionUtil.isEmptyOfCollection(orderList)) {
			return new ArrayList<OrderFormBrief2DTO>();
		}
		Map<Long, OrderFormBrief2DTO> ordMap = new HashMap<Long, OrderFormBrief2DTO>();
		List<Long> orderIdList = new ArrayList<Long>(orderList.size());
		for (OrderForm order : orderList) {
			if(null == order || userId != order.getUserId()) {
				continue;
			}
			long orderId = order.getOrderId();
			ordMap.put(orderId, new OrderFormBrief2DTO(order));
			orderIdList.add(orderId);
		}
		List<OrderSku> ordSkuList = orderSkuDao.getListByOrderIdsAndUserId(userId, orderIdList);
		if(!CollectionUtil.isEmptyOfList(ordSkuList)) {
			for(OrderSku ordSku : ordSkuList) {
				OrderFormBrief2DTO ordDTO = null;
				if(null == ordSku || null == (ordDTO = ordMap.get(ordSku.getOrderId()))) {
					continue;
				}
				ordDTO.getOrdSkuList().add(new OrderSkuDTO(ordSku));
			}
		}
		List<OrderFormBrief2DTO> orderBDTOList = new ArrayList<>(ordMap.size());
		for(Entry<Long, OrderFormBrief2DTO> entry : ordMap.entrySet()) {
			orderBDTOList.add(entry.getValue());
		}
		return orderBDTOList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderBriefService#queryOrderFormBriefByState(java.util.List,
	 *      java.lang.Boolean, com.xyl.mmall.order.enums.OrderFormState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryOrderFormBriefByState(List<Long> userIdList, Boolean isVisible, OrderFormState[] stateArray,
			DDBParam param) {
		RetArg retArg = new RetArg();
		List<OrderFormBriefDTO> orderDTOList = new ArrayList<OrderFormBriefDTO>();
		List<OrderForm> ordFormList = orderFormDao.queryOrderFormList(userIdList, isVisible, stateArray, param);
		if (CollectionUtil.isEmptyOfList(ordFormList)) {
			RetArgUtil.put(retArg, orderDTOList);
			RetArgUtil.put(retArg, param);
			return retArg;
		}
		for (OrderForm ord : ordFormList) {
			OrderFormBriefDTO orderBDTO = new OrderFormBriefDTO(ord);
			// 设置是否可以取消的标记位
//			boolean canCancel = canOrderBeCancelled(ord);
//			orderBDTO.setCanCancel(canCancel);
			orderDTOList.add(orderBDTO);
		}
		RetArgUtil.put(retArg, orderDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderBriefService#queryOrderFormBriefByStateWithTimeRange(long,
	 *      long, boolean, com.xyl.mmall.order.enums.OrderFormState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryOrderFormBriefByStateWithTimeRange(long start, long end, boolean lack,
			OrderFormState[] stateArray, DDBParam param) {
		param = OrderUtil.initDDBParamWithMinOrderId(param);
		RetArg retArg = new RetArg();
		List<OrderFormBriefDTO> orderDTOList = new ArrayList<OrderFormBriefDTO>();
		List<OrderForm> ordFormList = null;
		long[] orderTimeRange = new long[] { start, end };
		if (!lack) {
			ordFormList = orderFormDao.queryOrderFormList(null, stateArray, orderTimeRange, param);
		} 
//		else {
//			ordFormList = orderFormDao.queryOrderFormListWithLackPackage(null, stateArray, orderTimeRange, param);
//		}
		if (CollectionUtil.isEmptyOfList(ordFormList)) {
			RetArgUtil.put(retArg, orderDTOList);
			RetArgUtil.put(retArg, param);
			return retArg;
		}
		for (OrderForm ord : ordFormList) {
			OrderFormBriefDTO orderBDTO = new OrderFormBriefDTO(ord);
			// 设置是否可以取消的标记位
			//boolean canCancel = canOrderBeCancelled(ord);
			//orderBDTO.setCanCancel(canCancel);
			orderDTOList.add(orderBDTO);
		}
		RetArgUtil.put(retArg, orderDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.OrderBriefService#canOrderBeCancelled(com.xyl.mmall.order.meta.OrderForm)
	 */
	@Override
	public boolean canOrderBeCancelled(OrderForm ord) {
		long userId = ord.getUserId(), orderId = ord.getOrderId();
		List<OrderPackage> packageList = orderPackageDao.getListByOrderId(userId, orderId);
		return OrderUtil.canCancel(ord, packageList);
	}

	//TODO
	@Override
	public OrderFormBriefDTO queryOrderFormBriefBy(long orderId,
			long businessId, Boolean isVisible) {
		OrderForm ord = orderFormDao.getObjectById(orderId);
		if (ord == null || (isVisible != null && ord.isVisible() != isVisible)) {
			return null;
		}
		OrderFormBriefDTO orderBDTO = new OrderFormBriefDTO(ord);
		// 设置是否可以取消的标记位
		boolean canCancel = canOrderBeCancelled(ord);
		orderBDTO.setCanCancel(canCancel);
		return orderBDTO;
	}
	
	@Override
	public RetArg queryOrderFormBriefByOrderSearchParam(OrderSearchParam orderSearchParam) {
		RetArg retArg = new RetArg();
		List<OrderForm> ordFormList = orderFormDao.queryOrderFormListByOrderSearchParam(orderSearchParam);
		if (CollectionUtil.isEmptyOfList(ordFormList)) {
			RetArgUtil.put(retArg, null);
			RetArgUtil.put(retArg, orderSearchParam);
			return retArg;
		}
		List<OrderFormBriefDTO> orderDTOList = new ArrayList<OrderFormBriefDTO>();
		for (OrderForm ord : ordFormList) {
			OrderFormBriefDTO orderBDTO = new OrderFormBriefDTO(ord);
			orderDTOList.add(orderBDTO);
		}
		RetArgUtil.put(retArg, orderDTOList);
		RetArgUtil.put(retArg, orderSearchParam);
		return retArg;
	}

	@Override
	public List<OrderForm> queryOrderFormList(String businessIds, long startTime, long endTime, int state) {
		return orderFormDao.queryOrderFormList(businessIds, startTime, endTime, state);
	}

	@Override
	public Map<Long, Integer> getProductSaleNumMapByOrderIds(
			Collection<Long> orderIdColl) {
		return orderSkuDao.getProductSaleNumMapByOrderIds(orderIdColl);
	}

	@Override
	public RetArg queryLimitOrderBrief(OrderSearchParam orderSearchParam) {
		RetArg retArg = new RetArg();
		int limit = orderSearchParam.getLimit();
		int offset = orderSearchParam.getOffset();
		// 取出时间段内全部订单
		orderSearchParam.setLimit(0);
		orderSearchParam.setOffset(0);
		List<OrderForm> orderList = orderFormDao.queryOrderFormListByOrderSearchParam(orderSearchParam);
		orderSearchParam.setLimit(limit);
		orderSearchParam.setOffset(offset);
		RetArgUtil.put(retArg, orderList);
		RetArgUtil.put(retArg, orderSearchParam);
		return retArg;
	}
}
