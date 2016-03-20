package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.enums.DeprecatedReturnCouponHbRecycleState;
import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.meta.DeprecatedReturnForm;
import com.xyl.mmall.order.param.KFParam;
import com.xyl.mmall.order.param.PassReturnOperationParam;
import com.xyl.mmall.order.param.ReturnOperationParam;
import com.xyl.mmall.order.param.ReturnPackageExpInfoParam;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午5:14:37
 *
 */
@Deprecated
public interface DeprecatedReturnFormDao extends AbstractDao<DeprecatedReturnForm> {

	/**
	 * 通过订单id和用户id获取退货记录
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<DeprecatedReturnForm> queryObjectByOrderIdAndUserId(long orderId, long userId, DDBParam param);
	
	/**
	 * 通过订单id和用户id获取指定状态的退货记录
	 * 
	 * @param orderId
	 * @param userId
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnForm> queryObjectByOrderIdAndUserIdWithStates(long orderId, long userId, DeprecatedReturnState[] stateArray, 
			DDBParam param);
	
	/**
	 * 通过订单id和用户id获取退货记录
	 * 
	 * @param orderIdColl
	 * @param userId
	 * @return
	 */
	public List<DeprecatedReturnForm> queryObjectByOrderIdAndUserId(Collection<Long> orderIdColl, long userId, DDBParam param);
	
	/**
	 * 查询指定状态的退货记录
	 * 
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnForm> queryReturnFormListByState(DeprecatedReturnState[] stateArray, DDBParam param);
	
	/**
	 * 查询指定用户指定状态的退货记录
	 * 
	 * @param stateArray
	 * @param userId
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnForm> queryReturnFormListByStateWithUserId(DeprecatedReturnState[] stateArray, long userId, DDBParam param);
	
	/**
	 * 查询指定订单指定状态的退货记录
	 * 
	 * @param stateArray
	 * @param orderId
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnForm> queryReturnFormListByStateWithOrderId(DeprecatedReturnState[] stateArray, long orderId, DDBParam param);
	
	/**
	 * 查询需要回收优惠券+红包、且尚未回收的退货订单
	 * 
	 * @param minRetId
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnForm> queryWaitingRecycleReturnFormListByStateWithMinReturnId(long minRetId, DDBParam param);
	
	/**
	 * 查询向JIT推送信息失败的退货订单
	 * 
	 * @param stateArray
	 * @param minRetId
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnForm> queryJITFailedReturnFormListByStateWithMinReturnId(DeprecatedReturnState[] stateArray, 
			long minRetId, DDBParam param);

	/**
	 * 查询指定时间指定状态的退货记录
	 * 
	 * @param stateArray
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnForm> queryReturnFormListByStateWithTimeRange(DeprecatedReturnState[] stateArray, long start, long end, DDBParam param);
	
	/**
	 * 查询指定物流指定状态的退货记录
	 * 
	 * @param stateArray
	 * @param mailNO
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnForm> queryReturnFormListByStateWithMailNO(DeprecatedReturnState[] stateArray, String mailNO, DDBParam param);
	
	/**
	 * 更新退货状态
	 * 
	 * @param retForm
	 * @param stateArray
	 * @return
	 */
	public boolean setReturnState(DeprecatedReturnForm retForm, DeprecatedReturnState[] stateArray);
	
	/**
	 * 更新优惠券+红包回收状态
	 * 
	 * @param retForm
	 * @param stateArray
	 * @return
	 */
	public boolean setReturnCouponHbRecycleState(DeprecatedReturnForm retForm, DeprecatedReturnCouponHbRecycleState[] stateArray);
	
	/**
	 * 更新退货状态为“待收货”，同时添加地址信息
	 * 
	 * @param retForm
	 * @return
	 */
	public boolean setReturnStateToWaitingConfirmWithExpInfo(DeprecatedReturnForm retForm, ReturnPackageExpInfoParam param);
	
	/**
	 * 更新退货状态为“待退货”
	 * 
	 * @param retForm
	 * @param abnomal
	 * @return
	 */
	public boolean setReturnStateToWaitingReturnAuditWithConfirmTime(DeprecatedReturnForm retForm, boolean abnomal);

	/**
	 * 更新退货状态为“拒绝”
	 * 
	 * @param retForm
	 * @param param
	 * @return
	 */
	public boolean setReturnStateToRefusedWithParam(DeprecatedReturnForm retForm, ReturnOperationParam param, KFParam kf);
	
	/**
	 * 撤销拒绝操作
	 * 
	 * @param retForm
	 * @param param
	 * @return
	 */
	public boolean cancelRefuse(DeprecatedReturnForm retForm, ReturnOperationParam param, KFParam kf);
	
	/**
	 * 更新退货状态为“完成”
	 * 
	 * @param retForm
	 * @param param
	 * @return
	 */
	public boolean setReturnStateToReturnedWithParam(DeprecatedReturnForm retForm, PassReturnOperationParam param, KFParam kf);
	
	/**
	 * 找到不同省份下面的退货确认数目
	 * 
	 * @return
	 */
	public Map<Integer, Long> getWaitingReturnAuditCount();
	
	/**
	 * 找出订单发货时间超出detainTime、且仓库没有收到退货的退货记录Id
	 * 
	 * @return userId -> returnFormId List
	 */
	public Map<Long, List<Long>> getReturnFormListShouldBeCanceled(long detainTime);
	
	/**
	 * 找出退款成功、10元的优惠券补贴未发的退货记录
	 * 
	 * @param minRetId
	 * @param ddbParam
	 * @return
	 */
	public List<DeprecatedReturnForm> getReturnedButNotDistributedReturnFormList(long minRetId, DDBParam ddbParam);
	
	/**
	 * 更新状态：退款成功、10元的优惠券补贴未发的退货记录
	 * 
	 * @param retId
	 * @return
	 */
	public boolean distributeCoupon(DeprecatedReturnForm retForm);
}