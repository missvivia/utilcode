package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.order.dao.ReturnFormDao;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.ReturnFormDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.enums.ReturnCouponRecycleState;
import com.xyl.mmall.order.enums.ReturnOrderSkuNumState;
import com.xyl.mmall.order.meta.ReturnForm;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.ReturnFormService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.order.util.ReturnEntiyFactory;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:25:03
 *
 */
@Service("returnFormService")
public class ReturnFormServiceImpl implements ReturnFormService {
	
	@Autowired
	protected ReturnFormDao returnFormDao;
	
	@Autowired
	protected OrderPackageSimpleService orderPackageService;
	
	@Autowired
	protected OrderBriefService orderBriefService;
	
	@Autowired
	protected ReturnPackageQueryService retPkgQueryService;
	
	private ReturnFormDTO convertReturnFormMetaToDTO(ReturnForm retForm) {
		if(null == retForm) {
			return null;
		}
		ReturnFormDTO retFormDTO = new ReturnFormDTO(retForm);
//		long userId = retForm.getUserId();
//		long orderId = retForm.getOrderId();
//		OrderFormBriefDTO ordForm = orderBriefService.queryOrderFormBrief(userId, orderId, null);
//		retFormDTO.setOrderFormBriefDTO(ordForm);
//		List<_ReturnPackageDTO> retPkgDTOList = retPkgQueryService.queryReturnPackageByUserIdAndOrderId(userId, orderId, false, null);
//		retFormDTO.setRetPkgMap(convertListToMap(retPkgDTOList));
		return retFormDTO;
	}
	
	private List<ReturnFormDTO> convertReturnFormMetaListToDTOList(List<ReturnForm> retFormList) {
		List<ReturnFormDTO> retFormDTOList = new ArrayList<ReturnFormDTO>();
		if(null == retFormList) {
			return retFormDTOList;
		}
		for(ReturnForm retForm : retFormList) {
			if(null == retForm) {
				continue;
			}
			retFormDTOList.add(convertReturnFormMetaToDTO(retForm));
		}
		return retFormDTOList;
	}
	
	// key: orderPackageId, value: _ReturnPackageDTO
	private Map<Long, ReturnPackageDTO> convertListToMap(List<ReturnPackageDTO> retPkgList) {
		Map<Long, ReturnPackageDTO> ret = new HashMap<Long, ReturnPackageDTO>();
		if(null == retPkgList) {
			return ret;
		}
		for(ReturnPackageDTO retPkg : retPkgList) {
			if(null == retPkg) {
				continue;
			}
			ret.put(retPkg.getOrderPkgId(), retPkg);
		}
		return ret;
	}
	
	/**
	 * 重置_ReturnOrderSkuNumState状态
	 * 
	 * @param retForm
	 * @param applySituation
	 * @return
	 */
	private boolean resetNumState(ReturnFormDTO retForm, boolean applySituation) {
		if(null == retForm) {
			return false;
		}
		boolean isSucc = false;
		ReturnForm lockedRetForm = returnFormDao.getLockByKey(retForm);
		ReturnOrderSkuNumState[] oldStates = new ReturnOrderSkuNumState[] {lockedRetForm.getApplyedNumState()};
		isSucc = returnFormDao.updateApplyedNumState(lockedRetForm, ReturnOrderSkuNumState.APPLY_INIT, oldStates);
		if(!isSucc) {
			return false;
		} 
		oldStates = new ReturnOrderSkuNumState[] {lockedRetForm.getConfirmedNumState()};
		return returnFormDao.updateConfirmNumState(lockedRetForm, ReturnOrderSkuNumState.CONFIRM_INIT, oldStates);
	}
	
	/**
	 * 更新状态：
	 * 1. applySituation=true: 用户申请退货时，与订单退货数量关联的的状态
	 * 2. applySituation=false: 系统或客服退款操作时，与订单退货数量关联的的状态
	 * 
	 * @param userId
	 * @param orderId
	 * @param applySituation
	 * @return
	 */
	@Transaction
	private RetArg updateNumStateExec(ReturnFormDTO retForm, boolean applySituation) {
		RetArg retArg = new RetArg();
		if(ReturnCouponRecycleState.NONE == retForm.getCouponHbRecycleState()) {
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful due to no coupon used in order");
			return retArg;
		}
		long userId = retForm.getUserId(), orderId = retForm.getOrderId();
		List<ReturnPackageDTO> retPkgList = retPkgQueryService.queryReturnPackageByUserIdAndOrderId(userId, orderId, false, null);
		if(null == retPkgList || 0 == retPkgList.size()) {
			boolean isSucc = resetNumState(retForm, applySituation);
			RetArgUtil.put(retArg, isSucc ? Boolean.TRUE : Boolean.FALSE);
			RetArgUtil.put(retArg, "resetNumState(retForm, applySituation) called.");
			return retArg;
		}
		List<OrderPackageSimpleDTO> ordPkgList = orderPackageService.queryOrderPackageSimpleByOrderId(userId, orderId, OrderPackageState.values());
		if(null == ordPkgList || 0 == ordPkgList.size()) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no OrderPackage for userId:" + userId + ", orderId:" + orderId);
			return retArg;
		}
		Map<Long, ReturnPackageDTO> retPkgMap = convertListToMap(retPkgList);
		boolean lackSomePkg = false;
		boolean allFull = true;
		for(OrderPackageSimpleDTO ordPkg : ordPkgList) {
			if(null == ordPkg) {
				continue;
			}
			long ordPkgId = ordPkg.getPackageId();
			ReturnPackageDTO retPkg = retPkgMap.get(ordPkgId);
			if(null == retPkg) {
				lackSomePkg = true;
				continue;
			}
			ReturnOrderSkuNumState state = null;
			if(applySituation) {
				state = retPkg.applyedSkuNumState();
				if(null == state || state != ReturnOrderSkuNumState.APPLY_PACKAGE_FULL_RETURN) {
					allFull = false;
				}
			} else {
				state = retPkg.confirmedSkuNumState();
				if(null == state || state != ReturnOrderSkuNumState.CONFIRM_PACKAGE_FULL_RETURN) {
					allFull = false;
				}
			}
		}
		boolean isSucc = true;
		ReturnForm lockedRetForm = returnFormDao.getLockByKey(retForm);
		if(applySituation) {
			ReturnOrderSkuNumState[] oldStates = new ReturnOrderSkuNumState[] {lockedRetForm.getApplyedNumState()};
			if(lackSomePkg) {
				if(allFull) {
					isSucc = returnFormDao.updateApplyedNumState(lockedRetForm, ReturnOrderSkuNumState.APPLY_PACKAGE_FULL_RETURN, oldStates);
				} else {
					isSucc = returnFormDao.updateApplyedNumState(lockedRetForm, ReturnOrderSkuNumState.APPLY_PACKAGE_PART_RETURN, oldStates);
				}
			} else {
				if(allFull){
					isSucc = returnFormDao.updateApplyedNumState(lockedRetForm, ReturnOrderSkuNumState.APPLY_ORDER_FULL_RETURN, oldStates);
				} else {
					isSucc = returnFormDao.updateApplyedNumState(lockedRetForm, ReturnOrderSkuNumState.APPLY_PACKAGE_PART_RETURN, oldStates);
				}
			}
		} else {
			ReturnOrderSkuNumState[] oldStates = new ReturnOrderSkuNumState[] {lockedRetForm.getConfirmedNumState()};
			if(lackSomePkg) {
				if(allFull) {
					isSucc = returnFormDao.updateConfirmNumState(lockedRetForm, ReturnOrderSkuNumState.CONFIRM_PACKAGE_FULL_RETURN, oldStates);
				} else {
					isSucc = returnFormDao.updateConfirmNumState(lockedRetForm, ReturnOrderSkuNumState.CONFIRM_PACKAGE_PART_RETURN, oldStates);
				}
			} else {
				if(allFull) {
					isSucc = returnFormDao.updateConfirmNumState(lockedRetForm, ReturnOrderSkuNumState.CONFIRM_ORDER_FULL_RETURN, oldStates);
				} else {
					isSucc = returnFormDao.updateConfirmNumState(lockedRetForm, ReturnOrderSkuNumState.CONFIRM_PACKAGE_PART_RETURN, oldStates);
				}
			}
		}
		if(isSucc) {
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful");
		} else {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "failed returnFormDao.updateApplyedNumState(...)/returnFormDao.updateConfirmedNumState(...)");
		}
		return retArg;
	
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnFormService#createInstanceForOrder(long, long, boolean)
	 */
	@Override
	public ReturnFormDTO createInstanceForOrder(long userId, long orderId, boolean couponUsedInOrder) {
		ReturnFormDTO retFormDTO = queryReturnFormByUserIdAndOrderId(userId, orderId);
		if(null != retFormDTO) {
			return retFormDTO;
		}
		ReturnForm retForm = returnFormDao.addObject(ReturnEntiyFactory.createEmptyReturnForm(userId, orderId, couponUsedInOrder));
		return convertReturnFormMetaToDTO(retForm);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnFormService#queryReturnFormByOrderId(long)
	 */
	@Override
	public ReturnFormDTO queryReturnFormByOrderId(long orderId) {
		ReturnForm retForm = returnFormDao.getObjectById(orderId);
		return null == retForm ? null : convertReturnFormMetaToDTO(retForm);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnFormService#queryReturnFormByUserIdAndOrderId(long, long)
	 */
	@Override
	public ReturnFormDTO queryReturnFormByUserIdAndOrderId(long userId, long orderId) {
		ReturnForm retForm = returnFormDao.getObjectByIdAndUserId(orderId, userId);
		return null == retForm ? null : convertReturnFormMetaToDTO(retForm);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnFormService#updateApplyedNumState(long, long)
	 */
	@Override
	public RetArg updateApplyedNumState(long userId, long orderId) {
		RetArg retArg = new RetArg();
		ReturnFormDTO retForm = queryReturnFormByUserIdAndOrderId(userId, orderId);
		if(null == retForm) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no ReturnForm for userId:" + userId + ", orderId:" + orderId);
			return retArg;
		}
		return updateNumStateExec(retForm, true);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnFormService#updateApplyedNumState(com.xyl.mmall.order.dto.ReturnFormDTO)
	 */
	@Override
	public RetArg updateApplyedNumState(ReturnFormDTO retForm) {
		RetArg retArg = new RetArg();
		if(null == retForm) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null ReturnForm");
			return retArg;
		}
		return updateNumStateExec(retForm, true);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnFormService#updateConfirmedNumState(long, long)
	 */
	@Override
	public RetArg updateConfirmedNumState(long userId, long orderId) {
		RetArg retArg = new RetArg();
		ReturnFormDTO retForm = queryReturnFormByUserIdAndOrderId(userId, orderId);
		if(null == retForm) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "no ReturnForm for userId:" + userId + ", orderId:" + orderId);
			return retArg;
		}
		return updateNumStateExec(retForm, false);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnFormService#updateConfirmedNumState(com.xyl.mmall.order.dto.ReturnFormDTO)
	 */
	@Override
	public RetArg updateConfirmedNumState(ReturnFormDTO retForm) {
		RetArg retArg = new RetArg();
		if(null == retForm) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null ReturnForm");
			return retArg;
		}
		return updateNumStateExec(retForm, false);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnFormService#queryReturnFormShouldRecycleCouponByMinOrderId(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryReturnFormShouldRecycleCouponByMinOrderId(long minId, DDBParam ddbParam) {
		if(null == ddbParam) {
			ddbParam = DDBParam.genParamX(100);
		}
		ddbParam.setAsc(true);
		ddbParam.setOrderColumn("orderId");
		List<ReturnForm> retFormList = returnFormDao.queryWaitingRecycleReturnFormListWithMinOrderId(minId, ddbParam);
		List<ReturnFormDTO> retFormDTOList = convertReturnFormMetaListToDTOList(retFormList);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retFormDTOList);
		RetArgUtil.put(retArg, ddbParam);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnFormService#recycleCoupon(com.xyl.mmall.order.dto.ReturnFormDTO)
	 */
	@Override
	public RetArg recycleCoupon(ReturnFormDTO retForm) {
		RetArg retArg = new RetArg();
		if(null == retForm) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null retForm");
			return retArg;
		}
		boolean isSucc = returnFormDao.updateReturnCouponHbRecycleState(retForm, 
				ReturnCouponRecycleState.RETURNED, 
				new ReturnCouponRecycleState[] {ReturnCouponRecycleState.WAITING_RECYCEL});
		if(isSucc) {
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, queryReturnFormByUserIdAndOrderId(retForm.getUserId(), retForm.getOrderId()));
			RetArgUtil.put(retArg, "successful");
		} else {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "failed returnFormDao.updateReturnCouponHbRecycleState(...)");
		}
		return retArg;
	}

}
