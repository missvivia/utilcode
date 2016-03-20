package com.xyl.mmall.mobile.web.vo;

import java.math.BigDecimal;
import java.util.List;

import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;

public class ProductListProductVO {
	private String id;

	private String productName;

	private String brandId;

	private String brandName;

	private String colorName;

	private BigDecimal salePrice;

	private BigDecimal marketPrice;

	private String thumb;

	private List<String> listShowPicList;

	private List<ProductListSkuVO> skuList;

	private String shopName;
	
	private List<ProductPriceDTO> priceList;
	
	public String getId() {
		return id;
	}

	private int sameAsShop;

	public void setId(String id) {
		this.id = id;
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

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public List<String> getListShowPicList() {
		return listShowPicList;
	}

	public void setListShowPicList(List<String> listShowPiclist) {
		this.listShowPicList = listShowPiclist;
	}

	public List<ProductListSkuVO> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<ProductListSkuVO> skuList) {
		this.skuList = skuList;
	}

	public int getSameAsShop() {
		return sameAsShop;
	}

	public void setSameAsShop(int sameAsShop) {
		this.sameAsShop = sameAsShop;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List<ProductPriceDTO> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ProductPriceDTO> priceList) {
		this.priceList = priceList;
	}

}
