package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;

/**
 * 发票信息-供应商订单维度
 * 
 * @author dingmingliang
 */
@AnnonOfClass(tableName = "Mmall_Order_InvoiceInOrdSupplier", desc = "发票信息-供应商订单维度")
public class InvoiceInOrdSupplier implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "订单id", primary = true, primaryIndex = 1)
	private long orderId;

	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;

	@AnnonOfField(desc = "供应商id", primary = true, primaryIndex = 2)
	private long supplierId;

	@AnnonOfField(desc = "快递号", type = "VARCHAR(32)")
	private String barCode = "";

	@AnnonOfField(desc = "快递公司", type = "VARCHAR(16)")
	private String expressCompanyName = "";

	@AnnonOfField(desc = "创建时间")
	private long ctime;

	@AnnonOfField(desc = "下单时间")
	private long orderTime;

	@AnnonOfField(desc = "发票金额")
	private BigDecimal cash;

	@AnnonOfField(desc = "状态")
	private InvoiceInOrdSupplierState state;

	@AnnonOfField(desc = "sku快照(InvoiceSkuSPDTO的JSON数据)", type = "text")
	private String skuSnapshots;

	@AnnonOfField(desc = "发票抬头")
	private String title;

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSkuSnapshots() {
		return skuSnapshots;
	}

	public void setSkuSnapshots(String skuSnapshots) {
		this.skuSnapshots = skuSnapshots;
	}

	public InvoiceInOrdSupplierState getState() {
		return state;
	}

	public void setState(InvoiceInOrdSupplierState state) {
		this.state = state;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getExpressCompanyName() {
		return expressCompanyName;
	}

	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}
}
