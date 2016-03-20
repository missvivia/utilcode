package com.xyl.mmall.oms.dto;

import java.io.Serializable;

public class TomorrowOrderReportDTO implements Serializable {

	private static final long serialVersionUID = -121497695669553591L;

	private long time;
	
	private String expressCompany;
	
	private String warehouseName;
	
	private int totalOrderNumber;
	
	private int collectiveOrderNumber;
	
	private int cancelOrderNumber;
	
	private String totalOrderPercent;
	
	private String collectiveOrderPercent;
	
	private String cancelOrderPercent;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
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

	public String getTotalOrderPercent() {
		return totalOrderPercent;
	}

	public void setTotalOrderPercent(String totalOrderPercent) {
		this.totalOrderPercent = totalOrderPercent;
	}

	public String getCollectiveOrderPercent() {
		return collectiveOrderPercent;
	}

	public void setCollectiveOrderPercent(String collectiveOrderPercent) {
		this.collectiveOrderPercent = collectiveOrderPercent;
	}

	public String getCancelOrderPercent() {
		return cancelOrderPercent;
	}

	public void setCancelOrderPercent(String cancelOrderPercent) {
		this.cancelOrderPercent = cancelOrderPercent;
	}
	
}
