/**
 * 
 */
package com.xyl.mmall.content.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * @author hzlihui2014
 *
 */
@AnnonOfClass(tableName = "Mmall_Content_PresentProduct", desc = "活动展示商品表")
public class PresentProduct implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "销售区域")
	private long saleAreaId;

	@AnnonOfField(desc = "展示商品分类")
	private int category;

	@AnnonOfField(desc = "商品图片", type = "VARCHAR(255)")
	private String image;

	@AnnonOfField(desc = "logo图片", type = "VARCHAR(255)")
	private String logo;

	@AnnonOfField(desc = "正品价", defa = "0")
	private BigDecimal marketPrice;

	@AnnonOfField(desc = "销售价", defa = "0")
	private BigDecimal salePrice;

	@AnnonOfField(desc = "排序字段")
	private int orderBy;

	@AnnonOfField(desc = "商品名称", type = "VARCHAR(60)")
	private String productName;

	@AnnonOfField(desc = "商品ID")
	private long productId;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the saleAreaId
	 */
	public long getSaleAreaId() {
		return saleAreaId;
	}

	/**
	 * @param saleAreaId
	 *            the saleAreaId to set
	 */
	public void setSaleAreaId(long saleAreaId) {
		this.saleAreaId = saleAreaId;
	}

	/**
	 * @return the category
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return the marketPrice
	 */
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	/**
	 * @param marketPrice
	 *            the marketPrice to set
	 */
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	/**
	 * @return the salePrice
	 */
	public BigDecimal getSalePrice() {
		return salePrice;
	}

	/**
	 * @param salePrice
	 *            the salePrice to set
	 */
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * @return the orderBy
	 */
	public int getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName
	 *            the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the productId
	 */
	public long getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            the productId to set
	 */
	public void setProductId(long productId) {
		this.productId = productId;
	}

}
