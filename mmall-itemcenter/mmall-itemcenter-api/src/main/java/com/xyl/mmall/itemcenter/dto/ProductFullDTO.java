package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.Product;
import com.xyl.mmall.itemcenter.meta.ProductDetail;

public class ProductFullDTO extends ProductDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4430272223600497613L;

	/** 是否专柜同款 */
	protected int sameAsShop;

	/** 性别 */
	protected boolean isRecommend;

	/** 无线短标题 */
	protected String wirelessTitle;

	/** 用户富文本框编辑的HTML */
	protected String customEditHTML;

	/** 商品参数值 */
	protected String productParamValue = "[]";

	/** 售卖单位 */
	protected int unit;

	/** 是否航空禁运品 */
	protected int airContraband;

	/** 是否易碎品 */
	protected int fragile;

	/** 是否大件 */
	protected int big;

	/** 是否贵重品 */
	protected int valuables;

	/** 是否消费税 */
	protected int consumptionTax;

	/** 洗涤、使用说明 */
	protected String careLabel;

	/** 商品描述 */
	protected String productDescp;

	/** 配件说明 */
	protected String accessory;

	/** 售后说明 */
	protected String afterMarket;

	/** 产地 */
	protected String producing;

	/** 长 */
	protected String lenth;

	/** 宽 */
	protected String width;

	/** 高 */
	protected String height;

	/** 重量 */
	protected String weight;

	protected int infoFlag;
	
	public ProductFullDTO() {
	}

	public ProductFullDTO(ProductDTO obj, ProductDetail obj2) {
		ReflectUtil.convertObj(this, obj2, false);
		ReflectUtil.convertObj(this, obj, false);
	}

	public ProductFullDTO(Product obj, ProductDetail obj2) {
		ReflectUtil.convertObj(this, obj2, false);
		ReflectUtil.convertObj(this, obj, false);
	}

	public int getInfoFlag() {
		return infoFlag;
	}

	public void setInfoFlag(int infoFlag) {
		this.infoFlag = infoFlag;
	}

	public int getSameAsShop() {
		return sameAsShop;
	}

	public void setSameAsShop(int sameAsShop) {
		this.sameAsShop = sameAsShop;
	}

	public boolean isRecommend() {
		return isRecommend;
	}

	public void setRecommend(boolean isRecommend) {
		this.isRecommend = isRecommend;
	}

	public boolean getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(boolean isRecommend) {
		this.isRecommend = isRecommend;
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

	public String getProductParamValue() {
		return productParamValue;
	}

	public void setProductParamValue(String productParamValue) {
		this.productParamValue = productParamValue;
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

}
