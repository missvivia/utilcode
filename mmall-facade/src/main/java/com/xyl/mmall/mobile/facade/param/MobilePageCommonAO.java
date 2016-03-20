package com.xyl.mmall.mobile.facade.param;

import java.io.Serializable;

public class MobilePageCommonAO implements Serializable{
	/**
	 * 
	 */
	private static final int MAX_PAGE_SIZE = 10000;
	private static final long serialVersionUID = -3521515569407374880L;
	
	protected int pageSize = 10;
	//开始ID
	protected long  sinceId;
	//结束ID
	protected long  fromId;
	//时间戳标示
	protected long  timestamp;
		
	//开始位置
	protected int startIndex;
	public int getPageSize() {
		if(pageSize <= 0)
			return MAX_PAGE_SIZE;
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getSinceId() {
		return sinceId;
	}
	public void setSinceId(long sinceId) {
		this.sinceId = sinceId;
	}
	public long getFromId() {
		return fromId;
	}
	public void setFromId(long fromId) {
		this.fromId = fromId;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public boolean isMaxSize(){
		return pageSize <= 0;
	}
}
