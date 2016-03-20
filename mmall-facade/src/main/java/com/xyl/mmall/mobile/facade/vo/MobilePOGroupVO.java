package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.mobile.facade.converter.AppVersion;
@JsonInclude(Include.NON_NULL)
public class MobilePOGroupVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 361454552428498602L;
	
	//活动开始时间
	private long activeTime;
	//活动描述，对应UI的明日十点上线
	private String activeDesc;
	//是否正在活动的区域 0 否,1 是
	private int isActive;
	//对应的日专场信息
	private List<MobilePOSummaryVO> poList;
	public long getActiveTime() {
		return activeTime;
	}
	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}
	public String getActiveDesc() {
		return activeDesc;
	}
	public void setActiveDesc(String activeDesc) {
		this.activeDesc = activeDesc;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public List<MobilePOSummaryVO> getPoList() {
		return poList;
	}
	public void setPoList(List<MobilePOSummaryVO> poList) {
		this.poList = poList;
	}
	
}
