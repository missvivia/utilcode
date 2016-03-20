package com.xyl.mmall.oms.warehouse.adapter.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType (XmlAccessType.FIELD)
public class RequestInventorySku {
	@XmlElement
	private String owner_code = "";
	
	@XmlElement
	private String sku_code = "";

	@XmlElement
	private int count;

	@XmlElement
	private int junk_count;

	public String getOwner_code() {
		return owner_code;
	}

	public void setOwner_code(String owner_code) {
		this.owner_code = owner_code;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getJunk_count() {
		return junk_count;
	}

	public void setJunk_count(int junk_count) {
		this.junk_count = junk_count;
	}
	
	
}
