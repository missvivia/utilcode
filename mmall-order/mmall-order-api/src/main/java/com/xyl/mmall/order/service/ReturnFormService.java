package com.xyl.mmall.order.service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.ReturnFormDTO;

/**
 * 退货服务
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:23:48
 *
 */
public interface ReturnFormService {
	
	/**
	 * 创建实例，数据入库
	 * 
	 * @param userId
	 * @param orderId
	 * @param couponUsedInOrder
	 * @return
	 */
	public ReturnFormDTO createInstanceForOrder(long userId, long orderId, boolean couponUsedInOrder);
	
	/**
	 * 根据订单Id
	 * 
	 * @param orderId
	 * @param userId
	 * @param deprecated
	 * @return
	 */
	public ReturnFormDTO queryReturnFormByOrderId(long orderId);
	
	/**
	 * 根据订单Id、用户id查询退货信息
	 * 
	 * @param orderId
	 * @param userId
	 * @param deprecated
	 * @return
	 */
	public ReturnFormDTO queryReturnFormByUserIdAndOrderId(long userId, long orderId);
	
	/**
	 * 更新状态：用户申请退货时，与订单退货数量关联的的状态
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg updateApplyedNumState(long userId, long orderId);
	
	/**
	 * 更新状态：用户申请退货时，与订单退货数量关联的的状态
	 * 
	 * @param retForm
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg updateApplyedNumState(ReturnFormDTO retForm);
	
	/**
	 * 更新状态：系统或客服退款操作时，与订单退货数量关联的的状态
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg updateConfirmedNumState(long userId, long orderId);
	
	/**
	 * 更新状态：系统或客服退款操作时，与订单退货数量关联的的状态
	 * 
	 * @param retForm
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg updateConfirmedNumState(ReturnFormDTO retForm);
	
	/**
	 * 定时器任务：找出全部退货、且没有退回优惠券的退货订单记录
	 * 
	 * @param minId
	 * @param ddbParam
	 * @return
	 *     RetArg.ArrayList<_ReturnPackageDTO>
	 *     RetArg.DDBParam
	 */
	public RetArg queryReturnFormShouldRecycleCouponByMinOrderId(long minId, DDBParam ddbParam);
	
	/**
	 * 定时器任务：回收优惠券
	 * 
	 * @param retForm
	 * @return
	 *     RetArg._ReturnFormDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg recycleCoupon(ReturnFormDTO retForm);

}
