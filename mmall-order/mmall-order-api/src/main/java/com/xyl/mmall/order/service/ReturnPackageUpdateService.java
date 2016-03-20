package com.xyl.mmall.order.service;

import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.param.KFParam;
import com.xyl.mmall.order.param.PassReturnOperationParam;
import com.xyl.mmall.order.param.ReturnConfirmParam;
import com.xyl.mmall.order.param.ReturnOperationParam;
import com.xyl.mmall.order.param.ReturnPackageApplyParam;
import com.xyl.mmall.order.param.ReturnPackageExpInfoParam;

/**
 * 退货服务
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:23:48
 *
 */
public interface ReturnPackageUpdateService {
	
	public static final long ONE_DAY = 24 * 60 * 60 * 1000L;
	
	/**
	 * 申请退货
	 * 
	 * @param userId
	 * @param ordPkgId
	 * @param applyParam
	 * @return
	 *     RetArg.Boolean
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.String
	 */
	public RetArg applyReturn(long userId, long ordPkgId, ReturnPackageApplyParam applyParam);

	/**
	 * 取消退货申请
	 * 
	 * @param userId
	 * @param ordPkgId
	 * @return
	 */
	public boolean deprecateApply(long userId, long ordPkgId);
	
	/**
	 * 取消退货申请（异常件拒绝退货后，支持客服取消退货申请）
	 * 
	 * @param retPkg
	 * @param earliestPOEndTime
	 * @param kf
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg deprecateApplyByKf(ReturnPackageDTO retPkg, long earliestPOEndTime, KFParam kf);
	
	/**
	 * 将退货快递信息写入数据库
	 * 
	 * @param userId
	 * @param retPkgId
	 * @param param
	 * @return
	 *     RetArg.Boolean
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.String
	 */
	public RetArg updateReturnExpInfo(long userId, long retPkgId, ReturnPackageExpInfoParam param);
	
	/**
	 * 仓库确认收到退货
	 * 
	 * @param retPkgId
	 * @param userId
	 * @param receivedRetOrdSku: orderSkuId->ReturnConfirmParam
	 * @return
	 *     RetArg.Boolean
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.String
	 */
	public RetArg confirmReturnedOrderSku(long retPkgId, long userId, Map<Long, ReturnConfirmParam> receivedRetOrdSku);
	
	/**
	 * 更新退货状态：拒绝
	 * 
	 * @param param
	 * @param kf
	 * @return
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg refuseReturn(ReturnOperationParam param, KFParam kf);
	
	/**
	 * 更新退货状态：撤销拒绝
	 * 
	 * @param param
	 * @param kf
	 * @return
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg cancelRefuse(ReturnOperationParam param, KFParam kf);
	
	/**
	 * 更新退货状态：通过
	 * 
	 * 使用场景：异常件处理（到付/非到付，如果是非到付，直接退款）
	 * 
	 * @param param
	 * @param kf
	 * @return
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg passReturn(PassReturnOperationParam param, KFParam kf);
	
	/**
	 * 定时器任务/仓库确认收货
	 * 
	 * 更新退货状态：已退款(非到付退货申请)
	 * 
	 * 使用场景：正常件退款
	 * 
	 * @param retPkgDTO
	 * @param isCOD
	 * @param kf
	 * @return
	 */
	public boolean finishReturnForNotCOD(ReturnPackageDTO retPkgDTO, KFParam kf);
	
	/**
	 * 更新退货状态：已退款
	 * 
	 * 使用场景：财务退款(到付退货申请)
	 * 
	 * @param retPkgId
	 * @param userId
	 * @param kf
	 * @return
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg finishReturnForCOD(long retPkgId, long userId, KFParam kf);
	
	/**
	 * 定时器任务：设置JIT推送反馈结果
	 * 
	 * @param retPkgId
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg updateJITPushToSuccessful(ReturnPackageDTO retPkg);
	
	/**
	 * 定时器任务
	 * 更新退货状态：已取消
	 * 
	 * @param retPkgId
	 * @param userId
	 * @return
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg setReturnStateToCanceled(long retPkgId, long userId);
	
	/**
	 * 定时器任务：
	 * 更新状态：标记红包是否已发(退款成功，发10元的红包补贴)
	 * 
	 * @param retPkg
	 * @return
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg distributeReturnExpHb(ReturnPackageDTO retPkg);
	
}
