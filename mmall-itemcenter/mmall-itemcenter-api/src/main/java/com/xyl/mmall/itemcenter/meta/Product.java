package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.enums.SizeType;

/**
 * 商品 meta类
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_Product", desc = "商品表")
public class Product implements Serializable {

	private static final long serialVersionUID = -2718115736555396224L;

	/** 产品/商品id */
	@AnnonOfField(desc = "产品/商品id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	/** 最低层类目id */
	@AnnonOfField(desc = "最低层类目id")
	private long lowCategoryId;

	/** 添加的供应商id */
	@AnnonOfField(desc = "添加的供应商id", policy = true)
	private long supplierId;

	/** 表明货号，用于实现不同颜色产品在详情页的切换功能 */
	@AnnonOfField(desc = "货号", type = "VARCHAR(32)")
	private String goodsNo;

	/** 商品名称 */
	@AnnonOfField(desc = "商品名称 ", type = "VARCHAR(60)")
	private String productName;

	/** 商品品牌id */
	@AnnonOfField(desc = "商品品牌id")
	private long brandId;

	/** 商品色号 */
	@AnnonOfField(desc = "商品色号 ", type = "VARCHAR(12)")
	private String colorNum;

	/** 商品颜色 */
	@AnnonOfField(desc = "商品颜色", type = "VARCHAR(12)")
	private String colorName;

	/** 尺码是否自定义 */
	@AnnonOfField(desc = "尺码类型")
	private SizeType sizeType;

	/** 尺码模板id */
	@AnnonOfField(desc = "尺码模板id")
	private long sizeTemplateId;

	/** 尺码助手id */
	@AnnonOfField(desc = "尺码助手id")
	private long sizeAssistId;

	@AnnonOfField(desc = "是否显示尺寸图 ，位从右到左分别表示商品基本信息是否录入、尺码是否录入、图片是否录入、详情信息是否录入")
	private int infoFlag;

	/** 是否显示尺寸图 */
	@AnnonOfField(desc = "是否显示尺寸图 ")
	private boolean isShowSizePic;

	/** 正品价 */
	@AnnonOfField(desc = "正品价", defa = "0")
	private BigDecimal marketPrice;

	/** 销售价 */
	@AnnonOfField(desc = "销售价", defa = "0")
	private BigDecimal salePrice;

	/** 供货价 */
	@AnnonOfField(desc = "供货价", defa = "0")
	private BigDecimal basePrice;

	/** 商品的添加时间 */
	@AnnonOfField(desc = "商品的添加时间")
	private long addTime;

	/** 商品的修改时间 */
	@AnnonOfField(desc = "商品的修改时间")
	private long updateTime;

	@AnnonOfField(desc = "缩略图")
	private String showPicPath;
	
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

	public int getInfoFlag() {
		return infoFlag;
	}

	public void setInfoFlag(int infoFlag) {
		this.infoFlag = infoFlag;
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

	public long getSizeTemplateId() {
		return sizeTemplateId;
	}

	public SizeType getSizeType() {
		return sizeType;
	}

	public void setSizeType(SizeType sizeType) {
		this.sizeType = sizeType;
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

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getShowPicPath() {
		return showPicPath;
	}

	public void setShowPicPath(String showPicPath) {
		this.showPicPath = showPicPath;
	}

}
