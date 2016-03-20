package com.xyl.mmall.oms.dto;

import java.util.Comparator;
import java.util.List;

public class PickOrderBatchDTO implements Comparator<PickOrderBatchDTO> {

	public String title;

	/**
	 * 拣货日期
	 */
	public long pickDay;

	/**
	 * 拣货单列表
	 */
	public List<PickOrderDTO> pickList;

	public long getPickDay() {
		return pickDay;
	}

	public void setPickDay(long pickDay) {
		this.pickDay = pickDay;
	}

	public List<PickOrderDTO> getPickList() {
		return pickList;
	}

	public void setPickList(List<PickOrderDTO> pickList) {
		this.pickList = pickList;
	}

	@Override
	public int compare(PickOrderBatchDTO o1, PickOrderBatchDTO o2) {
		return -(int) (o1.getPickDay() - o2.getPickDay());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
