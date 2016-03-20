package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(tableName = "TB_ItemCenter_ProductDetail", desc = "商品详情")
public class ProductDetail implements Serializable {

	private static final long serialVersionUID = 4881972785726665833L;

	@AnnonOfField(desc = "主键", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "商品id")
	private long productId;

	@AnnonOfField(desc = "供应商id", policy = true)
	private long supplierId;

	/** 是否专柜同款 */
	@AnnonOfField(desc = "是否专柜同款")
	private int sameAsShop;

	/** 无线短标题 */
	@AnnonOfField(desc = "无线短标题")
	private String wirelessTitle = "";

	/** 性别 */
	@AnnonOfField(desc = "是否推荐")
	private boolean isRecommend;

	/** 售卖单位 */
	@AnnonOfField(desc = "售卖单位")
	private int unit;

	/** 是否航空禁运品 */
	@AnnonOfField(desc = "是否航空禁运品")
	private int airContraband;

	/** 是否易碎品 */
	@AnnonOfField(desc = "是否易碎品")
	private int fragile;

	/** 是否大件 */
	@AnnonOfField(desc = "是否大件", defa = "0")
	private int big;

	/** 是否贵重品 */
	@AnnonOfField(desc = "是否贵重品")
	private int valuables;

	/** 是否消费税 */
	@AnnonOfField(desc = "是否消费税 ", notNull = false)
	private int consumptionTax;

	/** 用户富文本框编辑的HTML */
	@AnnonOfField(desc = "用户富文本框编辑的HTML")
	private String customEditHTML;

	/** 洗涤、使用说明 */
	@AnnonOfField(desc = "洗涤、使用说明 ", notNull = false, type = "VARCHAR(360)")
	private String careLabel;

	/** 商品描述 */
	@AnnonOfField(desc = "商品描述", notNull = false, type = "VARCHAR(360)")
	private String productDescp;

	/** 配件说明 */
	@AnnonOfField(desc = "配件说明 ", notNull = false, type = "VARCHAR(360)")
	private String accessory;

	/** 售后说明 */
	@AnnonOfField(desc = "售后说明 ", notNull = false, type = "VARCHAR(360)")
	private String afterMarket;

	/** 产地 */
	@AnnonOfField(desc = "产地", notNull = false, type = "VARCHAR(360)")
	private String producing;

	/** 长 */
	@AnnonOfField(desc = "长 ", notNull = false, type = "VARCHAR(5)")
	private String lenth;

	/** 宽 */
	@AnnonOfField(desc = "宽 ", notNull = false, type = "VARCHAR(5)")
	private String width;

	/** 高 */
	@AnnonOfField(desc = "高 ", notNull = false, type = "VARCHAR(5)")
	private String height;

	/** 重量 */
	@AnnonOfField(desc = "重量 ", notNull = false, type = "VARCHAR(5)")
	private String weight;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public boolean isRecommend() {
		return isRecommend;
	}

	public void setRecommend(boolean isRecommend) {
		this.isRecommend = isRecommend;
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

	public String getCustomEditHTML() {
		return customEditHTML;
	}

	public void setCustomEditHTML(String customEditHTML) {
		this.customEditHTML = customEditHTML;
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

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

}
