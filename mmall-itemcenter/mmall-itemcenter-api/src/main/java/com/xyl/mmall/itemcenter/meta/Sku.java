package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 产品sku meta对象类
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_Sku", desc = "SKU表")
public class Sku implements Serializable {

	private static final long serialVersionUID = -2241250311918807487L;

	/** 主键id */
	@AnnonOfField(desc = "主键id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "供应商id", policy = true)
	private long supplierId;

	/** 归属的产品id */
	@AnnonOfField(desc = "归属的产品id")
	private long productId;

	/** 条形码 */
	@AnnonOfField(desc = "条形码")
	private String barCode;

	/** 尺码索引 */
	@AnnonOfField(desc = "指定尺码模板下的尺码索引")
	private int sizeIndex;

	/** 正品价 */
	@AnnonOfField(desc = "正品价", defa = "0")
	private BigDecimal marketPrice;

	/** 销售价 */
	@AnnonOfField(desc = "销售价", defa = "0")
	private BigDecimal salePrice;

	/** 供货价 */
	@AnnonOfField(desc = "供货价", defa = "0")
	private BigDecimal basePrice;

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

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
}
