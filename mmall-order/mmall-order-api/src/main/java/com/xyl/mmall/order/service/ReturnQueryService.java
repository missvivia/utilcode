package com.xyl.mmall.order.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.DeprecatedReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.DeprecatedReturnFormDTO;
import com.xyl.mmall.order.enums.DeprecatedReturnState;

/**
 * 退货服务
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:23:48
 *
 */
@Deprecated
public interface ReturnQueryService {
	
	/**
	 * 根据退货id
	 * 
	 * @param retId
	 * @param userId
	 * @return
	 */
	public DeprecatedReturnFormDTO queryReturnFormByReturnId(long retId);
	
	/**
	 * 根据退货id、订单id查询退货信息
	 * 
	 * @param retId
	 * @param userId
	 * @return
	 */
	public DeprecatedReturnFormDTO queryReturnFormByUserIdAndReturnId(long userId, long retId);
	
	/**
	 * 根据用户id、订单id查询退货信息
	 * 
	 * @param userId
	 * @param orderId
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnFormDTO> queryReturnFormByUserIdAndOrderId(long userId, long orderId, DDBParam param);
	
	/**
	 * 根据用户id、订单id查询退货信息
	 * 
	 * @param userId
	 * @param orderIdColl
	 * @param param
	 * @return Map.Key: OrderId<br>
	 *         Map.Value: List(ReturnFormDTO)
	 */
	public Map<Long, List<DeprecatedReturnFormDTO>> queryReturnFormByUserIdAndOrderId(long userId, Collection<Long> orderIdColl,
			DDBParam param);
	
	/**
	 * 根据用户id、订单id、退货状态查询退货信息
	 * 
	 * @param userId
	 * @param orderId
	 * @param orderId
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnFormDTO> queryReturnFormByUserIdAndOrderId(long userId, long orderId, DeprecatedReturnState[] stateArray, 
			DDBParam param);
	
	/**
	 * 查询某个用户某些状态的退货订单
	 * 
	 * @param userId
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnFormDTO> queryReturnFormByUserId(long userId, DeprecatedReturnState[] stateArray,
			DDBParam param);
	
	/**
	 * 查询某个订单某些状态的退货订单
	 * 
	 * @param orderId
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnFormDTO> queryReturnFormByOrderId(long orderId, DeprecatedReturnState[] stateArray,
			DDBParam param);
	
	/**
	 * 查询某个订单某些状态的退货订单
	 * 
	 * @param orderId
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public RetArg queryReturnFormByOrderId2(long orderId, DeprecatedReturnState[] stateArray,
			DDBParam param);
	
	/**
	 * 查询向JIT推送信息失败的退货订单
	 * 
	 * @param minRetId
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public RetArg queryJITFailedReturnFormByMinReturnId(long minRetId, DeprecatedReturnState[] stateArray,
			DDBParam param);
	
	/**
	 * 查询需要回收优惠券+红包、且尚未回收的退货订单
	 * 
	 * @param minRetId
	 * @param param
	 * @return
	 */
	public RetArg queryWaitingRecycleReturnFormByMinReturnId(long minRetId, DDBParam param);
	
	/**
	 * 根据时间范围、退货状态查询退货信息
	 * 
	 * @param start
	 * @param end
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnFormDTO> queryReturnFormByTimeRange(long start, long end, DeprecatedReturnState[] stateArray, 
			DDBParam param);
	
	/**
	 * 根据时间范围、退货状态查询退货信息
	 * 
	 * @param start
	 * @param end
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public RetArg queryReturnFormByTimeRange2(long start, long end, DeprecatedReturnState[] stateArray, 
			DDBParam param);
	
	/**
	 * 根据快递单号、退货状态查询退货信息
	 * 
	 * @param mailNO
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnFormDTO> queryReturnFormByMailNO(String mailNO, DeprecatedReturnState[] stateArray, 
			DDBParam param);
	
	/**
	 * 根据快递单号、退货状态查询退货信息
	 * 
	 * @param mailNO
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public RetArg queryReturnFormByMailNO2(String mailNO, DeprecatedReturnState[] stateArray, 
			DDBParam param);
	
	/**
	 * 货到付款：退货银行卡信息
	 * 
	 * @param retId
	 * @param userId
	 * @return
	 */
	public DeprecatedReturnCODBankCardInfoDTO queryReturnCODBankCardInfo(long retId, long userId);
	
	/**
	 * 退货确认（客服）：站点Id -> count
	 * 
	 * @return
	 */
	public Map<Integer, Long> waitingReturnAuditCount();
	
	/**
	 * 找出订单发货时间超出detainTime、且仓库没有收到退货的退货记录Id
	 * 
	 * @return userId -> returnFormID List
	 */
	public Map<Long, List<Long>> getReturnFormListShouldBeCanceled(long detainTime);
	
	/**
	 * 找出退款成功、10元的优惠券补贴未发的退货记录
	 * 
	 * @param ddbParam
	 * @return
	 */
	public RetArg getReturnedButNotDistributedReturnFormList(long minRetId, DDBParam ddbParam);
	
//	/**
//	 * 按照组合条件查找“退货信息”
//	 * 
//	 * @param rcqParam
//	 * @param ddbParam
//	 * @param skuId：大于0的时候有意义
//	 * @param stateArray
//	 * @return
//	 */
//	public List<ReturnFormDTO> queryReturnWithCombinedParam(ReturnFormQueryParam rcqParam, DDBParam ddbParam, 
//			long skuId, ReturnOrderSkuState[] stateArray);
//	
//	/**
//	 * 没有收到ReturnForm相关的ReturnOrderSku
//	 * 
//	 * @param retId
//	 * @return
//	 */
//	public boolean isNoRetOrdSkuConfirmed(long retId);
//	
//	/**
//	 * 部分收到ReturnForm相关的ReturnOrderSku
//	 * 
//	 * @param retId
//	 * @return
//	 */
//	public boolean isPartRetOrdSkuConfirmed(long retId);
//	
//	/**
//	 * 全部收到ReturnForm相关的ReturnOrderSku
//	 * 
//	 * @param retId
//	 * @return
//	 */
//	public boolean isAllRetOrdSkuConfirmed(long retId);
}
