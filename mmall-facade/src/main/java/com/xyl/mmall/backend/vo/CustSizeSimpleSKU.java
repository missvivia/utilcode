package com.xyl.mmall.backend.vo;

import java.util.List;

public class CustSizeSimpleSKU {
	/** 条形码 */
	private String barCode;

	/** 自定义模板值 */
	private List<String> body;

	private long id;
	
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public List<String> getBody() {
		return body;
	}

	public void setBody(List<String> body) {
		this.body = body;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
