package com.xyl.mmall.backend.vo;

import java.io.Serializable;
import java.util.List;

public class BatchUploadSize implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6484029190776007823L;

	private String goodNo;

	private String colorNum;

	private String barCode;

	private int rowNum;

	private List<String> sizeValue;

	public String getGoodNo() {
		return goodNo;
	}

	public void setGoodNo(String goodNo) {
		this.goodNo = goodNo;
	}

	public String getColorNum() {
		return colorNum;
	}

	public void setColorNum(String colorNum) {
		this.colorNum = colorNum;
	}

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

	@Override
	public int hashCode() {
		int result = 1;
		int elm3 = barCode.hashCode();
		result = 31 * result + elm3;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BatchUploadSize other = (BatchUploadSize) obj;
		if (!barCode.equals(other.getBarCode()))
			return false;
		return true;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
}
