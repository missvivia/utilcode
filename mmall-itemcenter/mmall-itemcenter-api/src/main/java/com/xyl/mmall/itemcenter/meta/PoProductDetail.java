package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(tableName = "Mmall_ItemCenter_PoProductDetail", desc = "po商品详情")
public class PoProductDetail implements Serializable {

	private static final long serialVersionUID = 3218507521911447002L;

	@AnnonOfField(desc = "主键", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "档期id", policy = true)
	private long poId;

	@AnnonOfField(desc = "商品id")
	private long productId;

	/** 无线短标题 */
	@AnnonOfField(desc = "无线短标题", notNull = false)
	private String wirelessTitle = "";

	/** 售卖单位 */
	@AnnonOfField(desc = "售卖单位", defa = "0")
	private int unit = 0;

	/** 是否航空禁运品 */
	@AnnonOfField(desc = "是否航空禁运品", defa = "0")
	private int airContraband = 0;

	/** 是否大件 */
	@AnnonOfField(desc = "是否大件", defa = "0")
	private int big;

	/** 是否易碎品 */
	@AnnonOfField(desc = "是否易碎品", defa = "0")
	private int fragile = 0;

	/** 是否贵重品 */
	@AnnonOfField(desc = "是否贵重品", defa = "0")
	private int valuables = 0;

	/** 是否消费税 */
	@AnnonOfField(desc = "是否消费税 ", notNull = false)
	private int consumptionTax;

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

	/** 用户富文本框编辑的HTML */
	@AnnonOfField(desc = "用户富文本框编辑的HTML")
	private String customEditHTML;

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

	public String getWirelessTitle() {
		return wirelessTitle;
	}

	public void setWirelessTitle(String wirelessTitle) {
		this.wirelessTitle = wirelessTitle;
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

	public String getCustomEditHTML() {
		return customEditHTML;
	}

	public void setCustomEditHTML(String customEditHTML) {
		this.customEditHTML = customEditHTML;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

}
