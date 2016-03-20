package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ProductSKU;

public class ProductSKUDTO extends ProductSKU implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8839653284428740410L;

	/**
	 * 商品类目名称  -全称
	 */
	private String categoryName;
	
	/**
	 * 条形码
	 */
	private String barCode;
	
	/** 缩略图路径  */
	private String showPicPath;
	
	/** 店铺名称. */
	private String storeName;
	
	/** 店铺订单起批金额 . */
	private BigDecimal batchCash; 
	
	/** 商品详情. */
	private ProductSKUDetailDTO detailDTO;
	
	/** 价格列表. */
	private List<ProductPriceDTO> priceList;

	/** 图片列表. */
	private List<ProdPicDTO> picList;

	/** 商品属性列表. */
	private List<ProdParamDTO> paramList;
	
	/** 商品规格列表. */
	private List<ProdSpeciDTO> speciList;
	
	public ProductSKUDTO() {
	}
	
	public ProductSKUDTO(ProductSKU obj){
		ReflectUtil.convertObj(this, obj, false);
	}
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public ProductSKUDetailDTO getDetailDTO() {
		return detailDTO;
	}

	public void setDetailDTO(ProductSKUDetailDTO detailDTO) {
		this.detailDTO = detailDTO;
	}

	public List<ProductPriceDTO> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ProductPriceDTO> priceList) {
		this.priceList = priceList;
	}

	public List<ProdPicDTO> getPicList() {
		return picList;
	}

	public void setPicList(List<ProdPicDTO> picList) {
		this.picList = picList;
	}

	public List<ProdParamDTO> getParamList() {
		return paramList;
	}

	public void setParamList(List<ProdParamDTO> paramList) {
		this.paramList = paramList;
	}

	public List<ProdSpeciDTO> getSpeciList() {
		return speciList;
	}

	public void setSpeciList(List<ProdSpeciDTO> speciList) {
		this.speciList = speciList;
	}

	public String getShowPicPath() {
		return showPicPath;
	}

	public void setShowPicPath(String showPicPath) {
		this.showPicPath = showPicPath;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public BigDecimal getBatchCash() {
		return batchCash;
	}

	public void setBatchCash(BigDecimal batchCash) {
		this.batchCash = batchCash;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	
	
}
