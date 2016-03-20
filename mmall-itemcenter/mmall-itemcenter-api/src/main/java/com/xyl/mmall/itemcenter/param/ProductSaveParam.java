package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商家平台商品管理中的添加商品参数类
 * 
 * @author hzhuangluqian
 *
 */
public class ProductSaveParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5350988112412423174L;

	private int rowNum = 0;

	private long id;

	private long scheduleId;

	/** 最低层类目id */
	private long lowCategoryId;

	/** 添加的供应商id */
	private long supplierId;

	/** 表明货号，用于实现不同颜色产品在详情页的切换功能 */
	private String goodsNo;

	/** 商品名称 */
	private String productName;

	/** 商品品牌id */
	private long brandId;

	/** 增值税率 */
	private BigDecimal valueAddedTax;

	/** 商品描述 */
	private String descp;

	/** 商品色号 */
	private String colorNum;

	/** 商品颜色 */
	private String colorName;

	/** 是否专柜同款 */
	private int sameAsShop;

	/** 性别 */
	private int isRecommend;

	/** 无线短标题 */
	private String wirelessTitle;

	/** 是否是尺码自定义 */
	private int sizeType;

	/** 尺码模板id */
	private long sizeTemplateId;

	/** 尺码助手id */
	private long sizeAssistId;

	/** 是否显示尺寸图 */
	private boolean isShowSizePic;

	/** 正品价 */
	private BigDecimal marketPrice;

	/** 销售价 */
	private BigDecimal salePrice;

	/** 供货价 */
	private BigDecimal basePrice;

	/** 售卖单位 */
	private int unit;

	/** 是否航空禁运品 */
	private int airContraband;

	/** 是否易碎品 */
	private int fragile;

	/** 是否大件 */
	private int big;

	/** 是否贵重品 */
	private int valuables;

	/** 是否消费税 */
	private int consumptionTax;

	/** 洗涤、使用说明 */
	private String careLabel;

	/** 商品描述 */
	private String productDescp;

	/** 配件说明 */
	private String accessory;

	/** 售后说明 */
	private String afterMarket;

	/** 产地 */
	private String producing;

	/** 长 */
	private String lenth;

	/** 宽 */
	private String width;

	/** 高 */
	private String height;

	/** 重量 */
	private String weight;

	/** 商品展示图片列表 */
	private List<String> prodShowPicList;

	/** 列表展示图片列表 */
	private List<String> listShowPicList;

	/** 细节展示图片列表 */
	private List<String> detailShowPicList;

	/** 用户富文本框编辑的HTML */
	private String customEditHTML = "";

	/** 挂在该商品上的sku列表 */
	private List<SkuSaveParam> SKUList;

	/** 商品尺码自定义字段列表 */
	private List<SizeColumnParam> sizeHeader;

	private List<ProdParamParam> ProductParamList;

	private String nosURL = "";

	private int infoFlag;

	private String showPicPath;

	public String getShowPicPath() {
		return showPicPath;
	}

	public void setShowPicPath(String showPicPath) {
		this.showPicPath = showPicPath;
	}

	public int getInfoFlag() {
		return infoFlag;
	}

	public void setInfoFlag(int infoFlag) {
		this.infoFlag = infoFlag;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public long getLowCategoryId() {
		return lowCategoryId;
	}

	public void setLowCategoryId(long lowCategoryId) {
		this.lowCategoryId = lowCategoryId;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
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

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public BigDecimal getValueAddedTax() {
		return valueAddedTax;
	}

	public void setValueAddedTax(BigDecimal valueAddedTax) {
		this.valueAddedTax = valueAddedTax;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
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

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public int getAirContraband() {
		return airContraband;
	}

	public void setAirContraband(int airContraband) {
		this.airContraband = airContraband;
	}

	public int getFragile() {
		return fragile;
	}

	public void setFragile(int fragile) {
		this.fragile = fragile;
	}

	public int getBig() {
		return big;
	}

	public void setBig(int big) {
		this.big = big;
	}

	public int getValuables() {
		return valuables;
	}

	public void setValuables(int valuables) {
		this.valuables = valuables;
	}

	public int getConsumptionTax() {
		return consumptionTax;
	}

	public void setConsumptionTax(int consumptionTax) {
		this.consumptionTax = consumptionTax;
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

	public int getSizeType() {
		return sizeType;
	}

	public void setSizeType(int sizeType) {
		this.sizeType = sizeType;
	}

	public long getSizeTemplateId() {
		return sizeTemplateId;
	}

	public void setSizeTemplateId(long sizeTemplateId) {
		this.sizeTemplateId = sizeTemplateId;
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

	public List<String> getProdShowPicList() {
		return prodShowPicList;
	}

	public void setProdShowPicList(List<String> prodShowPicList) {
		this.prodShowPicList = prodShowPicList;
	}

	public List<String> getListShowPicList() {
		return listShowPicList;
	}

	public void setListShowPicList(List<String> listShowPicList) {
		this.listShowPicList = listShowPicList;
	}

	public List<String> getDetailShowPicList() {
		return detailShowPicList;
	}

	public void setDetailShowPicList(List<String> detailShowPicList) {
		this.detailShowPicList = detailShowPicList;
	}

	public String getCustomEditHTML() {
		return customEditHTML;
	}

	public void setCustomEditHTML(String customEditHTML) {
		this.customEditHTML = customEditHTML;
	}

	public List<SkuSaveParam> getSKUList() {
		return SKUList;
	}

	public void setSKUList(List<SkuSaveParam> sKUList) {
		SKUList = sKUList;
	}

	public List<SizeColumnParam> getSizeHeader() {
		return sizeHeader;
	}

	public void setSizeHeader(List<SizeColumnParam> sizeHeader) {
		this.sizeHeader = sizeHeader;
	}

	public List<ProdParamParam> getProductParamList() {
		return ProductParamList;
	}

	public void setProductParamList(List<ProdParamParam> productParamList) {
		ProductParamList = productParamList;
	}

	public int getSameAsShop() {
		return sameAsShop;
	}

	public void setSameAsShop(int sameAsShop) {
		this.sameAsShop = sameAsShop;
	}

	public int getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(int isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getWirelessTitle() {
		return wirelessTitle;
	}

	public void setWirelessTitle(String wirelessTitle) {
		this.wirelessTitle = wirelessTitle;
	}

	public long getSizeAssistId() {
		return sizeAssistId;
	}

	public void setSizeAssistId(long sizeAssistId) {
		this.sizeAssistId = sizeAssistId;
	}

	public boolean isShowSizePic() {
		return isShowSizePic;
	}

	public void setShowSizePic(boolean isShowSizePic) {
		this.isShowSizePic = isShowSizePic;
	}

	public boolean getIsShowSizePic() {
		return isShowSizePic;
	}

	public void setIsShowSizePic(boolean isShowSizePic) {
		this.isShowSizePic = isShowSizePic;
	}

	@Override
	public int hashCode() {
		int elm1 = goodsNo.hashCode();
		int result = 1;
		result = 31 * result + elm1;
		int elm2 = colorNum.hashCode();
		result = 31 * result + elm2;
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
		ProductSaveParam other = (ProductSaveParam) obj;
		if (!goodsNo.equals(other.getGoodsNo()))
			return false;
		if (!colorNum.equals(other.getColorNum()))
			return false;
		return true;
	}

	public String getNosURL() {
		return nosURL;
	}

	public void setNosURL(String nosURL) {
		this.nosURL = nosURL;
	}
}
