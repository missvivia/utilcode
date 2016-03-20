package com.xyl.mmall.backend.vo;

import com.xyl.mmall.excelparse.ExcelExportField;

public class ExportPoSkuVO {

	@ExcelExportField(cellIndex = 0, desc = "条形码")
	private String barCode;

	@ExcelExportField(cellIndex = 1, desc = "自供货量")
	private int skuNum;

	@ExcelExportField(cellIndex = 2, desc = "品牌商参与供货量")
	private int supplyNum;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}

	public int getSupplyNum() {
		return supplyNum;
	}

	public void setSupplyNum(int supplyNum) {
		this.supplyNum = supplyNum;
	}
}
