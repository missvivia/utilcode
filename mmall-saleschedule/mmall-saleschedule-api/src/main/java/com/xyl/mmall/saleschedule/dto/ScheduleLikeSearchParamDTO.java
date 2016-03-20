package com.xyl.mmall.saleschedule.dto;

public class ScheduleLikeSearchParamDTO extends SearchBaseParamDTO{

	private static final long serialVersionUID = 1L;
	
	private Long userId;
	
	private Long areaId;
	
	private Long scheduleStartTime;
	
	private Long scheduleEndTime;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getScheduleStartTime() {
		return scheduleStartTime;
	}

	public void setScheduleStartTime(Long scheduleStartTime) {
		this.scheduleStartTime = scheduleStartTime;
	}

	public Long getScheduleEndTime() {
		return scheduleEndTime;
	}

	public void setScheduleEndTime(Long scheduleEndTime) {
		this.scheduleEndTime = scheduleEndTime;
	}

}
