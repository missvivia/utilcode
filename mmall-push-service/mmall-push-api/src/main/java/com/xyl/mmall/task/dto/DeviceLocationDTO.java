package com.xyl.mmall.task.dto;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.task.meta.DeviceLocation;

/**
 * ｐｕｓｈ　管理的DTO
 * 
 * @author dingmingliang
 * 
 */
public class DeviceLocationDTO extends DeviceLocation{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1597449607089151927L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public DeviceLocationDTO(DeviceLocation obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public DeviceLocationDTO() {
	}

}
