package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.InvoiceInOrdState;

/**
 * 发票信息-订单维度
 * 
 * @author dingmingliang
 */
@AnnonOfClass(tableName = "TB_Order_InvoiceInOrd", desc = "发票信息-订单维度")
public class InvoiceInOrd implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "订单id", primary = true, primaryIndex = 1)
	private long orderId;

	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;

	@AnnonOfField(desc = "发票抬头", notNull = false)
	private String title;
	
	@AnnonOfField(desc = "下单时间")
	private long orderTime;

	@AnnonOfField(desc = "OMS接单时间")
	private long omsTime;

	@AnnonOfField(desc = "全部订单生产结束时间")
	private long producedTime;

	@AnnonOfField(desc = "发票状态")
	private InvoiceInOrdState state;

	@AnnonOfField(desc = "扩展字段", type = "text")
	private String extInfo = "";	
	
	@AnnonOfField(desc = "修改前的地址 json格式", type = "VARCHAR(512)")
	private String modifiedValue;

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public InvoiceInOrdState getState() {
		return state;
	}

	public void setState(InvoiceInOrdState state) {
		this.state = state;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getOmsTime() {
		return omsTime;
	}

	public void setOmsTime(long omsTime) {
		this.omsTime = omsTime;
	}

	public long getProducedTime() {
		return producedTime;
	}

	public void setProducedTime(long producedTime) {
		this.producedTime = producedTime;
	}

	public String getModifiedValue() {
		return modifiedValue;
	}

	public void setModifiedValue(String modifiedValue) {
		this.modifiedValue = modifiedValue;
	}
	
	
}