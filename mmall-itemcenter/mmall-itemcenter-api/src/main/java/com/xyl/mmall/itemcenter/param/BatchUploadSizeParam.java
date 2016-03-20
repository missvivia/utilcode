package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;

public class BatchUploadSizeParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8604348876934879680L;

	private String goodNo;

	private String colorNum;

	private List<BatchUploadSizeSku> skuSizeList;

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

	public List<BatchUploadSizeSku> getSkuSizeList() {
		return skuSizeList;
	}

	public void setSkuSizeList(List<BatchUploadSizeSku> skuSizeList) {
		this.skuSizeList = skuSizeList;
	}
	
	@Override
	public int hashCode() {
		int elm1 = goodNo.hashCode();
		int result = 1;
		result = 31 * result + elm1;
		int elm2 = colorNum.hashCode();
		result = 31 * result + elm2;
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
		BatchUploadSizeParam other = (BatchUploadSizeParam) obj;
		if (!goodNo.equals(other.getGoodNo()))
			return false;
		if (!colorNum.equals(other.getColorNum()))
			return false;
		return true;
	}
}
