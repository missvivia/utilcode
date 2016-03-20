package com.xyl.mmall.framework.vo;

import java.io.Serializable;
import java.util.List;

public class BaseJsonListVO implements Serializable {

	private static final long serialVersionUID = -5266912696546343633L;
	
	protected boolean hasNext = false;
	
	protected List<?> list;
		
	public BaseJsonListVO(){
		
	}
	
	public BaseJsonListVO(List<?> list) {
		this.list = list;
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

}
