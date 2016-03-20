package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelState;
import com.xyl.mmall.order.meta.OrderCancelInfo;

/**
 * @author dingmingliang
 * 
 */
public interface OrderCancelInfoDao extends AbstractDao<OrderCancelInfo> {

	/**
	 * 更新退款方式 
	 * @param obj
	 * @param oldRTypes
	 * @param newRType
	 * @return
	 */
	public boolean setCancelRType(OrderCancelInfo obj, OrderCancelRType[] oldRTypes, OrderCancelRType newRType);
	
	/**
	 * 更新取消状态
	 * 
	 * @param obj
	 * @return
	 */
	public boolean setCancelState(OrderCancelInfo obj);

	/**
	 * 设置retryFlag==0的取消任务状态为完成
	 * 
	 * @return
	 */
	public boolean setOrderCancelToDone();

	/**
	 * 读取指定状态的订单取消原因记录
	 * 
	 * @param minOrderId
	 * @param cancelState
	 * @param param
	 * @return
	 */
	public List<OrderCancelInfo> getListByStateWithMinOrderId(long minOrderId, OrderCancelState cancelState,
			DDBParam param);
	
	/**
	 * 读取指定退款方式的订单取消原因记录
	 * 
	 * @param minOrderId
	 * @param cancelRType
	 * @param param
	 * @return
	 */
	public List<OrderCancelInfo> getListByRTypeWithMinOrderId(long minOrderId, OrderCancelRType cancelRType,
			DDBParam param);

	/**
	 * 更新retryFlag字段
	 * 
	 * @param obj
	 * @param retryFlagOfOld
	 * @return
	 */
	public boolean updateRetryFlag(OrderCancelInfo obj, long retryFlagOfOld);

	/**
	 * retryCount++
	 * 
	 * @param obj
	 * @return
	 */
	public boolean incrRetryCount(OrderCancelInfo obj);
}
