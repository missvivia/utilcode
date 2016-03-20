package com.xyl.mmall.order.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.order.api.util.ReturnPriceCalculator;
import com.xyl.mmall.order.api.util.ReturnPriceCalculator._ReturnPackagePriceParam;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dao.HBRecycleLogDao;
import com.xyl.mmall.order.dao.ReturnCODBankCardInfoDao;
import com.xyl.mmall.order.dao.ReturnPackageDao;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.ReturnFormDTO;
import com.xyl.mmall.order.dto.ReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.JITPushState;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.order.enums.ReturnOrderSkuConfirmState;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.order.meta.ReturnCODBankCardInfo;
import com.xyl.mmall.order.meta.ReturnOrderSku;
import com.xyl.mmall.order.meta.ReturnPackage;
import com.xyl.mmall.order.param.KFParam;
import com.xyl.mmall.order.param.PassReturnOperationParam;
import com.xyl.mmall.order.param.ReturnConfirmParam;
import com.xyl.mmall.order.param.ReturnOperationParam;
import com.xyl.mmall.order.param.ReturnPackageApplyParam;
import com.xyl.mmall.order.param.ReturnPackageExpInfoParam;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.ReturnFormService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.order.service.ReturnPackageUpdateService;
import com.xyl.mmall.order.util.OrderInstantiationUtil;
import com.xyl.mmall.order.util.ReturnEntiyFactory;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:25:03
 *
 */
@Service("returnPackageUpdateService")
public class ReturnPackageUpdateServiceImpl extends ReturnPacakgeService implements ReturnPackageUpdateService {
	
	private static final Logger logger = Logger.getLogger(ReturnPackageUpdateServiceImpl.class);
	
	@Autowired
	protected ReturnPackageDao retPkgDao;
	
	@Autowired
	protected ReturnCODBankCardInfoDao retCODBankCardInfoDao;
	
	@Autowired
	protected OrderPackageSimpleService ordPkgSimpleService;
	
	@Autowired
	protected ReturnFormService retFormService;
	
	@Autowired
	protected ReturnPackageQueryService retPkgQueryService;
	
//	@Autowired
//	protected TradeInternalProxyService tradeInternalService;
	
	@Autowired
	private OrderInstantiationUtil orderInstantiationUtil;
	
	@Autowired
	private HBRecycleLogDao hbRecycleLogDao;

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#applyReturn(long, long, com.xyl.mmall.order.param.ReturnPackageApplyParam)
	 */
	@Override
	@Transaction
	public RetArg applyReturn(long userId, long ordPkgId, ReturnPackageApplyParam applyParam) {
		RetArg retArg = new RetArg();
		OrderPackageSimpleDTO ordPkgDTO = ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
		if(null == ordPkgDTO) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no OrderPackage for [userId:" + userId + ", ordPkgId:" + ordPkgId + "]");
			return retArg;
		}
		if(null == applyParam || !applyParam.checkSelf()) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "illegal applyParam");
			return retArg;
		}
		// 0. 获取锁记录 + 退货记录校验
		RetArg lockArg = orderInstantiationUtil.isContinueForNormalServiceByPackageId(ordPkgId, userId);
		Boolean isContinue = RetArgUtil.get(lockArg, Boolean.class);
		if(Boolean.TRUE != isContinue) {
			logger.warn("orderInstantiationUtil.isContinueForNormalServiceByPackageId fail, ordPkgId=" + ordPkgId + " ,userId=" + userId);
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "orderInstantiationUtil.isContinueForNormalServiceByPackageId fail, ordPkgId=" + ordPkgId + " ,userId=" + userId);
			return retArg;
		}
		List<ReturnPackageDTO> checkRetPkgDTOList = retPkgQueryService.queryReturnPackageByOrderPackageId(userId, ordPkgId, false, null);
		if(!CollectionUtil.isEmptyOfList(checkRetPkgDTOList)) {
			logger.warn("重复提交退货申请, ordPkgId=" + ordPkgId + " ,userId=" + userId);
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "重复提交退货申请, ordPkgId=" + ordPkgId + " ,userId=" + userId);
			return retArg;
		}
		// 1. 创建ReturnPackage + List<_ReturnOrderSku>
		// 1.1 COD退款银行卡信息
		long bankCardInfoId = 0;
		RefundType refundType = applyParam.getRefundType();
		if(RefundType.BANKCARD == refundType) {
			ReturnCODBankCardInfo bankCard = ReturnEntiyFactory.createCODRetBankCard(userId, applyParam.getRetBankCard());
			bankCard = retCODBankCardInfoDao.addObject(bankCard);
			if(null == bankCard) {
				throw new ServiceException("retCODBankCardInfoDao.addObject(bankCard) failed. [userId:" + userId + ", ordPkgId:" + ordPkgId + "]");
			}
			bankCardInfoId = bankCard.getId();
		}
		// 1.2 创建ReturnPackage
		ReturnPackage retPkg = ReturnEntiyFactory.createApplyReturnPackage(ordPkgDTO, applyParam.getRetOrderSkuParamList(), refundType, bankCardInfoId);
		if(null == retPkg || null == (retPkg = retPkgDao.addObject(retPkg))) {
			throw new ServiceException("create ReturnPackage failed. [userId:" + userId + ", ordPkgId:" + ordPkgId + "]");
		}
		// 1.3 创建List<_ReturnOrderSku>
		long retPkgId = retPkg.getRetPkgId();
		List<ReturnOrderSku> retOrdSkuList = ReturnEntiyFactory.createApplyRetOrdSku(retPkgId, ordPkgDTO, applyParam.getRetOrderSkuParamList());
		if(CollectionUtil.isEmptyOfList(retOrdSkuList)) {
			throw new ServiceException("create List<_ReturnOrderSku> failed. [userId:" + userId + ", retPkgId:" + retPkgId + "]");
		}
		for(ReturnOrderSku retOrdSku : retOrdSkuList) {
			if(null == retOrdSku) {
				continue;
			}
			if(null == returnOrderSkuDao.addObject(retOrdSku)) {
				throw new ServiceException("add _ReturnOrderSku failed. [userId:" + userId + ", retPkgId:" + retPkgId + 
						", orderSkuId:" + retOrdSku.getOrderSkuId() + "]");
			}
		}
		// 2.1 创建ReturnForm
		long orderId = ordPkgDTO.getOrderId();
		ReturnFormDTO retFormDTO = retFormService.createInstanceForOrder(userId, orderId, applyParam.isCouponUsedInOrder());
		if(null == retFormDTO) {
			throw new ServiceException("create ReturnForm failed. [userId:" + userId + ", orderId:" + orderId + "]");
		}
		// 2.2 updateApplyedNumState
		RetArg updateArg = retFormService.updateApplyedNumState(retFormDTO);
		Boolean updateResult = RetArgUtil.get(updateArg, Boolean.class);
		if(null == updateResult || Boolean.FALSE == updateResult) {
			throw new ServiceException(RetArgUtil.get(updateArg, String.class));
		}
		// 3. 更新OrderPackage状态 
		if(!ordPkgSimpleService.setPackageToRPApply(ordPkgId, userId)) {
			throw new ServiceException("_OrderPackageSimpleService.setPackageToRPApply(...) failed. [userId:" + userId + ", ordPkgId:" + ordPkgId + "]");
		}
		// 4. finally, return the result
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		if(null == retPkgDTO) {
			throw new ServiceException("_ReturnPackageUpdateServiceImpl.applyReturn(...) failed. [userId:" + userId + ", ordPkgId:" + ordPkgId + "]");
		}
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, retPkgDTO);
		RetArgUtil.put(retArg, "successful");
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#deprecateApply(long, long)
	 */
	@Override
	@Transaction
	public boolean deprecateApply(long userId, long ordPkgId) {
		List<ReturnPackage> retPkgList = retPkgDao.queryObjectsByOrderPackageIdAndUserId(ordPkgId, userId, false, null);
		if(CollectionUtil.isEmptyOfList(retPkgList)) {
			return false;
		}
		if(1 != retPkgList.size()) {
			logger.error("more than one _ReturnPackage for [ordPkgId:" + ordPkgId + ", userId:" + userId + "]");
		}
		ReturnPackage retPkg = retPkgList.get(0);
		if(null == retPkg) {
			return false;
		}
		retPkg = retPkgDao.getLockByKey(retPkg);
		boolean isSucc = retPkgDao.deprecateRecord(retPkg);
		if(!isSucc) {
			throw new ServiceException("retPkgDao.deprecateRecord(retPkg) failed. [userId:" + userId + ", retPkgId:" + retPkg.getRetPkgId() + "]");
		}
		RetArg updateArg = null;
		long orderId = retPkg.getOrderId();
		updateArg = retFormService.updateApplyedNumState(userId, orderId);
		Boolean updateResult = RetArgUtil.get(updateArg, Boolean.class);
		if(null == updateResult || Boolean.FALSE == updateResult) {
			throw new ServiceException(RetArgUtil.get(updateArg, String.class));
		}
		if(!ordPkgSimpleService.setPackageToCancelRPApply(ordPkgId, userId)) {
			throw new ServiceException("_OrderPackageSimpleService.setPackageToCancelRPApply(...) failed. [userId:" + userId + ", ordPkgId:" + ordPkgId + "]");
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#deprecateApplyByKf(com.xyl.mmall.order.dto.ReturnPackageDTO, long, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public RetArg deprecateApplyByKf(ReturnPackageDTO retPkg, long earliestPOEndTime, KFParam kf) {
		RetArg retArg = new RetArg();
		long lastMoment = System.currentTimeMillis() + ONE_DAY;
		if(lastMoment > earliestPOEndTime) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "PO最早结束时间剩余不到一天");
			return retArg;
		} 
		if(null == retPkg) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null _ReturnPackageDTO");
			return retArg;
		}
		long retPkgId = retPkg.getRetPkgId();
		long userId = retPkg.getUserId();
		long ordPkgId = retPkg.getOrderPkgId();
		long orderId = retPkg.getOrderId();
		ReturnPackage lockedRetPkg = retPkgDao.getLockByKey(retPkg);
		boolean isSucc = retPkgDao.deprecateRecordByKf(lockedRetPkg, kf);
		if(!isSucc) {
			throw new ServiceException("retPkgDao.deprecateRecordByKf(retPkg, kf) failed. [userId:" + userId + ", retPkgId:" + retPkgId + "]");
		}
		RetArg updateArg = null;
		updateArg = retFormService.updateApplyedNumState(userId, orderId);
		Boolean updateResult = RetArgUtil.get(updateArg, Boolean.class);
		if(null == updateResult || Boolean.FALSE == updateResult) {
			throw new ServiceException(RetArgUtil.get(updateArg, String.class));
		}
		if(!ordPkgSimpleService.setPackageToCancelRPApply(ordPkgId, userId)) {
			throw new ServiceException("_OrderPackageSimpleService.setPackageToCancelRPApply(...) failed. [userId:" + userId + ", ordPkgId:" + ordPkgId + "]");
		}
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, "successful");
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#updateReturnExpInfo(long, long, com.xyl.mmall.order.param.ReturnPackageExpInfoParam)
	 */
	@Override
	@Transaction
	public RetArg updateReturnExpInfo(long userId, long retPkgId, ReturnPackageExpInfoParam param) {
		RetArg retArg = new RetArg();
		if(null == param) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null param");
			return retArg;
		}
		ReturnPackage retPkg = retPkgDao.getObjectByIdAndUserId(retPkgId, userId);
		if(null == retPkg) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no ReturnPackage for [userId:" + userId + ", retPkgId:" + retPkgId + "]");
			return retArg;
		}
		// 锁住记录
		retPkg = retPkgDao.getLockByKey(retPkg);
		if(!retPkgDao.updateReturnPackageStateToWaitingConfirmWithExpInfo(retPkg, param)) {
			throw new ServiceException("更新退货仓库地址信息、快递信息、退货状态失败 [userId:" + userId + ", retPkgId:" + retPkgId + "]");
		}
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, retPkgDTO);
		RetArgUtil.put(retArg, "successful");
		return retArg;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#confirmReturnedOrderSku(long, long, java.util.Map)
	 */
	@Override
	@Transaction
	public RetArg confirmReturnedOrderSku(long retPkgId, long userId, Map<Long, ReturnConfirmParam> receivedRetOrdSku) {
		try {
			return confirmReturnedOrderSkuExec(retPkgId, userId, receivedRetOrdSku);
		} catch (Exception e) {
			RetArg retArg = new RetArg();
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, e.getMessage());
			return retArg;
		}
	}
	
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
	@Transaction
	private RetArg confirmReturnedOrderSkuExec(long retPkgId, long userId, Map<Long, ReturnConfirmParam> receivedRetOrdSku) {
		RetArg retArg = new RetArg();
		if(null == receivedRetOrdSku) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null receivedRetOrdSku");
			return retArg;
		}
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		Map<Long, ReturnOrderSkuDTO> retOrdSkuMap = null;
		if(null == retPkgDTO || CollectionUtil.isEmptyOfMap(retOrdSkuMap = retPkgDTO.getRetOrdSkuMap())) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null _ReturnPackageDTO or empty _ReturnPackageDTO.retOrdSkuMap for [userId:" + userId + ", retPkgId:" + retPkgId + "]");
			return retArg;
		}
		OrderPackageSimpleDTO ordPkgDTO = retPkgDTO.getOrdPkgDTO();
		if(null == ordPkgDTO || CollectionUtil.isEmptyOfMap(ordPkgDTO.getOrderSkuMap())) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null _OrderPackageSimpleDTO or empty _OrderPackageSimpleDTO.orderSkuMap for [userId:" + userId + ", retPkgId:" + retPkgId + "]");
			return retArg;
		}
		OrderFormBriefDTO ordFormDTO = retPkgDTO.getOrdFormBriefDTO();
		if(null == ordFormDTO) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null OrderFormBriefDTO for [userId:" + userId + ", retPkgId:" + retPkgId + "]");
			return retArg;
		}
		
		boolean abnormal = false;
		for(Entry<Long, ReturnOrderSkuDTO> entry : retOrdSkuMap.entrySet()) {
			ReturnOrderSkuDTO retOrdSkuDTO = null;
			if(null == entry || null == (retOrdSkuDTO = entry.getValue())) {
				continue;
			}
			long ordSkuId = retOrdSkuDTO.getOrderSkuId();
			ReturnConfirmParam param = receivedRetOrdSku.get(ordSkuId);
			if(null == param) {
				abnormal = true;
				continue;
			}
			if(retOrdSkuDTO.getApplyedReturnCount() != param.getConfirmCount()) {
				abnormal = true;
			}
			// 获得记录锁
			ReturnOrderSku retOrdSku = returnOrderSkuDao.getLockByKey(retOrdSkuDTO);
			if(!returnOrderSkuDao.confirmReturnOrderSku(retOrdSku, param)) {
				throw new ServiceException("仓库更新退货Sku记录失败 [retPkgId:" + retPkgId + ", orderSkuId:" + ordSkuId + "]");
			}
		}
		boolean isCOD = (OrderFormPayMethod.COD == ordFormDTO.getOrderFormPayMethod());
		_ReturnPackagePriceParam retPkgPriceParam = ReturnPriceCalculator.compute(ordPkgDTO, receivedRetOrdSku);
		// 获得记录锁
		ReturnPackage retPkg = retPkgDao.getLockByKey(retPkgDTO);
		if(!retPkgDao.updateReturnPackageStateToWaitingAuditWithConfirmTime(retPkg, abnormal, isCOD, retPkgPriceParam)) {
			throw new ServiceException("更新退货状态失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
		}
		RetArg updateArg = retFormService.updateConfirmedNumState(userId, retPkg.getOrderId());
		Boolean updateResult = RetArgUtil.get(updateArg, Boolean.class);
		if(null == updateResult || Boolean.FALSE == updateResult) {
			throw new ServiceException(RetArgUtil.get(updateArg, String.class));
		}
		// 如果是非到付退款，且非异常件，尝试直接退款
		String appendInfo = null;
		if(!isCOD && !abnormal) {
			try {
				retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
				if(finishReturnForNotCOD(retPkgDTO, null)) {
					appendInfo = "cash return successful too";
				} else {
					appendInfo = "but cash return failed";
				}
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
				appendInfo = e.getMessage();
			}
		}
		retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, retPkgDTO);
		if(null == appendInfo) {
			RetArgUtil.put(retArg, "successful");
		} else {
			RetArgUtil.put(retArg, "successful -> " + appendInfo);
		}
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#refuseReturn(com.xyl.mmall.order.param.ReturnOperationParam, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public RetArg refuseReturn(ReturnOperationParam param, KFParam kf) {
		RetArg retArg = new RetArg();
		if(null == param || null == kf) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null param or kf");
			return retArg;
		}
		long retPkgId = param.getRetId();
		long userId = param.getUserId();
		ReturnPackage retPkg = retPkgDao.getObjectByIdAndUserId(retPkgId, userId);
		if(null == retPkg) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no ReturnPackage for [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		// 获得记录锁
		retPkg = retPkgDao.getLockByKey(retPkg);
		if(!retPkgDao.updateReturnPackageStateToRefusedWithParam(retPkg, param, kf)) {
			throw new ServiceException("拒绝退货失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
		}
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, retPkgDTO);
		RetArgUtil.put(retArg, "successful");
		return retArg;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#cancelRefuse(com.xyl.mmall.order.param.ReturnOperationParam, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public RetArg cancelRefuse(ReturnOperationParam param, KFParam kf) {
		RetArg retArg = new RetArg();
		if(null == param || null == kf) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null param or kf");
			return retArg;
		}
		long retPkgId = param.getRetId();
		long userId = param.getUserId();
		ReturnPackage retPkg = retPkgDao.getObjectByIdAndUserId(retPkgId, userId);
		if(null == retPkg) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no ReturnPackage for [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		// 获得记录锁
		retPkg = retPkgDao.getLockByKey(retPkg);
		if(!retPkgDao.cancelRefuse(retPkg, param, kf)) {
			throw new ServiceException("撤销拒绝退货失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
		}
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, retPkgDTO);
		RetArgUtil.put(retArg, "successful");
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#passReturn(com.xyl.mmall.order.param.PassReturnOperationParam, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public RetArg passReturn(PassReturnOperationParam param, KFParam kf) {
		RetArg retArg = new RetArg();
		if(null == param || null == kf) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null param or kf");
			return retArg;
		}
		long retPkgId = param.getRetId();
		long userId = param.getUserId();
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		BigDecimal applyedReturnPrice = null;
		if(null == retPkgDTO || null == (applyedReturnPrice = retPkgDTO.getApplyedReturnCashPrice())) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no ReturnPackage or null retPkgDTO.retOrdSkuMap for [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		if(applyedReturnPrice.compareTo(param.getCashPriceToUser()) < 0) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "退货金额异常 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		OrderFormBriefDTO ordFormDTO = retPkgDTO.getOrdFormBriefDTO();
		if(null == ordFormDTO) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null OrderFormBriefDTO for [userId:" + userId + ", retPkgId:" + retPkgId + "]");
			return retArg;
		}
		boolean isCOD = (OrderFormPayMethod.COD == ordFormDTO.getOrderFormPayMethod());
		// to be continued: 分配_ReturnPackage和_ReturnOrderSku中的实际退款金额
		// 获得记录锁
		ReturnPackage retPkg = retPkgDao.getLockByKey(retPkgDTO);
		if(!retPkgDao.updateReturnPackageStateToAuditPassedWithParam(retPkg, param, kf)) {
			throw new ServiceException("客服通过退货审核操作失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
		}
		// 非到付异常件，客服通过后直接退款
		if(!isCOD) {
			retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
			if(!finishReturnForNotCOD(retPkgDTO, kf)) {
				throw new ServiceException("退款操作 finishReturnExec(...) failed for [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			}
		}
		retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, retPkgDTO);
		RetArgUtil.put(retArg, "successful");
		return retArg;
	}

	/**
	 * 
	 * @param retPkgDTO
	 * @param isCOD
	 * @param kf
	 * @return
	 */
	@Transaction
	private boolean finishReturnExec(ReturnPackageDTO retPkgDTO, boolean isCOD, KFParam kf) {
		long retPkgId = retPkgDTO.getRetPkgId();
		long userId = retPkgDTO.getUserId();
		ReturnPackage retPkg = retPkgDao.getLockByKey(retPkgDTO);
		if(!retPkgDao.updateReturnPackageStateToCashReturned(retPkg, kf, isCOD)) {
			throw new ServiceException("退款操作（Package）失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
		}
		ReturnOrderSkuConfirmState[] stateArray = new ReturnOrderSkuConfirmState[] {
				ReturnOrderSkuConfirmState.PART_CONFIRMED, 
				ReturnOrderSkuConfirmState.ALL_CONFIRMED
		};
		Map<Long, ReturnOrderSkuDTO> retOrdSkuDTOMap = retPkgDTO.getRetOrdSkuMap();
		for(Entry<Long, ReturnOrderSkuDTO> entry : retOrdSkuDTOMap.entrySet()) {
			ReturnOrderSkuDTO retOrdSkuDTO = null;
			if(null == entry || null == (retOrdSkuDTO = entry.getValue())) {
				continue;
			}
			if(!CollectionUtil.isInArray(stateArray, retOrdSkuDTO.getRetOrdSkuState())) {
				continue;
			}
			// 获得记录锁
			ReturnOrderSku retOrdSku = returnOrderSkuDao.getLockByKey(retOrdSkuDTO);
			retOrdSku.setRetOrdSkuState(ReturnOrderSkuConfirmState.RETURNED);
			if(!returnOrderSkuDao.updateObjectByKey(retOrdSku)) {
				throw new ServiceException("退款操作（Package）失败 [retPkgId:" + retPkgId + 
						", userId:" + userId + ", orderSkuId:"  + retOrdSku.getOrderSkuId() + "]");
			}
		}
		long ordPkgId = retPkgDTO.getOrderPkgId();
		if(!ordPkgSimpleService.setPackageToRPDone(ordPkgId, userId)) {
			throw new ServiceException("_OrderPackageSimpleService.setPackageToRPDone(...) failed. [userId:" + userId + ", ordPkgId:" + ordPkgId + "]");
		}
		return true;
	}
	
	private OrderCancelRType convertOrderCancelRType(RefundType rtype) {
		if(null == rtype) {
			return null;
		}
		switch(rtype) {
		case ORIGINAL_PATH:
			return OrderCancelRType.ORI;
		case WANGYIBAO:
			return OrderCancelRType.UN_ORI;
		default:
			return OrderCancelRType.ORI;
		}
	}
	
	/**
	 * 通过网易宝实际退款
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#finishReturnForNotCOD(com.xyl.mmall.order.dto.ReturnPackageDTO, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public boolean finishReturnForNotCOD(ReturnPackageDTO retPkgDTO, KFParam kf) {
		if(null == retPkgDTO || CollectionUtil.isEmptyOfMap(retPkgDTO.getRetOrdSkuMap())) {
			return false;
		}
		OrderFormBriefDTO ordForm = retPkgDTO.getOrdFormBriefDTO();
		if(null == ordForm || OrderFormPayMethod.COD == ordForm.getOrderFormPayMethod()) {
			return false;
		}
		long retPkgId = retPkgDTO.getRetPkgId();
		long userId = retPkgDTO.getUserId();
		if(!finishReturnExec(retPkgDTO, false, kf)) {
			throw new ServiceException("退款操作失败:finishReturnExec(...) failed. [retPkgId:" + retPkgId + ", userId:" + userId + "]");
		}
//		OrderCancelRType rtype = OrderCancelRType.UN_ORI;
//		if(RefundType.ORIGINAL_PATH == retPkgDTO.getRefundType()) {
//			rtype = OrderCancelRType.ORI;
//		}
//		// important: tradeInternalService.cancelTrade(...) 支持重复调用
//		long ordPkgId = retPkgDTO.getOrderPkgId();
//		long orderId = retPkgDTO.getOrderId();
//		BigDecimal cash = retPkgDTO.getPayedCashPriceToUser();
//		if(!tradeInternalService.setOnlineTradeToRefundWithTransaction(ordPkgId, orderId, userId, cash, rtype)) {
//			throw new ServiceException("退款操作（TradeInternalService）失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
//		}
		BigDecimal cash = retPkgDTO.getPayedCashPriceToUser();
		BigDecimal hb = retPkgDTO.getPayedHbPriceToUser();
		OrderCancelRType rtype = convertOrderCancelRType(retPkgDTO.getRefundType());
		if(!ordPkgSimpleService.addOrderPackageRefundTask(retPkgDTO.getOrdPkgDTO(), cash, hb, rtype)) {
			throw new ServiceException("退款操作 ordPkgSimpleService.addOrderPackageRefundTask(...) 失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
		}
		/**
		 * ordPkgSimpleService.addOrderPackageRefundTask(...)已经添加了红包退款定时器，因此不需要下面的代码了
		 *
		if(null != hb && hb.compareTo(BigDecimal.ZERO) > 0) {
			HBRecycleLog hbRecycleLog = ReturnEntiyFactory.createHBRecycleLog(retPkgDTO, HBRecycleState.WAITING_RECYCLING);
			HBRecycleLog addedHBRecycleLog = hbRecycleLogDao.addObject(hbRecycleLog);
			if(null == addedHBRecycleLog) {
				throw new ServiceException("退款操作 hbRecycleLogDao.addObject(hbRecycleLog) 失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			}
		}
		 */
		return true;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#finishReturnForCOD(long, long, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public RetArg finishReturnForCOD(long retPkgId, long userId, KFParam kf) {
		RetArg retArg = new RetArg();
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		if(null == retPkgDTO || CollectionUtil.isEmptyOfMap(retPkgDTO.getRetOrdSkuMap())) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no ReturnPackage or null retPkgDTO.retOrdSkuMap for [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		OrderFormBriefDTO ordFormDTO = retPkgDTO.getOrdFormBriefDTO();
		if(null == ordFormDTO || OrderFormPayMethod.COD != ordFormDTO.getOrderFormPayMethod()) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null OrderFormBriefDTO or not COD Return for [userId:" + userId + ", retPkgId:" + retPkgId + "]");
			return retArg;
		}
		// ugly code: start
		if(null != kf) {
			StringBuilder sb = new StringBuilder();
			sb.append(retPkgDTO.getKfName()).append(";").append(kf.getKfName());
			kf.setKfName(sb.toString());
		}
		// ugly code: end
		if(!finishReturnExec(retPkgDTO, true, kf)) {
			throw new ServiceException("退款操作 finishReturnExec(...) failed for [retPkgId:" + retPkgId + ", userId:" + userId + "]");
		}
		BigDecimal cash = BigDecimal.ZERO;
		BigDecimal hb = retPkgDTO.getPayedHbPriceToUser();
		if(null != hb && hb.compareTo(BigDecimal.ZERO) > 0) {
			OrderCancelRType rtype = convertOrderCancelRType(retPkgDTO.getRefundType());
			if(!ordPkgSimpleService.addOrderPackageRefundTask(retPkgDTO.getOrdPkgDTO(), cash, hb, rtype)) {
				throw new ServiceException("退款操作 ordPkgSimpleService.addOrderPackageRefundTask(...) 失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			}
			/**
			 * ordPkgSimpleService.addOrderPackageRefundTask(...)已经添加了红包退款定时器，因此不需要下面的代码了
			 *
			HBRecycleLog hbRecycleLog = ReturnEntiyFactory.createHBRecycleLog(retPkgDTO, HBRecycleState.WAITING_RECYCLING);
			HBRecycleLog addedHBRecycleLog = hbRecycleLogDao.addObject(hbRecycleLog);
			if(null == addedHBRecycleLog) {
				throw new ServiceException("退款操作 hbRecycleLogDao.addObject(hbRecycleLog) 失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			}
			 */
		}
		retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, retPkgDTO);
		RetArgUtil.put(retArg, "successful");
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#setReturnStateToCanceled(long, long)
	 */
	@Override
	@Transaction
	public RetArg setReturnStateToCanceled(long retPkgId, long userId) {
		RetArg retArg = new RetArg();
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		if(null == retPkgDTO) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no ReturnPackage or null retPkgDTO.retOrdSkuMap for [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		// 获得记录锁
		ReturnPackage retPkg = retPkgDao.getLockByKey(retPkgDTO);
		ReturnPackageState[] stateArray = ReturnPackageState.values();
		retPkg.setReturnState(ReturnPackageState.CANCELLED);
		// http://jira6.hz.netease.com/browse/MMALL-2324
		if (!retPkgDao.updateReturnPackageState(retPkg, ReturnPackageState.CANCELLED, stateArray,
				ConstValueOfOrder.SEP_RP_APPLY_DAY + "天未收到包裹，自动取消退货申请")) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "取消退货失败 [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, retPkgDTO);
		RetArgUtil.put(retArg, "successful");
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#updateJITPushToSuccessful(com.xyl.mmall.order.dto.ReturnPackageDTO)
	 */
	@Override
	@Transaction
	public RetArg updateJITPushToSuccessful(ReturnPackageDTO retPkg) {
		RetArg retArg = new RetArg();
		if(null == retPkg) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null retPkg");
			return retArg;
		}
		JITPushState newState = JITPushState.PUSH_SUCCESSFUL;
		JITPushState[] stateArray = new JITPushState[] {JITPushState.WAITING_PUSH};
		// 获得记录锁
		if(!retPkgDao.updateJITPushState(retPkg, newState, stateArray)) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "retPkgDao.updateJITPushStat(...) to " + newState + " failed with old state " + retPkg.getJitPushState());
			return retArg;
		}
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, "successful");
		return retArg;
	
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageUpdateService#distributeReturnExpHb(com.xyl.mmall.order.dto.ReturnPackageDTO)
	 */
	@Override
	public RetArg distributeReturnExpHb(ReturnPackageDTO retPkg) {
		RetArg retArg = new RetArg();
		if(null == retPkg) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null retPkg");
			return retArg;
		}
		if(!retPkgDao.distributeHb(retPkg)) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "retPkgDao.distributeHb(retPkg) failed for [retPkgId:" + retPkg.getRetPkgId() + ", userId:" + retPkg.getUserId() + "]");
			return retArg;
		}
		retPkg = retPkgQueryService.queryReturnPackageByRetPkgId(retPkg.getUserId(), retPkg.getRetPkgId());
		RetArgUtil.put(retArg, Boolean.TRUE);
		RetArgUtil.put(retArg, retPkg);
		RetArgUtil.put(retArg, "successful");
		return retArg;
	}

}
