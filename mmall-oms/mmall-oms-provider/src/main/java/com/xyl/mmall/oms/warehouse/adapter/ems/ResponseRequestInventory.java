package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseRequestInventory {
	private String warehouse_code;
	private String snapshot_time;
	@XmlElementWrapper(name = "details")
	@XmlElement(name = "detail")
	private List<RequestInventorySku> skulist;
	public String getWarehouse_code() {
		return warehouse_code;
	}
	public void setWarehouse_code(String warehouse_code) {
		this.warehouse_code = warehouse_code;
	}
	public String getSnapshot_time() {
		return snapshot_time;
	}
	public void setSnapshot_time(String snapshot_time) {
		this.snapshot_time = snapshot_time;
	}
	public List<RequestInventorySku> getSkulist() {
		return skulist;
	}
	public void setSkulist(List<RequestInventorySku> skulist) {
		this.skulist = skulist;
	}
	
}
