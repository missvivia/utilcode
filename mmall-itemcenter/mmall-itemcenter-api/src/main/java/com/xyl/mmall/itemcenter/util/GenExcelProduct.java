package com.xyl.mmall.itemcenter.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.xyl.mmall.excelparse.ExcelBool;
import com.xyl.mmall.excelparse.ExcelField;
import com.xyl.mmall.excelparse.ExcelSex;
import com.xyl.mmall.excelparse.ExcelUnit;

public class GenExcelProduct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9141042055248069468L;

	public static int OFFSET = 30;

	/** 表明货号，用于实现不同颜色产品在详情页的切换功能 */
	@ExcelField(cellIndex = 0, title = "货号", desc = "商品原始货号【不含色号】")
	private String goodsNo;

	/** 条形码 */
	@ExcelField(cellIndex = 1, title = "条形码", desc = "商品条形码【长度限制32个字符内】")
	private String barCode;

	/** 商品名称 */
	@ExcelField(cellIndex = 2, title = "商品名称 ", desc = "商品名称【30字以内】")
	private String productName;

	/** 无线短标题 */
	@ExcelField(cellIndex = 3, required = false, title = "无线短标题 ", desc = "移动端展示的商品标题【20字以内】")
	private String wirelessTitle;

	/** 商品规格 */
	@ExcelField(cellIndex = 4, title = "商品规格", desc = "按实际情况填，若均码、无码等情况，请填“均码”二字。若有XXX码，请按3XL的格式填写。")
	private String spec;

	/** 是否显示尺寸图 */
	@ExcelField(cellIndex = 5, required = false, title = "是否显示尺寸图", desc = "是/否【为空则也不显示】")
	private ExcelBool isShowSizePic;

	/** 尺码助手id */
	@ExcelField(cellIndex = 6, required = false, title = "尺码助手id", desc = "请按后台尺码助手编号填写【为空则不显示】")
	private Long sizeAssistId;

	/** 性别 */
	@ExcelField(cellIndex = 7, title = "是否推荐 ", desc = "是/否")
	private ExcelSex sex;

	/** 商品颜色 */
	@ExcelField(cellIndex = 8, title = "商品颜色", desc = "颜色")
	private String colorName;

	/** 商品色号 */
	@ExcelField(cellIndex = 9, title = "商品色号", desc = "商家对商品颜色自定义的色号")
	private String colorNum;

	/** 正品价 */
	@ExcelField(cellIndex = 10, title = "正品价", desc = "商品吊牌价")
	private BigDecimal marketPrice;

	/** 销售价 */
	@ExcelField(cellIndex = 11, title = "销售价 ", desc = "折后售卖价")
	private BigDecimal salePrice;

	/** 供货价 */
	@ExcelField(cellIndex = 12, required = false, title = "供货价", desc = "采购成本价")
	private BigDecimal basePrice;


	/** 售卖单位 */
	@ExcelField(cellIndex = 13, title = "售卖单位", desc = "选择有效单位：台、双、本、支、片、个、套、件、副、束、盒")
	private ExcelUnit unit;

	/** 最低层类目id */
	@ExcelField(cellIndex = 14, title = "最低层类目id", desc = "请按商品类目表【填写三级类目ID】")
	private long lowCategoryId;

	/** 是否专柜同款 */
	@ExcelField(cellIndex = 15, title = "是否专柜同款 ", desc = "是/否")
	private ExcelBool sameAsShop;

	/** 是否航空禁运品 */
	@ExcelField(cellIndex = 16, title = "是否航空禁运品", desc = "是/否")
	private ExcelBool airContraband;

	/** 是否易碎品 */
	@ExcelField(cellIndex = 17, title = "是否易碎品", desc = "是/否")
	private ExcelBool fragile;

	/** 是否大件 */
	@ExcelField(cellIndex = 18, title = "是否大件", desc = "是/否")
	private ExcelBool big;

	/** 是否贵重品 */
	@ExcelField(cellIndex = 19, title = "是否贵重品", desc = "是/否")
	private ExcelBool valuables;

	/** 是否消费税 */
	@ExcelField(cellIndex = 20, required = false, title = "是否消费税", desc = "是/否")
	private ExcelBool consumptionTax;

	/** 洗涤、使用说明 */
	@ExcelField(cellIndex = 21, required = false, title = "洗涤、使用说明", desc = "洗涤说明/使用说明")
	private String careLabel;

	/** 商品描述 */
	@ExcelField(cellIndex = 22, required = false, title = "商品描述", desc = "商品（卖点）描述【180字以内】")
	private String productDescp;

	/** 配件说明 */
	@ExcelField(cellIndex = 23, required = false, title = "配件说明", desc = "配件备注【180字以内】")
	private String accessory;

	/** 售后说明 */
	@ExcelField(cellIndex = 24, required = false, title = "售后说明", desc = "售后服务说明【180字以内】")
	private String afterMarket;

	/** 产地 */
	@ExcelField(cellIndex = 25, required = false, title = "产地", desc = "产地")
	private String producing;

	/** 长 */
	@ExcelField(cellIndex = 26, required = false, title = "长", desc = "只以CM为单位填写数字")
	private String lenth;

	/** 宽 */
	@ExcelField(cellIndex = 27, required = false, title = "宽", desc = "只以CM为单位填写数字")
	private String width;

	/** 高 */
	@ExcelField(cellIndex = 28, required = false, title = "高", desc = "只以CM为单位填写数字")
	private String height;

	/** 重量 */
	@ExcelField(cellIndex = 29, required = false, title = "重量", desc = "只以KG为单位填写数字")
	private String weight;

	public long getLowCategoryId() {
		return lowCategoryId;
	}

	public void setLowCategoryId(long lowCategoryId) {
		this.lowCategoryId = lowCategoryId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getColorNum() {
		return colorNum;
	}

	public void setColorNum(String colorNum) {
		this.colorNum = colorNum;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getWirelessTitle() {
		return wirelessTitle;
	}

	public void setWirelessTitle(String wirelessTitle) {
		this.wirelessTitle = wirelessTitle;
	}

	public Long getSizeAssistId() {
		return sizeAssistId;
	}

	public void setSizeAssistId(Long sizeAssistId) {
		this.sizeAssistId = sizeAssistId;
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

	public String getAfterMarket() {
		return afterMarket;
	}

	public void setAfterMarket(String afterMarket) {
		this.afterMarket = afterMarket;
	}

	public String getProducing() {
		return producing;
	}

	public void setProducing(String producing) {
		this.producing = producing;
	}

	public String getLenth() {
		return lenth;
	}

	public void setLenth(String lenth) {
		this.lenth = lenth;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public ExcelBool getIsShowSizePic() {
		return isShowSizePic;
	}

	public void setIsShowSizePic(ExcelBool isShowSizePic) {
		this.isShowSizePic = isShowSizePic;
	}

	public ExcelSex getSex() {
		return sex;
	}

	public void setSex(ExcelSex sex) {
		this.sex = sex;
	}

	public ExcelUnit getUnit() {
		return unit;
	}

	public void setUnit(ExcelUnit unit) {
		this.unit = unit;
	}

	public ExcelBool getSameAsShop() {
		return sameAsShop;
	}

	public void setSameAsShop(ExcelBool sameAsShop) {
		this.sameAsShop = sameAsShop;
	}

	public ExcelBool getAirContraband() {
		return airContraband;
	}

	public void setAirContraband(ExcelBool airContraband) {
		this.airContraband = airContraband;
	}

	public ExcelBool getFragile() {
		return fragile;
	}

	public void setFragile(ExcelBool fragile) {
		this.fragile = fragile;
	}

	public ExcelBool getBig() {
		return big;
	}

	public void setBig(ExcelBool big) {
		this.big = big;
	}

	public ExcelBool getValuables() {
		return valuables;
	}

	public void setValuables(ExcelBool valuables) {
		this.valuables = valuables;
	}

	public ExcelBool getConsumptionTax() {
		return consumptionTax;
	}

	public void setConsumptionTax(ExcelBool consumptionTax) {
		this.consumptionTax = consumptionTax;
	}

}
