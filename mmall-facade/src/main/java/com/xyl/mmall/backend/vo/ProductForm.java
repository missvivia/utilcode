package com.xyl.mmall.backend.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.framework.vo.SimpleIdNamePaire;
import com.xyl.mmall.framework.vo.SimpleIdValuePaire;
import com.xyl.mmall.itemcenter.enums.SizeType;

public class ProductForm {
	/** 商品id */
	private long id;

	/** 商品名 */
	private String productName;

	/** 所属品牌id */
	private long brandId;

	/** 所属品牌名 */
	private String brandName;

	/** 增值税率 */
	private BigDecimal valueAddedTax;

	/** 商品描述 */
	private String desp;

	/** 商品色号 */
	private String colorNum;

	/** 商品颜色 */
	private String colorName;

	/** 商品尺码类型 */
	private int sizeType;

	/** 商品尺码模板id */
	private long sizeTemplateId;

	/** 正品价 */
	private BigDecimal marketPrice;

	/** 销售价 */
	private BigDecimal salePrice;

	/** 供货价 */
	private BigDecimal basePrice;

	/** 用户富文本框编辑的HTML */
	private String customEditHTML;



	private List<CustSizeSimpleSKU> skuList;

	private List<TmplSizeSimpleSKU> skuList2;


	private List<String> prodShowPicList;

	private List<String> listShowPicList;

	private List<String> detailShowPicList;

	private List<SimpleIdNamePaire> sizeHeader;
	
	private List<SimpleIdValuePaire> productParamList;

	public ProductForm() {
		marketPrice = new BigDecimal(0);
		salePrice = new BigDecimal(0);
		basePrice = new BigDecimal(0);
		valueAddedTax = new BigDecimal(17);
		productName = "";
		brandName = "";
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public BigDecimal getValueAddedTax() {
		return valueAddedTax;
	}

	public void setValueAddedTax(BigDecimal valueAddedTax) {
		this.valueAddedTax = valueAddedTax;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
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

	public int getSizeType() {
		return sizeType;
	}

	public void setSizeType(int sizeType) {
		this.sizeType = sizeType;
	}

	public long getSizeTemplateId() {
		return sizeTemplateId;
	}

	public void setSizeTemplateId(long sizeTemplateId) {
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

	public String getCustomEditHTML() {
		return customEditHTML;
	}

	public void setCustomEditHTML(String customEditHTML) {
		this.customEditHTML = customEditHTML;
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

	public List<SimpleIdValuePaire> getProductParamList() {
		return productParamList;
	}

	public void setProductParamList(List<SimpleIdValuePaire> productParamList) {
		this.productParamList = productParamList;
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

	public List<SimpleIdNamePaire> getSizeHeader() {
		return sizeHeader;
	}

	public void setSizeHeader(List<SimpleIdNamePaire> sizeHeader) {
		this.sizeHeader = sizeHeader;
	}

}
