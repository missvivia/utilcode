package com.xyl.mmall.mobile.web.vo;

public class DetailPOVO {
	private long id;

	private long startTime;

	private long poCountDownTime;

	private String title;
	
	private long[] areaCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getPoCountDownTime() {
		return poCountDownTime;
	}

	public void setPoCountDownTime(long poCountDownTime) {
		this.poCountDownTime = poCountDownTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long[] getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(long[] areaCode) {
		this.areaCode = areaCode;
	}
}
