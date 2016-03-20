package com.xyl.mmall.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Sku的快照信息
 * 
 * @author dingmingliang
 * 
 */
public class SkuSPDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/** 商品sku. */
	private long skuId;
	
	/**
	 * 商品名称
	 */
	private String productName;
	
	/**
	 * 商品副标题
	 */
	private String productTitle;
	
	/**
	 * 起批量
	 */
	private int batchNum;
	
	/**
	 * 商品单位
	 */
	private String unit;
	/**
	 * 商品图片Url
	 */
	private String picUrl;

	/**
	 * 产品链接
	 */
	private String linkUrl;

	/**
	 * 品牌名
	 */
	private String brandName;

	/**
	 * 品牌迷你页链接
	 */
	private String brandLinkUrl;

	/**
	 * 商品类目全名
	 */
	private String categoryFullName;
	/**
	 * 条形码
	 */
	private String prodBarCode;

	/**
	 * 保质期
	 */
	private String expireDate;

	/**
	 * 生产日期
	 */
	private long prodProduceDate;

	/**
	 * 退货政策，1允许，2拒绝.
	 */
	private int canReturn;

	/**
	 * 建议零售价
	 */
	private BigDecimal salePrice;
	
	/**
	 * 用户富文本框编辑的HTML
	 */
	private String CustomEditHTML;
	
	/**
	 * 商品参数详情 json.
	 */
	private String prodParam;
	
	/**
	 * 商品图片图片路径
	 */
	private List<String> picPath;
	
	/**
	 * 商品规格属性
	 */
	private List<SkuSpeciDTO> skuSpeciDTOs;
	
	/**
	 * 商品价格区间.
	 */
	private List<SkuPriceDTO> priceList;
	
	/** 快照链接. */
	private String snapShotUrl;
	
	/** 更新时间. */
	private String updateTime;
	
	/**
	 * POName
	 */
	@Deprecated
	private String poName;
	
	/**
	 * 商品颜色
	 */
	@Deprecated
	private String colorName;
	
	/**
	 * Sku规格描述<br>
	 * Key:规格名称,Value:规格值
	 */
	@Deprecated
	private Map<String, String> skuSpecValueMap;
	
	
	public List<SkuPriceDTO> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<SkuPriceDTO> priceList) {
		this.priceList = priceList;
	}

	public String getBrandLinkUrl() {
		return brandLinkUrl;
	}

	public void setBrandLinkUrl(String brandLinkUrl) {
		this.brandLinkUrl = brandLinkUrl;
	}

	public String getPoName() {
		return poName;
	}

	public void setPoName(String poName) {
		this.poName = poName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public Map<String, String> getSkuSpecValueMap() {
		return skuSpecValueMap;
	}

	public void setSkuSpecValueMap(Map<String, String> skuSpecValueMap) {
		this.skuSpecValueMap = skuSpecValueMap;
	}

	public List<SkuSpeciDTO> getSkuSpeciDTOs() {
		return skuSpeciDTOs;
	}

	public void setSkuSpeciDTOs(List<SkuSpeciDTO> skuSpeciDTOs) {
		this.skuSpeciDTOs = skuSpeciDTOs;
	}

	public int getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(int batchNum) {
		this.batchNum = batchNum;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCategoryFullName() {
		return categoryFullName;
	}

	public void setCategoryFullName(String categoryFullName) {
		this.categoryFullName = categoryFullName;
	}

	public String getProdBarCode() {
		return prodBarCode;
	}

	public void setProdBarCode(String prodBarCode) {
		this.prodBarCode = prodBarCode;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public long getProdProduceDate() {
		return prodProduceDate;
	}

	public void setProdProduceDate(long prodProduceDate) {
		this.prodProduceDate = prodProduceDate;
	}

	public int getCanReturn() {
		return canReturn;
	}

	public void setCanReturn(int canReturn) {
		this.canReturn = canReturn;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public String getCustomEditHTML() {
		return CustomEditHTML;
	}

	public void setCustomEditHTML(String customEditHTML) {
		CustomEditHTML = customEditHTML;
	}

	public String getProdParam() {
		return prodParam;
	}

	public void setProdParam(String prodParam) {
		this.prodParam = prodParam;
	}

	public List<String> getPicPath() {
		return picPath;
	}

	public void setPicPath(List<String> picPath) {
		this.picPath = picPath;
	}

	public String getSnapShotUrl() {
		return snapShotUrl;
	}

	public void setSnapShotUrl(String snapShotUrl) {
		this.snapShotUrl = snapShotUrl;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
