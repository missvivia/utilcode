package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.excelparse.ExcelField;

public class BatchUploadPOProd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8873533480618364290L;

	
	/** 条形码 */
	@ExcelField(cellIndex = 0, desc = "条形码")
	private String barCode;

	/** 供货量 */
	@ExcelField(cellIndex = 1, desc = "供货量 ")
	private Integer addNum;

	/** 品牌商供货量 */
	@ExcelField(cellIndex = 2, desc = "供货量 ")
	private Integer supplyNum;
	
	private int rowNum;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Integer getAddNum() {
		return addNum;
	}

	public void setAddNum(Integer addNum) {
		this.addNum = addNum;
	}

	public Integer getSupplyNum() {
		return supplyNum;
	}

	public void setSupplyNum(Integer supplyNum) {
		this.supplyNum = supplyNum;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

}
