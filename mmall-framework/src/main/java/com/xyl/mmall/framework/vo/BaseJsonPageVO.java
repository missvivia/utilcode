package com.xyl.mmall.framework.vo;

import java.io.Serializable;
import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;

public class BaseJsonPageVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7058992713801800310L;
	protected int limit=10;
	protected int offset=0;
	protected boolean hasNext = false;
	protected Integer totalCount;

	protected List<?> list;

	public BaseJsonPageVO() {
	}

	public BaseJsonPageVO(List<?> list, DDBParam ddbParam) {
		this.list = list;
		if (ddbParam != null) {
			this.limit = ddbParam.getLimit()== 0?10:ddbParam.getLimit();
			this.offset = ddbParam.getOffset();
			this.totalCount = ddbParam.getTotalCount()!= null ?ddbParam.getTotalCount():0;
			this.hasNext = this.limit + this.offset >= this.totalCount ? false : true;
		}

	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

}
