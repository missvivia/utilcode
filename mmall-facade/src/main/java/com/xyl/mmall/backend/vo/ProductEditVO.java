package com.xyl.mmall.backend.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.dto.CategoryArchitect;
import com.xyl.mmall.itemcenter.enums.SizeType;

public class ProductEditVO {
	/** 商品id */
	private String productId = "0";

	/** 是否是PO商品编辑 */
	private Map<String, Object> schedule;

	/** 商品名 */
	private String productName;

	private String goodsNo;

	/** 所属品牌id */
	private String brandId = "0";

	/** 所属品牌名 */
	private String brandName;

	/** 增值税率 */
	private BigDecimal addedTax;

	/** 商品描述 */
	private String desp;

	/** 是否推荐 */
	private int isRecommend;

	/** 商品色号 */
	private String colorNum;

	/** 商品颜色 */
	private String colorName;

	/** 是否专柜同款 */
	private int sameAsShop;

	/** 无线短标题 */
	private String wirelessTitle;

	/** 商品尺码类型 */
	private int sizeType;

	/** 商品尺码模板id */
	private String sizeTemplateId = "0";

	/** 尺码助手id */
	private String helperId = null;

	/** 是否显示尺寸图 */
	private int isShowSizePic;

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
	private String length;

	/** 宽 */
	private String width;

	/** 高 */
	private String height;

	/** 重量 */
	private String weight;

	/** 用户富文本框编辑的HTML */
	private String customEditHTML;

	private List<CategoryArchitect> categoryList;

	private List<String> categories;

	private List<CustSizeSimpleSKU> skuList;

	private List<TmplSizeSimpleSKU> skuList2;

	private List<ProductParamVO> productParamList;

	private List<String> prodShowPicList;

	private List<String> listShowPicList;

	private List<String> detailShowPicList;

	private SizeVO template;

	public ProductEditVO() {
		marketPrice = new BigDecimal(0);
		salePrice = new BigDecimal(0);
		basePrice = new BigDecimal(0);
		addedTax = new BigDecimal(17);
		productName = "";
		brandName = "";
		goodsNo = "";
		desp = "";
		colorNum = "";
		colorName = "";
		customEditHTML = "";
		sizeType = SizeType.ORIG_SIZE.getIntValue();
		skuList = new ArrayList<CustSizeSimpleSKU>();
		skuList2 = new ArrayList<TmplSizeSimpleSKU>();
		prodShowPicList = new ArrayList<String>();
		listShowPicList = new ArrayList<String>();
		detailShowPicList = new ArrayList<String>();
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Map<String, Object> getSchedule() {
		return schedule;
	}

	public void setSchedule(Map<String, Object> schedule) {
		this.schedule = schedule;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public BigDecimal getAddedTax() {
		return addedTax;
	}

	public void setAddedTax(BigDecimal addedTax) {
		this.addedTax = addedTax;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

	public int getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(int isRecommend) {
		this.isRecommend = isRecommend;
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

	public int getSameAsShop() {
		return sameAsShop;
	}

	public void setSameAsShop(int sameAsShop) {
		this.sameAsShop = sameAsShop;
	}

	public String getWirelessTitle() {
		return wirelessTitle;
	}

	public void setWirelessTitle(String wirelessTitle) {
		this.wirelessTitle = wirelessTitle;
	}

	public int getSizeType() {
		return sizeType;
	}

	public void setSizeType(int sizeType) {
		this.sizeType = sizeType;
	}

	public String getSizeTemplateId() {
		return sizeTemplateId;
	}

	public void setSizeTemplateId(String sizeTemplateId) {
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

	public int getBig() {
		return big;
	}

	public void setBig(int big) {
		this.big = big;
	}

	public int getFragile() {
		return fragile;
	}

	public void setFragile(int fragile) {
		this.fragile = fragile;
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

	public String getLength() {
		return length;
	}

	public void setLength(String lenth) {
		this.length = lenth;
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

	public String getCustomEditHTML() {
		return customEditHTML;
	}

	public void setCustomEditHTML(String customEditHTML) {
		this.customEditHTML = customEditHTML;
	}

	public List<CategoryArchitect> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryArchitect> categoryList) {
		this.categoryList = categoryList;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<CustSizeSimpleSKU> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<CustSizeSimpleSKU> skuList) {
		this.skuList = skuList;
	}

	public List<TmplSizeSimpleSKU> getSkuList2() {
		return skuList2;
	}

	public void setSkuList2(List<TmplSizeSimpleSKU> skuList2) {
		this.skuList2 = skuList2;
	}

	public List<ProductParamVO> getProductParamList() {
		return productParamList;
	}

	public void setProductParamList(List<ProductParamVO> productParamList) {
		this.productParamList = productParamList;
	}

	public SizeVO getTemplate() {
		return template;
	}

	public void setTemplate(SizeVO template) {
		this.template = template;
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

	public String getHelperId() {
		return helperId;
	}

	public void setHelperId(String helperId) {
		this.helperId = helperId;
	}

	public int getIsShowSizePic() {
		return isShowSizePic;
	}

	public void setIsShowSizePic(int isShowSizePic) {
		this.isShowSizePic = isShowSizePic;
	}
}
