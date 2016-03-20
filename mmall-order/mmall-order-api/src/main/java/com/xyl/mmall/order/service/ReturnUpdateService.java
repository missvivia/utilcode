package com.xyl.mmall.order.service;

import java.util.Map;

import com.xyl.mmall.order.dto.DeprecatedReturnFormDTO;
import com.xyl.mmall.order.param.DeprecatedReturnFormApplyParam;
import com.xyl.mmall.order.param.KFParam;
import com.xyl.mmall.order.param.PassReturnOperationParam;
import com.xyl.mmall.order.param.ReturnConfirmParam;
import com.xyl.mmall.order.param.ReturnOperationParam;
import com.xyl.mmall.order.param.ReturnPackageExpInfoParam;

/**
 * 退货服务
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:23:48
 *
 */
@Deprecated
public interface ReturnUpdateService {

	/**
	 * 申请退货
	 * 
	 * @param userId
	 * @param orderId
	 * @param isVisible
	 * @param applyParam
	 * @return
	 */
	public DeprecatedReturnFormDTO applyReturn(long userId, long orderId, Boolean isVisible, 
			DeprecatedReturnFormApplyParam applyParam);

	/**
	 * 取消退货申请
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public boolean deprecateApply(long userId, long orderId);
	
	/**
	 * 将退货快递信息写入数据库
	 * 
	 * @param retId
	 * @param param
	 * @return
	 */
	public DeprecatedReturnFormDTO updateReturnExpInfo(long retId, ReturnPackageExpInfoParam param);
	
	/**
	 * 确认仓库收到退货
	 * 
	 * @param retId
	 * @param userId
	 * @param receivedRetOrdSku: orderSkuId->ReturnConfirmParam
	 * @return
	 */
	public boolean confirmReturnedOrderSku(long retId, long userId, Map<Long, ReturnConfirmParam> receivedRetOrdSku);
	
	/**
	 * 更新退货状态为“完成”
	 * 
	 * @param param
	 * @param kf
	 * @return
	 */
	public boolean finishReturn(PassReturnOperationParam param, KFParam kf);
	
	/**
	 * 更新退货状态：拒绝退货
	 * 
	 * @param param
	 * @param kf
	 * @return
	 */
	public boolean refuseReturn(ReturnOperationParam param, KFParam kf);
	
	/**
	 * 撤销拒绝退货
	 * 
	 * @param param
	 * @param kf
	 * @return
	 */
	public boolean cancelRefuse(ReturnOperationParam param, KFParam kf);
	
	/**
	 * 更新退货状态：已取消
	 * 
	 * @param retId
	 * @param userId
	 * @return
	 */
	public boolean setReturnStateToCanceled(long retId, long userId);
	
	/**
	 * 更新优惠券+红包回收状态：已回收
	 * 
	 * @param retId
	 * @param userId
	 * @return
	 */
	public boolean setReturnCouponHbRecycleStateToReturned(long retId, long userId);
	
	/**
	 * 设置JIT推送反馈结果
	 * 
	 * @param retId
	 * @param userId
	 * @param jitSucc
	 * @return
	 */
	public boolean setJITSuccStatus(long retId, long userId, boolean jitSucc);
	
	/**
	 * 退款成功，发10元的优惠券补贴
	 * 
	 * @param retForm
	 * @return
	 */
	public boolean setUseCouponStateToDistributed(DeprecatedReturnFormDTO retForm);
}
