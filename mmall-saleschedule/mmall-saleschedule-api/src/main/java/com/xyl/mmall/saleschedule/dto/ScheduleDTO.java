package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;

/**
 * 档期DTO
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleDTO implements Serializable {

	private static final long serialVersionUID = -3115821976847180414L;

	private Schedule schedule;

	private ScheduleVice scheduleVice;

	private List<ScheduleSiteRela> siteRelaList = new ArrayList<ScheduleSiteRela>();

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public ScheduleVice getScheduleVice() {
		return scheduleVice;
	}

	public void setScheduleVice(ScheduleVice scheduleVice) {
		this.scheduleVice = scheduleVice;
	}

	public List<ScheduleSiteRela> getSiteRelaList() {
		return siteRelaList;
	}

	public void setSiteRelaList(List<ScheduleSiteRela> siteRelaList) {
		this.siteRelaList = siteRelaList;
	}

	@Override
	public String toString() {
		return "ScheduleDTO [schedule=" + schedule + ", scheduleVice=" + scheduleVice + ", siteRelaList="
				+ siteRelaList + "]";
	}
}
