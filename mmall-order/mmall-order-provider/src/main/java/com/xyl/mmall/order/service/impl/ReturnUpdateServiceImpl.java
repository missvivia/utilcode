package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.order.dto.DeprecatedReturnFormDTO;
import com.xyl.mmall.order.dto.DeprecatedReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.DeprecatedReturnCouponHbRecycleState;
import com.xyl.mmall.order.enums.DeprecatedReturnOrderSkuState;
import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.order.meta.DeprecatedReturnCODBankCardInfo;
import com.xyl.mmall.order.meta.DeprecatedReturnForm;
import com.xyl.mmall.order.meta.DeprecatedReturnOrderSku;
import com.xyl.mmall.order.param.DeprecatedReturnFormApplyParam;
import com.xyl.mmall.order.param.KFParam;
import com.xyl.mmall.order.param.PassReturnOperationParam;
import com.xyl.mmall.order.param.ReturnConfirmParam;
import com.xyl.mmall.order.param.ReturnOperationParam;
import com.xyl.mmall.order.param.ReturnPackageExpInfoParam;
import com.xyl.mmall.order.service.ReturnQueryService;
import com.xyl.mmall.order.service.ReturnUpdateService;
import com.xyl.mmall.order.util.DeprecatedReturnEntiyFactory;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:25:03
 *
 */
@Deprecated
@Service("returnUpdateService")
public class ReturnUpdateServiceImpl extends ReturnService implements ReturnUpdateService {
	
	@Autowired
	protected ReturnQueryService retQueryService;

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#applyReturn(long, long, java.lang.Boolean, com.xyl.mmall.order.param.DeprecatedReturnFormApplyParam)
	 */
	@Override
	@Transaction
	public DeprecatedReturnFormDTO applyReturn(long userId, long orderId, Boolean isVisible, 
			DeprecatedReturnFormApplyParam applyParam) {
		DeprecatedReturnForm retForm = DeprecatedReturnEntiyFactory.createApplyReturnForm(userId, orderId, applyParam);
		DeprecatedReturnForm addedRetForm = returnFormDao.addObject(retForm);
		if(null == addedRetForm) {
			throw new ServiceException("添加退货记录失败. [userId:" + userId + ", orderId:" + orderId + "]");
		}
		long retId = addedRetForm.getId();
		DeprecatedReturnFormDTO retFormDTO = new DeprecatedReturnFormDTO(addedRetForm);
		if(RefundType.BANKCARD == retForm.getRefundType()) {
			DeprecatedReturnCODBankCardInfo retCODBankCardInfo = 
					DeprecatedReturnEntiyFactory.createCODRetBankCard(orderId, userId, retId, applyParam.getPriceParam().getRetBankCard());
			if(null == codRetBankCardDao.addObject(retCODBankCardInfo)) {
				throw new ServiceException("添加退货银行卡信息失败. [userId:" + userId + ", orderId:" + orderId + "]");
			}
		}
// dto中的字段 -- start
		/** 1. 退货关联的Order */
		OrderFormDTO orderFormDTO = orderService.queryOrderForm(userId, orderId, isVisible);
		if(null == orderFormDTO) {
			throw new ServiceException("读取订单记录失败. [userId:" + userId + ", orderId:" + orderId + "]");
		}
		retFormDTO.setOrderFormDTO(orderFormDTO);
//------------------------------
		/** 2. 订单中退回的OrderSku */
		List<DeprecatedReturnOrderSku> retOrdSkuList = DeprecatedReturnEntiyFactory.createReturnOrderSkuList(
				orderId, userId, retId, applyParam.getRetOrderSkuParamList());
		List<DeprecatedReturnOrderSku> addedRetOrdSkuList = new ArrayList<DeprecatedReturnOrderSku>(retOrdSkuList.size());
		for(DeprecatedReturnOrderSku retOrdSku : retOrdSkuList) {
			/** ReturnOrderSku入库 */
			DeprecatedReturnOrderSku addedRetOrdSku = returnOrderSkuDao.addObject(retOrdSku);
			if(null == (addedRetOrdSku)) {
				throw new ServiceException("添加退货OrderSku记录失败. [userId:" + userId + ", orderId:" + orderId + 
						", returnId:" + retId + ", orderSkuId:" + retOrdSku.getOrderSkuId() + "]");
			}
			addedRetOrdSkuList.add(addedRetOrdSku);
		}
		Map<Long, OrderSkuDTO> allOrderSku = orderFormDTO.mapOrderSkusByTheirId();
		List<DeprecatedReturnOrderSkuDTO> retOrdSkuDTOList = convertToRetOrdSkuDTOList(addedRetOrdSkuList, allOrderSku);
		retFormDTO.setRetOrderSkuList(retOrdSkuDTOList);
// dto中的字段 -- end		
		return retFormDTO;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#deprecateApply(long, long)
	 */
	@Override
	@Transaction
	public boolean deprecateApply(long userId, long orderId) {
		DeprecatedReturnState[] stateArray = new DeprecatedReturnState[] { DeprecatedReturnState.APPLY_RETURN };
		List<DeprecatedReturnForm> retFormList = returnFormDao.queryObjectByOrderIdAndUserIdWithStates(orderId, userId, stateArray, null);
		if(null == retFormList || 0 == retFormList.size()) {
			throw new ServiceException("查找退货记录失败 [userId:" + userId + ", orderId:" + orderId + "]");
		}
		if(1 != retFormList.size()) {
			throw new ServiceException("业务逻辑错误：多条退货记录 [userId:" + userId + ", orderId:" + orderId + "]");
		}
		DeprecatedReturnForm retForm = returnFormDao.getLockByKey(retFormList.get(0));
		long retId = retForm.getId();
		if(!returnOrderSkuDao.deprecateRetOrdSku(retId, userId)) {
			throw new ServiceException("取消退货商品记录失败 [returnId:" + retId + ", userId:" + userId + "]");
		}
		if(!returnFormDao.deleteObjectByKey(retForm)) {
			throw new ServiceException("取消退货记录失败 [returnId:" + retId + "]");
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#updateReturnExpInfo(long, com.xyl.mmall.order.param.ReturnPackageExpInfoParam)
	 */
	@Override
	@Transaction
	public DeprecatedReturnFormDTO updateReturnExpInfo(long retId, ReturnPackageExpInfoParam param) {
		DeprecatedReturnForm retLock = returnFormDao.getObjectById(retId);
		if(null == retLock) {
			return null;
		}
		// 锁住记录
		retLock = returnFormDao.getLockByKey(retLock);
		if(!returnFormDao.setReturnStateToWaitingConfirmWithExpInfo(retLock, param)) {
			throw new ServiceException("填写退货地址快递信息、更新退货状态失败 [returnId:" + retId + "]");
		}
		DeprecatedReturnFormDTO ret = convertReturnFormMetaToDTO(retLock);
		return ret;
	}
	
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#confirmReturnedOrderSku(long, long, java.util.Map)
	 */
	@Override
	@Transaction
	public boolean confirmReturnedOrderSku(long retId, long userId, Map<Long, ReturnConfirmParam> receivedRetOrdSku) {
		DeprecatedReturnFormDTO retFormDTO = retQueryService.queryReturnFormByUserIdAndReturnId(userId, retId);
		if(null == retFormDTO) {
			throw new ServiceException("查找退货记录失败 [returnId:" + retId + ", userId:" + userId + "]");
		}
		boolean abnormal = false;
		List<DeprecatedReturnOrderSkuDTO> retOrdSkuList = retFormDTO.getRetOrderSkuList();
		for(DeprecatedReturnOrderSkuDTO retOrdSkuDTO : retOrdSkuList) {
			long ordSkuId = retOrdSkuDTO.getOrderSkuId();
			if(!receivedRetOrdSku.containsKey(ordSkuId)) {
				abnormal = true;
				continue;
			}
			ReturnConfirmParam param = receivedRetOrdSku.get(ordSkuId);
			if(retOrdSkuDTO.getReturnCount() != param.getConfirmCount()) {
				abnormal = true;
			}
			// 获得记录锁
			DeprecatedReturnOrderSku retOrdSku = returnOrderSkuDao.getLockByKey(retOrdSkuDTO);
			if(!returnOrderSkuDao.confirmReturnOrderSku(retOrdSku, param)) {
				throw new ServiceException("仓库添加退货Sku记录失败 [returnId:" + retId + ", orderSkuId:" + ordSkuId + "]");
			}
		}
		// 获得记录锁
		DeprecatedReturnForm retForm = returnFormDao.getLockByKey(retFormDTO);
		if(!returnFormDao.setReturnStateToWaitingReturnAuditWithConfirmTime(retForm, abnormal)) {
			throw new ServiceException("更新退货状态失败 [returnId:" + retForm.getId() + "]");
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#finishReturn(com.xyl.mmall.order.param.ReturnOperationParam, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public boolean finishReturn(PassReturnOperationParam param, KFParam kf) {
		if(null == param || null == kf) {
			return false;
		}
		long retId = param.getRetId();
		long userId = param.getUserId();
		DeprecatedReturnForm retForm = returnFormDao.getObjectByIdAndUserId(retId, userId);
		if(null == retForm) {
			return false;
		}
		List<DeprecatedReturnOrderSku> retOrdSkuList = returnOrderSkuDao.queryRetOrdSkuListByReturnId(retId);
		if(null == retOrdSkuList || 0 == retOrdSkuList.size()) {
			return false;
		}
		// 获得记录锁
		retForm = returnFormDao.getLockByKey(retForm);
		if(!returnFormDao.setReturnStateToReturnedWithParam(retForm, param, kf)) {
			throw new ServiceException("退货失败 [returnId:" + retForm.getId() + "]");
		}
		DeprecatedReturnOrderSkuState[] stateArray = new DeprecatedReturnOrderSkuState[] {
				DeprecatedReturnOrderSkuState.PART_CONFIRMED, 
				DeprecatedReturnOrderSkuState.ALL_CONFIRMED
		};
		for(DeprecatedReturnOrderSku retOrdSku : retOrdSkuList) {
			if(!CollectionUtil.isInArray(stateArray, retOrdSku.getRetOrdSkuState())) {
				continue;
			}
			// 获得记录锁
			retOrdSku = returnOrderSkuDao.getLockByKey(retOrdSku);
			retOrdSku.setRetOrdSkuState(DeprecatedReturnOrderSkuState.RETURNED);
			if(!returnOrderSkuDao.updateObjectByKey(retOrdSku)) {
				throw new ServiceException("退货失败 [orderId:" + retOrdSku.getOrderId()
						+ ", orderSkuId:"  + retOrdSku.getOrderSkuId() + "]");
			}
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#refuseReturn(com.xyl.mmall.order.param.ReturnOperationParam, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public boolean refuseReturn(ReturnOperationParam param, KFParam kf) {
		if(null == param || null == kf) {
			return false;
		}
		long retId = param.getRetId();
		long userId = param.getUserId();
		DeprecatedReturnForm retForm = returnFormDao.getObjectByIdAndUserId(retId, userId);
		if(null == retForm) {
			return false;
		}
		// 获得记录锁
		retForm = returnFormDao.getLockByKey(retForm);
		if(!returnFormDao.setReturnStateToRefusedWithParam(retForm, param, kf)) {
			throw new ServiceException("拒绝退货失败 [returnId:" + retForm.getId() + "]");
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#cancelRefuse(com.xyl.mmall.order.param.ReturnOperationParam, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public boolean cancelRefuse(ReturnOperationParam param, KFParam kf) {
		if(null == param || null == kf) {
			return false;
		}
		long retId = param.getRetId();
		long userId = param.getUserId();
		DeprecatedReturnForm retForm = returnFormDao.getObjectByIdAndUserId(retId, userId);
		if(null == retForm) {
			return false;
		}
		// 获得记录锁
		if(!returnFormDao.cancelRefuse(retForm, param, kf)){
			throw new ServiceException("撤销拒绝退货失败 [returnId:" + retForm.getId() + "]");
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#setReturnStateToCanceled(long, long)
	 */
	@Override
	@Transaction
	public boolean setReturnStateToCanceled(long retId, long userId) {
		DeprecatedReturnForm retForm = returnFormDao.getObjectByIdAndUserId(retId, userId);
		if(null == retForm) {
			return false;
		}
		// 获得记录锁
		retForm = returnFormDao.getLockByKey(retForm);
		DeprecatedReturnState[] stateArray = DeprecatedReturnState.values();
		retForm.setReturnState(DeprecatedReturnState.CANCELED);
		if(!returnFormDao.setReturnState(retForm, stateArray)){
			throw new ServiceException("取消退货失败 [returnId:" + retForm.getId() + "]");
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#setReturnCouponHbRecycleStateToReturned(long, long)
	 */
	@Override
	@Transaction
	public boolean setReturnCouponHbRecycleStateToReturned(long retId, long userId) {
		DeprecatedReturnForm retForm = returnFormDao.getObjectByIdAndUserId(retId, userId);
		if(null == retForm) {
			return false;
		}
		// 获得记录锁
		retForm = returnFormDao.getLockByKey(retForm);
		DeprecatedReturnCouponHbRecycleState[] stateArray = new DeprecatedReturnCouponHbRecycleState[] {DeprecatedReturnCouponHbRecycleState.WAITING_RETURN};
		retForm.setCouponHbRecycleState(DeprecatedReturnCouponHbRecycleState.RETURNED);
		if(!returnFormDao.setReturnCouponHbRecycleState(retForm, stateArray)){
			throw new ServiceException("更新退货优惠券+红包回收状态失败 [returnId:" + retForm.getId() + "]");
		}
		return true;
	
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#setJITSuccStatus(long, long, boolean)
	 */
	@Override
	@Transaction
	public boolean setJITSuccStatus(long retId, long userId, boolean jitSucc) {
		DeprecatedReturnForm retForm = returnFormDao.getObjectByIdAndUserId(retId, userId);
		if(null == retForm) {
			return false;
		}
		retForm = returnFormDao.getLockByKey(retForm);
		retForm.setJitSucc(jitSucc);
		return returnFormDao.updateObjectByKey(retForm);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnUpdateService#setUseCouponStateToDistributed(com.xyl.mmall.order.dto.DeprecatedReturnFormDTO)
	 */
	@Override
	public boolean setUseCouponStateToDistributed(DeprecatedReturnFormDTO retForm) {
		if(null == retForm) {
			return false;
		}
		return returnFormDao.distributeCoupon(retForm);
	}
	
}
