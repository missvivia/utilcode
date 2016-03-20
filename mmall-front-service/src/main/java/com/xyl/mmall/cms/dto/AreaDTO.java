package com.xyl.mmall.cms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.meta.Area;

/**
 * 销售区域信息
 * 
 * @author hzchaizhf
 * @create 2014年9月16日
 *
 */
public class AreaDTO extends Area {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140916L;
	
	private String name;
	
	/**
	 * 默认构造函数
	 */
	public AreaDTO() {
	}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public AreaDTO(Area obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public String getName() {
		return getAreaName();
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
