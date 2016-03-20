/*
 * @(#) 2014-9-26
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.activity;

/**
 * ActivitySchedule.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-26
 * @since      1.0
 */
public class ActivitySchedule {
	
	/**
	 * ID
	 */
	private long id;
	
	/**
	 * 档期名称
	 */
	private String title;
	
	/**
	 * 开始时间
	 */
	private long startTime;

	/**
	 * 站点名称
	 */
	private String areaName;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}
