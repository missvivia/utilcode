package com.xyl.mmall.task.service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.task.dto.PushManagementDTO;
import com.xyl.mmall.task.meta.PushManagement;

/**
 * push 管理接口
 * @author jiangww
 *
 */
public interface PushManagementService {
	
	/**
	 * 根据条件筛选
	 * 
	 * @param pushManagementAo
	 * @return
	 */
	public RetArg getPushConfigList(PushManagementDTO pushManagementAo,DDBParam param);
	
	/**
	 * 读取某条具体的记录
	 * 
	 * @param id
	 * @return
	 */
	public PushManagementDTO getPushConfigById(long id);

	/**
	 * 添加一个收货地址信息
	 * 
	 * @param userId
	 * @param pushManagementDTO
	 * @return
	 */
	public PushManagementDTO addPushConfig(PushManagement pushManagement);

	/**
	 * 根据id删除一个条发送记录
	 * 
	 * @param id
	 * @return
	 */
	public boolean deletePushConfigById(long id);

	/**
	 * 更新
	 * 
	 * @param userId
	 * @param pushManagementDTO
	 * @return
	 */
	public boolean updatePushManagement(long id, PushManagementDTO pushManagementDTO);

	
}
