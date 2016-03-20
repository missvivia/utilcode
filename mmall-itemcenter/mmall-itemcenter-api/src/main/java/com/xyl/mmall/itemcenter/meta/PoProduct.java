package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.enums.StatusType;

/**
 * 添加到档期中的商品meta对象
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_PoProduct", desc = "档期商品表", dbCreateTimeName = "CreateTime")
public class PoProduct implements Serializable {

	private static final long serialVersionUID = 4591689352189981907L;

	/** 档期商品id */
	@AnnonOfField(desc = "档期商品id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	/** 原商品id */
	@AnnonOfField(desc = "原商品id")
	private long productId;

	/** poId */
	@AnnonOfField(desc = "poId", policy = true)
	private long poId;

	/** 最低层类目id */
	@AnnonOfField(desc = "最低层类目id")
	private long lowCategoryId;

	/** 添加的供应商id */
	@AnnonOfField(desc = "添加的供应商id")
	private long supplierId;

	/** 表明货号，用于实现不同颜色产品在详情页的切换功能 */
	@AnnonOfField(desc = "货号", type = "VARCHAR(32)")
	private String goodsNo;

	/** 商品状态 */
	@AnnonOfField(desc = "商品状态")
	private StatusType status;

	/** 审核理由 */
	@AnnonOfField(desc = "审核理由")
	private String rejectReason;

	/** 审核描述 */
	@AnnonOfField(desc = "审核描述")
	private String rejectDescp;

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
	@AnnonOfField(desc = "尺码是否自定义")
	private SizeType sizeType;

	/** 尺码模板id */
	@AnnonOfField(desc = "尺码模板id")
	private long sizeTemplateId;

	/** 尺码助手id */
	@AnnonOfField(desc = "尺码助手id")
	private long sizeAssistId;

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
	private long uTime;

	/** 商品的修改时间 */
	@AnnonOfField(desc = "提交审核时间")
	private long submitTime;

	/** 单个手动排序 */
	@AnnonOfField(desc = "单个手动排序")
	private int singleIndex;

	/** 按类目排序 */
	@AnnonOfField(desc = "按类目排序")
	private int categoryIndex;

	/** 创建时间 */
	@AnnonOfField(desc = "创建时间")
	private long cTime;

	@AnnonOfField(desc = "缩略图")
	private String showPicPath;

	/** 是否专柜同款 */
	@AnnonOfField(desc = "是否专柜同款")
	private int sameAsShop;

	/** 是否曾经上过线 */
	@AnnonOfField(desc = "是否曾经上过线")
	private int isOnline;

	/** 上线之后是否被删除 */
	@AnnonOfField(desc = "上线之后是否被删除")
	private int isDelete;
	
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

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public long getLowCategoryId() {
		return lowCategoryId;
	}

	public void setLowCategoryId(long lowCategoryId) {
		this.lowCategoryId = lowCategoryId;
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

	public long getUTime() {
		return uTime;
	}

	public void setUTime(long uTime) {
		this.uTime = uTime;
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

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public String getRejectDescp() {
		return rejectDescp;
	}

	public void setRejectDescp(String rejectDescp) {
		this.rejectDescp = rejectDescp;
	}

	public int getSingleIndex() {
		return singleIndex;
	}

	public void setSingleIndex(int singleIndex) {
		this.singleIndex = singleIndex;
	}

	public int getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(int categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(long submitTime) {
		this.submitTime = submitTime;
	}

	public long getCTime() {
		return cTime;
	}

	public void setCTime(long cTime) {
		this.cTime = cTime;
	}

	public long getuTime() {
		return uTime;
	}

	public void setuTime(long uTime) {
		this.uTime = uTime;
	}

	public long getcTime() {
		return cTime;
	}

	public void setcTime(long cTime) {
		this.cTime = cTime;
	}

	public String getShowPicPath() {
		return showPicPath;
	}

	public void setShowPicPath(String showPicPath) {
		this.showPicPath = showPicPath;
	}

	public int getSameAsShop() {
		return sameAsShop;
	}

	public void setSameAsShop(int sameAsShop) {
		this.sameAsShop = sameAsShop;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public int hashCode() {
		int elm1 = goodsNo.hashCode();
		int result = 1;
		result = 31 * result + elm1;
		int elm2 = colorNum.hashCode();
		result = 31 * result + elm2;
		int elm3 = Long.valueOf(poId).hashCode();
		result = 31 * result + elm3;

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PoProduct other = (PoProduct) obj;
		if (!goodsNo.equals(other.getGoodsNo()))
			return false;
		if (!colorNum.equals(other.getColorNum()))
			return false;
		if (poId != other.poId)
			return false;
		return true;
	}
}
