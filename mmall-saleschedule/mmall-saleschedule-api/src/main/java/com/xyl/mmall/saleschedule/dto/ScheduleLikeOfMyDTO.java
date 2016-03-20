package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用于显示我关注的活动的信息对象
 * 用于：【我的关注活动】显示
 * @author hzzhaozhenzuo
 *
 */
public class ScheduleLikeOfMyDTO implements Serializable{
	
	private static final long serialVersionUID = -7815434963895073834L;

	private long scheduleId;
	
	//我的关注活动窗口展示的图片,从scheduleBanner的主图中找
	private String img;
	
	private BigDecimal minDiscount;
	
	private long startTime;
	
	private long endTime;
	
	private String activeTitle;
	
	//用户关注的的活动状态,0:未开始,1:已开始,2:已结束
	private int activeStatus;
	
	//档期活动未开始时,显示的logo图
	private String logoImg;
	
	//是否可以访问
	private boolean canAccess;
	
	//活动还剩几天
	private int remain;

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public BigDecimal getMinDiscount() {
		return minDiscount;
	}

	public void setMinDiscount(BigDecimal minDiscount) {
		this.minDiscount = minDiscount;
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

	public String getActiveTitle() {
		return activeTitle;
	}

	public void setActiveTitle(String activeTitle) {
		this.activeTitle = activeTitle;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}

	public boolean isCanAccess() {
		return canAccess;
	}

	public void setCanAccess(boolean canAccess) {
		this.canAccess = canAccess;
	}

	public int getRemain() {
		return remain;
	}

	public void setRemain(int remain) {
		this.remain = remain;
	}

}
