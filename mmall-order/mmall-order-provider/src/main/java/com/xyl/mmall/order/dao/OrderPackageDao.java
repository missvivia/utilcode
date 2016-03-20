package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.meta.OrderExpInfo;
import com.xyl.mmall.order.meta.OrderPackage;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月12日 上午10:23:15
 * 
 */
public interface OrderPackageDao extends AbstractDao<OrderPackage> {

	/**
	 * 读取某个用户的包裹信息
	 * 
	 * @param userId
	 * @param states
	 * @param ddbParam
	 * @return
	 */
	public List<OrderPackage> getListByUserIdWithState(long userId, OrderPackageState[] states, DDBParam ddbParam);

	/**
	 * 读取符合条件的包裹记录
	 * 
	 * @param minPackageId
	 * @param orderTimeRange
	 * @param opStateArray
	 * @param param
	 * @return
	 */
	public List<OrderPackage> getListWithMinPackageId(long minPackageId, long[] orderTimeRange,
			OrderPackageState[] opStateArray, DDBParam param);
	
	/**
	 * 读取用户的某些订单的包裹信息
	 * 
	 * @param userId
	 * @param orderIdColl
	 * @return
	 */
	public List<OrderPackage> getListByOrderIdsAndUserId(long userId, Collection<Long> orderIdColl);

	/**
	 * 读取某个订单的包裹信息
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public List<OrderPackage> getListByOrderId(long userId, long orderId);

	/**
	 * 读取某个订单的包裹信息
	 * 
	 * @param userId
	 * @param orderId
	 * @param states
	 * @return
	 */
	public List<OrderPackage> getListByOrderIdWithState(long userId, long orderId, OrderPackageState[] states);
	
	/**
	 * 读取某个订单的包裹数量
	 * 
	 * @param userId
	 * @param orderId
	 * @param states
	 * @return
	 */
	public int getOrderPackageNumWithState(long userId, long orderId, OrderPackageState[] states);

	/**
	 * 根据快递单号查询包裹信息
	 * 
	 * @param mailNO
	 * @return
	 */
	public List<OrderPackage> getListByMailNO(String mailNO);

	/**
	 * 更新快递号
	 * 
	 * @param obj
	 * @return
	 */
	public boolean setMailNO(OrderPackage obj);

	/**
	 * 更新包裹状态
	 * 
	 * @param obj
	 * @param oldState
	 *            更新前的状态,不允许为null
	 * @return false: 更新失败 or oldState==null
	 */
	public boolean updateOrderPackageState(OrderPackage obj, OrderPackageState oldState);

	/**
	 * 更新包裹状态+confirmTime
	 * 
	 * @param obj
	 *            (OrderPackageState值必须是OrderPackageState.SIGN_IN)
	 * @param oldState
	 *            更新前的状态,不允许为null
	 * @return false: 更新失败 or oldState==null
	 */
	public boolean updateOrderPackageStateAndConfirmTime(OrderPackage obj, OrderPackageState oldState);

	/**
	 * 更新包裹状态+取消时间
	 * 
	 * @param obj
	 *            (OrderPackageState值必须是OrderPackageState.getCancelArray()中的一个)
	 * @param oldState
	 *            更新前的状态,不允许为null
	 * @return false: 更新失败 or oldState==null
	 */
	public boolean updateOrderPackageStateAndCancelTime(OrderPackage obj, OrderPackageState oldState);

	/**
	 * 更新包裹状态+取消时间
	 * 
	 * @param obj
	 *            (CancelTime自动设置为0)
	 * @param oldState
	 *            更新前的状态,不允许为null
	 * @return false: 更新失败 or oldState==null
	 */
	public boolean updateOrderPackageStateAndZeroCancelTime(OrderPackage obj, OrderPackageState oldState);
}
