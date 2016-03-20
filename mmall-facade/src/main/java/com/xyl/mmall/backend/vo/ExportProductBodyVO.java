package com.xyl.mmall.backend.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xyl.mmall.excelparse.ExcelBool;
import com.xyl.mmall.excelparse.ExcelExportField;
import com.xyl.mmall.excelparse.ExcelUnit;
import com.xyl.mmall.framework.vo.SimpleIdValuePaire;

public class ExportProductBodyVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3561968540288138523L;

	private long superCategoryId;
	
	@ExcelExportField(cellIndex = 0)
	private String brandName;
	
	@ExcelExportField(cellIndex = 1)
	private long lowCategoryId;
	
	/** 表明货号，用于实现不同颜色产品在详情页的切换功能 */
	@ExcelExportField(cellIndex = 2, desc = "货号")
	private String goodsNo;

	/** 条形码 */
	@ExcelExportField(cellIndex = 3, desc = "条形码")
	private String barCode;

	/** 商品名称 */
	@ExcelExportField(cellIndex = 4, desc = "商品名称 ")
	private String productName;

	/** 无线短标题 */
	@ExcelExportField(cellIndex = 5, desc = "无线短标题 ")
	private String wirelessTitle;

	/** 商品颜色 */
	@ExcelExportField(cellIndex = 6, desc = "商品颜色")
	private String colorName;

	/** 商品色号 */
	@ExcelExportField(cellIndex = 7, desc = "商品色号")
	private String colorNum;
	
	/** 商品规格 */
	@ExcelExportField(cellIndex = 8, desc = "商品规格")
	private String spec;

	/** 正品价 */
	@ExcelExportField(cellIndex = 9, desc = "正品价")
	private BigDecimal marketPrice;

	/** 销售价 */
	@ExcelExportField(cellIndex = 10, desc = "销售价 ")
	private BigDecimal salePrice;

	/** 是否专柜同款 */
	@ExcelExportField(cellIndex = 11, desc = "是否专柜同款 ")
	private ExcelBool sameAsShop;
	
	/** 产地 */
	@ExcelExportField(cellIndex = 12, desc = "产地")
	private String producing;

	/** 洗涤、使用说明 */
	@ExcelExportField(cellIndex = 13, desc = "洗涤、使用说明")
	private String careLabel;

	/** 商品描述 */
	@ExcelExportField(cellIndex = 14, desc = "商品描述")
	private String productDescp;

	/** 配件说明 */
	@ExcelExportField(cellIndex = 15, desc = "配件说明")
	private String accessory;

	/** 年份 */
	@ExcelExportField(cellIndex = 16)
	private String year;

	/** 季节 */
	@ExcelExportField(cellIndex = 17)
	private String season;

	/** 适用性别 */
	@ExcelExportField(cellIndex = 18)
	private String sex;

	/** 成分含量 */
	@ExcelExportField(cellIndex = 19)
	private String component;

	@ExcelExportField(cellIndex = 20)
	private String filler;
	
	public long getSuperCategoryId() {
		return superCategoryId;
	}

	public void setSuperCategoryId(long superCategoryId) {
		this.superCategoryId = superCategoryId;
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

	public String getWirelessTitle() {
		return wirelessTitle;
	}

	public void setWirelessTitle(String wirelessTitle) {
		this.wirelessTitle = wirelessTitle;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getColorNum() {
		return colorNum;
	}

	public void setColorNum(String colorNum) {
		this.colorNum = colorNum;
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

	public ExcelBool getSameAsShop() {
		return sameAsShop;
	}

	public void setSameAsShop(ExcelBool sameAsShop) {
		this.sameAsShop = sameAsShop;
	}



	public String getCareLabel() {
		return careLabel;
	}

	public void setCareLabel(String careLabel) {
		this.careLabel = careLabel;
	}

	public String getProductDescp() {
		return productDescp;
	}

	public void setProductDescp(String productDescp) {
		this.productDescp = productDescp;
	}

	public String getAccessory() {
		return accessory;
	}

	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}

	
	public String getProducing() {
		return producing;
	}

	public void setProducing(String producing) {
		this.producing = producing;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	public long getLowCategoryId() {
		return lowCategoryId;
	}

	public void setLowCategoryId(long lowCategoryId) {
		this.lowCategoryId = lowCategoryId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

}
