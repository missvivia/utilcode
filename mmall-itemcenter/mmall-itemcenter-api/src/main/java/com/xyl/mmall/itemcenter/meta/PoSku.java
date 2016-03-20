package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.enums.StatusType;

/**
 * 添加到档期中的sku meta对象
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_PoSku", desc = "档期sku表", dbCreateTimeName = "CreateTime")
public class PoSku implements Serializable {

	private static final long serialVersionUID = 8567223542084336159L;

	/** 主键id */
	@AnnonOfField(desc = "主键id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "供应商id")
	private long supplierId;

	@AnnonOfField(desc = "poId", policy = true)
	private long poId;

	/** 货号 */
	@AnnonOfField(desc = "货号 ")
	private String goodsNo;

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

	/** 提交审核的时间 */
	@AnnonOfField(desc = "提交审核的时间")
	private long submitTime;

	/** 商品状态 */
	@AnnonOfField(desc = "商品状态")
	private StatusType status;

	/** 审核理由 */
	@AnnonOfField(desc = "审核理由")
	private String rejectReason;

	/** 提交的sku数量 */
	@AnnonOfField(desc = "提交的sku数量")
	private int skuNum;

	/** 品牌商提供的sku数量 */
	@AnnonOfField(desc = "品牌商提供的sku数量")
	private int supplierSkuNum;

	/** 创建时间 */
	@AnnonOfField(desc = "创建时间")
	private long cTime;

	/** 修改时间 */
	@AnnonOfField(desc = "修改时间")
	private long uTime;

	/** 是否曾经上过线 */
	@AnnonOfField(desc = "是否曾经上过线")
	private int isOnline;

	/** 上线之后是否被删除 */
	@AnnonOfField(desc = "上线之后是否被删除")
	private int isDelete;

	public PoSku() {
		marketPrice = new BigDecimal(0);
		salePrice = new BigDecimal(0);
		basePrice = new BigDecimal(0);
		submitTime = 0;
		status = StatusType.NOTSUBMIT;
		rejectReason = "";
		skuNum = 0;
	}

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

	public long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(long submitTime) {
		this.submitTime = submitTime;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public int getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}

	public int getSupplierSkuNum() {
		return supplierSkuNum;
	}

	public void setSupplierSkuNum(int supplierSkuNum) {
		this.supplierSkuNum = supplierSkuNum;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public long getCTime() {
		return cTime;
	}

	public void setCTime(long cTime) {
		this.cTime = cTime;
	}

	public long getUTime() {
		return uTime;
	}

	public void setUTime(long updateTime) {
		this.uTime = updateTime;
	}

	public long getcTime() {
		return cTime;
	}

	public void setcTime(long cTime) {
		this.cTime = cTime;
	}

	public long getuTime() {
		return uTime;
	}

	public void setuTime(long uTime) {
		this.uTime = uTime;
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
	
}
