package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.meta.Sku;

public class BaseSkuDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4618712465351423844L;

	protected long id;

	protected long supplierId;
	
	protected String storeName;

	/**
	 * 品牌id
	 */
	protected long brandId;

	/** 归属的产品id */
	protected long productId;

	/** 条形码 */
	protected String barCode;

	/** 尺码索引 */
	protected int sizeIndex;

	protected String goodsNo;

	protected String colorNum;
	/** 正品价 */
	protected BigDecimal marketPrice;

	/** 销售价 */
	protected BigDecimal salePrice;

	/** 供货价 */
	protected BigDecimal basePrice;

	/** 所属的商品名 */
	protected String productName;

	/** 所属的商品颜色 */
	protected String colorName;

	/** 尺码 */
	protected String size;

	/** 缩略图地址 */
	protected String thumb;

	public BaseSkuDTO() {
	}

	public BaseSkuDTO(PoSku obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public BaseSkuDTO(Sku obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getSizeIndex() {
		return sizeIndex;
	}

	public void setSizeIndex(int sizeIndex) {
		this.sizeIndex = sizeIndex;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
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

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getColorNum() {
		return colorNum;
	}

	public void setColorNum(String colorNum) {
		this.colorNum = colorNum;
	}

}
