package com.xyl.mmall.order.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.OrderCartItemBriefDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderPackageBriefDTO;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.param.OrderSearchParam;

/**
 * 读取一些BreifDTO对象
 * 
 * @author dingmingliang
 * 
 */
public interface OrderBriefService {

	/**
	 * 获取订单包裹
	 * 
	 * @param userId
	 * @param packageId
	 * @return
	 */
	public OrderPackageBriefDTO queryOrderPackageBriefDTO(long userId, long packageId);

	/**
	 * 获取订单包裹
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public List<OrderPackageBriefDTO> queryOrderPackageBriefDTOList(long userId, long orderId);

	/**
	 * 获取订单包裹
	 * 
	 * @param packageId
	 * @param orderTimeRange
	 * @param opStateArray
	 * @param param
	 * @return RetArg.ArrayList: List(OrderPackageBriefDTO)<br>
	 *         RetArg.DDBParam
	 */
	public RetArg queryOrderPackageBriefDTOListWithMinPackageId(long minPackageId, long[] orderTimeRange,
			OrderPackageState[] opStateArray, DDBParam param);
	
	/**
	 * 获取订单包裹(包裹状态为:取消+退货)
	 * 
	 * @param packageId
	 * @param orderTimeRange
	 * @param param
	 * @return RetArg.ArrayList: List(OrderPackageBriefDTO)<br>
	 *         RetArg.DDBParam
	 */
	public RetArg queryOrderPackageBriefDTOListByType1WithMinPackageId(long minPackageId, long[] orderTimeRange, DDBParam param);

	/**
	 * 查询订单信息
	 * 
	 * @param userId
	 * @param orderId
	 * @param isVisible
	 * @return
	 */
	public OrderFormBriefDTO queryOrderFormBrief(long userId, long orderId, Boolean isVisible);
	
	/**
	 * 根据userid和parentid获取订单信息列表
	 * @param userId
	 * @param parentId
	 * @param isVisibale
	 * @return
	 */
	public List<OrderFormBriefDTO> queryOrderFormBriefList(long userId, long parentId, Boolean isVisible);
	
	
	/**
	 * 根据订单ID和商家ID查询订单信息
	 * 
	 * @param orderId
	 * @param businessId
	 * @param isVisible
	 * @return
	 */
	public OrderFormBriefDTO queryOrderFormBriefBy(long orderId,long businessId, Boolean isVisible);

	/**
	 * 根据poId,查询对应的订单和poId的映射关系
	 * 
	 * @param minId
	 * @param poId
	 * @param param
	 * @return RetArg.ArrayList: List(OrderPOInfoBriefDTO)<br>
	 *         RetArg.DDBParam
	 */
	public RetArg queryOrderPOInfoBriefDTOList(long minId, long poId, DDBParam param);

	/**
	 * 读取订单明细
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public List<OrderCartItemBriefDTO> queryOrderCartItemBriefDTOList(long userId, long orderId);

	/**
	 * 查询某些状态的订单
	 * 
	 * @param minOrderId
	 * @param stateArray
	 *            null: 全部状态的订单
	 * @param orderTimeRange
	 * @param param
	 * @return RetArg.ArrayList: List(OrderFormBriefDTO)<br>
	 *         RetArg.DDBParam: 数据库查询参数
	 */
	public RetArg queryOrderFormBriefDTOListByStateWithMinOrderId(long minOrderId, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param);
	
	/**
	 * 查询用户某些状态的订单
	 * 
	 * @param userId
	 * @param stateArray
	 *            null: 全部状态的订单
	 * @param orderTimeRange
	 * @param param
	 * @return RetArg.ArrayList: List(OrderFormBriefDTO)<br>
	 *         RetArg.DDBParam: 数据库查询参数
	 */
	public RetArg queryOrderFormBriefDTOListByStateWithUserId(long userId, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param);
	
	/**
	 * 查询用户某些状态的订单
	 * 
	 * @param userId
	 * @param stateArray
	 *            null: 全部状态的订单
	 * @param orderTimeRange
	 * @param param
	 * @return RetArg.ArrayList: List(OrderFormBrief2DTO)<br>
	 *         RetArg.DDBParam: 数据库查询参数
	 */
	public RetArg queryOrderFormBrief2DTOListByStateWithUserId(long userId, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param);

	/**
	 * 查询指定用户、指定状态的订单
	 * 
	 * @param userIdList
	 * @param isVisible
	 * @param stateArray
	 * @param param
	 * @return RetArg.OrderFormBriefDTO RetArg.DDBParam
	 */
	public RetArg queryOrderFormBriefByState(List<Long> userIdList, Boolean isVisible, OrderFormState[] stateArray,
			DDBParam param);

	/**
	 * 根据时间范围查询指定状态的订单
	 * 
	 * @param start
	 * @param end
	 * @param lack
	 * @param stateArray
	 * @param param
	 * @return RetArg.OrderFormBriefDTO RetArg.DDBParam
	 */
	public RetArg queryOrderFormBriefByStateWithTimeRange(long start, long end, boolean lack,
			OrderFormState[] stateArray, DDBParam param);
	
	/**
	 * 订单是否可以取消
	 * 
	 * @param ordForm
	 * @return
	 */
	public boolean canOrderBeCancelled(OrderForm ordForm);
	
	/**
	 * 分页搜索订单
	 * @param orderSearchParam
	 * @return
	 */
	public RetArg queryOrderFormBriefByOrderSearchParam(OrderSearchParam orderSearchParam);

	/**
	 * 查询订单id erp接口
	 * @param businessIds
	 * @param startTime
	 * @param endTime
	 * @param state
	 * @return
	 */
	public List<OrderForm> queryOrderFormList(String businessIds, long startTime, long endTime, int state);
	
	/**
	 * 
	 * 根据订单获取商品销售数量
	 * @param orderIdColl
	 * @return
	 */
	public Map<Long, Integer> getProductSaleNumMapByOrderIds(Collection<Long> orderIdColl);

	/**
	 * 分页搜索商品限购订单
	 * @param orderSearchParam
	 * @return RetArg
	 */
	public RetArg queryLimitOrderBrief(OrderSearchParam orderSearchParam);
}
