package com.xyl.mmall.timer.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.order.dto.CODAuditLogDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.enums.CODAuditState;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.service.CODAuditService;
import com.xyl.mmall.order.service.OrderExpInfoService;
import com.xyl.mmall.promotion.service.RebateService;
import com.xyl.mmall.timer.facade.CODAuditTimerFacade;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月29日 上午9:19:21
 *
 */
@Facade("codAuditTimerFacade")
public class CODAuditTimerFacadeImpl implements CODAuditTimerFacade {

	// 待审核订单超过2小时，系统自动审核通过
	private static final long TIME_DETAIN = 2 * 60 * 60 * 1000L;
	
	// 被拒绝的订单保留24小时，超过24小时未有撤销操作，到付订单自动变为取消状态
	private static final long TIME_DETAIN_2 = 24 * 60 * 60 * 1000L;
	// private static final long TIME_DETAIN_2 = 60 * 60 * 1000L;
	
	@Autowired
	private CODAuditService codAuditService;
	
	@Autowired
	private OrderFacade orderFacade;
	
	@Autowired
	private OrderExpInfoService orderExpInfoService;
	
	@Autowired
	private RebateService rebateService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.timer.facade.CODAuditTimerFacade#passCODAuditBeforeSomeTime()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg passCODAuditBeforeSomeTime() {
		RetArg response = new RetArg();
		long totalCount = 0, codFailCount = 0, rebateFailCount = 0;
		
		long someTime = System.currentTimeMillis() - TIME_DETAIN;
		CODAuditState[] states = new CODAuditState[] { CODAuditState.WAITING };
		DDBParam param = DDBParam.genParamX(100);
		RetArg retArg = codAuditService.queryCODAuditLogBeforeSomeTime(someTime, states, param);
		
		List<CODAuditLogDTO> logList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfCollection(logList)) {
			for(CODAuditLogDTO log : logList) {
				if(null == log) {
					continue;
				}
				totalCount++;
				try {
					if(!codAuditService.passCODAuditByRobot(log.getUserId(), log.getId(), 0)) {
						logger.warn("到付审核通过(系统操作)失败: " + log.getId());
						codFailCount++;
						rebateFailCount++;
						continue;
					} 
				} catch (Exception e) {
					logger.warn("到付审核通过(系统操作)失败: " + log.getId(), e);
					codFailCount++;
					rebateFailCount++;
					continue;
				}
				if(!rebateService.rebate(log.getUserId(), log.getOrderId())) {
					logger.warn("rebate失败: [userId:" + log.getUserId() + ", orderId:" + log.getOrderId() + "]");
					rebateFailCount++;
				}
			}
			DDBParam remoteParam = RetArgUtil.get(retArg, DDBParam.class);
			if(null != remoteParam && remoteParam.isHasNext()) {
				retArg = codAuditService.queryCODAuditLogBeforeSomeTime(someTime, states, remoteParam);
				logList = RetArgUtil.get(retArg, ArrayList.class);
			} else {
				logList = null;
			}
		}
		
		boolean isSucc = (0 == codFailCount && 0 == rebateFailCount);
		RetArgUtil.put(response, isSucc ? Boolean.TRUE : Boolean.FALSE);
		RetArgUtil.put(response, "[passCODAuditBeforeSomeTime()] total:" + totalCount + ", cod failed:" + codFailCount + ", rebate failed:" + rebateFailCount);
		return response;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.timer.facade.CODAuditTimerFacade#passCODAuditInWhiteList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg passCODAuditInWhiteList() {
		RetArg response = new RetArg();
		long totalCount = 0, codFailCount = 0, rebateFailCount = 0;
		
		CODAuditState[] states = new CODAuditState[] { CODAuditState.WAITING };
		DDBParam param = DDBParam.genParamX(100);
		RetArg retArg = codAuditService.queryAllCODAuditLog2(states, param);
		List<CODAuditLogDTO> logList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfCollection(logList)) {
			for(CODAuditLogDTO log : logList) {
				if(null == log) {
					continue;
				}
				long userId = log.getUserId();
				long orderId = log.getOrderId();
				OrderExpInfoDTO expDTO = orderExpInfoService.queryInfoByUserIdAndOrderId(userId, orderId);
				if(null == expDTO || !codAuditService.isAddressInWhitelist(userId, expDTO)) {
					continue;
				}
				totalCount++;
				try {
					if(!codAuditService.passCODAuditByRobot(log.getUserId(), log.getId(), 0)) {
						logger.warn("到付审核通过(系统操作)失败: " + log.getId());
						codFailCount++;
						rebateFailCount++;
						continue;
					}
				} catch (Exception e) {
					logger.warn("到付审核通过(系统操作)失败: " + log.getId(), e);
					codFailCount++;
					rebateFailCount++;
					continue;
				}
				if(!rebateService.rebate(log.getUserId(), log.getOrderId())) {
					logger.warn("rebate失败: [userId:" + log.getUserId() + ", orderId:" + log.getOrderId() + "]");
					rebateFailCount++;
				}
			}
			DDBParam remoteParam = RetArgUtil.get(retArg, DDBParam.class);
			if(null != remoteParam && remoteParam.isHasNext()) {
				retArg = codAuditService.queryAllCODAuditLog2(states, remoteParam);
				logList = RetArgUtil.get(retArg, ArrayList.class);
			} else {
				logList = null;
			}
		}
		
		boolean isSucc = (0 == codFailCount && 0 == rebateFailCount);
		RetArgUtil.put(response, isSucc ? Boolean.TRUE : Boolean.FALSE);
		RetArgUtil.put(response, "[passCODAuditInWhiteList()] total:" + totalCount + ", cod failed:" + codFailCount + ", rebate failed:" + rebateFailCount);
		return response;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.timer.facade.CODAuditTimerFacade#cancelCODAuditOfTimeout()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg cancelCODAuditOfTimeout() {
		RetArg response = new RetArg();
		long totalCount = 0, failCount = 0;
		
		long someTime = System.currentTimeMillis() - TIME_DETAIN_2;
		DDBParam param = DDBParam.genParamX(100);
		// DDBParam param = DDBParam.genParamX(2);
		RetArg retArg = codAuditService.queryCODAuditLogOfTimeout(someTime, param);
		
		List<CODAuditLogDTO> logList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfCollection(logList)) {
			for(CODAuditLogDTO log : logList) {
				if(null == log) {
					continue;
				}
				totalCount++;
				// 0.参数准备
				OrderCancelInfoDTO cancelDTO = new OrderCancelInfoDTO();
				cancelDTO.setCancelSource(OrderCancelSource.OT_SYS);
				cancelDTO.setOrderId(log.getOrderId());
				cancelDTO.setReason("到付拒绝超过24小时未有撤销操作");
				cancelDTO.setRtype(OrderCancelRType.UN_ORI);
				cancelDTO.setUserId(log.getUserId());
				// 1.调用取消订单的服务
				RetArg cancelRetArg = orderFacade.cancelOrder(cancelDTO);
				Boolean retOfCancel = RetArgUtil.get(cancelRetArg, Boolean.class);
				if(null == retOfCancel || retOfCancel != Boolean.TRUE) {
					logger.warn("取消(到付审核拒绝的)订单失败: " + log.getOrderId());
					failCount++;
				}
			}
			DDBParam remoteParam = RetArgUtil.get(retArg, DDBParam.class);
			if(null != remoteParam && remoteParam.isHasNext()) {
				retArg = codAuditService.queryCODAuditLogOfTimeout(someTime, remoteParam);
				logList = RetArgUtil.get(retArg, ArrayList.class);
			} else {
				logList = null;
			}
		}
	
		boolean isSucc =  (0 == failCount);
		RetArgUtil.put(response, isSucc ? Boolean.TRUE : Boolean.FALSE);
		RetArgUtil.put(response, "[cancelCODAuditOfTimeout()] total:" + totalCount + ", cod failed:" + failCount);
		return response;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.timer.facade.CODAuditTimerFacade#cancelIllegalCODAudit()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg cancelIllegalCODAudit() {
		RetArg response = new RetArg();
		long totalCount = 0, failCount = 0;
		
		DDBParam param = DDBParam.genParamX(100);
		// DDBParam param = DDBParam.genParamX(2);
		RetArg retArg = codAuditService.queryIllegalCODAuditLog(param);
		List<CODAuditLogDTO> logList = RetArgUtil.get(retArg, ArrayList.class);
		while (CollectionUtil.isNotEmptyOfCollection(logList)) {
			for(CODAuditLogDTO log : logList) {
				if(null == log) {
					continue;
				}
				totalCount++;
				boolean isSucc = codAuditService.setCODAuditStateToCanceled(log.getUserId(), log.getId(), log.getAuditUserId(), "失效取消 (定时器处理)", true);
				if(!isSucc) {
					logger.warn("取消失效的到付请求失败: " + log.getId());
					failCount++;
				}
			}
			DDBParam remoteParam = RetArgUtil.get(retArg, DDBParam.class);
			if(null != remoteParam && remoteParam.isHasNext()) {
				retArg = codAuditService.queryIllegalCODAuditLog(remoteParam);
				logList = RetArgUtil.get(retArg, ArrayList.class);
			} else {
				logList = null;
			}
		}
		
		boolean isSucc = (0 == failCount);
		RetArgUtil.put(response, isSucc ? Boolean.TRUE : Boolean.FALSE);
		RetArgUtil.put(response, "[cancelIllegalCODAudit()] total:" + totalCount + ", cod failed:" + failCount);
		return response;
	}

	
}
