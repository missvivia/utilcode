package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MobileCountDwonVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 676180817586626401L;

	private long id;
	private long endTime;

	private long countDownTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getCountDownTime() {
		return countDownTime;
	}

	public void setCountDownTime(long countDownTime) {
		this.countDownTime = countDownTime;
	}
	
	

}
