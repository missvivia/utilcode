package com.xyl.mmall.oms.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author hzzhanghui
 *
 */
public class OmsReportListDTO implements Serializable {

	private static final long serialVersionUID = 6321580100716253434L;

	private List<?> list;
	
	private int total;
	
	private boolean hasNext;

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
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
		return "OmsReportListDTO [list=" + list + ", total=" + total + ", hasNext=" + hasNext + "]";
	}
	
}
