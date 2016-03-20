package com.xyl.mmall.cms.vo;

import java.math.BigDecimal;

import com.xyl.mmall.excelparse.ExcelExportField;

public class ExportMaterialVO {
	@ExcelExportField(cellIndex = 0, desc = "po单编号")
	private long poId;

	@ExcelExportField(cellIndex = 1, desc = "商品货号")
	private String goodsNo;

	@ExcelExportField(cellIndex = 2, desc = "条形码")
	private String barCode;

	@ExcelExportField(cellIndex = 3, desc = "商品名称")
	private String productName;

	@ExcelExportField(cellIndex = 4, desc = "商品颜色")
	private String colorName;

	@ExcelExportField(cellIndex = 5, desc = "商品尺码")
	private String size;

	@ExcelExportField(cellIndex = 6, desc = "正品价")
	private BigDecimal marketPrice;

	@ExcelExportField(cellIndex = 7, desc = "销售价")
	private BigDecimal salePrice;

	@ExcelExportField(cellIndex = 8, desc = "销售价")
	private BigDecimal basePrice;

	@ExcelExportField(cellIndex = 9, desc = "总供货量")
	private int num;

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
