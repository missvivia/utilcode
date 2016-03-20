package com.xyl.mmall.task.dto;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.task.meta.PushManagement;

/**
 * ｐｕｓｈ　管理的DTO
 * 
 * @author dingmingliang
 * 
 */
public class PushManagementDTO extends PushManagement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7697734683661147358L;

	private long startTime;
	
	private long endTime;
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public PushManagementDTO(PushManagement obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public PushManagementDTO() {
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
