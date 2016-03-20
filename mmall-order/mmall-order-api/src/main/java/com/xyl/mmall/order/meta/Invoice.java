package com.xyl.mmall.order.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.common.meta.BaseVersion;
import com.xyl.mmall.order.enums.InvoiceInOrdState;

/**
 * 发票
 * @author author:lhp
 *
 * @version date:2015年6月5日下午1:18:16
 */
@AnnonOfClass(desc = "发票", tableName = "Mmall_Order_InvoiceInOrd", dbCreateTimeName = "CreateTime")
public class Invoice extends BaseVersion{
	
	private static final long serialVersionUID = -5316752263021432172L;

	@AnnonOfField(desc = "Id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "订单id", policy = true)
	private long orderId;

	@AnnonOfField(desc = "用户id")
	private long userId;
	
	@AnnonOfField(desc = "商家id", policy = true)
	private long businessId;
	
	@AnnonOfField(desc = "发票号", notNull = false)
	private String invoiceNo;

	@AnnonOfField(desc = "发票抬头", notNull = false)
	private String title;

	@AnnonOfField(desc = "发票状态")
	private InvoiceInOrdState state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public InvoiceInOrdState getState() {
		return state;
	}

	public void setState(InvoiceInOrdState state) {
		this.state = state;
	}
	
	

}
