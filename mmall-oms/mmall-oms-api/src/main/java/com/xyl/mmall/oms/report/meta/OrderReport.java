package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(desc = "全国订单产生概况", tableName = "Mmall_Oms_Report_OrderReport")
public class OrderReport implements Serializable {

	private static final long serialVersionUID = -6738182108248153421L;
	
	@AnnonOfField(desc = "全国订单产生概况主键id", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "时间戳")
	private long time;
	
	@AnnonOfField(desc = "产生总订单")
	private int totalOrderNumber;
	
	@AnnonOfField(desc = "汇总订单")
	private int collectiveOrderNumber;
	
	@AnnonOfField(desc = "取消订单")
	private int cancelOrderNumber;
	
	@AnnonOfField(desc = "快递公司", type = "VARCHAR(25)")
	private String expressCompany;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getTotalOrderNumber() {
		return totalOrderNumber;
	}

	public void setTotalOrderNumber(int totalOrderNumber) {
		this.totalOrderNumber = totalOrderNumber;
	}

	public int getCollectiveOrderNumber() {
		return collectiveOrderNumber;
	}

	public void setCollectiveOrderNumber(int collectiveOrderNumber) {
		this.collectiveOrderNumber = collectiveOrderNumber;
	}

	public int getCancelOrderNumber() {
		return cancelOrderNumber;
	}

	public void setCancelOrderNumber(int cancelOrderNumber) {
		this.cancelOrderNumber = cancelOrderNumber;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}
	
}
