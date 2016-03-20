package com.xyl.mmall.framework.vo;

import java.io.Serializable;
import java.util.List;

public class BaseJsonListResultVO implements Serializable {

	private static final long serialVersionUID = -5266912696546343633L;
	
	private int total;
	
	protected boolean hasNext;
	
	protected List<?> list;
	
	private long lastId;
	
	public BaseJsonListResultVO(){
		
	}
	
	// must set the total number
	public BaseJsonListResultVO(List<?> list) {
		this.list = list;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public long getLastId() {
		return lastId;
	}

	public void setLastId(long lastId) {
		this.lastId = lastId;
	}
}
