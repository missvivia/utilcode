package com.xyl.mmall.cms.vo;

import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.saleschedule.meta.Schedule;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class POSortVO {

	private List<String> poIds;

	// here means saleAreaId
	private long curSupplierAreaId;

	private String date;

	private List<Schedule> poOrderList;

	public List<String> getPoIds() {
		return poIds;
	}

	public void setPoIds(List<String> poIds) {
		this.poIds = poIds;
	}

	public long getCurSupplierAreaId() {
		return curSupplierAreaId;
	}

	public void setCurSupplierAreaId(long curSupplierAreaId) {
		this.curSupplierAreaId = curSupplierAreaId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Schedule> getPoOrderList() {
		if (poIds == null) {
			return new ArrayList<Schedule>();
		}
		
		poOrderList = new ArrayList<Schedule>();
		for (int i = 0; i < poIds.size(); i++) {
			Schedule po = new Schedule();
			po.setId(Long.parseLong(poIds.get(i)));
			po.setSaleAreaId(curSupplierAreaId);
			po.setShowOrder(i + 1);
			poOrderList.add(po);
		}
		
		return poOrderList;
	}

//	public void setPoOrderList(List<Schedule> poOrderList) {
//		this.poOrderList = poOrderList;
//	}
}
