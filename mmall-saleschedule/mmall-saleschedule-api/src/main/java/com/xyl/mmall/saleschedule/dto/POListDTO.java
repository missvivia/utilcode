package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * 档期列表
 * 
 * @author hzzhanghui
 * 
 */
public class POListDTO implements Serializable {

	private static final long serialVersionUID = -2999243141294018212L;

	private List<PODTO> poList = new ArrayList<PODTO>();

	private int total = 0;

	private boolean hasNext = false;
	
	public List<PODTO> getPoList() {
		return poList;
	}

	public void setPoList(List<PODTO> poList) {
		this.poList = poList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	@Override
	public String toString() {
		return "POListDTO [poList=" + poList + ", total=" + total + ", hasNext=" + hasNext + "]";
	}

}
