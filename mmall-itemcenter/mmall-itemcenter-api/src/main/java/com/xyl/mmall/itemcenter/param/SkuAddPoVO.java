package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;

/**
 * 档期添加商品的sku信息接收类
 * 
 * @author hzhuangluqian
 *
 */
public class SkuAddPoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2873681860269987645L;

	/** skuId */
	private long skuId;

	/** 添加的数量 */
	private int addNum;

	/** 品牌商供货的数量 */
	private int supplyAddNum;

	/** 条形码 */
	private String barCode;
	
	private int rowNum;

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public int getAddNum() {
		return addNum;
	}

	public void setAddNum(int addNum) {
		this.addNum = addNum;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getSupplyAddNum() {
		return supplyAddNum;
	}

	public void setSupplyAddNum(int supplyAddNum) {
		this.supplyAddNum = supplyAddNum;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
}
