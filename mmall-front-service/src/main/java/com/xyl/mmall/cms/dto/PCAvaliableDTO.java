package com.xyl.mmall.cms.dto;

import java.util.List;

import com.xyl.mmall.cms.meta.Area;
/**
 * 
 * @author hzliujie
 * 2014年10月27日 下午3:16:32
 */
public class PCAvaliableDTO {
	private long currDate;
	private List<Area> aratList;
	public long getCurrDate() {
		return currDate;
	}
	public void setCurrDate(long currDate) {
		this.currDate = currDate;
	}
	public List<Area> getAratList() {
		return aratList;
	}
	public void setAratList(List<Area> aratList) {
		this.aratList = aratList;
	}
}