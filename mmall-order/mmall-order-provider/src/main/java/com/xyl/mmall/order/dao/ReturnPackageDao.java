package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.api.util.ReturnPriceCalculator._ReturnPackagePriceParam;
import com.xyl.mmall.order.enums.JITPushState;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.order.meta.ReturnPackage;
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
public interface ReturnPackageDao extends AbstractDao<ReturnPackage> {

	/**
	 * 通过订单id和用户id获取退货包裹记录
	 * 
	 * @param orderId
	 * @param userId
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryObjectsByOrderIdAndUserId(long orderId, long userId, boolean deprecated, DDBParam param);
	
	/**
	 * 通过订单id集和、用户id获取退货包裹记录
	 * 
	 * @param orderIdColl
	 * @param userId
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryObjectsByOrderIdAndUserId(Collection<Long> orderIdColl, long userId, boolean deprecated, DDBParam param);
	
	/**
	 * 通过包裹id和用户id获取退货包裹记录
	 * 
	 * @param orderPackageId
	 * @param userId
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryObjectsByOrderPackageIdAndUserId(long orderPackageId, long userId, boolean deprecated, DDBParam param);
	
	/**
	 * 查询指定状态的退货包裹记录
	 * 
	 * @param stateArray
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryObjectsByState(ReturnPackageState[] stateArray, boolean deprecated, DDBParam param);
	
	/**
	 * 查询指定用户指定状态的退货包裹记录
	 * 
	 * @param stateArray
	 * @param userId
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryObjectsByStateWithUserId(ReturnPackageState[] stateArray, long userId, boolean deprecated, DDBParam param);
	
	/**
	 * 通过订单id和用户id获取指定状态的退货包裹记录
	 * 
	 * @param orderId
	 * @param userId
	 * @param deprecated
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryObjectsByStateWithOrderIdAndUserId(long orderId, long userId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param);
	
	/**
	 * 查询指定订单指定状态的退货包裹记录
	 * 
	 * @param stateArray
	 * @param orderId
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryObjectsByStateWithOrderId(ReturnPackageState[] stateArray, long orderId, boolean deprecated, DDBParam param);
	
	/**
	 * 查询指定订单指定状态的退货包裹记录
	 * 
	 * @param stateArray
	 * @param ordPkgId
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryObjectsByStateWithOrderPackageId(ReturnPackageState[] stateArray, long ordPkgId, boolean deprecated, DDBParam param);
	
	/**
	 * 查询指定时间指定状态的退货包裹记录
	 * 
	 * @param stateArray
	 * @param start
	 * @param end
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryObjectsByStateWithTimeRange(ReturnPackageState[] stateArray, long start, long end, 
			boolean deprecated, DDBParam param);
	
	/**
	 * 查询指定物流指定状态的退货包裹记录
	 * 
	 * @param stateArray
	 * @param mailNO
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryObjectsByStateWithMailNO(ReturnPackageState[] stateArray, String mailNO, 
			boolean deprecated, DDBParam param);
	
	/**
	 * 更新退货状态
	 * 
	 * @param retPkg
	 * @param newState
	 * @param stateArray
	 * @param extInfo
	 * @return
	 */
	public boolean updateReturnPackageState(ReturnPackage retPkg, ReturnPackageState newState, 
			ReturnPackageState[] stateArray, String extInfo);
	
	/**
	 * 添加地址信息，同时更新退货状态为“待收货”
	 * 
	 * @param retPkg
	 * @param param
	 * @return
	 */
	public boolean updateReturnPackageStateToWaitingConfirmWithExpInfo(ReturnPackage retPkg, ReturnPackageExpInfoParam param);
	
	/**
	 * 更新退货状态为“待退款（审核）”
	 * 
	 * @param retPkg
	 * @param abnomal
	 * @param isCOD
	 * @param param
	 * @return
	 */
	public boolean updateReturnPackageStateToWaitingAuditWithConfirmTime(ReturnPackage retPkg, boolean abnormal, 
			boolean isCOD, _ReturnPackagePriceParam param);

	/**
	 * 更新退货状态为“拒绝”
	 * 
	 * @param retPkg
	 * @param param
	 * @param kf
	 * @return
	 */
	public boolean updateReturnPackageStateToRefusedWithParam(ReturnPackage retPkg, ReturnOperationParam param, KFParam kf);
	
	/**
	 * 撤销拒绝操作
	 * 
	 * @param retPkg
	 * @param param
	 * @param kf
	 * @return
	 */
	public boolean cancelRefuse(ReturnPackage retPkg, ReturnOperationParam param, KFParam kf);
	
	/**
	 * 更新退货状态为“通过”
	 * 
	 * @param retPkg
	 * @param param
	 * @param kf
	 * @return
	 */
	public boolean updateReturnPackageStateToAuditPassedWithParam(ReturnPackage retPkg, PassReturnOperationParam param, KFParam kf);
	
	/**
	 * 更新COD退货状态为“已退款”
	 * 
	 * @param retPkg
	 * @param kf
	 * @param isCOD
	 * @return
	 */
	public boolean updateReturnPackageStateToCashReturned(ReturnPackage retPkg, KFParam kf, boolean isCOD);
	
	/**
	 * 找到不同省份下面的退货待确认数目
	 * 
	 * @return
	 */
	public Map<Integer, Long> getWaitingReturnAuditCount();
	
	/**
	 * 找到财务退款待确认数目
	 * 
	 * @return
	 */
	public Map<Integer, Long> getWaitingReturnCountOfCOD();
	
	/**
	 * 查询向JIT推送信息失败的退货包裹
	 * 
	 * @param minRetPkgId
	 * @param deprecated
	 * @param param
	 * @return
	 */
	public List<ReturnPackage> queryWaitingJITPushObjectsByStateWithMinRetPkgId(long minRetPkgId, boolean deprecated, DDBParam param);

	/**
	 * 找出订单发货时间超出detainTime、且仓库没有收到退货的退货包裹记录Id
	 * 
	 * @param detainTime
	 * @return userId -> returnFormId List
	 */
	public Map<Long, List<Long>> getObjectsShouldBeCanceled(long detainTime);

	/**
	 * 找出非到付、网易宝应退钱给用户的退货订单
	 * 
	 * @param minRetPkgId
	 * @param ddbParam
	 * @return
	 */
	public List<ReturnPackage> getReturnPackageShouldReturnCashByMinRetPkgId(long minRetPkgId, DDBParam ddbParam);
	
	/**
	 * 找出退款成功、10元的红包补贴未发的退货包裹记录
	 * 
	 * @param minRetPkgId
	 * @param ddbParam
	 * @return
	 */
	public List<ReturnPackage> getReturnedButNotDistributedObjects(long minRetPkgId, DDBParam ddbParam);
	
	/**
	 * 更新状态：退款成功、10元的红包补贴未发的退货包裹记录
	 * 
	 * @param retPkg
	 * @return
	 */
	public boolean distributeHb(ReturnPackage retPkg);
	
	/**
	 * 将记录标记为无效
	 * 
	 * @param retPkg
	 * @return
	 */
	public boolean deprecateRecord(ReturnPackage retPkg);
	
	/**
	 * 将记录标记为无效（异常件拒绝退货后，支持客服取消退货申请）
	 * 
	 * @param retPkg
	 * @param kf
	 * @return
	 */
	public boolean deprecateRecordByKf(ReturnPackage retPkg, KFParam kf);
	
	/**
	 * 更新JIT推送状态
	 * 
	 * @param retPkg
	 * @param newState
	 * @param oldStates
	 * @return
	 */
	public boolean updateJITPushState(ReturnPackage retPkg, JITPushState newState, JITPushState[] oldStates);
}