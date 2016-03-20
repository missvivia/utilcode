package com.xyl.mmall.order.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dao.ReturnPackageDao;
import com.xyl.mmall.order.dto.ReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.order.meta.ReturnCODBankCardInfo;
import com.xyl.mmall.order.meta.ReturnPackage;
import com.xyl.mmall.order.service.ReturnPackageQueryService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:25:03
 *
 */
@Service("returnPackageQueryService")
public class ReturnPackageQueryServiceImpl extends ReturnPacakgeService implements ReturnPackageQueryService {
	
	@Autowired
	protected ReturnPackageDao retPkgDao;
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByRetPkgId(long)
	 */
	@Override
	public ReturnPackageDTO queryReturnPackageByRetPkgId(long retPkgId) {
		ReturnPackage retPkg = retPkgDao.getObjectById(retPkgId);
		return (null == retPkg) ? null : convertReturnPackageMetaToDTO(retPkg);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnQueryService#queryReturnPackageByUserIdAndReturnId(long, long)
	 */
	@Override
	public ReturnPackageDTO queryReturnPackageByRetPkgId(long userId, long retPkgId) {
		ReturnPackage retPkg = retPkgDao.getObjectByIdAndUserId(retPkgId, userId);
		return (null == retPkg) ? null : convertReturnPackageMetaToDTO(retPkg);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByUserIdWithState(long, boolean, com.xyl.mmall.order.enums.ReturnPackageState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackageDTO> queryReturnPackageByUserIdWithState(long userId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param) {
		List<ReturnPackage> retPkgList = retPkgDao.queryObjectsByStateWithUserId(stateArray, userId, deprecated, param);
		return convertReturnPackageMetaListToDTOList(retPkgList);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByOrderPackageId(long, long, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackageDTO> queryReturnPackageByOrderPackageId(long userId, long orderPackageId, boolean deprecated, DDBParam param) {
		List<ReturnPackage> retPkgList = retPkgDao.queryObjectsByOrderPackageIdAndUserId(orderPackageId, userId, deprecated, param);
		return convertReturnPackageMetaListToDTOList(retPkgList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByOrderPackageIdWithState(long, boolean, com.xyl.mmall.order.enums.ReturnPackageState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackageDTO> queryReturnPackageByOrderPackageIdWithState(long orderPackageId, boolean deprecated, ReturnPackageState[] stateArray, DDBParam param) {
		List<ReturnPackage> retPkgList = retPkgDao.queryObjectsByStateWithOrderPackageId(stateArray, orderPackageId, deprecated, param);
		return convertReturnPackageMetaListToDTOList(retPkgList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByOrderPackageIdWithState2(long, boolean, com.xyl.mmall.order.enums.ReturnPackageState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryReturnPackageByOrderPackageIdWithState2(long orderPackageId, boolean deprecated, ReturnPackageState[] stateArray, DDBParam param) {
		RetArg retArg = new RetArg();
		List<ReturnPackageDTO> retPkgDTOList = queryReturnPackageByOrderPackageIdWithState(orderPackageId, deprecated, stateArray, param);
		RetArgUtil.put(retArg, retPkgDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByUserIdAndOrderId(long, long, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackageDTO> queryReturnPackageByUserIdAndOrderId(long userId, long orderId, boolean deprecated, DDBParam param) {
		List<ReturnPackage> retPkgList = retPkgDao.queryObjectsByOrderIdAndUserId(orderId, userId, deprecated, param);
		return convertReturnPackageMetaListToDTOList(retPkgList);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#querySuccessfullyReturnedPackageByUserIdAndOrderId(long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackageDTO> querySuccessfullyReturnedPackageByUserIdAndOrderId(long userId, long orderId, DDBParam param) {
		ReturnPackageState[] stateArray = new ReturnPackageState[] {
				ReturnPackageState.FINALLY_RETURNED_TO_USER, ReturnPackageState.FINALLY_COD_RETURNED_TO_USER
		};
		return queryReturnPackageByUserIdAndOrderIdWithState(userId, orderId, false, stateArray, param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryWaitingReturningPackageByUserIdAndOrderId(long, long)
	 */
	@Override
	public int queryWaitingReturningPackageByUserIdAndOrderId(long userId, long orderId) {
		DDBParam param = new DDBParam("retPkgId", true, 1, 0);
		ReturnPackageState[] stateArray = new ReturnPackageState[] {
				ReturnPackageState.WAITING_CONFIRM, 
				ReturnPackageState.ABNORMAL_WAITING_AUDIT, ReturnPackageState.FINISH_RETURN, 
				ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT, ReturnPackageState.CW_WAITING_RETURN
				
		};
		queryReturnPackageByUserIdAndOrderIdWithState(userId, orderId, false, stateArray, param);
		return null == param ? -1 : param.getTotalCount();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByUserIdAndOrderIdWithState(long, long, boolean, com.xyl.mmall.order.enums.ReturnPackageState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackageDTO> queryReturnPackageByUserIdAndOrderIdWithState(long userId, long orderId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param) {
		List<ReturnPackage> retPkgList = retPkgDao.queryObjectsByStateWithOrderIdAndUserId(orderId, userId, deprecated, stateArray, param);
		return convertReturnPackageMetaListToDTOList(retPkgList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByOrderIdWithState(long, boolean, com.xyl.mmall.order.enums.ReturnPackageState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackageDTO> queryReturnPackageByOrderIdWithState(long orderId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param) {
		List<ReturnPackage> retPkgList = retPkgDao.queryObjectsByStateWithOrderId(stateArray, orderId, deprecated, param);
		return convertReturnPackageMetaListToDTOList(retPkgList);
	}

	/**
	 * 查询某个订单某些状态的退货订单
	 * 
	 * @param orderId
	 * @param deprecated 
	 * @param stateArray
	 * @param param
	 * @return
	 *     RetArg.ArrayList<ReturnPackageDTO>
	 *     RetArg.DDBParam
	 */
	public RetArg queryReturnPackageByOrderIdWithState2(long orderId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param) {
		RetArg retArg = new RetArg();
		List<ReturnPackageDTO> retPkgDTOList = queryReturnPackageByOrderIdWithState(orderId, deprecated, stateArray, param);
		RetArgUtil.put(retArg, retPkgDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryJITFailedReturnPackageByMinRetPkgId(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryJITFailedReturnPackageByMinRetPkgId(long minId, DDBParam param) {
		if(null == param) {
			param = DDBParam.genParamX(100);
		}
		param.setAsc(true);
		param.setOrderColumn("retPkgId");
		List<ReturnPackage> retPkgList = retPkgDao.queryWaitingJITPushObjectsByStateWithMinRetPkgId(minId, false, param);
		List<ReturnPackageDTO> retPkgDTOList = convertReturnPackageMetaListToDTOList(retPkgList);
		// 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retPkgDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByTimeRange(long, long, boolean, com.xyl.mmall.order.enums.ReturnPackageState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackageDTO> queryReturnPackageByTimeRange(long start, long end, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param) {
		List<ReturnPackage> retPkgList = retPkgDao.queryObjectsByStateWithTimeRange(stateArray, start, end, deprecated, param);
		return convertReturnPackageMetaListToDTOList(retPkgList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByTimeRange2(long, long, boolean, com.xyl.mmall.order.enums.ReturnPackageState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryReturnPackageByTimeRange2(long start, long end, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param) {
		List<ReturnPackageDTO> retPkgList = queryReturnPackageByTimeRange(start, end, deprecated, stateArray, param);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retPkgList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByMailNO(java.lang.String, boolean, com.xyl.mmall.order.enums.ReturnPackageState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackageDTO> queryReturnPackageByMailNO(String mailNO, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param) {
		List<ReturnPackage> retPkgList = retPkgDao.queryObjectsByStateWithMailNO(stateArray, mailNO, deprecated, param);
		return convertReturnPackageMetaListToDTOList(retPkgList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageByMailNO2(java.lang.String, boolean, com.xyl.mmall.order.enums.ReturnPackageState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryReturnPackageByMailNO2(String mailNO, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param) {
		List<ReturnPackageDTO> retPkgList = queryReturnPackageByMailNO(mailNO, deprecated, stateArray, param);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retPkgList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}
	
	/**
	 * 货到付款：退货银行卡信息
	 * 
	 * @param cardRecordId
	 * @param userId
	 * @return
	 */
	@Override
	public ReturnCODBankCardInfoDTO queryReturnCODBankCardInfo(long cardRecordId, long userId) {
		ReturnCODBankCardInfo bankCard = codRetBankCardDao.getObjectByIdAndUserId(cardRecordId, userId);
		return null == bankCard ? null : new ReturnCODBankCardInfoDTO(bankCard);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#waitingReturnAuditCount()
	 */
	@Override
	public Map<Integer, Long> waitingReturnAuditCount() {
		return retPkgDao.getWaitingReturnAuditCount();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#waitingReturnCountOfCOD()
	 */
	@Override
	public Map<Integer, Long> waitingReturnCountOfCOD() {
		return retPkgDao.getWaitingReturnCountOfCOD();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#getReturnPackageShouldBeCanceled(long)
	 */
	@Override
	public Map<Long, List<Long>> getReturnPackageShouldBeCanceled(long detainTime) {
		return retPkgDao.getObjectsShouldBeCanceled(detainTime);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#queryReturnPackageShouldReturnCashByMinRetPkgId(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryReturnPackageShouldReturnCashByMinRetPkgId(long minId, DDBParam param) {
		if(null == param) {
			param = DDBParam.genParamX(100);
		}
		param.setAsc(true);
		param.setOrderColumn("retPkgId");
		List<ReturnPackage> retPkgList = retPkgDao.getReturnPackageShouldReturnCashByMinRetPkgId(minId, param);
		List<ReturnPackageDTO> retPkgDTOList = convertReturnPackageMetaListToDTOList(retPkgList);
		// 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retPkgDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.ReturnPackageQueryService#getReturnedButNotDistributedReturnPackage(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg getReturnedButNotDistributedReturnPackage(long minId, DDBParam param) {
		if(null == param) {
			param = DDBParam.genParamX(100);
		}
		param.setAsc(true);
		param.setOrderColumn("retPkgId");
		List<ReturnPackage> retPkgList = retPkgDao.getReturnedButNotDistributedObjects(minId, param);
		List<ReturnPackageDTO> retPkgDTOList = convertReturnPackageMetaListToDTOList(retPkgList);
		// 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, retPkgDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

}
