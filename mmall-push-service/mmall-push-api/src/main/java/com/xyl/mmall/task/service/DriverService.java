package com.xyl.mmall.task.service;

import java.util.HashMap;

import com.xyl.mmall.task.dto.DeviceLocationDTO;

/**
 * push 管理接口
 * @author jiangww
 *
 */
public interface DriverService {


	/**
	 * 添加或者更改
	 * 
	 * @param userId
	 * @param pushManagementDTO
	 * @return
	 */
	public DeviceLocationDTO addOrUpdateId(DeviceLocationDTO deviceLocationDTO);

	/**
	 * 添加或者更改
	 * 
	 * @param userId
	 * @param pushManagementDTO
	 * @return
	 */
	public HashMap<String,Object> genSign(String account);
}
