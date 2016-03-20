package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 退回的商品
 * 
 */
@AnnonOfClass(desc = "退回的商品明细", tableName = "Mmall_Oms_OmsReturnOrderFormSku")
public class OmsReturnOrderFormSku implements Serializable {

	private static final long serialVersionUID = -166308351606622131L;

	@AnnonOfField(desc = "退货Id")
	private long returnId;
	
	// part-1：用户申请退货时填入
	@AnnonOfField(desc = "订单Id(PK)", primary = true, primaryIndex = 1)
	private long orderId;

	@AnnonOfField(desc = "OrderSKuId(PK)", primary = true, primaryIndex = 2)
	private long orderSkuId;

	@AnnonOfField(desc = "与orderSkuId绑定的skuId")
	private long skuId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "申请时间")
	private long ctime;

	@AnnonOfField(desc = "退货数量")
	private int returnCount;

	@AnnonOfField(desc = "退货理由")
	private String reason = "";

	// part-2：仓库收到退货时填入
	@AnnonOfField(desc = "仓库实际收到的退货数量")
	private int confirmCount;

	@AnnonOfField(desc = "仓库退货备注信息")
	private String confirmInfo = "";

	@AnnonOfField(desc = "供应商id")
	private long supplierId;

	@AnnonOfField(desc = "poId")
	private long poId;
	
	/** 所属的商品名 */
	@AnnonOfField(desc = "所属的商品名", type = "varchar(128)")
	private String productName = "";

	/** 所属的商品颜色 */
	@AnnonOfField(desc = "所属的商品颜色", type = "varchar(128)")
	private String colorName = "";

	/** 尺码 */
	@AnnonOfField(desc = "尺码", type = "varchar(128)")
	private String size = "";

	/** 条形码 */
	@AnnonOfField(desc = "条形码")
	private String barCode = "";

	@AnnonOfField(desc = "入库站点")
	private long storeAreaId;
	
	@AnnonOfField(desc = "原始的产品的供应商Id")
	private long oriSupplierId;

	public String getReason() {
		return reason;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public void setOrderSkuId(long orderSkuId) {
		this.orderSkuId = orderSkuId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setReturnId(long returnId) {
		this.returnId = returnId;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getOrderSkuId() {
		return orderSkuId;
	}

	public long getOrderId() {
		return orderId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public long getUserId() {
		return userId;
	}

	public long getReturnId() {
		return returnId;
	}

	public int getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}

	public int getConfirmCount() {
		return confirmCount;
	}

	public void setConfirmCount(int confirmCount) {
		this.confirmCount = confirmCount;
	}

	public String getConfirmInfo() {
		return confirmInfo;
	}

	public void setConfirmInfo(String confirmInfo) {
		this.confirmInfo = confirmInfo;
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

	public long getStoreAreaId() {
		return storeAreaId;
	}

	public void setStoreAreaId(long storeAreaId) {
		this.storeAreaId = storeAreaId;
	}

	public long getOriSupplierId() {
		return oriSupplierId;
	}

	public void setOriSupplierId(long oriSupplierId) {
		this.oriSupplierId = oriSupplierId;
	}

}
