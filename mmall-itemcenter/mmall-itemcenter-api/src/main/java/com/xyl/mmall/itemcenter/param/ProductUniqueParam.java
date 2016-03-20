package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;

public class ProductUniqueParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5940938370787876832L;

	private long supplierId;

	private String goodNo;

	private String colorNum;

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

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

}
