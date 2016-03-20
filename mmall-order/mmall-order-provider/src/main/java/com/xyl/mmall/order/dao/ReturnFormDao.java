package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.enums.ReturnCouponRecycleState;
import com.xyl.mmall.order.enums.ReturnOrderSkuNumState;
import com.xyl.mmall.order.meta.ReturnForm;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午5:14:37
 *
 */
public interface ReturnFormDao extends AbstractDao<ReturnForm> {

	/**
	 * 通过订单id和用户id获取退货记录
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public ReturnForm queryObjectByOrderIdAndUserId(long orderId, long userId);
	
	/**
	 * 更新状态：用户申请退货时，与订单退货数量关联的的状态
	 * 
	 * @param retForm
	 * @param newState
	 * @param oldStates
	 * @return
	 */
	public boolean updateApplyedNumState(ReturnForm retForm, ReturnOrderSkuNumState newState, ReturnOrderSkuNumState[] oldStates);
	
	/**
	 * 更新状态：系统或客服退款操作时，与订单退货数量关联的的状态
	 * 
	 * @param retForm
	 * @param newState
	 * @param oldStates
	 * @return
	 */
	public boolean updateConfirmNumState(ReturnForm retForm, ReturnOrderSkuNumState newState, ReturnOrderSkuNumState[] oldStates);
	
	/**
	 * 查询需要回收优惠券+红包、且尚未回收的退货订单
	 * 
	 * @param minOrdId
	 * @param param
	 * @return
	 */
	public List<ReturnForm> queryWaitingRecycleReturnFormListWithMinOrderId(long minOrdId, DDBParam param);
	
	/**
	 * 更新优惠券+红包回收状态
	 * 
	 * @param retForm
	 * @param newState
	 * @param oldStates
	 * @return
	 */
	public boolean updateReturnCouponHbRecycleState(ReturnForm retForm, ReturnCouponRecycleState newState, 
			ReturnCouponRecycleState[] oldStates);
	
}