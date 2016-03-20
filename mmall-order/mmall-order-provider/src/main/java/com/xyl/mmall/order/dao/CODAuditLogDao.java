package com.xyl.mmall.order.dao;

import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.enums.CODAuditState;
import com.xyl.mmall.order.meta.CODAuditLog;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午11:03:32
 *
 */
public interface CODAuditLogDao extends AbstractDao<CODAuditLog> {

	/**
	 * 
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<CODAuditLog> queryCODAuditLogByState(CODAuditState[] stateArray, DDBParam param);
	
	/**
	 * 
	 * @param stateArray
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public List<CODAuditLog> queryCODAuditLogByStateWithUserIdAndOrderId(CODAuditState[] stateArray, long userId, long orderId);
	
	/**
	 * 
	 * @param stateArray
	 * @param userId
	 * @param param
	 * @return
	 */
	public List<CODAuditLog> queryCODAuditLogByStateWithOrderIdList(CODAuditState[] stateArray, 
			List<Long> orderIdList, DDBParam param);
	
	/**
	 * 
	 * @param stateArray
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public List<CODAuditLog> queryCODAuditLogByStateWithTimeRange(CODAuditState[] stateArray, long start, long end, DDBParam param);
	
	/**
	 * 
	 * @param someTime
	 * @param states
	 * @param ddbParam
	 * @return
	 */
	public List<CODAuditLog> queryCODAuditLogByStateBeforeSomeTime(long someTime, CODAuditState[] states, DDBParam ddbParam);
	
	/**
	 * 
	 * @param someTime
	 * @param ddbParam
	 * @return
	 */
	public List<CODAuditLog> queryCODAuditLogOfTimeout(long someTime, DDBParam ddbParam);
	
	/**
	 * 
	 * @param ddbParam
	 * @return
	 */
	public List<CODAuditLog> queryIllegalCODAuditLog(DDBParam ddbParam);
	
	/**
	 * 更新审核状态
	 * 
	 * @param userId
	 * @param auditLogId
	 * @param auditUserId
	 * @param extInfo
	 * @param byRobot
	 * @param newState
	 * @param stateArray
	 * @return
	 */
	public boolean updateCODAuditState(long userId, long auditLogId, long auditUserId, String extInfo, boolean byRobot, 
			CODAuditState newState, CODAuditState[] stateArray);
	
	/**
	 * 更新审核状态
	 * 
	 * @param auditLog
	 * @param extInfo
	 * @param newState
	 * @param stateArray
	 * @return
	 */
	public boolean updateCODAuditState(CODAuditLog auditLog, String extInfo, CODAuditState newState, CODAuditState[] stateArray);
	
	/**
	 * 站点Id -> count
	 * 
	 * @return
	 */
	public Map<Integer, Long> getWaitingAuditCount();
}
