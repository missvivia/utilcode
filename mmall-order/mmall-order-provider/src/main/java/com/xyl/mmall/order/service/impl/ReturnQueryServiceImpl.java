package com.xyl.mmall.order.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.DeprecatedReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.DeprecatedReturnFormDTO;
import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.meta.DeprecatedReturnCODBankCardInfo;
import com.xyl.mmall.order.meta.DeprecatedReturnForm;
import com.xyl.mmall.order.service.ReturnQueryService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:25:03
 *
 */
@Deprecated
@Service("returnQueryService")
public class ReturnQueryServiceImpl extends ReturnService implements ReturnQueryService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByReturnId(long)
	 */
	@Override
	public DeprecatedReturnFormDTO queryReturnFormByReturnId(long retId) {
		DeprecatedReturnForm retForm = returnFormDao.getObjectById(retId);
		return (null == retForm) ? null : convertReturnFormMetaToDTO(retForm);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByUserIdAndReturnId(long, long)
	 */
	@Override
	public DeprecatedReturnFormDTO queryReturnFormByUserIdAndReturnId(long userId, long retId) {
		DeprecatedReturnForm retForm = returnFormDao.getObjectByIdAndUserId(retId, userId);
		return (null == retForm) ? null : convertReturnFormMetaToDTO(retForm);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByUserIdAndOrderId(long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnFormDTO> queryReturnFormByUserIdAndOrderId(long userId, long orderId, DDBParam param) {
		List<DeprecatedReturnForm> retFormList = returnFormDao.queryObjectByOrderIdAndUserId(orderId, userId, param);
		if(null != retFormList && retFormList.size() > 1) {
			logger.warn("业务逻辑错误：多条退货记录 [userId:" + userId + ", orderId:" + orderId + "]");
		}
		return convertToRetFormDTOList(retFormList);
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByUserIdAndOrderId(long,
	 *      java.util.Collection, com.netease.print.daojar.meta.base.DDBParam)
	 */
	public Map<Long, List<DeprecatedReturnFormDTO>> queryReturnFormByUserIdAndOrderId(long userId, Collection<Long> orderIdColl,
			DDBParam param) {
		List<DeprecatedReturnForm> retFormList = returnFormDao.queryObjectByOrderIdAndUserId(orderIdColl, userId, param);
		List<DeprecatedReturnFormDTO> retFormDTOList = convertToRetFormDTOList(retFormList);

		return CollectionUtil.convertCollToListMap(retFormDTOList, "orderId");
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByUserIdAndOrderId(long, long, com.xyl.mmall.order.enums.DeprecatedReturnState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnFormDTO> queryReturnFormByUserIdAndOrderId(long userId, long orderId, DeprecatedReturnState[] stateArray,
			DDBParam param) {
		List<DeprecatedReturnForm> retFormList = returnFormDao.queryObjectByOrderIdAndUserIdWithStates(orderId, userId, stateArray, param);
		if(null != retFormList && retFormList.size() > 1) {
			logger.warn("业务逻辑错误：多条退货记录 [userId:" + userId + ", orderId:" + orderId + "]");
		}
		return convertToRetFormDTOList(retFormList);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByUserId(long, com.xyl.mmall.order.enums.DeprecatedReturnState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnFormDTO> queryReturnFormByUserId(long userId, DeprecatedReturnState[] stateArray, DDBParam param) {
		List<DeprecatedReturnForm> retFormList = returnFormDao.queryReturnFormListByStateWithUserId(stateArray, userId, param);
		return convertToRetFormDTOList(retFormList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByOrderId(long, com.xyl.mmall.order.enums.DeprecatedReturnState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnFormDTO> queryReturnFormByOrderId(long orderId, DeprecatedReturnState[] stateArray, DDBParam param) {
		List<DeprecatedReturnForm> retFormList = returnFormDao.queryReturnFormListByStateWithOrderId(stateArray, orderId, param);
		if(null != retFormList && retFormList.size() > 1) {
			logger.warn("业务逻辑错误：多条退货记录 [orderId:" + orderId + "]");
		}
		return convertToRetFormDTOList(retFormList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByOrderId2(long, com.xyl.mmall.order.enums.DeprecatedReturnState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryReturnFormByOrderId2(long orderId, DeprecatedReturnState[] stateArray, DDBParam param) {
		List<DeprecatedReturnFormDTO> retFormList = queryReturnFormByOrderId(orderId, stateArray, param);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retFormList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryJITFailedReturnFormByMinReturnId(long, com.xyl.mmall.order.enums.DeprecatedReturnState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryJITFailedReturnFormByMinReturnId(long minRetId, DeprecatedReturnState[] stateArray, DDBParam param) {
		if(null == param) {
			param = DDBParam.genParamX(100);
		}
		param.setAsc(true);
		param.setOrderColumn("id");
		List<DeprecatedReturnForm> retFormList = returnFormDao.queryJITFailedReturnFormListByStateWithMinReturnId(stateArray, minRetId, param);
		List<DeprecatedReturnFormDTO> retFormDTOList = convertToRetFormDTOList(retFormList);
		// 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retFormDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryWaitingRecycleReturnFormByMinReturnId(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryWaitingRecycleReturnFormByMinReturnId(long minRetId, DDBParam param) {
		if(null == param) {
			param = DDBParam.genParamX(100);
		}
		param.setAsc(true);
		param.setOrderColumn("id");
		List<DeprecatedReturnForm> retFormList = returnFormDao.queryWaitingRecycleReturnFormListByStateWithMinReturnId(minRetId, param);
		List<DeprecatedReturnFormDTO> retFormDTOList = convertToRetFormDTOList(retFormList);
		// 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retFormDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByTimeRange(long, long, com.xyl.mmall.order.enums.DeprecatedReturnState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnFormDTO> queryReturnFormByTimeRange(long start, long end, DeprecatedReturnState[] stateArray,
			DDBParam param) {
		List<DeprecatedReturnForm> retFormList = returnFormDao.queryReturnFormListByStateWithTimeRange(stateArray, start, end, param);
		return convertToRetFormDTOList(retFormList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByTimeRange2(long, long, com.xyl.mmall.order.enums.DeprecatedReturnState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryReturnFormByTimeRange2(long start, long end, DeprecatedReturnState[] stateArray, DDBParam param) {
		List<DeprecatedReturnFormDTO> retFormList = queryReturnFormByTimeRange(start, end, stateArray, param);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retFormList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByMailNO(java.lang.String, com.xyl.mmall.order.enums.DeprecatedReturnState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnFormDTO> queryReturnFormByMailNO(String mailNO, DeprecatedReturnState[] stateArray, DDBParam param) {
		List<DeprecatedReturnForm> retFormList = returnFormDao.queryReturnFormListByStateWithMailNO(stateArray, mailNO, param);
		return convertToRetFormDTOList(retFormList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnFormByMailNO2(java.lang.String, com.xyl.mmall.order.enums.DeprecatedReturnState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryReturnFormByMailNO2(String mailNO, DeprecatedReturnState[] stateArray, DDBParam param) {
		List<DeprecatedReturnFormDTO> retFormList = queryReturnFormByMailNO(mailNO, stateArray, param);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retFormList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnCODBankCardInfo(long, long)
	 */
	@Override
	public DeprecatedReturnCODBankCardInfoDTO queryReturnCODBankCardInfo(long retId, long userId) {
		DeprecatedReturnCODBankCardInfo card = codRetBankCardDao.getObjectByIdAndUserId(retId, userId);
		return (null == card) ? null : new DeprecatedReturnCODBankCardInfoDTO(card);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#waitingReturnAuditCount()
	 */
	@Override
	public Map<Integer, Long> waitingReturnAuditCount() {
		return returnFormDao.getWaitingReturnAuditCount();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#getReturnFormListShouldBeCanceled(long)
	 */
	@Override
	public Map<Long, List<Long>> getReturnFormListShouldBeCanceled(long detainTime) {
		return returnFormDao.getReturnFormListShouldBeCanceled(detainTime);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#getReturnedButNotDistributedReturnFormIdList(com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg getReturnedButNotDistributedReturnFormList(long minRetId, DDBParam param) {
		if(null == param) {
			param = DDBParam.genParamX(100);
		}
		param.setAsc(true);
		param.setOrderColumn("id");
		List<DeprecatedReturnForm> retFormList = returnFormDao.getReturnedButNotDistributedReturnFormList(minRetId, param);
		List<DeprecatedReturnFormDTO> retFormDTOList = convertToRetFormDTOList(retFormList);
		// 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retFormDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

//	/**
//	 * 
//	 * (non-Javadoc)
//	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnWithCombinedParam(com.xyl.mmall.order.param.ReturnFormQueryParam, com.netease.print.daojar.meta.base.DDBParam, long, com.xyl.mmall.order.enums.ReturnOrderSkuState[])
//	 */
//	@Override
//	public List<ReturnFormDTO> queryReturnWithCombinedParam(ReturnFormQueryParam rcqParam, DDBParam ddbParam, 
//			long skuId, ReturnOrderSkuState[] stateArray) {
//		List<ReturnForm> retFormList = returnFormDao.queryReturnFormListByCombinedParam(rcqParam, ddbParam);
//		if(null == retFormList || 0 == retFormList.size()) {
//			return null;
//		}
//		List<ReturnForm> filteredRetFormList = new ArrayList<ReturnForm>();
//		for(ReturnForm retForm : retFormList) {
//			List<ReturnOrderSku> retOrdSkuList = 
//					returnOrderSkuDao.queryRetOrdSkuListByReturnIdWithState(retForm.getId(), stateArray);
//			if(null == retOrdSkuList || 0 == retOrdSkuList.size()) {
//				continue;
//			}
//			if(skuId <= 0) {
//				filteredRetFormList.add(retForm);
//				continue;
//			}
//			for(ReturnOrderSku retOrdSku : retOrdSkuList) {
//				if(skuId == retOrdSku.getSkuId()) {
//					filteredRetFormList.add(retForm);
//					break;
//				}
//			}
//		}
//		return convertToRetFormDTOList(filteredRetFormList);
//	}
//
//	// 判断retOrdSkuList中所有的ReturnOrderSku是否为state状态
//	private boolean allInSpicialState(List<ReturnOrderSku> retOrdSkuList, ReturnOrderSkuState state) {
//		if(null == state || null == retOrdSkuList || 0 == retOrdSkuList.size()) {
//			return false;
//		}
//		for(ReturnOrderSku retOrdSku : retOrdSkuList) {
//			if(retOrdSku.getRetOrdSkuState() != state) {
//				return false;
//			}
//		}
//		return true;
//	}
//	
//	/**
//	 * 
//	 * (non-Javadoc)
//	 * @see com.xyl.mmall.order.service.ReturnQueryService#isNoRetOrdSkuConfirmed(long)
//	 */
//	@Override
//	public boolean isNoRetOrdSkuConfirmed(long retId) {
//		ReturnOrderSkuState[] states = new ReturnOrderSkuState[] {
//				ReturnOrderSkuState.NOT_CONFIRMED, 
//				ReturnOrderSkuState.PART_CONFIRMED, 
//				ReturnOrderSkuState.ALL_CONFIRMED
//		};
//		List<ReturnOrderSku> retOrdSkuList = 
//			returnOrderSkuDao.queryRetOrdSkuListByReturnIdWithState(retId, states);
//		return allInSpicialState(retOrdSkuList, ReturnOrderSkuState.NOT_CONFIRMED);
//	}
//
//	/**
//	 * 
//	 * (non-Javadoc)
//	 * @see com.xyl.mmall.order.service.ReturnQueryService#isPartRetOrdSkuConfirmed(long)
//	 */
//	@Override
//	public boolean isPartRetOrdSkuConfirmed(long retId) {
//		ReturnOrderSkuState[] states = new ReturnOrderSkuState[] {
//				ReturnOrderSkuState.NOT_CONFIRMED, 
//				ReturnOrderSkuState.PART_CONFIRMED, 
//				ReturnOrderSkuState.ALL_CONFIRMED
//		};
//		List<ReturnOrderSku> retOrdSkuList = 
//			returnOrderSkuDao.queryRetOrdSkuListByReturnIdWithState(retId, states);
//		return (!allInSpicialState(retOrdSkuList, ReturnOrderSkuState.NOT_CONFIRMED)) && 
//			   (!allInSpicialState(retOrdSkuList, ReturnOrderSkuState.ALL_CONFIRMED));
//	}
//
//	/**
//	 * 
//	 * (non-Javadoc)
//	 * @see com.xyl.mmall.order.service.ReturnQueryService#isAllRetOrdSkuConfirmed(long)
//	 */
//	@Override
//	public boolean isAllRetOrdSkuConfirmed(long retId) {
//		ReturnOrderSkuState[] states = new ReturnOrderSkuState[] {
//				ReturnOrderSkuState.NOT_CONFIRMED, 
//				ReturnOrderSkuState.PART_CONFIRMED, 
//				ReturnOrderSkuState.ALL_CONFIRMED
//		};
//		List<ReturnOrderSku> retOrdSkuList = 
//			returnOrderSkuDao.queryRetOrdSkuListByReturnIdWithState(retId, states);
//		return allInSpicialState(retOrdSkuList, ReturnOrderSkuState.ALL_CONFIRMED);
//	}
	
}
