package com.xyl.mmall.order.service;

import java.util.List;
import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.CODAuditLogDTO;
import com.xyl.mmall.order.dto.CODBlacklistAddressDTO;
import com.xyl.mmall.order.dto.CODBlacklistUserDTO;
import com.xyl.mmall.order.dto.CODWhitelistAddressDTO;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.enums.CODAuditState;
import com.xyl.mmall.order.param.CODWBlistAddressParam;

/**
 * 到付审核服务
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月20日 下午5:11:04
 *
 */
public interface CODAuditService {

	/**
	 * 查询指定的到付审核
	 * 
	 * @param logId
	 * @param userId
	 * @return
	 */
	public CODAuditLogDTO queryCODAuditLog(long logId, long userId);
	
	/**
	 * 根据用户Id、订单Id查询到付审核
	 * 
	 * @param states
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public List<CODAuditLogDTO> queryCODAuditLogByUserIdAndOrderId(CODAuditState[] states, long userId, long orderId);
	
	/**
	 * 查询指定状态的到付审核请求
	 * 
	 * @return
	 */
	public List<CODAuditLogDTO> queryAllCODAuditLog(CODAuditState[] states, DDBParam param);
	
	/**
	 * 查询指定状态的到付审核请求
	 * 
	 * @return
	 */
	public RetArg queryAllCODAuditLog2(CODAuditState[] states, DDBParam param);
	
	/**
	 * 查询指定时间区间的到付审核请求
	 * 
	 * @param startTime，格式：yyyy-MM-dd HH:mm
	 * @param endTime，格式：yyyy-MM-dd HH:mm
	 * @param param
	 * @return
	 */
	public List<CODAuditLogDTO> queryCODAuditLogWithTimeRange(CODAuditState[] states, 
			long startTime, long endTime, DDBParam param);
	
	/**
	 * 查询指定时间区间的到付审核请求
	 * 
	 * @param startTime，格式：yyyy-MM-dd HH:mm
	 * @param endTime，格式：yyyy-MM-dd HH:mm
	 * @param param
	 * @return
	 */
	public RetArg queryCODAuditLogWithTimeRange2(CODAuditState[] states, 
			long startTime, long endTime, DDBParam param);
	
	/**
	 * 查询指定订单的到付审核请求
	 * 
	 * @param userId
	 * @param param
	 * @return
	 */
	public RetArg queryCODAuditLogWithOrderIdList(CODAuditState[] states, List<Long> orderIdList, DDBParam param);

	/**
	 * 查询指定时间之前、指定状态的到付审核请求
	 * 
	 * @param someTime
	 * @param states
	 * @param ddbParam
	 * @return
	 */
	public RetArg queryCODAuditLogBeforeSomeTime(long someTime, CODAuditState[] states, DDBParam ddbParam);
	
	/**
	 * 查询“被拒绝的&&超过24小时未有撤销操作的&&到付订单非取消状态的”审核请求
	 * 
	 * @param someTime
	 * @param ddbParam
	 * @return
	 */
	public RetArg queryCODAuditLogOfTimeout(long someTime, DDBParam ddbParam);
	
	/**
	 * 查询失效的审核请求
	 * 
	 * @param someTime
	 * @param ddbParam
	 * @return
	 */
	public RetArg queryIllegalCODAuditLog(DDBParam ddbParam);
	
	/**
	 * 设置审核状态为“审核通过”
	 * 
	 * @param userId
	 * @param auditLogId
	 * @param auditUserId
	 * @param byRobot
	 * @return
	 */
	public boolean setCODAuditStateToPassed(long userId, long auditLogId, long auditUserId, boolean byRobot);
	
	/**
	 * 设置审核状态为“审核拒绝”
	 * 
	 * @param userId
	 * @param auditLogId
	 * @param auditUserId
	 * @param extInfo
	 * @return
	 */
	public boolean setCODAuditStateToRefused(long userId, long auditLogId, long auditUserId, String extInfo);
	
	/**
	 * 设置审核状态为“已取消”
	 * 
	 * @param userId
	 * @param auditLogId
	 * @param auditUserId
	 * @param extInfo
	 * @param byRobot
	 * @return
	 */
	public boolean setCODAuditStateToCanceled(long userId, long auditLogId, long auditUserId, String extInfo, boolean byRobot);
	
	/**
	 * 设置审核状态为“已取消”
	 * 
	 * @param auditLog
	 * @param extInfo
	 * @return
	 */
	public boolean cancelCODLogOfWaitingAudit(CODAuditLogDTO auditLog, String extInfo);
	
	/**
	 * 重新设置审核状态为“等待审核”
	 * 
	 * @param userId
	 * @param auditLogId
	 * @param auditUserId
	 * @return
	 */
	public boolean cancelCODAuditStateToWaiting(long userId, long auditLogId, long auditUserId);
	
	/**
	 * 添加到用户黑名单
	 * 
	 * @param userId
	 * @param auditUserId
	 * @return
	 */
	public boolean addToBlacklistUser(long userId, long auditUserId);
	
	/**
	 * 添加到地址黑名单
	 * 
	 * @param auditUserId
	 * @param param
	 * @return
	 */
	public boolean addToBlacklistAddress(long auditUserId, CODWBlistAddressParam param);
	
	/**
	 * 添加到地址白名单
	 * 
	 * @param auditUserId
	 * @param param
	 * @return
	 */
	public boolean addToWhitelistAddress(long auditUserId, CODWBlistAddressParam param);
	
	/**
	 * 系统审核通过
	 * 
	 * @param userId
	 * @param auditLogId
	 * @param robotId
	 * @return
	 */
	public boolean passCODAuditByRobot(long userId, long auditLogId, long robotId);
	
	/**
	 * 客服审核通过
	 * 
	 * @param userId
	 * @param auditLogId
	 * @param auditUserId
	 * @param param
	 * @return
	 */
	public boolean passCODAuditByCustomerService(long userId, long auditLogId, long auditUserId, CODWBlistAddressParam param);
	
	/**
	 * 获取用户黑名单
	 * 
	 * @param userId
	 * @return
	 */
	public CODBlacklistUserDTO queryBlacklistUserByUserId(long userId);
	
	/**
	 * 获取与用户相关的黑名单地址
	 * 
	 * @param userId
	 * @return
	 */
	public List<CODBlacklistAddressDTO> queryBlacklistAddressByUserId(long userId);
	
	/**
	 * 用户Id或者用户的收货地址是否在COD黑名单里
	 * 
	 * @param userId
	 * @param caDTO
	 * @return
	 */
	public boolean isInBlacklist(Long userId, ConsigneeAddressDTO caDTO);
	
	/**
	 * 用户是否在用户黑名单中
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isUserInBlackList(long userId);
	
	/**
	 * 收获地址信息是否在地址黑名单中
	 * 
	 * @param caDTO
	 * @return
	 */
	public boolean isAddressInBlacklist(long userId, ConsigneeAddressDTO caDTO);
	
	/**
	 * 收获地址信息是否在地址黑名单中
	 * 
	 * @param caDTO
	 * @return
	 */
	public boolean isAddressInSpecialBlacklist(long userId, ConsigneeAddressDTO caDTO, List<CODBlacklistAddressDTO> blackList);
	
	/**
	 * 获取与用户相关的白名单地址
	 * 
	 * @param userId
	 * @return
	 */
	public List<CODWhitelistAddressDTO> queryWhitelistAddressByUserId(long userId);
	
	/**
	 * 收获地址信息是否在地址白名单中
	 * 
	 * @param caDTO
	 * @return
	 */
	public boolean isAddressInWhitelist(long userId, OrderExpInfoDTO expDTO);
	
	/**
	 * 收获地址信息是否在地址白名单中
	 * 
	 * @param caDTO
	 * @return
	 */
	public boolean isAddressInSpecialWhitelist(long userId, OrderExpInfoDTO expDTO, List<CODWhitelistAddressDTO> whiteList);
	
	/**
	 * 站点Id -> count
	 * 
	 * @return
	 */
	public Map<Integer, Long> waitingAuditCount();
	
	/**
	 * 根据用户Id查询用户黑名单信息
	 * 
	 * @param userIdList
	 * @param param
	 * @return
	 */
	public List<CODBlacklistUserDTO> queryBlacklistUserByUserIdList(List<Long> userIdList, DDBParam param);
	
	/**
	 * 根据用户Id查询地址黑名单信息
	 * 
	 * @param userIdList
	 * @param param
	 * @return
	 */
	public List<CODBlacklistAddressDTO> queryBlacklistAddressByUserIdList(List<Long> userIdList, DDBParam param);
	
	/**
	 * 移出用户黑名单
	 * 
	 * @param userId
	 * @return
	 */
	public boolean removeFromBlacklistUser(long userId);
	
	/**
	 * 移出地址黑名单
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	public boolean removeFromBlacklistAddress(long id, long userId);
}
