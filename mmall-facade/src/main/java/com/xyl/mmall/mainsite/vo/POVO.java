/*
 * @(#) 2014-10-21
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mainsite.vo;

/**
 * POVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-21
 * @since      1.0
 */
public class POVO {
	
	private String imgUrl;
	
	private long id;
	
	private String brandNameEn;
	
	private String brandNameZh;
	
	private int status;
	
	private long nextStartTime;
	
	private long nextEndTime;
	
	private long scheduleId;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBrandNameEn() {
		return brandNameEn;
	}

	public void setBrandNameEn(String brandNameEn) {
		this.brandNameEn = brandNameEn;
	}

	public String getBrandNameZh() {
		return brandNameZh;
	}

	public void setBrandNameZh(String brandNameZh) {
		this.brandNameZh = brandNameZh;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getNextStartTime() {
		return nextStartTime;
	}

	public void setNextStartTime(long nextStartTime) {
		this.nextStartTime = nextStartTime;
	}

	public long getNextEndTime() {
		return nextEndTime;
	}

	public void setNextEndTime(long nextEndTime) {
		this.nextEndTime = nextEndTime;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}
}
