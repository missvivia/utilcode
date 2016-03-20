package com.xyl.mmall.backend.vo;

import java.util.List;

public class SizeAssistVO {
	private String id = "0";

	private long supplierId;

	private String name;

	private SizeAssistAxis haxis;

	private SizeAssistAxis vaxis;

	private List<List<?>> body;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SizeAssistAxis getHaxis() {
		return haxis;
	}

	public void setHaxis(SizeAssistAxis haxis) {
		this.haxis = haxis;
	}

	public SizeAssistAxis getVaxis() {
		return vaxis;
	}

	public void setVaxis(SizeAssistAxis vaxis) {
		this.vaxis = vaxis;
	}

	public List<List<?>> getBody() {
		return body;
	}

	public void setBody(List<List<?>> body) {
		this.body = body;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
}
