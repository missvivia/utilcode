package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.param.OrderSearchParam;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月12日 上午10:32:19
 * 
 */
public interface OrderFormDao extends AbstractDao<OrderForm> {

	/**
	 * 查询用户的订单数据
	 * 
	 * @param userId
	 * @param isVisible
	 *            订单是否可见
	 * @param stateArray
	 * @param orderTimeRange
	 * @param param
	 * @return
	 */
	public List<OrderForm> queryOrderFormList(long userId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param);
	
    /**
     * 根据userid和parentid获取订单数据列表
     * 
     * @param userId
     * @param parentId
     * @param isVisible
     * @return
     */
    public List<OrderForm> queryOrderFormListByUserIdAndParentId(long userId, long parentId);
    
    /**
     * 根据parentId获取订单列表
     * @param parentId
     * @param isVisible
     * @return
     */
    public List<OrderForm> queryOrderFormListByParentId(long parentId, boolean isVisible);
    
    /**
     * 根据parentId获取订单列表
     * 
     * @param parentId
     * @return
     */
    public List<OrderForm> queryOrderFormListByParentId(long parentId);
    
    /**
     * 根据userid、parentid和isVisible获取订单数据列表
     * 
     * @param userId
     * @param parentId
     * @param isVisible
     * @return
     */
    public List<OrderForm> queryOrderFormListByUserIdAndParentId(long userId, long parentId,
            Boolean isVisible);

	/**
	 * 查询用户的订单数量
	 * 
	 * @param userId
	 * @param isVisible
	 * @param stateArray
	 * @param orderTimeRange
	 * @param isOnPay
	 * @return
	 */
	public int queryOrderFormListCount(long userId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange,Boolean isOnPay);
	
	/**
	 * 查询商家的订单数量
	 * 
	 * @param userId
	 * @param isVisible
	 * @param stateArray
	 * @param orderTimeRange
	 * @return
	 */
	public int queryBusiOrderFormListCount(long businessId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange);

	/**
	 * 查询指定时间范围的订单数据
	 * 
	 * @param isVisible
	 *            订单是否可见
	 * @param stateArray
	 * @param orderTimeRange
	 * @param param
	 * @return
	 */
	public List<OrderForm> queryOrderFormList(Boolean isVisible, OrderFormState[] stateArray, long[] orderTimeRange,
			DDBParam param);

	/**
	 * 查询指定时间范围的、取货的订单数据
	 * 
	 * @param isVisible
	 *            订单是否可见
	 * @param stateArray
	 * @param orderTimeRange
	 * @param param
	 * @return
	 */
	public List<OrderForm> queryOrderFormListWithLackPackage(Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param);

	/**
	 * 根据用户Id列表查询OrderForm
	 * 
	 * @param userIduserIdList
	 * @param isVisible
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<OrderForm> queryOrderFormList(List<Long> userIdList, Boolean isVisible, OrderFormState[] stateArray,
			DDBParam param);

	/**
	 * 查询用户的订单数据(支持订单Id的模糊查询)
	 * 
	 * @param userId
	 * @param isVisible
	 *            订单是否可见
	 * @param orderIdOfPart
	 *            订单Id的模糊查询
	 * @param orderTimeRange
	 *            下单时间范围
	 * @param param
	 * @return
	 */
	public List<OrderForm> queryOrderFormList(long userId, Boolean isVisible, Long orderIdOfPart,
			long[] orderTimeRange, DDBParam param);

	/**
	 * 查询订单数据(根据订单状态+下单时间范围查询)
	 * 
	 * @param minOrderId
	 *            起始OrderId
	 * @param stateArray
	 *            订单状态
	 * @param orderTimeRange
	 *            下单时间范围
	 * @param param
	 * @return
	 */
	public List<OrderForm> queryOrderFormListWithMinOrderId(long minOrderId, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param);

	/**
	 * 查询订单
	 * 
	 * @param minOrderId
	 *            起始OrderId
	 * @param osArray
	 * @param orderTimeRange
	 *            下单时间范围
	 * @param psArray
	 * @param param
	 * @return
	 */
	public List<OrderForm> queryOrderFormDTOListWithMinOrderId(long minOrderId, OrderFormState[] osArray,
			long[] orderTimeRange, PayState[] psArray, DDBParam param);

	/**
	 * 查询某些状态的订单
	 * 
	 * @param minOrderId
	 * @param stateArray
	 *            null: 全部状态的订单
	 * @param param
	 * @return
	 */
	public List<OrderForm> queryOrderFormListByStateWithMinOrderId(long minOrderId, OrderFormState[] stateArray,
			DDBParam param);

	/**
	 * 查询用户的订单数据数量
	 * 
	 * @param userId
	 * @param isVisible
	 * @param stateArray
	 * @return
	 */
	public int queryOrderFormCount(long userId, Boolean isVisible, OrderFormState[] stateArray, long[] orderTimeRange);

	/**
	 * 批量获得订单锁
	 * 
	 * @param objList
	 */
	public List<OrderForm> getLockByKeys(Collection<OrderForm> objList);

	/**
	 * 批量设置订单状态为newState(更新前状态为oldStateArray)
	 * 
	 * @param orderColl
	 * @param newState
	 *            更新后的订单状态
	 * @param oldStateArray
	 *            更新前的订单状态
	 * @return 0:更新数量为0<br>
	 *         1:更新成功,而且数量一致<br>
	 *         2:更新成功,但是数量比输入参数少
	 */
	@Deprecated
	public int updateOrdState(Collection<? extends OrderForm> orderColl, OrderFormState newState,
			OrderFormState[] oldStateArray);

	/**
	 * 更新订单的状态
	 * 
	 * @param obj
	 *            必须设置的字段有: UserId,OrderId,OrderFormState
	 * @param oldStateArray
	 *            更新前的状态
	 * @return
	 */
	public boolean updateOrdState(OrderForm obj, OrderFormState[] oldStateArray);
	
	/**
	 * 更新备注
	 * @param obj
	 * @return
	 */
	public boolean updateComment(OrderForm obj);

	/**
	 * 更新订单的状态+ExtInfo
	 * 
	 * @param obj
	 *            必须设置的字段有: UserId,OrderId,OrderFormState
	 * @param oldStateArray
	 *            更新前的状态
	 * @param oldExtInfo
	 *            更新前的extInfo
	 * @return
	 */
	public boolean updateOrdStateAndExtInfo(OrderForm obj, OrderFormState[] oldStateArray, String oldExtInfo);

	/**
	 * 
	 * @param obj
	 * @param oldStateArray
	 * @return
	 */
	@Deprecated
	public boolean updatePayState(OrderForm obj, PayState[] oldStateArray);

	/**
	 * 
	 * @param obj
	 * @param oldStateArray
	 * @return
	 */
	@Deprecated
	public boolean updatePayStateWithPayTime(OrderForm obj, PayState[] oldStateArray);

	/**
	 * 更新订单的状态+OMSTime
	 * 
	 * @param obj
	 *            必须设置的字段有: UserId,OrderId,OrderFormState,OMSTime
	 * @param oldStateArray
	 *            更新前的状态
	 * @return
	 */
	public boolean updateOrdStateWithOMSTime(OrderForm obj, OrderFormState[] oldStateArray);

	/**
	 * 更新订单的状态为已签收
	 * 
	 * @param obj
	 *            必须设置的字段有: UserId,OrderId
	 * @return
	 */
	public boolean updateOrdStateToFinishDelive(OrderForm obj);

	/**
	 * 更新:订单的状态+支付状态+支付时间<br>
	 * (支付时间的更新,只有在PayTime大于0才会更新)
	 * 
	 * @param obj
	 *            必须设置的字段有: UserId,OrderId,OrderFormState,PayState,PayTime
	 * @param oldOrderFormStateArray
	 *            更新前的订单状态
	 * @param oldPayStateArray
	 *            更新前的支付状态
	 * @return
	 */
	public boolean updateOrdStateAndPayState(OrderForm obj, OrderFormState[] oldOrderFormStateArray,
			PayState[] oldPayStateArray);

	/**
	 * 更新: 订单的状态+支付方式+支付状态
	 * 
	 * @param objOfOri
	 *            更新前的对象<br>
	 *            (WHERE条件:OrderId,UserId,OrderFormState,OrderFormPayMethod,
	 *            PayState)
	 * @param objOfNew
	 *            更新后的对象<br>
	 *            (SET条件:OrderFormState,OrderFormPayMethod,PayState)
	 * @return
	 */
	public boolean updateOrdByType1(OrderForm objOfOri, OrderForm objOfNew);

	/**
	 * 更新是否可见
	 * 
	 * @param obj
	 * @param oldStateArray
	 *            更新前的状态
	 * @return
	 */
	public boolean updateIsVisible(OrderForm obj, OrderFormState[] oldStateArray);

	/**
	 * 查询在指定的发货时间之后有多少条记录
	 * 
	 * @param userId
	 * @param omsTime
	 * @param stateArray
	 * @return
	 */
	public int queryCountAfterOmsTime(long userId, long omsTime, OrderFormState[] stateArray);

	/**
	 * @param ordForm
	 * @return
	 */
	public boolean updateCODAuditPayTime(OrderForm ordForm);
	
	/**
	 * 根据parentId取订单Ids
	 * @param parentId
	 * @return
	 */
	public List<Long> getSubOrderIdsByParentId(long parentId);
	
	/**
	 * 获取一定数量的parentId
	 * @param parentId 获取到的parentId大于此parentId
	 * @param count 获取的数量
	 * @return
	 */
	public List<Long> getParentIds(long parentId, int count);
	
	/**
	 * 根据搜索条件查询订单
	 * @param param
	 * @return
	 */
	public List<OrderForm> queryOrderFormListByOrderSearchParam(OrderSearchParam param);
	
	/**
	 * 查询订单id erp接口
	 * @param businessIds
	 * @param startTime
	 * @param endTime
	 * @param state
	 * @return
	 */
	public List<OrderForm> queryOrderFormList(String businessIds, long startTime, long endTime, int state);
}