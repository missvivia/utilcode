package com.xyl.mmall.activity.fw;


/**
 * 
 * @author hzzhanghui
 *
 */
public class DateRange {
	private long startTime;
	
	private long endTime;
	
	public DateRange(long startTime, long endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public boolean isActivityOngoing() {
		long now = System.currentTimeMillis();
		return (startTime < now) && (now < endTime);
	}
}
