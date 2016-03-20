package com.xyl.mmall.backend.vo;

/**
 * 尺码选择尺码模板或者默认尺码时候的简单SKU信息
 * 
 * @author hzhuangluqian
 *
 */
public class TmplSizeSimpleSKU {
	/** 条形码 */
	private String barCode;

	/** 尺码id */
	private int sizeId;

	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getSizeId() {
		return sizeId;
	}

	public void setSizeId(int sizeId) {
		this.sizeId = sizeId;
	}

}
