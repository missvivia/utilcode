package com.xyl.mmall.order.service;

import java.util.List;
import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.ReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.ReturnPackageState;

/**
 * 退货服务
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:23:48
 *
 */
public interface ReturnPackageQueryService {
	
	/**
	 * 根据退货包裹id查询退货信息
	 * 
	 * @param retPkgId
	 * @return
	 */
	public ReturnPackageDTO queryReturnPackageByRetPkgId(long retPkgId);
	
	/**
	 * 根据退货包裹id、用户id查询退货信息
	 * 
	 * @param userId
	 * @param retPkgId
	 * @return
	 */
	public ReturnPackageDTO queryReturnPackageByRetPkgId(long userId, long retPkgId);
	
	/**
	 * 查询某个用户某些状态的退货订单
	 * 
	 * @param userId
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<ReturnPackageDTO> queryReturnPackageByUserIdWithState(long userId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param);
	
	/**
	 * 根据用户id、包裹id查询退货信息
	 * 
	 * @param userId
	 * @param orderPackageId
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackageDTO> queryReturnPackageByOrderPackageId(long userId, long orderPackageId, boolean deprecated, DDBParam param);
	
	/**
	 * 根据用户id、订单id查询退货信息
	 * 
	 * @param userId
	 * @param orderId
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackageDTO> queryReturnPackageByUserIdAndOrderId(long userId, long orderId, boolean deprecated, DDBParam param);
	
	/**
	 * 根据用户id、订单id查询退货成功的退款信息
	 * 
	 * @param userId
	 * @param orderId
	 * @param param
	 * @return
	 */
	public List<ReturnPackageDTO> querySuccessfullyReturnedPackageByUserIdAndOrderId(long userId, long orderId, DDBParam param);
	
	/**
	 * 根据用户id、订单id查询还在处理中的退货记录数
	 * 
	 * @param userId
	 * @param orderId
	 * @return -1 for error; other for total
	 */
	public int queryWaitingReturningPackageByUserIdAndOrderId(long userId, long orderId);
	
	/**
	 * 根据用户id、订单id、退货状态查询退货信息
	 * 
	 * @param userId
	 * @param orderId
	 * @param deprecated
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<ReturnPackageDTO> queryReturnPackageByUserIdAndOrderIdWithState(long userId, long orderId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param);
	
	/**
	 * 根据用户id、订单id查询退货信息
	 * 
	 * @param userId
	 * @param deprecated 
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<ReturnPackageDTO> queryReturnPackageByOrderIdWithState(long orderId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param);
	
	/**
	 * 查询某个订单某些状态的退货订单
	 * 
	 * @param orderId
	 * @param deprecated 
	 * @param stateArray
	 * @param param
	 * @return
	 *     RetArg.ArrayList<_ReturnPackageDTO>
	 *     RetArg.DDBParam
	 */
	public RetArg queryReturnPackageByOrderIdWithState2(long orderId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param);
	/**
	 * 根据包裹id查询退货信息
	 * 
	 * @param orderPackageId
	 * @param deprecated
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<ReturnPackageDTO> queryReturnPackageByOrderPackageIdWithState(long orderPackageId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param);
	
	/**
	 * 根据用包裹id查询退货信息
	 * 
	 * @param orderPackageId
	 * @param deprecated
	 * @param stateArray
	 * @param param
	 * @return
	 *     RetArg.ArrayList<_ReturnPackageDTO>
	 *     RetArg.DDBParam
	 */
	public RetArg queryReturnPackageByOrderPackageIdWithState2(long orderPackageId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param);
	
	/**
	 * 根据时间范围、退货状态查询退货信息
	 * 
	 * @param start
	 * @param end
	 * @param deprecated 
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<ReturnPackageDTO> queryReturnPackageByTimeRange(long start, long end, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param);
	
	/**
	 * 根据时间范围、退货状态查询退货信息
	 * 
	 * @param start
	 * @param end
	 * @param deprecated 
	 * @param stateArray
	 * @param param
	 * @return
	 *     RetArg.ArrayList<_ReturnPackageDTO>
	 *     RetArg.DDBParam
	 */
	public RetArg queryReturnPackageByTimeRange2(long start, long end, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param);
	
	/**
	 * 根据快递单号、退货状态查询退货信息
	 * 
	 * @param mailNO
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<ReturnPackageDTO> queryReturnPackageByMailNO(String mailNO, boolean deprecated, ReturnPackageState[] stateArray, 
			DDBParam param);
	
	/**
	 * 根据快递单号、退货状态查询退货信息
	 * 
	 * @param mailNO
	 * @param stateArray
	 * @param param
	 * @return
	 *     RetArg.ArrayList<_ReturnPackageDTO>
	 *     RetArg.DDBParam
	 */
	public RetArg queryReturnPackageByMailNO2(String mailNO, boolean deprecated, ReturnPackageState[] stateArray, 
			DDBParam param);
	
	/**
	 * 货到付款：退货银行卡信息
	 * 
	 * @param cardRecordId
	 * @param userId
	 * @return
	 */
	public ReturnCODBankCardInfoDTO queryReturnCODBankCardInfo(long cardRecordId, long userId);
	
	/**
	 * 退货确认（客服）：站点Id -> count
	 * 
	 * @return
	 */
	public Map<Integer, Long> waitingReturnAuditCount();
	
	/**
	 * 退款确认（财务）：站点Id -> count
	 * 
	 * @return
	 */
	public Map<Integer, Long> waitingReturnCountOfCOD();
	
	/**
	 * 定时器任务：查询向JIT推送信息失败的退货订单
	 * 
	 * @param minId
	 * @param param
	 * @return
	 *     RetArg.ArrayList<_ReturnPackageDTO>
	 *     RetArg.DDBParam
	 */
	public RetArg queryJITFailedReturnPackageByMinRetPkgId(long minId, DDBParam param);
	
	/**
	 * 定时器任务：查询非到付、网易宝应退钱给用户的退货订单
	 * 
	 * @param minId
	 * @param stateArray
	 * @param param
	 * @return
	 *     RetArg.ArrayList<_ReturnPackageDTO>
	 *     RetArg.DDBParam
	 */
	public RetArg queryReturnPackageShouldReturnCashByMinRetPkgId(long minId, DDBParam param);
	
	/**
	 * 定时器任务：找出订单发货时间超出detainTime、且仓库没有收到退货的退货记录Id
	 * 
	 * @return userId -> returnPackageId List
	 */
	public Map<Long, List<Long>> getReturnPackageShouldBeCanceled(long detainTime);
	
	/**
	 * 定时器任务：找出退款成功、10元的优惠券补贴未发的退货记录
	 * 
	 * @param ddbParam
	 * @return
	 *     RetArg.ArrayList<_ReturnPackageDTO>
	 *     RetArg.DDBParam
	 */
	public RetArg getReturnedButNotDistributedReturnPackage(long minId, DDBParam ddbParam);
	
}
