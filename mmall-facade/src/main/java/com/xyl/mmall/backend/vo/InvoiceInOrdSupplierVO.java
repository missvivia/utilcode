package com.xyl.mmall.backend.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;

/**
 * @author dingmingliang
 * 
 */
public class InvoiceInOrdSupplierVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "订单id", primary = true, primaryIndex = 0)
	private long orderId;

	@AnnonOfField(desc = "收件人-姓名", type = "VARCHAR(16)")
	private String consigneeName;

	@AnnonOfField(desc = "收件人-电话", type = "VARCHAR(32)")
	private String consigneePhone = "";

	@AnnonOfField(desc = "抬头", type = "VARCHAR(32)")
	private String title;

	/**
	 * 详细地址
	 */
	private String fullAddress;

	/**
	 * 商品
	 */
	private String goods;

	/**
	 * 下单时间
	 */
	private String orderDate;

	@AnnonOfField(desc = "快递号", type = "VARCHAR(32)")
	private String barCode;

	@AnnonOfField(desc = "快递公司", type = "VARCHAR(16)")
	private String expressCompanyName;

	@AnnonOfField(desc = "发票金额")
	private BigDecimal cash;

	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;

	@AnnonOfField(desc = "状态")
	private InvoiceInOrdSupplierState state;

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

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneePhone() {
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
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

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
