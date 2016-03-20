package com.xyl.mmall.order.service;

import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.OrderPackageRefundTaskDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderPackageRefundTaskState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.enums.OrderSkuReturnJudgement;
import com.xyl.mmall.order.enums.PackageReturnJudgement;
import com.xyl.mmall.order.meta.OrderPackage;
import com.xyl.mmall.order.param.OrderServiceUpdatePackageIdParam;
import com.xyl.mmall.order.param.SetPackageToOutTimeParam;

/**
 * 订单包裹服务
 * 
 * @author hzwangjianyi@corp.netease.com
 * 
 */
public interface OrderPackageSimpleService {

	/**
	 * 查询订单包裹消息
	 * 
	 * @param orderPackageId
	 * @return
	 */
	public OrderPackageSimpleDTO queryOrderPackageSimple(long orderPackageId);

	/**
	 * 查询订单包裹消息
	 * 
	 * @param userId
	 * @param orderPackageId
	 * @return
	 */
	public OrderPackageSimpleDTO queryOrderPackageSimple(long userId, long orderPackageId);

	/**
	 * 查询订单包裹消息
	 * 
	 * @param userId
	 * @param orderId
	 * @param stateArray
	 * @return
	 */
	public List<OrderPackageSimpleDTO> queryOrderPackageSimpleByOrderId(long userId, long orderId,
			OrderPackageState[] stateArray);

	/**
	 * 查询订单包裹数量
	 * 
	 * @param userId
	 * @param orderId
	 * @param stateArray
	 * @return
	 */
	public int queryOrderPackageNum(long userId, long orderId, OrderPackageState[] stateArray);
	
	/**
	 * 查询某个用户，某些状态的订单包裹
	 * 
	 * @param userId
	 *            null: 忽略是否可见的条件
	 * @param stateArray
	 *            null: 全部状态的订单包裹
	 * @param param
	 * @return
	 */
	public List<OrderPackageSimpleDTO> queryOrderPackageSimpleByState(long userId, OrderPackageState[] stateArray,
			DDBParam param);

	/**
	 * 根据快递单号查询订单包裹
	 * 
	 * @param mailNO
	 * @return
	 */
	public List<OrderPackageSimpleDTO> queryOrderPackageSimpleByMailNO(String mailNO);

	/**
	 * 订单包裹是否可以退货
	 * 
	 * @param userId
	 * @param orderPackageId
	 * @param earliestPOEndTime
	 * @return
	 */
	public PackageReturnJudgement canUserReturnPackage(long userId, long orderPackageId, long earliestPOEndTime);

	/**
	 * 订单包裹是否可以退货
	 * 
	 * @param orderPackage
	 * @param earliestPOEndTime
	 * @return
	 */
	public PackageReturnJudgement canUserReturnPackage(OrderPackageSimpleDTO orderPackage, long earliestPOEndTime);

	/**
	 * 包裹里的OrderSku是否可以退货
	 * 
	 * @param orderJudgement
	 * @param userId
	 * @param orderSkuId
	 * @return
	 */
	public OrderSkuReturnJudgement canOrderSkuBeReturned(PackageReturnJudgement orderJudgement, long userId,
			long orderSkuId);

	/**
	 * 包裹里的OrderSku是否可以退货
	 * 
	 * @param orderJudgement
	 * @param orderSkuDTO
	 * @return
	 */
	public OrderSkuReturnJudgement canOrderSkuBeReturned(PackageReturnJudgement orderJudgement,
			OrderSkuDTO orderSkuDTO);

	/**
	 * 是否向客服展示“开放退货入口”
	 * 
	 * @param userId
	 * @param orderPackageId
	 * @param earliestPOEndTime
	 * @return
	 */
	public boolean canReopenReturnShowToKF(long userId, long orderPackageId, long earliestPOEndTime);

	/**
	 * 是否向客服展示“开放退货入口”
	 * 
	 * @param ordPkgDTO
	 * @param earliestPOEndTime
	 * @return
	 */
	public boolean canReopenReturnShowToKF(OrderPackageSimpleDTO ordPkgDTO, long earliestPOEndTime);

	/**
	 * 客服开放退货入口
	 * 
	 * @param userId
	 * @param orderPackageId
	 * @param earliestPOEndTime
	 * @return
	 */
	public boolean reOpenReturn(long userId, long orderPackageId, long earliestPOEndTime);
	
	/**
	 * 添加异步退款任务
	 * 
	 * @param op
	 * @param wybCash
	 * @param redCash
	 * @param rtype
	 * @return
	 */
	public boolean addOrderPackageRefundTask(OrderPackage op, BigDecimal wybCash, BigDecimal redCash, OrderCancelRType rtype);
	
	/**
	 * 更新包裹退款任务记录的retryFlag字段(附带retryCount++)
	 * 
	 * @param obj
	 * @param retryFlagOfOld
	 * @return
	 */
	public boolean updateRetryFlag(OrderPackageRefundTaskDTO obj, long retryFlagOfOld);
	
	
	/**
	 * 读取包裹退款任务记录
	 * 
	 * @param minId
	 * @param state
	 * @param param
	 * @return
	 */
	public List<OrderPackageRefundTaskDTO> getOrderPackageRefundTaskListByStateWithMinId(long minId, OrderPackageRefundTaskState state,
			DDBParam param);

	/**
	 * 设置包裹信息
	 * 
	 * @param paramList
	 * @return
	 */
	public boolean updatePackageId(List<OrderServiceUpdatePackageIdParam> paramList);
	
	/**
	 * 设置某个快递的包裹状态为超时未配送
	 * 
	 * @param param
	 * @return
	 */
	public boolean setPackageToOutTime(SetPackageToOutTimeParam param);

	/**
	 * 设置某个快递的包裹状态为已签收
	 * 
	 * @param mailNO
	 * @param expressCompanyReturn
	 * @return
	 */
	public boolean setPackageToSignIn(String mailNO, String expressCompanyReturn);

	/**
	 * 设置某个包裹状态为退货申请中
	 * 
	 * @param packageId
	 * @param userId
	 * @return
	 */
	public boolean setPackageToRPApply(long packageId, long userId);

	/**
	 * 设置某个包裹状态为退货完成
	 * 
	 * @param packageId
	 * @param userId
	 * @return
	 */
	public boolean setPackageToRPDone(long packageId, long userId);

	/**
	 * 设置某个包裹状态为撤销退货
	 * 
	 * @param packageId
	 * @param userId
	 * @return
	 */
	public boolean setPackageToCancelRPApply(long packageId, long userId);

	/**
	 * 确认包裹状态为:包裹取消-超时未配送<br>
	 * 1.更新状态为 CANCEL_OT<br>
	 * 2.发送退款操作(现金+红包)
	 * 
	 * @param packageId
	 * @param userId
	 * @return
	 */
	public boolean setPackageToCancelOT(long packageId, long userId);

	/**
	 * 确认包裹状态为:包裹取消-拒收<br>
	 * 1.更新状态为 CANCEL_SR<br>
	 * 2.发送退款操作(现金+红包)
	 * 
	 * @param packageId
	 * @param userId
	 * @return
	 */
	public boolean setPackageToCancelSR(long packageId, long userId);

	/**
	 * 确认包裹状态为:包裹取消-丢件<br>
	 * 1.更新状态为 CANCEL_LOST<br>
	 * 2.发送退款操作(现金+红包)
	 * 
	 * @param packageId
	 * @param userId
	 * @return
	 */
	public boolean setPackageToCancelLost(long packageId, long userId);

}
