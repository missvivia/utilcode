package com.xyl.mmall.oms.warehouse.adapter.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType (XmlAccessType.FIELD)
public class RequestCycleResultDetail {
	private String sku_code;
	private String Inventory_type;
	private int count;
	private String reason;
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	public String getInventory_type() {
		return Inventory_type;
	}
	public void setInventory_type(String inventory_type) {
		Inventory_type = inventory_type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
