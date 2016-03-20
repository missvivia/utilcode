package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SizeTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7341433978505383234L;

	private List<SizeColumnParam> sizeHeader;

	/**
	 * key:recordIndex+columnIndex
	 */
	private Map<String, String> valueMap;

	private List<Long> recordList;
	
	public List<SizeColumnParam> getSizeHeader() {
		return sizeHeader;
	}

	public void setSizeHeader(List<SizeColumnParam> sizeHeader) {
		this.sizeHeader = sizeHeader;
	}

	public Map<String, String> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, String> valueMap) {
		this.valueMap = valueMap;
	}

	public List<Long> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<Long> recordList) {
		this.recordList = recordList;
	}

}
