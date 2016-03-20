package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;

public class BatchUploadSizeSku implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4640647891644201422L;

	private int rowNum;

	private String barCode;

	private List<String> sizeValue;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public List<String> getSizeValue() {
		return sizeValue;
	}

	public void setSizeValue(List<String> sizeValue) {
		this.sizeValue = sizeValue;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

}
