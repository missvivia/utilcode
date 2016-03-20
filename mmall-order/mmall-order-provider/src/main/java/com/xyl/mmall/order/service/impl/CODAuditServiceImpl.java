package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.order.dao.CODAuditLogDao;
import com.xyl.mmall.order.dao.CODBlacklistAddressDao;
import com.xyl.mmall.order.dao.CODBlacklistUserDao;
import com.xyl.mmall.order.dao.CODWhitelistAddressDao;
import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.dto.CODAuditLogDTO;
import com.xyl.mmall.order.dto.CODBlacklistAddressDTO;
import com.xyl.mmall.order.dto.CODBlacklistUserDTO;
import com.xyl.mmall.order.dto.CODWhitelistAddressDTO;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.enums.CODAuditState;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.meta.CODAuditLog;
import com.xyl.mmall.order.meta.CODBlacklistAddress;
import com.xyl.mmall.order.meta.CODBlacklistUser;
import com.xyl.mmall.order.meta.CODWhitelistAddress;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.param.CODWBlistAddressParam;
import com.xyl.mmall.order.service.CODAuditService;
import com.xyl.mmall.order.service.ConsigneeAddressService;
import com.xyl.mmall.order.util.OrderInstantiationUtil;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午11:06:53
 * 
 */
@Service("CODAuditService")
public class CODAuditServiceImpl implements CODAuditService {

	@Autowired
	protected CODAuditLogDao codAuditLogDao;

	@Autowired
	protected CODBlacklistUserDao codBlacklistUserDao;

	@Autowired
	protected CODBlacklistAddressDao codBlacklistAddressDao;

	@Autowired
	protected CODWhitelistAddressDao codWhitelistAddressDao;
	
	@Autowired
	protected OrderFormDao orderFormDao;
	
	@Autowired
	protected ConsigneeAddressService caService;
	
	@Autowired
	protected OrderInstantiationUtil orderInstantiationUtil;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<CODAuditLogDTO> convertLogMetaToDTO(List<CODAuditLog> logList) {
		if (null == logList) {
			return new ArrayList<CODAuditLogDTO>();
		}
		List<CODAuditLogDTO> logDTOList = new ArrayList<CODAuditLogDTO>(logList.size());
		for (CODAuditLog log : logList) {
			logDTOList.add(new CODAuditLogDTO(log));
		}
		return logDTOList;
	}

	private List<CODBlacklistUserDTO> convertUserMetaToDTO(List<CODBlacklistUser> userList) {
		if (null == userList) {
			return new ArrayList<CODBlacklistUserDTO>();
		}
		List<CODBlacklistUserDTO> userDTOList = new ArrayList<CODBlacklistUserDTO>(userList.size());
		for (CODBlacklistUser user : userList) {
			userDTOList.add(new CODBlacklistUserDTO(user));
		}
		return userDTOList;
	}
	
	private List<CODBlacklistAddressDTO> convertAddressMetaToDTO(List<CODBlacklistAddress> addressList) {
		if (null == addressList) {
			return new ArrayList<CODBlacklistAddressDTO>();
		}
		List<CODBlacklistAddressDTO> addressDTOList = new ArrayList<CODBlacklistAddressDTO>(addressList.size());
		for (CODBlacklistAddress address : addressList) {
			addressDTOList.add(new CODBlacklistAddressDTO(address));
		}
		return addressDTOList;
	}
	
	private List<CODWhitelistAddressDTO> convertWhiteAddressMetaToDTO(List<CODWhitelistAddress> addressList) {
		if (null == addressList) {
			return new ArrayList<CODWhitelistAddressDTO>();
		}
		List<CODWhitelistAddressDTO> addressDTOList = new ArrayList<CODWhitelistAddressDTO>(addressList.size());
		for (CODWhitelistAddress address : addressList) {
			addressDTOList.add(new CODWhitelistAddressDTO(address));
		}
		return addressDTOList;
	}

	private boolean isAlreadyInBlacklistAddress(CODWBlistAddressParam param) {
		if(null == param) {
			return false;
		}
		long userId = param.getUserId();
		List<CODBlacklistAddress> baList = codBlacklistAddressDao.getListByUserId(userId);
		if(null == baList) {
			return false;
		}
		for(CODBlacklistAddress ba : baList) {
			if(null == ba) {
				continue;
			}
			if(!param.hitBlack(new CODBlacklistAddressDTO(ba))) {
				continue;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.CODAuditService#queryCODAuditLog(long,
	 *      long)
	 */
	@Override
	public CODAuditLogDTO queryCODAuditLog(long logId, long userId) {
		CODAuditLog log = codAuditLogDao.getObjectByIdAndUserId(logId, userId);
		return null == log ? null : new CODAuditLogDTO(log);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryCODAuditLogByUserIdAndOrderId(com.xyl.mmall.order.enums.CODAuditState[], long, long)
	 */
	@Override
	public List<CODAuditLogDTO> queryCODAuditLogByUserIdAndOrderId(CODAuditState[] states, long userId, long orderId) {
		if(null == states) {
			states = CODAuditState.values();
		}
		List<CODAuditLog> logList = codAuditLogDao.queryCODAuditLogByStateWithUserIdAndOrderId(states, userId, orderId);
		return convertLogMetaToDTO(logList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryAllCODAuditLog(com.xyl.mmall.order.enums.CODAuditState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODAuditLogDTO> queryAllCODAuditLog(CODAuditState[] states, DDBParam param) {
		if(null == states) {
			states = CODAuditState.values();
		}
		List<CODAuditLog> logList = codAuditLogDao.queryCODAuditLogByState(states, param);
		return convertLogMetaToDTO(logList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryAllCODAuditLog2(com.xyl.mmall.order.enums.CODAuditState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryAllCODAuditLog2(CODAuditState[] states, DDBParam param) {
		if(null == states) {
			states = CODAuditState.values();
		}
		List<CODAuditLogDTO> codList = queryAllCODAuditLog(states, param);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, codList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.CODAuditService#queryCODAuditLogWithTimeRange(java.lang.String,
	 *      java.lang.String, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODAuditLogDTO> queryCODAuditLogWithTimeRange(CODAuditState[] states, long startTime, long endTime,
			DDBParam param) {
		List<CODAuditLog> logList = codAuditLogDao.queryCODAuditLogByStateWithTimeRange(states, startTime, endTime,
				param);
		return convertLogMetaToDTO(logList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.CODAuditService#queryCODAuditLogWithTimeRange2(com.xyl.mmall.order.enums.CODAuditState[],
	 *      long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryCODAuditLogWithTimeRange2(CODAuditState[] states, long startTime, long endTime, DDBParam param) {
		List<CODAuditLogDTO> codList = queryCODAuditLogWithTimeRange(states, startTime, endTime, param);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, codList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.CODAuditService#queryCODAuditLogWithOrderIdList(com.xyl.mmall.order.enums.CODAuditState[],
	 *      java.util.List, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryCODAuditLogWithOrderIdList(CODAuditState[] states, List<Long> orderIdList, DDBParam param) {
		List<CODAuditLogDTO> logDTOList = new ArrayList<CODAuditLogDTO>();
		if (null != orderIdList && orderIdList.size() > 0) {
			List<CODAuditLog> logList = codAuditLogDao.queryCODAuditLogByStateWithOrderIdList(states, orderIdList,
					param);
			logDTOList = convertLogMetaToDTO(logList);
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, logDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryCODAuditLogBeforeSomeTime(long, com.xyl.mmall.order.enums.CODAuditState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryCODAuditLogBeforeSomeTime(long someTime, CODAuditState[] states, DDBParam ddbParam) {
		DDBParam retParam = (null != ddbParam) ? ddbParam : DDBParam.genParamX(100);
		retParam.setAsc(true);
		retParam.setOrderColumn("id");
		List<CODAuditLog> logList = codAuditLogDao.queryCODAuditLogByStateBeforeSomeTime(someTime, states, retParam);
		List<CODAuditLogDTO> logDTOList = convertLogMetaToDTO(logList);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, logDTOList);
		RetArgUtil.put(retArg, retParam);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryCODAuditLogOfTimeout(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryCODAuditLogOfTimeout(long someTime, DDBParam ddbParam) {
		DDBParam retParam = (null != ddbParam) ? ddbParam : DDBParam.genParamX(100);
		retParam.setAsc(true);
		retParam.setOrderColumn("id");
		List<CODAuditLog> logList = codAuditLogDao.queryCODAuditLogOfTimeout(someTime, retParam);
		List<CODAuditLogDTO> logDTOList = convertLogMetaToDTO(logList);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, logDTOList);
		RetArgUtil.put(retArg, retParam);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryIllegalCODAuditLog(com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryIllegalCODAuditLog(DDBParam ddbParam) {
		DDBParam retParam = (null != ddbParam) ? ddbParam : DDBParam.genParamX(100);
		retParam.setAsc(true);
		retParam.setOrderColumn("id");
		List<CODAuditLog> logList = codAuditLogDao.queryIllegalCODAuditLog(retParam);
		List<CODAuditLogDTO> logDTOList = convertLogMetaToDTO(logList);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, logDTOList);
		RetArgUtil.put(retArg, retParam);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#setCODAuditStateToPassed(long, long, long, boolean)
	 */
	@Override
	public boolean setCODAuditStateToPassed(long userId, long auditLogId, long auditUserId, boolean byRobot) {
		CODAuditState[] stateArray = new CODAuditState[] { CODAuditState.WAITING };
		return codAuditLogDao.updateCODAuditState(userId, auditLogId, auditUserId, "无", false, CODAuditState.PASSED,
				stateArray);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#setCODAuditStateToRefused(long, long, long, java.lang.String)
	 */
	@Override
	@Transaction
	public boolean setCODAuditStateToRefused(long userId, long auditLogId, long auditUserId, String extInfo) {
		CODAuditLog codAuditLog = codAuditLogDao.getObjectByIdAndUserId(auditLogId, userId);
		if(null == codAuditLog) {
			return false;
		}
		long orderId = codAuditLog.getOrderId();
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + orderId + " ,userId=" + userId);
			return false;
		}
		OrderForm ordForm = RetArgUtil.get(retArgOfTmp, OrderForm.class);
		if(null == ordForm) {
			throw new ServiceException("非法审核日志记录 -> 订单不存在 [orderId:" + orderId + ", userId:" + userId + "]");
		}
		OrderFormState currentState = ordForm.getOrderFormState();
		if(null == currentState) {
			return false;
		}
		if(currentState == OrderFormState.CANCEL_ING || currentState == OrderFormState.CANCEL_ED) {
			setCODAuditStateToCanceled(userId, auditLogId, auditUserId, CODAuditState.CANCELED.getDesc(), false);
			return false;
		}
		if(!OrderFormState.canExecCODReject(ordForm.getOrderFormState())) {
			return false;
		}
		boolean isSucc = false;
		ordForm = orderFormDao.getLockByKey(ordForm);
		ordForm.setOrderFormState(OrderFormState.COD_AUDIT_REFUSE);
		isSucc = orderFormDao.updateOrdState(ordForm, new OrderFormState[] {OrderFormState.WAITING_COD_AUDIT});
		if(!isSucc) {
			throw new ServiceException("客服更新到付审核状态(订单状态)失败 [orderId:" + orderId + ", userId:" + userId + "]");
		}
		CODAuditState[] stateArray = new CODAuditState[] { CODAuditState.WAITING };
		isSucc = codAuditLogDao.updateCODAuditState(userId, auditLogId, auditUserId, extInfo, false, CODAuditState.REFUSED, stateArray);
		if(!isSucc) {
			throw new ServiceException("客服更新到付审核状态(审核日志状态)失败 [logId:" + auditLogId + "]");
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#setCODAuditStateToCanceled(long, long, long, java.lang.String, boolean)
	 */
	@Override
	public boolean setCODAuditStateToCanceled(long userId, long auditLogId, long auditUserId, String extInfo, boolean byRobot) {
		CODAuditState[] stateArray = CODAuditState.values();
		return codAuditLogDao.updateCODAuditState(userId, auditLogId, auditUserId, extInfo, byRobot, CODAuditState.CANCELED,
				stateArray);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#cancelCODLogOfWaitingAudit(com.xyl.mmall.order.dto.CODAuditLogDTO, java.lang.String)
	 */
	@Override
	@Transaction
	public boolean cancelCODLogOfWaitingAudit(CODAuditLogDTO auditLog, String extInfo) {
		if(null == auditLog) {
			return false;
		}
		CODAuditLog lockedLog = codAuditLogDao.getLockByKey(auditLog);
		if(null == lockedLog) {
			return false;
		}
		return codAuditLogDao.updateCODAuditState(auditLog, extInfo, CODAuditState.CANCELED, new CODAuditState[] {CODAuditState.WAITING});
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#cancelCODAuditStateToWaiting(long, long, long)
	 */
	@Override
	@Transaction
	public boolean cancelCODAuditStateToWaiting(long userId, long auditLogId, long auditUserId) {
		CODAuditLog log = codAuditLogDao.getObjectByIdAndUserId(auditLogId, userId);
		if(null == log) {
			return false;
		}
		long orderId = log.getOrderId();
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + orderId + " ,userId=" + userId);
			return false;
		}
		OrderForm ordForm = RetArgUtil.get(retArgOfTmp, OrderForm.class);
		if(null == ordForm) {
			throw new ServiceException("撤销审核拒绝(审核日志状态)失败 -> 没有找到相关订单 [orderId:" + orderId + ", userId:" + userId + "]");
		}
		OrderFormState currentState = ordForm.getOrderFormState();
		if(null == currentState) {
			return false;
		}
		if(currentState == OrderFormState.CANCEL_ING || currentState == OrderFormState.CANCEL_ED) {
			setCODAuditStateToCanceled(userId, auditLogId, auditUserId, CODAuditState.CANCELED.getDesc(), false);
			return false;
		}
		if(!OrderFormState.canExecCODCancelReject(currentState)) {
			return false;
		}
		boolean isSucc = false;
		ordForm = orderFormDao.getLockByKey(ordForm);
		ordForm.setOrderFormState(OrderFormState.WAITING_COD_AUDIT);
		isSucc = orderFormDao.updateOrdState(ordForm, new OrderFormState[] { OrderFormState.COD_AUDIT_REFUSE });
		if(!isSucc) {
			String desc = null == currentState ? "" : currentState.getDesc();
			throw new ServiceException("撤销审核拒绝(订单状态)失败 [orderId:" + orderId + ", userId:" + userId 
					+ "orderFormState:" + desc + "]");
		}
		CODAuditState[] stateArray = new CODAuditState[] { CODAuditState.PASSED, CODAuditState.REFUSED };
		isSucc = codAuditLogDao.updateCODAuditState(userId, auditLogId, auditUserId, "无", false, CODAuditState.WAITING, stateArray);
		if(!isSucc) {
			throw new ServiceException("撤销审核拒绝(审核日志状态)失败 [logId:" + auditLogId + "]");
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.CODAuditService#addToBlacklistUser(long,
	 *      long)
	 */
	@Override
	public boolean addToBlacklistUser(long userId, long auditUserId) {
		if(isUserInBlackList(userId)) {
			return true;
		}
		CODBlacklistUser blackUser = new CODBlacklistUser();
		blackUser.setUserId(userId);
		blackUser.setAuditUserId(auditUserId);
		blackUser.setCtime(System.currentTimeMillis());
		return (null != codBlacklistUserDao.addObject(blackUser));
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.CODAuditService#addToBlacklistAddress(long,
	 *      com.xyl.mmall.order.param.CODWBlistAddressParam)
	 */
	@Override
	public boolean addToBlacklistAddress(long auditUserId, CODWBlistAddressParam param) {
		if(isAlreadyInBlacklistAddress(param)) {
			return true;
		}
		CODBlacklistAddressDTO blackAddress = new CODBlacklistAddressDTO();
		blackAddress.fillWithAddressParam(auditUserId, param);
		return (null != codBlacklistAddressDao.addObject(blackAddress));
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.CODAuditService#addToWhitelistAddress(long,
	 *      com.xyl.mmall.order.param.CODWBlistAddressParam)
	 */
	@Override
	public boolean addToWhitelistAddress(long auditUserId, CODWBlistAddressParam param) {
		CODWhitelistAddressDTO whiteAddress = new CODWhitelistAddressDTO();
		whiteAddress.fillWithAddressParam(auditUserId, param);
		return (null != codWhitelistAddressDao.addObject(whiteAddress));
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#passCODAuditByRobot(long, long, long)
	 */
	@Override
	@Transaction
	public boolean passCODAuditByRobot(long userId, long auditLogId, long robotId) {
		CODAuditLog log = codAuditLogDao.getObjectByIdAndUserId(auditLogId, userId);
		if(null == log) {
			return false;
		}
		long orderId = log.getOrderId();
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + orderId + " ,userId=" + userId);
			return false;
		}
		OrderForm ordForm = RetArgUtil.get(retArgOfTmp, OrderForm.class);
		if(null == ordForm) {
			return false;
		}
		OrderFormState state = ordForm.getOrderFormState();
		if(null == state) {
			return false;
		}
		if(state == OrderFormState.CANCEL_ING || state == OrderFormState.CANCEL_ED) {
			setCODAuditStateToCanceled(userId, auditLogId, robotId, CODAuditState.CANCELED.getDesc(), true);
			return false;
		}
		if(!OrderFormState.canExecCODPass(state)) {
			return false;
		}
		boolean isSucc = false;
		ordForm = orderFormDao.getLockByKey(ordForm);
		ordForm.setOrderFormState(OrderFormState.WAITING_SEND_ORDER);
		isSucc = orderFormDao.updateOrdState(ordForm, new OrderFormState[] { OrderFormState.WAITING_COD_AUDIT });
		if(!isSucc) {
			throw new ServiceException("系统更新到付审核状态(订单状态)失败 [orderId:" + orderId + ", userId:" + userId + "]");
		}
		isSucc = setCODAuditStateToPassed(userId, auditLogId, robotId, true);
		if(!isSucc) {
			throw new ServiceException("系统更新到付审核状态(审核日志状态)失败 [logId:" + auditLogId + "]");
		}
		orderFormDao.updateCODAuditPayTime(ordForm);
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#passCODAuditByCustomerService(long, long, long, com.xyl.mmall.order.param.CODWBlistAddressParam)
	 */
	@Override
	@Transaction
	public boolean passCODAuditByCustomerService(long userId, long auditLogId, long auditUserId, CODWBlistAddressParam param) {
		if(null == param) {
			return false;
		}
		CODAuditLog log = codAuditLogDao.getObjectByIdAndUserId(auditLogId, userId);
		if(null == log) {
			return false;
		}
		long orderId = log.getOrderId();
		RetArg retArgOfTmp = orderInstantiationUtil.isContinueForNormalService(orderId, userId);
		Boolean isContinue = RetArgUtil.get(retArgOfTmp, Boolean.class);
		if (isContinue != Boolean.TRUE) {
			logger.error("orderInstantiationUtil.isContinueForNormalService fail, orderId=" + orderId + " ,userId=" + userId);
			return false;
		}
		OrderForm ordForm = RetArgUtil.get(retArgOfTmp, OrderForm.class);
		if(null == ordForm) {
			return false;
		}
		OrderFormState state = ordForm.getOrderFormState();
		if(null == state) {
			return false;
		}
		if(state == OrderFormState.CANCEL_ING || state == OrderFormState.CANCEL_ED) {
			setCODAuditStateToCanceled(userId, auditLogId, auditUserId, CODAuditState.CANCELED.getDesc(), false);
			return false;
		}
		if(!OrderFormState.canExecCODPass(state)) {
			return false;
		}
		boolean isSucc = false;
		ordForm = orderFormDao.getLockByKey(ordForm);
		ordForm.setOrderFormState(OrderFormState.WAITING_SEND_ORDER);
		isSucc = orderFormDao.updateOrdState(ordForm, new OrderFormState[] { OrderFormState.WAITING_COD_AUDIT });
		if(!isSucc) {
			throw new ServiceException("客服更新到付审核状态(订单状态)失败 [orderId:" + orderId + ", userId:" + userId + "]");
		}
		isSucc = setCODAuditStateToPassed(userId, auditLogId, auditUserId, false);
		if (!isSucc) {
			throw new ServiceException("设置审核状态失败 [auditLogId:" + auditLogId + "]");
		}
		orderFormDao.updateCODAuditPayTime(ordForm);
		isSucc = addToWhitelistAddress(auditUserId, param);
		if (!isSucc) {
			throw new ServiceException("添加地址白名单失败 [auditLogId:" + auditLogId + "]");
		}
		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryBlacklistUserByUserId(long)
	 */
	@Override
	public CODBlacklistUserDTO queryBlacklistUserByUserId(long userId) {
		CODBlacklistUser blackUser = codBlacklistUserDao.getObjectById(userId);
		return null == blackUser ? null : new CODBlacklistUserDTO(blackUser);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryBlacklistAddressByUserId(long)
	 */
	@Override
	public List<CODBlacklistAddressDTO> queryBlacklistAddressByUserId(long userId) {
		List<CODBlacklistAddress> addressList = codBlacklistAddressDao.getListByUserId(userId);
		return convertAddressMetaToDTO(addressList);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.CODAuditService#isInBlacklist(java.lang.Long,
	 *      com.xyl.mmall.order.dto.ConsigneeAddressDTO)
	 */
	public boolean isInBlacklist(Long userId, ConsigneeAddressDTO caDTO) {
		// 1.判断用户Id是否在黑名单里
		boolean isValidOfUser = true;
		if (userId != null && userId > 0) {
			CODBlacklistUser blackUser = codBlacklistUserDao.getObjectByUserId(userId);
			isValidOfUser = blackUser == null;
		}
		// 2.判断用户的收货地址是否在黑名单里
		boolean isValidOfAddress = true;
		if (caDTO != null) {
			isValidOfAddress = !isAddressInBlacklist(caDTO.getUserId(), caDTO);
		}
		return !(isValidOfUser && isValidOfAddress);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#isUserInBlackList(long)
	 */
	@Override
	public boolean isUserInBlackList(long userId) {
		return (null != queryBlacklistUserByUserId(userId));
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.CODAuditService#isAddressInBlacklist(long,
	 *      com.xyl.mmall.order.dto.ConsigneeAddressDTO)
	 */
	@Override
	public boolean isAddressInBlacklist(long userId, ConsigneeAddressDTO caDTO) {
		if (null == caDTO || userId != caDTO.getUserId()) {
			return false;
		}
		List<CODBlacklistAddressDTO> blackList = queryBlacklistAddressByUserId(userId);
		return isAddressInSpecialBlacklist(userId, caDTO, blackList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.CODAuditService#isAddressInSpecialBlacklist(long,
	 *      com.xyl.mmall.order.dto.ConsigneeAddressDTO, java.util.List)
	 */
	@Override
	public boolean isAddressInSpecialBlacklist(long userId, ConsigneeAddressDTO caDTO,
			List<CODBlacklistAddressDTO> blackList) {
		if (null == caDTO || null == blackList || userId != caDTO.getUserId()) {
			return false;
		}
		for (CODBlacklistAddressDTO ba : blackList) {
			if (null != ba && ba.hitBlack(caDTO)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryWhitelistAddressByUserId(long)
	 */
	@Override
	public List<CODWhitelistAddressDTO> queryWhitelistAddressByUserId(long userId) {
		List<CODWhitelistAddress> addressList = codWhitelistAddressDao.getListByUserId(userId);
		return convertWhiteAddressMetaToDTO(addressList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#isAddressInWhitelist(long, com.xyl.mmall.order.dto.ConsigneeAddressDTO)
	 */
	@Override
	public boolean isAddressInWhitelist(long userId, OrderExpInfoDTO expDTO) {
		if (null == expDTO || userId != expDTO.getUserId()) {
			return false;
		}
		List<CODWhitelistAddressDTO> whiteList = queryWhitelistAddressByUserId(userId);
		return isAddressInSpecialWhitelist(userId, expDTO, whiteList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#isAddressInSpecialWhitelist(long, com.xyl.mmall.order.dto.ConsigneeAddressDTO, java.util.List)
	 */
	@Override
	public boolean isAddressInSpecialWhitelist(long userId, OrderExpInfoDTO expDTO,
			List<CODWhitelistAddressDTO> whiteList) {
		if (null == expDTO || null == whiteList || userId != expDTO.getUserId()) {
			return false;
		}
		for (CODWhitelistAddressDTO wa : whiteList) {
			if (null != wa && wa.hitWhite(expDTO)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#waitingAuditCount()
	 */
	@Override
	public Map<Integer, Long> waitingAuditCount() {
//		Map<Integer, Long> ret = new HashMap<Integer, Long>();
//		List<CODAuditLog> logs = codAuditLogDao.queryCODAuditLogByState(new CODAuditState[] { CODAuditState.WAITING }, null);
//		if(null == logs) {
//			return ret;
//		}
//		for(CODAuditLog log : logs) {
//			if(null == log) {
//				continue;
//			}
//			int provinceId = log.getProvinceId();
//			long count = 1;
//			Long inCount = ret.get(provinceId);
//			if(null != inCount) {
//				count += inCount;
//			}
//			ret.put(provinceId, count);
//		}
//		return ret;
		return codAuditLogDao.getWaitingAuditCount();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryBlacklistUserByUserIdList(java.util.List, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODBlacklistUserDTO> queryBlacklistUserByUserIdList(List<Long> userIdList, DDBParam param) {
		List<CODBlacklistUserDTO> dtoList = new ArrayList<CODBlacklistUserDTO>();
		if(CollectionUtil.isEmptyOfList(userIdList)) {
			return dtoList;
		}
		List<CODBlacklistUser> userList = codBlacklistUserDao.queryObjectsByUserIdList(userIdList, param);
		return convertUserMetaToDTO(userList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#queryBlacklistAddressByUserIdList(java.util.List, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODBlacklistAddressDTO> queryBlacklistAddressByUserIdList(List<Long> userIdList, DDBParam param) {
		List<CODBlacklistAddressDTO> dtoList = new ArrayList<CODBlacklistAddressDTO>();
		if(CollectionUtil.isEmptyOfList(userIdList)) {
			return dtoList;
		}
		List<CODBlacklistAddress> addressList = codBlacklistAddressDao.queryObjectsByUserIdList(userIdList, param);
		return convertAddressMetaToDTO(addressList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#removeFromBlacklistUser(long)
	 */
	@Override
	@Transaction
	public boolean removeFromBlacklistUser(long userId) {
		CODBlacklistUser bu = codBlacklistUserDao.getObjectById(userId);
		if(null == bu) {
			return false;
		}
		bu = codBlacklistUserDao.getLockByKey(bu);
		return codBlacklistUserDao.deleteObjectByKey(bu);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.CODAuditService#removeFromBlacklistAddress(long, long)
	 */
	@Override
	@Transaction
	public boolean removeFromBlacklistAddress(long id, long userId) {
		CODBlacklistAddress ba = codBlacklistAddressDao.getObjectByIdAndUserId(id, userId);
		if(null == ba) {
			return false;
		}
		ba = codBlacklistAddressDao.getLockByKey(ba);
		return codBlacklistAddressDao.deleteObjectByKey(ba);
	}

}
