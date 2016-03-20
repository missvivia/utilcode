package com.xyl.mmall.task.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.task.meta.DeviceLocation;
import com.xyl.mmall.task.meta.MobileConfig;

/**
 * ｐｕｓｈ　管理的DTO
 * 
 * @author dingmingliang
 * 
 */
public class MobileConfigDTO extends MobileConfig{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1597449607089151927L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public MobileConfigDTO(MobileConfig obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public MobileConfigDTO() {
	}

}
