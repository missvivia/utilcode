package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;

public class SkuSaveParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1494962144003641648L;

	/** skuId */
	private long id;

	/** 条形码 */
	private String barCode;

	/** 如果商品尺码选择的是尺码模板 此处为尺码模板中的尺码id */
	private int sizeIndex;

	/** 如果商品尺码选择自定义，此处自定义尺码值列表 */
	private List<String> customizedSizeValue;

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

	public int getSizeIndex() {
		return sizeIndex;
	}

	public void setSizeIndex(int sizeIndex) {
		this.sizeIndex = sizeIndex;
	}

	public List<String> getCustomizedSizeValue() {
		return customizedSizeValue;
	}

	public void setCustomizedSizeValue(List<String> customizedSizeValue) {
		this.customizedSizeValue = customizedSizeValue;
	}

}
