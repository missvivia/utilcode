package com.xyl.mmall.oms.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * @author zb
 * 
 */
@AnnonOfClass(desc = "oms订单明细表", tableName = "Mmall_Oms_OmsOrderFormSku")
public class OmsOrderFormSku implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "订单Id")
	private long omsOrderFormId;

	@AnnonOfField(desc = "最终零售价格[包含红包优惠](单位)")
	private BigDecimal rprice = BigDecimal.ZERO;

	@AnnonOfField(desc = "红包优惠的差额(单位)")
	private BigDecimal redSPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "总数量")
	private int totalCount;

	@AnnonOfField(desc = "原始价格/闪购价(单价)")
	private BigDecimal oriRPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "产品的供应商Id")
	private long supplierId;

	@AnnonOfField(desc = "入库站点")
	private long storeAreaId;

	@AnnonOfField(desc = "产品Id")
	private long productId;

	@AnnonOfField(desc = "skuId")
	private long skuId;

	@AnnonOfField(desc = "poId")
	private long poId;

	@AnnonOfField(desc = "userId", policy = true)
	private long userId;

	/** 所属的商品名 */
	@AnnonOfField(desc = "所属的商品名", type = "varchar(128)")
	private String productName;

	/** 所属的商品颜色 */
	@AnnonOfField(desc = "所属的商品颜色", type = "varchar(128)")
	private String colorName;

	/** 尺码 */
	@AnnonOfField(desc = "尺码", type = "varchar(128)")
	private String size;

	/** 条形码 */
	@AnnonOfField(desc = "条形码", type = "varchar(128)")
	private String barCode;

	@AnnonOfField(desc = "重量", type = "varchar(128)")
	private String weight;

	@AnnonOfField(desc = "原始的产品的供应商Id")
	private long oriSupplierId;

	public BigDecimal getRedSPrice() {
		return redSPrice;
	}

	public void setRedSPrice(BigDecimal redSPrice) {
		this.redSPrice = redSPrice;
	}

	public BigDecimal getOriRPrice() {
		return oriRPrice;
	}

	public void setOriRPrice(BigDecimal oriRPrice) {
		this.oriRPrice = oriRPrice;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOmsOrderFormId() {
		return omsOrderFormId;
	}

	public void setOmsOrderFormId(long omsOrderFormId) {
		this.omsOrderFormId = omsOrderFormId;
	}

	public BigDecimal getRprice() {
		return rprice;
	}

	public void setRprice(BigDecimal rprice) {
		this.rprice = rprice;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public long getStoreAreaId() {
		return storeAreaId;
	}

	public void setStoreAreaId(long storeAreaId) {
		this.storeAreaId = storeAreaId;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public long getOriSupplierId() {
		return oriSupplierId;
	}

	public void setOriSupplierId(long oriSupplierId) {
		this.oriSupplierId = oriSupplierId;
	}

}
