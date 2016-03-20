package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;

/**
 * 活动通知的查询对象
 * 条件中不为null的字段会转换成sql条件
 * 注：这个对象的字段名与sql中条件表字段名是一致的
 * @author hzzhaozhenzuo
 *
 */
public class ActiveTellCommonParamDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer tellActiveType;
	
	private Long activeBeginTime;
	
	private Long tellActiveId;
	
	private Long areaId;
	
	private Integer tellTargetType;
	
	private String tellTargetValue;

	public Integer getTellActiveType() {
		return tellActiveType;
	}

	public void setTellActiveType(Integer tellActiveType) {
		this.tellActiveType = tellActiveType;
	}

	public Long getActiveBeginTime() {
		return activeBeginTime;
	}

	public void setActiveBeginTime(Long activeBeginTime) {
		this.activeBeginTime = activeBeginTime;
	}

	public Long getTellActiveId() {
		return tellActiveId;
	}

	public void setTellActiveId(Long tellActiveId) {
		this.tellActiveId = tellActiveId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Integer getTellTargetType() {
		return tellTargetType;
	}

	public void setTellTargetType(Integer tellTargetType) {
		this.tellTargetType = tellTargetType;
	}

	public String getTellTargetValue() {
		return tellTargetValue;
	}

	public void setTellTargetValue(String tellTargetValue) {
		this.tellTargetValue = tellTargetValue;
	}

}
