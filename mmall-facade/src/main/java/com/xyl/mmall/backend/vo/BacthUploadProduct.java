package com.xyl.mmall.backend.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import com.xyl.mmall.excelparse.ExcelBool;
import com.xyl.mmall.excelparse.ExcelField;
import com.xyl.mmall.excelparse.ExcelSex;
import com.xyl.mmall.excelparse.ExcelUnit;
import com.xyl.mmall.itemcenter.param.BatchUploadSizeParam;

/**
 * @author chengximing
 *
 */
public class BacthUploadProduct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9141042055248069468L;

	private int rowNum = 0;

	@ExcelField(cellIndex = 0, title = "品牌", desc = "品牌名称", max = "32")
	private String brandName;
	
	/** 最低层类目id */
	@ExcelField(cellIndex = 1, title = "最低层类目id", desc = "请按商品类目表【填写三级类目ID】")
	private long lowCategoryId;
	
	/** 表明货号，用于实现不同颜色产品在详情页的切换功能 */
	@ExcelField(cellIndex = 2, title = "货号", desc = "商品原始货号【不含色号】", max = "32")
	private String goodsNo;

	/** 条形码 */
	@ExcelField(cellIndex = 3, title = "条形码", desc = "商品条形码【长度限制32个字符内】", max = "32")
	private String barCode;

	/** 商品名称 */
	@ExcelField(cellIndex = 4, title = "商品名称 ", desc = "商品名称【30字以内】", max = "30")
	private String productName;

	/** 无线短标题 */
	@ExcelField(cellIndex = 5, title = "无线短标题 ", desc = "移动端展示的商品标题【20字以内】", max = "20")
	private String wirelessTitle;

	/** 商品颜色 */
	@ExcelField(cellIndex = 6, title = "商品颜色", desc = "颜色", max = "12")
	private String colorName;

	/** 商品色号 */
	@ExcelField(cellIndex = 7, title = "商品色号", desc = "商家对商品颜色自定义的色号", max = "12")
	private String colorNum;
	
	/** 商品规格 */
	@ExcelField(cellIndex = 8, title = "商品规格", desc = "按实际情况填，若均码、无码等情况，请填“均码”二字。若有XXX码，请按3XL的格式填写。", max = "10")
	private String spec;

	/** 正品价 */
	@ExcelField(cellIndex = 9, title = "正品价", desc = "商品吊牌价", max = "9999999")
	private BigDecimal marketPrice;

	/** 销售价 */
	@ExcelField(cellIndex = 10, title = "销售价 ", desc = "折后售卖价", max = "9999999")
	private BigDecimal salePrice;
	
	/** 是否专柜同款 */
	@ExcelField(cellIndex = 11, title = "是否专柜同款 ", desc = "是否专柜同款")
	private ExcelBool sameAsShop;

	/** 产地 */
	@ExcelField(cellIndex = 12, title = "产地", desc = "产地", max = "180")
	private String producing;
	
	/** 洗涤、使用说明 */
	@ExcelField(cellIndex = 13, title = "洗涤、使用说明", desc = "洗涤说明/使用说明", max = "180")
	private String careLabel;

	/** 商品描述 */
	@ExcelField(cellIndex = 14, title = "商品描述", desc = "商品（卖点）描述【180字以内】", max = "180")
	private String productDescp;

	/** 配件说明 */
	@ExcelField(cellIndex = 15, required = false, title = "配件说明", desc = "配件备注【180字以内】", max = "180")
	private String accessory;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	/** 附加的可变属性 */
	private Map<Long, String> properties;

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

	public ExcelBool getSameAsShop() {
		return sameAsShop;
	}

	public void setSameAsShop(ExcelBool sameAsShop) {
		this.sameAsShop = sameAsShop;
	}

	public Map<Long, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<Long, String> properties) {
		this.properties = properties;
	}

	@Override
	public int hashCode() {
		int result = 1;
		int elm3 = barCode.hashCode();
		result = 31 * result + elm3;
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
		BacthUploadProduct other = (BacthUploadProduct) obj;
		if (!barCode.equals(other.getBarCode()))
			return false;
		return true;
	}

}
