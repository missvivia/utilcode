package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.meta.Product;

public class ProductDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4430272223600497613L;

	/** 产品/商品id */
	protected long id;

	/** 最低层类目id */
	protected long lowCategoryId;

	/** 添加的供应商id */
	protected long supplierId;

	/** 表明货号，用于实现不同颜色产品在详情页的切换功能 */
	protected String goodsNo;

	/** 商品名称 */
	protected String productName;

	/** 商品品牌id */
	protected long brandId;

	/** 商品色号 */
	protected String colorNum;

	/** 商品颜色 */
	protected String colorName;

	/** 尺码是否自定义 */
	protected SizeType sizeType;

	/** 尺码模板id */
	protected long sizeTemplateId;

	/** 尺码助手id */
	protected long sizeAssistId;

	/** 是否显示尺寸图 */
	protected boolean isShowSizePic;

	/** 正品价 */
	protected BigDecimal marketPrice;

	/** 销售价 */
	protected BigDecimal salePrice;

	/** 供货价 */
	protected BigDecimal basePrice;

	/** 商品展示图列表 */
	protected List<String> prodShowPicList;

	/** 商品列表 */
	protected List<String> listShowPicList;

	protected List<? extends BaseSkuDTO> SKUList;

	/** 类目名称 */
	protected String categoryName;

	protected String showPicPath;

	public ProductDTO(Product obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public ProductDTO() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLowCategoryId() {
		return lowCategoryId;
	}

	public void setLowCategoryId(long lowCategoryId) {
		this.lowCategoryId = lowCategoryId;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getShowPicPath() {
		return showPicPath;
	}

	public void setShowPicPath(String showPicPath) {
		this.showPicPath = showPicPath;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
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

	public SizeType getSizeType() {
		return sizeType;
	}

	public void setSizeType(SizeType sizeType) {
		this.sizeType = sizeType;
	}

	public long getSizeTemplateId() {
		return sizeTemplateId;
	}

	public void setSizeTemplateId(long sizeTemplateId) {
		this.sizeTemplateId = sizeTemplateId;
	}

	public long getSizeAssistId() {
		return sizeAssistId;
	}

	public void setSizeAssistId(long sizeAssistId) {
		this.sizeAssistId = sizeAssistId;
	}

	public boolean isShowSizePic() {
		return isShowSizePic;
	}

	public void setShowSizePic(boolean isShowSizePic) {
		this.isShowSizePic = isShowSizePic;
	}

	public boolean getIsShowSizePic() {
		return isShowSizePic;
	}

	public void setIsShowSizePic(boolean isShowSizePic) {
		this.isShowSizePic = isShowSizePic;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<? extends BaseSkuDTO> getSKUList() {
		return SKUList;
	}

	public void setSKUList(List<? extends BaseSkuDTO> sKUList) {
		SKUList = sKUList;
	}
}
